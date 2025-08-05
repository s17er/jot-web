package com.gourmetcaree.admin.pc.webdata.action.webdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListViewDto;
import com.gourmetcaree.admin.pc.webdata.form.webdata.ListForm;
import com.gourmetcaree.admin.service.logic.WebDataCsvLogic;
import com.gourmetcaree.admin.service.property.WebListCsvProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.TerminalKbn;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.InterestService;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.db.common.service.WebAccessCounterService;
import com.gourmetcaree.db.common.service.WebAccessCounterService.AccessCountDto;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.common.service.WebShopListService;
import com.gourmetcaree.db.webdata.dto.webdata.VWebListDto;
/**
*
* WEBデータ一覧を表示するクラス
* @author Makoto Otani
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** 一括処理可能な件数 */
	private static final int LUMP_LIMIT = 50;

	/** フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** WEB一覧ビューサービス */
	@Resource
	protected WebListService webListService;

	/** WEBリストCSVロジック */
	@Resource
	private WebDataCsvLogic webDataCsvLogic;

	/** アクセスカウントサービス */
	@Resource
	private WebAccessCounterService webAccessCounterService;

	/** IP電話履歴サービス */
	@Resource
	private WebIpPhoneHistoryService webIpPhoneHistoryService;

	@Resource
	private ApplicationService applicationService;

	/** 気になるサービス */
	@Resource
	private InterestService interestService;


	/** プレ応募サービス */
	@Resource
	private PreApplicationService preApplicationService;

	/** WEBデータ系列店舗サービス */
	@Resource
	private WebShopListService webShopListService;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 一覧表示用リスト */
	public List<ListDto> list;

	/** カウント側を表示するフラグ */
	private boolean displayCountFlg = false;

	/** 詳細画面から一覧に戻った時のアンカー */
	public int anchor;

	/** 一覧表示用DTO。※今後はここの中に集約 */
	public ListViewDto listViewDto;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_LIST_INDEX")
	public String index() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		return show();
	}

	/**
	 * 検索
	 * @return 一覧画面
	 */
	@Execute(validator = true, validate = "validate", reset = "resetSearch", input = TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_LIST_SEARCH")
	public String search() {


		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = convertShowList(doSearch(GourmetCareeConstants.DEFAULT_PAGE));

		log.debug("検索結果表示");

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		// 一覧画面のパス
		return TransitionConstants.Webdata.JSP_APC01L01;

	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return WEBデータ一覧のパス
	 */
	@Execute(validator = false, reset ="resetId", urlPattern = "changePage/{pageNum}", input=TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_LIST_CHANGEPAGE")
	public String changePage() {

		// ページ番号を指定。ページ番号が取得できない場合は初期値で検索
		int targetPage = NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE);

		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = convertShowList(doSearch(targetPage));

		return TransitionConstants.Webdata.JSP_APC01L01;
	}


	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return WEBデータ一覧のパス
	 */
	@Execute(validator = false, reset ="resetId", urlPattern = "changeCountPage/{pageNum}", input=TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_LIST_CHANGECOUNTPAGE")
	public String changeCountPage() {

		// ページ番号を指定。ページ番号が取得できない場合は初期値で検索
		int targetPage = NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE);

		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = convertShowList(doSearch(targetPage));
		setDisplayCountFlg(true);

		return TransitionConstants.Webdata.JSP_APC01L01;
	}


	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return WEBデータ一覧のパス
	 */
	@Execute(validator = false, reset ="resetId", input=TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_LIST_CHANGEMAXROW")
	public String changeMaxRow() {



		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = convertShowList(doSearch(GourmetCareeConstants.DEFAULT_PAGE));

		// 一覧画面のパス
		return TransitionConstants.Webdata.JSP_APC01L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return WEBデータ一覧のパス
	 */
	@Execute(validator=false, reset ="resetId", input=TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_LIST_SEARCHAGAIN")
	public String searchAgain() {
		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute("anchor") != null) {
			anchor = (int)session.getAttribute("anchor");
		}

		int targetPage = NumberUtils.toInt(listForm.pageNum,
				GourmetCareeConstants.DEFAULT_PAGE);

		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = convertShowList(doSearch(targetPage));
		// 一覧画面のパス
		return TransitionConstants.Webdata.JSP_APC01L01;
	}



	/**
	 * 一括処理画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return WEBデータ一覧のパス
	 */
	@Execute(validator=false, input=TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_LIST_BACKSEARCH")
	public String backSearch() {

		int targetPage = NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE);


		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = convertShowList(doSearch(targetPage));

		// 一覧画面のパス
		return TransitionConstants.Webdata.JSP_APC01L01;
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		// 自身の会社IDをセット
		if(StringUtil.isEmpty(listForm.companyId)) {
			listForm.companyId = userDto.companyId;
		}
		// 自身の営業担当者IDをセット
		if(StringUtil.isEmpty(listForm.salesId)) {
			listForm.salesId = userDto.userId;
		}

		// 一覧画面のパス
		return TransitionConstants.Webdata.JSP_APC01L01;
	}

	/**
	 * WEBデータ一覧ビューからデータを取得する
	 * @param targetPage 表示対象ページ
	 * @return WEBデータ一覧リスト
	 */
	private SqlSelect<VWebList> doSearch(int targetPage) {

		pageNavi = new PageNavigateHelper(getMaxRow());

		listForm.convertWebAreaKbnToForeignAreaKbn();

		try{
			VWebListDto dto = createSearchCondition();
			// データの取得
			return webListService.getWebdataListAutoSelectA(dto, pageNavi, targetPage);

		// 検索結果が無い場合のエラー
		} catch (WNoResultException e) {

			// 画面表示をしない
			listForm.setExistDataFlgNg();

			// 件数が0件の場合はエラーメッセージを返す「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}


	}

	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC01L01)
	public String output() {
		try {
			VWebListDto dto = createSearchCondition();
			webDataCsvLogic.outputWebListCsv(createWebListCsvProperty(dto));
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return null;
	}

	private WebListCsvProperty createWebListCsvProperty(VWebListDto dto) {
		WebListCsvProperty property = new WebListCsvProperty();
		property.pass = getCommonProperty("gc.csv.filepass");
		property.encode = getCommonProperty("gc.csv.encoding");
		property.fileName = getCommonProperty("gc.webdataList.csv.filename");
		property.vWebListDto = dto;

		return property;

	}


    /**
     * 検索条件のDTOを作成する。
     * 検索とCSV出力のSQL検索に使用
     * XXX プレビューにも同条件の検索があるが、メソッドの切り分け方などが違いリファクタに時間がかかるため別のままにしている。
     * @return WEBデータ検索条件DTO
     * @throws WNoResultException IP電話番号の条件が入っていて、それが見つからなかった場合にスロー。
     *                            これがスローされた場合はデータ検索は行わなくて良い。
     */
	private VWebListDto createSearchCondition() throws WNoResultException {
		// エンティティのコピー（空白以外、日付のフォーマット）
		VWebListDto dto = new VWebListDto();
		Beans.copy(listForm, dto).excludesWhitespace()
				.dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "postFromDate", "postToDate")
				.execute();

		// ユーザが代理店の場合、必ず所属する会社になるようIDをセット
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
			dto.companyId = Integer.parseInt(userDto.companyId);
		}

		dto.webIpPhoneIdList = webIpPhoneService.selectIdListFromIpPhoneNumber(listForm.ipPhone);

		return dto;
	}
	/**
	 * 表示用リストを生成
	 * @param webListSelect WEBデータカーソル
	 * @return WEBデータ一覧Dtoをセットしたリスト
	 */
	private List<ListDto> convertShowList(SqlSelect<VWebList> webListSelect) {

		List<MArea> areaList;
		try {
			areaList = areaService.getMAreaList(NumberUtils.toInt(listForm.areaCd));
		} catch (WNoResultException e1) {
			return new ArrayList<>();
		}

		List<String> ignoreIpPhoneList = readIgnoreIpPhoneList();
		final Map<Integer, Integer> ipPhoneHistoryCountMap = webIpPhoneHistoryService.getHistoryCountMap(ignoreIpPhoneList);

		List<ListDto> retList = webListSelect.iterate(new IterationCallback<VWebList, List<ListDto>>() {
			private List<ListDto> dtoList = new ArrayList<>();
			@Override
			public List<ListDto> iterate(VWebList entity, IterationContext context) {
				if (entity == null) {
					return dtoList;
				}
				ListDto dto = new ListDto();
				Beans.copy(entity, dto).execute();

				// 詳細画面のパスをセット
				dto.detailPath = GourmetCareeUtil.makePath(TransitionConstants.Webdata.ACTION_WEBDATA_DETAIL_INDEX, String.valueOf(dto.id));

				int[] serialKbnArray = webAttributeService.getWebAttributeValueArray(entity.id, MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD);
				if (!ArrayUtils.isEmpty(serialKbnArray)) {
					dto.serialPublicationKbn = String.valueOf(serialKbnArray[0]);
				}

				// 店舗一覧の数
				dto.shopListCount = shopListService.countByCustomerId(entity.customerId);
				dto.shopListJobOfferCount = shopListService.countByCustomerIdAndJobOffer(entity.customerId);
				dto.shopListPublicationCount = webShopListService.countByWebId(entity.id);
				dto.applicationCount = applicationService.countByWebId(entity.id);
				dto.preApplicationCount = preApplicationService.getPreApplicationCount(entity.id);
				// 管理者のみカウント表示
				if (ManageAuthLevel.ADMIN.value().equals(userDto.getAuthLevel())
						|| ManageAuthLevel.STAFF.value().equals(userDto.getAuthLevel())
						|| ManageAuthLevel.OTHER.value().equals(userDto.getAuthLevel())
						|| ManageAuthLevel.SALES.value().equals(userDto.getAuthLevel())) {
					// PV件数
					List<AccessCountDto> countDtoList = webAccessCounterService.getAllAccessCount(entity.id, entity.areaCd);
					for (AccessCountDto countDto : countDtoList) {
						switch (Integer.parseInt(countDto.terminalKbn)) {
							case TerminalKbn.PC_VALUE:
								dto.pvPcCount = countDto.accessCount;
								break;
							case TerminalKbn.SMART_VALUE:
								dto.pvSmartPhoneCount = countDto.accessCount;
								break;
							case TerminalKbn.MOBILE_VALUE:
								dto.pvMbCount = countDto.accessCount;
								break;
							default:
								break;
						}
					}
					// IP電話応募件数
					if(ipPhoneHistoryCountMap != null && ipPhoneHistoryCountMap.containsKey(entity.id)) {
						dto.ipPhoneHistoryCount = ipPhoneHistoryCountMap.get(entity.id);
					}
				}

				// プレビューURL
				String previewKey = DigestUtils.md5Hex(dto.id + GourmetCareeConstants.SYSTEM_HASH_SOLT);
				StringBuilder previewUrl = new StringBuilder();
				previewUrl.append(getCommonProperty("gc.sslDomain"));
				previewUrl.append(String.format(getCommonProperty("gc.preview.url.webdata.list"), areaList.get(0).linkName));
				previewUrl.append(String.format("?ids=%s&key=%s&info=1", dto.id, previewKey));
				dto.previewUrl = previewUrl.toString();

				dtoList.add(dto);
				return dtoList;

			}
		});

		if (CollectionUtils.isEmpty(retList)) {
			listForm.setExistDataFlgNg();

			// 件数が0件の場合はエラーメッセージを返す「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// プレビュー用パラメータを作る
		List<String> webIdList = new ArrayList<>();
		for (ListDto ret : retList) {
			webIdList.add(ret.id);
		}

		listViewDto = new ListViewDto();
		listViewDto.webIds = String.join(",", webIdList);
		listViewDto.previewKey = DigestUtils.md5Hex(listViewDto.webIds + GourmetCareeConstants.SYSTEM_HASH_SOLT);

		StringBuilder previewUrl = new StringBuilder();
		previewUrl.append(getCommonProperty("gc.sslDomain"));
		previewUrl.append(String.format(getCommonProperty("gc.preview.url.webdata.list"), areaList.get(0).linkName));
		previewUrl.append(String.format("?ids=%s&key=%s&info=1", listViewDto.webIds, listViewDto.previewKey));
		listViewDto.previewUrl = previewUrl.toString();

		return retList;
	}


	/**
	 * 最大表示件数の取得
	 */
	private int getMaxRow() {
		try {
			return Integer.parseInt(listForm.maxRow);
		} catch (NumberFormatException e) {
			listForm.maxRow = getCommonProperty("gc.webdata.initMaxRow");
			return NumberUtils.toInt(listForm.maxRow, GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
		}
	}

	/**
	 * 一括コピー
	 */
	@Execute(validator = false, reset = "resetId", validate = "lumpValidate", input=TransitionConstants.Webdata.FWD_WEBDATA_LIST_BACKSEARCH)
	@MethodAccess(accessCode="WEBDATA_LIST_LUMPCOPY")
	public String lumpCopy() {

		// 一括コピー可能データかチェック
		checkEnableCopy(listForm.webId);

		// 一括処理可能な件数の制御
		int lumpLimit = NumberUtils.toInt(getCommonProperty("gc.webdata.lumpLimit"), LUMP_LIMIT);
		// 一括処理件数以内の場合のみ処理可能にする
		if (lumpLimit < listForm.webId.length) {
			//「{一括処理コピー}は{50}件以内で行ってください。」
			throw new ActionMessagesException("errors.doLimitProcessing",
					MessageResourcesUtil.getMessage("msg.app.lumpCopy"),
					String.valueOf(lumpLimit));
		}

		session.setAttribute("webId", listForm.webId);
		session.setAttribute("areaCd", listForm.areaCd);

		return TransitionConstants.Webdata.REDIRECT_LUMPCOPY_INPUT_INDEX;
	}

	/**
	 * 一括確定
	 * @return
	 */
	@Execute(validator = false, reset = "resetId", validate = "lumpValidate", input=TransitionConstants.Webdata.FWD_WEBDATA_LIST_BACKSEARCH)
	@MethodAccess(accessCode="WEBDATA_LIST_LUMPDECIDE")
	public String lumpDecide() {

		// 一括確定可能データ可チェック
		if (!checkEnableDecide(listForm.webId)) {
			return TransitionConstants.Webdata.FWD_WEBDATA_LIST_BACKSEARCH;
		}

		session.setAttribute("webId", listForm.webId);
		session.setAttribute("areaCd", listForm.areaCd);

		return TransitionConstants.Webdata.REDIRECT_LUMPDECIDE_EDIT_INDEX;
	}

	/**
	 * 一括削除
	 * @return
	 */
	@Execute(validator = false, reset = "resetId", validate = "lumpValidate", input=TransitionConstants.Webdata.FWD_WEBDATA_LIST_BACKSEARCH)
	@MethodAccess(accessCode="WEBDATA_LIST_LUMPDELETE")
	public String lumpDelete() {

		// 一括削除可能データチェック
		if (!checkEnableDelete(listForm.webId)) {
			return TransitionConstants.Webdata.FWD_WEBDATA_LIST_BACKSEARCH;
		}

		session.setAttribute("webId", listForm.webId);

		return TransitionConstants.Webdata.REDIRECT_LUMPDELETE_DELETE_INDEX;
	}

	/**
	 * プレビュー(一覧画面用)を表示します。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC01L01)
	public String previewSearch() {
		return TransitionConstants.Preview.REDIRECT_SEARCH_PREVIEW;
	}


	public boolean isDisplayCountFlg() {
		return displayCountFlg;
	}

	public void setDisplayCountFlg(boolean displayCountFlg) {
		this.displayCountFlg = displayCountFlg;
	}
}

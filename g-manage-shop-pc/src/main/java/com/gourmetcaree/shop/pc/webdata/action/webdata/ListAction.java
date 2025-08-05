package com.gourmetcaree.shop.pc.webdata.action.webdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.AreaService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService.ResultDto;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService.SearchProperty;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.logic.dto.WebdataDto;
import com.gourmetcaree.shop.logic.logic.WebdataLogic;
import com.gourmetcaree.shop.logic.property.WebdataProperty;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;
import com.gourmetcaree.shop.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.shop.pc.webdata.form.webdata.ListForm;

/**
 * 求人原稿一覧を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class ListAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 求人原稿一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** WEBデータロジック */
	@Resource
	protected WebdataLogic webdataLogic;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	@Resource
	private AreaService areaService;

	/** 求人データ */
	public TWeb tWeb;

	/** 電話応募履歴 */
	public List<ResultDto> dtoList;

	/** 電話応募履歴サービス */
	@Resource
	private WebIpPhoneHistoryService WebIpPhoneHistoryService;

	/** Webサービス */
	@Resource
	private WebService webService;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 新着順 */
	private static final String RECENT_ORDER = "1";

	/** PV数順 */
	private static final String PV_ORDER = "2";

	/** 応募数順 */
	private static final String APP_ORDER = "3";

	/** プレ応募数順 */
	private static final String PRE_ORDER = "4";

	/** 電話応募数順 */
	private static final String PHONE_ORDER = "5";

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Webdata.JSP_SPD01L01)
	public String index() {
		return show();
	}

	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_SPD01L01)
	public String showList() {

		pageNavi = new PageNavigateHelper(Integer.parseInt(listForm.where_displayCount));
		listForm.maxRow = listForm.where_displayCount;

		// 初期ページでDBからデータを取得し、画面表示データを作成
		listForm.list = createDisplayValue(getData(GourmetCareeConstants.DEFAULT_PAGE));

		listForm.setExistDataFlgOk();
		checkUnReadMail();

		return TransitionConstants.Webdata.JSP_SPD01L01;
	}

	/**
	 * 検索
	 */
	@Execute(urlPattern = "search", reset = "resetForm", input = TransitionConstants.Webdata.JSP_SPD01L01)
	public String search() {

		pageNavi = new PageNavigateHelper(Integer.parseInt(listForm.where_displayCount));
		listForm.maxRow = listForm.where_displayCount;

		// 初期ページでDBからデータを取得し、画面表示データを作成
		listForm.list = createDisplayValue(getData(GourmetCareeConstants.DEFAULT_PAGE));

		log.debug("求人原稿一覧を取得しました。");

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("求人原稿一覧を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// 表示フラグを表示
		listForm.setExistDataFlgOk();
		checkUnReadMail();

		return TransitionConstants.Webdata.JSP_SPD01L01;
	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return 求人原稿一覧画面JSPのパス
	 */
	@Execute(urlPattern = "changePage/{pageNum}", input=TransitionConstants.Webdata.JSP_SPD01L01)
	public String changePage() {

		// ページ番号を指定。ページ番号が取得できない場合は初期値で検索
		int targetPage = NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE);

		if(StringUtils.isNotBlank(listForm.where_displayCount)) {
			pageNavi = new PageNavigateHelper(Integer.parseInt(listForm.where_displayCount));
		} else {
			pageNavi = new PageNavigateHelper(getInitMaxRow());
		}

		// DBからデータを取得し、画面表示データを作成
		listForm.list = createDisplayValue(getData(targetPage));
		checkUnReadMail();

		// 一覧画面
		return TransitionConstants.Webdata.JSP_SPD01L01;
	}



	/**
	 * 電話応募者一覧画面に遷移する
	 * @return
	 */
	@Execute(validator = false, urlPattern = "focusPhoneApplication/{webId}", reset ="resetWebdata", input = TransitionConstants.Webdata.JSP_SPD01L01)
	public String focusPhoneApplication() {
		int webId = Integer.parseInt(listForm.webId);

		try {
			tWeb = webService.findById(webId);

			if(userDto.customerId != (int)tWeb.customerId) {
				throw new PageNotFoundException();
			}
			MVolume volume = volumeService.findById(tWeb.volumeId);

			listForm.postStartDatetime = volume.postStartDatetime;
			listForm.postEndDatetime = volume.postEndDatetime;
		}catch (SNoResultException e) {
			throw new PageNotFoundException();
		}


		SearchProperty property = listForm.createSearchProperty(webId);
		try {
			dtoList = new ArrayList<WebIpPhoneHistoryService.ResultDto>();
			WebIpPhoneHistoryService.select(property, new IterationCallback<WebIpPhoneHistoryService.ResultDto, Void>() {
				@Override
				public Void iterate(ResultDto entity,
						IterationContext context) {

					if (entity == null) {
						return null;
					}

					entity.telTime = GourmetCareeUtil.convertSecondsToMinuteSecondStr(entity.memberTelSecond);

					// 電話番号変換
					entity.memberTel = convertIgnoreMembertel(entity.memberTel);

					// 通話ステータスがエラーの場合は表示を変更する
					entity.telStatusName = convertTelStatusName(entity.telStatusName);

					dtoList.add(entity);
					return null;
				}

			});
		} catch (WNoResultException e) {
			listForm.existDataFlg = false;
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		checkUnReadMail();

		return TransitionConstants.Webdata.JSP_SPD02L01;
	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return 求人原稿一覧画面JSPのパス
	 */
	@Execute(validator = false, urlPattern = "focusPhoneApplicationOrder", input=TransitionConstants.Webdata.JSP_SPD01L01)
	public String focusPhoneApplicationOrder() {

		int webId = Integer.parseInt(listForm.webId);

		try {
			tWeb = webService.findById(webId);

			if(userDto.customerId != (int)tWeb.customerId) {
				throw new PageNotFoundException();
			}
			MVolume volume = volumeService.findById(tWeb.volumeId);

			listForm.postStartDatetime = volume.postStartDatetime;
			listForm.postEndDatetime = volume.postEndDatetime;
		}catch (SNoResultException e) {
			throw new PageNotFoundException();
		}


		SearchProperty property = listForm.createSearchProperty(webId);
		try {
			dtoList = new ArrayList<WebIpPhoneHistoryService.ResultDto>();
			WebIpPhoneHistoryService.select(property, new IterationCallback<WebIpPhoneHistoryService.ResultDto, Void>() {
				@Override
				public Void iterate(ResultDto entity,
						IterationContext context) {

					if (entity == null) {
						return null;
					}

					entity.telTime = GourmetCareeUtil.convertSecondsToMinuteSecondStr(entity.memberTelSecond);

					// 電話番号変換
					entity.memberTel = convertIgnoreMembertel(entity.memberTel);

					// 通話ステータスがエラーの場合は表示を変更する
					entity.telStatusName = convertTelStatusName(entity.telStatusName);

					dtoList.add(entity);
					return null;
				}

			});
		} catch (WNoResultException e) {
			listForm.existDataFlg = false;
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		checkUnReadMail();

		return TransitionConstants.Webdata.JSP_SPD02L01;
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		listForm.where_displayCount = "20";
		//ページナビゲータを初期化
		pageNavi = new PageNavigateHelper(Integer.parseInt(listForm.where_displayCount));
		listForm.maxRow = listForm.where_displayCount;

		// 初期ページでDBからデータを取得し、画面表示データを作成
		listForm.list = createDisplayValue(getData(GourmetCareeConstants.DEFAULT_PAGE));

		log.debug("求人原稿一覧を取得しました。");

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("求人原稿一覧を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// 表示フラグを表示
		listForm.setExistDataFlgOk();
		checkUnReadMail();

		// 一覧画面
		return TransitionConstants.Webdata.JSP_SPD01L01;

	}

	/**
	 * 一覧データの取得
	 * @param targetPage 表示ページ
	 * @return WEBデータ一覧エンティティ
	 */
	private List<VWebList> getData(int targetPage) {

		Map<String,String[]> parameterMap = new HashMap<>();

		if(StringUtils.isNotBlank(listForm.where_displayCount)) {
			parameterMap.put("where_displayCount", new String[] {listForm.where_displayCount});
		}
		if(listForm.where_webStatus != null) {
			parameterMap.put("where_webStatus", listForm.where_webStatus);
		}
		if(listForm.where_areaCd != null) {
			parameterMap.put("where_areaCd", listForm.where_areaCd);
		}
		if(listForm.where_sizeKbn != null) {
			parameterMap.put("where_sizeKbn", listForm.where_sizeKbn);
		}

		try {
			// データの取得
			List<VWebList> entityList = webdataLogic.getWebdataList(pageNavi, targetPage, parameterMap);

			// リストが空の場合はエラーメッセージを返す
			if (entityList == null || entityList.isEmpty()) {

				// 画面表示をしない
				listForm.setExistDataFlgNg();

				//「{求人原稿}は現在登録されていません。」
				throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.webdata"));
			}

		// 取得したリストを返却
		return entityList;

		// 検索結果が無い場合のエラー
		} catch (WNoResultException e) {

			// 画面表示をしない
			listForm.setExistDataFlgNg();

			//「{求人原稿}は現在登録されていません。」
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.webdata"));
		}
	}

	/**
	 * 表示用リストを生成
	 * @param entityList WEBデータ一覧リスト
	 * @return WEBデータ一覧Dtoをセットしたリスト
	 */
	private List<ListDto> createDisplayValue(List<VWebList> entityList) {

		// 変換のため、プロパティにセット
		WebdataProperty property = new WebdataProperty();
		property.vWebListList = entityList;

		// 画面表示の共通項目を変換
		List<WebdataDto> webDatadtolist = webdataLogic.createDisplayList(property);

		// 画面Dtoを保持するリスト
		List<ListDto> dtoList = new ArrayList<ListDto>();

		// Dtoにコピー
		for (WebdataDto webdataDto : webDatadtolist) {

			ListDto dto = new ListDto();
			Beans.copy(webdataDto, dto).execute();

			// 応募画面のパスをセット
			dto.applicationPath = GourmetCareeUtil.makePath(TransitionConstants.Application.ACTION_APPLICATION_MAIL_LIST_FOCUSWEB, String.valueOf(webdataDto.id));
			dto.phoneApplicationPath = GourmetCareeUtil.makePath(TransitionConstants.Webdata.FOCUS_PHONE_APPLICATION_LIST, String.valueOf(webdataDto.id));
			dto.preApplicationPath = GourmetCareeUtil.makePath(TransitionConstants.Application.ACTION_PRE_APPLICATION_MAIL_LIST_FOCUSWEB, String.valueOf(webdataDto.id));
			String previewKey = DigestUtils.md5Hex(dto.id + GourmetCareeConstants.SYSTEM_HASH_SOLT);

			List<MArea> areaList;
			try {
				areaList = areaService.getMAreaList(dto.areaCd);
			} catch (WNoResultException e1) {
				continue;
			}

			StringBuilder previewUrl = new StringBuilder();
			previewUrl.append(getCommonProperty("gc.sslDomain"));
			previewUrl.append(String.format(getCommonProperty("gc.preview.url.webdata.list"), areaList.get(0).linkName));
			previewUrl.append(String.format("?ids=%s&key=%s", dto.id, previewKey));
			dto.listPreviewUrl = previewUrl.toString();
			dto.MArea = areaList.get(0);

			dtoList.add(dto);
		}

		if(StringUtils.isNotBlank(listForm.where_displayOrder)) {
			dtoList = sortListDto(dtoList, listForm.where_displayOrder);
		} else {
			dtoList = sortListDto(dtoList, RECENT_ORDER);
		}

		// Dtoリストを返却
		return dtoList;
	}

	/**
	 * 検索結果を表示順で入れ替える
	 * @param dtoList
	 * @param showOrder
	 * @return 入れ替え後の検索結果
	 */
	private List<ListDto> sortListDto(List<ListDto> dtoList, String showOrder){
		List<ListDto> result = dtoList;

//		新着順（掲載開始日時の新しい順）でソート
		if(showOrder.equals(RECENT_ORDER)) {
			Collections.sort(
					result,
	                  new Comparator<ListDto>() {
	                    @Override
	                    public int compare(ListDto obj1, ListDto obj2){
	                      return obj2.postStartDatetime.compareTo(obj1.postStartDatetime);
	                       }
	                    }
	                  );
		}

//		PV数でソート
		if(showOrder.equals(PV_ORDER)) {
			Collections.sort(
					result,
	                  new Comparator<ListDto>() {
	                    @Override
	                    public int compare(ListDto obj1, ListDto obj2){
	                      return obj2.allAccessCount - obj1.allAccessCount;
	                       }
	                    }
	                  );
		}

//		応募数でソート
		if(showOrder.equals(APP_ORDER)) {
			Collections.sort(
					result,
	                  new Comparator<ListDto>() {
	                    @Override
	                    public int compare(ListDto obj1, ListDto obj2){
	                      return obj2.applicationCount - obj1.applicationCount;
	                       }
	                    }
	                  );
		}

//		プレ応募数でソート
		if(showOrder.equals(PRE_ORDER)) {
			Collections.sort(
					result,
	                  new Comparator<ListDto>() {
	                    @Override
	                    public int compare(ListDto obj1, ListDto obj2){
	                      return obj2.preApplicationCount - obj1.preApplicationCount;
	                       }
	                    }
	                  );
		}

//		電話応募数でソート
		if(showOrder.equals(PHONE_ORDER)) {
			Collections.sort(
					result,
	                  new Comparator<ListDto>() {
	                    @Override
	                    public int compare(ListDto obj1, ListDto obj2){
	                      return obj2.phoneApplicationCount - obj1.phoneApplicationCount;
	                       }
	                    }
	                  );
		}

		return result;
	}

	/**
	 * 電話番号が無効な場合、ジェイオフィス東京に変換する
	 * @param memberTel 変換対象
	 * @return 無効番号の場合はジェイオフィス東京、そうでない場合はそのまま返す
	 */
	private String convertIgnoreMembertel(String memberTel) {
		return StringUtils.isNotEmpty(memberTel) ? memberTel : "ジェイオフィス東京";
	}

	/**
	 * 通話ステータスが"エラー"の場合は"不通状態"に変換する
	 * @param telStatusName 変換対象
	 * @return エラーの場合は、不通状態、そうでない場合はそのまま返す
	 */
	private String convertTelStatusName(String telStatusName) {
		// 通話ステータスがエラーの場合は表示を変更する
		return "エラー".equals(telStatusName) ? "不通状態" : telStatusName;
	}

	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.webdata.initMaxRow"),
				GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.webDataInstance();
	}

}

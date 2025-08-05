package com.gourmetcaree.admin.pc.preview.action.listPreview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm.ImgMethodKbn;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.PreviewLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.webdata.dto.webdata.VWebListDto;


/**
 * 求人情報一覧を表示するクラス
 * @author Takahiro Ando
 * @version 1.0
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends PreviewBaseAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	@Resource
	private PreviewLogic previewLogic;

	/**
	 * リストでのプレビューを表示します。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "showListPreview/{webId}", reset = "resetForm")
	public String showListPreview() {

		PreviewDto sessionPreviewDto;
		try {
			sessionPreviewDto = previewLogic.createPreviewDtoByWebId(NumberUtils.toInt(listForm.webId));
		} catch (WNoResultException e) {
			throw new FraudulentProcessException("プレビュー表示用のデータのセッションからの取得に失敗しました。");
		}



		//ログインユーザがアクセス可能かチェックする。
		checkAgencyAccess(sessionPreviewDto.companyId);
		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;


		List<PreviewDto> dataList = new ArrayList<PreviewDto>();
		dataList.add(sessionPreviewDto);
		listForm.dataList = dataList;
		listForm.areaCd = sessionPreviewDto.areaCd;
		listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + sessionPreviewDto.areaCd);
		if (GourmetCareeUtil.eqInt(sessionPreviewDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			listForm.cssLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
			listForm.imgLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
			listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
			listForm.incLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_INC);
			listForm.helpLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
		}
		listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));


		return TransitionConstants.Preview.JSP_APO02A01;
	}


	@Execute(validator = false, urlPattern = "showInputPreview/{inputFormKbn}")
	public String showInputPreview() {
		PreviewDto dto = createPreviewDtoFromInputForm(listForm.inputFormKbn);
		checkAgencyAccess(dto.companyId);
		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_SESSION;
		previewLogic.createVolumeData(dto);

		previewLogic.createCustomerData(dto);
		previewLogic.createIpPhoneData(dto);

		List<PreviewDto> dataList = new ArrayList<PreviewDto>();
		dataList.add(dto);
		listForm.dataList = dataList;
		listForm.areaCd = dto.areaCd;
		listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + dto.areaCd);
		if (GourmetCareeUtil.eqInt(dto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			listForm.cssLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
			listForm.imgLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
			listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
			listForm.incLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_INC);
			listForm.helpLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
		}

		listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));
		return TransitionConstants.Preview.JSP_APO02A01;
	}

	@Execute(validator = false, urlPattern = "showMobileInputPreview/{inputFormKbn}")
	public String showMobileInputPreview() {
		PreviewDto dto = createPreviewDtoFromInputForm(listForm.inputFormKbn);
		checkAgencyAccess(dto.companyId);
		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_SESSION;

		List<PreviewDto> dataList = new ArrayList<PreviewDto>();
		dataList.add(dto);
		listForm.dataList = dataList;
		listForm.areaCd = dto.areaCd;
		listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + dto.areaCd);
		if (GourmetCareeUtil.eqInt(dto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
		}
		listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));
		listForm.SITE_COLOR = getCommonProperty("gc.siteColor" + dto.areaCd);
		listForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + dto.areaCd);
		listForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + dto.areaCd);
		listForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + dto.areaCd);
		return TransitionConstants.Preview.JSP_FMB01L01;
	}




	/**
	 * リストでのプレビューを表示します。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "showMobileListPreview/{webId}", reset = "resetForm")
	public String showMobileListPreview() {

		PreviewDto sessionPreviewDto;
		try {
			sessionPreviewDto = previewLogic.createPreviewDtoByWebId(NumberUtils.toInt(listForm.webId));
		} catch (WNoResultException e) {
			throw new FraudulentProcessException("プレビュー表示用のデータのセッションからの取得に失敗しました。");
		}

		//ログインユーザがアクセス可能かチェックする。
		checkAgencyAccess(sessionPreviewDto.companyId);
		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;

		List<PreviewDto> dataList = new ArrayList<PreviewDto>();
		dataList.add(sessionPreviewDto);
		listForm.dataList = dataList;
		listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + sessionPreviewDto.areaCd);
		if (GourmetCareeUtil.eqInt(sessionPreviewDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
		}
		listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));
		listForm.SITE_COLOR = getCommonProperty("gc.siteColor" + sessionPreviewDto.areaCd);
		listForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + sessionPreviewDto.areaCd);
		listForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + sessionPreviewDto.areaCd);
		listForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + sessionPreviewDto.areaCd);
		return TransitionConstants.Preview.JSP_FMB01L01;
	}


	/**
	 * スマートフォンリストでのプレビューを表示します。
	 * @return 一覧プレビュー画面
	 * @author Makoto Otani
	 */
	@Execute(validator = false, urlPattern = "showSmartPhoneListPreview/{webId}", reset = "resetForm")
	public String showSmartPhoneListPreview() {

		PreviewDto sessionPreviewDto;
		try {
			sessionPreviewDto = previewLogic.createPreviewDtoByWebId(NumberUtils.toInt(listForm.webId));
		} catch (WNoResultException e) {
			throw new FraudulentProcessException("プレビュー表示用のデータのセッションからの取得に失敗しました。");
		}

		//ログインユーザがアクセス可能かチェックする。
		checkAgencyAccess(sessionPreviewDto.companyId);
		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;

		// 画面表示するメイン1が登録されているか保持する
		int[] checkSize = {SizeKbn.A, SizeKbn.B, SizeKbn.C};
		if (Arrays.binarySearch(checkSize, sessionPreviewDto.sizeKbn) >= 0) {
			sessionPreviewDto.materialSearchListExistFlg = materialService.isMaterialEntityExist(sessionPreviewDto.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.MAIN_1));
		} else if (sessionPreviewDto.sizeKbn == SizeKbn.D || sessionPreviewDto.sizeKbn == SizeKbn.E) {
			sessionPreviewDto.materialSearchListExistFlg = materialService.isMaterialEntityExist(sessionPreviewDto.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.FREE));
		} else {
			sessionPreviewDto.materialSearchListExistFlg = false;
		}

		List<PreviewDto> dataList = new ArrayList<PreviewDto>();
		dataList.add(sessionPreviewDto);
		listForm.dataList = dataList;
		listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + sessionPreviewDto.areaCd);
		if (GourmetCareeUtil.eqInt(sessionPreviewDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			listForm.cssLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
			listForm.imgLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
			listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
			listForm.incLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_INC);
			listForm.helpLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
		}
		listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));
		listForm.SITE_COLOR = getCommonProperty("gc.siteColor" + sessionPreviewDto.areaCd);
		listForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + sessionPreviewDto.areaCd);
		listForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + sessionPreviewDto.areaCd);
		listForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + sessionPreviewDto.areaCd);
		// 一覧プレビュー画面へ
		return TransitionConstants.Preview.JSP_FSO01L01;
	}

	/**
	 * スマホの登録・編集画面からのプレビュー呼び出し
	 * @return プレビュー画面
	 * @author Makoto Otani
	 */
	@Execute(validator = false, urlPattern = "showSmartPhoneInputPreview/{inputFormKbn}")
	public String showSmartPhoneInputPreview() {

		PreviewDto dto = createPreviewDtoFromInputForm(listForm.inputFormKbn);
		checkAgencyAccess(dto.companyId);
		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_SESSION;

		try {
			// 画面表示するメイン1が登録されているか保持する
			int[] checkSize = {SizeKbn.A, SizeKbn.B, SizeKbn.C};
			if (Arrays.binarySearch(checkSize, dto.sizeKbn) >= 0) {

				if (dto.imageCheckMap != null && dto.imageCheckMap.containsKey(MaterialKbn.MAIN_1)) {
					dto.materialSearchListExistFlg = dto.imageCheckMap.get(MaterialKbn.MAIN_1);
				}
			} else if (dto.sizeKbn == SizeKbn.D || dto.sizeKbn == SizeKbn.E) {
				dto.materialSearchListExistFlg = dto.imageCheckMap.get(MaterialKbn.FREE);
			} else {
				dto.materialSearchListExistFlg = false;
			}
		} catch (NullPointerException e) {
			// 何もしない
		}

		List<PreviewDto> dataList = new ArrayList<PreviewDto>();
		dataList.add(dto);
		listForm.dataList = dataList;
		listForm.areaCd = dto.areaCd;
		listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + dto.areaCd);
		if (GourmetCareeUtil.eqInt(dto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			listForm.cssLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
			listForm.imgLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
			listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
			listForm.incLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_INC);
			listForm.helpLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
		}
		listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));
		listForm.SITE_COLOR = getCommonProperty("gc.siteColor" + dto.areaCd);
		listForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + dto.areaCd);
		listForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + dto.areaCd);
		listForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + dto.areaCd);

		return TransitionConstants.Preview.JSP_FSO01L01;
	}


	/**
	 * 代理店の場合、データにアクセス可能かチェックする<br />
	 * アクセス権限が無い場合、権限エラーへ遷移
	 * @param companyId 会社ID
	 */
	private void checkAgencyAccess(int companyId) {

		// 代理店の場合は自分自身の会社のみ閲覧可とする。
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel) &&
				!Integer.toString(companyId).equals(userDto.companyId)) {

			throw new AuthorizationException("アクセスする権限がありません");
		}
	}

	/**
	 * プレビューの初期表示
	 */
	@Execute(validator = false, input=TransitionConstants.Preview.JSP_APO02A01)
	public String previewSearch() {

		//ページナビゲータを初期化
		listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));

		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;
		doSearch(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Preview.JSP_APO02A01;
	}


	/**
	 * 最大件数をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数
	 */
	private String getInitMaxRow() {
		return  StringUtils.defaultString(getCommonProperty("gc.preview.list.maxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW);
	}

	/**
	 * WEBデータ一覧ビューからデータを取得する
	 * @param targetPage 表示対象ページ
	 * @return WEBデータ一覧リスト
	 */
	private void doSearch(int targetPage) {

		if (listForm.pageNavi == null) {
			//ページナビゲータを初期化
			listForm.pageNavi = new PageNavigateHelper(Integer.parseInt(getInitMaxRow()));
		}

		VWebListDto vWebListDto = createVWebListDtoFromWebDataSearchForm();
		listForm.areaCd = vWebListDto.areaCd;
		listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + vWebListDto.areaCd);

		if (GourmetCareeUtil.eqInt(vWebListDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA )) {
			listForm.cssLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
			listForm.imgLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
			listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
			listForm.incLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_INC);
			listForm.helpLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
		}

		try {
			listForm.dataList = previewLogic.createWebDataSearchPreviewDtoList(vWebListDto, listForm.pageNavi, targetPage);
		} catch (WNoResultException e) {
			noWebdataError();
		}



	}


	/**
	 * webデータが存在しないエラーを出力します。
	 */
	private void noWebdataError() {
		// 画面表示をしない
		listForm.setExistDataFlgNg();
		listForm.dataList = new ArrayList<PreviewDto>();

		// 件数が0件の場合はエラーメッセージを返す「該当するデータが見つかりませんでした。」
		throw new ActionMessagesException("errors.app.dataNotFound");
	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return WEBデータ一覧のパス
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input=TransitionConstants.Preview.JSP_APO02A01)
	public String changePage() {

		// ページ番号を指定。ページ番号が取得できない場合は初期値で検索
		int targetPage = NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE);
		listForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;

		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		doSearch(targetPage);

		return TransitionConstants.Preview.JSP_APO02A01;
	}


	/**
	 * WEBデータ検索フォームから、VWebListDtoを取得します。
	 */
	private VWebListDto createVWebListDtoFromWebDataSearchForm() {
		com.gourmetcaree.admin.pc.webdata.form.webdata.ListForm searchForm =
				(com.gourmetcaree.admin.pc.webdata.form.webdata.ListForm)
					SingletonS2ContainerFactory.getContainer().getComponent(com.gourmetcaree.admin.pc.webdata.form.webdata.ListForm.class);

		VWebListDto dto = new VWebListDto();
		Beans.copy(searchForm, dto)
							.dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "postFromDate", "postToDate")
							.execute();

		// ユーザが代理店の場合、必ず所属する会社になるようIDをセット
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
			dto.companyId = Integer.parseInt(userDto.companyId);
		}

		return dto;
	}



}
package com.gourmetcaree.shop.pc.preview.action.detailPreview;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MaterialNoDataService;
import com.gourmetcaree.db.common.service.MaterialService;
import com.gourmetcaree.db.common.service.ReleaseWebService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.logic.logic.WebdataLogic;
import com.gourmetcaree.shop.pc.preview.form.detailPreview.DetailForm;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * 求人情報詳細を表示するクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class MobileDraftDetailAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(MobileDraftDetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 公開可能なWebデータView用サービス */
	@Resource
	protected ReleaseWebService releaseWebService;

	/** 素材サービス */
	@Resource
	protected MaterialService materialService;

	/** 号数サービス */
	@Resource
	protected VolumeService volumeService;


	/** Webデータ属性サービス */
	@Resource
	protected WebAttributeService webAttributeService;

	/** 名称変換サービス */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** Webデータロジック */
	@Resource
	private WebdataLogic webdataLogic;

	/** データ本体のない素材Viewサービス */
	@Resource
	protected MaterialNoDataService materialNoDataService;

	/** Webデータサービス */
	@Resource
	private WebService webService;

//	/** メールからのプレビュー時の閲覧可能年数 */
//	private static final int DRAFT_PREVIEW_MINUS_MONTH = -1;

//	/**
//	 * 代理店の場合、データにアクセス可能かチェックする<br />
//	 * アクセス権限が無い場合、権限エラーへ遷移
//	 * @param String companyId
//	 * @param form WEBデータフォーム
//	 */
//	private void checkAgencyAccess(int companyId) {
//
//		// 代理店の場合は自分自身の会社のみ閲覧可とする。
//		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel) &&
//				!Integer.toString(companyId).equals(userDto.companyId)) {
//
//			throw new AuthorizationException("アクセスする権限がありません");
//		}
//	}

	/**
	 * 初期表示
	 * @return 求人情報詳細画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 詳細画面のパス
	 */
	private String show() {

		createDisplayData();

//		//「この画面を見ている人はこんな求人も見ています」データ取得
//		detailForm.anotherChoiceList = webdataLogic.getAnotherCheckWebdata(
//															toInt(detailForm.id),
//															detailForm.getIndustryKbnList());

		// 詳細画面のパス
		return TransitionConstants.Preview.JSP_FMB01R01;
	}

	/**
	 * 顧客側の原稿一覧からのプレビュー時の権限チェックを行います。
	 * @param entityTWeb
	 */
	private void checkReleaseAccess(TWeb entityTWeb) {

		/** アクセスコードをチェック */
		if (!detailForm.accessCd.equals(entityTWeb.accessCd)) {
			log.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。エンティティのアクセスコード：" + entityTWeb.accessCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("メールからのプレビュー時にアクセスコードが一致しませんでした。営業ID：%s, 顧客ID：%s, エンティティのアクセスコード：%s", userDto.masqueradeUserId, userDto.customerId, entityTWeb.accessCd));
			}

			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

		//エリアはアクセスされたメソッドで見分けて判定
		if (!eqInt(NumberUtils.toInt(detailForm.areaCd), entityTWeb.areaCd)) {
			log.warn("メールからのプレビュー時にエリアが一致しませんでした。エンティティのエリアコード：" + entityTWeb.areaCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("メールからのプレビュー時にエリアが一致しませんでした。営業ID：%s, 顧客ID：%s, エンティティのエリアコード：%s", userDto.masqueradeUserId, userDto.customerId, entityTWeb.areaCd));
			}
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

//		Date todayDate = new Date();
//
//		if (entityTWeb.updateDatetime != null) {
//			if (entityTWeb.updateDatetime.before(DateUtils.addMonths(todayDate, DRAFT_PREVIEW_MINUS_MONTH))) {
//				log.warn("メールからのプレビュー時に更新日時が範囲外でした。更新日時：" + entityTWeb.updateDatetime);
//				detailForm.setExistDataFlgNg();
//				throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
//			}
//		} else if (entityTWeb.insertDatetime != null) {
//			if (entityTWeb.insertDatetime.before(DateUtils.addMonths(todayDate, DRAFT_PREVIEW_MINUS_MONTH))) {
//				log.warn("メールからのプレビュー時に登録日時が範囲外でした。登録日時：" + entityTWeb.insertDatetime);
//				detailForm.setExistDataFlgNg();
//				throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
//			}
//		}
	}

	/**
	 * 表示データを作成します。
	 * 表示データが取得できない場合はPageNotFoundExceptionをスローします。
	 */
	private void createDisplayData() {

		detailForm.setExistDataFlgNg();

		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.accessCd, detailForm.areaCd);
		detailForm.previewMethodKbn = PreviewMethodKbn.DRAFT_PREVIEW;

		try {
			TWeb entityTWeb = webService.findById(NumberUtils.toInt(detailForm.id));

			checkReleaseAccess(entityTWeb);

			Beans.copy(entityTWeb, detailForm).execute();
			detailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + detailForm.areaCd);
			if (GourmetCareeUtil.eqInt(entityTWeb.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
				detailForm.cssLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_CSS);
				detailForm.imgLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMG);
				detailForm.imagesLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMAGES);
				detailForm.incLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_INC);
				detailForm.helpLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_HELP);
			}
			detailForm.SITE_COLOR = getCommonProperty("gc.siteColor" + detailForm.areaCd);
			detailForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + detailForm.areaCd);
			detailForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + detailForm.areaCd);
			detailForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + detailForm.areaCd);

			//周辺テーブルのデータを取得
			String[] treatmentKbnArray = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.TreatmentKbn.TYPE_CD));
			detailForm.treatmentKbnList =  Arrays.asList(treatmentKbnArray);

			String[] employPtnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.EmployPtnKbn.TYPE_CD));
			detailForm.employPtnList = Arrays.asList(employPtnList);

			detailForm.imageUniqueKeyMap = webdataLogic.getImageUniqueKeyMap(entityTWeb.id);

			createMaterialData(entityTWeb.id);

			if (entityTWeb.volumeId != null) {
				try {
					MVolume volumeEntity = volumeService.findById(entityTWeb.volumeId);
					detailForm.postStartDatetime = volumeEntity.postStartDatetime;
					detailForm.postEndDatetime = volumeEntity.postEndDatetime;
				} catch (SNoResultException e) {
					//未処理
				}
			}

			// 地図を表示するためエンコーディング
			try {
				if (StringUtil.isNotBlank(detailForm.mapAddress)) {
					detailForm.mapAddress = URLEncoder.encode(detailForm.mapAddress, "UTF-8");
				} else {
					detailForm.mapAddress = "";
				}

			} catch (UnsupportedEncodingException e) {
				detailForm.mapAddress = "";
			}

			//SEO用の情報を作成
			createSeoCondition();

			detailForm.setExistDataFlgOk();

		} catch (NumberFormatException e) {
			throw new PageNotFoundException();
		} catch (SNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * 画像データが存在するかどうかをMapに保持します。
	 */
	private void createMaterialData(int webId) {

		Map<String, String> imageUniqueKeyMap = new HashMap<String, String>();
		Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();

		try {
			List<VMaterialNoData> retList = materialNoDataService.getMaterialList(webId);

			for (VMaterialNoData entity : retList) {
				//動画かサムネイル以外の画像の場合に情報を保持する
				if (MaterialKbn.MOVIE_WM.equals(Integer.toString(entity.materialKbn))) {
					detailForm.wmMovieName = entity.fileName;
				} else if (MaterialKbn.MOVIE_QT.equals(Integer.toString(entity.materialKbn))) {
					detailForm.qtMovieName = entity.fileName;
				} else if (!MaterialKbn.isThumbKbn(Integer.toString(entity.materialKbn))) {
					imageCheckMap.put(Integer.toString(entity.materialKbn), true);
					imageUniqueKeyMap.put(Integer.toString(entity.materialKbn), createUniqueKey(entity.insertDatetime));
				}
			}

		} catch (WNoResultException e) {
			//素材データがない場合は未処理とする。
		}

		detailForm.imageUniqueKeyMap = imageUniqueKeyMap;
		detailForm.imageCheckMap = imageCheckMap;
	}

	/**
	 * SEO用の文言をラベルに変換。
	 */
	private void createSeoCondition() {

		Integer[] integerIndustryList = ArrayUtils.toObject(detailForm.getIndustryKbnList());

		List<String> list = new ArrayList<String>();
		list.add(detailForm.manuscriptName);

		if (!ArrayUtil.isEmpty(integerIndustryList)) {
			list.add(valueToNameConvertLogic.convertToIndustryName(ValueToNameConvertLogic.DELIMITER, integerIndustryList));
		}

		StringBuilder sb = new StringBuilder("");

		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				if (i != 0) {
					sb.append(", ");
				}
				sb.append(list.get(i));
			}
		}

		detailForm.seoH1 = sb.toString().replaceAll(", ", " ");
		detailForm.seoDescription = sb.toString().replaceAll(", ", " ");
		detailForm.seoTitle = sb.toString().replaceAll(", ", " ");
		detailForm.seoKeywords = "," + sb.toString().replaceAll(", ", ",");
	}

	/**
	 * 応募ページの表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayRecruit/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayRecruit() {
		createDisplayData();

//		//「この画面を見ている人はこんな求人も見ています」データ取得
//		detailForm.anotherChoiceList = webdataLogic.getAnotherCheckWebdata(
//														toInt(detailForm.id),
//														detailForm.getIndustryKbnList());

		return TransitionConstants.Preview.JSP_FMB01R01;
	}

//	/**
//	 * 待遇ページの表示
//	 * @return メッセージ画面
//	 */
//	@Execute(validator = false, urlPattern = "displayTreatment/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
//	public String displayTreatment() {
//		createDisplayData();
//		return TransitionConstants.Preview.JSP_FMB01R01;
//	}

	/**
	 * 詳細ページ２の表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayDetail2/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayDetail2() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R02;
	}

	/**
	 * 詳細ページ3の表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayDetail3/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayDetail3() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R03;
	}

	/**
	 * 詳細ページ4の表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayDetail4/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayDetail4() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R04;
	}
//
//	/**
//	 * 応募ページの表示
//	 * @return メッセージ画面
//	 */
//	@Execute(validator = false, urlPattern = "displayApplication/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
//	public String displayApplication() {
//		createDisplayData();
//		return TransitionConstants.Preview.JSP_FMB01R01;
//	}

	/**
	 * 会社データの表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayCompany/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayCompany() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R05;
	}

//	/**
//	 * メッセージ表示
//	 * @return メッセージ画面
//	 */
//	@Execute(validator = false, urlPattern = "displayMessage/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
//	public String displayMessage() {
//
//		createDisplayData();
//
//		//サイズF以外では表示不可
//		if (detailForm.sizeKbn != SizeKbn.F) {
//			throw new PageNotFoundException();
//		}
//
//		return TransitionConstants.Preview.JSP_FMB01R02;
//	}

	/**
	 * 地図表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "displayMap/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayMap() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R06;
	}

	/**
	 * 動画表示
	 * @return 動画画面
	 */
	@Execute(validator = false, urlPattern = "displayMovie/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayMovie() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R04;
	}

	/**
	 * 地図の拡大表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "zoomInMap/{id}/{accessCd}/{areaCd}/{zoom}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String zoomInMap() {

		// 入力値のチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id, String.valueOf(detailForm.zoom));

		detailForm.zoomMap(1);
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R06;
	}

	/**
	 * 地図の縮小表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "zoomOutMap/{id}/{accessCd}/{areaCd}/{zoom}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String zoomOutMap() {

		// 入力値のチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id, detailForm.zoom);

		detailForm.zoomMap(-1);
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R06;
	}


}
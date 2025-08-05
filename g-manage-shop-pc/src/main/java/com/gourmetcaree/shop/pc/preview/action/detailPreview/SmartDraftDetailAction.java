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
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.logic.logic.WebdataLogic;
import com.gourmetcaree.shop.pc.preview.form.detailPreview.DetailForm;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

public class SmartDraftDetailAction extends PcShopAction {

	private static final Logger log = Logger.getLogger(SmartDraftDetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** アクションフォーム */
	@ActionForm
	@Resource
	private DetailForm detailForm;


	/** webサービス */
	@Resource
	private WebService webService;

	/** WEB属性サービス */
	@Resource
	private WebAttributeService webAttributeService;

	/** WEBデータロジック */
	@Resource
	private WebdataLogic webdataLogic;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** データなしの素材サービス */
	@Resource
	private MaterialNoDataService materialNoDataService;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** 店舗一覧サービス*/
	@Resource
	private ShopListService shopListService;


	/**
	 * 初期表示
	 * @return 求人情報詳細画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FSB01R01)
	public String index() {
		return show();
	}


	/**
	 * 初期表示遷移用
	 * @return 詳細画面のパス
	 */
	private String show() {

		createDisplayData();


		// TODO スマホ用に変更 詳細画面のパス
		return TransitionConstants.Preview.JSP_FSB01R01;
	}


	/**
	 * 詳細メッセージを表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayDetailMsg/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_FSB01R01)
	public String displayDetailMsg() {
		createDisplayData();

		if (detailForm.isShopMessageExist()) {
			return TransitionConstants.Preview.JSP_FSB01R02;
		}

		return TransitionConstants.Preview.JSP_FSB01R01;
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
			detailForm.siteAreaCd = detailForm.areaCd;
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

			detailForm.recruitmentIndustry = valueToNameConvertLogic.convertToIndustryName(" / ", createIndustryArray(entityTWeb));

			//周辺テーブルのデータを取得
			String[] treatmentKbnArray = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.TreatmentKbn.TYPE_CD));
			detailForm.treatmentKbnList =  Arrays.asList(treatmentKbnArray);

			String[] employPtnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.EmployPtnKbn.TYPE_CD));
			detailForm.employPtnList = Arrays.asList(employPtnList);

			detailForm.imageUniqueKeyMap = webdataLogic.getImageUniqueKeyMap(entityTWeb.id);

			detailForm.otherConditionKbnList = convertIntArrayToStringList(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.OtherConditionKbn.TYPE_CD));

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

			// TODO SEO用の情報を作成
//			createSeoCondition();

			if (entityTWeb.customerId != null && GourmetCareeUtil.eqInt(MTypeConstants.ShopListDisplayKbn.ARI, entityTWeb.shopListDisplayKbn)) {
				detailForm.shopListDisplayFlg = shopListService.existsShopListByCustomerId(entityTWeb.customerId);
			} else {
				detailForm.shopListDisplayFlg = false;
			}

			detailForm.setExistDataFlgOk();

		} catch (NumberFormatException e) {
			throw new PageNotFoundException();
		} catch (SNoResultException e) {
			throw new PageNotFoundException();
		}
	}



	private void createMaterialData(Integer webId) {
		Map<String, String> imageUniqueKeyMap = new HashMap<String, String>();
		Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();

		try {
			List<VMaterialNoData> retList = materialNoDataService.getMaterialList(webId);

			for (VMaterialNoData entity : retList) {
				imageCheckMap.put(Integer.toString(entity.materialKbn), true);
				imageUniqueKeyMap.put(Integer.toString(entity.materialKbn), createUniqueKey(entity.insertDatetime));
			}

		} catch (WNoResultException e) {
			//素材データがない場合は未処理とする。
		}

		detailForm.imageUniqueKeyMap = imageUniqueKeyMap;
		detailForm.imageCheckMap = imageCheckMap;
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

	}

	/**
	 * 業種配列の取得
	 * @param web
	 * @return
	 */
	private String[] createIndustryArray(TWeb web) {
		List<String> list = new ArrayList<String>();
		list.add(String.valueOf(web.industryKbn1));

		if (web.industryKbn2 != null) {
			list.add(String.valueOf(web.industryKbn2));
		}

		if (web.industryKbn3 != null) {
			list.add(String.valueOf(web.industryKbn3));
		}

		return list.toArray(new String[0]);
	}

	private static List<String> convertIntArrayToStringList(int[] intArray) {
		List<String> list = new ArrayList<String>();
		if (ArrayUtils.isEmpty(intArray)) {
			return list;
		}
		for (int num : intArray) {
			list.add(String.valueOf(num));
		}
		return list;
	}


	/**
	 * JSP側で毎回生成せずにここでやる
	 * @return メッセージ詳細ページパス
	 */
	public String getMessageDetailPath() {
		return String.format("/detailPreview/smartDraftDetail/displayDetailMsg/%s/%s/%s/",
				detailForm.id, detailForm.accessCd, detailForm.areaCd);
	}

	/**
	 * JSP側で毎回生成せずにここでやる
	 * @return 求人詳細ページパス
	 */
	public String getJobDetailPath() {
		return String.format("/detailPreview/smartDraftDetail/index/%s/%s/%s/",
				detailForm.id, detailForm.accessCd, detailForm.areaCd);
	}
}

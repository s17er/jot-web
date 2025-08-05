package com.gourmetcaree.admin.pc.preview.action.shopListPreview;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.preview.action.listPreview.PreviewBaseAction;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm.ImgMethodKbn;
import com.gourmetcaree.admin.pc.preview.form.shopListPreview.SmartDetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.logic.ShopListLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ShopListMaterialKbn;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ReleaseWebService;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListRouteService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebService;

/**
 * 店舗詳細（スマホプレビュー）
 * @author Yamane
 *
 */
public class SmartDetailAction extends PreviewBaseAction {

	/** フォーム */
	@ActionForm
	@Resource
	private SmartDetailForm smartDetailForm;

	@Resource
	protected ShopListLogic shopListLogic;

	@Resource
	protected ShopListService shopListService;

	@Resource
	protected ReleaseWebService releaseWebService;

	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	/** Webデータサービス */
	@Resource
	private WebService webService;

	/** Webデータ属性サービス */
	@Resource
	private WebAttributeService webAttributeService;

	@Resource
	private ShopListRouteService shopListRouteService;


	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	@Execute(validator = false, urlPattern = "{shopListId}/{id}")
	public String index() {
		return show();
	}


	@Execute(validator = false, urlPattern = "indexSession/{shopListId}/{inputFormKbn}")
	public String indexSession() {
		createWebDataFromSession();
		createShopListData();
		return TransitionConstants.ShopListForSmart.JSP_FSD01R01;
	}

	/**
	 * WEBデータと店舗詳細を取得する
	 * @return
	 */
	private String show() {

		try {

			createDisplayData();

			createShopListData();


		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return TransitionConstants.ShopListForSmart.JSP_FSD01R01;
	}

	/**
	 * 代理店の場合、データにアクセス可能かチェックする<br />
	 * アクセス権限が無い場合、権限エラーへ遷移
	 * @param String companyId
	 * @param form WEBデータフォーム
	 */
	private void checkAgencyAccess(int companyId) {

		// 代理店の場合は自分自身の会社のみ閲覧可とする。
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel) &&
				!Integer.toString(companyId).equals(userDto.companyId)) {

			throw new AuthorizationException("アクセスする権限がありません");
		}
	}

	/**
	 * 表示データを作成します。
	 * 表示データが取得できない場合はPageNotFoundExceptionをスローします。
	 */
	private void createDisplayData() {

		checkArgsNull(NO_BLANK_FLG_NG, smartDetailForm.id);

		smartDetailForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;

		try {
			TWeb entityTWeb = webService.findById(NumberUtils.toInt(smartDetailForm.id));

			smartDetailForm.web = entityTWeb;
			checkAgencyAccess(entityTWeb.companyId);

			Beans.copy(entityTWeb, smartDetailForm).execute();
			//周辺テーブルのデータを取得
			String[] treatmentKbnArray = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.TreatmentKbn.TYPE_CD));
			smartDetailForm.treatmentKbnList =  Arrays.asList(treatmentKbnArray);
			if (GourmetCareeUtil.eqInt(entityTWeb.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
				smartDetailForm.cssLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_CSS);
				smartDetailForm.imgLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMG);
				smartDetailForm.imagesLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMAGES);
				smartDetailForm.incLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_INC);
				smartDetailForm.helpLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_HELP);
			}

			String[] employPtnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.EmployPtnKbn.TYPE_CD));
			String[] otherConditionKbnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.OtherConditionKbn.TYPE_CD));
			smartDetailForm.employPtnList = Arrays.asList(employPtnList);
			smartDetailForm.otherConditionKbnList = Arrays.asList(otherConditionKbnList);

			createMaterialData();

			//号数情報を取得
			if (entityTWeb.volumeId != null) {
				try {
					MVolume volumeEntity = volumeService.findById(entityTWeb.volumeId);
					smartDetailForm.postStartDatetime = volumeEntity.postStartDatetime;
					smartDetailForm.postEndDatetime = volumeEntity.postEndDatetime;
				} catch (SNoResultException e) {
					//volumeIdが未セットの場合は取得しない。
				}
			}

			if (StringUtils.isNotBlank(smartDetailForm.id)) {
				try {
					if (entityTWeb.customerId != null
							&& GourmetCareeUtil.eqInt(MTypeConstants.ShopListDisplayKbn.ARI, entityTWeb.shopListDisplayKbn)) {
						smartDetailForm.firstShopListId = shopListService.getShopListIdByCustomerId(entityTWeb.customerId);
						smartDetailForm.shopListDisplayFlg = true;
					} else {
						smartDetailForm.shopListDisplayFlg = false;
					}
				} catch (WNoResultException e) {
					smartDetailForm.shopListDisplayFlg = false;
				}
			} else {
				smartDetailForm.shopListDisplayFlg = false;
			}


			smartDetailForm.backPath = "/shopListPreview/smartList/index/".concat(smartDetailForm.id);
			smartDetailForm.webDetailPath = "/detailPreview/smartPhoneDbDetail/index/".concat(smartDetailForm.id);

			//SEO用の情報を作成
//			createSeoCondition();

		} catch (NumberFormatException e) {
			throw new PageNotFoundException();
		} catch (SNoResultException e) {
			throw new PageNotFoundException();
		}
	}


	/**
	 * SESSIONから原稿データを作成
	 */
	private void createWebDataFromSession() {
		PreviewDto prevDto = super.createPreviewDtoFromInputForm(smartDetailForm.inputFormKbn);
		Beans.copy(prevDto, smartDetailForm).execute();
		smartDetailForm.backPath = "/shopListPreview/smartList/sessionIndex/".concat(smartDetailForm.inputFormKbn);
		smartDetailForm.webDetailPath = "/detailPreview/smartPhoneDetail/index/0/".concat(smartDetailForm.inputFormKbn);

		if (GourmetCareeUtil.eqInt(prevDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			smartDetailForm.cssLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_CSS);
			smartDetailForm.imgLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMG);
			smartDetailForm.imagesLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMAGES);
			smartDetailForm.incLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_INC);
			smartDetailForm.helpLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_HELP);
		}

		if(StringUtils.isNotBlank(prevDto.volumeId)) {
			try {
				MVolume vol = volumeService.findById(NumberUtils.toInt(prevDto.volumeId));
				smartDetailForm.postStartDatetime = vol.postStartDatetime;

				smartDetailForm.postEndDatetime = vol.postEndDatetime;
			} catch (SNoResultException e) {
				// 何もしない
			}
		}
		createMaterialData();
	}


	/**
	 * 店舗一覧の情報を作成します。
	 */
	private void createShopListData() {
		try {
			TShopList entity = shopListService.getResisteredData(Integer.parseInt(smartDetailForm.shopListId));

			Beans.copy(entity, smartDetailForm).excludes("id").execute();
			smartDetailForm.shopListIndustryKbn1 = entity.industryKbn1;
			if (entity.industryKbn2 != null) {
				smartDetailForm.shopListIndustryKbn2 = entity.industryKbn2;
			}

			// アルバイト募集
			if (GourmetCareeUtil.eqInt(MTypeConstants.JobOfferFlg.ARI, entity.jobOfferFlg)) {
				smartDetailForm.arbeitPreviewPath =  String.format(
						getCommonProperty("gc.arbeitPreview.url.format"),
						MArbeitConstants.ArbeitSite.getArbeitSiteConst(entity.arbeitTodouhukenId),
						entity.id,
						entity.accessKey);
			}

			smartDetailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + smartDetailForm.areaCd);

			// 路線取得
			smartDetailForm.routeList = shopListRouteService.findByShopListId(Integer.parseInt(smartDetailForm.shopListId));

		} catch (NumberFormatException e) {
			throw new FraudulentProcessException();
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}


	/**
	 * 画像データを作成
	 */
	private void createMaterialData() {
		createMaterialData(NumberUtils.toInt(smartDetailForm.shopListId));
	}

	/**
	 * 画像データを作成
	 */
	private void createMaterialData(int shopListId) {
		try {
			VShopListMaterialNoData entity = shopListMaterialNoDataService.getMaterialEntity(NumberUtils.toInt(smartDetailForm.shopListId), NumberUtils.toInt(ShopListMaterialKbn.MAIN_1));
			smartDetailForm.imagePath = String.format("/util/imageUtility/displayShopListImageCache/%d/%s/%s", entity.shopListId, ShopListMaterialKbn.MAIN_1, GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
		} catch (WNoResultException e) {
			smartDetailForm.imagePath = null;
		}
	}



}

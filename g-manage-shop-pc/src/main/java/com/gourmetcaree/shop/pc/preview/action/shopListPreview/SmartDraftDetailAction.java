package com.gourmetcaree.shop.pc.preview.action.shopListPreview;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ShopListMaterialKbn;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListRouteService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.pc.preview.form.shopListPreview.SmartDetailForm;
import com.gourmetcaree.shop.pc.shop.action.shop.ShopBaseAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * スマホ用ドラフト詳細アクション
 * @author Takehiro Nakamori
 *
 */
public class SmartDraftDetailAction extends ShopBaseAction {

	private static final Logger log = Logger.getLogger(SmartDraftDetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** アクションフォーム */
	@ActionForm
	@Resource
	private  SmartDetailForm smartDetailForm;

	/** WEBデータサービス */
	@Resource
	private WebService webService;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;
	/** 店舗一覧用素材データなし素材サービス */
	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	@Resource
	private ShopListRouteService shopListRouteService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "{id}/{shopListId}/{accessCd}/{areaCd}", input = TransitionConstants.Preview.JSP_FSB02R01)
	public String index() {
		smartDetailForm.siteAreaCd = smartDetailForm.areaCd;
		return show();
	}


	/**
	 * 初期表示作成
	 * @return
	 */
	private String show() {
		createWebdata();
		createShopListData();

		smartDetailForm.backPath = String.format("/shopListPreview/smartDraftList/index/%s/%s/%s", smartDetailForm.id, smartDetailForm.accessCd, smartDetailForm.siteAreaCd);
		smartDetailForm.webDetailPath = String.format("/detailPreview/smartDraftDetail/index/%s/%s/%s", smartDetailForm.id, smartDetailForm.accessCd, smartDetailForm.siteAreaCd);
		return TransitionConstants.Preview.JSP_FSB02R01;
	}



	/**
	 * WEBデータ作成
	 */
	private void createWebdata() {
		TWeb web;
		try {
			web = webService.findByIdAndAreaCd(NumberUtils.toInt(smartDetailForm.id), NumberUtils.toInt(smartDetailForm.areaCd));
		} catch (SNoResultException e) {
			throw new PageNotFoundException("WEBデータが見つかりません", e);
		} catch (WNoResultException e) {
			throw new PageNotFoundException("WEBデータが見つかりません", e);
		}
		Beans.copy(web, smartDetailForm).execute();
		createVolume(web.volumeId);
		smartDetailForm.web = web;

		/** アクセスコードをチェック */
		if (!smartDetailForm.accessCd.equals(web.accessCd)) {
			log.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。エンティティのアクセスコード：" + web.accessCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("求人原稿一覧を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。エンティティのアクセスコード：" + web.accessCd);
			}
			smartDetailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

		//エリアはアクセスされたメソッドで見分けて判定
		if (!eqInt(NumberUtils.toInt(smartDetailForm.areaCd), web.areaCd)) {
			log.warn("メールからのプレビュー時にエリアが一致しませんでした。エンティティのエリアコード：" + web.areaCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("求人原稿一覧を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。エンティティのアクセスコード：" + web.accessCd);
			}
			smartDetailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}
		smartDetailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + smartDetailForm.areaCd);
		if (GourmetCareeUtil.eqInt(web.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			smartDetailForm.cssLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_CSS);
			smartDetailForm.imgLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMG);
			smartDetailForm.imagesLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMAGES);
			smartDetailForm.incLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_INC);
			smartDetailForm.helpLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_HELP);
		}

	}


	/**
	 * 店舗一覧データ作成
	 */
	private void createShopListData() {
		TShopList shopList;
		try {
			shopList = shopListService.findById(NumberUtils.toInt(smartDetailForm.shopListId));
		} catch (SNoResultException e) {
			throw new PageNotFoundException("店舗一覧が見つかりません。", e);
		}

		Beans.copy(shopList, smartDetailForm).excludes("id").execute();

		try {
			VShopListMaterialNoData entity = shopListMaterialNoDataService.getMaterialEntity(shopList.id, NumberUtils.toInt(ShopListMaterialKbn.MAIN_1));
			smartDetailForm.imagePath = String.format("/util/imageUtility/displayShopListImageCache/%d/%s/%s", shopList.id, ShopListMaterialKbn.MAIN_1, GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
		} catch (WNoResultException e) {
			smartDetailForm.imagePath = null;
		}

		if (GourmetCareeUtil.eqInt(shopList.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			smartDetailForm.cssLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_CSS);
			smartDetailForm.imgLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMG);
			smartDetailForm.imagesLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_IMAGES);
			smartDetailForm.incLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_INC);
			smartDetailForm.helpLocation = smartDetailForm.getSendaiPath(SmartDetailForm.SYUTOKEN_HELP);
		}

		// 路線取得
		smartDetailForm.routeList = shopListRouteService.findByShopListId(Integer.parseInt(smartDetailForm.shopListId));

		smartDetailForm.arbeitPreviewPath = String.format(getCommonProperty("gc.arbeitPreview.smartPhone.url.format"), MArbeitConstants.ArbeitSite.getArbeitSiteConst(String.valueOf(shopList.arbeitTodouhukenId)), shopList.id, shopList.accessKey);
	}


	/**
	 * 号数作成
	 * @param volumeId
	 */
	private void createVolume(Integer volumeId) {
		if (volumeId == null) {
			return;
		}

		MVolume volume;
		try {
			volume = volumeService.findById(volumeId);
		} catch (SNoResultException e) {
			return;
		}

		Beans.copy(volume, smartDetailForm).excludes("id", "areaCd").execute();
	}
}

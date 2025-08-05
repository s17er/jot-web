package com.gourmetcaree.shop.pc.preview.action.shopListPreview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ShopListMaterialKbn;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.ShopListService.ShopListSearchProperty;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.db.shopList.dto.shopList.RelationShopListRetDto;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.preview.form.shopListPreview.SmartDraftListForm;
import com.gourmetcaree.shop.pc.shop.action.shop.ShopBaseAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * スマホ用店舗一覧ドラフトプレビューアクション
 * @author Takehiro Nakamori
 *
 */
public class SmartDraftListAction extends ShopBaseAction {

	private static final Logger log = Logger.getLogger(SmartDraftListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** アクションフォーム */
	@Resource
	@ActionForm
	private SmartDraftListForm smartDraftListForm;

	/** webサービス */
	@Resource
	private WebService webService;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;


	/** 関連する店舗一覧のリスト */
	private List<RelationShopListRetDto> relationDtoList;

	/** ページナビ */
	private PageNavigateHelper pageNavi;



	/**
	 * DB側からのインデックス
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{webId}/{accessCd}/{areaCd}", input = TransitionConstants.Preview.JSP_FSB01L02)
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, smartDraftListForm.webId, smartDraftListForm.accessCd, smartDraftListForm.areaCd);
		createWebData();
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	private String show() {
		try {
			createShopListData(GourmetCareeConstants.DEFAULT_PAGE);
		} catch (WNoResultException e) {
			smartDraftListForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
		return TransitionConstants.Preview.JSP_FSB01L02;
	}


	/**
	 * 次へパス
	 * @return
	 */
	@Execute(validator = false, urlPattern = "next/{webId}/{accessCd}/{areaCd}/{targetPage}")
	public String next() {
		checkArgsNull(NO_BLANK_FLG_NG, smartDraftListForm.webId);
		createWebData();
		try {
			createShopListData(NumberUtils.toInt(smartDraftListForm.targetPage, GourmetCareeConstants.DEFAULT_PAGE));
		} catch (WNoResultException e) {
			smartDraftListForm.setExistDataFlgNg();
		}
		return "/preview/body/fsO01LA02_list.jsp";
	}

	/**
	 * 店舗一覧の情報を作成します。
	 * @throws WNoResultException
	 */
	private void createShopListData(int targetPage) throws WNoResultException {
		smartDraftListForm.previewMethodKbn = PreviewMethodKbn.DRAFT_PREVIEW;
			ShopListSearchProperty property = new ShopListSearchProperty();
			property.customerId = smartDraftListForm.customerId;
			property.targetPage = NumberUtils.toInt(smartDraftListForm.targetPage, GourmetCareeConstants.DEFAULT_PAGE);
			property.maxRow = GourmetCareeConstants.DEFAULT_MAX_ROW_INT;

			property.smartImageCreator = new ShopListService.SmartPhoneImageCreator() {

				@Override
				public String createImagePath(TShopList shopList, VShopListMaterialNoData img) {
					return  String.format("/util/imageUtility/displayShopListImageCache/%d/%s/%s", shopList.id, ShopListMaterialKbn.MAIN_1, GourmetCareeUtil.createUniqueKey(img.insertDatetime));
				}
			};

			List<RelationShopListRetDto> dtoList = shopListService.createRelationShopListSelectByCustomerIdIterate(property);
			if (CollectionUtils.isEmpty(dtoList)) {
				smartDraftListForm.setExistDataFlgNg();
				throw new WNoResultException();
			}

			if (property.pageNavi.currentPage != targetPage) {
				relationDtoList = new ArrayList<RelationShopListRetDto>(0);
				pageNavi = new PageNavigateHelper(property.maxRow);
				smartDraftListForm.setExistDataFlgNg();
				throw new WNoResultException();
			}

			if (GourmetCareeUtil.eqInt(Integer.parseInt(smartDraftListForm.areaCd), MAreaConstants.AreaCd.SENDAI_AREA)) {
				smartDraftListForm.cssLocation = smartDraftListForm.getSendaiPath(SmartDraftListForm.SYUTOKEN_CSS);
				smartDraftListForm.imgLocation = smartDraftListForm.getSendaiPath(SmartDraftListForm.SYUTOKEN_IMG);
				smartDraftListForm.imagesLocation = smartDraftListForm.getSendaiPath(SmartDraftListForm.SYUTOKEN_IMAGES);
				smartDraftListForm.incLocation = smartDraftListForm.getSendaiPath(SmartDraftListForm.SYUTOKEN_INC);
				smartDraftListForm.helpLocation = smartDraftListForm.getSendaiPath(SmartDraftListForm.SYUTOKEN_HELP);
			}

			relationDtoList = dtoList;
			pageNavi = property.pageNavi;
			smartDraftListForm.setExistDataFlgOk();


	}


	/**
	 * webデータ作成
	 */
	private void createWebData() {
		try {
			TWeb web = webService.findById(NumberUtils.toInt(smartDraftListForm.webId));
			checkDraftAccess(web);
			Beans.copy(web, smartDraftListForm).execute();
			if (web.customerId == null || !GourmetCareeUtil.eqInt(MTypeConstants.ShopListDisplayKbn.ARI, web.shopListDisplayKbn)) {
				throw new FraudulentProcessException();
			}
			createVolume(web.volumeId);


			smartDraftListForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + smartDraftListForm.areaCd);
		} catch (SNoResultException e) {
			throw new FraudulentProcessException(e);
		}
	}


	/**
	 * ボリューム作成
	 * @param volumeId 号数ID
	 */
	private void createVolume(Integer volumeId) {
		if (volumeId != null) {
			try {
				MVolume volume = volumeService.findById(volumeId);
				SimpleDateFormat format = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
				smartDraftListForm.postStartDatetime = format.format(volume.postStartDatetime);
				smartDraftListForm.postEndDatetime = format.format(volume.postEndDatetime);
			} catch (SNoResultException e) {
				return;
			}
		}
	}


	/**
	 * メールからのプレビュー時のアクセス権限のチェック
	 * @param entityTWeb
	 */
	private void checkDraftAccess(TWeb entityTWeb) {

		//アクセスコードチェック
		if (!smartDraftListForm.accessCd.equals(entityTWeb.accessCd)) {
			log.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。 id : " + entityTWeb.id + " アクセスコード：" + entityTWeb.accessCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("スマホ用店舗一覧を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。 id : " + entityTWeb.id + " アクセスコード：" + entityTWeb.accessCd + "営業ID：" + userDto.masqueradeUserId);
			}
			smartDraftListForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

		try {
			//エリアはアクセスされたメソッドで見分けて判定
			if (!GourmetCareeUtil.eqInt(Integer.parseInt(smartDraftListForm.areaCd), entityTWeb.areaCd)) {
				log.warn("メールからのプレビュー時にエリアが一致しませんでした。 id : " + entityTWeb.id + " エリアコード：" + entityTWeb.areaCd);
				if (userDto.isMasqueradeFlg()) {
					sysMasqueradeLog.warn(String.format("スマホ用店舗一覧を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
					sysMasqueradeLog.warn("メールからのプレビュー時にエリアが一致しませんでした。 id : " + entityTWeb.id + " エリアコード：" + entityTWeb.areaCd + "営業ID：" + userDto.masqueradeUserId);
				}
				smartDraftListForm.setExistDataFlgNg();
				throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
			}
		} catch (NumberFormatException e) {
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}


	}



	public List<RelationShopListRetDto> getRelationDtoList() {
		return relationDtoList;
	}

	public PageNavigateHelper getPageNavi() {
		return pageNavi;
	}
}

package com.gourmetcaree.admin.pc.preview.action.shopListPreview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.preview.action.listPreview.PreviewBaseAction;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm;
import com.gourmetcaree.admin.pc.preview.form.shopListPreview.SmartListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.PreviewDto;
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
import com.gourmetcaree.db.common.service.ShopListService.SmartPhoneImageCreator;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.db.shopList.dto.shopList.RelationShopListRetDto;

/**
 * 店舗一覧スマホ用プレビューアクション
 * @author Takehiro Nakamori
 *
 */
public class SmartListAction extends PreviewBaseAction {

	/** アクションフォーム */
	@Resource
	@ActionForm
	private SmartListForm smartListForm;

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
	@Execute(validator = false, urlPattern = "index/{webId}", input = TransitionConstants.Preview.JSP_FSO01L02)
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, smartListForm.webId);
		smartListForm.imgMethodKbn = ListForm.ImgMethodKbn.IMG_FROM_DB;
		createWebData();
		return show();
	}


	/**
	 * webデータ作成
	 */
	private void createWebData() {
		try {
			TWeb web = webService.findById(NumberUtils.toInt(smartListForm.webId));
			Beans.copy(web, smartListForm).execute();
			if (web.customerId == null || !GourmetCareeUtil.eqInt(MTypeConstants.ShopListDisplayKbn.ARI, web.shopListDisplayKbn)) {
				throw new FraudulentProcessException();
			}
			createVolume(web.volumeId);
			smartListForm.areaCd = Integer.toString(web.areaCd);

			smartListForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + web.areaCd);
			if (GourmetCareeUtil.eqInt(web.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
				smartListForm.cssLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_CSS);
				smartListForm.imgLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_IMG);
				smartListForm.imagesLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_IMAGES);
				smartListForm.incLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_INC);
				smartListForm.helpLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_HELP);
			}

		} catch (SNoResultException e) {
			throw new FraudulentProcessException(e);
		}
	}


	/**
	 * セッション用INDEX
	 * @return
	 */
	@Execute(validator = false, urlPattern = "sessionIndex/{inputFormKbn}", input = TransitionConstants.Preview.JSP_FSO01L02)
	public String sessionIndex() {

		checkArgsNull(NO_BLANK_FLG_NG, smartListForm.inputFormKbn);
		smartListForm.imgMethodKbn = ListForm.ImgMethodKbn.IMG_FROM_SESSION;
		createSessionData();
		return show();
	}

	/**
	 * セッションデータ作成
	 */
	private void createSessionData() {
		PreviewDto previewDto = super.createPreviewDtoFromInputForm(smartListForm.inputFormKbn);
		Beans.copy(previewDto, smartListForm).execute();
		if (StringUtils.isNotBlank(previewDto.volumeId)) {
			createVolume(NumberUtils.toInt(previewDto.volumeId));
		}
		smartListForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + previewDto.areaCd);
		smartListForm.areaCd = Integer.toString(previewDto.areaCd);
		if (GourmetCareeUtil.eqInt(previewDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			smartListForm.cssLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_CSS);
			smartListForm.imgLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_IMG);
			smartListForm.imagesLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_IMAGES);
			smartListForm.incLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_INC);
			smartListForm.helpLocation = smartListForm.getSendaiPath(SmartListForm.SYUTOKEN_HELP);
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
				smartListForm.postStartDatetime = format.format(volume.postStartDatetime);
				smartListForm.postEndDatetime = format.format(volume.postEndDatetime);
			} catch (SNoResultException e) {
				return;
			}
		}
	}

	/**
	 * 初期表示
	 * @return
	 */
	private String show() {
		try {
			createShopListData(GourmetCareeConstants.DEFAULT_PAGE);
		} catch (WNoResultException e) {
			smartListForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
		return TransitionConstants.Preview.JSP_FSO01L02;
	}


	/**
	 * 次へパス
	 * @return
	 */
	@Execute(validator = false, urlPattern = "next/{webId}/{targetPage}")
	public String next() {
		checkArgsNull(NO_BLANK_FLG_NG, smartListForm.webId);
		createWebData();
		try {
			createShopListData(NumberUtils.toInt(smartListForm.targetPage, GourmetCareeConstants.DEFAULT_PAGE));
		} catch (WNoResultException e) {
			smartListForm.setExistDataFlgNg();
		}
		smartListForm.imgMethodKbn = ListForm.ImgMethodKbn.IMG_FROM_DB;
		return "/preview/body/fsO01LA02_list.jsp";
	}

	@Execute(validator = false, urlPattern = "nextSession/{inputFormKbn}/{targetPage}")
	public String nextSession() {
		createSessionData();

		try {
			createShopListData(NumberUtils.toInt(smartListForm.targetPage, GourmetCareeConstants.DEFAULT_PAGE));
		} catch (WNoResultException e) {
			smartListForm.setExistDataFlgNg();
		}
		smartListForm.imgMethodKbn = ListForm.ImgMethodKbn.IMG_FROM_SESSION;

		return "/preview/body/fsO01LA02_list.jsp";
	}

	/**
	 * 店舗一覧の情報を作成します。
	 * @throws WNoResultException
	 */
	private void createShopListData(int targetPage) throws WNoResultException {
			ShopListSearchProperty property = new ShopListSearchProperty();
			property.customerId = smartListForm.customerId;
			property.targetPage = NumberUtils.toInt(smartListForm.targetPage, GourmetCareeConstants.DEFAULT_PAGE);
			property.maxRow = GourmetCareeConstants.DEFAULT_MAX_ROW_INT;

			property.smartImageCreator = new SmartPhoneImageCreator() {
				@Override
				public String createImagePath(TShopList shop, VShopListMaterialNoData entity) {
					String imagePath = String.format(
							"/util/imageUtility/displayShopListImageCache/%d/%s/%s",
							entity.shopListId, ShopListMaterialKbn.MAIN_1,
							GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
					return imagePath;
				}
			};

			List<RelationShopListRetDto> dtoList = shopListService.createRelationShopListSelectByCustomerIdIterate(property);
			if (CollectionUtils.isEmpty(dtoList)) {
				smartListForm.setExistDataFlgNg();
				throw new WNoResultException();
			}

			if (property.pageNavi.currentPage != targetPage) {
				relationDtoList = new ArrayList<RelationShopListRetDto>(0);
				pageNavi = new PageNavigateHelper(property.maxRow);
				smartListForm.setExistDataFlgNg();
				throw new WNoResultException();
			}



			relationDtoList = dtoList;
			pageNavi = property.pageNavi;
			smartListForm.setExistDataFlgOk();

	}



	public List<RelationShopListRetDto> getRelationDtoList() {
		return relationDtoList;
	}

	public PageNavigateHelper getPageNavi() {
		return pageNavi;
	}
}

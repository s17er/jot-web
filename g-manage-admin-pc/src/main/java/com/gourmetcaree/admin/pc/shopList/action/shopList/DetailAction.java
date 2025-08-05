package com.gourmetcaree.admin.pc.shopList.action.shopList;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.shopList.form.shopList.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;

/**
 * 店舗一覧詳細画面
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DetailAction extends ShopListBaseAction {

	private static final Logger log = Logger.getLogger(DetailAction.class);

	/**
	 * 店舗一覧詳細アクションフォーム
	 */
	@ActionForm
	@Resource
	private DetailForm detailForm;

	/** 素材データなしサービス */
	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}/{id}", input = TransitionConstants.ShopList.JSP_APQ03R01)
	@MethodAccess(accessCode="SHOPLIST_DETAIL_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.customerId, detailForm.id);
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {
		createShopListData();
		return TransitionConstants.ShopList.JSP_APQ03R01;
	}

	/**
	 * 店舗一覧データの作成
	 */
	private void createShopListData() {
		try {
			TShopList entity = shopListService.getData(NumberUtils.toInt(detailForm.customerId),
													NumberUtils.toInt(detailForm.id),
													MTypeConstants.ShopListStatus.REGISTERED);
			Beans.copy(entity, detailForm).execute();
			detailForm.materialExistsDto = shopListMaterialNoDataService.getExistsDto(Integer.parseInt(detailForm.id));
			setMaterial(detailForm);
			createStationDtoList(entity.id, detailForm);
			createAttributeArrays(entity.id, detailForm);
			createConditionDtoList(entity.id, detailForm);
			getShopListTag(entity.id, detailForm);


		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * 店舗一覧データの削除
	 * @return
	 */
	@Execute(validator = false, reset = "resetShopId", input = TransitionConstants.ShopList.JSP_APQ03R01)
	@MethodAccess(accessCode="SHOPLIST_DETAIL_DELETE")
	public String delete() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.customerId, detailForm.id);
		TShopList entity = new TShopList();
		Beans.copy(detailForm, entity).execute();
		shopListService.regulationDelete(NumberUtils.toInt(detailForm.id), NumberUtils.toInt(detailForm.customerId), detailForm.version);
		shopListTagMappingService.deleteByShopListId(entity.id);
		log.info("店舗一覧を削除しました。ID=" + entity.id);
		StringBuilder sb = new StringBuilder(0);
		sb.append(TransitionConstants.ShopList.REDIRECT_SHOPLIST_DETAIL_DELETE_COMP);
		sb.append("&customerId=");
		sb.append(detailForm.customerId);
		return sb.toString();
	}

	/**
	 * 店舗一覧データ削除完了画面
	 */
	@Execute(validator = false, reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ03R01)
	@MethodAccess(accessCode="SHOPLIST_DETAIL_DELETECOMP")
	public String deleteComp() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.customerId);
		return TransitionConstants.ShopList.JSP_APQ03D01;
	}

	/**
	 * インデックスへ遷移
	 * @return
	 */
	@Execute(validator = false, urlPattern = "backToIndex/{customerId}", reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ03R01)
	@MethodAccess(accessCode="SHOPLIST_DETAIL_BACKTOINDEX")
	public String backToIndex() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.customerId);
		String backPath = createReindexPath(detailForm.customerId);
		detailForm.customerId = null;
		return backPath;
	}

	@Execute(validator = false, reset="resetForm", input = TransitionConstants.ShopList.JSP_APQ03R01)
	public String backToList() {
		return GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_LIST, "reShowList", "");
	}



}

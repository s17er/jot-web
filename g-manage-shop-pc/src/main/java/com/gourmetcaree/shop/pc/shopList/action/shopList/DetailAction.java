package com.gourmetcaree.shop.pc.shopList.action.shopList;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.shop.pc.shopList.form.shopList.DetailForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * 店舗一覧詳細画面
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired
public class DetailAction extends ShopListBaseAction {

	private static final Logger log = Logger.getLogger(DetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

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
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}", input = TransitionConstants.ShopList.JSP_SPJ04R01)
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {
		createShopListData();
		checkUnReadMail();
		return TransitionConstants.ShopList.JSP_SPJ04R01;
	}

	/**
	 * 店舗一覧データの作成
	 */
	private void createShopListData() {
		try {
			TShopList entity = shopListService.getData(userDto.getCustomerCd(),
													NumberUtils.toInt(detailForm.id),
													MTypeConstants.ShopListStatus.REGISTERED);
			Beans.copy(entity, detailForm).execute();
			detailForm.materialExistsDto = shopListMaterialNoDataService.getExistsDto(Integer.parseInt(detailForm.id));
			setMaterial(detailForm);
			createStationDtoList(entity.id, detailForm);
			createAttributeArrays(entity.id, detailForm);
            createConditionDtoList(entity.id, detailForm);
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * 店舗一覧データの削除
	 * @return
	 */
	@Execute(validator = false, reset = "resetShopId", input = TransitionConstants.ShopList.JSP_SPJ04R01)
	public String delete() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.deleteVersion);

		shopListService.regulationDelete(NumberUtils.toInt(detailForm.id),
				userDto.getCustomerCd(),
				NumberUtils.toLong(detailForm.deleteVersion));
		log.info("店舗一覧を削除しました。ID=" + detailForm.id);

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.info(String.format("店舗一覧を削除しました。ID：%s, 営業ID：%s, 顧客ID：%s", detailForm.id, userDto.masqueradeUserId, userDto.customerId));
		}

		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_DETAIL_DELETE_COMP;
	}

	/**
	 * 店舗一覧データ削除完了画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_SPJ04R01)
	public String deleteComp() {
		checkUnReadMail();
		return TransitionConstants.ShopList.JSP_SPJ04D01;
	}

	@Execute(validator = false, reset="resetForm", input = TransitionConstants.ShopList.JSP_SPJ04D01)
	public String backToList() {
		return GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_LIST, "reShowList", "");
	}

}

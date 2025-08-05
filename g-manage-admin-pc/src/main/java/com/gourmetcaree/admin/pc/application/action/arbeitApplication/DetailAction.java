package com.gourmetcaree.admin.pc.application.action.arbeitApplication;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.application.form.arbeitApplication.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.service.ArbeitApplicationService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.ShopListService;

/**
 * グルメdeバイト応募管理詳細アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DetailAction {

	/** アクションフォーム */
	@Resource
	@ActionForm
	private DetailForm detailForm;


	/** バイト応募サービス */
	@Resource
	private ArbeitApplicationService arbeitApplicationService;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	/** 顧客サービス */
	@Resource
	private CustomerService customerService;



	@Execute(validator = false, urlPattern = "{id}", input = TransitionConstants.Application.JSP_API03R01)
	public String index() {
		return show();
	}



	private String show() {
		createData();
		return TransitionConstants.Application.JSP_API03R01;
	}


	@Execute(validator = false)
	public String back() {
		return TransitionConstants.Application.REDIRECT_ARBEIT_APPLICATION_SEARCH_AGAIN;
	}

	/**
	 * データを作成します。
	 */
	private void createData() {
		try {
			TArbeitApplication application = arbeitApplicationService.findById(NumberUtils.toInt(detailForm.id));
			Beans.copy(application, detailForm).execute();

			TShopList shop = shopListService.findById(application.shopListId);

			MCustomer customer = customerService.findById(shop.customerId);
			detailForm.customerName = customer.customerName;
		} catch (SNoResultException e) {
			// TODO 見つからなかった場合の処理
		}
	}
}

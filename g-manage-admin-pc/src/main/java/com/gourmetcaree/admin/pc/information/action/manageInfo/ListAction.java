package com.gourmetcaree.admin.pc.information.action.manageInfo;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.information.action.information.InformationBaseAction;
import com.gourmetcaree.admin.pc.information.form.manageInfo.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.TInformation;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AreaService;

/**
 * お知らせ一覧を表示するActionです。
 * @author ando
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends InformationBaseAction {

	/** 店舗側のお知らせの個数 */
	private static final int FIXED_SHOP_INFORMATION_MAX = 1;

	/** 公開側のお知らせの個数 */
	private static final int FIXED_MYPAGE_INFORMATION_MAX = 2;

	/** アクションフォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	@Resource
	private AreaService areaService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{managementScreenKbn}", input = TransitionConstants.Information.JSP_APL02L01)
	@MethodAccess(accessCode="INFORMATION_LIST_INDEX")
	public String index() {

		checkId(listForm, listForm.managementScreenKbn);

		return show();
	}

	/**
	 * 初期表示遷移用
	 * お知らせは全データが初期登録されている前提があるので、
	 * データが全てなければ表示しない仕様とする。
	 * @return 登録画面のパス
	 */
	private String show() {

		try {
			List<TInformation> entityList = informationService.getInformationList(NumberUtils.toInt(listForm.managementScreenKbn));
			List<MArea> areaList = areaService.getAllArea();

			listForm.informationList = createInformationList(entityList, areaList,"/manageInfo/edit/", NumberUtils.toInt(listForm.managementScreenKbn));

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noSystemData");
		}

		// 登録画面へ遷移
		return TransitionConstants.Information.JSP_APL02L01;
	}

}
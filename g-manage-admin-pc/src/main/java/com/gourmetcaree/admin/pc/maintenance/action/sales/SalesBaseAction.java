package com.gourmetcaree.admin.pc.maintenance.action.sales;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.sales.SalesForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.constants.MTypeConstants.SubmailReceptionFlg;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.service.SalesService;

/**
 * 営業担当者管理Baseアクション
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
abstract public class SalesBaseAction extends PcAdminAction {


	/** 名前変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** 営業担当者サービス */
	@Resource
	protected SalesService salesService;

	/**
	 * 会社名、権限、サブメール受信可否をコードから名称へ変換
	 */
	protected void convertToName(SalesForm form) {

		// 会社名を取得
		form.companyName = valueToNameConvertLogic.convertToCompanyName(new String[] {form.companyId});

		// 権限名を取得
		form.authorityName = MSales.AUTH_LEVEL_MAP.get(form.authorityCd);

		// サブメール受信可否名を取得
		form.submailReceptionFlgName = valueToNameConvertLogic.convertToTypeName(SubmailReceptionFlg.TYPE_CD, new String[]{form.submailReceptionFlg});
	}

	/**
	 * 詳細画面表示データをセットする
	 */
	protected void convertShowData(SalesForm form) {

		try {
			MSales entity = salesService.findById(Integer.parseInt(form.id));

			// エンティティからフォームへデータをコピー
			Beans.copy(entity, form).dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "registrationDatetime").excludes("password").excludesNull().execute();

			// 登録日をフォーマット
//			form.registrationDatetime = DateUtils.getDateStr(entity.registrationDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);

			convertToName(form);

		} catch (NumberFormatException e) {
			form.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (SNoResultException e) {
			form.setExistDataFlgNg();
			// データなしエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");

		}
	}

}
package com.gourmetcaree.admin.pc.maintenance.action.advancedRegistration;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration.CsvForm;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationTempSignInCsvLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.exception.WNoResultException;


/**
 * 事前登録用CSVアクション
 * @author nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class CsvAction extends AbstractAdvancedRegistrationBaseAction {

	@ActionForm
	@Resource(name = "advancedRegistration_csvForm")
	private CsvForm form;

	@Resource(name = "advancedRegistrationTempSignInCsvLogic")
	private AdvancedRegistrationTempSignInCsvLogic tempCsvLogic;


	@Execute(validator = false, urlPattern = "tempSignIn/{advancedRegistrationId}")
	public String tempSignIn() {
		try {
			tempCsvLogic.output(NumberUtils.toInt(form.advancedRegistrationId));
		} catch (WNoResultException e) {
			log.warn(String.format("対象データが見つかりませんでした。アクションフォーム：[%s]", form), e);
			throw new ActionMessagesException("errors.app.csvDataNotFound");
		} catch (IOException e) {
			log.warn(String.format("CSV出力時にエラーが発生しました。アクションフォーム：[%s]", form), e);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}
		return null;
	}
}

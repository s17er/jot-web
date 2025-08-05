package com.gourmetcaree.admin.pc.application.action.observateApplication;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.beans.util.Copy;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.application.form.observateApplication.ObservateApplicationForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.logic.ApplicationCsvLogic;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ApplicationAttributeService;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.db.common.service.WebService;

/**
 * 応募管理Baseアクション
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
abstract public class ObservateApplicationBaseAction extends PcAdminAction {

	/** 応募サービス */
	@Resource
	protected ObservateApplicationService observateApplicationService;

	/** 応募属性サービス */
	@Resource
	protected ApplicationAttributeService applicationAttributeService;

	/** 応募CSVロジック */
	@Resource
	protected ApplicationCsvLogic applicationCsvLogic;

	@Resource
	protected WebService webService;


	/**
	 * 応募情報表示データをフォームにセット
	 * @param form 応募フォーム
	 */
	protected void convertDispData(ObservateApplicationForm form) {

		try {
			int id = Integer.parseInt(form.id);
			TObservateApplication entity = observateApplicationService.getApplicationDataById(id);

			Copy copy = Beans.copy(entity, form);
			copy.execute();
			form.applicationDateTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
			if (StringUtils.isNotEmpty(entity.phoneNo1)) {
				form.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
			}

			if (entity.mCustomer != null) {
				form.customerName = entity.mCustomer.customerName;
			}

			// リニューアル後は生年月日で登録
			if (entity.birthday != null) {
				form.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			}

		} catch (NumberFormatException e) {
			form.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			form.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

}
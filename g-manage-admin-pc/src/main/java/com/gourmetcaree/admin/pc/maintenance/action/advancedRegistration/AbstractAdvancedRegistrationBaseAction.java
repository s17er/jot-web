package com.gourmetcaree.admin.pc.maintenance.action.advancedRegistration;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration.AbstractAdvancedRegistrationInputBaseForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;
import com.gourmetcaree.db.common.service.AdvancedRegistrationService;

/**
 * 事前登録ベースアクション
 * @author Takehiro Nakamori
 *
 */
abstract class AbstractAdvancedRegistrationBaseAction extends PcAdminAction {

	/** ログ */
	protected final Logger log = Logger.getLogger(this.getClass());

	/** 事前登録マスタサービス */
	@Resource
	protected AdvancedRegistrationService advancedRegistrationService;


	/**
	 * 詳細データを作成します。
	 * @param form 入力系ベースフォーム
	 */
	protected void createDetail(AbstractAdvancedRegistrationInputBaseForm form) {
		MAdvancedRegistration entity;
		try {
			entity = advancedRegistrationService.findById(NumberUtils.toInt(form.id));
		} catch (SNoResultException e) {
			form.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		Beans.copy(entity, form).execute();
		String[] startDates = convertDateToStringArray(entity.termStartDatetime);
		form.termStartDate = startDates[0];
		form.termStartHour = startDates[1];
		form.termStartMinute = startDates[2];



		String[] endDates = convertDateToStringArray(entity.termEndDatetime);
		form.termEndDate = endDates[0];
		form.termEndHour = endDates[1];
		form.termEndMinute = endDates[2];

		form.setExistDataFlgOk();
	}

	private String[] convertDateToStringArray(Date date) {
		String dateStr = DateUtils.getDateStr(date, GourmetCareeConstants.DATE_TIME_FORMAT);
		return dateStr.split(" |:");
	}

}

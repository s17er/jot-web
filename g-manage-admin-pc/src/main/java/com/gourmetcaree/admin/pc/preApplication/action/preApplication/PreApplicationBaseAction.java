package com.gourmetcaree.admin.pc.preApplication.action.preApplication;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.beans.util.Copy;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.preApplication.dto.PreApplicationCareerHistoryDto;
import com.gourmetcaree.admin.pc.preApplication.form.preApplication.PreApplicationForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.logic.PreApplicationCsvLogic;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.entity.TPreApplicationAttribute;
import com.gourmetcaree.db.common.entity.TPreApplicationCareerHistory;
import com.gourmetcaree.db.common.entity.TPreApplicationSchoolHistory;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.PreApplicationAttributeService;
import com.gourmetcaree.db.common.service.PreApplicationCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.PreApplicationCareerHistoryService;
import com.gourmetcaree.db.common.service.PreApplicationSchoolHistoryService;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.db.common.service.WebService;

abstract class PreApplicationBaseAction extends PcAdminAction{

	/** 応募サービス */
	@Resource
	protected PreApplicationService preApplicationService;

	/** 応募属性サービス */
	@Resource
	protected PreApplicationAttributeService preApplicationAttributeService;

	/** 応募学歴サービス */
	@Resource
	protected PreApplicationSchoolHistoryService preApplicationSchoolHistoryService;

	/** 応募職歴サービス */
	@Resource
	protected PreApplicationCareerHistoryService preApplicationCareerHistoryService;

	/** 応募職歴属性サービス */
	@Resource
	protected PreApplicationCareerHistoryAttributeService preApplicationCareerHistoryAttributeService;

	/** WEBデータサービス */
	@Resource
	protected WebService webService;

	@Resource
	protected MemberService memberService;

	@Resource
	protected PreApplicationCsvLogic preApplicationCsvLogic;

	/** 値から名前の文字列への変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/**
	 * 応募情報表示データをフォームにセット
	 * @param form 応募フォーム
	 */
	protected void convertDispData(PreApplicationForm form) {

		try {
			int id = Integer.parseInt(form.id);
			TPreApplication entity = preApplicationService.getPreApplicationDataById(id);

			Copy copy = Beans.copy(entity, form);
			if (!ManageAuthLevel.ADMIN.value().equals(userDto.authLevel)) {
				copy.excludes("address", "municipality");
				form.municipality = GourmetCareeUtil.toMunicipality(entity.municipality);
			}
			copy.execute();
			form.applicationDateTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

			TWeb tWeb = webService.findById(entity.webId);
			form.manuscriptName = tWeb.manuscriptName;

			MMember mMember = memberService.findById(entity.memberId);
			form.pcMail = mMember.loginId;
			form.mobileMail = mMember.mobileMail;
			form.employPtnKbn = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD, entity.employPtnKbn);


			if (entity.mCustomer != null) {
				form.customerName = entity.mCustomer.customerName;
			}

			if(entity.salaryKbn != null) {
				form.salary = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalaryKbn.TYPE_CD, entity.salaryKbn);
			}

			if(entity.transferFlg != null) {
				form.transfer = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TransferFlg.TYPE_CD, entity.transferFlg);
			}

			if(entity.midnightShiftFlg != null) {
				form.midnightShift = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MidnightShiftFlg.TYPE_CD, entity.midnightShiftFlg);
			}

			List<TPreApplicationAttribute> attributeList = preApplicationAttributeService.getPreApplicationAttrListByApplicationId(entity.id);

			//応募属性をセット
			for(TPreApplicationAttribute attribute : attributeList) {
				if(MTypeConstants.FoodExpKbn.TYPE_CD.equals(attribute.attributeCd)) {
					form.foodExpKbn = attribute.attributeValue;
				}
				if(MTypeConstants.ExpManagerKbn.TYPE_CD.equals(attribute.attributeCd)) {
					form.expManagerKbn = attribute.attributeValue;
				}
				if(MTypeConstants.ExpManagerYearKbn.TYPE_CD.equals(attribute.attributeCd)) {
					form.expManagerYearKbn = attribute.attributeValue;
				}
				if(MTypeConstants.ExpManagerPersonsKbn.TYPE_CD.equals(attribute.attributeCd)) {
					form.expManagerPersonsKbn = attribute.attributeValue;
				}
			}

			form.qualificationKbnList = GourmetCareeUtil.toIntToStringArray(
					preApplicationAttributeService.getPreApplicationAttrValue(
							id, MTypeConstants.QualificationKbn.TYPE_CD));

			createHistories(entity.id, form);

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

	/**
	 * WEB履歴書学歴、WEB履歴書職歴を取得し、フォームに設定します。
	 * @param memberId 会員ID
	 * @param form 応募フォーム
	 * @author Makoto Otani
	 */
	private void createHistories(int applicationId, PreApplicationForm form) {

		// 学歴の取得

		TPreApplicationSchoolHistory school = preApplicationSchoolHistoryService.getPreApplicationSchoolHistoryByApplicationId(applicationId);

		if(school != null) {
			form.schoolHistory = school;
		}

		// 職歴の取得
		List<TPreApplicationCareerHistory> entityList = preApplicationCareerHistoryService.getPreApplicationCareerHistoryByApplicationId(applicationId);
		if(entityList != null) {
			List<PreApplicationCareerHistoryDto> dtoList = new ArrayList<PreApplicationCareerHistoryDto>();
			for (TPreApplicationCareerHistory entity : entityList) {
				PreApplicationCareerHistoryDto dto = new PreApplicationCareerHistoryDto();
				Beans.copy(entity, dto).execute();

				try {
					List<Integer> valueList = preApplicationCareerHistoryService.getAttributeValueListByPreApplicationCareerHistoryId(entity.id, "job_kbn");
					dto.jobKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD,
								GourmetCareeUtil.toIntToStringArray(ArrayUtils.toPrimitive(valueList.toArray(new Integer[0]))));
				} catch (WNoResultException e) {
					dto.jobKbnName = "";
				}

				try {
					List<Integer> valueList = preApplicationCareerHistoryService.getAttributeValueListByPreApplicationCareerHistoryId(entity.id, "industry_kbn");
					dto.industryKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.IndustryKbn.TYPE_CD,
								GourmetCareeUtil.toIntToStringArray(ArrayUtils.toPrimitive(valueList.toArray(new Integer[0]))));
				} catch (WNoResultException e) {
					dto.industryKbnName = "";
				}
				dtoList.add(dto);
			}
			form.careerHistoryList = dtoList;
		}
	}
}

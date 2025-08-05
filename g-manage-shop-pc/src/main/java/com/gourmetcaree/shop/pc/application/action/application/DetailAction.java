package com.gourmetcaree.shop.pc.application.action.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TApplicationAttribute;
import com.gourmetcaree.db.common.entity.TApplicationCareerHistory;
import com.gourmetcaree.db.common.entity.TApplicationCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ApplicationAttributeService;
import com.gourmetcaree.db.common.service.ApplicationCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.ApplicationCareerHistoryService;
import com.gourmetcaree.db.common.service.ApplicationSchoolHistoryService;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.CareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.CareerHistoryService;
import com.gourmetcaree.db.common.service.SchoolHistoryService;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.pc.application.dto.application.CareerHistoryDto;
import com.gourmetcaree.shop.pc.application.form.application.DetailForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;


/**
 * 応募者詳細を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class DetailAction extends MailListAction {

	/** 応募者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 応募サービス */
	@Resource
	protected ApplicationService applicationService;

	@Resource
	protected ApplicationCareerHistoryService applicationCareerHistoryService;

	@Resource
	protected ApplicationCareerHistoryAttributeService applicationCareerHistoryAttributeService;

	@Resource
	protected ApplicationSchoolHistoryService applicationSchoolHistoryService;

	@Resource
	private CareerHistoryService careerHistoryService;

	@Resource
	private SchoolHistoryService schoolHistoryService;

	@Resource
	private CareerHistoryAttributeService careerHistoryAttributeService;

	@Resource
	private ApplicationAttributeService applicationAttributeService;

	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC01R01)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		return show();
	}


	/**
	 * 応募者情報のダイアログ
	 * @return
	 */
	@Execute(validator = false, urlPattern = "subApplicationDetail/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC02R01_SUB)
	public String subApplicationDetail() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		show();

		return TransitionConstants.Application.JSP_SPC02R01_SUB;
	}


	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		try {
			TApplication entity = applicationLogic.findByIdFromApplication(NumberUtils.toInt(detailForm.id));
			entity.tApplicationAttributeList = applicationAttributeService.getApplicationAttrListByApplicationId(entity.id);
			Beans.copy(entity, detailForm).execute();
			//初回メールを保持
			detailForm.firstMailId = entity.mailId;

			for(TApplicationAttribute applicationAttribute : entity.tApplicationAttributeList) {
				if("exp_manager_kbn".equals(applicationAttribute.attributeCd)) {
					detailForm.expManagerKbn = applicationAttribute.attributeValue;
				} else if ("exp_manager_year_kbn".equals(applicationAttribute.attributeCd)) {
					detailForm.expManagerYearKbn = applicationAttribute.attributeValue;
				} else if ("exp_manager_persons_kbn".equals(applicationAttribute.attributeCd)) {
					detailForm.expManagerPersonsKbn = applicationAttribute.attributeValue;
				} else if ("food_exp_kbn".equals(applicationAttribute.attributeCd)) {
					detailForm.foodExpKbn = applicationAttribute.attributeValue;
				}
			}

			detailForm.qualificationKbnList = GourmetCareeUtil.toIntToStringArray(
					applicationAttributeService.getApplicationAttrValue(
							entity.id, MTypeConstants.QualificationKbn.TYPE_CD));

			if (entity.memberId != null) {
				createHistories(entity.memberId);
			} else {
				createNonMemberHistories(entity.id);
			}

			if (entity.birthday != null) {
				detailForm.age = GourmetCareeUtil.convertToAge(entity.birthday);
			}

			detailForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return TransitionConstants.Application.JSP_SPC01R01;
	}

	/**
	 * 応募者のメモを編集する
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC01R01)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TApplication entity = new TApplication();
		Beans.copy(detailForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		entity.memoUpdateDatetime = new Date();

		applicationService.update(entity);

		return "/application/detail/index/" + detailForm.id + GourmetCareeConstants.REDIRECT_STR;
	}


	private void createHistories(int memberId) {
		if (!GourmetCareeUtil.eqInt(MTypeConstants.JobHistoryDisplayFlg.DISPLAY, detailForm.jobHistoryDisplayFlg)) {
			return;
		}

		try {
			detailForm.schoolHistory = schoolHistoryService.getTSchoolHistoryDataByMemberId(memberId);
		} catch (WNoResultException e) {
			// 何もしない
		}

		try {
			List<TCareerHistory> entityList = careerHistoryService.getCareerHistoryDataByMemberId(memberId);
			List<CareerHistoryDto> dtoList = new ArrayList<CareerHistoryDto>();

			for (TCareerHistory entity : entityList) {

				CareerHistoryDto dto = new CareerHistoryDto();
				Beans.copy(entity, dto).execute();

				List<String> jobList = new ArrayList<String>();
				List<String> industryList = new ArrayList<String>();

				for (TCareerHistoryAttribute attrEntity : entity.tCareerHistoryAttributeList) {

					// 職種
					if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						jobList.add(String.valueOf(attrEntity.attributeValue));
						continue;
					}

					// 業態
					if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						industryList.add(String.valueOf(attrEntity.attributeValue));
						continue;
					}
				}

				// 職種の値設定
				if (CollectionUtils.isNotEmpty(jobList)) {
					dto.jobKbnName = valueToNameConvertLogic.convertToJobName(jobList.toArray(new String[0]));
				} else {
					dto.jobKbnName = "";
				}

				// 業態の値設定
				if (CollectionUtils.isNotEmpty(industryList)) {
					dto.industryKbnName = valueToNameConvertLogic.convertToIndustryName(industryList.toArray(new String[0]));
				} else {
					dto.industryKbnName = "";
				}

				dtoList.add(dto);
			}

			detailForm.careerHistoryList = dtoList;

		} catch (WNoResultException e) {
			// 何もしない
		}
	}

	private void createNonMemberHistories(int applicationId) {
		if (!GourmetCareeUtil.eqInt(MTypeConstants.JobHistoryDisplayFlg.DISPLAY, detailForm.jobHistoryDisplayFlg)) {
			return;
		}

		detailForm.applicationSchoolHistory = applicationSchoolHistoryService.getApplicationSchoolHistoryByApplicationId(applicationId);

			List<TApplicationCareerHistory> entityList = applicationCareerHistoryService.getApplicationCareerHistoryByApplicationId(applicationId);
			List<CareerHistoryDto> dtoList = new ArrayList<CareerHistoryDto>();
			if(entityList != null) {
				for (TApplicationCareerHistory entity : entityList) {
					entity.tApplicationCareerHistoryAttributeList = applicationCareerHistoryAttributeService.getApplicationCareerHistoryByApplicationCareerHistoryId(entity.id);
					CareerHistoryDto dto = new CareerHistoryDto();
					Beans.copy(entity, dto).execute();

					List<String> jobList = new ArrayList<String>();
					List<String> industryList = new ArrayList<String>();

					if(entity.tApplicationCareerHistoryAttributeList != null) {
						for (TApplicationCareerHistoryAttribute attrEntity : entity.tApplicationCareerHistoryAttributeList) {

							// 職種
							if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
								jobList.add(String.valueOf(attrEntity.attributeValue));
								continue;
							}

							// 業態
							if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
								industryList.add(String.valueOf(attrEntity.attributeValue));
								continue;
							}
						}
					}

					// 職種の値設定
					if (CollectionUtils.isNotEmpty(jobList)) {
						dto.jobKbnName = valueToNameConvertLogic.convertToJobName(jobList.toArray(new String[0]));
					} else {
						dto.jobKbnName = "";
					}

					// 業態の値設定
					if (CollectionUtils.isNotEmpty(industryList)) {
						dto.industryKbnName = valueToNameConvertLogic.convertToIndustryName(industryList.toArray(new String[0]));
					} else {
						dto.industryKbnName = "";
					}

					dtoList.add(dto);
				}
			}

			detailForm.careerHistoryList = dtoList;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.APPLICATION_MAIL;
	}
}

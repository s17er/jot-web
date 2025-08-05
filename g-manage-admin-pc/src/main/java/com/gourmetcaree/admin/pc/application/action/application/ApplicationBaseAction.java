package com.gourmetcaree.admin.pc.application.action.application;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.beans.util.Copy;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.application.dto.application.CareerHistoryDto;
import com.gourmetcaree.admin.pc.application.form.application.ApplicationForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.logic.ApplicationCsvLogic;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TApplicationAttribute;
import com.gourmetcaree.db.common.entity.TApplicationCareerHistory;
import com.gourmetcaree.db.common.entity.TApplicationCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TApplicationSchoolHistory;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ApplicationAttributeService;
import com.gourmetcaree.db.common.service.ApplicationCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.ApplicationCareerHistoryService;
import com.gourmetcaree.db.common.service.ApplicationSchoolHistoryService;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.CareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.CareerHistoryService;
import com.gourmetcaree.db.common.service.SchoolHistoryService;
import com.gourmetcaree.db.common.service.ShopListService;

/**
 * 応募管理Baseアクション
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
abstract public class ApplicationBaseAction extends PcAdminAction {

	/** 応募サービス */
	@Resource
	protected ApplicationService applicationService;

	/** 応募属性サービス */
	@Resource
	protected ApplicationAttributeService applicationAttributeService;

	/** 応募学歴サービス */
	@Resource
	protected ApplicationSchoolHistoryService applicationSchoolHistoryService;

	/** 応募職歴サービス */
	@Resource
	protected ApplicationCareerHistoryService applicationCareerHistoryService;

	/** 応募職歴属性サービス */
	@Resource
	protected ApplicationCareerHistoryAttributeService applicationCareerHistoryAttributeService;

	/** 応募CSVロジック */
	@Resource
	protected ApplicationCsvLogic applicationCsvLogic;

	/** WEB履歴書学歴サービス */
	@Resource
	private SchoolHistoryService schoolHistoryService;

	/** WEB履歴書職歴サービス */
	@Resource
	private CareerHistoryService careerHistoryService;

	/** WEB履歴書職歴属性サービス */
	@Resource
	private CareerHistoryAttributeService careerHistoryAttributeService;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	/** 値から名前の文字列への変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;


	/**
	 * 応募情報表示データをフォームにセット
	 * @param form 応募フォーム
	 */
	protected void convertDispData(ApplicationForm form) {

		try {
			int id = Integer.parseInt(form.id);
			TApplication entity = applicationService.getApplicationDataById(id);

			Copy copy = Beans.copy(entity, form);
			if (!ManageAuthLevel.ADMIN.value().equals(userDto.authLevel) && !ManageAuthLevel.SALES.value().equals(userDto.authLevel)) {
				copy.excludes("address", "municipality");
				form.municipality = GourmetCareeUtil.toMunicipality(entity.municipality);
			}
			copy.execute();
			form.applicationDateTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
			if (StringUtils.isNotEmpty(entity.phoneNo1)) {
				form.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
			}

			if (entity.mCustomer != null) {
				form.customerName = entity.mCustomer.customerName;
			}
			List<TApplicationAttribute> attributeList = applicationAttributeService.getApplicationAttrListByApplicationId(entity.id);

			//応募属性をセット
			for(TApplicationAttribute attribute : attributeList) {
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
					applicationAttributeService.getApplicationAttrValue(
							id, MTypeConstants.QualificationKbn.TYPE_CD));

			createHistories(entity.id, form);

			// 会員で経歴を公開する場合は、応募者の職歴、学歴の取得
//			if (GourmetCareeUtil.eqInt(MTypeConstants.MemberFlg.MEMBER, entity.memberFlg)
//					&& entity.mailId != null && entity.memberId != 0
//					 && GourmetCareeUtil.eqInt(MTypeConstants.JobHistoryDisplayFlg.DISPLAY, entity.jobHistoryDisplayFlg)) {
//				createHistories(entity.memberId, form);
//			}

			if (entity.birthday != null) {
				form.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			}

			if (entity.shopListId != null) {
				form.shopName = shopListService.findById(entity.shopListId).shopName;
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

	/**
	 * WEB履歴書学歴、WEB履歴書職歴を取得し、フォームに設定します。
	 * @param memberId 会員ID
	 * @param form 応募フォーム
	 * @author Makoto Otani
	 */
	private void createHistories(int applicationId, ApplicationForm form) {

		// 学歴の取得

		TApplicationSchoolHistory school = applicationSchoolHistoryService.getApplicationSchoolHistoryByApplicationId(applicationId);

		if(school != null) {
			form.schoolHistory = school;
		}

		// 職歴の取得
		List<TApplicationCareerHistory> entityList = applicationCareerHistoryService.getApplicationCareerHistoryByApplicationId(applicationId);
		if(entityList != null) {
			List<CareerHistoryDto> dtoList = new ArrayList<CareerHistoryDto>();
			for (TApplicationCareerHistory entity : entityList) {
				CareerHistoryDto dto = new CareerHistoryDto();
				Beans.copy(entity, dto).execute();
				List<TApplicationCareerHistoryAttribute> arrtibuteList = applicationCareerHistoryAttributeService.getApplicationCareerHistoryByApplicationCareerHistoryId(entity.id);
				if(arrtibuteList != null) {
					List<Integer> valueList  = new ArrayList<>();
					for(TApplicationCareerHistoryAttribute attribute : arrtibuteList) {
						if("job_kbn".equals(attribute.attributeCd) ) {
							valueList.add(Integer.valueOf(attribute.attributeValue));
						}
					}
					dto.jobKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD,
							GourmetCareeUtil.toIntToStringArray(ArrayUtils.toPrimitive(valueList.toArray(new Integer[0]))));
				} else {
					dto.jobKbnName = "";
				}
				dtoList.add(dto);
			}
			form.careerHistoryList = dtoList;
		}
	}

}
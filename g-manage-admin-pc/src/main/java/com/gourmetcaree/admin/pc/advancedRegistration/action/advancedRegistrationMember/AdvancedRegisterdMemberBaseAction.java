package com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMember;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.ListForm;
import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationLogic;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationLogic.SearchProperty;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.logic.advancedregistration.MemberLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryAttributeService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryCareerHistoryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntrySchoolHistoryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationService;


/**
 * 事前登録会員用規定アクションクラス
 * @author Takehiro Nakamori
 *
 */
public abstract class AdvancedRegisterdMemberBaseAction extends PcAdminAction {

	/** 事前登録マスタサービス */
	@Resource
	protected AdvancedRegistrationService advancedRegistrationService;

	/** 事前登録エントリサービス */
	@Resource
	protected AdvancedRegistrationEntryService advancedRegistrationEntryService;

	/** 事前登録エントリ属性サービス */
	@Resource
	protected AdvancedRegistrationEntryAttributeService advancedRegistrationEntryAttributeService;

	/** 事前登録職歴サービス */
	@Resource
	protected AdvancedRegistrationEntryCareerHistoryService advancedRegistrationEntryCareerHistoryService;

	/** 事前登録職歴属性サービス */
	@Resource
	protected AdvancedRegistrationEntryCareerHistoryAttributeService advancedRegistrationEntryCareerHistoryAttributeService;

	/** 事前登録学歴サービス */
	@Resource
	protected AdvancedRegistrationEntrySchoolHistoryService advancedRegistrationEntrySchoolHistoryService;

	/** 事前登録ロジック */
	@Resource
	protected AdvancedRegistrationLogic advancedRegistrationLogic;

	/** 名称変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource(name = "advancedregistration_memberLogic")
	MemberLogic logic;


	/**
	 * 職歴を作成します。
	 * @param id 事前登録ID
	 */
	protected List<CareerDto> createCareerHistory(Integer id) {
		return logic.createCareerHistory(id, CareerDto.class);
	}




	/**
	 * プロパティを作成します
	 * @return
	 */
	protected SearchProperty createProperty(int targetPage, ListForm listForm) {
		SearchProperty property = advancedRegistrationLogic.createSearchProperty();
		Beans.copy(listForm, property)
				.excludesWhitespace()
				.excludesNull()
				.execute();

		property.advancedRegistrationIdList = GourmetCareeUtil.convertStringArrayToIntegerList(listForm.advancedRegistrationIdArray);

		property.registrationStartTimestamp = listForm.createRegistrationStartTimestamp();
		property.registrationEndTimestamp = listForm.createRegistrationEndTimestamp();
		property.targetPage = targetPage;

		return property;
	}

	/**
	 * 転職希望時期文字列作成
	 */
	protected String createHopeTermStr(String year, String month, List<Integer> kbnList) {
		return logic.createHopeTermStr(year, month, kbnList);
	}
}

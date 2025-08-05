package com.gourmetcaree.admin.pc.member.action.member;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.DetailForm;
import com.gourmetcaree.admin.pc.juskill.form.juskillMember.JuskillMemberForm;
import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.admin.pc.member.form.member.MemberForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.dto.AdvancedRegistrationSearchResultDto;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationLogic;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationLogic.SearchProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.logic.advancedregistration.MemberLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;
import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.MMemberAttribute;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TSchoolHistory;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.AdvancedRegistrationService;
import com.gourmetcaree.db.common.service.CareerHistoryService;
import com.gourmetcaree.db.common.service.CityService;
import com.gourmetcaree.db.common.service.JuskillMemberListNewService;
import com.gourmetcaree.db.common.service.JuskillMemberListService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.SchoolHistoryService;
import com.gourmetcaree.db.common.service.TypeService;


/**
 * 会員管理Baseアクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
abstract public class MemberBaseAction extends PcAdminAction {


	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** 学歴サービス */
	@Resource
	protected SchoolHistoryService schoolHistoryService;

	/** 職歴サービス */
	@Resource
	protected CareerHistoryService careerHistoryService;

	/** 区分マスタサービス */
	@Resource
	protected TypeService typeService;

	/** 事前登録ロジック */
	@Resource
	protected AdvancedRegistrationLogic advancedRegistrationLogic;

	/** 事前登録マスタサービス */
	@Resource
	protected AdvancedRegistrationService advancedRegistrationService;

	@Resource(name = "advancedregistration_memberLogic")
	MemberLogic logic;

	/** 事前登録のリスト */
	private List<MAdvancedRegistration> advancedRegistrationList;

	/** 会員情報リスト */
	private List<AdvancedRegistrationSearchResultDto> advancedRegistrationMemberDtoList;

	/** 事前登録のFORM */
	@Resource(name = "advancedRegistrationMember_detailForm")
	private com.gourmetcaree.common.form.advancedregistration.MemberForm<CareerHistoryDtoAccessor> advancedRegistrationForm;

	/** ページナビ */
	private PageNavigateHelper pageNavi;

	/** ジャスキル会員サービス */
	@Resource
	protected JuskillMemberListNewService juskillMemberListNewService;
	@Resource
	protected JuskillMemberListService juskillMemberListService;

	/** ジャスキルフォーム */
	private JuskillMemberForm juskillMemberForm;

	/** 市区町村サービス */
	@Resource
	protected CityService cityService;

	/**
	 * 表示データをフォームにセットする
	 * @param form 会員フォーム
	 */
	protected void convertDispData(MemberForm form) {

		try {
			// 会員情報・会員属性情報をフォームにセット
			convertmemberData(form);

			// 学歴情報をフォームにセット
			convertSchoolHistoryData(form);

			// 職歴・職歴属性情報をフォームにセット
			convertCareerData(form);

			// 合同説明会の会員情報をフォームにセット
			convertAdvancedRegistrationMemberData(form);

			// ジャスキル情報をフォームにセット
			convertJuskillData(form);

		} catch (NumberFormatException e) {
			form.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			form.setExistDataFlgNg();
			// データなしのエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * ジャスキル情報をフォームにセット
	 * @param form 会員フォーム
	 * @throws NumberFormatException
	 * @throws WNoResultException
	 */
	protected void convertJuskillData(MemberForm form) throws NumberFormatException, WNoResultException {

		try {

			MJuskillMember mJuskillMember = juskillMemberListNewService.findByMemberId(Integer.parseInt(form.id));

			MJuskillMember vJuskillMember = juskillMemberListNewService.getDetail(mJuskillMember.id);

			//Dateは下で変換
			juskillMemberForm = Beans.createAndCopy(JuskillMemberForm.class, vJuskillMember).execute();
			if (vJuskillMember.birthday != null) {
				juskillMemberForm.birthYear = DateUtils.getDateStr(vJuskillMember.birthday, GourmetCareeConstants.YEAR_FORMAT);
				juskillMemberForm.birthMonth = DateUtils.getDateStr(vJuskillMember.birthday, GourmetCareeConstants.SINGLE_MONTH_FORMAT);
				juskillMemberForm.birthDate = DateUtils.getDateStr(vJuskillMember.birthday, GourmetCareeConstants.SINGLE_DAY_FORMAT);
				juskillMemberForm.age = String.valueOf(calcAge(
						DateUtils.convertStrDate(juskillMemberForm.birthYear, juskillMemberForm.birthMonth, juskillMemberForm.birthDate),
						DateUtils.getJustDate()));
			}

			// 職歴リストをセット
			if (CollectionUtils.isNotEmpty(vJuskillMember.mJuskillMemberCareerHistoryList)) {
				juskillMemberForm.careerHistoryList = vJuskillMember.mJuskillMemberCareerHistoryList.stream()
						.map(entity -> entity.careerHistory).collect(Collectors.toList());
			}

			form.juskillMemberForm = juskillMemberForm;
			form.juskillInfoFlg = true;
		} catch (SNoResultException e) {
			form.juskillMemberForm = null;
			form.juskillInfoFlg = false;
		} catch (WNoResultException e) {
			form.juskillMemberForm = null;
			form.juskillInfoFlg = false;
		} catch (ParseException e) {
			form.juskillMemberForm = null;
			form.juskillInfoFlg = false;
		}
	}

	private int calcAge(Date birthday, Date now) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    return (Integer.parseInt(sdf.format(now)) - Integer.parseInt(sdf.format(birthday))) / 10000;
	}

	/**
	 * 合同説明会の登録情報（会員）をフォームにセット
	 * @param form 会員フォーム
	 * @throws NumberFormatException
	 * @throws WNoResultException
	 */
	protected void convertAdvancedRegistrationMemberData(MemberForm form) throws NumberFormatException, WNoResultException {

		SearchProperty property = createProperty(1, form);
		pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

		try {
			/* 登録情報 */
			advancedRegistrationMemberDtoList = advancedRegistrationLogic.doSearch(property, pageNavi);
			if (CollectionUtils.isEmpty(advancedRegistrationMemberDtoList)) {
				throw new WNoResultException();
			}
			Map<Integer, DetailForm> advancedRegistrationFormMap = new HashMap<>();

			for (AdvancedRegistrationSearchResultDto dto : advancedRegistrationMemberDtoList) {
				if (advancedRegistrationFormMap.containsKey(dto.advancedRegistrationId)) {
					continue;
				}
				final DetailForm f = new DetailForm();
				f.id = dto.id.toString();
				logic.createDetailData(f);
				advancedRegistrationFormMap.put(dto.advancedRegistrationId, f);
			}

			/* 合同説明会情報取得 */
			List<Integer> advancedRegistrationIdArray = advancedRegistrationMemberDtoList
					.stream()
					.map(dto -> dto.advancedRegistrationId)
					.collect(Collectors.toList());


			advancedRegistrationList = advancedRegistrationService.findById(advancedRegistrationIdArray.toArray(new Integer[advancedRegistrationIdArray.size()]));
			if (CollectionUtils.isEmpty(advancedRegistrationList)) {
				throw new WNoResultException();
			}

			form.advanceRegistrationFlg = true;
			form.advancedRegistrationList = advancedRegistrationList;
			form.advancedRegistrationDtoMemberMap = advancedRegistrationFormMap;
		} catch (WNoResultException e) {
			// 職歴がない場合は、空の職歴を作成
			form.advancedRegistrationList = new ArrayList<>();
			form.advanceRegistrationFlg = false;
		}
	}

	/**
	 * プロパティを作成します
	 * @return
	 */
	protected SearchProperty createProperty(int targetPage, MemberForm form) {
		SearchProperty property = advancedRegistrationLogic.createSearchProperty();
		property.advancedRegistrationUserId = Integer.parseInt(form.id);

		return property;
	}

	/**
	 * 会員情報・会員属性情報をフォームにセット
	 * @param form 会員フォーム
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void convertmemberData(MemberForm form) throws NumberFormatException, WNoResultException {

		MMember member = memberService.getMemberDataById(Integer.parseInt(form.id));

		//Dateは下で変換
		Beans.copy(member, form).excludes("password", "registrationDatetime").execute();
		form.insertDatatime = DateUtils.getDateStr(member.insertDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
		form.registrationDatetime = DateUtils.getDateStr(member.registrationDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
		form.birthYear = DateUtils.getDateStr(member.birthday, GourmetCareeConstants.YEAR_FORMAT);
		form.birthMonth = DateUtils.getDateStr(member.birthday, GourmetCareeConstants.SINGLE_MONTH_FORMAT);
		form.birthDay = DateUtils.getDateStr(member.birthday, GourmetCareeConstants.SINGLE_DAY_FORMAT);

		if (member.tLoginHistory != null && member.tLoginHistory.lastLoginDatetime != null) {
			form.lastLoginDatetime = DateUtils.getDateStr(member.tLoginHistory.lastLoginDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
		}

		if (StringUtils.isNotEmpty(member.phoneNo1)) {
			form.phoneNo = member.phoneNo1 + "-" + member.phoneNo2 + "-" + member.phoneNo3;
		}

		// 会員属性をフォームにセット
		List<String> jobList = new ArrayList<String>();
		List<String> industryList = new ArrayList<String>();
		List<String> qualificationList = new ArrayList<String>();
		List<String> employPtnList = new ArrayList<String>();

		for (MMemberAttribute attrEntity : member.mMemberAttributeList) {

			if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				jobList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				industryList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.QualificationKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				qualificationList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.EmployPtnKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				employPtnList.add(String.valueOf(attrEntity.attributeValue));
			}
		}

		form.job = (String[]) jobList.toArray(new String[0]);
		form.industry = (String[]) industryList.toArray(new String[0]);
		form.qualification = (String[]) qualificationList.toArray(new String[0]);
		form.employPtnKbns = (String[]) employPtnList.toArray(new String[0]);

		form.hopeCityCdList = member.mMemberHopeCityList.stream().map(s -> String.valueOf(s.cityCd)).collect(Collectors.toList());
		converDisptHopeCity(form, form.hopeCityCdList);
	}

	/**
	 * 希望勤務地の表示用に変換
	 * @param form
	 * @param list
	 */
	protected void converDisptHopeCity(MemberForm form, List<String> cityCdList) {
		form.hopeCityListMap = new LinkedHashMap<>();

		for (String cityCd : cityCdList) {
			Integer prefCd = cityService.getPrefectureCd(cityCd);
			if (form.hopeCityListMap.containsKey(prefCd)) {
				form.hopeCityListMap.get(prefCd).add(cityCd);
				continue;
			}
			List<String> cityList = new ArrayList<>();
			cityList.add(cityCd);
			form.hopeCityListMap.put(prefCd, cityList);
		}
	}

	/**
	 * 学歴情報をフォームにセット
	 * @param form 会員フォーム
	 */
	private void convertSchoolHistoryData(MemberForm form) throws NumberFormatException {

		try {
			TSchoolHistory tSchoolHistory = schoolHistoryService.getTSchoolHistoryDataByMemberId(Integer.parseInt(form.id));

			Beans.copy(tSchoolHistory, form).excludes("id", "version").execute();
		} catch (WNoResultException e) {
			// 学歴情報がない場合は、何も処理しない。
		}
	}

	/**
	 * 職歴情報をフォームにセット
	 * @param form
	 */
	private void convertCareerData(MemberForm form) throws NumberFormatException {

		try {
			List<TCareerHistory> entityList = careerHistoryService.getCareerHistoryDataByMemberId(Integer.parseInt(form.id));

			List<CareerDto> careerList = new ArrayList<CareerDto>();
			for (TCareerHistory entity : entityList) {
				CareerDto dto = new CareerDto();
				List<String> jobList = new ArrayList<String>();
				List<String> industryList = new ArrayList<String>();
				Beans.copy(entity, dto).execute();

				for (TCareerHistoryAttribute attrEntity : entity.tCareerHistoryAttributeList) {

					// 職種
					if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						jobList.add(String.valueOf(attrEntity.attributeValue));
					}

					// 業態
					if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						industryList.add(String.valueOf(attrEntity.attributeValue));
					}
				}

				dto.job = (String[]) jobList.toArray(new String[0]);
				dto.industry = (String[]) industryList.toArray(new String[0]);
				careerList.add(dto);
			}

			form.careerList = careerList;

		} catch (WNoResultException e) {
			// 職歴がない場合は、空の職歴を作成
			form.careerList = new ArrayList<CareerDto>();
			CareerDto dto = new CareerDto();
			dto.companyName = "";
			form.careerList.add(dto);
		}
	}

	protected boolean isEast(String areaCd) {
		return MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(Integer.parseInt(areaCd));
	}
}
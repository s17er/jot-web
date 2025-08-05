package com.gourmetcaree.admin.service.logic.tempMember;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionMessages;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;

import com.google.common.collect.Lists;
import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.admin.service.accessor.tempMember.CareerDtoAccessor;
import com.gourmetcaree.admin.service.accessor.tempMember.DetailAccessor;
import com.gourmetcaree.admin.service.logic.AbstractAdminLogic;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.DBAccessUtilLogic;
import com.gourmetcaree.common.logic.tempMember.SaveLogic;
import com.gourmetcaree.common.property.tempMember.TempMemberProperty;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ShutokenWebAreaKbn;
import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.MMemberArea;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntry;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.entity.TTempJuskill;
import com.gourmetcaree.db.common.entity.member.TTempMember;
import com.gourmetcaree.db.common.entity.member.TTempMemberArea;
import com.gourmetcaree.db.common.entity.member.TTempMemberAttribute;
import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistory;
import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.member.TTempMemberSchoolHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryService;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.JuskillMemberService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.MemberAreaService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.TempJuskillService;
import com.gourmetcaree.db.common.service.member.TempMemberAreaService;
import com.gourmetcaree.db.common.service.member.TempMemberAttributeService;
import com.gourmetcaree.db.common.service.member.TempMemberCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.member.TempMemberCareerHistoryService;
import com.gourmetcaree.db.common.service.member.TempMemberSchoolHistoryService;
import com.gourmetcaree.db.common.service.member.TempMemberService;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;

/**
 * 運営管理用仮会員ロジック
 * @author nakamori
 *
 */
public class TempMemberLogic extends AbstractAdminLogic {

	@Resource
	private MemberService memberService;

	@Resource
	private TempMemberService tempMemberService;

	@Resource
	private TempMemberAttributeService tempMemberAttributeService;

	@Resource
	private TempMemberAreaService tempMemberAreaService;

	@Resource
	private TempMemberSchoolHistoryService tempMemberSchoolHistoryService;

	@Resource(name = "member_tempMemberCareerHistoryService")
	private TempMemberCareerHistoryService careerHistoryService;

	@Resource(name = "member_tempMemberCareerHistoryAttributeService")
	private TempMemberCareerHistoryAttributeService careerHistoryAttrService;

	@Resource
	private SaveLogic saveLogic;

	@Resource
	private DBAccessUtilLogic dbAccessUtilLogic;

	@Resource
	private MemberAreaService memberAreaService;

	@Resource
	private AdvancedRegistrationEntryService advancedRegistrationEntryService;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private MailService mailService;

	@Resource
	private JuskillMemberService juskillMemberService;

	@Resource
	private TempJuskillService tempJuskillService;


	/**
	 * 仮会員編集時のバリデート
	 * @param accessor 詳細データへのアクセサ
	 */
	public <E extends CareerDtoAccessor> ActionMessages  validate(DetailAccessor<E> accessor) {
		ActionMessages errors = new ActionMessages();
		validateMailAddress(errors, accessor);
		return errors;
	}

	/**
	 * メールアドレスのバリデート
	 */
	private void validateMailAddress(ActionMessages errors, DetailAccessor<?> accessor) {
		validateLoginId(errors, accessor);
	}

	/**
	 * 希望勤務地のバリデート
	 * 「こだわらない」と他の値が同時に選ばれていないかチェック
	 */
	private void validateWebArea(ActionMessages errors, DetailAccessor<?> accessor) {
		// 空は有り得ない。また、1件の時はチェックする必要が無い。
		if (ArrayUtils.isEmpty(accessor.getWorkLocation())
				|| accessor.getWorkLocation().length == 1) {
			return;
		}

		// こだわらない が含まれていたらNG
		if (ArrayUtils.contains(accessor.getWorkLocation(), String.valueOf(ShutokenWebAreaKbn.NOT_DECIDE))) {
			ActionMessageUtil.addActionMessage(errors, "errors.notSelectOtherData",
																					"labels.workLocation",
																					"msg.webAreaKbn.notDecide",
																					"msg.otherValue");
		}


	}

	/**
	 * ログインIDのバリデート
	 */
	private void validateLoginId(ActionMessages errors, DetailAccessor<?> accessor) {
		if (memberService.isLoginIdExists(accessor.getLoginId())) {
			ActionMessageUtil.addActionMessage(errors, "errors.duplicatedLoginId");
		}
	}




	/**
	 * 仮会員の詳細を作成
	 * @param accessor  詳細データへのアクセサ
	 * @throws NumberFormatException
	 */
	public <E extends CareerDtoAccessor> void createData(DetailAccessor<E> accessor) throws NumberFormatException {
		TTempMember member = convertmemberData(accessor);
		convertSchoolHistoryData(member.id, accessor);
		convertCareerData(member.id, accessor);

		accessor.setEditable(GourmetCareeUtil.eqInt(MTypeConstants.MemberRegisteredFlg.NOT_REGISTERED, member.memberRegisteredFlg));
	}


	/**
	 * 確認画面で表示するデータを生成
	 * @param accessor
	 */
	public <E extends CareerDtoAccessor> void createConfData(DetailAccessor<E> accessor) {
		accessor.setDispPassword(GourmetCareeUtil.convertPassword(accessor.getPassword()));

		if (StringUtils.isNotEmpty(accessor.getPhoneNo1())) {
			String no = accessor.getPhoneNo1() + "-" + accessor.getPhoneNo2() + "-" + accessor.getPhoneNo3();
			accessor.setPhoneNo(no);
		} else {
			accessor.setPhoneNo("");
		}

		convertCareerDataForConf(accessor);

	}



	/**
	 * 会員情報・会員属性情報をフォームにセット
	 * @param accessor 会員フォーム
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private <E extends CareerDtoAccessor> TTempMember convertmemberData(DetailAccessor<E> accessor) throws NumberFormatException {

		TTempMember member = tempMemberService.findById(NumberUtils.toInt(accessor.getId()));

		//Dateは下で変換
		Beans.copy(member, accessor).excludes("password", "registrationDatetime").execute();
		accessor.setLoginId(member.loginId);

		accessor.setInsertDatatime(DateUtils.getDateStr(member.insertDatetime, GourmetCareeConstants.DATE_TIME_FORMAT));

		// TODO
		// form.registrationDatetime = DateUtils.getDateStr(member.registrationDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
		accessor.setBirthYear(DateUtils.getDateStr(member.birthday, GourmetCareeConstants.YEAR_FORMAT));
		accessor.setBirthMonth(DateUtils.getDateStr(member.birthday, GourmetCareeConstants.SINGLE_MONTH_FORMAT));
		accessor.setBirthDay(DateUtils.getDateStr(member.birthday, GourmetCareeConstants.SINGLE_DAY_FORMAT));


		if (StringUtils.isNotEmpty(member.phoneNo1)) {
			accessor.setPhoneNo(member.phoneNo1 + "-" + member.phoneNo2 + "-" + member.phoneNo3);
		}

		if(MTypeConstants.MemberRegisteredFlg.NOT_REGISTERED == member.memberRegisteredFlg) {
			if(member.applicationId != null) {
				accessor.setRegistrationUrl(
						ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gc.realPath")
						+ "/" + MAreaConstants.Prefix.getRenewalAreaName(member.areaCd)
						+ "/signIn/complete/" + member.id + "/" + member.accessCd);
			} else {
				accessor.setRegistrationUrl(
						ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gc.realPath")
						+ "/" + MAreaConstants.Prefix.getRenewalAreaName(member.areaCd)
						+ "/signIn/entry/" + member.id + "/" + member.accessCd);
			}

		}


		convertAttributeData(member.id, accessor);

		// 会員エリア
		convertAreaData(member.id, accessor);

		return member;
	}


	/**
	 * 属性データを作成
	 */
	private <E extends CareerDtoAccessor> void convertAttributeData(Integer tempMemberId, DetailAccessor<E> accessor) {
		if (tempMemberId == null) {
			return;
		}
		// 会員属性をフォームにセット
		List<String> jobList = new ArrayList<String>();
		List<String> industryList = new ArrayList<String>();
		List<String> qualificationList = new ArrayList<String>();
		List<String> workLocationList = new ArrayList<String>();

		List<TTempMemberAttribute> attrList =
				tempMemberAttributeService.findByTempMemberId(tempMemberId);
		for (TTempMemberAttribute attrEntity : attrList) {

			if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				jobList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				industryList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.QualificationKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				qualificationList.add(String.valueOf(attrEntity.attributeValue));

			} else if (MTypeConstants.ShutokenWebAreaKbn.TYPE_CD.equals(attrEntity.attributeCd)
					|| MTypeConstants.SendaiWebAreaKbn.TYPE_CD.equals(attrEntity.attributeCd)
					|| MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				workLocationList.add(String.valueOf(attrEntity.attributeValue));
			}
		}

		accessor.setJob(jobList.toArray(new String[0]));
		accessor.setIndustry(industryList.toArray(new String[0]));
		accessor.setQualification(qualificationList.toArray(new String[0]));
		accessor.setWorkLocation(workLocationList.toArray(new String[0]));
	}

	/**
	 * エリアデータの作成
	 */
	private <E extends CareerDtoAccessor> void convertAreaData(Integer tempMemberId, DetailAccessor<E> accessor) {
		List<Integer> areaIntList = new ArrayList<Integer>();
		List<String> areaList = new ArrayList<String>();

		List<TTempMemberArea> entityList = tempMemberAreaService.findByTempMemberId(tempMemberId);
		for (TTempMemberArea areaEntity : entityList){
			areaIntList.add(areaEntity.areaCd);
		}
		Collections.sort(areaIntList);
		for (Integer area : areaIntList){
			areaList.add(Integer.toString(area));
		}
		accessor.setAreaList(areaList.toArray(new String[0]));
	}




	/**
	 * 学歴情報をフォームにセット
	 * @param form 会員フォーム
	 */
	private <E extends CareerDtoAccessor> void convertSchoolHistoryData(Integer tempMemberId, DetailAccessor<E> accessor) throws NumberFormatException {
		TTempMemberSchoolHistory tSchoolHistory = tempMemberSchoolHistoryService.findOneByTempMemberId(tempMemberId);

		if (tSchoolHistory != null) {
			Beans.copy(tSchoolHistory, accessor).excludes("id", "version").execute();
		}
	}

	/**
	 * 確認表示用に職歴データを変換
	 */
	private <E extends CareerDtoAccessor> void convertCareerDataForConf(DetailAccessor<E> accessor) {
		List<E> list = accessor.getCareerList();
		if (CollectionUtils.isNotEmpty(list)
				&& list.size() > 1) {

			for (int i = 0; i < list.size(); i++) {
				E dto = list.get(i);
				if (StringUtils.isEmpty(dto.getCompanyName())) {
					list.remove(i);
				}
			}
		}
	}

	/**
	 * 職歴情報をフォームにセット
	 * @param accessor
	 */
	private <E extends CareerDtoAccessor> void convertCareerData(Integer tempMemberId, DetailAccessor<E> accessor) throws NumberFormatException {

		List<TTempMemberCareerHistory> entityList = careerHistoryService.findByTempMemberId(tempMemberId);
		if (CollectionUtils.isEmpty(entityList)) {
			// 職歴がない場合は、空の職歴を作成
			List<E> list = Lists.newArrayList();
			E dto = accessor.newCareerDtoInstance();
			dto.setCompanyName("");
			list.add(dto);
			accessor.setCareerList(list);
			return;
		}


		List<E> careerList = Lists.newArrayList();
		for (TTempMemberCareerHistory entity : entityList) {
			List<String> jobList = new ArrayList<String>();
			List<String> industryList = new ArrayList<String>();
			E dto = accessor.newCareerDtoInstance();
			Beans.copy(entity, dto).execute();

			List<TTempMemberCareerHistoryAttribute> attrList =
					careerHistoryAttrService.findByTempMemberCareerHistoryId(entity.id);
			for (TTempMemberCareerHistoryAttribute attrEntity : attrList) {

				// 職種
				if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
					jobList.add(String.valueOf(attrEntity.attributeValue));
				}

				// 業態
				if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
					industryList.add(String.valueOf(attrEntity.attributeValue));
				}
			}

			dto.setJob(jobList.toArray(new String[0]));
			dto.setIndustry(industryList.toArray(new String[0]));
			careerList.add(dto);
		}

		accessor.setCareerList(careerList);

	}



	/**
	 * 仮会員の更新を行います。
	 */
	public <E extends CareerDtoAccessor, C extends CareerHistoryDtoAccessor>
			void update(DetailAccessor<E> accessor) {
		TempMemberProperty<C> property = createUpdateProperty(accessor);
		saveLogic.update(property);
	}

	/**
	 * 仮会員の更新を行うためのプロパティを生成
	 * @param accessor  詳細データへのアクセサ
	 */
	private <E extends CareerDtoAccessor, C extends CareerHistoryDtoAccessor>
			TempMemberProperty<C> createUpdateProperty(DetailAccessor<E> accessor) {

		TempMemberProperty<C> property = new TempMemberProperty<C>();

		convertMemberUpdateData(accessor, property);

		convertMemberAttributeUpdateData(accessor, property);

		convertSchoolHistoryUpdateData(accessor, property);

		convertCareerHistoryUpdateData(accessor, property);

		property.areaList = Arrays.asList(accessor.getAreaList());


		return property;
	}


	/**
	 * 更新用会員データを生成する
	 */
	private <E extends CareerDtoAccessor, C extends CareerHistoryDtoAccessor>
		void convertMemberUpdateData (DetailAccessor<E> accessor, TempMemberProperty<C> property) {

		TTempMember member = Beans.createAndCopy(TTempMember.class, accessor)
				.excludes(
						"birthDay",	// Form側の名前(誕生日の日)とDBの生年月日が同名のため除外
						MMember.PASSWORD,
						toCamelCase(MMember.REGISTRATION_DATETIME))
				.converter(new ZenkakuKanaConverter()).execute();

		property.member = member;

		member.birthday = GourmetCareeUtil.convertBirthDayData(
													NumberUtils.toInt(accessor.getBirthYear()),
													NumberUtils.toInt(accessor.getBirthMonth()),
													NumberUtils.toInt(accessor.getBirthDay()));

		//property.member.lastUpdateDatetime =  new Timestamp(new Date().getTime());
		member.jobInfoReceptionFlg = member.mailMagazineReceptionFlg;

		if (StringUtils.isNotEmpty(accessor.getPassword())) {
			member.password = DigestUtil.createDigest(accessor.getPassword());
		} else {
			member.password = null;
		}

		member.loginId = accessor.getLoginId();

	}


	/**
	 * 更新用会員属性データを生成する
	 * @param property 会員プロパティ
	 */
	private <E extends CareerDtoAccessor, C extends CareerHistoryDtoAccessor>
		void convertMemberAttributeUpdateData(DetailAccessor<E> accessor, TempMemberProperty<C> property) {

		List<TTempMemberAttribute> attrEntityList = Lists.newArrayList();

		// 希望職種をセット
		for (String job : accessor.getJob()) {
			TTempMemberAttribute attrEntity = new TTempMemberAttribute();
			attrEntity.tempMemberId = property.member.id;
			attrEntity.attributeCd = MTypeConstants.JobKbn.TYPE_CD;
			attrEntity.attributeValue = NumberUtils.toInt(job);
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		// 希望業種をセット
		for (String industry : accessor.getIndustry()) {
			TTempMemberAttribute attrEntity = new TTempMemberAttribute();
			attrEntity.tempMemberId = property.member.id;
			attrEntity.attributeCd = MTypeConstants.IndustryKbn.TYPE_CD;
			attrEntity.attributeValue = NumberUtils.toInt(industry);
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		// 希望勤務地をセット
		for (String workLocation : accessor.getWorkLocation()) {
			TTempMemberAttribute attrEntity = new TTempMemberAttribute();
			attrEntity.tempMemberId = property.member.id;

			int webAreaValue = NumberUtils.toInt(workLocation);
			// [100001]~[199999]:首都圏海外エリア区分
			// [200000]~:仙台勤務地エリア区分
			// その他:首都圏WEBエリア区分
			if (100001 <= webAreaValue && webAreaValue < 200000)  {
				attrEntity.attributeCd = MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD;
			} else if (200000 <= webAreaValue) {
				attrEntity.attributeCd = MTypeConstants.SendaiWebAreaKbn.TYPE_CD;
			} else {
				attrEntity.attributeCd = MTypeConstants.ShutokenWebAreaKbn.TYPE_CD;
			}
			attrEntity.attributeValue = webAreaValue;
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		// 取得資格をセット
		for (String qualification : accessor.getQualification()) {
			TTempMemberAttribute attrEntity = new TTempMemberAttribute();
			attrEntity.tempMemberId = property.member.id;
			attrEntity.attributeCd = MTypeConstants.QualificationKbn.TYPE_CD;
			attrEntity.attributeValue = NumberUtils.toInt(qualification);
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		property.memberAttrList = attrEntityList;
	}


	/**
	 * 更新用学歴データ生成
	 * @param property 会員プロパティ
	 */
	private <E extends CareerDtoAccessor, C extends CareerHistoryDtoAccessor>
	void convertSchoolHistoryUpdateData(DetailAccessor<E> accessor, TempMemberProperty<C> property) {

		property.schoolHistory = new TTempMemberSchoolHistory();
		if (StringUtils.isNotEmpty(accessor.getSchoolName())) {
			Beans.copy(accessor, property.schoolHistory).excludes("id").converter(new ZenkakuKanaConverter()).execute();
			property.schoolHistory.tempMemberId = property.member.id;
		}
	}


	/**
	 * 更新用職歴・職歴属性データ生成
	 * @param property 会員プロパティ
	 */
	private <E extends CareerDtoAccessor, C extends CareerHistoryDtoAccessor>
				void convertCareerHistoryUpdateData(DetailAccessor<E> accessor, TempMemberProperty<C> property) {

		List<TTempMemberCareerHistory> entityList = Lists.newArrayList();

		for (E dto : accessor.getCareerList()) {
			if (StringUtils.isEmpty(dto.getCompanyName())) {
				continue;
			}
			TTempMemberCareerHistory entity = new TTempMemberCareerHistory();
			List<TTempMemberCareerHistoryAttribute> attrEntityList = Lists.newArrayList();
			Beans.copy(dto, entity).excludes("id").converter(new ZenkakuKanaConverter()).execute();
//			entity.memberId = property.member.id;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			// 職種
			for (String job : dto.getJob()) {
				TTempMemberCareerHistoryAttribute attrEntity = new TTempMemberCareerHistoryAttribute();
				attrEntity.attributeCd = MTypeConstants.JobKbn.TYPE_CD;
				attrEntity.attributeValue = NumberUtils.toInt(job);
				attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
				attrEntityList.add(attrEntity);
			}

			// 業態
			for (String industry : dto.getIndustry()) {
				TTempMemberCareerHistoryAttribute attrEntity = new TTempMemberCareerHistoryAttribute();
				attrEntity.attributeCd = MTypeConstants.IndustryKbn.TYPE_CD;
				attrEntity.attributeValue = NumberUtils.toInt(industry);
				attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
				attrEntityList.add(attrEntity);
			}

			entity.tCareerHistoryAttributeList = attrEntityList;
			entityList.add(entity);
		}

		property.careerHistoryList = entityList;
	}


	/**
	 * 仮会員の削除
	 */
	public void delete(DetailAccessor<?> accessor) {
		if (!NumberUtils.isDigits(accessor.getId())) {
			throw new FraudulentProcessException("IDが不正です。 ID:" + accessor.getId());
		}

		try {
			TTempMember entity = tempMemberService.findById(Integer.parseInt(accessor.getId()));
			tempMemberService.logicalDelete(entity);
			logicLog.info(String.format("仮会員を削除しました。ID:%s", accessor.getId()));
		} catch (SNoResultException e) {
			logicLog.warn(String.format("仮会員が見つかりませんでした。ID:%s", accessor.getId()), e);
		}
	}

	/**
	 * 仮会員を本登録します
	 * @param tempMemberId
	 */
	public void signIn(Integer tempMemberId) {
		try {
		TTempMember entity = tempMemberService.findById(tempMemberId);

		// 既に会員登録されている場合は処理を終了させる
		if(entity.memberRegisteredFlg.equals(MTypeConstants.MemberRegisteredFlg.REGISTERED)) {
			return;
		}

		// 会員登録
		MMember member = setMemberPropatys(entity);
		memberService.insert(member);

		// 会員エリアを登録
		List<TTempMemberArea> entityArea = tempMemberAreaService.findByTempMemberId(entity.id);
		entityArea.forEach(action -> {
			MMemberArea area = new MMemberArea();
			area.memberId = member.id;
			area.areaCd = action.areaCd;
			memberAreaService.insert(area);
		});

		// 仮会員を更新
		entity.memberRegisteredFlg = MTypeConstants.MemberRegisteredFlg.REGISTERED;
		entity.memberId = member.id;
		tempMemberService.update(entity);

		// 事前登録の紐付け
		SimpleWhere where = new SimpleWhere().eq("loginId", entity.loginId);
		try {
			List<TAdvancedRegistrationEntry> list = advancedRegistrationEntryService.findByCondition(where);
			list.forEach(action -> {
				action.advancedRegistrationUserId = member.id;
				advancedRegistrationEntryService.update(action);
			});
		} catch (WNoResultException e) {
			// 事前登録無しなので処理を行わない
		}

		// 応募データとの紐付け
		if(Objects.nonNull(entity.applicationId)) {
			TApplication application = applicationService.findById(entity.applicationId);
			if(Objects.nonNull(application)) {
				application.memberFlg = MTypeConstants.MemberFlg.MEMBER;
				application.memberId = member.id;
				applicationService.update(application);

				// 仮会員時にやり取りしたメールを会員メールに変更
				SimpleWhere memberMailWhere = new SimpleWhere()
						.eq("applicationId", application.id)
						.eq("senderKbn", MTypeConstants.SenderKbn.NO_MEMBER);
				List<TMail> memberMailList = mailService.findByCondition(memberMailWhere);
				memberMailList.forEach(mail -> {
					mail.senderKbn = MTypeConstants.SenderKbn.MEMBER;
					mail.fromId = application.memberId;
					mailService.update(mail);
				});

				SimpleWhere customerMailWhere = new SimpleWhere()
						.eq("applicationId", application.id)
						.eq("senderKbn", MTypeConstants.SenderKbn.CUSTOMER);
				List<TMail> customerMailList = mailService.findByCondition(customerMailWhere);
				customerMailList.forEach(mail -> {
					mail.toId = application.memberId;
					mailService.update(mail);
				});

			}
		}

		// ジャスキル登録
		if(member.juskillFlg == MTypeConstants.JuskillFlg.HOPE) {
			TTempJuskill tempJuskill = tempJuskillService.findByCondition(new SimpleWhere().eq("tempMemberId", entity.id)).get(0);
			juskillMemberService.insert(setJuskillMemberPropartys(member, tempJuskill));
		}

		} catch (SNoResultException e) {
			logicLog.warn(String.format("仮会員が見つかりませんでした。ID:%s", tempMemberId), e);
			throw new FraudulentProcessException("仮会員が見つかりませんでした。");
		} catch (WNoResultException e) {
			logicLog.warn(String.format("応募メールが見つかりませんでした。ID:%s", tempMemberId), e);
		}
	}

	/**
	 * 本会員登録に必要なデータをセットする
	 * @param entity 仮会員
	 * @return
	 */
	private MMember setMemberPropatys(TTempMember entity) {
		MMember member = new MMember();
		String token;

		do {
			token = RandomStringUtils.randomAlphanumeric(6);
		}while(memberService.existMailMagazineToken(token));

		Beans.copy(entity, member).excludes("id", "version", "insert_datetime", "update_datetime").execute();

		member.mailMagazineToken = token;

		if(Integer.valueOf(MTypeConstants.MailMagazineReceptionFlg.RECEPTION).equals(member.mailMagazineReceptionFlg)) {
			member.deliveryStatus = MTypeConstants.deliveryStatus.NOT_SEND;
		}

		return member;
	}

	/**
	 * ジャスキル登録に必要な値をセットする
	 * @param member
	 * @return
	 */
	private MJuskillMember setJuskillMemberPropartys(MMember member, TTempJuskill tempJuskill) {
		MJuskillMember juskillMember = new MJuskillMember();

		juskillMember.memberId = member.id;
		juskillMember.juskillEntryDate = new Date(System.currentTimeMillis());
		juskillMember.juskillMemberName = member.memberName;
		juskillMember.birthday = new Date(member.birthday.getTime());
		juskillMember.sexKbn = member.sexKbn;
		juskillMember.zipCd = member.zipCd;
		juskillMember.prefecturesCd = member.prefecturesCd;
		juskillMember.address = member.municipality + member.address;
		juskillMember.mail = member.loginId;

		if(StringUtils.isNotBlank(member.phoneNo1) && StringUtils.isNotBlank(member.phoneNo2) && StringUtils.isNotBlank(member.phoneNo3)) {
			juskillMember.phoneNo1 = member.phoneNo1 + "-" + member.phoneNo2 + "-" + member.phoneNo3;
		}

		juskillMember.entryPath = "無料転職相談 登録フォーム";

		String password;

		do {
			password = RandomStringUtils.randomAlphanumeric(8);
		}while(!juskillMemberService.existJuskillPassword(password));

		juskillMember.password = password;

		Beans.copy(tempJuskill, juskillMember).excludes("id").execute();

		return juskillMember;
	}
}

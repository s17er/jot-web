package com.gourmetcaree.admin.pc.member.action.member;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.admin.pc.member.form.member.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.JuskillLogic;
import com.gourmetcaree.admin.service.logic.MemberLogic;
import com.gourmetcaree.admin.service.property.MemberProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.entity.MJuskillMemberCareerHistory;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.MMemberAttribute;
import com.gourmetcaree.db.common.entity.MMemberHopeCity;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TSchoolHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.service.JuskillMemberCareerHistoryService;
import com.gourmetcaree.db.common.service.JuskillMemberService;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;

/**
 * 会員管理編集アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class EditAction extends MemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** 編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** 会員ロジック */
	@Resource
	protected MemberLogic memberLogic;

	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	protected JuskillLogic juskillLogic;

	@Resource
	protected JuskillMemberService juskillMemberService;

	@Resource
	protected JuskillMemberCareerHistoryService juskillMemberCareerHistoryService;
	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}", input = TransitionConstants.Member.JSP_APH01E01)
	@MethodAccess(accessCode="MEMBER_EDIT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Member.JSP_APH01E01, reset = "resetMultibox")
	@MethodAccess(accessCode="MEMBER_EDIT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// idが数値かどうかチェック
		if (!StringUtils.isNumeric(editForm.id)) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// 編集データを取得できているかチェック
		if (!editForm.existDataFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// 確認画面用にデータを生成する
		convertConfDispData();

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();
		if(StringUtils.isNotBlank(editForm.gourmetMagazineReceptionFlg)) {
			if (Integer.valueOf(editForm.gourmetMagazineReceptionFlg) == MTypeConstants.gourmetMagazineReceptionFlg.NOT_DESIRE) {
				editForm.deliveryStatus = null;
			} else if (Integer.valueOf(editForm.gourmetMagazineReceptionFlg) == MTypeConstants.gourmetMagazineReceptionFlg.DESIRE && editForm.deliveryStatus == null) {
				editForm.deliveryStatus = String.valueOf(MTypeConstants.deliveryStatus.NOT_SEND);
			}
		} else {
			editForm.deliveryStatus = String.valueOf(MTypeConstants.deliveryStatus.NOT_SEND);
		}

		// 確認画面へ遷移
		return TransitionConstants.Member.JSP_APH01E02;
	}

	/**
	 * 確認画面用にデータを生成する
	 */
	private void convertConfDispData() {

		// パスワードを確認画面表示用に変換
		editForm.dispPassword = GourmetCareeUtil.convertPassword(editForm.password);

		// 電話番号を生成
		if (StringUtils.isNotEmpty(editForm.phoneNo1)) {
			editForm.phoneNo = editForm.phoneNo1 + "-" + editForm.phoneNo2 + "-" + editForm.phoneNo3;
		}


		// 職歴データを編集
		editCareerData();

		// 希望勤務地を変換
		converDisptHopeCity(editForm, editForm.hopeCityCdList);
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBER_EDIT_BACK")
	public String back() {
		// 確認画面へ遷移
		return TransitionConstants.Member.REDIRECT_MEMBER_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBER_EDIT_CORRECT")
	public String correct() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Member.JSP_APH01E01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Member.JSP_APH01E01)
	@MethodAccess(accessCode="MEMBER_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!editForm.id.equals(editForm.hiddenId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// 登録処理
		MemberProperty property = doUpdate();

		log.debug("会員データをUPDATEしました。");

		// ジャスキル登録処理
		if(Objects.nonNull(editForm.entryJuskillFlg) && Integer.valueOf(editForm.entryJuskillFlg) == 1) {
			signInJuskillMember(property);
			memberService.updateJuskillFlg(property.member.id);
			log.debug("ジャスキルデータをINSERTしました。");
		}

		return TransitionConstants.Member.REDIRECT_MEMBER_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBER_EDIT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Member.JSP_APH01E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// 表示データを取得
		convertDispData(editForm);

		// プレゼント希望欄表示判定
		editForm.gourmetMagazineReceptionDisplayFlg = isEast(editForm.areaCd);

		// チェック用のIdに値を保持
		editForm.hiddenId = editForm.id;

		// 登録画面へ遷移
		return TransitionConstants.Member.JSP_APH01E01;
	}

	/**
	 * 削除された職歴データをリストから削除
	 */
	private void editCareerData() {

		if (editForm.careerList.size() > 1) {
			for (int i = 0; i < editForm.careerList.size(); i++) {
				CareerDto dto = editForm.careerList.get(i);
				if (StringUtils.isEmpty(dto.companyName)) {
					editForm.careerList.remove(i);
				}
			}
		}
	}

	/**
	 * 会員データ更新処理を行う
	 */
	private MemberProperty doUpdate() {

		MemberProperty property = new MemberProperty();

		// 更新用データを生成
		createUpdateData(property);

		// 更新
		memberLogic.updateMemberData(property);

		return property;
	}

	/**
	 * 会員情報更新データを生成
	 * @param property 会員プロパティ
	 */
	private void createUpdateData(MemberProperty property) {

		// 更新用会員データ生成
		convertMemberUpdateData(property);

		// 更新用会員属性データ生成
		convertMemberAttributeUpdateData(property);

		// 更新用学歴データ生成
		convertSchoolHistoryUpdateData(property);

		// 更新用職歴・職歴属性データ生成
		convertCareerHistoryUpdateData(property);

		// 更新用希望勤務地
		conbertHopeCityUpdateData(property);
	}

	/**
	 * 更新用会員データを生成する
	 * @param property 会員プロパティ
	 */
	private void convertMemberUpdateData(MemberProperty property) {

		property.member = new MMember();
		Beans.copy(editForm, property.member)
				.excludes(
						"birthDay",	// Form側の名前(誕生日の日)とDBの生年月日が同名のため除外
						MMember.PASSWORD,
						toCamelCase(MMember.ADMIN_PASSWORD),
						toCamelCase(MMember.REGISTRATION_DATETIME))
				.converter(new ZenkakuKanaConverter()).execute();
		property.member.birthday = convertBirthDayData();
		property.member.lastUpdateDatetime =  new Timestamp(new Date().getTime());
		property.member.jobInfoReceptionFlg = property.member.mailMagazineReceptionFlg;

		// 管理者のみ管理者用パスワードを更新する
		if (ManageAuthLevel.ADMIN.value().equals(userDto.authLevel) && StringUtils.isNotBlank(editForm.adminPassword)) {
			property.member.adminPassword = editForm.adminPassword;
		}

		if (StringUtils.isNotEmpty(editForm.password)) {
			property.member.password = DigestUtil.createDigest(editForm.password);
		} else {
			property.member.password = null;
		}
	}

	/**
	 * 更新用会員属性データを生成する
	 * @param property 会員プロパティ
	 */
	private void convertMemberAttributeUpdateData(MemberProperty property) {

		List<MMemberAttribute> attrEntityList = new ArrayList<MMemberAttribute>();

		// 希望職種をセット
		for (String job : editForm.job) {
			MMemberAttribute attrEntity = new MMemberAttribute();
			attrEntity.memberId = property.member.id;
			attrEntity.attributeCd = MTypeConstants.JobKbn.TYPE_CD;
			attrEntity.attributeValue = NumberUtils.toInt(job);
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		// 希望業種をセット
		for (String industry : editForm.industry) {
			MMemberAttribute attrEntity = new MMemberAttribute();
			attrEntity.memberId = property.member.id;
			attrEntity.attributeCd = MTypeConstants.IndustryKbn.TYPE_CD;
			attrEntity.attributeValue = NumberUtils.toInt(industry);
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		// リニューアル移行は使わないので編集不可とする
//		// 希望勤務地をセット
//		for (String workLocation : editForm.workLocation) {
//			MMemberAttribute attrEntity = new MMemberAttribute();
//			attrEntity.memberId = property.member.id;
//
//			int webAreaValue = NumberUtils.toInt(workLocation);
//			// [100001]~[199999]:首都圏海外エリア区分
//			// [200000]~:仙台勤務地エリア区分
//			// その他:首都圏WEBエリア区分
//			if (100001 <= webAreaValue && webAreaValue < 200000)  {
//				attrEntity.attributeCd = MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD;
//			} else if (200000 <= webAreaValue) {
//				attrEntity.attributeCd = MTypeConstants.SendaiWebAreaKbn.TYPE_CD;
//			} else {
//				attrEntity.attributeCd = MTypeConstants.ShutokenWebAreaKbn.TYPE_CD;
//			}
//			attrEntity.attributeValue = webAreaValue;
//			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
//
//			attrEntityList.add(attrEntity);
//		}

		// 取得資格をセット
		for (String qualification : editForm.qualification) {
			MMemberAttribute attrEntity = new MMemberAttribute();
			attrEntity.memberId = property.member.id;
			attrEntity.attributeCd = MTypeConstants.QualificationKbn.TYPE_CD;
			attrEntity.attributeValue = NumberUtils.toInt(qualification);
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		// 雇用形態をセット
		for (String employPtnKbn : editForm.employPtnKbns) {
			MMemberAttribute attrEntity = new MMemberAttribute();
			attrEntity.memberId = property.member.id;
			attrEntity.attributeCd = MTypeConstants.EmployPtnKbn.TYPE_CD;
			attrEntity.attributeValue = NumberUtils.toInt(employPtnKbn);
			attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			attrEntityList.add(attrEntity);
		}

		property.mMemberAttributeList = attrEntityList;
	}

	/**
	 * 更新用学歴データ生成
	 * @param property 会員プロパティ
	 */
	private void convertSchoolHistoryUpdateData(MemberProperty property) {

		property.tSchoolHistory = new TSchoolHistory();
		if (StringUtils.isNotEmpty(editForm.schoolName)) {
			Beans.copy(editForm, property.tSchoolHistory).excludes("id").converter(new ZenkakuKanaConverter()).execute();
			property.tSchoolHistory.memberId = property.member.id;
		}
	}

	/**
	 * 更新用希望勤務地生成
	 * @param property
	 */
	private void conbertHopeCityUpdateData(MemberProperty property) {
		property.mMemberHopeCityList = new ArrayList<MMemberHopeCity>();
		editForm.hopeCityCdList.stream().forEach(cityCd -> {
			MMemberHopeCity entity = new MMemberHopeCity();
			entity.memberId = Integer.parseInt(editForm.id);
			entity.cityCd = cityCd;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			property.mMemberHopeCityList.add(entity);
		});
	}

	/**
	 * 更新用職歴・職歴属性データ生成
	 * @param property 会員プロパティ
	 */
	private void convertCareerHistoryUpdateData(MemberProperty property) {

		List<TCareerHistory> entityList = new ArrayList<TCareerHistory>();

		for (CareerDto dto : editForm.careerList) {
			if (StringUtils.isEmpty(dto.companyName)) {
				continue;
			}
			TCareerHistory entity = new TCareerHistory();
			List<TCareerHistoryAttribute> attrEntityList = new ArrayList<TCareerHistoryAttribute>();
			Beans.copy(dto, entity).excludes("id").converter(new ZenkakuKanaConverter()).execute();
			entity.memberId = property.member.id;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			// 職種
			for (String job : dto.job) {
				TCareerHistoryAttribute attrEntity = new TCareerHistoryAttribute();
				attrEntity.attributeCd = MTypeConstants.JobKbn.TYPE_CD;
				attrEntity.attributeValue = NumberUtils.toInt(job);
				attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
				attrEntityList.add(attrEntity);
			}

			// 業態
			for (String industry : dto.industry) {
				TCareerHistoryAttribute attrEntity = new TCareerHistoryAttribute();
				attrEntity.attributeCd = MTypeConstants.IndustryKbn.TYPE_CD;
				attrEntity.attributeValue = NumberUtils.toInt(industry);
				attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
				attrEntityList.add(attrEntity);
			}

			entity.tCareerHistoryAttributeList = attrEntityList;
			entityList.add(entity);
		}

		property.tCareerHistoryList = entityList;
	}

	/**
	 * 入力された生年月日をTimestampへ変換
	 * @return
	 */
	private Timestamp convertBirthDayData() {

		Calendar cal = Calendar.getInstance();
		cal.set(NumberUtils.toInt(editForm.birthYear), NumberUtils.toInt(editForm.birthMonth)-1, NumberUtils.toInt(editForm.birthDay ));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * ジャスキル関連の登録処理を行う
	 * @param memberProperty
	 */
	private void signInJuskillMember(MemberProperty memberProperty) {

		MJuskillMember juskillMember = new MJuskillMember();

		createJuskillMember(juskillMember, memberProperty);

		int juskillMemberId = juskillMemberService.findByMemberId(memberProperty.member.id).id;

		createJuskillHistory(juskillMemberId, memberProperty.tCareerHistoryList);
	}

	/**
	 * ジャスキル登録
	 * @param juskillMember
	 * @param memberProperty
	 * @return
	 */
	private void createJuskillMember(MJuskillMember juskillMember, MemberProperty memberProperty) {

		juskillMember.memberId = memberProperty.member.id;
		juskillMember.juskillEntryDate = new java.sql.Date(System.currentTimeMillis());
		juskillMember.juskillMemberName = memberProperty.member.memberName;
		juskillMember.birthday = new java.sql.Date(memberProperty.member.birthday.getTime());
		juskillMember.sexKbn = memberProperty.member.sexKbn;
		juskillMember.zipCd = memberProperty.member.zipCd;
		juskillMember.prefecturesCd = memberProperty.member.prefecturesCd;
		juskillMember.password = juskillLogic.createJuskillPassword();

		// 希望業態、希望職種、取得資格をセット
		if(CollectionUtils.isNotEmpty(memberProperty.mMemberAttributeList)) {
			List<String> industryList = new ArrayList<>();
			List<String> jobList = new ArrayList<>();
			List<String> qualificationList = new ArrayList<>();
			for(MMemberAttribute attribute : memberProperty.mMemberAttributeList) {
				if(attribute.attributeCd.equals("industry_kbn")) {
					industryList.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.IndustryKbn.TYPE_CD, attribute.attributeValue));
				}
				if(attribute.attributeCd.equals("job_kbn")) {
					jobList.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD, attribute.attributeValue));
				}
				if(attribute.attributeCd.equals("qualification_kbn")) {
					qualificationList.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.QualificationKbn.TYPE_CD, attribute.attributeValue));
				}
			}
			if(!industryList.isEmpty()) {
				juskillMember.hopeIndustry = String.join("、", industryList);
			}
			if(!jobList.isEmpty()) {
				juskillMember.hopeJob = String.join("、", jobList);
			}
			if(!qualificationList.isEmpty()) {
				juskillMember.qualification = String.join("、", qualificationList);
			}
		}

		// 経験をセット
		List<String> exp = new ArrayList<>();
		if(Objects.nonNull(memberProperty.member.foodExpKbn)) {
			exp.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.FoodExpKbn.TYPE_CD, memberProperty.member.foodExpKbn));
		}
		if(Objects.nonNull(memberProperty.member.expManagerKbn)) {
			exp.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.ExpManagerKbn.TYPE_CD, memberProperty.member.expManagerKbn));
		}
		if(Objects.nonNull(memberProperty.member.expManagerYearKbn)) {
			exp.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.ExpManagerYearKbn.TYPE_CD, memberProperty.member.expManagerYearKbn));
		}
		if(Objects.nonNull(memberProperty.member.expManagerPersonsKbn)) {
			exp.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.ExpManagerPersonsKbn.TYPE_CD, memberProperty.member.expManagerPersonsKbn));
		}
		juskillMember.experience = String.join(" ", exp);

		if(Objects.nonNull(memberProperty.member.salaryKbn)) {
			juskillMember.hopeSalary = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalaryKbn.TYPE_CD, memberProperty.member.salaryKbn);
		}

		juskillMember.address = memberProperty.member.municipality + memberProperty.member.address;

        if(StringUtils.isNotBlank(memberProperty.member.phoneNo1) && StringUtils.isNotBlank(memberProperty.member.phoneNo2)
        		&& StringUtils.isNotBlank(memberProperty.member.phoneNo3)) {
        	juskillMember.phoneNo1 = memberProperty.member.phoneNo1 + "-" + memberProperty.member.phoneNo2 + "-" + memberProperty.member.phoneNo3;
        }

        juskillMember.mail = memberProperty.member.loginId;

        // 学歴をセット
        List<String> school = new ArrayList<>();
        if(StringUtils.isNotBlank(memberProperty.tSchoolHistory.schoolName)) {
        	school.add(memberProperty.tSchoolHistory.schoolName);
        }
        if(StringUtils.isNotBlank(memberProperty.tSchoolHistory.department)) {
        	school.add(memberProperty.tSchoolHistory.department);
        }
        if(Objects.nonNull(memberProperty.tSchoolHistory.graduationKbn)) {
        	school.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.GraduationKbn.TYPE_CD, memberProperty.tSchoolHistory.graduationKbn));
        }
        juskillMember.finalSchoolHistory = String.join("　", school);

        juskillMember.entryPath = "管理画面からの登録";

        juskillMemberService.insert(juskillMember);
	}

	/**
	 * ジャスキル会員の職歴を登録
	 * @param juskillMemberId
	 * @param tCareerHistoryList
	 */
	private void createJuskillHistory(int juskillMemberId, List<TCareerHistory> tCareerHistoryList) {

        if(!tCareerHistoryList.isEmpty()) {
        	for(int i = 0; i < 4; i++) {
            	MJuskillMemberCareerHistory juskillMemberHistory = new MJuskillMemberCareerHistory();
            	juskillMemberHistory.juskillMemberId = juskillMemberId;
        		List<String> career = new ArrayList<>();
        		try {
        			TCareerHistory history = tCareerHistoryList.get(i);
        			if(StringUtils.isNotBlank(history.companyName)) {
        				career.add("会社名:" + history.companyName);
        			}
        			if(StringUtils.isNotBlank(history.workTerm)) {
        				career.add("勤務期間:" + history.workTerm);
        			}
        			if(StringUtils.isNotBlank(history.businessContent)) {
        				career.add("事業内容:" + history.businessContent);
        			}
        			if(StringUtils.isNotBlank(history.seat)) {
        				career.add("座席数:" + history.seat);
        			}
        			if(StringUtils.isNotBlank(history.monthSales)) {
        				career.add("月売上:" + history.monthSales);
        			}
        			juskillMemberHistory.careerHistory = String.join(",", career);
        			juskillMemberCareerHistoryService.insert(juskillMemberHistory);
        		} catch(IndexOutOfBoundsException e) {
        			break;
        		}
        	}
        }
	}

}
package com.gourmetcaree.admin.pc.member.form.member;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.admin.pc.juskill.form.juskillMember.JuskillMemberForm;
import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;

import jp.co.whizz_tech.common.sastruts.annotation.EmailType;
import jp.co.whizz_tech.common.sastruts.annotation.KatakanaType;
import jp.co.whizz_tech.common.sastruts.annotation.NotWhiteSpaceOnly;
import jp.co.whizz_tech.common.sastruts.annotation.ZipType;

/**
 * 会員管理のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class MemberForm extends BaseForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4694288295805475063L;

	/** 会員ID */
	public String id;

	/** 登録日時 */
	public String insertDatatime;

	public String registrationDatetime;

	/** 最終ログイン日時 */
	public String lastLoginDatetime;

	/** 会員名 */
	@Required
	public String memberName;

	/** 会員名(カナ) */
	@Required
	@KatakanaType(markFlg=true)
	public String memberNameKana;

	/** エリアコード */
	public String areaCd;

	/** エリアコードリスト 会員エリアテーブル */
	public String[] areaList;

	/** ログインメールアドレス */
	public String loginId;

	/** サブメールアドレス */
	@EmailType
	public String subMailAddress;

	/** 生年月日(年) */
	public String birthYear;

	/** 生年月日(月) */
	public String birthMonth;

	/** 生年月日(日) */
	public String birthDay;

	/** パスワード */
	@NotWhiteSpaceOnly
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.passMaxLimit"), arg0 = @Arg(key = "6", resource = false))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.passMinLimit"), arg0 = @Arg(key = "20", resource = false))
	public String password;

	/** パスワード(確認) */
	@NotWhiteSpaceOnly
	public String rePassword;

	/** 表示用パスワード */
	public String dispPassword;

	/** 性別区分 */
	public String sexKbn;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** 電話番号 */
	public String phoneNo;

	/** 郵便番号 */
	@ZipType
	public String zipCd;

	/** 都道府県 */
	@Required
	public String prefecturesCd;

	/** 市区町村 */
	@Required
	public String municipality;

	/** 住所 */
	public String address;

	/** 資格 */
	public String[] qualification;

	/** 取得資格その他 */
	public String qualificationOther;

	/** 飲食業界経験年数区分 */
	public String foodExpKbn;

	/** 海外勤務経験区分 */
	public String foreignWorkFlg;

	/** スカウト自己PR */
	public String scoutSelfPr;

	/** 応募自己PR */
	public String applicationSelfPr;

	/** 新着求人情報受信フラグ */
	public String jobInfoReceptionFlg;

	/** メルマガ受信フラグ */
	@Required
	public String mailMagazineReceptionFlg;

	/** スカウトメール受信フラグ */
	@Required
	public String scoutMailReceptionFlg;

	/** 希望職種区分 */
	public String[] job;

	/** 希望業種区分 */
	public String[] industry;

	/** 希望勤務地区分 */
	public String[] workLocation;

	/** 希望雇用形態区分 */
	public String[] employPtnKbns;

	/** 転勤フラグ */
	public String transferFlg;

	/** 深夜勤務フラグ */
	public String midnightShiftFlg;

	/** 希望年収区分 */
	public String salaryKbn;

	/** 希望年収区分名 */
	public String salaryKbnName;

	/** 学校名 */
	public String schoolName;

	/** 学部・学科 */
	public String department;

	/** 状況 */
	public String graduationKbn;

	/** 職歴リスト */
	public List<CareerDto> careerList;

	/** ジャスキル登録フラグ */
	@Required
	public String juskillFlg;

	/** PCメール配信停止フラグ */
	@Required
	public String pcMailStopFlg;

	/** モバイルメール配信停止フラグ */
	@Required
	public String mobileMailStopFlg;

	/** 端末区分 */
	public String terminalKbn;

	/** 会員区分 */
	@Required
	public String memberKbn;

	/** 会員マスタバージョン */
	public Long version;

	/** 事前登録のリスト */
	public List<MAdvancedRegistration> advancedRegistrationList;

	/** 会員情報マップ */
	public Map<Integer, com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.DetailForm> advancedRegistrationDtoMemberMap;

	/** 会員情報の有無 */
	public Boolean advanceRegistrationFlg = false;

	/** ジャスキルフォーム */
	public JuskillMemberForm juskillMemberForm;

	/** ジャスキルの有無 */
	public Boolean juskillInfoFlg = false;

	/** ジャスキル連絡フラグ（グルメキャリー転職相談窓口） */
	public String juskillContactFlg;

	/** 希望勤務地 */
	public List<String> hopeCityCdList = new ArrayList<>();

	/** 希望勤務地表示用(都道府県コード: 市区町村コードリスト) */
	public Map<Integer, List<String>> hopeCityListMap;

	/** グルメキャリー雑誌受け取りフラグ */
	public String gourmetMagazineReceptionFlg;

	/** 配送状況 */
	public String deliveryStatus;

	/** プレゼント希望表示フラグ */
	public boolean gourmetMagazineReceptionDisplayFlg;

	/** 管理者用パスワード */
	public String adminPassword;

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		super.resetBaseForm();
		id = null;
		insertDatatime = null;
		lastLoginDatetime = null;
		memberName = null;
		memberNameKana = null;
		areaCd = null;
		areaList = null;
		loginId = null;
		subMailAddress = null;
		birthYear = null;
		birthMonth = null;
		birthDay = null;
		password = null;
		rePassword = null;
		dispPassword = null;
		sexKbn = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		phoneNo = null;
		zipCd = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		qualification = null;
		foodExpKbn = null;
		foreignWorkFlg = null;
		scoutSelfPr = null;
		applicationSelfPr = null;
		jobInfoReceptionFlg = null;
		mailMagazineReceptionFlg = null;
		scoutMailReceptionFlg = null;
		job = null;
		industry = null;
		workLocation = null;
		employPtnKbns = null;
		transferFlg = null;
		midnightShiftFlg = null;
		salaryKbn = null;
		salaryKbnName = null;
		schoolName = null;
		department = null;
		graduationKbn = null;
		careerList = null;
		juskillFlg = null;
		pcMailStopFlg = null;
		mobileMailStopFlg = null;
		terminalKbn = null;
		memberKbn = null;
		version = null;
		registrationDatetime = null;
		advancedRegistrationList = null;
		advancedRegistrationDtoMemberMap = null;
		advanceRegistrationFlg = false;
		juskillInfoFlg = false;
		juskillContactFlg = null;
		hopeCityCdList = new ArrayList<>();
		hopeCityListMap = new LinkedHashMap<>();
		gourmetMagazineReceptionFlg = null;
		deliveryStatus = null;
		gourmetMagazineReceptionDisplayFlg = false;
		adminPassword = null;
	}

	public boolean isWorkLocationExist () {
		return !ArrayUtils.isEmpty(workLocation);
	}
}
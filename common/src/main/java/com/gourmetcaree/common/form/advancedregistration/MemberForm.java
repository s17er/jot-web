package com.gourmetcaree.common.form.advancedregistration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.util.MessageResourcesUtil;

import com.google.common.collect.Lists;
import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.logic.DBAccessUtilLogic;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;

import jp.co.whizz_tech.commons.WztValidateUtil;

/**
 * 事前登録会員を変更・登録をするためのアクションフォーム
 * Created by ZRX on 2017/06/08.
 */
public abstract class MemberForm<CAREER extends CareerHistoryDtoAccessor> extends BaseForm {


    private static final long serialVersionUID = 4341221926154504555L;

    /** 自己PR最大文字数 */
    public static final int SELF_PR_MAX_LENGTH = 800;

    private static final Logger formLog = Logger.getLogger(MemberForm.class);
    /**
     * 事前登録エントリユーザID
     */
    public String id;

    /**
     * 登録日時
     */
    public String registrationDatetime;

    /** 来場ステータス */
    public String attendedStatus;

    /**
     * 会員名
     */
    public String memberName;

    /**
     * 会員名（カナ）
     */
    public String memberNameKana;

    /**
     * エリアコード
     */
    public Integer areaCd;

    /**
     * PCメールアドレス
     */
    public String pcMail;

    /**
     * 携帯メールアドレス
     */
    public String mobileMail;

    /**
     * 誕生年
     */
    public String birthYear;

    /**
     * 誕生月
     */
    public String birthMonth;

    /**
     * 誕生日
     * これは日にちのみのやつ。
     */
    public String birthDayDay;
    /**
     * 生年月日
     * これは、yyyy/MM/ddの文字列にしたやつ
     */
    public String birthday;

    /**
     * 性別区分
     */
    public String sexKbn;

    /**
     * 電話番号1
     */
    public String phoneNo1;

    /**
     * 電話番号2
     */
    public String phoneNo2;

    /**
     * 電話番号3
     */
    public String phoneNo3;

    /**
     * 郵便番号
     */
    public String zipCd;

    /**
     * 都道府県コード
     */
    public String prefecturesCd;

    /**
     * 市区町村
     */
    public String municipality;

    /**
     * 住所
     */
    public String address;

    /**
     * 雇用形態区分
     */
    public Integer employPtnKbn;

    /**
     * 転勤フラグ
     */
    public Integer transferFlg;

    /**
     * 深夜勤務フラグ
     */
    public Integer midnightShiftFlg;

    /**
     * 希望年収区分
     */
    public Integer salaryKbn;

    /**
     * 現在（前職）の年収 
     */
    public String workSalary;

    /**
     * 事前登録用自己PR
     */
    public String advancedRegistrationSelfPr;

    /**
     * 転職先に望むこと
     */
    public String hopeCareerChangeText;

    /**
     * 転職希望時期テキスト
     */
    public String hopeCareerChangeStr;

    /**
     * 新着求人情報受信フラグ
     */
    public Integer jobInfoReceptionFlg;

    /**
     * メルマガ受信フラグ
     */
    public Integer mailMagazineReceptionFlg;

    /**
     * スカウトメール受信フラグ
     */
    public Integer scoutMailReceptionFlg;

    /**
     * 事前会員メルマガ受信フラグ
     */
    public String advancedMailMagazineReceptionFlg;

    /**
     * PCメール配信停止フラグ
     */
    public String pcMailStopFlg;

    /**
     * モバイルメール配信停止フラグ
     */
    public String mobileMailStopFlg;

    /**
     * 端末区分
     */
    public Integer terminalKbn;

    /**
     * 事前登録エントリログインID
     */
    public String loginId;

    /**
     * 会員区分
     */
    public Integer memberKbn;

    /**
     * 職歴リスト
     */
    public List<CAREER> careerList;

    /**
     * 学校名
     */
    public String schoolName;

    /**
     * 学部・学科
     */
    public String department;

    /**
     * 状況区分
     */
    public String graduationKbn;

    /**
     * 現在の状況 区分
     */
    public String currentSituationKbn;

    /**
     * 職種リスト
     */
    public String[] jobKbnList;

    /**
     * 業態リスト
     */
    public String[] industryKbnList;

    /**
     * 取得資格リスト
     */
    public String[] qualificationKbnList;

    /** 取得資格その他 */
    public String qualificationOther;

    /** 転職希望時期その他 */
    public String hopeCareerChangeTermOther;

    /**
     * 最終ログイン日時
     */
    public String lastLoginDatetime;

    /**
     * 事前登録区分
     */
    public Integer advancedRegistrationKbn;


    public String hopeCareerChangeYear;

    public String hopeCareerChangeMonth;

    public String[] hopeCareerTermList;

    public Long version;

    public String password;

    public String rePassword;

    /**
     * キャリアDTOアクセサのクラスを取得
     */
    public abstract Class<CAREER> getCareerDtoClass();

    /**
     * 電話番号の取得
     *
     * @return 電話番号
     */
    public String getPhoneNo() {
        if (StringUtils.isBlank(phoneNo1)
                || StringUtils.isBlank(phoneNo2)
                || StringUtils.isBlank(phoneNo3)) {
            return "";
        }

        StringBuilder sb = new StringBuilder(0);
        sb.append(phoneNo1)
                .append("-")
                .append(phoneNo2)
                .append("-")
                .append(phoneNo3);

        return sb.toString();
    }







    /**
     * バリデート
     * @return エラー
     */
    public ActionMessages validate() {

        ActionMessages errors = new ActionMessages();

        // 現在の状況(リニューアルで必須から除外)
//        if (StringUtil.isBlank(currentSituationKbn)) {
//            formLog.debug("現在の状況");
//            errors.add("errors", new ActionMessage("errors.required",
//                    MessageResourcesUtil.getMessage("labels.currentSituationKbn")));
//        }

        // 氏名
        if (StringUtils.isBlank(memberName)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.memberName")));
        }

        // 氏名(カナ)
        if (StringUtils.isBlank(memberNameKana)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.memberNameKana")));
        } else if (!WztValidateUtil.isKatakanaWithMark(memberNameKana)) {
            errors.add("errors", new ActionMessage("errors.katakana",
                    MessageResourcesUtil.getMessage("labels.memberNameKana")));
        }

        // 性別
        if (StringUtils.isBlank(sexKbn)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.sexKbn")));
        }

        // 生年月日(年
        if (StringUtils.isBlank(birthYear)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.birthdayYear")));
        }

        // 生年月日(月
        if (StringUtils.isBlank(birthMonth)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.birthdayMonth")));
        }

        // 生年月日(日
        if (StringUtils.isBlank(birthDayDay)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.birthdayDay")));
        }

        // 生年月日のチェック
        if (StringUtils.isNotBlank(birthYear)
                && StringUtils.isNotBlank(birthMonth)
                && StringUtils.isNotBlank(birthDayDay)) {
            checkBirthDay(errors);
        }

        checkPassword(errors);

        // 電話番号
        checkPhoneNo(errors);


        checkZipCode(errors);

        // 都道府県
        if (StringUtils.isBlank(prefecturesCd)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.prefecturesCd")));
        }

        // 市区町村
        if (StringUtils.isBlank(municipality)) {

            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.municipality")));
        }

        // 事前登録自己PR
        if (StringUtils.isNotBlank(advancedRegistrationSelfPr) && advancedRegistrationSelfPr.length() > SELF_PR_MAX_LENGTH) {
            errors.add("errors", new ActionMessage("errors.lengthAdvancedRegistrationSelfPr", SELF_PR_MAX_LENGTH));
        }

        // 事前登録自己PR
        if (StringUtils.isNotBlank(hopeCareerChangeText) && hopeCareerChangeText.length() > SELF_PR_MAX_LENGTH) {
            errors.add("errors", new ActionMessage("errors.lengthAdvancedRegistrationHopeText", SELF_PR_MAX_LENGTH));
        }

        // 転職希望職種
        if (ArrayUtils.isEmpty(jobKbnList)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("msg.careerChange") +
                            MessageResourcesUtil.getMessage("labels.jobKbnList")));
        }

        // 転職希望業態
        if (ArrayUtils.isEmpty(industryKbnList)) {
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("msg.careerChange") +
                            MessageResourcesUtil.getMessage("labels.industryKbnList")));
        }

        // 転職希望時期
        checkHopeCareerChange(errors);


        // 職歴リスト
        if (careerList != null) {
            checkCareerHistory(errors);
        }

        // メルマガ
        if (StringUtils.isBlank(advancedMailMagazineReceptionFlg)) {
            errors.add("errors", new ActionMessage("errors.required",
                    "メルマガ(合同企業説明会用)"));
        }


        // 状況が入力されている場合
        if (StringUtil.isNotEmpty(graduationKbn)) {
            // 学校名が未入力の場合はエラー
            if (StringUtil.isBlank(schoolName)) {
                // 「{最終学歴}を{入力する}場合は、{学校名}を入力してください。」
                errors.add("errors", new ActionMessage("errors.requiredWithOtherData",
                        MessageResourcesUtil.getMessage("msg.schoolHistory"),
                        MessageResourcesUtil.getMessage("msg.doInput"),
                        MessageResourcesUtil.getMessage("msg.schoolName")
                ));
            }
        }

        // メール
        checkMail(errors);

        // TODO 必要があれば
//        checkNonMemberForm(errors);

        return errors;
    }


    /**
     * 電話番号をチェック<br />
     * 電話番号が登録されている場合、全て入力されているか<br />
     * 値がすべて半角数字かどうかチェックする
     * @param errors エラーを格納するActionMessages
     */
    protected void checkPhoneNo(ActionMessages errors) {

        // 電話番号に入力値があればチェックを行う
        if (!StringUtil.isEmpty(phoneNo1) || !StringUtil.isEmpty(phoneNo2) || !StringUtil.isEmpty(phoneNo3)) {
            formLog.debug("電話番号チェック");
            // 全ての値が入力されているかチェック
            if (StringUtil.isEmpty(phoneNo1) || StringUtil.isEmpty(phoneNo2) || StringUtil.isEmpty(phoneNo3)) {
                // 「{電話番号}を入力してください。」
                errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("msg.phoneNo")));

            } else {
                // 半角数字で入力されているかチェック
                if (!WztValidateUtil.isDigit(phoneNo1) || !WztValidateUtil.isDigit(phoneNo2) || !WztValidateUtil.isDigit(phoneNo3)) {
                    // 「{電話番号}は半角数字を入力してください。」
                    errors.add("errors", new ActionMessage("errors.integerhankaku", MessageResourcesUtil.getMessage("msg.phoneNo")));
                }
            }
        }
    }

    /**
     * 生年月日入力チェック<br />
     * 生年月日が日付として妥当かチェックする
     * @param errors エラーを格納するActionMessages
     */
    protected void checkBirthDay(ActionMessages errors) {

        // 生年月日が正しい日付かチェックする
        if (!GourmetCareeUtil.checkDate(birthYear, birthMonth, birthDayDay)) {
            // 「{生年月日}は存在しない日付です。」
            errors.add("errors", new ActionMessage("errors.noExistDate", MessageResourcesUtil.getMessage("msg.birthDay")));
            return;
        }
        try {
            // 年月日の変換
            DateUtils.convertStrDate(birthYear, birthMonth, birthDayDay);

            // 変換に失敗した場合
        } catch (ParseException e) {
            // 「{生年月日}は存在しない日付です。」
            errors.add("errors", new ActionMessage("errors.noExistDate", MessageResourcesUtil.getMessage("msg.birthDay")));
        }
    }


    /**
     * メールのバリデートを行う
     */
    protected void checkMail(ActionMessages errors) {

        if (StringUtils.isNotBlank(pcMail)) {
            if (!WztValidateUtil.isEmailAddress(pcMail)) {
                errors.add("errors", new ActionMessage("errors.email",
                        MessageResourcesUtil.getMessage("labels.pcMail")));
            } else if (isMobileMail(pcMail)) {
                errors.add("errors", new ActionMessage("errors.email",
                        MessageResourcesUtil.getMessage("labels.pcMail")));
            }
        }


        if (StringUtils.isNotBlank(mobileMail)) {
            if (!WztValidateUtil.isEmailAddress(mobileMail)) {
                errors.add("errors", new ActionMessage("errors.email",
                        MessageResourcesUtil.getMessage("labels.mobileMail")));
            } else if (!isMobileMail(mobileMail)) {
                errors.add("errors", new ActionMessage("errors.mobilemailaddress",
                        MessageResourcesUtil.getMessage("labels.mobileMail")));
            }
        }
    }

    /**
     * 携帯用メールアドレスかどうかをチェックする。
     * @param mailAddress メールアドレス
     * @return 携帯用のメールアドレスだった場合にtrue
     */
    private boolean isMobileMail(String mailAddress) {
        DBAccessUtilLogic logic = SingletonS2Container.getComponent(DBAccessUtilLogic.class);
        return logic.checkMobileDomain(mailAddress);
    }

    /**
     * 転職希望時期のチェック
     */
    private void checkHopeCareerChange(ActionMessages errors) {
        if (!isInputHopeCareerChangeKbn()
                && !isInputHopeCareerChangeText()) {
            formLog.debug("転職希望がない");
            errors.add("errors", new ActionMessage("errors.required",
                    MessageResourcesUtil.getMessage("labels.preferredDateKbn")));
        }


        if (isInputHopeCareerChangeKbn()
                && (StringUtils.isNotBlank(hopeCareerChangeYear) || StringUtils.isNotBlank(hopeCareerChangeMonth))) {
            errors.add("errors", ActionMessageUtil.createActionMessage("errors.advancedRegistrationHopeChangeCareer"));
            return;
        }


        if (StringUtils.isNotBlank(hopeCareerChangeYear) && !NumberUtils.isDigits(hopeCareerChangeYear)) {
            errors.add("errors", new ActionMessage("errors.integer",
                    MessageResourcesUtil.getMessage("labels.hopeCareerChangeYear")));
        }


        if (StringUtils.isNotBlank(hopeCareerChangeMonth)) {
            if (NumberUtils.isDigits(hopeCareerChangeMonth)) {
                formLog.debug("");
                int val = Integer.parseInt(hopeCareerChangeMonth);
                if (val < 1 || val > 12) {
                    errors.add("errors", new ActionMessage("errors.range",
                            MessageResourcesUtil.getMessage("labels.hopeCareerChangeMonth"),
                            1,
                            12));
                }
            } else {
                errors.add("errors", new ActionMessage("errors.integer",
                        MessageResourcesUtil.getMessage("labels.hopeCareerChangeMonth")));
            }
        }


        // 年月とチェックボックスが入力されているか、チェックボックスが選択されていて、複数選択されているか
        if ((isInputHopeCareerChangeKbn() && isInputHopeCareerChangeText())
                || (!isInputSingleHopeCareerChangeKbn() && isInputHopeCareerChangeKbn())) {
            errors.add("errors", new ActionMessage("errors.advancedRegistrationHopeChangeCareer"));
        }
    }


    /**
     * 転職希望時期の年/月が入力されているかどうか
     */
    private boolean isInputHopeCareerChangeText() {
        return StringUtils.isNotBlank(hopeCareerChangeYear)
                && StringUtils.isNotBlank(hopeCareerChangeMonth);
    }

    /**
     * 転職希望時期のチェックボックスが選択されているかどうか
     */
    private boolean isInputHopeCareerChangeKbn() {
        return !ArrayUtils.isEmpty(hopeCareerTermList);
    }

    /**
     * 転職希望時期のチェックボックスが一個だけ選択されているかどうか
     */
    private boolean isInputSingleHopeCareerChangeKbn() {
        if (isInputHopeCareerChangeKbn()) {
            return hopeCareerTermList.length == 1;
        }
        return false;
    }


    /**
     * 職務経歴入力チェック<br />
     * 職歴入力時、会社名が入力されているかチェックする
     * @param errors エラーを格納するActionMessages
     */
    protected void checkCareerHistory(ActionMessages errors) {

        List<CAREER> careerHistoryList = careerList;
        // 職歴が複数ある場合
        if (careerHistoryList.size() > 1) {
            // 職歴のリストをチェック
            for (int i = 0; i < careerHistoryList.size(); i++ ) {

                CAREER dto = careerHistoryList.get(i);

                // 追加した職歴の最後でなければ会社名が入っているかチェックする
                if (i != careerHistoryList.size()-1 && StringUtil.isBlank(dto.getCompanyName())) {

                    // メッセージをセット
                    // 「{職務経歴}を{入力する}場合は、{会社名}を入力してください。」
                    errors.add("errors", new ActionMessage("errors.requiredWithOtherData",
                            MessageResourcesUtil.getMessage("msg.careerHistory"),
                            MessageResourcesUtil.getMessage("msg.doInput"),
                            MessageResourcesUtil.getMessage("msg.companyName")
                    ));
                    // メッセージが重複しないようエラーを返す
                    return;
                }
                // 職歴の入力チェック
                if (isErrorCareeHistory(dto, errors)) {
                    // メッセージが重複しないようエラーを返す
                    return;
                }
            }
            // 職歴が１つの場合(アドレス直打ちを防ぐため1を指定)
        } else if (careerHistoryList.size() == 1) {
            // 職歴の入力チェック
            if (isErrorCareeHistory(careerHistoryList.get(0), errors)) {
            }
        }
    }


    /**
     * 職歴の入力チェックを行います。<br />
     * エラーがあった場合は、メッセージをセットします。
     * @param dto チェックするWEB履歴書職歴Dto
     * @param errors セットするActionMessages
     * @return エラーの場合はtrue、エラーでない場合はfalseを返します。
     */
    private boolean isErrorCareeHistory(CAREER dto, ActionMessages errors) {

        // 勤務期間、職種、業態、業務内容、客席数・坪数、月売上のいずれかが入力されている場合
        if (!StringUtil.isEmpty(dto.getWorkTerm()) || (dto.getJobKbnList() != null && !dto.getJobKbnList().isEmpty())
                || (dto.getIndustryKbnList() != null && !dto.getIndustryKbnList().isEmpty())
                || !StringUtil.isEmpty(dto.getBusinessContent()) || !StringUtil.isEmpty(dto.getSeat()) || !StringUtil.isEmpty(dto.getMonthSales())) {

            // 会社名が未入力の場合はエラー
            if (StringUtil.isBlank(dto.getCompanyName())) {

                // メッセージをセット
                setCareeHistoryError(errors);
                // エラーがある場合はtrueを返す
                return true;
            }
        }
        return false;
    }


    /**
     * 職歴チェック時のエラーをセットします。
     * @param errors セットするActionMessages
     */
    private void setCareeHistoryError(ActionMessages errors) {

        // 「{職務経歴}を{入力する}場合は、{会社名}を入力してください。」
        errors.add("errors", new ActionMessage("errors.requiredWithOtherData",
                MessageResourcesUtil.getMessage("msg.careerHistory"),
                MessageResourcesUtil.getMessage("msg.doInput"),
                MessageResourcesUtil.getMessage("msg.companyName")
        ));
    }

    /**
     * 郵便番号の入力チェック
     * 必須チェックは行わないが、郵便番号の形式でなければエラーとする。
     * @param errors アクションメッセージ
     */
    private void checkZipCode(ActionMessages errors) {
        // 郵便番号
        if (StringUtils.isNotBlank(zipCd) && !WztValidateUtil.isZipcode(zipCd)) {
            errors.add("errors", new ActionMessage("errors.zip",
                    MessageResourcesUtil.getMessage("labels.zipCd")));
        }
    }



    private void checkPassword(ActionMessages errors) {
        // 両方入力されていなければ何もしない
        if(StringUtils.isEmpty(password) && StringUtils.isEmpty(rePassword)) {
            return;
        }

        if (StringUtil.isEmpty(password) || !password.equals(rePassword)) {
            // 「{パスワード}と{パスワード(確認用)}は一致していません。」
            errors.add("errors", new ActionMessage("error.notSameData",
                    MessageResourcesUtil.getMessage("labels.password"),
                    MessageResourcesUtil.getMessage("labels.rePassword")
            ));
        }

        if (StringUtils.isNotBlank(password)) {

            if (!GourmetCareeUtil.isHankakuStr(password)) {
                errors.add("errors", new ActionMessage("errors.singleAlphanum",
                        MessageResourcesUtil.getMessage("labels.password")));
            }
            int length = password.length();
            if (length > 20) {
                errors.add("errors", new ActionMessage("errors.maxlength",
                        MessageResourcesUtil.getMessage("labels.password"),
                        "20"));
            } else if (length < 6) {
                errors.add("errors", new ActionMessage("errors.minlength",
                        MessageResourcesUtil.getMessage("labels.password"),
                        "6"));
            }
        }

        if (StringUtils.isNotBlank(rePassword)) {

            if (!GourmetCareeUtil.isHankakuStr(rePassword)) {
                errors.add("errors", new ActionMessage("errors.singleAlphanum",
                        MessageResourcesUtil.getMessage("labels.rePassword")));
            }

            int length = rePassword.length();
            if (length > 20) {
                errors.add("errors", new ActionMessage("errors.maxlength",
                        MessageResourcesUtil.getMessage("labels.rePassword"),
                        "20"));
            } else if (length < 6) {
                errors.add("errors", new ActionMessage("errors.minlength",
                        MessageResourcesUtil.getMessage("labels.rePassword"),
                        "6"));
            }
        }
    }


    /**
     * チェックボックスのリセット
     */
    public void resetCheckBox() {
        if (CollectionUtils.isNotEmpty(careerList)) {
            for (CAREER dto : careerList) {
                dto.setJobKbnList(null);
                dto.setIndustryKbnList(null);
            }
        }
        jobKbnList = null;
        industryKbnList = null;
        hopeCareerTermList = null;
        qualificationKbnList = null;
    }


    /**
     * フォームのリセット
     */
    public void resetForm() {
        super.resetBaseForm();
        advancedRegistrationKbn = null;
        advancedMailMagazineReceptionFlg = null;
        advancedRegistrationSelfPr = null;
        memberKbn = null;
        memberName = null;
        currentSituationKbn = null;
        hopeCareerChangeText = null;
        hopeCareerChangeYear = null;
        hopeCareerChangeMonth = null;
        hopeCareerTermList = null;
        id = null;
        version = 0L;
        terminalKbn = 0;
        memberName = null;
        memberNameKana = null;
        loginId = null;
        password = null;
        rePassword = null;
        pcMail = null;
        mobileMail = null;
        sexKbn = null;
        birthYear = null;
        birthMonth = null;
        birthDayDay = null;
        birthday = null;
        phoneNo1 = null;
        phoneNo2 = null;
        phoneNo3 = null;
        zipCd = null;
        prefecturesCd = null;
        municipality = null;
        address = null;
        jobInfoReceptionFlg = null;
        mailMagazineReceptionFlg = null;
        scoutMailReceptionFlg = null;
        jobKbnList = null;
        industryKbnList = null;
        employPtnKbn = null;
        transferFlg = null;
        midnightShiftFlg = null;
        salaryKbn = null;
        schoolName = null;
        department = null;
        graduationKbn = null;
        careerList = null;
        workSalary = null;
        qualificationOther = null;
        hopeCareerChangeTermOther = null;
    }



    /**
     * 誕生日をパースしてフィールドにセット
     * @param birthDay 誕生日
     */
    public void formatBirthDay(Date birthDay) {
        if (birthDay == null) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(birthDay);
        this.birthYear = String.valueOf(cal.get(Calendar.YEAR));
        this.birthMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        this.birthDayDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        this.birthday = DateUtils.getDateStr(birthDay, "yyyy年MM月dd日");

    }


    /**
     * 誕生日をtimestampにパース
     * @return timestamp
     * @throws ParseException パースに失敗した時にスロー
     */
    public Timestamp parseBirthDay() throws ParseException{
        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(String.format("%s/%s/%s",
                birthYear,
                birthMonth,
                birthDayDay));
        return new Timestamp(date.getTime());
    }


    /**
     * パースした後の日付をフォーマットしたものを返す
     */
    public String getParsedBirthDay() {
        if (birthYear == null || birthMonth == null || birthDayDay == null) {
            return "";
        }

        try {
            Timestamp date = parseBirthDay();
            return new SimpleDateFormat("yyyy年MM月dd日").format(date);
        } catch (ParseException e) {
            formLog.warn("誕生日のパースに失敗しました。", e);
            return "";
        }
    }


    /**
     * 転職可能区分リストのIntegerリストを生成
     */
    public List<Integer> createHopeCareerTermIntegerList() {
        if (hopeCareerTermList == null) {
            return Collections.emptyList();
        }

        List<Integer> list = Lists.newArrayList();
        for (String value : hopeCareerTermList) {
            if (NumberUtils.isDigits(value)) {
                list.add(Integer.parseInt(value));
            }
        }
        return list;
    }

}

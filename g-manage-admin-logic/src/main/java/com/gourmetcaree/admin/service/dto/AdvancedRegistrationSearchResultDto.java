package com.gourmetcaree.admin.service.dto;

import java.sql.Timestamp;

import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * 事前登録DTO
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationSearchResultDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8702855112683207148L;

	/** 事前登録エントリユーザID */
	public Integer id;

	/** 事前登録ID */
	public Integer advancedRegistrationId;

	/** 事前登録ユーザID */
	public Integer advancedRegistrationUserId;

	public Integer attendedStatus;

	/**
	 * 名前
	 */
	public String memberName;

	/**
	 * 名前カナ
	 */
	public String memberNameKana;

	/**
	 *  郵便番号
	 */
	public String zipCd;

	/**
	 * 都道府県コード
	 */
	public Integer prefecturesCd;


	/**
	 * 市区町村
	 */
	public String municipality;

	/**
	 * 住所
	 */
	public String address;

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

	/** ログインID */
	public String loginId;

	/**
	 * エリアコード
	 */
	public Integer areaCd;

	/**
	 * 性別区分
	 */
	public Integer sexKbn;

	/**
	 *  生年月日
	 */
	public Timestamp birthday;


	/**
	 * 会員区分
	 */
	public Integer memberKbn;


	/**
	 * PCメールアドレス
	 */
	public String pcMail;

	/**
	 * 携帯メールアドレス
	 */
	public String mobileMail;

	/**
	 * 事前登録メルマガフラグ
	 */
	public Integer advancedMailMagazineReceptionFlg;

	/**
	 * 端末区分
	 */
	public Integer terminalKbn;

	/** 事前登録登録日時 */
	public Timestamp registrationDatetime;

	/** 現在（前職）の年収  */
	public String workSalary;

	/** 事前登録用自己PR */
	public String advancedRegistrationSelfPr;

	/** 転職先に望むこと */
	public String hopeCareerChangeText;

	/** 転職希望年 */
	public String hopeCareerChangeYear;

	/** 転職希望月 */
	public String hopeCareerChangeMonth;

	/** 学校名 */
	public String schoolName;

	/** 学部・学科 */
	public String department;

	/** PCメール配信停止フラグ */
	public Integer pcMailStopFlg;

	/** モバイルメール配信停止フラグ */
	public Integer mobileMailStopFlg;

	/** 取得資格その他 */
	public String qualificationOther;

	/** 転職希望時期その他 */
	public String hopeCareerChangeTermOther;

	public Integer getAge() {
		if (birthday == null) {
			return null;
		}
		return GourmetCareeUtil.convertToAge(birthday);
	}


	public Integer getMemberStatus() {
		return GourmetCareeUtil.convertMemberKbnToAdvancedStatusKbn(memberKbn);
	}
}

package com.gourmetcaree.admin.pc.member.dto.member;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;

/**
 * 会員印刷用DTO
 * @author Takehiro Nakamori
 *
 */
public class PrintMemberDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6654693888447909080L;

	/** ID */
	public Integer id;

	/** 氏名 */
	public String memberName;

	/** 氏名カナ */
	public String memberNameKana;

	/** 年齢 */
	public int age;

	/** 性別区分 */
	public Integer sexKbn;

	/** 郵便番号 */
	public String zipCd;

	/** 都道府県コード */
	public Integer prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 住所 */
	public String address;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** 端末区分 */
	public Integer terminalKbn;

	/** ログインID */
	public String loginId;

	/** PCメールアドレス */
	public String pcMail;

	/** 携帯メールアドレス */
	public String mobileMail;

	/** 事前登録用自己PR */
	public String advancedRegistrationSelfPr;

	/** 転職先に望むこと */
	public String hopeCareerChangeText;

	/** 転職希望文字列 */
	public String hopeCareerChangeStr;

	/** 学校名 */
	public String schoolName;

	/** 学部・学科 */
	public String department;

	/** 在学状況区分 */
	public Integer graduationKbn;

	/** 現在（前職）の年収  */
	public String workSalary;

	/** 職歴リスト */
	public List<CareerDto> careerDtoList;

	/** 業種リスト */
	public List<Integer> industryKbnList;

	/** 職種リスト */
	public List<Integer> jobKbnList;

	/** 現在の状況 */
	public Integer currentSituationKbn;

	/** 取得資格リスト */
	public List<Integer> qualificationKbnList;

	/** 取得資格その他 */
	public String qualificationOther;

	/** 転職希望時期その他 */
	public String hopeCareerChangeTermOther;

	/** 事前登録マスタ名 */
	public String advancedRegistrationName;


	public String getPhoneNo() {
		if (StringUtils.isBlank(phoneNo1)
				|| StringUtils.isBlank(phoneNo2)
				|| StringUtils.isBlank(phoneNo3)) {
			return "";
		}

		return new StringBuilder(0)
				.append(phoneNo1)
				.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
				.append(phoneNo2)
				.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
				.append(phoneNo3)
				.toString();
	}
}

package com.gourmetcaree.admin.pc.interest.dto.interest;

import java.io.Serializable;
import java.util.List;

/**
 * 気になる通知DTOクラス
 * @author t_shiroumaru
 *
 */
public class InterestDto implements Serializable {

	/** ID */
	public int id;

	/** 会員ID */
	public int memberId;

	/** 気になる通知日時 */
	public String interestDatetime;

	/** 原稿名 */
	public String manuscriptName;

	/** エリアコード */
	public String areaCd;

	/** 都道府県コード */
	public Integer prefecturesCd;

	/** 市区町村名 */
	public String municipality;

	/** 性別区分 */
	public String sexKbn;

	/** 会員名 */
	public String memberName;

	/** 年齢 */
	public String age;

	/** 希望雇用形態リスト */
	public List<String> hopeEmployPtnKbnList;

	/** 希望職種リスト */
	public List<String> hopeJobKbnList;

	/** 希望業態リスト */
	public List<String> hopeIndustryKbnList;

	/** 会員詳細のパス */
	public String detailPath;
}

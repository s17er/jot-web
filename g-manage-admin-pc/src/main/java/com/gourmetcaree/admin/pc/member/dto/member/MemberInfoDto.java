package com.gourmetcaree.admin.pc.member.dto.member;

import java.io.Serializable;
import java.util.List;

/**
 * 会員DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class MemberInfoDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2888770493851821921L;

	/** 会員ID */
	public int id;

	/** エリアコード */
	public String[] memberAreaList;

	/** 氏名 */
	public String memberName;

	/** 氏名カナ */
	public String memberNameKana;

	/** 性別 */
	public String sexKbn;

	/** 年齢 */
	public String age;

	/** 都道府県 */
	public String prefecturesCd;

	/** 住所 */
	public String municipality;

	/** 雇用形態 */
	public List<Integer> employPtnKbnList;

	/** メールアドレス */
	public String loginId;

	/** 更新日 */
	public String lastUpdateDate;

	/** 希望業種リスト */
	public List<AttrDto> industryList;

	/** 職種リスト */
	public List<AttrDto> jobList;

	/** 詳細のパス */
	public String detailPath;

	/** メルマガ区分 */
	public String mailMagazineReceptionFlg;

	/** 端末区分 */
	public String terminalKbn;

	/** 更新日 */
	public String updateDatetime;

	/** 登録状況 */
	public int advancedStatus;

	/** 事前登録登録日時 */
	public String advancedRegistrationDatetime;

}
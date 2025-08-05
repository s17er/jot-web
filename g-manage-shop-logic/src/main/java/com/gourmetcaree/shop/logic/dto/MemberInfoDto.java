package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gourmetcaree.db.common.entity.TFootprint;
import com.gourmetcaree.db.common.entity.VMemberMailbox;

/**
 * 会員DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class MemberInfoDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5226642801652181637L;

	/** 会員ID */
	public int id;

	/** エリア */
	public String areaCd;

	/** 希望勤務地（prefectures_cd,市区町村名） */
	public Map<Integer, List<String>> hopeCityMap;

	/** 検討中ID */
	public int keepId;

	/** スカウトフラグ */
	public int scoutFlg;

	/** 応募フラグ */
	public int applicationFlg;

	/** 足あとフラグ */
	public int footprintFlg;

	/** 検討中フラグ */
	public int considerationFlg;

	/** スカウト受取区分 */
	public Integer scoutReceiveKbn;

	/** スカウト */
	public VMemberMailbox vMemberMailbox;

	/** 応募 */
	public VMemberMailbox applicationMail;

	/** 足あと（気になる） */
	public TFootprint tFootprint;

	/** 足あと（気になる）の登録（更新）日時 */
	public Date tFootprintDate;

	/** 性別 */
	public String sexKbn;

	/** 年齢 */
	public String age;

	/** 都道府県 */
	public String prefecturesCd;

	/** 住所 */
	public String municipality;

	/** 雇用形態 */
	public List<String> employPtnKbnList;

	/** 最終ログイン日 */
	public String lastLoginDatetime;

	/** 希望職種区分 */
	public String[] job;

	/** 希望業種区分 */
	public String[] industry;

	/** 詳細のパス */
	public String detailPath;

	/** スカウト可否フラグ */
	public boolean scoutMailOkFlg;

	/** 会員エリアリスト */
	public String[] areaList;

	/** 希望年収区分 */
	public Integer salaryKbn;

	/** スカウト済フラグ */
	public int scoutedFlg;

	/** 気になるフラグ */
	public int favoriteFlg;

	/** 応募ありフラグ */
	public int subscFlg;

	/** 辞退フラグ */
	public int refuseFlg;
}
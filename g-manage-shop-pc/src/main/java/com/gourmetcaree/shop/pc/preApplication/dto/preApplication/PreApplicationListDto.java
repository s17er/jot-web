package com.gourmetcaree.shop.pc.preApplication.dto.preApplication;

import java.util.Date;
import java.util.List;

import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.scoutFoot.dto.member.MemberStatusDto;

/**
 *
 * プレ応募者一覧のDTO
 */
public class PreApplicationListDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 768107738943022130L;

	/** 応募ID */
	public int id;

	/** 応募日時 */
	public Date applicationDatetime;

	/** 会員ID */
	public Integer memberId;

	/** 性別区分 */
	public int sexKbn;

	/** 年齢 */
	public int age;

	/** 都道府県コード */
	public String prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 住所（番地以下の住所情報） */
	public String address;

	/** 雇用形態区分（リニューアルで対象外） */
	public int employPtnKbn;

	/** 顧客ID */
	public int customerId;

	/** 応募先 */
	public String applicationName;

	/** 希望職種 */
	public String hopeJob;

	/** WEBデータID */
	public int webId;

	/** GCWコード */
	public String gcwCd;

	/** 選考対象フラグ */
	public int selectionFlg;

	/** 選考対象フラグカラー */
	public String selectionFlgColor;

	/** メモ */
	public String memo;

	/** 未読メールがあるかどうかのフラグ */
	public boolean unopenedMailFlg = true;

	/** バージョン */
	public long version;

	/** チェックされたかどうかのフラグ */
	public boolean containsCheckedFlg;

	/** 希望職種（リニューアル後のプルダウン項目） */
	public String jobKbn;

	/** 希望業態 */
	public List<String> industryKbnList;

    /** 雇用形態区分リスト */
	public List<String> employPtnKbnList;

	/** 希望職種 */
	public List<String> jobKbnList;

	/** 会員のステータスDTO */
	public MemberStatusDto memberStatusDto;

	/**
	 * 男性かどうかを取得する。
	 * @return true:男性、false:女性
	 */
	public boolean isApplicantMale() {
		return (sexKbn == MTypeConstants.Sex.MALE) ? true : false;
	}

	/**
	 * 選考対象かどうかを取得する
	 * @return true:選考対象、false：選考対象ではない
	 */
	public boolean isSelectionTarget() {
		return (selectionFlg == MTypeConstants.SelectionFlg.SELECTION_TARGET) ? true : false;
	}

}
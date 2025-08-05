package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;
import java.util.Date;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.scoutFoot.dto.member.MemberStatusDto;

/**
 * スカウト会員DTOクラス
 * @author Motoaki Hara
 * @version 1.0
 */
public class ScoutMemberInfoDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5074811335668409723L;

	/** 会員ID */
	public int memberId;

	/** メールログID */
	public Integer mailLogId;

	/** 応募フラグ */
	public int applicationFlg;

	/** 足あとフラグ */
	public int footprintFlg;

	/** 性別 */
	public int sexKbn;

	/** 年齢 */
	public int age;

	/** 都道府県コード */
	public String prefecturesCd;

	/** 住所 */
	public String municipality;

	/** 住所 */
	public String address;

	/** 雇用形態 */
	public String employPtnKbn;

	/** 希望職種区分 */
	public String[] job;

	/** 希望業種区分 */
	public String[] industry;

	/** 詳細のパス */
	public String memberDetailPath;

	/** スカウトメール受信可否フラグ */
	public Integer scoutMailReceptionFlg;

	/** スカウトブロックフラグ */
	public Integer scoutBlockFlg;

	/** 送信日時（スカウト日） */
    public String sendDatetime;

    /** ステータス */
    public String status;

    /** 選考対象フラグ */
    public String selectionFlg;

    /** 受信側の閲覧日時 */
    public Date receiveReadingDatetime;

    /**
     * 受信者がメールを読んだかどうか
     * @return
     */
    public boolean isReceiveReadFlg() {
    	return receiveReadingDatetime != null;
    }

	/**
	 * 男性かどうかを取得する。
	 * @return true:男性、false:女性
	 */
	public boolean isApplicantMale() {
		return (sexKbn == MTypeConstants.Sex.MALE) ? true : false;
	}

	/**
	 * 会員がスカウトメールが可能な状態かどうか
	 * @return true:可能、false:不可
	 */
	public boolean isScoutMailOkFlg () {
		if (MTypeConstants.ScoutMailReceptionFlg.RECEPTION == scoutMailReceptionFlg
				&& scoutBlockFlg == MemberStatusDto.ScoutBlockFlgKbn.NOT_BLOCK) {
			return true;
		}
		return false;
	}

}
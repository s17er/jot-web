package com.gourmetcaree.db.scoutFoot.dto.scoutMail;

import java.io.Serializable;
import java.util.Date;

/**
 * スカウトメールリストのDTO
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ScoutMailListDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 54851617701185759L;

	/** ID */
	public Integer id;

	/** 送信者ID */
	public Integer fromId;

    /** 受信者ID */
    public Integer toId;

    /** 件名 */
    public String subject;

    /** メールステータス */
    public Integer mailStatus;

    /** 送信日時 */
    public Date sendDatetime;

    /** 会員ID */
    public Integer memberId;

    /** 受信側の閲覧日時 */
    public Date receiveReadingDatetime;

    /** スカウトメールログID */
	public Integer scoutMailLogId;

	/** 生年月日 */
	public Date birthday;

	/** エリアコード */
	public Integer areaCd;

	/** 都道府県コード */
	public Integer prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 飲食業界経験区分 */
	public Integer foodExpKbn;

	/** ステータス */
	public Integer selectionFlg;

	/** メモ */
	public String memo;

}
package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;

/**
 * スカウトメール詳細DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ScoutMailDetailDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8658749015740975804L;

	/** ID */
	public int id;

	/** 送受信区分 */
	public int sendKbn;

	/** 会員ID */
	public int memberId;

    /** 件名 */
    public String subject;

    /** 本文 */
    public String body;

    /** メールステータス */
    public int mailStatus;

    /** 送信日時 */
    public String sendDatetime;

    /** バージョン番号 */
    public Long version;

    /** メール返信フラグ */
    public boolean returnMailFlg;

    /** 気になるメールフラグ */
    public boolean interestFlg;

}
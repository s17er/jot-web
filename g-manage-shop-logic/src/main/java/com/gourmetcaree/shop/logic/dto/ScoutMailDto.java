package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * スカウトメールリストDTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ScoutMailDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6577351819333546191L;

	/** ID */
	public int id;

    /** 件名 */
    public String subject;

    /** メールステータス */
    public String mailStatus;

    /** 送信日時 */
    public String sendDatetime;

    /** 会員ID */
    public int memberId;

    /** 会員存在フラグ */
    public boolean existMemberFlg;

    /** メール詳細パス */
    public String mailDetailPath;

    /** 求職者詳細パス */
    public String memberDetailPath;

    /** 受信側の閲覧日時 */
    public Date receiveReadingDatetime;

    /** スカウトメールログID */
	public Integer scoutMailLogId;

	/** 初回スカウトメールにアクセス可能か */
	public boolean isFirstScoutAccessible;

	public Integer age;

	public Integer selectionFlg;

	public String memo;

	public String areaName;

	/** 都道府県コード */
	public Integer prefecturesCd;

	/** 市区町村 */
	public String municipality;

	public Integer foodExpKbn;

	public int toId;


	/**
	 * 初回スカウトメール詳細画面へアクセス可能か
	 */
	public boolean isScoutDetailAccessible() {
		return existMemberFlg && isFirstScoutAccessible;
	}
    /**
     * 受信者がメールを読んだかどうか
     * @return
     */
    public boolean isReceiveReadFlg() {
    	return receiveReadingDatetime != null;
    }

}
package com.gourmetcaree.shop.pc.preApplication.dto.preApplicationMail;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 *
 * プレ応募メールのDTO
 */
public class PreApplicationMailListDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4399503571336479539L;

    /** ID */
    public Integer id;

    /** メール区分 */
    public Integer mailKbn;

    /** 送受信区分 */
    public Integer sendKbn;

    /** 送信者区分 */
    public Integer senderKbn;

    /** 送信者ID */
    public Integer fromId;

    /** 送信者名 */
    public String fromName;

    /** 受信者ID */
    public Integer toId;

    /** 受信者名 */
    public String toName;

    /** 件名 */
    public String subject;

    /** 本文 */
    public String body;

    /** 店舗名 */
    public String shopName;

    /** 店舗ID */
    public String shopId;

    /** 応募ID */
    public Integer preApplicationId;

    /** メールステータス */
    public Integer mailStatus;

    /** 送信日時（受信日時） */
    public String sendDatetime;

    /** 閲覧日時 */
	public Date receiveReadingDatetime;

	/** 店舗見学ID */
	public Integer observateApplicationId;

	/** エリア名 */
	public String areaName;

	/** 選考フラグ */
	public Integer selectionFlg;

	/** メモ */
	public String memo;

	/** 年齢 */
	public Integer age;

	/** 雇用形態区分 */
	public Integer employPtnKbn;

	/** 職種区分 */
	public Integer jobKbn;

	/** 飲食業界経験年数区分 */
	public Integer foodExpKbn;

    public boolean unsubscribeFlg;

	/**
	 * 受信者が読んだかどうかのフラグ
	 * @return
	 */
	public boolean isReceiveReadFlg() {
		return receiveReadingDatetime != null;
	}

}
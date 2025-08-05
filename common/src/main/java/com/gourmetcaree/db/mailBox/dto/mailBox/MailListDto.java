package com.gourmetcaree.db.mailBox.dto.mailBox;

import java.io.Serializable;
import java.util.Date;

/**
 * スカウトメールリストのDTO
 * @author Takahiro Kimura
 * @version 1.0
 */
public class MailListDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5783658463786090112L;

	/** ID */
	public Integer id;

	/** メール区分 */
	public Integer mailKbn;

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

    /** メールステータス */
    public Integer mailStatus;

    /** 送信日時 */
    public Date sendDatetime;

    /** 顧客ID */
    public Integer customerId;

}
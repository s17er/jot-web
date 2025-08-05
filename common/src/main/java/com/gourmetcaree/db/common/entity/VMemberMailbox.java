package com.gourmetcaree.db.common.entity;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Tue Aug 26 21:21:01 JST 2008
 */
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 送受信を整理した会員のメールボックスview用エンティティ
 * @author whizz
 * @version 1.0
 *
 */
@Entity
@Table(name="v_member_mailbox")
public class VMemberMailbox extends AbstractBaseEntity implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3802384142063137749L;

	/** ID */
    @Id
	@Column(name = ID)
	public Integer id;

	/** メール区分（1応募、2スカウト、見学質問） */
	@Column(name = MAIL_KBN)
	public Integer mailKbn;

	/** 送信区分（1送信、2受信） */
	@Column(name = SEND_KBN)
	public Integer sendKbn;

	/** メールステータス */
	@Column(name = MAIL_STATUS)
	public Integer mailStatus;

	/** 送信日時 */
	@Column(name = SEND_DATETIME)
	public Date sendDatetime;

	/** スカウト受信区分 */
	@Column(name = SCOUT_RECEIVE_KBN)
	public Integer scoutReceiveKbn;

	/** 会員ID */
	@Column(name = MEMBER_ID)
	public Integer memberId;

	/** 会員名（送信時） */
	@Column(name = MEMBER_NAME)
	public String memberName;

	/** 顧客ID */
	@Column(name = CUSTOMER_ID)
	public Integer customerId;

	/** 顧客名（スカウト時は顧客名、応募時は原稿名） */
	@Column(name = CUSTOMER_NAME)
	public String customerName;

	/** 件名 */
	@Column(name = SUBJECT)
	public String subject;

	/** 本文 */
	@Column(name = BODY)
	public String body;

	public static final String VIEW_NAME = "v_member_mailbox";

	/** ID */
	public static final String ID = "id";

	/** メール区分（1応募、2スカウト、見学質問） */
	public static final String MAIL_KBN = "mail_kbn";

	/** 送信区分（1送信、2受信） */
	public static final String SEND_KBN = "send_kbn";

	/** メールステータス */
	public static final String MAIL_STATUS = "mail_status";

	/** 送信日時 */
	public static final String SEND_DATETIME = "send_datetime";

	/** スカウト受信区分 */
	public static final String SCOUT_RECEIVE_KBN = "scout_receive_kbn";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** 会員名（送信時） */
	public static final String MEMBER_NAME = "member_name";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 顧客名（スカウト時は顧客名、応募時は原稿名） */
	public static final String CUSTOMER_NAME = "customer_name";

	/** 件名 */
	public static final String SUBJECT = "subject";

	/** 本文 */
	public static final String BODY = "body";


}

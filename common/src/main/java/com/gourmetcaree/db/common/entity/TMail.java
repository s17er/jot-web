package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * メールのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_mail")
public class TMail extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_mail_id_gen")
	@SequenceGenerator(name="t_mail_id_gen", sequenceName="t_mail_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** メール区分 */
	@Column(name="mail_kbn")
	public Integer mailKbn;

	/** 送受信区分 */
	@Column(name="send_kbn")
	public Integer sendKbn;

	/** 送信者区分 */
	@Column(name="sender_kbn")
	public Integer senderKbn;

	/** 送信者ID */
	@Column(name="from_id")
	public Integer fromId;

	/** 送信者名 */
	@Column(name="from_name")
	public String fromName;

	/** 受信者ID */
	@Column(name="to_id")
	public Integer toId;

	/** 受信者名 */
	@Column(name="to_name")
	public String toName;

	/** 受信メールID */
	@Column(name="receive_id")
	public Integer receiveId;

	/** 件名 */
	@Column(name="subject")
	public String subject;

	/** 本文 */
	@Column(name="body")
	public String body;

	/** 応募ID */
	@Column(name="application_id")
	public Integer applicationId;

	/** 店舗応募ID */
	@Column(name="observate_application_id")
	public Integer observateApplicationId;

	/** アルバイト応募ID */
	@Column(name="arbeit_application_id")
	public Integer arbeitApplicationId;

	/**
	 * スカウト履歴ID
	 * @deprecated 使わない。scoutMailLogIdを使うこと。
	 */
	@Deprecated
	@Column(name="scout_history_id")
	public Integer scoutHistoryId;

	/** スカウトメールログID */
	@Column(name="scout_mail_log_id")
	public Integer scoutMailLogId;

	/** メールステータス */
	@Column(name="mail_status")
	public Integer mailStatus;

	/** 送信日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="send_datetime")
	public Date sendDatetime;

	/** 閲覧日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="reading_datetime")
	public Date readingDatetime;

	/** 親メールID */
	@Column(name="parent_mail_id")
	public Integer parentMailId;

	/** アクセスコード */
	@Column(name="access_cd")
	public String accessCd;

    /** スカウト受け取り区分 */
    @Column(name = SCOUT_RECEIVE_KBN)
    public Integer scoutReceiveKbn;

    /** 気になるフラグ */
    @Column(name = INTEREST_FLG)
    public Integer interestFlg;

    /** 表示フラグ */
    @Column(name = DISPLAY_FLG)
    public Integer displayFlg;

	/** ID */
	public static final String ID = "id";

	/** メール区分 */
	public static final String MAIL_KBN = "mail_kbn";

	/** 送受信区分 */
	public static final String SEND_KBN = "send_kbn";

	/** 送信者区分 */
	public static final String SENDER_KBN = "sender_kbn";

	/** 送信者ID */
	public static final String FROM_ID = "from_id";

	/** 送信者名 */
	public static final String FROM_NAME = "from_name";

	/** 受信者ID */
	public static final String TO_ID = "to_id";

	/** 受信者名 */
	public static final String TO_NAME = "to_name";

	/** 受信メールID */
	public static final String RECEIVE_ID = "receive_id";

	/** 件名 */
	public static final String SUBJECT = "subject";

	/** 本文 */
	public static final String BODY = "body";

	/** 応募ID */
	public static final String APPLICATION_ID = "application_id";

	/** 店舗見学応募ID */
	public static final String OBSERVATE_APPLICATION_ID = "observate_application_id";

	/** アルバイト応募ID */
	public static final String ARBEIT_APPLICATION_ID = "arbeit_application_id";


	/**
	 * スカウト履歴ID
	 * @deprecated 使わない。SCOUT_MAIL_LOG_IDを使うこと。
	 */
	@Deprecated
	public static final String SCOUT_HISTORY_ID = "scout_history_id";

	/** スカウトメールログID */
	public static final String SCOUT_MAIL_LOG_ID = "scout_mail_log_id";

	/** メールステータス */
	public static final String MAIL_STATUS = "mail_status";

	/** 送信日時 */
	public static final String SEND_DATETIME = "send_datetime";

	/** 閲覧日時 */
	public static final String READING_DATETIME = "reading_datetime";

	/** 親メールID */
	public static final String PARENT_MAIL_ID = "parent_mail_id";

	/** アクセスコード */
	public static final String ACCESS_CD = "access_cd";

    /** スカウト受け取り区分 */
    public static final String SCOUT_RECEIVE_KBN = "scout_receive_kbn";

    /** 気になるフラグ */
    public static final String INTEREST_FLG = "interest_flg";

    /** 表示フラグ */
    public static final String DISPLAY_FLG = "display_flg";

}
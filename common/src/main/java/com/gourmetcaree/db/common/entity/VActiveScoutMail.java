package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 使用可能なスカウトメールVIEWのエンティティクラスです。
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="v_active_scout_mail")
public class VActiveScoutMail extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5824374307128105951L;

	/** ID */
	@Id
	@Column(name="id")
	public Integer id;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 原稿ID */
	@Column(name="web_id")
	public Integer webId;

	/** スカウトメール残り数 */
	@Column(name="scout_remain_count")
	public Integer scoutRemainCount;

	/** 使用期限 */
	@Column(name="use_end_datetime")
	public Timestamp useEndDatetime;

	/** スカウトメール区分 */
	@Column(name="scout_mail_kbn")
	public Integer scoutMailKbn;

	/** 確認メール送信フラグ */
	@Column(name="send_confirm_flg")
	public Integer sendConfirmFlg;

	/** ID */
	public static final String ID = "id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 原稿ID */
	public static final String WEB_ID = "web_id";

	/** スカウトメール残り数 */
	public static final String SCOUT_REMAIN_COUNT = "scout_remain_count";

	/** 使用期限 */
	public static final String USE_END_DATETIME = "use_end_datetime";

	/** スカウトメール区分 */
	public static final String SCOUT_MAIL_KBN = "scout_mail_kbn";

	/** 確認メール送信フラグ */
	public static final String SEND_CONFIRM_FLG = "send_confirm_flg";

}

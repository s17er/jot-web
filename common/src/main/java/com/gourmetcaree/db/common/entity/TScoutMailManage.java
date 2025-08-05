package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_scout_mail_manage")
public class TScoutMailManage extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1185517347160007097L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_scout_mail_manage_id_gen")
	@SequenceGenerator(name="t_scout_mail_manage_id_gen", sequenceName="t_scout_mail_manage_id_seq", allocationSize=1)
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

	@Column(name="use_start_datetime")
	public Timestamp useStartDatetime;

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

	/** 使用開始日 */
	public static final String USE_START_DATETIME = "use_start_datetime";


}

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
 * 顧客ログイン履歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_customer_login_history")
public class TCustomerLoginHistory extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_customer_login_history_id_gen")
	@SequenceGenerator(name="t_customer_login_history_id_gen", sequenceName="t_customer_login_history_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 最終ログイン日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_login_datetime")
	public Date lastLoginDatetime;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** リモートアドレス */
	@Column(name="remote_address")
	public String remoteAddress;

	/** ユーザエージェント */
	@Column(name="user_agent")
	public String userAgent;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_customer_login_history";

	/** ID */
	public static final String ID ="id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 最終ログイン日時 */
	public static final String LAST_LOGIN_DATETIME = "last_login_datetime";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** リモートアドレス */
	public static final String REMOTE_ADDRESS = "remote_address";

	/** ユーザエージェント */
	public static final String USER_AGENT = "user_agent";
}
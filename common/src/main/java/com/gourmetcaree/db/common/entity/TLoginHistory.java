package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ログイン履歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_login_history")
public class TLoginHistory extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_login_history_id_gen")
	@SequenceGenerator(name="t_login_history_id_gen", sequenceName="t_login_history_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

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

	/** 会員エンティティ */
	@OneToOne
	@JoinColumn(name="member_id")
	public MMember mMember;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_login_history";

	/** ID */
	public static final String ID ="id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** 最終ログイン日時 */
	public static final String LAST_LOGIN_DATETIME = "last_login_datetime";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** リモートアドレス */
	public static final String REMOTE_ADDRESS = "remote_address";

	/** ユーザエージェント */
	public static final String USER_AGENT = "user_agent";

	/** 会員エンティティ */
	public static final String M_MEMBER = "m_member";
}
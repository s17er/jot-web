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
 * 自動ログインのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_auto_login")
public class TAutoLogin extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3499730923407967444L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_auto_login_id_gen")
	@SequenceGenerator(name="t_auto_login_id_gen", sequenceName="t_auto_login_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** 端末情報 */
	@Column(name="terminal_info")
	public String terminalInfo;

	/** 識別ID */
	@Column(name="identified_cd")
	public String identifiedCd;

	/** ログインID */
	@Column(name="login_id")
	public String loginId;

	/** ユーザー区分 */
	@Column(name="user_kbn")
	public Integer userKbn;

	/** ユーザエージェント */
	@Column(name="user_agent")
	public String userAgent;

	/** 自動ログイン更新日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="auto_login_update_datetime")
	public Date autoLoginUpdateDatetime;

	/** 有効フラグ */
	@Column(name="valid_flg")
	public Integer validFlg;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_auto_login";

	/** ID */
	public static final String ID ="id";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

	/** 端末情報 */
	public static final String TERMINAL_INFO = "terminal_info";

	/** 識別ID */
	public static final String IDENTIFIED_CD = "identified_cd";

	/** ログインID */
	public static final String LOGIN_ID = "login_id";

	/** ユーザ区分 */
	public static final String USER_KBN = "user_kbn";

	/** ユーザエージェント */
	public static final String USER_AGENT = "user_agent";

	/** 自動ログイン更新日時 */
	public static final String AUTO_LOGIN_UPDATE_DATETIME = "auto_login_update_datetime";

	/** 有効フラグ */
	public static final String VALID_FLG = "valid_flg";
}
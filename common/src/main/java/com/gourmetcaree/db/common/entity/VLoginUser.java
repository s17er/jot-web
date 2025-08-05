package com.gourmetcaree.db.common.entity;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Tue Aug 26 21:21:01 JST 2008
 */
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ユーザ情報・店舗情報ビュー用エンティティ
 * @author Takahiro Ando
 * @version 1.0
 *
 */
@Entity
@Table(name="v_login_user")
public class VLoginUser extends AbstractBaseEntity implements Serializable {

	/** シリアルバージョンUID */
	public static final long serialVersionUID = -2623337075939572948L;

	/** ユーザID */
	@Column(name="user_id")
	public String userId;

	/** パスワード */
	public String password;

	/** 名前 */
	public String name;

	/** ユーザ区分 */
	@Column(name="user_kbn")
	public int userKbn;

	/** 権限レベル */
	@Column(name="auth_level")
	public String authLevel;

	/** 店舗ID */
	@Column(name="shop_id")
	public int shopId;

	/** ログイン可否フラグ */
	@Column(name="login_kahi_flg")
	public int loginKahiFlg;

	/** 有効開始日付 */
	@Column(name="yuko_start_date")
	public Date yukoStartDate;

	/** 有効終了日付 */
	@Column(name="yuko_end_date")
	public Date yukoEndDate;

	/** ユーザ最終更新日時 */
	@Column(name="user_last_update")
	public Timestamp userLastUpdate;

	/** 店舗名 */
	@Column(name="shop_name")
	public String shopName;

	/** 営業担当者ID */
	@Column(name="sales_id")
	public String salesId;

	/** 編集担当者ID */
	@Column(name="editor_id")
	public String editorId;

	/** 請求書番号 */
	@Column(name="invoice_no")
	public String invoiceNo;

	/** 管理用電話番号 */
	@Column(name="admin_telephone_no")
	public String adminTelephoneNo;

	/** 管理用メールアドレス */
	@Column(name="admin_mail_address")
	public String adminMailAddress;

	/** 店舗最終更新日時 */
	@Column(name="shop_last_update")
	public Timestamp shopLastUpdate;

}

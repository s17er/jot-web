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
 * 応募テストのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_application_test")
public class TApplicationTest extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_application_test_id_gen")
	@SequenceGenerator(name="t_application_test_id_gen", sequenceName="t_application_test_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** アクセスコード */
	@Column(name="access_cd")
	public String accessCd;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 営業担当者ID */
	@Column(name="sales_id")
	public Integer salesId;

	/** 完了送信メインメールアドレス */
	@Column(name="main_mail")
	public String mainMail;

	/** 完了送信サブメールアドレス */
	@Column(name="sub_mail")
	public String subMail;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** コメント */
	@Column(name="comment")
	public String comment;

	/** アクセスフラグ */
	@Column(name="access_flg")
	public Integer accessFlg;

	/** アクセス日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="access_datetime")
	public Date accessDatetime;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_application_test";

	/** ID */
	public static final String ID ="id";
	/** アクセスコード */
	public static final String ACCESS_CD = "access_cd";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 営業担当者ID */
	public static final String SALES_ID = "sales_id";

	/** 完了送信メインメールアドレス */
	public static final String MAIN_MAIL = "main_mail";

	/** 完了送信サブメールアドレス */
	public static final String SUB_MAIL = "sub_mail";

	/** WEBデータID */
	public static final String WEB_ID = "web_id";

	/** コメント */
	public static final String COMMENT = "comment";

	/** アクセスフラグ */
	public static final String ACCESS_FLG = "access_flg";

	/** アクセス日時 */
	@Temporal(TemporalType.TIMESTAMP)
	public static final String ACCESS_DATETIME = "access_datetime";
}
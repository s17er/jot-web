package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * いたずらメールマスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_mischief_application")
public class MMischiefApplication extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_mischief_application_id_gen")
	@SequenceGenerator(name="m_mischief_application_id_gen", sequenceName="m_mischief_application_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** メールアドレス */
	@Column(name="mail_address")
	public String mailAddress;

	/** 電話番号1 */
	@Column(name="phone_no1")
	public String phoneNo1;

	/** 電話番号2 */
	@Column(name="phone_no2")
	public String phoneNo2;

	/** 電話番号3 */
	@Column(name="phone_no3")
	public String phoneNo3;

	/** 備考 */
	@Column(name="note")
	public Integer note;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_mischief_application";

	/** ID */
	public static final String ID ="id";

	/** メールアドレス */
	public static final String MAIL_ADDRESS ="mail_address";

	/** 電話番号1 */
	public static final String PHONE_NO1 = "phone_no1";

	/** 電話番号2 */
	public static final String PHONE_NO2 = "phone_no2";

	/** 電話番号3 */
	public static final String PHONE_NO3 = "phone_no3";

	/** 備考 */
	public static final String NOTE = "note";

}
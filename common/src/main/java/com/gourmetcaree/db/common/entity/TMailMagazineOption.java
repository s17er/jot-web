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
 * メルマガオプションのエンティティクラスです。
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="t_mail_magazine_option")
public class TMailMagazineOption extends AbstractCommonEntity implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2459126882137151441L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_mail_magazine_option_id_gen")
	@SequenceGenerator(name="t_mail_magazine_option_id_gen", sequenceName="t_mail_magazine_option_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** メルマガID */
	@Column(name="mail_magazine_id")
	public Integer mailMagazineId;

	/** メルマガ区分 */
	@Column(name="mailmagazine_kbn")
	public Integer mailmagazineKbn;

	/** メルマガオプション区分 */
	@Column(name="mailmagazine_option_kbn")
	public Integer mailmagazineOptionKbn;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** オプションの値 */
	@Column(name="option_value")
	public String optionValue;

	/** メルマガ配信予定日 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="delivery_schedule_datetime")
	public Date deliveryScheduleDatetime;

	/** 配信形式 */
	@Column(name="delivery_type")
	public Integer deliveryType;

	/** ID */
	public static final String ID = "id";
	/** メルマガID */
	public static final String MAIL_MAGAZINE_ID = "mail_magazine_id";
	/** メルマガ区分 */
	public static final String MAIL_MAGAZINE_KBN = "mailmagazine_kbn";
	/** メルマガオプション区分 */
	public static final String MAIL_MAGAZINE_OPTION_KBN = "mailmagazine_option_kbn";
	/** エリアコード */
	public static final String AREA_CD = "area_cd";
	/** オプションの値 */
	public static final String OPTION_VALUE = "option_value";
	/** メルマガ配信予定日 */
	public static final String DELIVERY_SCHEDULE_DATETIME = "delivery_schedule_datetime";
	/** 配信形式 */
	public static final String DELIVERY_TYPE = "delivery_type";



}

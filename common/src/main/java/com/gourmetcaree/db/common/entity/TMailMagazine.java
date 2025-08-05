package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * メルマガのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_mail_magazine")
public class TMailMagazine extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_mail_magazine_id_gen")
	@SequenceGenerator(name="t_mail_magazine_id_gen", sequenceName="t_mail_magazine_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 登録日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registration_datetime")
	public Date registrationDatetime;

	/** 配信予定日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="delivery_schedule_datetime")
	public Date deliveryScheduleDatetime;

	/** 配信日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="delivery_datetime")
	public Date deliveryDatetime;

	/** 配信先区分 */
	@Column(name="delivery_kbn")
	public Integer deliveryKbn;

	/** メルマガ区分 */
	@Column(name="mailmagazine_kbn")
	public Integer mailmagazineKbn;

	/** 配信フラグ */
	@Column(name="delivery_flg")
	public Integer deliveryFlg;

	/** メルマガ詳細のリスト */
	@OneToMany(mappedBy = "tMailMagazine")
	public List<TMailMagazineDetail> tMailMagazineDetailList;

	/** メルマガ配信先のリスト */
	@OneToMany(mappedBy = "tMailMagazine")
	public List<TMailMagazineDelivery> tMailMagazineDeliveryList;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_mail_magazine";

	/** ID */
	public static final String ID = "id";

	/** 登録日時 */
	public static final String REGISTRATION_DATETIME = "registration_datetime";

	/** 配信予定日時 */
	public static final String DELIVERY_SCHEDULE_DATETIME = "delivery_schedule_datetime";

	/** 配信日時 */
	public static final String DELIVERY_DATETIME = "delivery_datetime";

	/** 配信先区分 */
	public static final String DELIVERY_KBN = "delivery_kbn";

	/** メルマガ区分 */
	public static final String MAILMAGAZINE_KBN = "mailmagazine_kbn";

	/** 配信フラグ */
	public static final String DELIVERY_FLG = "delivery_flg";

	/** メルマガ詳細のリスト */
	public static final String T_MAIL_MAGAZINE_DETAIL_LIST = "t_mail_magazine_detail_list";

	/** メルマガ配信先のリスト */
	public static final String T_MAIL_MAGAZINE_DELIVERY_LIST = "t_mail_magazine_delivery_list";
}
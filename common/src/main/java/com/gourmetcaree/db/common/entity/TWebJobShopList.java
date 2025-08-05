package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEBデータ職種店舗のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_web_job_shop_list")
public class TWebJobShopList extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 295935770751562792L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_job_shop_list_id_gen")
	@SequenceGenerator(name="t_web_job_shop_list_id_gen", sequenceName="t_web_job_shop_list_id_seq", allocationSize=1)
	@Column(name=ID)
	public Integer id;

	/** WEBデータ職種ID */
	@Column(name=WEB_JOB_ID)
	public Integer webJobId;

	/** 店舗ID */
	@Column(name=SHOP_LIST_ID)
	public Integer shopListId;

	/** WEBデータ職種エンティティ */
	@ManyToOne
	@JoinColumn(name=WEB_JOB_ID)
	public TWebJob tWebJob;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_job_shop_list";

	/** ID */
	public static final String ID ="id";
	/** WEB職種データID */
	public static final String WEB_JOB_ID = "web_job_id";
	/** 店舗ID */
	public static final String SHOP_LIST_ID = "shop_list_id";

	/** WEBデータ職種エンティティ */
	public static final String T_WEB_JOB ="t_web_job";

}
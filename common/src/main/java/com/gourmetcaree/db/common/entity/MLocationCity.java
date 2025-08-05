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
 * 地域市区町村マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_location_city")
public class MLocationCity extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3156106742628044874L;

	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_location_city_id_gen")
	@SequenceGenerator(name = "m_location_city_id_gen", sequenceName = "m_location_city_id_seq", allocationSize = 1)
	@Column(name = ID)
	public Integer id;

	/** 地域ID */
	@Column(name = LOCATION_ID)
	public Integer locationId;

	/** 市区町村コード */
	@Column(name = CITY_CD)
	public String cityCd;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_location";

	/** ID */
	public static final String ID = "id";

	/** 地域ID */
	public static final String LOCATION_ID = "location_id";

	/** 市区町村コード */
	public static final String CITY_CD = "city_cd";
}
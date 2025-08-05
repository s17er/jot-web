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
 * 地域マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_location")
public class MLocation extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7072874351403699298L;

	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_location_id_gen")
	@SequenceGenerator(name = "m_location_id_gen", sequenceName = "m_location_id_seq", allocationSize = 1)
	@Column(name = ID)
	public Integer id;

	/** 都道府県コード */
	@Column(name = PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 地域名 */
	@Column(name = LOCATION_NAME)
	public String locationName;

	/** 表示順 */
	@Column(name = DISPLAY_ORDER)
	public Integer displayOrder;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_location";

	/** ID */
	public static final String ID = "id";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 地域名 */
	public static final String LOCATION_NAME = "location_name";

	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";
}
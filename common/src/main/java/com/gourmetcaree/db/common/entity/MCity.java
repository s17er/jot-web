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
 * グルメキャリーリニューアル後の市区町村マスタのエンティティクラスです。
 * 市区町村コードは、JIS X 0402のチェックディジット有りに準拠します。
 * @version 1.0
 */
@Entity
@Table(name="m_city")
public class MCity extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7992424105691589368L;

	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_city_id_gen")
	@SequenceGenerator(name = "m_city_id_gen", sequenceName = "m_city_id_seq", allocationSize = 1)
	@Column(name = ID)
	public Integer id;

	/** 都道府県コード */
	@Column(name = PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 市区町村コード */
	@Column(name = CITY_CD)
	public String cityCd;

	/** 市区町村名 */
	@Column(name = CITY_NAME)
	public String cityName;

	/** 市区町村名(カナ) */
	@Column(name = CITY_NAME_KANA)
	public String cityNameKana;

	/** 表示順 */
	@Column(name = DISPLAY_ORDER)
	public Integer displayOrder;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_city";

	/** ID */
	public static final String ID = "id";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 市区町村コード */
	public static final String CITY_CD = "city_cd";

	/** 市区町村名 */
	public static final String CITY_NAME = "city_name";

	/** 市区町村名(カナ) */
	public static final String CITY_NAME_KANA = "city_name_kana";

	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";



}
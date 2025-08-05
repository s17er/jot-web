package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * グルメキャリーリニューアル後の駅マスタのエンティティクラスです。
 * 駅データ.jpの仕様に基づきます
 * @version 1.0
 */
@Entity
@Table(name="m_r_station")
public class MRStation extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6721803175698845175L;

	/** 駅コード */
	@Id
	@Column(name = STATION_CD)
	public Integer stationCd;

	/** 駅グループコード */
	@Column(name = STATION_G_CD)
	public Integer stationGCd;

	/** 駅名称 */
	@Column(name = STATION_NAME)
	public String stationName;

	/** 駅名称(カナ) */
	@Column(name = STATION_NAME_K)
	public String stationNameK;

	/** 駅名称(ローマ字) */
	@Column(name = STATION_NAME_R)
	public String stationNameR;

	/** 路線コード */
	@Column(name = LINE_CD)
	public Integer lineCd;

	/** 都道府県コード */
	@Column(name = PREF_CD)
	public Integer prefCd;

	/** 駅郵便番号 */
	@Column(name = POST)
	public String post;

	/** 住所 */
	@Column(name = ADD)
	public String add;

	/** 経度 */
	@Column(name = LON)
	public Double lon;

	/** 緯度 */
	@Column(name = LAT)
	public Double lat;

	/** 開業年月日 */
	@Column(name = OPEN_YMD)
	public Date openYmd;

	/** 廃止年月日 */
	@Column(name = CLOSE_YMD)
	public Date closeYmd;

	/** 状態 */
	@Column(name = E_STATUS)
	public Integer eStatus;

	/** 並び順 */
	@Column(name = E_SORT)
	public Integer eSort;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_r_station";

	/** 駅コード */
	public static final String STATION_CD = "station_cd";

	/** 駅グループコード */
	public static final String STATION_G_CD = "station_g_cd";

	/** 駅名称 */
	public static final String STATION_NAME = "station_name";

	/** 駅名称(カナ) */
	public static final String STATION_NAME_K = "station_name_k";

	/** 駅名称(ローマ字) */
	public static final String STATION_NAME_R = "station_name_r";

	/** 路線コード */
	public static final String LINE_CD = "line_cd";

	/** 都道府県コード */
	public static final String PREF_CD = "pref_cd";

	/** 駅郵便番号 */
	public static final String POST = "post";

	/** 住所 */
	public static final String ADD = "add";

	/** 経度 */
	public static final String LON = "lon";

	/** 緯度 */
	public static final String LAT = "lat";

	/** 開業年月日 */
	public static final String OPEN_YMD = "open_ymd";

	/** 廃止年月日 */
	public static final String CLOSE_YMD = "close_ymd";

	/** 状態 */
	public static final String E_STATUS = "e_status";

	/** 並び順 */
	public static final String E_SORT = "e_sort";


	/** 状態:運用中 */
	public static final int IN_OPERATIONAL = 0;
	/** 状態:運用前 */
	public static final int BEFORE_OPERATIONAL = 1;
	/** 状態:廃止 */
	public static final int OBSOLETE = 2;

}
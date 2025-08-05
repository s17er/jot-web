package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * グルメキャリーリニューアル後の鉄道会社マスタのエンティティクラスです。
 * 駅データ.jpの仕様に基づきます
 * @version 1.0
 */
@Entity
@Table(name="m_r_join_station")
public class MRJoinStation extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5483892436704117058L;

	/** 路線コード */
	@Id
	@Column(name = LINE_CD)
	public Integer lineCd;

	/** 駅コード１ */
	@Id
	@Column(name = STATION_CD1)
	public Integer stationCd1;

	/** 駅コード２ */
	@Id
	@Column(name = STATION_CD2)
	public Integer stationCd2;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_r_join_station";

	/** 路線コード */
	public static final String LINE_CD = "line_cd";

	/** 駅コード１ */
	public static final String STATION_CD1 = "station_cd1";

	/** 駅コード２ */
	public static final String STATION_CD2 = "station_cd2";


}
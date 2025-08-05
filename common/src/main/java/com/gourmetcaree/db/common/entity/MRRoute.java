package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * グルメキャリーリニューアル後の路線マスタのエンティティクラスです。
 * 駅データ.jpの仕様に基づきます
 * @version 1.0
 */
@Entity
@Table(name="m_r_route")
public class MRRoute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -841895098529845060L;

	/** 路線コード */
	@Id
	@Column(name = LINE_CD)
	public Integer lineCd;

	/** 事業者コード */
	@Column(name = COMPANY_CD)
	public Integer companyCd;

	/** 路線名称(一般) */
	@Column(name = LINE_NAME)
	public String lineName;

	/** 路線名称(一般・カナ) */
	@Column(name = LINE_NAME_K)
	public String lineNameK;

	/** 路線名称(正式名称) */
	@Column(name = LINE_NAME_H)
	public String lineNameH;

	/** 路線カラー（コード） */
	@Column(name = LINE_COLOR_C)
	public String lineColorC;

	/** 路線カラー(名称） */
	@Column(name = LINE_COLOR_T)
	public String lineColorT;

	/** 路線区分 */
	@Column(name = LINE_TYPE)
	public Integer lineType;

	/** 中央経度 */
	@Column(name = LON)
	public Double lon;

	/** 中央緯度 */
	@Column(name = LAT)
	public Double lat;

	/** GoogleMap倍率 */
	@Column(name = ZOOM)
	public Integer zoom;

	/** 状態 */
	@Column(name = E_STATUS)
	public Integer eStatus;

	/** 並び順 */
	@Column(name = E_SORT)
	public Integer eSort;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_r_route";

	/** 路線コード */
	public static final String LINE_CD = "line_cd";

	/** 事業者コード */
	public static final String COMPANY_CD = "company_cd";

	/** 路線名称(一般) */
	public static final String LINE_NAME = "line_name";

	/** 路線名称(一般・カナ) */
	public static final String LINE_NAME_K = "line_name_k";

	/** 路線名称(正式名称) */
	public static final String LINE_NAME_H = "line_name_h";

	/** 路線カラー（コード） */
	public static final String LINE_COLOR_C = "line_color_c";

	/** 路線カラー(名称） */
	public static final String LINE_COLOR_T = "line_color_t";

	/** 路線区分 */
	public static final String LINE_TYPE = "line_type";

	/** 中央経度 */
	public static final String LON = "lon";

	/** 中央緯度 */
	public static final String LAT = "lat";

	/** GoogleMap倍率 */
	public static final String ZOOM = "zoom";

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
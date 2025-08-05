package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.seasar.extension.jdbc.annotation.ReferentialConstraint;

/**
 * 都道府県、鉄道会社、路線、駅をグループ化したVIEWのエンティティクラスです。
 * @author t_shiroumaru
 *
 */
@Entity
@Table(name="v_station_group")
public class VStationGroup implements Serializable{

	private static final long serialVersionUID = 1522292726675373821L;

	/** 都道府県コード */
	@Column(name=PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 都道府県名 */
	@Column(name=PREFECTURES_NAME)
	public String prefecturesName;

	/** 鉄道会社コード */
	@Column(name=COMPANY_CD)
	public Integer companyCd;

	/** 鉄道会社名 */
	@Column(name=COMPANY_NAME)
	public String companyName;

	/** 鉄道会社(カナ) */
	@Column(name=COMPANY_NAME_K)
	public String companyNameK;

	/** 鉄道会社(略称) */
	@Column(name=COMPANY_NAME_R)
	public String companyNameR;

	/** 路線コード */
	@Column(name=LINE_CD)
	public Integer lineCd;

	/** 路線名 */
	@Column(name=LINE_NAME)
	public String lineName;

	/** 路線名(カナ) */
	@Column(name=LINE_NAME_K)
	public String lineNameK;

	/** 駅コード */
	@Column(name=STATION_CD)
	public Integer stationCd;

	/** 駅名 */
	@Column(name=STATIN_NAME)
	public String stationName;

	/** 並び順 */
	@Column(name=E_SORT)
	public Integer eSort;

	/** ターミナル駅マスタ */
	@ReferentialConstraint(enable = false)
	@OneToOne
	@JoinColumn(name = STATION_CD, referencedColumnName = STATION_CD)
	public MTerminalStation mTerminalStation;

	/** 店舗路線 */
	@ReferentialConstraint(enable = false)
	@OneToOne
	@JoinColumn(name = STATION_CD, referencedColumnName = STATION_CD)
	public TShopListLine tShopListLine;


	/** テーブル名 */
	public static final String TABLE_NAME = "v_station_group";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 都道府県名 */
	public static final String PREFECTURES_NAME = "prefectures_name";

	/** 鉄道会社コード */
	public static final String COMPANY_CD = "company_cd";

	/** 鉄道会社名 */
	public static final String COMPANY_NAME = "company_name";

	/** 鉄道会社名(カナ) */
	public static final String COMPANY_NAME_K = "company_name_k";

	/** 鉄道会社名(略称) */
	public static final String COMPANY_NAME_R = "company_name_r";

	/** 路線コード */
	public static final String LINE_CD = "line_cd";

	/** 路線名 */
	public static final String LINE_NAME = "line_name";

	/** 路線名(カナ) */
	public static final String LINE_NAME_K = "line_name_k";

	/** 駅コード */
	public static final String STATION_CD = "station_cd";

	/** 駅名 */
	public static final String STATIN_NAME = "station_name";

	/** 並び順 */
	public static final String E_SORT = "e_sort";

}

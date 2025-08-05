package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 路線と都道府県のコードのサマリviewクラスです。
 * @version 1.0
 */
@Entity
@Table(name="v_summary_route_pref")
public class VSummaryRoutePref implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7991324115691589368L;

	/** 都道府県コード */
	@Column(name = PREF_CD)
	public Integer prefCd;

	/** 路線コード */
	@Column(name = LINE_CD)
	public Integer lineCd;

	/** テーブル名 */
	public static final String TABLE_NAME = "v_summary_route_pref";

	/** 都道府県コード */
	public static final String PREF_CD = "pref_cd";

	/** 路線コード */
	public static final String LINE_CD = "line_cd";


}
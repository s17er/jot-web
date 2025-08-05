package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 鉄道会社と都道府県のコードのサマリviewクラスです。
 * @version 1.0
 */
@Entity
@Table(name="v_summary_railroad_pref")
public class VSummaryRailroadPref implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7991324105691589368L;

	/** 都道府県コード */
	@Column(name = PREF_CD)
	public Integer prefCd;

	/** 鉄道事業者コード */
	@Column(name = COMPANY_CD)
	public Integer companyCd;

	/** テーブル名 */
	public static final String TABLE_NAME = "v_summary_railroad_pref";

	/** 都道府県コード */
	public static final String PREF_CD = "pref_cd";

	/** 事業者コード */
	public static final String COMPANY_CD = "company_cd";

}
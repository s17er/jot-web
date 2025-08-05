package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * WEBに設定された系列店舗の業態viewクラスです。
 * @version 1.0
 */
@Entity
@Table(name="v_web_industry_kbn")
public class VWebIndustryKbn implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7911324115691589368L;

	/** WEBID */
	@Column(name = WEB_ID)
	public Integer webId;

	/** 業態区分 */
	@Column(name = INDUSTRY_KBN)
	public Integer industryKbn;

	/** テーブル名 */
	public static final String TABLE_NAME = "v_web_industry_kbn";

	/** WEBID */
	public static final String WEB_ID = "web_id";

	/** 業態区分 */
	public static final String INDUSTRY_KBN = "industry_kbn";


}
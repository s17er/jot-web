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
@Table(name="m_r_railroad")
public class MRRailroad extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7992424105691589368L;

	/** 事業者コード */
	@Id
	@Column(name = COMPANY_CD)
	public Integer companyCd;

	/** 鉄道コード */
	@Column(name = RR_CD)
	public Integer rrCd;

	/** 事業者名(一般) */
	@Column(name = COMPANY_NAME)
	public String companyName;

	/** 事業者名(一般・カナ) */
	@Column(name = COMPANY_NAME_K)
	public String companyNameK;

	/** 事業者名(正式名称) */
	@Column(name = COMPANY_NAME_H)
	public String companyNameH;

	/** 事業者名(略称) */
	@Column(name = COMPANY_NAME_R)
	public String companyNameR;

	/** Webサイト */
	@Column(name = COMPANY_URL)
	public String companyUrl;

	/** 事業者区分 */
	@Column(name = COMPANY_TYPE)
	public Integer companyType;

	/** 状態 */
	@Column(name = E_STATUS)
	public Integer eStatus;

	/** 並び順 */
	@Column(name = E_SORT)
	public Integer eSort;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_r_railroad";

	/** 事業者コード */
	public static final String COMPANY_CD = "company_cd";

	/** 鉄道コード */
	public static final String RR_CD = "rr_cd";

	/** 事業者名(一般) */
	public static final String COMPANY_NAME = "company_name";

	/** 事業者名(一般・カナ) */
	public static final String COMPANY_NAME_K = "company_name_k";

	/** 事業者名(正式名称) */
	public static final String COMPANY_NAME_H = "company_name_h";

	/** 事業者名(略称) */
	public static final String COMPANY_NAME_R = "company_name_r";

	/** Webサイト */
	public static final String COMPANY_URL = "company_url";

	/** 事業者区分 */
	public static final String COMPANY_TYPE = "company_type";

	/** 状態 */
	public static final String E_STATUS = "e_status";

	/** 並び順 */
	public static final String E_SORT = "e_sort";

}
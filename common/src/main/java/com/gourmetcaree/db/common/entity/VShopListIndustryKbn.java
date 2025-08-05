package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 系列店舗一覧の業態区分を集計したVIEWのエンティティクラスです。
 * @author Otani
 *
 */
@Entity
@Table(name="v_shop_list_industry_kbn")
public class VShopListIndustryKbn implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5824314307128105951L;

	/** 顧客ID */
	@Column(name=CUSTOMER_ID)
	public Integer customerId;

	/** 業態区分 */
	@Column(name=INDUSTRY_KBN)
	public Integer industryKbn;

    @ManyToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 業態区分 */
	public static final String INDUSTRY_KBN = "industry_kbn";
}

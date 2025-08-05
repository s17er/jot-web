package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 顧客に紐づく店舗数を集計したVIEWのエンティティクラスです。
 * @author Otani
 *
 */
@Entity
@Table(name="v_customer_shop_count")
public class VCustomerShopCount implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5814314307128105951L;

	/** 顧客ID */
	@Column(name=CUSTOMER_ID)
	public Integer customerId;

	/** 店舗数 */
	@Column(name=COUNT)
	public Integer count;

	/** 顧客マスタエンティティ */
    @OneToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 業態区分 */
	public static final String COUNT = "count";
}

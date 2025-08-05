package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 系列店舗一覧の海外エリアを集計したVIEWのエンティティクラスです。
 * @author Otani
 *
 */
@Entity
@Table(name="v_shop_list_shutoken_foreign_area_kbn")
public class VShopListShutokenForeignAreaKbn implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3351991029276954333L;

	/** 顧客ID */
	@Column(name=CUSTOMER_ID)
	public Integer customerId;

	/** 海外エリア区分 */
	@Column(name=SHUTOKEN_FOREIGN_AREA_KBN)
	public Integer shutokenForeignAreaKbn;

    @ManyToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 海外エリア区分 */
	public static final String SHUTOKEN_FOREIGN_AREA_KBN = "shutoken_foreign_area_kbn";
}

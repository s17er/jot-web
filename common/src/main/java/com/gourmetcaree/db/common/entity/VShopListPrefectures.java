package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 系列店舗一覧の都道府県を集計したVIEWのエンティティクラスです。
 * @author Otani
 *
 */
@Entity
@Table(name="v_shop_list_prefectures")
public class VShopListPrefectures implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5824314307128115951L;

	/** 顧客ID */
	@Column(name=CUSTOMER_ID)
	public Integer customerId;

	/** アルバイト都道府県 */
	@Column(name=PREFECTURES_CD)
	public Integer prefecturesCd;

    @ManyToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 都道府県 */
	public static final String PREFECTURES_CD = "prefectures_cd";
}

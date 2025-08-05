package com.gourmetcaree.db.common.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 系列店舗ラベル
 *
 */
@Entity
@Table(name="t_shop_list_label")
public class TShopListLabel extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7592113163901996862L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_shop_list_label_id_gen")
	@SequenceGenerator(name="t_shop_list_label_id_gen", sequenceName="t_shop_list_label_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 顧客ID */
	@Column(name=CUSTOMER_ID)
	public Integer customerId;

	/** ラベル名 */
	@Column(name=LABEL_NAME)
	public String labelName;

	/** 表示順 */
	@Column(name=DISPLAY_ORDER)
	public Integer displayOrder;

	/** 系列店舗エンティティ */
	@ManyToOne
	@JoinColumn(name=CUSTOMER_ID)
	public MCustomer mCustomer;

	/** 系列店舗ラベルグループエンティティリスト */
	@OneToMany(mappedBy="tShopListLabel")
	public List<TShopListLabelGroup> tShopListLabelGroupList;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_list_attribute";

	/** ID */
	public static final String ID = "id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** ラベル名 */
	public static final String LABEL_NAME = "label_name";

	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";

	/** 顧客 */
	public static final String M_CUSTOMER = "mCustomer";

	/** 系列店舗ラベルグループリスト */
	public static final String T_SHOP_LIST_LABEL_GROUP_LIST = "tShopListLabelGroupList";

}

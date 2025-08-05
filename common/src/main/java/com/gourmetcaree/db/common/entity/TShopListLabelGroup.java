package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 系列店舗ラベルグループ
 *
 */
@Entity
@Table(name="t_shop_list_label_group")
public class TShopListLabelGroup extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7562163163901996862L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_shop_list_label_group_id_gen")
	@SequenceGenerator(name="t_shop_list_label_group_id_gen", sequenceName="t_shop_list_label_group_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;


	/** 店舗一覧ID */
	@Column(name=SHOP_LIST_ID)
	public Integer shopListId;


	/** 店舗一覧ラベルID */
	@Column(name=SHOP_LIST_LABEL_ID)
	public Integer shopListLabelId;

	/** 表示順 */
	@Column(name=DISPLAY_ORDER)
	public Integer displayOrder;

	/** 系列店舗エンティティ */
	@ManyToOne
	@JoinColumn(name=SHOP_LIST_ID)
	public TShopList tShopList;

	/** 系列店舗ラベルエンティティ */
	@ManyToOne
	@JoinColumn(name=SHOP_LIST_LABEL_ID)
	public TShopListLabel tShopListLabel;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_list_attribute";

	/** ID */
	public static final String ID = "id";

	/** 店舗一覧ID */
	public static final String SHOP_LIST_ID = "shop_list_id";

	/** 店舗一覧ラベルID */
	public static final String SHOP_LIST_LABEL_ID = "shop_list_label_id";

	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";

	/** 系列店舗 */
	public static final String T_SHOP_LIST = "tShopList";

	/** 系列店舗ラベル */
	public static final String T_SHOP_LIST_LABEL = "tShopListLabel";

}

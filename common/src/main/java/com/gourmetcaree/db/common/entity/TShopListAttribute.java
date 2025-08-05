package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 店舗一覧属性
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="t_shop_list_attribute")
public class TShopListAttribute extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7561163163901996862L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_shop_list_attribute_id_gen")
	@SequenceGenerator(name="t_shop_list_attribute_id_gen", sequenceName="t_shop_list_attribute_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;


	/** 店舗一覧ID */
	@Column(name="shop_list_id")
	public Integer shopListId;

	/** 属性コード */
	@Column(name="attribute_cd")
	public String attributeCd;

	/** 属性値 */
	@Column(name="attribute_value")
	public Integer attributeValue;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_list_attribute";

	/** ID */
	public static final String ID = "id";

	/** 店舗一覧ID */
	public static final String SHOP_LIST_ID = "shop_list_id";

	/** 属性コード */
	public static final String ATTRIBUTE_CD = "attribute_cd";

	/** 属性値 */
	public static final String ATTRIBUTE_VALUE = "attribute_value";


}

package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 店舗データ用のタグ紐づけのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_shop_list_tag_mapping")
public class MShopListTagMapping extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_shop_list_tag_mapping_id_gen")
	@SequenceGenerator(name="m_shop_list_tag_mapping_id_gen", sequenceName="m_shop_list_tag_mapping_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 店舗タグID */
	@Column(name = "shop_list_tag_id")
	public Integer shopListTagId;

	/** 店舗一覧ID */
	@Column(name = "shop_list_id")
	public Integer shopListId;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_shop_list_tag_mapping";

	/** ID */
	public static final String ID ="id";

	/** 店舗タグID */
	public static final String SHOP_LIST_TAG_ID = "shop_list_tag_id";

	/** 店舗一覧ID */
	public static final String SHOP_LIST_ID = "shop_list_id";

}
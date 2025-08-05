package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;

/**
 * 店舗データ用のタグのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_shop_list_tag")
@Getter
public class MShopListTag extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_shop_list_tag_id_gen")
	@SequenceGenerator(name="m_shop_list_tag_id_gen", sequenceName="m_shop_list_tag_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 店舗タグ名 */
	@Column(name = "shop_list_tag_name")
	public String shopListTagName;

	/** 店舗タグコード（インディード用） */
	@Column(name = "code")
	public String code;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_shop_list_tag";

	/** ID */
	public static final String ID ="id";

	/** 店舗タグ名 */
	public static final String SHOP_LIST_TAG_NAME = "shop_list_tag_name";

}
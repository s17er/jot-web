package com.gourmetcaree.arbeitsys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * サブエリアマスタ
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="mst_subarea")
public class MstSubArea {


	/** ID */
	@Column(name = ID)
	public Integer id;

	/** 都道府県ID */
	@Column(name = TODOUHUKEN_ID)
	public Integer todouhukenId;

	/** エリアID */
	@Column(name = AREA_ID)
	public Integer areaId;

	/** 名称 */
	@Column(name = NAME)
	public String name;

	/** ID */
	public static final String ID = "id";

	/** 都道府県ID */
	public static final String TODOUHUKEN_ID = "todouhuken_id";

	/** エリアID */
	public static final String AREA_ID = "area_id";

	/** 名称 */
	public static final String NAME = "name";
}

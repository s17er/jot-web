package com.gourmetcaree.arbeitsys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * グルメdeバイト用mst_areaエンティティです。<br />
 * 市区町村のデータ用
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="mst_area")
public class MstArea implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4485645940676098617L;

	/** ID */
	@Id
	@Column(name="id")
	public Integer id;

	/** 都道府県ID */
	@Column(name="todouhuken_id")
	public Integer todouhukenId;

	/** 名前 */
	@Column(name="name")
	public String name;


	/** ID */
	public static final String ID = "id";

	/** 都道府県ID */
	public static final String TODOUHUKEN_ID = "todouhuken_id";

	/** 名前 */
	public static final String NAME = "name";

}

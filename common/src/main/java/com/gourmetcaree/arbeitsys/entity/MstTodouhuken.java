package com.gourmetcaree.arbeitsys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * グルメdeバイト用mst_todouhukenエンティティです。<br />
 * 都道府県用
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="mst_todouhuken")
public class MstTodouhuken implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2790761452834993975L;

	/** ID */
	@Id
	@Column(name="id")
	public Integer id;

	/** 都道府県名 */
	@Column(name="name")
	public String name;


	/** ID */
	public static final String ID = "id";

	/** 都道府県名 */
	public static final String NAME = "name";

}

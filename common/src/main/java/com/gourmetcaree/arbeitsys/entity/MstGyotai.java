package com.gourmetcaree.arbeitsys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * グルメdeバイト用mst_gyotaiエンティティです。<br />
 * 業態のデータ用
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name = "mst_gyotai")
public class MstGyotai implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -590939329944383831L;

	@Id
	@Column(name="id")
	public Integer id;

	@Column(name="name")
	public String name;

}

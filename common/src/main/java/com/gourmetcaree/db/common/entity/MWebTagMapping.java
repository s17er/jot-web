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
 * WEBデータ用のタグ紐づけのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_web_tag_mapping")
@Getter
public class MWebTagMapping extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_web_tag_mapping_id_gen")
	@SequenceGenerator(name="m_web_tag_mapping_id_gen", sequenceName="m_web_tag_mapping_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータタグID */
	@Column(name = "web_tag_id")
	public Integer webTagId;

	/** WEBデータID */
	@Column(name = "web_id")
	public Integer webId;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_web_tag_mapping";

	/** ID */
	public static final String ID ="id";

	/** WEBデータタグID */
	public static final String WEB_TAG_ID = "web_tag_id";

	/** WEBID */
	public static final String WEB_ID = "web_id";

}
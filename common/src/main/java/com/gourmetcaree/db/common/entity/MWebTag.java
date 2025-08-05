package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;

/**
 * WEBデータ用のタグのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_web_tag")
@Getter
public class MWebTag extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_web_tag_id_gen")
	@SequenceGenerator(name="m_web_tag_id_gen", sequenceName="m_web_tag_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータタグ名 */
	@Column(name = "web_tag_name")
	public String webTagName;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_web_tag";

	/** ID */
	public static final String ID ="id";

	/** WEBデータタグ名 */
	public static final String WEB_TAG_NAME = "web_tag_name";

}
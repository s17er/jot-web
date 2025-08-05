package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEBデータ職種のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name=TWebSearch.TABLE_NAME)
public class TWebSearch extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_search_id_gen")
	@SequenceGenerator(name="t_web_search_id_gen", sequenceName="t_web_search_id_seq", allocationSize=1)
	@Column(name=ID)
	public Integer id;

	/** WEBデータID */
	@Column(name=WEB_ID)
	public Integer webId;

	/** JSON */
	@Column(name=JSON)
	public String json;

	/** WEBデータエンティティ */
	@OneToOne
	@JoinColumn(name="web_id")
	public TWeb tWeb;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_search";

	/** ID */
	public static final String ID ="id";
	/** WEBデータID */
	public static final String WEB_ID = "web_id";
	/** JSON */
	public static final String JSON = "json";

	/** WEBデータエンティティ */
	public static final String T_WEB ="t_web";
}
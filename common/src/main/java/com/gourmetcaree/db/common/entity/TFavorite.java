package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 検討中のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_favorite")
public class TFavorite extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_favorite_id_gen")
	@SequenceGenerator(name="t_favorite_id_gen", sequenceName="t_favorite_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** 追加日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_datetime")
	public Date addDatetime;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_favorite";

	/** ID */
	public static final String ID ="id";

	/** 会員ID */
	public static final String MEMBER_ID ="member_id";

	/** WEBデータID */
	public static final String WEB_ID ="web_id";

	/** 追加日時 */
	public static final String ADD_DATETIME ="add_datetime";
}
package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEBデータ特集のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_web_special")
public class TWebSpecial extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_special_id_gen")
	@SequenceGenerator(name="t_web_special_id_gen", sequenceName="t_web_special_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** 特集ID */
	@Column(name="special_id")
	public Integer specialId;

	/** 対象外フラグ */
	@Column(name="not_target_flg")
	public Integer notTargetFlg;

	/** WEBデータ */
	@ManyToOne
	@JoinColumn(name="web_id")
	public TWeb tWeb;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_special";

	/** ID */
	public static final String ID ="id";

	/** WEBデータID */
	public static final String WEB_ID ="web_id";

	/** 特集ID */
	public static final String SPECIAL_ID ="special_id";

	/** 対象外フラグ */
	public static final String NOT_TARGET_FLG = "not_target_flg";

	/** WEBデータ */
	public static final String T_WEB ="t_web";
}
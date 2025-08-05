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
 * プレ応募データ属性のエンティティクラスです。
 */
@Entity
@Table(name="t_pre_application_attribute")
public class TPreApplicationAttribute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_pre_application_attribute_id_gen")
	@SequenceGenerator(name="t_pre_application_attribute_id_gen", sequenceName="t_pre_application_attribute_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータID */
	@Column(name="application_id")
	public Integer applicationId;

	/** 属性コード */
	@Column(name="attribute_cd")
	public String attributeCd;

	/** 属性値 */
	@Column(name="attribute_value")
	public Integer attributeValue;

	/** 応募データ */
	@ManyToOne
	@JoinColumn(name="application_id")
	public TPreApplication tPreApplication;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_pre_application_attribute";

	/** ID */
	public static final String ID ="id";

	/** WEBデータID */
	public static final String WEB_ID ="application_id";

	/** 属性コード */
	public static final String ATTRIBUTE_CD ="attribute_cd";

	/** 属性値 */
	public static final String ATTRIBUTE_VALUE ="attribute_value";

	/** 対象外フラグ */
	public static final String NOT_TARGET_FLG = "not_target_flg";

	/** WEBデータ */
	public static final String T_WEB ="t_web";
}
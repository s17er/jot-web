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
 * WEBデータ職種属性のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_web_job_attribute")
public class TWebJobAttribute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_job_attribute_id_gen")
	@SequenceGenerator(name="t_web_job_attribute_id_gen", sequenceName="t_web_job_attribute_id_seq", allocationSize=1)
	@Column(name=ID)
	public Integer id;

	/** WEBデータ職種ID */
	@Column(name=WEB_JOB_ID)
	public Integer webJobId;

	/** 属性コード */
	@Column(name=ATTRIBUTE_CD)
	public String attributeCd;

	/** 属性値 */
	@Column(name=ATTRIBUTE_VALUE)
	public Integer attributeValue;

	/** WEBデータ職種エンティティ */
	@ManyToOne
	@JoinColumn(name=WEB_JOB_ID)
	public TWebJob tWebJob;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_job_attribute";

	/** ID */
	public static final String ID ="id";

	/** WEBデータ職種ID */
	public static final String WEB_JOB_ID ="web_job_id";

	/** 属性コード */
	public static final String ATTRIBUTE_CD ="attribute_cd";

	/** 属性値 */
	public static final String ATTRIBUTE_VALUE ="attribute_value";

	/** WEBデータ職種エンティティ */
	public static final String T_WEB_JOB ="t_web_job";
}
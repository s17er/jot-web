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
 * 定型文マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_sentence")
public class MSentence extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_sentence_id_gen")
	@SequenceGenerator(name="m_sentence_id_gen", sequenceName="m_sentence_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 登録日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registration_datetime")
	public Date registrationDatetime;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 定型文タイトル */
	@Column(name="sentence_title")
	public String sentenceTitle;

	/** 本文 */
	@Column(name="body")
	public String body;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_sentence";

	/** ID */
	public static final String ID ="id";

	/** 登録日時 */
	public static final String REGISTRATION_DATETIME = "registration_datetime";

	/** 顧客ID */
	public static final String CUSTOMER_ID ="customer_id";

	/** 定型文タイトル */
	public static final String SENTENCE_TITLE ="sentence_title";

	/** 本文 */
	public static final String BODY ="body";
}
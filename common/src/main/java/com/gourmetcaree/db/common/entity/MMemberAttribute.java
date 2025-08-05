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
 * 会員属性マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_member_attribute")
public class MMemberAttribute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_member_attribute_id_gen")
	@SequenceGenerator(name="m_member_attribute_id_gen", sequenceName="m_member_attribute_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** 属性コード */
	@Column(name="attribute_cd")
	public String attributeCd;

	/** 属性値 */
	@Column(name="attribute_value")
	public Integer attributeValue;

	/** 会員エンティティ */
	@ManyToOne
	@JoinColumn(name="member_id")
	public MMember mMember;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_member_attribute";

	/** ID */
	public static final String ID ="id";

	/** WEBデータID */
	public static final String MEMBER_ID ="member_id";

	/** 属性コード */
	public static final String ATTRIBUTE_CD ="attribute_cd";

	/** 属性値 */
	public static final String ATTRIBUTE_VALUE ="attribute_value";

	/** 会員エンティティ */
	public static final String M_MEMBER ="m_member";
}
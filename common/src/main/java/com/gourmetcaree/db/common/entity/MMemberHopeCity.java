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
 * 会員希望勤務地マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_member_hope_city")
public class MMemberHopeCity extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3156101242628044874L;

	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_member_hope_city_id_gen")
	@SequenceGenerator(name = "m_member_hope_city_id_gen", sequenceName = "m_member_hope_city_id_seq", allocationSize = 1)

	@Column(name = ID)
	public Integer id;

	/** 会員ID */
	@Column(name = MEMBER_ID)
	public Integer memberId;

	/** 市区町村コード */
	@Column(name = CITY_CD)
	public String cityCd;

    /** 会員エンティティ */
	@ManyToOne
    @JoinColumn(name="member_id")
    public MMember mMember;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_location";

	/** ID */
	public static final String ID = "id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** 市区町村コード */
	public static final String CITY_CD = "city_cd";
}
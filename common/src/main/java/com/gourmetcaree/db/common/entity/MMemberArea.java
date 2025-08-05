package com.gourmetcaree.db.common.entity;

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
 * 会員エリアマスタ
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name = "m_member_area")
public class MMemberArea extends AbstractCommonEntity {


	/**
	 *
	 */
	private static final long serialVersionUID = 2902602202087827896L;
	/** ID */
	@Id
	@Column(name = ID)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_member_area_id_gen")
	@SequenceGenerator(name="m_member_area_id_gen", sequenceName="m_member_area_id_seq", allocationSize=1)
	public Integer id;

	/** 会員ID */
	@Column(name = MEMBER_ID)
	public Integer memberId;

	/** エリアコード */
	@Column(name = AREA_CD)
	public Integer areaCd;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_member_area";

	/** ID */
	public static final String ID = "id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

    /** 会員エンティティ */
	@ManyToOne
    @JoinColumn(name="member_id")
    public MMember mMember;

}

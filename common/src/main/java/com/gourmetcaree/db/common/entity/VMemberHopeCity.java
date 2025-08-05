package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 会員の希望勤務地view用エンティティ
 * @author whizz
 * @version 1.0
 *
 */
@Entity
@Table(name="v_member_hope_city")
public class VMemberHopeCity extends AbstractBaseEntity implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3802314142061137749L;

	/** ID */
    @Id
	@Column(name = ID)
	public Integer id;

	/** 会員ID */
	@Column(name = MEMBER_ID)
	public Integer memberId;

	/** 都道府県コード */
	@Column(name = PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 市区町村コード */
	@Column(name = CITY_CD)
	public String cityCd;

	/** 市区町村名 */
	@Column(name = CITY_NAME)
	public String cityName;

    /** 会員エンティティ */
	@ManyToOne
    @JoinColumn(name="member_id")
    public MMember mMember;

	public static final String VIEW_NAME = "v_member_hope_city";

	/** ID */
	public static final String ID = "id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 市区町村コード */
	public static final String CITY_CD = "city_cd";

	/** 市区町村名 */
	public static final String CITY_NAME = "city_name";

}

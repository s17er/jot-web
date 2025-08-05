package com.gourmetcaree.db.common.entity.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.accessor.TempMemberSubDataAccessor;

/**
 * 仮会員エリアテーブル
 * @author nakamori
 *
 */
@Entity
@Table(name = TTempMemberArea.TABLE_NAME)
public class TTempMemberArea extends AbstractCommonEntity implements TempMemberSubDataAccessor {

	/**
	 *
	 */
	private static final long serialVersionUID = -4952636723354226862L;

	/** ID */
	@Id
	@Column(name = ID)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temp_member_area_id_gen")
	@SequenceGenerator(name="t_temp_member_area_id_gen", sequenceName="t_temp_member_area_id_seq", allocationSize=1)
	public Integer id;

	/** 仮会員ID */
	@Column(name = TEMP_MEMBER_ID)
	public Integer tempMemberId;

	/** エリアコード */
	@Column(name = AREA_CD)
	public Integer areaCd;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_temp_member_area";

	/** ID */
	public static final String ID = "id";

	/** 仮会員ID */
	public static final String TEMP_MEMBER_ID = "temp_member_id";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Integer getTempMemberId() {
		return tempMemberId;
	}

	@Override
	public void setTempMemberId(Integer tempMemberId) {
		this.tempMemberId = tempMemberId;
	}
}

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

@Entity
@Table(name = TTempMemberAttribute.TABLE_NAME)
public class TTempMemberAttribute extends AbstractCommonEntity implements TempMemberSubDataAccessor {

	/**
	 *
	 */
	private static final long serialVersionUID = -6376752307258618359L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temp_member_attribute_id_gen")
	@SequenceGenerator(name="t_temp_member_attribute_id_gen", sequenceName="t_temp_member_attribute_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 仮会員ID */
	@Column(name = TEMP_MEMBER_ID)
	public Integer tempMemberId;

	/** 属性コード */
	@Column(name = ATTRIBUTE_CD)
	public String attributeCd;

	/** 属性値 */
	@Column(name = ATTRIBUTE_VALUE)
	public Integer attributeValue;



	/** テーブル名 */
	public static final String TABLE_NAME = "t_temp_member_attribute";

	/** ID */
	public static final String ID ="id";

	/** 仮会員ID */
	public static final String TEMP_MEMBER_ID = "temp_member_id";

	/** 属性コード */
	public static final String ATTRIBUTE_CD ="attribute_cd";

	/** 属性値 */
	public static final String ATTRIBUTE_VALUE ="attribute_value";

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

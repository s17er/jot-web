package com.gourmetcaree.db.common.entity.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gourmetcaree.db.common.entity.accessor.TempMemberSubDataAccessor;

/**
 * WEB履歴書学歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name = TTempMemberSchoolHistory.TABLE_NAME)
public class TTempMemberSchoolHistory extends BaseSchoolHistoryEntity implements TempMemberSubDataAccessor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temp_member_school_history_id_gen")
	@SequenceGenerator(name="t_temp_member_school_history_id_gen", sequenceName="t_temp_member_school_history_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 仮会員ID */
	@Column(name = TEMP_MEMBER_ID)
	public Integer tempMemberId;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_temp_member_school_history";

	/** ID */
	public static final String ID ="id";

	/** 仮会員ID */
	public static final String TEMP_MEMBER_ID = "temp_member_id";

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
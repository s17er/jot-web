package com.gourmetcaree.db.common.entity.member;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gourmetcaree.db.common.entity.accessor.TempMemberSubDataAccessor;


/**
 * WEB履歴書職歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name = TTempMemberCareerHistory.TABLE_NAME)
public class TTempMemberCareerHistory extends BaseCareerHistoryEntity implements TempMemberSubDataAccessor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temp_member_career_history_id_gen")
	@SequenceGenerator(name="t_temp_member_career_history_id_gen", sequenceName="t_temp_member_career_history_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 仮会員ID */
	@Column(name = TEMP_MEMBER_ID)
	public Integer tempMemberId;

	@Transient
	public List<TTempMemberCareerHistoryAttribute> tCareerHistoryAttributeList;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_temp_member_career_history";

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
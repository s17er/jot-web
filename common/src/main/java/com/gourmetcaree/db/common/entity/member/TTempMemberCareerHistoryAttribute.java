package com.gourmetcaree.db.common.entity.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEB履歴書職歴属性のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name = TTempMemberCareerHistoryAttribute.TABLE_NAME)
public class TTempMemberCareerHistoryAttribute extends BaseCareerHistoryAttributeEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temp_member_career_history_attribute_id_gen")
	@SequenceGenerator(name="t_temp_member_career_history_attribute_id_gen", sequenceName="t_temp_member_career_history_attribute_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 仮WEB履歴書職歴ID */
	@Column(name = TEMP_MEMBER_CAREER_HISTORY_ID)
	public Integer tempMemberCareerHistoryId;




	/** テーブル名 */
	public static final String TABLE_NAME = "t_temp_member_career_history_attribute";

	/** ID */
	public static final String ID = "id";

	/** 仮WEB履歴書職歴ID */
	public static final String TEMP_MEMBER_CAREER_HISTORY_ID = "temp_member_career_history_id";


	/** 職歴属性エンティティリスト */
	public static final String T_CAREER_HISTORY = "t_career_history";
}
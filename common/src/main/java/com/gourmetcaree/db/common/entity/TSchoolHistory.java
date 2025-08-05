package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gourmetcaree.db.common.entity.member.BaseSchoolHistoryEntity;

/**
 * WEB履歴書学歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_school_history")
public class TSchoolHistory extends BaseSchoolHistoryEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_school_history_id_gen")
	@SequenceGenerator(name="t_school_history_id_gen", sequenceName="t_school_history_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;



	/** テーブル名 */
	public static final String TABLE_NAME = "t_school_history";

	/** ID */
	public static final String ID ="id";

	/** 会員ID */
	public static final String MEMBER_ID ="member_id";
}
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
 * ジャスキル会員マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_juskill_member_career_history")
public class MJuskillMemberCareerHistory extends AbstractCommonEntity implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 513836841156014670L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_juskill_member_career_history_id_gen")
	@SequenceGenerator(name="m_juskill_member_career_history_id_gen", sequenceName="m_juskill_member_career_history_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** ジャスキル会員ID */
	@Column(name = JUSKILL_MEMBER_ID)
	public Integer juskillMemberId;

	/** 職歴 */
	@Column(name = CAREER_HISTORY)
	public String careerHistory;

	/** ジャスキル会員エンティティ */
	@ManyToOne
	@JoinColumn(name="juskill_member_id")
	public VJuskillMemberList vJuskillMember;

	@ManyToOne
	@JoinColumn(name="juskill_member_id")
	public MJuskillMember mJuskillMember;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_juskill_member_career_history";

	/** ID */
	public static final String ID = "id";

	/** 会員ID */
	public static final String JUSKILL_MEMBER_ID = "juskill_member_id";

	/** 職歴 */
	public static final String CAREER_HISTORY = "career_history";

}
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

import com.gourmetcaree.db.common.entity.member.BaseCareerHistoryAttributeEntity;

/**
 * WEB履歴書職歴属性のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_career_history_attribute")
public class TCareerHistoryAttribute extends BaseCareerHistoryAttributeEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_career_history_attribute_id_gen")
	@SequenceGenerator(name="t_career_history_attribute_id_gen", sequenceName="t_career_history_attribute_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEB履歴書職歴ID */
	@Column(name="career_history_id")
	public Integer careerHistoryId;


	/** WEB履歴書職歴 */
	@ManyToOne
	@JoinColumn(name="career_history_id")
	public TCareerHistory tCareerHistory;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_career_history_attribute";

	/** ID */
	public static final String ID ="id";

	/** WEB履歴書職歴ID */
	public static final String CAREER_HISTORY_ID ="career_history_id";

	/** 区分コード */
	public static final String attribute_cd ="attribute_cd";

	/** 区分値 */
	public static final String attribute_value ="attribute_value";

	/** 職歴属性エンティティリスト */
	public static final String T_CAREER_HISTORY = "t_career_history";
}
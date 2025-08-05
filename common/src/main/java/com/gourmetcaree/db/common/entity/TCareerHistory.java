package com.gourmetcaree.db.common.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gourmetcaree.db.common.entity.member.BaseCareerHistoryEntity;

/**
 * WEB履歴書職歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_career_history")
public class TCareerHistory extends BaseCareerHistoryEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_career_history_id_gen")
	@SequenceGenerator(name="t_career_history_id_gen", sequenceName="t_career_history_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;


	/** 職歴属性エンティティリスト */
	@OneToMany(mappedBy = "tCareerHistory")
	public List<TCareerHistoryAttribute> tCareerHistoryAttributeList;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_career_history";

	/** ID */
	public static final String ID ="id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";


	/** 職歴属性エンティティリスト */
	public static final String T_CAREER_HISTORY_ATTRIBUTE_LIST = "t_career_history_attribute_list";
}
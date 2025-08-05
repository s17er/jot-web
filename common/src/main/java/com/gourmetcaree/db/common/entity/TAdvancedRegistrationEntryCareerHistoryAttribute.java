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
 * WEB履歴書職歴属性のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_advanced_registration_entry_career_history_attribute")
public class TAdvancedRegistrationEntryCareerHistoryAttribute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2209169374635618245L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_advanced_registration_entry_career_history_attribute_id_gen")
	@SequenceGenerator(name="t_advanced_registration_entry_career_history_attribute_id_gen", sequenceName="t_advanced_registration_entry_career_history_attribute_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEB履歴書職歴ID */
	@Column(name=ADVANCED_REGISTRATION_ENTRY_CAREER_HISTORY_ID)
	public Integer advancedRegistrationEntryCareerHistoryId;

	/** 区分コード */
	@Column(name=ATTRIBUTE_CD)
	public String attributeCd;

	/** 区分値 */
	@Column(name=ATTRIBUTE_VALUE)
	public Integer attributeValue;

	/** 職歴属性エンティティリストエンティティ */
	@ManyToOne
	@JoinColumn(name=ADVANCED_REGISTRATION_ENTRY_CAREER_HISTORY_ID)
	public TAdvancedRegistrationEntryCareerHistory tAdvancedRegistrationEntryCareerHistory;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_advanced_registration_entry_career_history_attribute";

	/** ID */
	public static final String ID ="id";

	/** WEB履歴書職歴ID */
	public static final String ADVANCED_REGISTRATION_ENTRY_CAREER_HISTORY_ID ="advanced_registration_entry_career_history_id";

	/** 区分コード */
	public static final String ATTRIBUTE_CD ="attribute_cd";

	/** 区分値 */
	public static final String ATTRIBUTE_VALUE ="attribute_value";


	public TAdvancedRegistrationEntryCareerHistoryAttribute() {
	}

	public TAdvancedRegistrationEntryCareerHistoryAttribute(Integer advancedRegistrationEntryCareerHistoryId, Integer attributeValue, String attributeCd) {
		this.advancedRegistrationEntryCareerHistoryId = advancedRegistrationEntryCareerHistoryId;
		this.attributeCd = attributeCd;
		this.attributeValue = attributeValue;
	}
}
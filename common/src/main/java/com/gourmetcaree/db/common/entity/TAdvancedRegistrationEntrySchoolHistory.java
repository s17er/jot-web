package com.gourmetcaree.db.common.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEB履歴書学歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_advanced_registration_entry_school_history")
public class TAdvancedRegistrationEntrySchoolHistory extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1079339223232285610L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_advanced_registration_entry_school_history_id_gen")
	@SequenceGenerator(name="t_advanced_registration_entry_school_history_id_gen", sequenceName="t_advanced_registration_entry_school_history_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 事前登録エントリID */
	@Column(name = ADVANCED_REGISTRATION_ENTRY_ID)
	public Integer advancedRegistrationEntryId;

	/** 学校名 */
	@Column(name="school_name")
	public String schoolName;

	/** 学部・学科 */
	@Column(name="department")
	public String department;

	/** 状況区分 */
	@Column(name="graduation_kbn")
	public Integer graduationKbn;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_advanced_registration_entry_school_history";

	/** ID */
	public static final String ID ="id";

	/** 事前登録エントリID */
	public static final String ADVANCED_REGISTRATION_ENTRY_ID = "advanced_registration_entry_id";

	/** 学校名 */
	public static final String SCHOOL_NAME ="school_name";

	/** 学部・学科 */
	public static final String DEPARTMENT ="department";

	/** 状況区分 */
	public static final String GRADUATION_KBN ="graduation_kbn";


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
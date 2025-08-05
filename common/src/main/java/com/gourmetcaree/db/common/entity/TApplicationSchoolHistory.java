package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 応募学歴のエンティティクラスです。
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="t_application_school_history")
public class TApplicationSchoolHistory extends AbstractCommonEntity implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7532247406465402112L;

	/** ID */
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_application_school_history_id_gen")
	@SequenceGenerator(name="t_application_school_history_id_gen", sequenceName="t_application_school_history_id_seq", allocationSize=1)
	@Column(name="id")
	@Id
	public Integer id;

	/** 応募ID */
	@Column(name="application_id")
	public Integer applicationId;

	/** 学校名 */
	@Column(name="school_name")
	public String schoolName;

	/** 学部・学科 */
	@Column(name="department")
	public String department;

	/** 状況区分 */
	@Column(name="graduation_kbn")
	public Integer graduationKbn;

	/** 応募テーブルのリスト */
    @OneToOne(mappedBy="tApplicationSchoolHistory")
    public TApplication tApplication;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_application_school_history";

	/** 応募ID */
	public static final String APPLICATION_ID = "application_id";

	/** 学校名 */
	public static final String SCHOOL_NAME = "school_name";

	/** 学部・学科 */
	public static final String DEPARTMENT = "department";

	/** 状況区分 */
	public static final String GRADUATION_KBN = "graduation_kbn";

	/** 応募テーブルのリスト */
	public static final String T_APPLICATION = "t_application";
}

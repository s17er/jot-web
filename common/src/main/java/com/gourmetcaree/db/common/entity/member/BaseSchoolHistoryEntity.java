package com.gourmetcaree.db.common.entity.member;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;

/**
 * 学歴エンティティの基底クラス
 * @author nakamori
 *
 */
@MappedSuperclass
public class BaseSchoolHistoryEntity extends AbstractCommonEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -4687765799298950705L;

	/** 学校名 */
	@Column(name = SCHOOL_NAME)
	public String schoolName;

	/** 学部・学科 */
	@Column(name = DEPARTMENT)
	public String department;

	/** 状況区分 */
	@Column(name = GRADUATION_KBN)
	public Integer graduationKbn;


	/** 学校名 */
	public static final String SCHOOL_NAME ="school_name";

	/** 学部・学科 */
	public static final String DEPARTMENT ="department";

	/** 状況区分 */
	public static final String GRADUATION_KBN ="graduation_kbn";
}

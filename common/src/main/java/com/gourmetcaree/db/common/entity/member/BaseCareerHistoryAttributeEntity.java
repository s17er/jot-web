package com.gourmetcaree.db.common.entity.member;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;

/**
 * WEB履歴書職歴属性の基底クラス
 * @author nakamori
 *
 */
@MappedSuperclass
public abstract class BaseCareerHistoryAttributeEntity extends AbstractCommonEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 4676959318730692275L;

	/** 区分コード */
	@Column(name = ATTRIBUTE_CD)
	public String attributeCd;

	/** 区分値 */
	@Column(name = ATTRIBUTE_VALUE)
	public Integer attributeValue;


	/** 区分コード */
	public static final String ATTRIBUTE_CD = "attribute_cd";

	/** 区分値 */
	public static final String ATTRIBUTE_VALUE = "attribute_value";
}

package com.gourmetcaree.db.common.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

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
 * 事前登録エントリ属性マスタ
 * @author Makoto Otani
 *
 */
@Entity
@Table(name = "t_advanced_registration_entry_attribute")
public class TAdvancedRegistrationEntryAttribute extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4593983596633841349L;

	public TAdvancedRegistrationEntryAttribute() {

	}

	public TAdvancedRegistrationEntryAttribute(Integer advancedRegistrationEntryId, Integer attributeValue, String attributeCd) {
		this.advancedRegistrationEntryId = advancedRegistrationEntryId;
		this.attributeCd = attributeCd;
		this.attributeValue = attributeValue;
	}

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_advanced_registration_entry_attribute_id_gen")
	@SequenceGenerator(name="t_advanced_registration_entry_attribute_id_gen", sequenceName="t_advanced_registration_entry_attribute_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 事前登録エントリID */
	@Column(name = ADVANCED_REGISTRATION_ENTRY_ID)
	public Integer advancedRegistrationEntryId;

	/** 属性コード */
	@Column(name = ATTRIBUTE_CD)
	public String attributeCd;

	/** 属性値 */
	@Column(name = ATTRIBUTE_VALUE)
	public Integer attributeValue;


	/** 事前登録エントリエンティティ */
	@ManyToOne
	@JoinColumn(name=ADVANCED_REGISTRATION_ENTRY_ID)
	public TAdvancedRegistrationEntry tAdvancedRegistrationEntry;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_advanced_registration_entry_attribute";

	/** ID */
	public static final String ID = "id";

	/** 事前登録エントリID */
	public static final String ADVANCED_REGISTRATION_ENTRY_ID = "advanced_registration_entry_id";

	/** 属性コード */
	public static final String ATTRIBUTE_CD ="attribute_cd";

	/** 属性値 */
	public static final String ATTRIBUTE_VALUE ="attribute_value";

	/** 事前登録エントリエンティティ */
	public static final String T_ADVANCED_REGISTRATION_ENTRY = "t_advanced_registration_entry";


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

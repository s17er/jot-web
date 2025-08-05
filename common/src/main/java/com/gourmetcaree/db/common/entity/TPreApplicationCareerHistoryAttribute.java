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
 * プレ応募職歴属性テーブルのエンティティクラスです。
 *
 */
@Entity
@Table(name="t_pre_application_career_history_attribute")
public class TPreApplicationCareerHistoryAttribute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 45516114094475743L;

	/** ID */
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_pre_application_career_history_attribute_id_gen")
	@SequenceGenerator(name="t_pre_application_career_history_attribute_id_gen", sequenceName="t_pre_application_career_history_attribute_id_seq", allocationSize=1)
	@Column(name="id")
	@Id
	public Integer id;

	/** 応募職歴ID */
	@Column(name="application_career_history_id")
	public Integer applicationCareerHistoryId;

	/** 区分コード */
	@Column(name="attribute_cd")
	public String attributeCd;

	/** 区分値 */
	@Column(name="attribute_value")
	public String attributeValue;

	/** 応募職歴データ */
	@ManyToOne
	@JoinColumn(name="application_career_history_id")
	public TPreApplicationCareerHistory tPreApplicationCareerHistory;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_pre_application_career_history_attribute";

	/** 応募職歴ID */
	public static final String APPLICATION_CAREER_HISTORY_ID = "application_career_history_id";

	/** 区分コード */
	public static final String ATTRIBUTE_CD = "attribute_cd";

	/** 区分値 */
	public static final String ATTRIBUTE_VALUE = "attributeValue";
}

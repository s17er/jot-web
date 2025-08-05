package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ジャスキル会員の素材のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_juskill_preview_access")
public class TJuskillPreviewAccess extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_juskill_preview_access_id_gen")
	@SequenceGenerator(name="t_juskill_preview_access_id_gen", sequenceName="t_juskill_preview_access_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** ジャスキル会員ID */
	@Column(name="juskill_member_id")
	public Integer juskillMemberId;

	/** アクセスコード */
	@Column(name="access_cd")
	public String accessCd;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_juskill_preview_access";

	/** ID */
	public static final String ID ="id";

	/** ジャスキル会員ID */
	public static final String JUSKILL_MEMBER_ID ="juskill_member_id";

	/** アクセスコード */
	public static final String ACCESS_CD ="access_cd";

}
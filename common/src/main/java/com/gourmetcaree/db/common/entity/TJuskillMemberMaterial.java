package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ジャスキル会員の素材のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_juskill_member_material")
public class TJuskillMemberMaterial extends AbstractCommonEntity implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_juskill_member_material_id_gen")
	@SequenceGenerator(name="t_juskill_member_material_id_gen", sequenceName="t_juskill_member_material_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** ジャスキル会員ID */
	@Column(name="juskill_member_id")
	public Integer juskillMemberId;

	/** 素材区分 */
	@Column(name="material_kbn")
	public Integer materialKbn;

	/** ファイル名 */
	@Column(name="file_name")
	public String fileName;

	/** コンテントタイプ */
	@Column(name="content_type")
	public String contentType;

	/** 素材データ */
	@Column(name="material_data")
	public String materialData;

	/** 対象外フラグ */
	@Column(name="not_target_flg")
	public Integer notTargetFlg;

	/** WEBデータ */
	@OneToOne
	@JoinColumn(name="juskill_member_id")
	public MJuskillMember mJuskillMember;

	@ManyToOne
	@JoinColumn(name="juskill_member_id")
	public VJuskillMemberList vJuskillMember;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_juskill_member_material";

	/** ID */
	public static final String ID ="id";

	/** ジャスキル会員ID */
	public static final String JUSKILL_MEMBER_ID ="juskill_member_id";

	/** 素材区分 */
	public static final String MATERIAL_KBN ="material_kbn";

	/** ファイル名 */
	public static final String FILE_NAME ="file_name";

	/** コンテントタイプ */
	public static final String CONTENT_TYPE ="content_type";

	/** 素材データ */
	public static final String MATERIAL_DATA ="material_data";

	/** 対象外フラグ */
	public static final String NOT_TARGET_FLG = "not_target_flg";

	/** ジャスキル会員 */
	public static final String M_JUSKILL_MEMBER ="m_juskill_member";
}
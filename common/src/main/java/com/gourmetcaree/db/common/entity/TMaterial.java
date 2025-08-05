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
 * 素材のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_material")
public class TMaterial extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_material_id_gen")
	@SequenceGenerator(name="t_material_id_gen", sequenceName="t_material_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

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
	@ManyToOne
	@JoinColumn(name="web_id")
	public TWeb tWeb;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_material";

	/** ID */
	public static final String ID ="id";

	/** WEBデータID */
	public static final String WEB_ID ="web_id";

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

	/** WEBデータ */
	public static final String T_WEB ="t_web";
}
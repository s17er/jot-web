package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 素材データからデータ内容を除いたデータのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="v_material_no_data")
public class VMaterialNoData implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 985577238720957746L;

	/** ID */
	@Id
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

	/** 登録日時 */
	@Column(name="insert_datetime")
	public Timestamp insertDatetime;

	/** テーブル名 */
	public static final String TABLE_NAME = "v_material_no_data";

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

	/** WEBデータ */
	public static final String T_WEB ="t_web";
}
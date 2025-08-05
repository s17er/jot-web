package com.gourmetcaree.admin.service.property.tempWebdata;

import java.io.Serializable;

/**
 * 一時Webデータに保存する素材のプロパティ
 * PHP側に対応するため意図的にスネークケースで定義している。
 *
 * @author hara
 *
 */
public class TempMaterialProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 568098934583047965L;

	/** ID */
	public String id;

	/** WEBデータID */
	public String web_id;

	/** 素材区分 */
	public String material_kbn;

	/** ファイル名 */
	public String file_name;

	/** コンテントタイプ */
	public String content_type;

}

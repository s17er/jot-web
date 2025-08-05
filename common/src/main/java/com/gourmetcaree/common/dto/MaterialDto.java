package com.gourmetcaree.common.dto;

import java.io.Serializable;


/**
 * 素材DTOクラス
 * @author Makoto Otani
 * @version 1.0
 */
public class MaterialDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 568098934583047965L;

	/** ID */
	public String id;

	/** WEBデータID */
	public String webId;

	/** 素材区分 */
	public String materialKbn;

	/** ファイル名 */
	public String fileName;

	/** コンテントタイプ */
	public String contentType;

	/** 素材データ */
	public byte[] materialData;
}
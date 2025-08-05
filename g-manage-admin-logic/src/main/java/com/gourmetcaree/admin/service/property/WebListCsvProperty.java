package com.gourmetcaree.admin.service.property;

import java.io.Serializable;

import com.gourmetcaree.db.webdata.dto.webdata.VWebListDto;

/**
 * WEBデータCSVのデータを受け渡しするクラス
 * @author Takehiro Nakamori
 *
 */
public class WebListCsvProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7563758571022928829L;

	/** 出力パス */
	public String pass;

	/** ファイル名 */
	public String fileName;

	/** 出力カウント */
	public int count;

	/** エンコード */
	public String encode;

	/** ソートキー */
	public String sortKey;

	/** WEBリストDTO */
	public VWebListDto vWebListDto;

}

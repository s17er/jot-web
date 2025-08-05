package com.gourmetcaree.shop.logic.property;

import java.io.Serializable;

import com.gourmetcaree.common.property.BaseProperty;

/**
 *
 * 応募CSVのデータを受け渡しするクラス
 * @author Takahiro Kimura
 *
 */
public class ApplicationCsvProperty extends BaseProperty implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8737684352354667172L;

	/** 出力パス */
	public String pass;

	/** ファイル名 */
	public String faileName;

	/** 出力カウント */
	public int count;

	/** エンコード */
	public String encode;

	/** ソートキー */
	public String sortKey;

}

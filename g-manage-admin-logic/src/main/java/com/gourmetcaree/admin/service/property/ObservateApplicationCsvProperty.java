package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * 応募CSVのデータを受け渡しするクラス
 * @author Takahiro Kimura
 *
 */
public class ObservateApplicationCsvProperty extends BaseProperty implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3620294105579919216L;

	/** 検索条件マップ */
	public Map<String, String> map;

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

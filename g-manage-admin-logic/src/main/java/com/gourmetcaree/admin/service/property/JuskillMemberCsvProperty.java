package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * 会員のデータを受け渡しするクラス
 * @author Takahiro Kimura
 *
 */
public class JuskillMemberCsvProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6259917200137740192L;

	/** 検索条件マップ */
	public Map<String, String> map;

	/** 出力パス */
	public String pass;

	/** ファイル名 */
	public String faileName;

	/** 文字コード */
	public String encode;

	/** ヘッダー */
	public String header;

	/** 出力カウント */
	public int count;

	/** ソートキー */
	public String sortKey;

}

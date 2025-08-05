package com.gourmetcaree.admin.service.property;

import org.seasar.extension.jdbc.SqlSelect;

import com.gourmetcaree.admin.service.dto.AdvancedRegistrationSearchResultDto;

public class AdvancedMemberCsvProperty extends BaseProperty{


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4153734703610868883L;

	/** 検索結果select */
	public SqlSelect<AdvancedRegistrationSearchResultDto> select;

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
}

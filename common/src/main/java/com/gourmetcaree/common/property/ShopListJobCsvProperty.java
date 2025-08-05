package com.gourmetcaree.common.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.common.csv.ShopListJobCsv;

/**
 * 店舗一覧のCSV用プロパティ
 * @author Takehiro Nakamori
 *
 */
public class ShopListJobCsvProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9219424291472726745L;

	/** 店舗一覧リスト */
	public List<ShopListJobCsv> shopListJob;

	/** 出力パス */
	public String pass;

	/** ファイル名 */
	public String faileName;

	/** 文字コード */
	public String encode;

	/** ヘッダー */
	public String header;

	/** ヘッダー(職歴) */
	public String careerHeader;

	/** 出力カウント */
	public int count;

}

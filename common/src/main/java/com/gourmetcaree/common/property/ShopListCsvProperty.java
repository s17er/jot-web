package com.gourmetcaree.common.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.common.csv.ShopListArbeitCsv;
import com.gourmetcaree.common.csv.ShopListCsv;

/**
 * 店舗一覧のCSV用プロパティ
 * @author Takehiro Nakamori
 *
 */
public class ShopListCsvProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9219424291472726745L;

	/** 店舗一覧リスト */
	public List<ShopListArbeitCsv> shopListList;

	/** 店舗一覧リスト */
	public List<ShopListCsv> shopList;

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

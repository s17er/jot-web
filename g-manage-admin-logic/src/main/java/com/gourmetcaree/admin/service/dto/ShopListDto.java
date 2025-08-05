package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系列店舗DTO
 * @author whizz
 *
 */
public class ShopListDto implements Serializable {

	private static final long serialVersionUID = -1096463277489957136L;

	/** ID */
	public Integer id;
	/** 店舗名 */
	public String shopName;
	/** 顧客ID */
	public Integer customerId;
	/** 業態1 */
	public Integer industryKbn1;
	/** 業態1ラベル */
	public String industryKbn1Label;
	/** 業態2 */
	public Integer industryKbn2;
	/** 業態2ラベル */
	public String industryKbn2Label;
	/** 電話番号 */
	public String csvPhoneNo;
	/** 都道府県コード */
	public Integer prefecturesCd;
	/** 都道府県名 */
	public String prefecturesName;
	/** 市区町村コード */
	public String cityCd;
	/** 市区町村名 */
	public String cityName;
	/** その他住所 */
	public String address;
	/** ラベルのリスト */
	public List<LabelDto> labelDtoList = new ArrayList<>();

	/**
	 * ラベルのID、名前を保持するクラス
	 * @author whizz
	 *
	 */
	public static class LabelDto implements Serializable {
		private static final long serialVersionUID = 1316709263076014963L;
		public Integer id;
		public String labelName;
	}
}
package com.gourmetcaree.admin.pc.customer.dto.customer;

import java.io.Serializable;
import java.util.List;

/**
 * 顧客情報DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class CustomerInfoDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2054170911618070420L;

	/** ID */
	public int id;

	/** 顧客名 */
	public String customerName;

	/** 担当者名 */
	public String contactName;

	/** エリアコード */
	public String areaCd;

	/** 電話番号 */
	public String phoneNo;

	/** メールアドレス */
	public String mainMail;

	/** サブメールアドレス */
	public List<String> subMailList;

	/** 担当会社・営業担当DTOリスト */
	public List<CompanySalesDto> companySalesDtoList;

	/** 詳細パス */
	public String detailPath;

	/** メルマガ受信フラグ */
	public Integer mailMagazineReceptionFlg;

	/** メルマガ受信エリア */
	public List<Integer> mailMagazineAreaCdList;

	/** 系列店舗の都道府県 */
	public List<Integer> shopListPrefecturesCdList;

	/** 系列店舗の海外エリア */
	public List<Integer> shopListShutokenForeignAreaKbnList;

	/** 系列店舗の業態 */
	public List<Integer> shopListIndustryKbnList;

	/** 系列店舗数 */
	public int shopCount;

}
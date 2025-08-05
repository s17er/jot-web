package com.gourmetcaree.admin.pc.webdata.dto.webdata;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * WEBデータ一覧DTOクラス
 * @author Makoto Otani
 * @version 1.0
 */
public class ListDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4042298128153754906L;

	/** 原稿番号(ID) */
	public String id;

	/** サイズ区分 */
	public String sizeKbn;

	/** 原稿名 */
	public String manuscriptName;

	/** 号数ID */
	public Integer volumeId;

	/** 号数 */
	public Integer volume;

	/** 締切日時 */
	public Date deadlineDatetime;

	/** 確定締切日時 */
	public Date fixedDeadlineDatetime;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 画面表示ステータス */
	public String displayStatus;

	/** チェックステータス */
	public Integer checkedStatus;

	/** 顧客ID */
	public Integer customerId;

	/** 顧客名 */
	public String customerName;

	/** 所属会社ID */
	public Integer companyId;

	/** 営業担当者ID */
	public Integer salesId;

	/** 応募フォームフラグ */
	public Integer applicationFormKbn;

	/** 店舗一覧フラグ */
	public Integer shopListDisplayKbn;

	/** バージョン番号 */
	public Long version;

	/** 詳細のパス */
	public String detailPath;

	/** 連載区分 */
	public String serialPublicationKbn;
	/** コピー時の連載区分 */
	public String copySerialPublicationKbn;
	/** 連載 */
	public String serialPublication;

	/** 全店舗数 */
	public Integer shopListCount;

	/** 公開店舗数 */
	public Integer shopListPublicationCount;

	/** ライト版求人数 */
	public Integer shopListJobOfferCount;

	/** PV/PC */
	public Integer pvPcCount;

	/** PV/スマホ */
	public Integer pvSmartPhoneCount;

	/** PV/携帯 */
	public Integer pvMbCount;

	/** IP電話応募 */
	public Integer ipPhoneHistoryCount;

	/** 応募数 */
	public long applicationCount;

	/** 気になる数 */
	public Integer interestCount;

	public Integer preApplicationCount;

	/** エリアコード */
	public Integer areaCd;

	/** プレビュー用URL */
	public String previewUrl;

	/** 求人識別番号 */
    public String webNo;

	/**
	 * @return PVのPC/スマホの合計値
	 */
	public int getPvSumCount() {
		final int pc = pvPcCount == null ? 0 : pvPcCount;
		final int sm = (pvSmartPhoneCount == null ? 0 : pvSmartPhoneCount) + (pvMbCount == null ? 0 : pvMbCount);
		return pc + sm;
	}
}
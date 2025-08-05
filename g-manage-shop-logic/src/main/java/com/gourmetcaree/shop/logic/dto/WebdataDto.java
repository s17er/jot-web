package com.gourmetcaree.shop.logic.dto;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * WEBデータの情報を保持するクラスです。
 * @author Makoto Otani
 */
public class WebdataDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4367650832794580447L;

	/** 原稿番号 */
	public String id;

	/** エリアコード */
	public Integer areaCd;

	/** 原稿名 */
	public String manuscriptName;

	/** サイズ区分 */
	public String sizeKbn;

	/** 号数ID */
	public String volumeId;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 画面表示ステータス */
	public String displayStatus;

	/** 応募フォームフラグ */
	public Integer applicationFormKbn;

	/** アクセスコード */
	public String accessCd;

	/** 応募件数 */
	public int applicationCount;

	/** 電話応募件数 */
	public int phoneApplicationCount;

	/** 気になる件数 */
	public int interestCount;

	/** プレ応募件数 */
	public int preApplicationCount;

	/** アクセスカウント総数 */
	public int allAccessCount;

	/** renewalフラグ */
	public boolean renewalFlg;

	/** 登録日時 */
	public Date insertDateime;

}

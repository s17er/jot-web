package com.gourmetcaree.admin.pc.report.dto.report;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * レポート一覧DTOクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class ListDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2769692265278014766L;

	/** 号数ID */
	public Integer volumeId;

	/** 号数 */
	public Integer volume;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** ステータス[下書き]の合計 */
	public int draftCount = 0;

	/** ステータス[承認中]の合計 */
	public int reqApprovalCount = 0;

	/** ステータス[掲載待ち]の合計 */
	public int postWaitCount = 0;

	/** ステータス[掲載中]の合計 */
	public int postDuringCount = 0;

	/** ステータス[掲載終了]の合計 */
	public int postEndCount = 0;

	/** 詳細のパス */
	public String detailPath;

	/**
	 * 合計数を取得します。
	 * @return
	 */
	public int getTotalCount() {
		return draftCount + reqApprovalCount + postWaitCount + postDuringCount + postEndCount;
	}
}
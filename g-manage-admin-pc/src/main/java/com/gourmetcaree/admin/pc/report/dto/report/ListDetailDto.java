package com.gourmetcaree.admin.pc.report.dto.report;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * レポート詳細DTOクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class ListDetailDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3489461279039671852L;

	/** 営業担当者ID */
	public int salesId;

	/** 会社ID */
	public int companyId;

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

	/**
	 * 合計数を取得します。
	 * @return
	 */
	public int getTotalCount() {
		return draftCount + reqApprovalCount + postWaitCount + postDuringCount + postEndCount;
	}
}
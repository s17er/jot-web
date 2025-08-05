package com.gourmetcaree.admin.service.dto;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * レポート一覧詳細用DTOクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class ReportListDetailDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8911067881943169272L;

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

}
package com.gourmetcaree.admin.service.dto;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * レポート一覧用SQLのSELECT結果取得用DTOクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class ReportListDetailSqlDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5051251965756674449L;

	/** 営業担当者ID */
	public int salesId;

	/** 会社ID */
	public int companyId;

	/** 画面表示ステータス */
	public int displayStatus = 0;

	/** 画面表示ステータスに一致するWebdata数 */
	public int statusCount = 0;
}
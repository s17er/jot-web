package com.gourmetcaree.admin.service.dto;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * レポート一覧用SQLのSELECT結果取得用DTOクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class ReportListSqlDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5051251965756674449L;

	/** 号数ID */
	public int id;

	/** 号数 */
	public int volume;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 画面表示ステータス */
	public int displayStatus = 0;

	/** 画面表示ステータスに一致するWebdata数 */
	public int statusCount = 0;
}
package com.gourmetcaree.common.dto;

public class TerminalDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -880240486329719144L;

	/** 都道府県コード */
	public String prefecturesCd;

	/** 鉄道会社ID */
	public String companyCd;

	/** 路線ID */
	public String lineCd;

	/** 駅ID */
	public String stationCd;

}

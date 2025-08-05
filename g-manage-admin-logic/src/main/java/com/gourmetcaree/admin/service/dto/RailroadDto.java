package com.gourmetcaree.admin.service.dto;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 路線の情報を保持するDtoクラスです。
 * @author Makoto Otani
 */
public class RailroadDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5327100161020733073L;

	/** 鉄道会社名 */
	public String railroadName;

	/** 路線名 */
	public String routeName;

	/** 駅名 */
	public String stationName;
}

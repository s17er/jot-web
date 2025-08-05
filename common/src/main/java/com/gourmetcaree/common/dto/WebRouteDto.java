package com.gourmetcaree.common.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * WEBデータ路線図DTOクラス
 * @author Makoto Otani
 * @version 1.0
 */
public class WebRouteDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4529371278139371717L;

	/** ID */
	public Integer id;

	/** WEBデータID */
	public String webId;

	/** 鉄道会社ID */
	public Integer railroadId;

	/** 鉄道会社名 */
	public String railroadName;

	/** 路線ID */
	public Integer routeId;

	/** 路線名 */
	public String routeName;

	/** 駅ID */
	public Integer stationId;

	/** 駅名 */
	public String stationName;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
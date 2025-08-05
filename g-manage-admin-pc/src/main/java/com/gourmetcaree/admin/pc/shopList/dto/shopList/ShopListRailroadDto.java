package com.gourmetcaree.admin.pc.shopList.dto.shopList;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.admin.service.dto.RailroadDto;

/** 路線DTOです。 */
public class ShopListRailroadDto extends RailroadDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6518858633260399507L;

	/** 鉄道会社ID */
	public String railroadId;

	/** 路線ID */
	public String routeId;

	/** 駅ID */
	public String stationId;

	/** コメント */
	public String comment;

	/**
	 * 入力がないかどうか
	 * @return 入力がない場合にtrue
	 */
	public boolean isNotExistInput() {
		return getInputCount() == 0;
	}

	/**
	 * 鉄道会社、路線、駅すべてが入力されているかどうか
	 * @return 入力されている場合にtrue
	 */
	public boolean isExistAllInput() {
		return getInputCount() == 3;
	}

	/**
	 * 鉄道会社、路線、駅の入力が未完了かどうか
	 * @return 未完了の場合にtrue
	 */
	public boolean isNotCompleteInput() {
		int inputCount = getInputCount();
		if (inputCount == 0 || inputCount == 3) {
			return false;
		}

		return true;
	}





	/**
	 * 鉄道会社、路線、駅の入力カウントを取得
	 * @return カウント数
	 */
	private int getInputCount() {
		int inputCount = 0;
		if (StringUtils.isNotBlank(railroadId)) {
			inputCount++;
		}
		if (StringUtils.isNotBlank(routeId)) {
			inputCount++;
		}
		if (StringUtils.isNotBlank(stationId)) {
			inputCount++;
		}

		return inputCount;
	}
}

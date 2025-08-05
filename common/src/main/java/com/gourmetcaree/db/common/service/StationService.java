package com.gourmetcaree.db.common.service;

import org.seasar.extension.jdbc.exception.SNoResultException;

import com.gourmetcaree.db.common.entity.MStation;

/**
 * 駅マスタのサービスクラスです。
 * @version 1.0
 */
public class StationService extends AbstractGroumetCareeBasicService<MStation> {

	/**
	 * 主キーを元に表示順を取得します。
	 * 取得失敗時はデフォルトとしてゼロを戻します。
	 * @param id
	 * @return
	 */
	public int getDisplayOrder (int id) {
		try {
			return findById(id).displayOrder;
		} catch (SNoResultException e) {
			return 0;
		}
	}


}

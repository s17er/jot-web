package com.gourmetcaree.db.common.service;

import com.gourmetcaree.db.common.entity.MSpecialDisplay;

/**
 * 特集表示マスタのサービスクラスです。
 * @version 1.0
 */
public class SpecialDisplayService extends AbstractGroumetCareeBasicService<MSpecialDisplay> {


	/**
	 * 特集IDをキーに特集表示マスタを物理削除
	 * @param specialId 特集ID
	 */
	public void deleteMSpecialDisplayBySpecialId(int specialId) {

		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(MSpecialDisplay.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(MSpecialDisplay.SPECIAL_ID).append(" = ? ");

		// 特集IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(specialId)
				.execute();
	}

}
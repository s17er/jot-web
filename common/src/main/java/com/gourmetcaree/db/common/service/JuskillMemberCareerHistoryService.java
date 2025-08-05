package com.gourmetcaree.db.common.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gourmetcaree.db.common.entity.MJuskillMemberCareerHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * ジャスキル会員職歴のサービスクラス
 * @author whizz
 *
 */
public class JuskillMemberCareerHistoryService extends AbstractGroumetCareeBasicService<MJuskillMemberCareerHistory> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(JuskillMemberCareerHistoryService.class);


	/**
	 * 削除登録する
	 * @param juskillMemberId
	 * @param list
	 */
	public void deleteInsert(int juskillMemberId, List<MJuskillMemberCareerHistory> list) {


		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(MJuskillMemberCareerHistory.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(MJuskillMemberCareerHistory.JUSKILL_MEMBER_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(juskillMemberId)
				.execute();

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (MJuskillMemberCareerHistory entity : list) {
			entity.juskillMemberId = juskillMemberId;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		}

		insertBatch(list);
	}
}

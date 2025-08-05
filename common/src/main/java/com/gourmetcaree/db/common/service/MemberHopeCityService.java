package com.gourmetcaree.db.common.service;

import org.seasar.extension.jdbc.SqlBatchUpdate;

import com.gourmetcaree.db.common.entity.MMemberHopeCity;

/**
 * 会員希望勤務地マスタのサービスクラスです。
 * @version 1.0
 */
public class MemberHopeCityService extends AbstractGroumetCareeBasicService<MMemberHopeCity> {


	/**
	 * 会員IDをキーに会員属性データを物理削除
	 * @param memberId 会員ID
	 */
	public void deleteByMemberId(int memberId) {

		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
			    "DELETE FROM m_member_hope_city WHERE member_id = ? "
				, Integer.class);

		    batchUpdate.params(memberId);

		    batchUpdate.execute();
	}
}
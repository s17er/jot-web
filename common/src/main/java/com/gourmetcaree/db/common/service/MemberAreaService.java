package com.gourmetcaree.db.common.service;

import org.seasar.extension.jdbc.SqlBatchUpdate;

import com.gourmetcaree.db.common.entity.MMemberArea;

/**
 * 会員エリアサービス
 * @author Takehiro Nakamori
 *
 */
public class MemberAreaService extends AbstractGroumetCareeBasicService<MMemberArea>{

	/**
	 * 会員IDをキーに会員マスタデータを物理削除
	 * @param memberId 会員ID
	 */
	public void deleteMemberAreaByMemberId(int memberId) {

		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
			    "DELETE FROM m_member_area WHERE member_id = ? "
				, Integer.class);

		    batchUpdate.params(memberId);

		    batchUpdate.execute();
	}

}

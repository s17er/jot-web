package com.gourmetcaree.db.common.service;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TJuskillMemberMaterial;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * ジャスキル会員の素材のサービスクラス
 * @author whizz
 *
 */
public class JuskillMemberMaterialService extends AbstractGroumetCareeBasicService<TJuskillMemberMaterial> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(JuskillMemberMaterialService.class);

	@Override
	protected void setCommonInsertColmun(TJuskillMemberMaterial entity) {
		super.setCommonInsertColmun(entity);
		entity.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;
		entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
	}


	/**
	 * 削除登録する
	 * @param juskillMemberId
	 * @param entity
	 */
	public void deleteInsert(int juskillMemberId, TJuskillMemberMaterial entity) {

		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TJuskillMemberMaterial.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TJuskillMemberMaterial.JUSKILL_MEMBER_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(juskillMemberId)
				.execute();

		if (entity == null) {
			return;
		}

		insertBatch(entity);
	}


	public TJuskillMemberMaterial findJuskillMemberMaterialByJuskillMemberId(String juskillMemberId) {
		return jdbcManager.from(TJuskillMemberMaterial.class)
				.where(new SimpleWhere()
				.eq("juskillMemberId", Integer.parseInt(juskillMemberId))
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.getSingleResult();
	}
}

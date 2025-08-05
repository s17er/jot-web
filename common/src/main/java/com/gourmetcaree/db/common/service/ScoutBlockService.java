package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.entity.TScoutBlock;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * スカウトブロックのサービスクラスです。
 * @version 1.0
 */
public class ScoutBlockService extends AbstractGroumetCareeBasicService<TScoutBlock> {

	/**
	 * スカウトブロックに設定されているかチェック
	 * @param memberId 会員ID
	 * @param customerId 顧客ID
	 * @return スカウトブロックに設定されていない場合、trueを返す
	 */
	public boolean isSetScoutBlock(int memberId, int customerId) {

		Long count = jdbcManager.from(TScoutBlock.class)
						.where(new SimpleWhere()
						.eq("memberId", memberId)
						.eq("customerId", customerId)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getCount();

		return count > 0 ? false : true;
	}


	/**
	 * スカウトブロックに登録します。<br />
	 * すでに登録されている顧客は登録されません。
	 * @param memberId 会員ID
	 * @param customerIdList 登録する顧客ID
	 */
	public void insertScoutBlock(int memberId, List<Integer> customerIdList) {

		try {
			// 顧客IDがすでに登録されていないかチェックする
			SimpleWhere where = new SimpleWhere();
			where.eq(StringUtil.camelize(TScoutBlock.MEMBER_ID), memberId);
			where.in(StringUtil.camelize(TScoutBlock.CUSTOMER_ID), customerIdList.toArray());
			where.eq(StringUtil.camelize(TScoutBlock.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			List<TScoutBlock> entityList = findByCondition(where);
			for (TScoutBlock entity : entityList) {
				// すでに登録されている顧客があれば登録しない
				if (customerIdList.contains((int)entity.customerId)) {
					customerIdList.remove(entity.customerId);
				}
			}
		} catch (WNoResultException e) {
			// 重複する顧客IDがなければそのまま登録
		}

		// 登録するエンティティを生成
		List<TScoutBlock> insertList = new ArrayList<TScoutBlock>();
		for (int customerId : customerIdList) {
			TScoutBlock entity = new TScoutBlock();
			// 会員IDをセット
			entity.memberId = memberId;
			// 顧客IDをセット
			entity.customerId = customerId;
			// 削除フラグをセット
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			insertList.add(entity);
		}

		// 値が登録されていれば処理をする
		if (insertList != null && !insertList.isEmpty()) {
			// データの登録
			insertBatch(insertList);
		}
	}

	/**
	 * 会員IDを条件に、スカウトブロックのデータを物理削除します。
	 * @param memberId 会員ID
	 */
	public void deleteTScoutBlockByMemberId(int memberId) {

		// SQLの作成
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(TScoutBlock.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TScoutBlock.MEMBER_ID).append(" = ? ");

		// 会員IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(memberId)
				.execute();
	}
}
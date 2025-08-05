package com.gourmetcaree.db.common.service;

import java.util.Date;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.entity.TFavorite;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 検討中のサービスクラスです。
 * @version 1.0
 */
public class FavoriteService extends AbstractGroumetCareeBasicService<TFavorite> {

	/**
	 * 指定された検討中IDが会員の操作可能なメールかどうかを取得します。
	 * @param id
	 * @param memberId
	 * @return
	 */
	public boolean canMemberHandleEntity(int id, int memberId) {

		Where sendWhere =  new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TFavorite.ID), id)
							.eq(WztStringUtil.toCamelCase(TFavorite.MEMBER_ID), memberId)
							.eq(WztStringUtil.toCamelCase(TFavorite.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							;

		return (countRecords(sendWhere) > 0);
	}

	/* (非 Javadoc)
	 * @see com.gourmetcaree.db.common.service.AbstractGroumetCareeBasicService#setCommonInsertColmun(java.lang.Object)
	 */
	@Override
	protected void setCommonInsertColmun(TFavorite entity) {
		super.setCommonInsertColmun(entity);
		// 追加日時をセット
		entity.addDatetime = entity.insertDatetime;
	}

	/**
	 * 検討中BOXに追加します。<br />
	 * データがすでに存在すれば、追加日時を更新します。
	 * @param entity 検討中エンティティ
	 */
	public void addFavorite(TFavorite entity) {

		// 引数チェック
		if (entity == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		SimpleWhere where = new SimpleWhere();
		where.eq(StringUtil.camelize(TFavorite.MEMBER_ID), entity.memberId);
		where.eq(StringUtil.camelize(TFavorite.WEB_ID), entity.webId);
		where.eq(StringUtil.camelize(TFavorite.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			try {
				List<TFavorite> entityList = findByCondition(where);

				// 重複するデータが複数存在する場合は、不整合のためdelete insert(通常ありえない)
				if (entityList.size() > 1) {
					deleteBatchIgnoreVersion(entityList);
					insert(entity);

				// データが存在すれば追加日時をupdate
				} else {
					entity = entityList.get(0);
					entity.addDatetime = new Date();
					update(entity);
				}

			// データが存在しなければ新しく登録する
			} catch (WNoResultException e) {
				insert(entity);
			}
	}
}
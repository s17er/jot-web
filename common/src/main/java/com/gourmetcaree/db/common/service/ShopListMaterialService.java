package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 素材のサービスクラスです。
 * @version 1.0
 */
public class ShopListMaterialService extends AbstractGroumetCareeBasicService<TShopListMaterial> {

	/**
	 * shopListIdと素材区分を指定してエンティティを取得します。
	 * @param shopListId
	 * @param materialKbn
	 * @return
	 * @throws WNoResultException
	 */
	public TShopListMaterial getMaterialEntity(int shopListId, int materialKbn)
	throws WNoResultException {

		SimpleWhere where = getWhereByShopListId(shopListId, materialKbn);
		return findByCondition(where).get(0);
	}

	/**
	 * 素材データが存在しているかを取得します。
	 * @param shopListId
	 * @param materialKbn
	 * @return true:存在する、false:存在しない
	 */
	public boolean isMaterialEntityExist(int shopListId, int materialKbn) {

		SimpleWhere where = getWhereByShopListId(shopListId, materialKbn);
		long count = countRecords(where);

		return (count > 0) ? true : false;
	}

	/**
	 * ShopListIdと素材区分を特定した検索条件です。
	 * @param shpoListId
	 * @param materialKbn
	 * @return
	 */
	private SimpleWhere getWhereByShopListId(int shpoListId, int materialKbn) {
		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(TShopListMaterial.SHOP_LIST_ID), shpoListId)
		.eq(toCamelCase(TShopListMaterial.MATERIAL_KBN), materialKbn)
		.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
		;
		return where;
	}

	/**
	 * ShopListIDをキー素材を物理削除
	 * @param shopListId shopListId
	 */
	public void deleteTShopListMaterialByShopListId(int shopListId) {

		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TShopListMaterial.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TShopListMaterial.SHOP_LIST_ID).append(" = ? ");

		// shopListIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(shopListId)
				.execute();
	}

	/**
	 * 画像の移動
	 * @param fromId
	 * @param toId
	 */
	public void moveMaterial(int fromId, int toId) {
		try {
			List<TShopListMaterial> entityList = jdbcManager.from(TShopListMaterial.class)
														.where(new SimpleWhere()
														.eq(WztStringUtil.toCamelCase(TShopListMaterial.SHOP_LIST_ID), fromId))
														.disallowNoResult()
														.getResultList();

			for (TShopListMaterial entity : entityList) {
				deleteMaterial(toId, entity.materialKbn);
				entity.shopListId = toId;
				update(entity);
			}
		// 何も検索されなかった場合は終了
		} catch (SNoResultException e) {
			return;
		}
	}

	public void deleteMaterial(int shopListId, int materialKbn) {
		List<TShopListMaterial> entityList = jdbcManager.from(TShopListMaterial.class)
														.where(new SimpleWhere()
														.eq(WztStringUtil.toCamelCase(TShopListMaterial.SHOP_LIST_ID), shopListId)
														.eq(WztStringUtil.toCamelCase(TShopListMaterial.MATERIAL_KBN), materialKbn)
														.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
														.getResultList();
		if (CollectionUtils.isEmpty(entityList)) {
			return;
		}
		for (TShopListMaterial entity : entityList) {
			delete(entity);
		}
	}

	/**
	 * 指定した条件でサロゲートキー「id」のリストを取得します。
	 * @param shopListId
	 * @return
	 */
	public List<IdSelectDto> getIdList(int shopListId) {

		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT id FROM t_shop_list_material WHERE shop_list_id = ? AND delete_flg = ? AND material_kbn = 1";

		params.add(shopListId);
		params.add(DeleteFlgKbn.NOT_DELETED);

		return getIdList(sql, params);
	}



	/**
	 * 指定した条件でサロゲートキー「id」を取得します。
	 * @param shopListId
	 * @return
	 */
	public Integer getId(int shopListId, Integer materialKbn) {
		BeanMap result = jdbcManager.selectBySql(
				BeanMap.class,
				"SELECT id FROM t_shop_list_material WHERE shop_list_id = ? AND material_kbn = ? AND delete_flg = ?",
				shopListId, materialKbn, DeleteFlgKbn.NOT_DELETED)
				.getSingleResult();

		if (result == null) {
			return null;
		}
		return (Integer) result.get("id");
	}
}
package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TMaterial;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

/**
 * 素材のサービスクラスです。
 * @version 1.0
 */
public class MaterialService extends AbstractGroumetCareeBasicService<TMaterial> {

	/**
	 * webIdと素材区分を指定してエンティティを取得します。
	 * @param webId
	 * @param materialKbn
	 * @return
	 * @throws WNoResultException
	 */
	public TMaterial getMaterialEntity(int webId, int materialKbn)
	throws WNoResultException {

		SimpleWhere where = getWhereByWebId(webId, materialKbn);
		return findByCondition(where).get(0);
	}

	/**
	 * 素材データが存在しているかを取得します。
	 * @param webId
	 * @param materialKbn
	 * @return true:存在する、false:存在しない
	 */
	public boolean isMaterialEntityExist(int webId, int materialKbn) {

		SimpleWhere where = getWhereByWebId(webId, materialKbn);
		long count = countRecords(where);

		return (count > 0) ? true : false;
	}

	/**
	 * WebdataIdと素材区分を特定した検索条件です。
	 * @param webId
	 * @param materialKbn
	 * @return
	 */
	private SimpleWhere getWhereByWebId(int webId, int materialKbn) {
		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(TMaterial.WEB_ID), webId)
		.eq(toCamelCase(TMaterial.MATERIAL_KBN), materialKbn)
		.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
		;
		return where;
	}

	/**
	 * WEBIDをキー素材を物理削除
	 * @param webId WEBID
	 */
	public void deleteTMaterialByWebId(int webId) {

		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TMaterial.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TMaterial.WEB_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(webId)
				.execute();
	}

	/**
	 * 指定した条件でサロゲートキー「id」のリストを取得します。
	 * @param webId
	 * @return
	 */
	public List<IdSelectDto> getIdList(int webId) {

		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT id FROM t_material WHERE web_id = ? AND delete_flg = ? ";

		params.add(webId);
		params.add(DeleteFlgKbn.NOT_DELETED);

		return getIdList(sql, params);
	}
}
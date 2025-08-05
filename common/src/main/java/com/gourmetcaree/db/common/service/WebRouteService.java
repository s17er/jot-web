package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TWebRoute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

/**
 * WEBデータ路線図のサービスクラスです。
 * @version 1.0
 */
public class WebRouteService extends AbstractGroumetCareeBasicService<TWebRoute> {

	/**
	 * WEBIDをキーにWEBデータ路線図を物理削除
	 * @param webId WEBID
	 */
	public void deleteTWebRouteByWebId(int webId) {

		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebRoute.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebRoute.WEB_ID).append(" = ? ");

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
		String sql = "SELECT id FROM t_web_route WHERE web_id = ? AND delete_flg = ? ";

		params.add(webId);
		params.add(DeleteFlgKbn.NOT_DELETED);

		return getIdList(sql, params);
	}

	/**
	 * webIdからwebRouteを取得します。
	 * @param webId
	 * @return
	 */
	public List<TWebRoute> findByWebId(int webId) {
		return jdbcManager.from(TWebRoute.class)
							.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TWebRoute.WEB_ID), webId)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.getResultList();
	}



	/**
	 * webIdからwebRouteを取得します。
	 * @param webId
	 * @return
	 */
	public AutoSelect<TWebRoute> findSelectByWebId(int webId) {
		return jdbcManager.from(TWebRoute.class)
							.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TWebRoute.WEB_ID), webId)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							;
	}

}
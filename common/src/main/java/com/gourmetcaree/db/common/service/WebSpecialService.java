package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TWebSpecial;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

/**
 * WEBデータ特集のサービスクラスです。
 * @version 1.0
 */
public class WebSpecialService extends AbstractGroumetCareeBasicService<TWebSpecial> {

	/**
	 * WEBIDをキーにWEBデータ特集を物理削除
	 * @param webId WEBID
	 */
	public void deleteTWebSpecialByWebId(int webId) {

		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebSpecial.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebSpecial.WEB_ID).append(" = ? ");

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
		String sql = "SELECT id FROM t_web_special WHERE web_id = ? AND delete_flg = ? ";

		params.add(webId);
		params.add(DeleteFlgKbn.NOT_DELETED);

		return getIdList(sql, params);
	}

	/**
	 * WEBデータのIDから、特集IDのリストを取得します。
	 * @param webId
	 * @return
	 */
	public List<String> getSpecialIdListByWebId(int webId) {
		List<String> retList = jdbcManager.from(TWebSpecial.class)
					.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(TWebSpecial.WEB_ID), webId)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.iterate(new IterationCallback<TWebSpecial, List<String>>() {
						List<String> specialIdList = new ArrayList<String>();
						@Override
						public List<String> iterate(TWebSpecial entity, IterationContext context) {
							if (entity != null) {
								specialIdList.add(String.valueOf(entity.specialId));
							}
							return specialIdList;
						}
					});

		if (retList == null) {
			return new ArrayList<String>();
		}

		return retList;


	}
}
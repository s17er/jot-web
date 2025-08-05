package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MTerminal;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * ターミナルマスタのサービスクラスです。
 * @version 1.0
 */
public class TerminalService extends AbstractGroumetCareeBasicService<MTerminal> {

	/**
	 * ターミナルIDと都道府県コードのMapを取得する
	 * @return
	 */
	public Map<String, String> getTerminalMap() {

		Map<String, String> terminalIdMap = jdbcManager.from(MTerminal.class)
		 	.where(new SimpleWhere()
		 	.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
		 	.orderBy(MTerminal.ID)
		 	.iterate(new IterationCallback<MTerminal, Map<String, String>>() {
		 		Map<String, String> map = new HashMap<>();
		 		@Override
		 		public Map<String, String> iterate(MTerminal entity, IterationContext context) {
		 			if (entity != null) {
		 				map.put(String.valueOf(entity.id), String.valueOf(entity.prefecturesCd));
		 			}
		 			return map;
		 		}
			});

		 return terminalIdMap;
	}

	/**
	 * 都道府県コードで取得
	 * @param prefecturesCd
	 * @return
	 * @throws WNoResultException
	 */
	public List<MTerminal> findByPrefecturesCd(Integer prefecturesCd) throws WNoResultException {
		return findByCondition(createNotDeleteWhere().eq(toCamelCase(MTerminal.PREFECTURES_CD), prefecturesCd));
	}

	/**
	 * 一覧を全て取得
	 * @return ターミナル一覧
	 * @throws WNoResultException
	 */
	public List<MTerminal> getAllList() throws WNoResultException {
		return findByCondition(createNotDeleteWhere(),
				SqlUtils.createCommaStr(new String[]{
						toCamelCase(MTerminal.PREFECTURES_CD),
						toCamelCase(MTerminal.ID)
				}));
	}
}

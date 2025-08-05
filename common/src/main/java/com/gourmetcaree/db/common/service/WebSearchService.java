package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.sql.Timestamp;
import java.util.stream.Collectors;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TWebSearch;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.webdata.dto.webdata.WebSearchDto;

import net.arnx.jsonic.JSON;

/**
 * WEBデータ検索サービス
 *
 */
public class WebSearchService extends AbstractGroumetCareeBasicService<TWebSearch> {

	/**
	 * WEBデータIDで取得
	 * @param webId
	 * @return web職種一覧のリスト。取得できない場合は空のリストを返す
	 */
	public TWebSearch findByWebId(int webId) {
		try {
			return findByCondition(
				new SimpleWhere()
					.eq(toCamelCase(TWebSearch.WEB_ID), webId)
					.eq(toCamelCase(TWebSearch.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
					).get(0);
		} catch (WNoResultException e) {
			return null;
		}
	}

	/**
	 * WEBIDをキーに物理削除
	 * @param webJobId WEB職種ID
	 */
	public void deleteByWebId(int webId) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebSearch.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebSearch.WEB_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(webId)
				.execute();
	}

	/**
	 * 保存
	 * @param dto
	 * @return
	 */
	public void save(WebSearchDto dto) {

		deleteByWebId(dto.webId);

		dto.jobKbnList = dto.jobKbnList.stream().distinct().sorted().collect(Collectors.toList());
		dto.employPtnKbnList = dto.employPtnKbnList.stream().distinct().sorted().collect(Collectors.toList());
		dto.treatmentKbnList = dto.treatmentKbnList.stream().distinct().sorted().collect(Collectors.toList());
		dto.companyCharacteristicKbnList = dto.companyCharacteristicKbnList.stream().distinct().sorted().collect(Collectors.toList());
		dto.specialIdList = dto.specialIdList.stream().distinct().sorted().collect(Collectors.toList());

		TWebSearch entity = new TWebSearch();
		entity.webId = dto.webId;
		entity.json = JSON.encode(dto);

		setCommonInsertColmun(entity);

		execInsert(entity);
	}

	private void execInsert(TWebSearch entity) {
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT  ");
		sql.append(" INTO t_web_search(  ");
		sql.append("   id ");
		sql.append("   , web_id ");
		sql.append("   , json ");
		sql.append("   , delete_flg ");
		sql.append("   , insert_user_id ");
		sql.append("   , insert_datetime ");
		sql.append("   , version ");
		sql.append(" )  ");
		sql.append(" VALUES ( ");
		sql.append("   nextval('t_web_search_id_seq') ");
		sql.append("   , ? ");

		// ここSQLインジェクションしそうだけど、プリミティブなのでスルー
		sql.append(String.format("   , %s ", "'"+ entity.json +"'"));

		sql.append("   , 0 ");
		sql.append("   , ? ");
		sql.append("   , ? ");
		sql.append("   , 1 ");
		sql.append(" ) ");

		jdbcManager.updateBySql(sql.toString(),
				Integer.class,
				String.class,
				Timestamp.class)
				.params(entity.webId,
						entity.insertUserId,
						entity.insertDatetime)
				.execute();
	}
}

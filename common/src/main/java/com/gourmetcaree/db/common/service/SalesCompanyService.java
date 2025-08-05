package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;

import com.gourmetcaree.db.common.entity.VSalesCompany;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 営業担当者・会社ビューのサービスクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class SalesCompanyService extends AbstractGroumetCareeBasicService<VSalesCompany> {


	/**
	 * 検索のメインロジック
	 * @param targetPage 表示するページ数
	 * @throws WNoResultException
	 */
	public List<VSalesCompany> doSearch(PageNavigateHelper pageNavi,Map<String, String> map, int targetPage) throws WNoResultException {

		List<VSalesCompany> entityList = null;

		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		//カウント用SQL作成
		createSearchSql(sqlStr, params, map);

		//ページナビゲータを使用
		int count = (int)getCountBySql(sqlStr.toString(), params.toArray());
		pageNavi.changeAllCount(count);
		pageNavi.setPage(targetPage);

		try{
			entityList = getMsalesBySql(sqlStr.toString(), params.toArray(), pageNavi);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}

	/**
	 * 全件検索ロジック
	 * @param pageNavi ページナビヘルパークラス
	 * @param map 検索条件Map
	 * @return エンティティリスト
	 * @throws WNoResultException
	 */
	public List<VSalesCompany> doAllSearch(PageNavigateHelper pageNavi, Map<String, String> map) throws WNoResultException {

		List<VSalesCompany> entityList = null;

		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		//SQL作成
		createSearchSql(sqlStr, params, map);

		try {
			entityList = getAllMsalesBySql(sqlStr.toString(), params.toArray(), pageNavi);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}

	/**
	 * 検索用SQLを生成
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 */
	private void createSearchSql(StringBuilder sqlStr, List<Object> params, Map<String, String> map ) {

		sqlStr.append("SELECT *  FROM v_sales_company ");
		sqlStr.append("WHERE delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 営業担当者名が入力されている場合
		if (StringUtils.isNotBlank(map.get("salesName").toString())) {
			sqlStr.append("AND sales_name like ? ");
			params.add("%" + map.get("salesName") + "%");
		}

		// 営業担当者カナが入力されている場合
		if (StringUtils.isNotBlank(map.get("salesNameKana").toString())) {
			sqlStr.append("AND sales_name_kana like ? ");
			params.add("%" + map.get("salesNameKana") + "%");
		}

		// 会社が選択されている場合
		if (StringUtils.isNotBlank(map.get("companyId").toString())) {
			sqlStr.append("AND company_id = ?");
			params.add(NumberUtils.toInt(map.get("companyId")));
		}

		// 権限が選択されている場合
		if (StringUtils.isNotBlank(map.get("authorityCd").toString())) {
			sqlStr.append("AND authority_cd = ?");
			params.add(map.get("authorityCd"));
		}
	}

	/**
	 * 営業担当者のリストを取得
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 * @param pageNavi ﾍﾟｰｼﾞナビヘルパー
	 * @return 営業担当者エンティティリスト
	 */
	public List<VSalesCompany> getMsalesBySql(String sqlStr,Object[] params,PageNavigateHelper pageNavi) {

		StringBuilder wrapperSQL = new StringBuilder();

		//ORDER BYを有効にするために対象のSQLを大きなSQLでラップする。
		wrapperSQL.append(" SELECT " +
				" TEMP.* " +
				" FROM (" +
				sqlStr +
				" ) TEMP " +
				" ORDER BY " + pageNavi.sortKey +
				" OFFSET " + pageNavi.offset +
				" LIMIT " + pageNavi.limit);

		List<VSalesCompany> entityList = jdbcManager.selectBySql(VSalesCompany.class, wrapperSQL.toString(), params).disallowNoResult().getResultList();

		return entityList;

	}

	/**
	 * 営業担当者のリストを取得
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 * @param pageNavi ﾍﾟｰｼﾞナビヘルパー
	 * @return 営業担当者エンティティリスト
	 */
	private List<VSalesCompany> getAllMsalesBySql(String sqlStr,Object[] params,PageNavigateHelper pageNavi) {

		StringBuilder wrapperSQL = new StringBuilder();

		//ORDER BYを有効にするために対象のSQLを大きなSQLでラップする。
		wrapperSQL.append(" SELECT " +
				" TEMP.* " +
				" FROM (" +
				sqlStr +
				" ) TEMP " +
				" ORDER BY " + pageNavi.sortKey );

		List<VSalesCompany> entityList = jdbcManager.selectBySql(VSalesCompany.class, wrapperSQL.toString(), params).disallowNoResult().getResultList();

		return entityList;

	}
}
package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.SqlFileUpdate;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.service.S2AbstractService;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;



/**
 * 参照専用(ビュー)サービスのサービスクラスです。
 * @author Takahiro Ando
 * @version 1.0
 * @param <T> エンティティ
 */
public class AbstractGroumetCareeReferenceService<T> extends S2AbstractService<T> {

	private static final String UNSUPPORTED_MSG = "このメソッドはサポートされません";

	/*
	 * (非 Javadoc)
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#delete(java.lang.Object)
	 */
	@Override
	public int delete(T entity) {
		throw new UnsupportedOperationException(UNSUPPORTED_MSG);
	}

	/*
	 * (非 Javadoc)
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#insert(java.lang.Object)
	 */
	@Override
	public int insert(T entity) {
		throw new UnsupportedOperationException(UNSUPPORTED_MSG);
	}

	/*
	 * (非 Javadoc)
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#update(java.lang.Object)
	 */
	@Override
	public int update(T entity) {
		throw new UnsupportedOperationException(UNSUPPORTED_MSG);
	}

	/*
	 * (非 Javadoc)
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#updateBySqlFile(java.lang.String, java.lang.Object)
	 */
	@Override
	public SqlFileUpdate updateBySqlFile(String path, Object parameter) {
		throw new UnsupportedOperationException(UNSUPPORTED_MSG);
	}

	/*
	 * (非 Javadoc)
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#updateBySqlFile(java.lang.String)
	 */
	@Override
	public SqlFileUpdate updateBySqlFile(String path) {
		throw new UnsupportedOperationException(UNSUPPORTED_MSG);
	}

	/**
	 * BeanMapを条件とし、offset,limitを使用したSELECTを行います。
	 */
	public List<T> findByCondition(BeanMap beanMap, PageNavigateHelper navi) throws SNoResultException {

		List<T> retList = jdbcManager
		.from(entityClass)
		.where(beanMap)
		.orderBy(navi.sortKey)
		.offset(navi.offset).limit(navi.limit)
		.disallowNoResult()
		.getResultList();

		return retList;
	}

	/**
	 * Whereの実装オブジェクトを条件とし、SELECTを行います。
	 * @throws WNoResultException
	 */
	public List<T> findByCondition(Where where) throws WNoResultException {
		return findByCondition(where, "");
	}

	/**
	 * Whereの実装オブジェクトを条件とし、SELECTを行います。
	 * @throws WNoResultException
	 */
	public List<T> findByCondition(Where where, String sortKey)
	throws WNoResultException {
		List<T> retList = jdbcManager
							.from(entityClass)
							.where(where)
							.orderBy(sortKey)
							.getResultList();

		if (retList == null || retList.isEmpty()) {
			throw new WNoResultException("Select結果が0件です。");
		}

		return retList;
	}

	/**
	 * 指定した条件で、SELECTを行います。
	 * @param whereStr 条件の文字列
	 * @param params 検索値
	 * @param sortKey 表示順
	 * @return 検索結果のリスト
	 * @throws WNoResultException
	 */
	public List<T> findByCondition(String whereStr, Object[] params, String sortKey) throws WNoResultException {

		try {
			return jdbcManager
					.from(entityClass)
					.where(whereStr, params)
					.orderBy(sortKey)
					.disallowNoResult()
					.getResultList();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * Whereを条件とし、offset,limitを使用したSELECTを行います。
	 * @param where 検索条件
	 * @param navi ページナビゲーター
	 * @return 検索結果のリスト
	 * @throws SNoResultException
	 */
	public List<T> findByCondition(Where where, PageNavigateHelper navi) throws SNoResultException {

		List<T> retList = jdbcManager
			.from(entityClass)
			.where(where)
			.orderBy(navi.sortKey)
			.offset(navi.offset).limit(navi.limit)
			.disallowNoResult()
			.getResultList();

		return retList;
	}

	/**
	 * 指定した条件で、offset,limitを使用したSELECTを行います。
	 * @param whereStr 条件の文字列
	 * @param params 検索値
	 * @param navi ページナビゲーター
	 * @return 検索結果のリスト
	 * @throws WNoResultException
	 */
	public List<T> findByCondition(String whereStr, Object[] params, PageNavigateHelper navi) throws WNoResultException {

		try {
			return jdbcManager
					.from(entityClass)
					.where(whereStr, params)
					.orderBy(navi.sortKey)
					.offset(navi.offset).limit(navi.limit)
					.disallowNoResult()
					.getResultList();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * 指定した条件でテーブルを結合しデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param where 検索条件
	 * @param sortKey ソート順
	 * @return 検索結果のリスト
	 * @throws WNoResultException
	 */
	public List<T> findByConditionInnerJoin(String tableProperty, Where where, String sortKey) throws WNoResultException {

		try {
			return select()
					.innerJoin(tableProperty)
					.where(where)
					.orderBy(sortKey)
					.disallowNoResult()
					.getResultList();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * 指定した条件でテーブルを外部結合しデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param where 検索条件
	 * @param sortKey ソート順
	 * @return 検索結果のリスト
	 * @throws WNoResultException
	 */
	public List<T> findByConditionLeftJoin(String tableProperty, Where where, String sortKey) throws WNoResultException {

		try {
			return select()
					.leftOuterJoin(tableProperty)
					.where(where)
					.orderBy(sortKey)
					.disallowNoResult()
					.getResultList();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * 指定した条件でテーブルを結合しデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id データのID
	 * @param sortKey ソート順
	 * @return 検索結果
	 * @throws WNoResultException
	 */
	public T findByIdInnerJoin(String tableProperty, Integer id, String sortKey) throws WNoResultException {

		try {
			return select()
					.innerJoin(tableProperty)
					.id(id)
					.where(new SimpleWhere()
					.eq(StringUtil.camelize(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
					.orderBy(sortKey)
					.disallowNoResult()
					.getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * BeanMapを条件とし、COUNTした結果を返します。
	 * @param beanMap
	 * @return
	 */
	public long countRecords(BeanMap beanMap) {

		long count = jdbcManager
			.from(entityClass)
			.where(beanMap)
			.getCount();

		return count;
	}

	/**
	 * Whereを条件とし、COUNTした結果を返します。
	 * @param beanMap
	 * @return 件数
	 */
	public long countRecords(Where where) {

		long count = jdbcManager
			.from(entityClass)
			.where(where)
			.getCount();

		return count;
	}

	/**
	 * 指定した条件をもとにCOUNTした結果を返します。
	 * @param whereStr 条件の文字列
	 * @param params 検索値
	 * @return 件数
	 */
	public long countRecords(String whereStr, Object[] params) {

		long count = jdbcManager
			.from(entityClass)
			.where(whereStr, params)
			.getCount();

		return count;
	}

	/**
	 * SQLのカウント結果を返す
	 * @param map
	 * @param navi
	 * @return
	 */
	public long getCountBySql(String sql, Object[] params) {
		return jdbcManager.getCountBySql(sql, params);
	}

	/**
	 * データが存在するかチェックします。
	 * @param id 検索するID
	 * @return データが存在すればtrue、なければfalse
	 */
	public boolean isDataExists(Integer id) {
		return (int) select().id(id).getCount() > 0;
	}
}

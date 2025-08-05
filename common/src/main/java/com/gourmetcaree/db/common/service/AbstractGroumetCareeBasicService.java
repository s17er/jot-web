package com.gourmetcaree.db.common.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.service.S2AbstractService;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.AbstractCommonMasqueradeEntity;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.dto.ServiceAccessUser;
import com.gourmetcaree.db.common.service.dto.ServiceShopAccessUser;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * グルメキャリー用サービスの抽象クラスです。
 * @author Takahiro Ando
 * @version 1.0
 * @param <T> エンティティ
 */
public abstract class AbstractGroumetCareeBasicService<T> extends S2AbstractService<T> {

	protected final Logger serviceLog = Logger.getLogger(this.getClass());

	private ServiceAccessUser getServiceAccessUser() {
		ExternalContext ectx = SingletonS2ContainerFactory.getContainer().getExternalContext();
		if (ectx == null) {
			return null;
		}
		Map<?, ?> sessionMap = (Map<?, ?>) ectx.getSessionMap();
		return (ServiceAccessUser) sessionMap.get("userDto");
	}
	/**
	 * INSERT時の共通カラムにデータをセットします。
	 * @param Entity エンティティ
	 */
	protected void setCommonInsertColmun(T entity) {

		ServiceAccessUser serviceAccessUser = getServiceAccessUser();


		/* なりすまし営業の場合 */
		if (isMasqueradeId(serviceAccessUser)) {

			if (entity instanceof AbstractCommonMasqueradeEntity) {
				AbstractCommonMasqueradeEntity commonEntity = (AbstractCommonMasqueradeEntity) entity;
				commonEntity.insertDatetime = new Timestamp(new Date().getTime());
				commonEntity.insertMasqueradeDatetime = new Timestamp(new Date().getTime());
				commonEntity.version = 1L;
				if (serviceAccessUser != null) {
					commonEntity.insertMasqueradeId = serviceAccessUser.getUserId();
				}
			}

		/* なりすましでない場合 */
		} else {

			if (entity instanceof AbstractCommonEntity) {
				AbstractCommonEntity commonEntity = (AbstractCommonEntity) entity;
				commonEntity.insertDatetime = new Timestamp(new Date().getTime());
				commonEntity.version = 1L;
				if (serviceAccessUser != null) {
					commonEntity.insertUserId = serviceAccessUser.getUserId();
				}
			}
		}

	}

	/**
	 * UPDATE時の共通カラムにデータをセットします。
	 * @param entity エンティティ
	 */
	protected void setCommonUpdateColmun(T entity) {

		ServiceAccessUser serviceAccessUser = getServiceAccessUser();

		/* なりすまし営業の場合 */
		if (isMasqueradeId(serviceAccessUser)) {

			if (entity instanceof AbstractCommonMasqueradeEntity) {
				AbstractCommonMasqueradeEntity commonEntity = (AbstractCommonMasqueradeEntity) entity;
				commonEntity.updateMasqueradeDatetime = new Timestamp(new Date().getTime());
				if (serviceAccessUser != null) {
					commonEntity.updateMasqueradeId = serviceAccessUser.getUserId();
				}
			}

		/* なりすましでない場合 */
		} else {

			if (entity instanceof AbstractCommonEntity) {
				AbstractCommonEntity commonEntity = (AbstractCommonEntity) entity;
				commonEntity.updateDatetime = new Timestamp(new Date().getTime());
				if (serviceAccessUser != null) {
					commonEntity.updateUserId = serviceAccessUser.getUserId();
				}
			}
		}
	}

	/**
	 * 論理削除時の共通カラムにデータをセットします。
	 * @param entity エンティティ
	 */
	protected void setCommonLogicalDeleteColmun(T entity) {

		ServiceAccessUser serviceAccessUser = getServiceAccessUser();

		/* なりすまし営業の場合 */
		if (isMasqueradeId(serviceAccessUser)) {

			if (entity instanceof AbstractCommonMasqueradeEntity) {
				AbstractCommonMasqueradeEntity commonEntity = (AbstractCommonMasqueradeEntity) entity;
				commonEntity.deleteMasqueradeDatetime = new Timestamp(new Date().getTime());
				commonEntity.deleteFlg = DeleteFlgKbn.DELETED;
				if (serviceAccessUser != null) {
					commonEntity.deleteMasqueradeId = serviceAccessUser.getUserId();
				}
			}

		/* なりすましでない場合 */
		} else {

			if (entity instanceof AbstractCommonEntity) {
				AbstractCommonEntity commonEntity = (AbstractCommonEntity) entity;
				commonEntity.deleteDatetime = new Timestamp(new Date().getTime());
//				commonEntity.deleteFlg = AbstractCommonEntity.DeleteFlgValue.DELETED;
				commonEntity.deleteFlg = DeleteFlgKbn.DELETED;
				if (serviceAccessUser != null) {
					commonEntity.deleteUserId = serviceAccessUser.getUserId();
				}
			}
		}
	}

	/**
	 * エンティティがnullの場合は、エラーを返す。
	 * @param entity チェックするプロパティ
	 */
	protected void checkEmptyEntity(T entity) {

		// エンティティがnullの場合はエラー
		if (entity == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/*
	 * (非 Javadoc)
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#insert(java.lang.Object)
	 */
	@Override
	public int insert(T entity) {
		setCommonInsertColmun(entity);
		return jdbcManager.insert(entity).excludesNull().execute();
	}

	/*
	 * (非 Javadoc)
	 * ※このUPDATEメソッドではカラムの値をNULLにUPDATEすることはできません。
	 * NULLへのUPDATEが必要なカラムが存在する場合は、
	 * AbstractGroumetCareeBasicService.updateWithNull()を使用してください。
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#update(java.lang.Object)
	 */
	@Override
	public int update(T entity) {
		setCommonUpdateColmun(entity);
		return jdbcManager.update(entity).excludesNull().execute();
	}

	/**
	 * テーブルに対してUPDATEを行います。
	 * バージョン番号は無視します。
	 * @param entity
	 * @return
	 */
	public int updateIncludesVersion(T entity) {
		setCommonUpdateColmun(entity);
		return jdbcManager.update(entity).excludesNull().includesVersion().execute();
	}

	/**
	 * テーブルに対してUPDATEを行います。
	 * 引数で指定したカラムをNULLでUPDATEします。
	 * その他のカラムについてはNULLの場合はUPDATEの対象になりません。<br>
	 * 全てのカラムでNULLへのUPDATEを可能にするわけではないので、使用には注意してください。<br>
	 * @param entity
	 * @param nullEnableColumnList NULLでのUPDATEを許可するカラム
	 * @return
	 * deprecated Integerなどの数撃ちカラムの場合、NULLが適応されないかも
	 */
	public int updateWithNull(T entity, String... nullEnableColumnList) {
		setCommonUpdateColmun(entity);
		jdbcManager.update(entity).includes(nullEnableColumnList).includesVersion().execute();
		return jdbcManager.update(entity).excludes(nullEnableColumnList).excludesNull().execute();
	}

	/**
	 * 論理削除を行います。
	 * @param entity エンティティ
	 * @return 削除件数
	 */
	public int logicalDelete(T entity) {
		setCommonLogicalDeleteColmun(entity);
		return jdbcManager.update(entity).excludesNull().execute();

	}
	/**
	 * 論理削除を行います。
	 * バージョン番号を無視します。
	 * @param entity エンティティ
	 * @return 削除件数
	 */
	public int logicalDeleteIncludesVersion(T entity) {
		setCommonLogicalDeleteColmun(entity);
		return jdbcManager.update(entity).excludesNull().includesVersion().execute();
	}

	/**
	 * エンティティのリストをバッチで追加します。
	 * @param entities エンティティのリスト
	 * @return 件数の配列
	 */
	public int[] insertBatch(List<T> entities) {
		for (T entity : entities) {
			setCommonInsertColmun(entity);
		}

		return jdbcManager.insertBatch(entities).execute();
	}

	/**
	 * エンティティをバッチで追加します。
	 * @param entities エンティティ
	 * @return 件数の配列
	 */
	public int[] insertBatch(T... entities) {
		for (int i = 0; i < entities.length; i++) {
			setCommonInsertColmun(entities[i]);
		}

		return jdbcManager.insertBatch(entities).execute();
	}

	/**
	 * エンティティのリストを更新します。
	 * @param entities エンティティのリスト
	 * @return 件数の配列
	 */
	public int[] updateBatch(List<T> entities) {
		for (T entity : entities) {
			setCommonUpdateColmun(entity);
		}

		return jdbcManager.updateBatch(entities).execute();
	}

	/**
	 * エンティティを更新します。
	 * @param entities エンティティ
	 * @return 件数の配列
	 */
	public int[] updateBatch(T... entities) {
		for (int i = 0; i < entities.length; i++) {
			setCommonUpdateColmun(entities[i]);
		}

		return jdbcManager.updateBatch(entities).execute();
	}

	/**
	 * エンティティのリストを削除します。
	 * @param entities エンティティのリスト
	 * @return 件数の配列
	 */
	public int[] deleteBatch(List<T> entities) {
		return jdbcManager.deleteBatch(entities).execute();
	}

	/**
	 * エンティティを削除します。
	 * @param entities エンティティ
	 * @return 件数の配列
	 */
	public int[] deleteBatch(T... entities) {
		return jdbcManager.deleteBatch(entities).execute();
	}

	/**
	 * バージョンをチェックしないで物理削除を行う。
	 */
	public int deleteIgnoreVersion(T entity) {

		return jdbcManager.delete(entity)
					.ignoreVersion()
					.execute();
	}

	/**
	 * バージョンをチェックしないで物理削除を行う。
	 * @param entities エンティティのリスト
	 * @return 件数の配列
	 */
	public int[] deleteBatchIgnoreVersion(List<T> entities) {

		return jdbcManager.deleteBatch(entities)
					.ignoreVersion()
					.execute();
	}

	/**
	 * エンティティのリストを論理削除します。
	 * @param entities エンティティのリスト
	 * @return 件数の配列
	 */
	public int[] logicalDeleteBatch(List<T> entities) {
		for (T entity : entities) {
			setCommonLogicalDeleteColmun(entity);
		}

		return jdbcManager.updateBatch(entities).execute();
	}

	/**
	 * エンティティを論理削除します。
	 * @param entities エンティティ
	 * @return 件数の配列
	 */
	public int[] logicalDeleteBatch(T... entities) {
		for (int i = 0; i < entities.length; i++) {
			setCommonLogicalDeleteColmun(entities[i]);
		}

		return jdbcManager.updateBatch(entities).execute();
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
	 * Whereを条件とし、offset,limitを使用したSELECTを行います。
	 * @param where
	 * @param orwhere
	 * @param navi
	 * @return
	 * @throws SNoResultException
	 */
	public List<T> findByCondition(Where where, Where orwhere, PageNavigateHelper navi) throws SNoResultException {
		List<T> retList;
		if(orwhere != null) {
			 retList = jdbcManager
						.from(entityClass)
						.where(where, orwhere)
						.orderBy(navi.sortKey)
						.offset(navi.offset).limit(navi.limit)
						.disallowNoResult()
						.getResultList();
		}else {
			 retList = jdbcManager
						.from(entityClass)
						.where(where)
						.orderBy(navi.sortKey)
						.offset(navi.offset).limit(navi.limit)
						.disallowNoResult()
						.getResultList();
		}

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
	 * @return
	 */
	public long countRecords(Where where) {

		long count = jdbcManager
			.from(entityClass)
			.where(where)
			.getCount();

		return count;
	}

	/**
	 * Whereを条件とし、COUNTした結果を返します。
	 * @param beanMap
	 * @return
	 */
	public long countRecords(Where where, Where orWhere) {

		long count;
		if(orWhere != null) {
			count = jdbcManager
					.from(entityClass)
					.where(where, orWhere)
					.getCount();
		}else {
			count = jdbcManager
					.from(entityClass)
					.where(where)
					.getCount();
		}

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
	 * Whereを条件とし、テーブルを結合してCOUNTした結果を返します。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param where 検索条件
	 * @return 検索件数
	 */
	public long countRecordsInnerJoin(String tableProperty, Where where) {

		long count = jdbcManager
			.from(entityClass)
			.innerJoin(tableProperty)
			.where(where)
			.getCount();

		return count;
	}

	/**
	 * BeanMapを条件とし、取得結果から全件を取得するSELECTを行います。
	 */
	public List<T> findByNoOffset(BeanMap beanMap, String sortKey) throws SNoResultException {
		List<T> retList = jdbcManager
							.from(entityClass)
							.where(beanMap)
							.orderBy(sortKey)
							.disallowNoResult()
							.getResultList();

		return retList;
	}

	/**
	 * BeanMapを条件とし、取得結果のから最大件数までを取得するSELECTを行います。
	 */
	public List<T> findByNoOffset(BeanMap beanMap, String sortKey, int maxRow) throws SNoResultException {

		List<T> retList = jdbcManager
							.from(entityClass)
							.where(beanMap)
							.orderBy(sortKey)
							.limit(maxRow)
							.disallowNoResult()
							.getResultList();

		return retList;
	}

	/**
	 * 指定したIDで未削除データを取得します。
	 * @param id
	 * @return エンティティのリスト
	 */
	public T findById(Integer id) {
		return findById(id, DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 指定したIDで未削除データを取得します。
	 * @param id
	 * @param deleteFlg
	 * @return エンティティのリスト
	 */
	public T findById(Integer id, DeleteFlgKbn deleteFlg) {
		return jdbcManager.from(entityClass)
						.where(new SimpleWhere()
						.eq("id", id)
						.eq("deleteFlg", deleteFlg))
						.disallowNoResult()
						.getSingleResult();
	}

	/**
	 * 指定したIDで未削除データを取得します。
	 * 結果がない場合に例外を出さない
	 * @param id
	 * @return エンティティのリスト
	 */
	public T findByIdAllowNoResult(Integer id) {
		return jdbcManager.from(entityClass)
						.where(new SimpleWhere()
						.eq("id", id)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();
	}

	/**
	 * 論理削除を含む指定したIDで店舗を取得します。
	 * @param id
	 * @return エンティティのリスト
	 */
	public T findByIdIncludeDelete(Integer id) {
		return jdbcManager.from(entityClass)
						.where(new SimpleWhere()
						.eq("id", id))
						.disallowNoResult()
						.getSingleResult();
	}

	/**
	 * 指定したMemberIdで未削除データを取得します。
	 * @param memberId
	 * @return エンティティのリスト
	 */
	public T findByMemberId(Integer memberId) {
		return findByMemberId(memberId, DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 指定したMemberIdで未削除データを取得します。
	 * @param memberId
	 * @param deleteFlg
	 * @return エンティティのリスト
	 */
	public T findByMemberId(Integer memberId, DeleteFlgKbn deleteFlg) {
		return jdbcManager.from(entityClass)
						.where(new SimpleWhere()
						.eq("memberId", memberId)
						.eq("deleteFlg", deleteFlg))
						.disallowNoResult()
						.getSingleResult();
	}

	/**
	 * 指定したIDで未削除データを取得します。
	 * @param id IDの配列
	 * @return エンティティのリスト
	 * @throws WNoResultException
	 */
	public List<T> findById(Integer[] id) throws WNoResultException {
		return findById(id, DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 指定したIDでデータを取得します。
	 * @param id IDの配列
	 * @param deleteFlg
	 * @return エンティティのリスト
	 * @throws WNoResultException
	 */
	public List<T> findById(Integer[] id, DeleteFlgKbn deleteFlg) throws WNoResultException {
		try {
			return jdbcManager.from(entityClass)
			.where(new SimpleWhere()
			.in("id", (Object[]) id)
			.eq("deleteFlg", deleteFlg))
			.disallowNoResult()
			.getResultList();

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

	}

	/**
	 * 主キーに加え、条件を補足した上でデータを取得します。
	 * 削除フラグは未削除のデータのみ取得する。
	 * @param id
	 * @param where
	 * @return
	 * @throws WNoResultException
	 */
	public T findById(Integer id, SimpleWhere where) throws WNoResultException {
		try {
			return jdbcManager.from(entityClass)
			.where(where
				.eq("id", id)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
			.disallowNoResult()
			.getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
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
	 * SQLのカウント結果を返す
	 * @param map
	 * @param navi
	 * @return
	 */
	public long getCountBySql(String sql, Object[] params) {
		return jdbcManager.getCountBySql(sql, params);
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
	 * 指定した条件でテーブルを結合し、offset,limitを使用したSELECTを行います。
	 * @param tableProperty
	 * @param where 検索条件
	 * @param navi ページナビゲーター
	 * @return 検索結果のリスト
	 * @throws WNoResultException
	 */
	public List<T> findByConditionInnerJoin(String tableProperty, Where where, PageNavigateHelper navi) throws WNoResultException {
		try {
			return select()
					.innerJoin(tableProperty)
					.where(where)
					.orderBy(navi.sortKey)
					.offset(navi.offset).limit(navi.limit)
					.disallowNoResult()
					.getResultList();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * 指定した条件でテーブルを結合し取得したデータ件数を取得します
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param where 検索条件
	 * @return 件数
	 */
	public long countByConditionInnerJoin(String tableProperty, Where where) {

			return select()
						.innerJoin(tableProperty)
						.where(where)
						.getCount();
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
	 * 指定した条件で2つのテーブルを外部結合しデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty1 結合するテーブル1のプロパティ名
	 * @param tableProperty2 結合するテーブル2のプロパティ名
	 * @param where 検索条件
	 * @param sortKey ソート順
	 * @return 検索結果のリスト
	 * @throws WNoResultException
	 */
	public List<T> findByConditionLeftJoinTwoTables(String tableProperty1, String tableProperty2, Where where, String sortKey) throws WNoResultException {

		try {
			return select()
					.leftOuterJoin(tableProperty1)
					.leftOuterJoin(tableProperty2)
					.where(where)
					.orderBy(sortKey)
					.disallowNoResult()
					.getResultList();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * 指定した条件でテーブルを外部結合し未削除のデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id データのID
	 * @param sortKey ソート順
	 * @return 検索結果
	 * @throws WNoResultException
	 */
	public T findByIdLeftJoin(String tableProperty, Integer id, String sortKey) throws WNoResultException {
		return findByIdLeftJoin(tableProperty, id, new SimpleWhere(), sortKey, DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 指定した条件でテーブルを外部結合しデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id データのID
	 * @param sortKey ソート順
	 * @param deleteFlg 削除フラグ
	 * @return 検索結果
	 * @throws WNoResultException
	 */
	public T findByIdLeftJoin(String tableProperty, Integer id, String sortKey, DeleteFlgKbn deleteFlg) throws WNoResultException {
		return findByIdLeftJoin(tableProperty, id, new SimpleWhere(), sortKey, deleteFlg);
	}

	/**
	 * 主キーに加え、条件を補足した上で指定したテーブルと外部結合したデータを取得します。
	 * 削除フラグは未削除のデータのみ取得します。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id ID
	 * @param where 検索条件
	 * @param sortKey ソート順
	 * @return エンティティ
	 * @throws WNoResultException
	 */
	public T findByIdLeftJoin(String tableProperty, Integer id, SimpleWhere where, String sortKey) throws WNoResultException {
		return findByIdLeftJoin(tableProperty, id, where, sortKey, DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 主キーに加え、条件を補足した上で指定したテーブルと外部結合したデータを取得します。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id ID
	 * @param where 検索条件
	 * @param sortKey ソート順
	 * @param deleteFlg 削除フラグ
	 * @return エンティティ
	 * @throws WNoResultException
	 */
	public T findByIdLeftJoin(String tableProperty, Integer id, SimpleWhere where, String sortKey, DeleteFlgKbn deleteFlg) throws WNoResultException {
		try {
			return select()
				.leftOuterJoin(tableProperty)
				.id(id)
				.where(where
				.eq("deleteFlg",deleteFlg))
				.orderBy(sortKey)
				.disallowNoResult()
				.getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * 指定した条件でテーブルを結合し未削除のデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id データのID
	 * @param sortKey ソート順
	 * @return 検索結果
	 * @throws WNoResultException
	 */
	public T findByIdInnerJoin(String tableProperty, Integer id, String sortKey) throws WNoResultException {
		return findByIdInnerJoin(tableProperty, id, new SimpleWhere(), sortKey, DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 指定した条件でテーブルを結合しデータ共に取得します。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id データのID
	 * @param sortKey ソート順
	 * @param deleteFlg 削除フラグ
	 * @return 検索結果
	 * @throws WNoResultException
	 */
	public T findByIdInnerJoin(String tableProperty, Integer id, String sortKey, DeleteFlgKbn deleteFlg) throws WNoResultException {
		return findByIdInnerJoin(tableProperty, id, new SimpleWhere(), sortKey, deleteFlg);
	}

	/**
	 * 主キーに加え、条件を補足した上で指定したテーブルと内部結合したデータを取得します。
	 * 削除フラグは未削除のデータのみ取得します。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id ID
	 * @param where 検索条件
	 * @param sortKey ソート順
	 * @return エンティティ
	 * @throws WNoResultException
	 */
	public T findByIdInnerJoin(String tableProperty, Integer id, SimpleWhere where, String sortKey) throws WNoResultException {
		return findByIdInnerJoin(tableProperty, id, where, sortKey, DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 主キーに加え、条件を補足した上で指定したテーブルと内部結合したデータを取得します。
	 * @param tableProperty 結合するテーブルのプロパティ名
	 * @param id ID
	 * @param where 検索条件
	 * @param sortKey ソート順
	 * @param deleteFlg 削除フラグ
	 * @return エンティティ
	 * @throws WNoResultException
	 */
	public T findByIdInnerJoin(String tableProperty, Integer id, SimpleWhere where, String sortKey, DeleteFlgKbn deleteFlg) throws WNoResultException {
		try {
			return select()
				.innerJoin(tableProperty)
				.id(id)
				.where(where
				.eq("deleteFlg",deleteFlg))
				.orderBy(sortKey)
				.disallowNoResult()
				.getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * データが存在するかチェックします。<br />
	 * 論理削除されているデータは含みません。
	 * @param id 検索するID
	 * @return データが存在すればtrue、なければfalse
	 */
	public boolean isDataExists(Integer id) {
		return (int) select().id(id).where(new SimpleWhere().eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)).getCount() > 0;
	}

	/**
	 * 指定した条件でサロゲートキー「id」のリストを取得します。
	 * ※「SELECT id FROM ～」というSELECT句が渡されることを前提とした限定的なメソッドです。
	 * 使用する際には注意してください。
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<IdSelectDto> getIdList(String sql, List<Object> params) {

		try {
			List<IdSelectDto> retList = jdbcManager
										.selectBySql(IdSelectDto.class, sql, params.toArray(new Object[0]))
										.disallowNoResult()
										.getResultList();
			return retList;

		} catch (SNoResultException e) {
			return new ArrayList<IdSelectDto>();
		}
	}

	/**
	 * なりすまし営業ログインIDが存在するかチェックします。<br />
	 * 権限が管理者を対象とします。
	 * @param serviceAccessUser ユーザ情報
	 * @return 存在する場合、true, 存在しない場合、false
	 */
	public boolean isMasqueradeId(ServiceAccessUser serviceAccessUser) throws SNonUniqueResultException {

		ServiceShopAccessUser shopAccessUser = null;
		if (serviceAccessUser instanceof ServiceShopAccessUser) {
			shopAccessUser = (ServiceShopAccessUser) serviceAccessUser;
		}

		// なりすましログインでない場合
		if (shopAccessUser == null || !shopAccessUser.isMasqueradeFlg()) {
			return false;
		}

		SimpleWhere sw = new SimpleWhere();
		/* ログインID */
		sw.eq(WztStringUtil.toCamelCase(MSales.ID), serviceAccessUser.getUserId());
		/* 権限：管理者 */
		sw.eq(WztStringUtil.toCamelCase(MSales.AUTHORITY_CD), MSales.AuthLevelValue.OWNER_ADMIN);
		sw.eq(WztStringUtil.toCamelCase(MSales.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		try {

			MSales mSales = jdbcManager
				.from(MSales.class)
				.where(sw)
				.getSingleResult();

			if (mSales == null) {
				return false;
			}

		} catch (SNonUniqueResultException e) {
			throw new SNonUniqueResultException("検索結果が複数あります。");
		}

		return true;
	}

	/**
	 * 削除フラグに未削除を設定したSimpleWhereを返す
	 * @return
	 */
	protected SimpleWhere createNotDeleteWhere() {
		return new SimpleWhere().eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * テーブル名の取得
	 * @return
	 */
	protected String getTableName() {
		Table table = entityClass.getAnnotation(Table.class);
		if (table == null) {
			throw new IllegalStateException(String.format("Tableアノテーションのnameを指定してください。 エンティティクラス：[%s]", entityClass.getName()));
		}

		return table.name();
	}
}

package com.gourmetcaree.db.common.service;

import static org.seasar.framework.util.StringUtil.camelize;

import java.util.Date;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplicationTest;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 応募テストのサービスクラスです。
 * @version 1.0
 */
public class ApplicationTestService extends AbstractGroumetCareeBasicService<TApplicationTest> {

	/**
	 * 応募テスト確認時にデータが存在するかチェックします。
	 * @param entity 応募テストエンティティ
	 * @return データが存在すればtrue、存在しなければfalse
	 */
	public boolean isAppTestConfExists(TApplicationTest entity) {

		// 引数のチェック
		checkEmptyEntity(entity);

		return isAppTestExists(createConfWhere(entity));
	}

	/**
	 * データが存在するかチェックします。
	 * @param where 検索条件
	 * @return データが存在すればtrue、存在しなければfalse
	 */
	private boolean isAppTestExists(Where where) {

		// 応募テストを検索
		int count = (int) countRecords(where);

		// データが存在すればtrueを返却
		if (count > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 応募テスト確認の検索条件を返します。
	 * @param entity 応募テストエンティティ
	 * @return 応募テスト確認の検索条件
	 */
	private Where createConfWhere(TApplicationTest entity) {

		SimpleWhere where = new SimpleWhere();

		// ID
		where.eq(camelize(TApplicationTest.ID), entity.id);
		// アクセスコード
		where.eq(camelize(TApplicationTest.ACCESS_CD), entity.accessCd);
		// 削除フラグ
		where.eq(camelize(TApplicationTest.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return where;
	}


	/**
	 * 応募テスト確認がアクセス済みかどうか判別します。<br />
	 * 取得したデータはエンティティにセットされます。
	 * @param entity 応募テストエンティティ
	 * @return アクセス済みの場合はtrue、未アクセスの場合はfalse
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public boolean isAlreadyAccess(TApplicationTest entity) throws WNoResultException {

		try  {
			// データの取得(参照渡しのためコピー)
			Beans.copy(findById(entity.id), entity).execute();

		// データが無い場合はエラーを返す
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		// アクセス済みの場合はtrue
		if (MTypeConstants.AccessFlg.ALREADY == entity.accessFlg) {
			return true;
		}
		return false;
	}

	/**
	 * 応募テスト確認時のアクセス状況を更新します。
	 * @param entity 応募テストエンティティ
	 */
	public void updateApptestConf(TApplicationTest entity) {

		// アクセスフラグをアクセス済み
		entity.accessFlg = MTypeConstants.AccessFlg.ALREADY;
		// アクセス日時
		entity.accessDatetime = new Date();

		// データを更新
		update(entity);
	}

}
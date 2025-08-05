package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static com.gourmetcaree.db.common.entity.AbstractCommonEntity.*;
import static com.gourmetcaree.db.common.entity.TFootprint.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.framework.util.StringUtil.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity.DeleteFlgValue;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.TFootprint;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 足あとのサービスクラスです。
 * @version 1.0
 */
public class FootprintService extends AbstractGroumetCareeBasicService<TFootprint> {

	/**
	 * 足あと一覧のリストを返します。<br />
	 * 顧客マスタと内部結合し、表示対象のデータのみ取得します。
	 * @param memberId 会員ID
	 * @param limitMonth 表示期間
	 * @param targetPage 表示するページ
	 * @param pageNavi ページナビゲータ
	 * @return 指定された件数分の足あとリスト
	 * @throws WNoResultException 検索結果がない場合はエラー
	 */
	public List<TFootprint> getTFootList(int memberId, int limitMonth, int targetPage, PageNavigateHelper pageNavi) throws WNoResultException{

			//検索条件
			SimpleWhere where = createListWhere(memberId, limitMonth);
			// 顧客マスタ結合後の条件を追加する
			createJoinCustomerWhere(where);

			// レコードの件数を取得
			int count = (int) countRecordsInnerJoin(camelize(TFootprint.M_CUSTOMER), where);

			// ページ件数のセット
			pageNavi.changeAllCount(count);

			// 件数がない場合は処理しない
			if (count < 1) {
				throw new WNoResultException();
			}

			// 現在のページをセット
			pageNavi.setPage(targetPage);
			// ソート順セット
			pageNavi.setSortKey(createListSort());

			//足あとリストの取得
			return findByConditionInnerJoin(camelize(TFootprint.M_CUSTOMER), where, pageNavi);
	}

	/**
	 * 顧客IDと会員IDから足あと（気になる）を取得する
	 * @param customerId
	 * @param memberId
	 * @return 足あと　取得できなければnull
	 */
	public TFootprint getTFootprint(int customerId, int memberId) {
		return jdbcManager.from(TFootprint.class)
		.where(new SimpleWhere()
			.eq(toCamelCase(TFootprint.CUSTOMER_ID), customerId)
			.eq(toCamelCase(TFootprint.MEMBER_ID), memberId)
		).orderBy(desc(toCamelCase(TFootprint.ID)))
		.limit(1)
		.getSingleResult();
	}

	/**
	 * 足あと（気になる）の登録日時（更新日時）を取得します　気になるがNullならNull
	 * @param tFootprint
	 * @return 登録日時（更新日時）
	 */
	public Date getTFootprintDate(TFootprint tFootprint) {
		if(tFootprint == null) {
			return null;
		}else {
			if(tFootprint.updateDatetime == null) {
				return tFootprint.insertDatetime;
			}else {
				return tFootprint.updateDatetime;
			}
		}
	}

	/**
	 * 足あと一覧の検索条件を返します
	 * @param memberId 検索する会員ID
	 * @param limitMonth 表示する期間
	 * @return 足あと一覧の検索条件
	 */
	private SimpleWhere createListWhere(int memberId, int limitMonth) {

		//検索実行日から指定した月を引いた日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -limitMonth);

		// 検索条件の設定
		SimpleWhere where = new SimpleWhere()
		.eq(camelize(TFootprint.MEMBER_ID), memberId)					// 足あと.会員ID
		.eq(camelize(TFootprint.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)	// 足あと.削除フラグ
		.ge(camelize(TFootprint.ACCESS_DATETIME), cal.getTime());		// 足あと.訪問日時

		return where;
	}

	/**
	 * 顧客マスタと結合する際の条件を追加します。
	 * @param where セットする検索条件
	 * @return 顧客マスタの条件を追加した結果
	 */
	private SimpleWhere createJoinCustomerWhere(SimpleWhere where) {

		// 顧客マスタ.削除フラグ
		where.eq(dot(camelize(TFootprint.M_CUSTOMER), camelize(MCustomer.DELETE_FLG)), DeleteFlgKbn.NOT_DELETED);

		return where;
	}

	/**
	 * 足あと一覧の表示順を返します
	 * @return 足あと一覧の表示順
	 */
	private String createListSort(){

		String[] sortKey = new String[]{
				//ソート順を設定
				desc(camelize(TFootprint.ACCESS_DATETIME)),		//訪問日時の降順
				desc(camelize(TFootprint.ID))					//IDの降順
		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * 未閲覧の足あとデータが存在するかどうか判別します。
	 * @param memberId 会員ID
	 * @param limitMonth 表示する期間
	 * @return 未閲覧のデータが存在する場合はtrue、無い場合はfalse
	 */
	public boolean isNeverReadExist(int memberId, int limitMonth) {

		// 一覧表示の検索条件
		SimpleWhere where = createListWhere(memberId, limitMonth);
		// 閲覧フラグが未閲覧を条件に追加
		where.eq(camelize(TFootprint.READ_FLG), MTypeConstants.ReadFlg.NEVER);
		// 顧客マスタ結合後の条件を追加する
		createJoinCustomerWhere(where);

		// レコードの件数を取得
		int count = (int) countRecordsInnerJoin(camelize(TFootprint.M_CUSTOMER), where);

		// データがない場合はfalse
		if (count < 1) {
			return false;
		}

		return true;
	}

	/**
	 * 未閲覧の足あとを閲覧済に更新します。
	 * @param memberId 会員ID
	 */
	public void updateReadFlgAlready(int memberId) {

		// SQLの生成
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("UPDATE ");
		sqlStr.append(TABLE_NAME);
		sqlStr.append(" SET ");
		sqlStr.append(READ_FLG).append(" = ? ,");
		sqlStr.append(UPDATE_USER_ID).append(" = ? , ");
		sqlStr.append(UPDATE_DATETIME).append(" = ? ");
		sqlStr.append(" WHERE ");
		sqlStr.append(MEMBER_ID).append(" = ? ");
		sqlStr.append(" AND ");
		sqlStr.append(READ_FLG).append(" = ? ");
		sqlStr.append(" AND ");
		sqlStr.append(DELETE_FLG).append(" = ? ");

		// 実行オブジェクト生成
		SqlBatchUpdate batchUpdate =
			jdbcManager.updateBatchBySql(sqlStr.toString(),
				Integer.class,		// 閲覧フラグ
				Integer.class,		// 更新ユーザ
				Timestamp.class,	// 更新日時
				Integer.class,		// 会員ID
				Integer.class,		// 閲覧フラグ
				Integer.class		// 削除フラグ
			);

		// パラメータのセット
		batchUpdate.params(
			MTypeConstants.ReadFlg.ALREADY,			// 閲覧フラグを「閲覧済」
			memberId,								// 登録ユーザ（会員ID）
			new Timestamp(new Date().getTime()),	// 更新日時
			memberId,								// 会員ID
			MTypeConstants.ReadFlg.NEVER,			// 閲覧フラグ「未閲覧」が対象
			DeleteFlgValue.NOT_DELETED				// 未削除
		);

		// 更新処理実行
		batchUpdate.execute();
	}

	/**
	 * 指定した会員に有効な気になるがあるかチェックする
	 * @param customerId
	 * @param memberId
	 * @return
	 */
	public boolean isExitMemberFootPrint(int customerId, int memberId) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);

		return (int)jdbcManager.from(TFootprint.class)
		.where(new SimpleWhere()
			.eq(toCamelCase(TFootprint.CUSTOMER_ID), customerId)
			.eq(toCamelCase(TFootprint.MEMBER_ID), memberId)
			.ge(toCamelCase(TFootprint.ACCESS_DATETIME), cal.getTime())
		).getCount() > 0;

	}
}
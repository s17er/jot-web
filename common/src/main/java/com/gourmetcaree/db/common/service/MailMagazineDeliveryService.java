package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMailMagazineDelivery;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * メルマガ配信先のサービスクラスです。
 * @version 1.0
 */
public class MailMagazineDeliveryService extends AbstractGroumetCareeBasicService<TMailMagazineDelivery> {

	/**
	 * メルマガIDからメルマガ配信先を取得します。
	 * @param id メルマガID
	 * @return メルマガ配信先一覧
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<TMailMagazineDelivery> findByMailMagazineIdDelivery(Integer mailMagazineId) throws WNoResultException {

		try {
			SimpleWhere where = new SimpleWhere();
			// メルマガID
			where.eq(camelize(TMailMagazineDelivery.MAIL_MAGAZINE_ID), mailMagazineId);
			// 削除フラグ
			where.eq(camelize(TMailMagazineDelivery.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			// 配信先を取得する
			return findByCondition(where, createDeliverySort());

		} catch (SNoResultException e) {
			throw new WNoResultException("メルマガ配信先が0件です。メルマガID" + mailMagazineId);
		}
	}

	/**
	 * メルマガIDからメルマガ配信先を取得します。
	 * @param id メルマガID
	 * @return メルマガ配信先一覧
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<TMailMagazineDelivery> findByMailMagazineIdHeaderDelivery(Integer mailMagazineId, Integer areaCd) throws WNoResultException {

		try {
			SimpleWhere where = new SimpleWhere();
			// メルマガID
			where.eq(camelize(TMailMagazineDelivery.MAIL_MAGAZINE_ID), mailMagazineId);
			// エリアコード
			where.eq(camelize(TMailMagazineDelivery.AREA_CD), areaCd);
			// 削除フラグ
			where.eq(camelize(TMailMagazineDelivery.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			// 配信先を取得する
			return findByCondition(where, createDeliverySort());

		} catch (SNoResultException e) {
			throw new WNoResultException("メルマガ配信先が0件です。メルマガID" + mailMagazineId);
		}
	}

	/**
	 * 配信先一覧のソート順を返します。
	 * @return 配信先ソート順
	 */
	private String createDeliverySort(){

		String[] sortKey = new String[] {
			// ソート順を設定
			asc(camelize(TMailMagazineDelivery.USER_KBN)),		// ユーザー区分
			asc(camelize(TMailMagazineDelivery.AREA_CD)),		// エリアコード
			asc(camelize(TMailMagazineDelivery.DELIVERY_ID)),	// 配信先ID
			asc(camelize(TMailMagazineDelivery.ID)),			// ID
		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}


	/**
	 * 未配送の配送先のリストを取得します。
	 * @param mailMagazineId
	 * @return
	 * @throws WNoResultException
	 */
	public List<TMailMagazineDelivery> getNotDeliveryList(int mailMagazineId, int limit, int offset) throws WNoResultException {

		Where where = new SimpleWhere()
							.eq(toCamelCase(TMailMagazineDelivery.MAIL_MAGAZINE_ID), mailMagazineId)
							.eq(toCamelCase(TMailMagazineDelivery.DELIVERY_FLG), MTypeConstants.DeliveryFlg.NON)
							.eq(toCamelCase(TMailMagazineDelivery.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		String sortKey = TMailMagazineDelivery.ID;

		return jdbcManager.from(TMailMagazineDelivery.class)
					.where(where)
					.limit(limit)
					.offset(offset)
					.orderBy(sortKey)
					.getResultList();
	}
}
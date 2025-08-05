package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.asc;
import static org.seasar.framework.util.StringUtil.camelize;

import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * メルマガ詳細のサービスクラスです。
 * @version 1.0
 */
public class MailMagazineDetailService extends AbstractGroumetCareeBasicService<TMailMagazineDetail> {

	/**
	 * メルマガIDを元に、メルマガ詳細リストを取得します。
	 * @param mailMagazineId メルマガID
	 * @return メルマガ詳細リスト
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<TMailMagazineDetail> getListByMailMagazineId(int mailMagazineId) throws WNoResultException {

		// 検索条件の取得
		SimpleWhere where = new SimpleWhere()
			.eq(camelize(TMailMagazineDetail.MAIL_MAGAZINE_ID), mailMagazineId)		// メルマガID
			.eq(camelize(TMailMagazineDetail.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)	// 削除フラグ
			;

		// データを取得して返す
		return findByCondition(where, createListSort());
	}

	/**
	 * 詳細リストのソート順を返します。
	 * @return 詳細リストのソート順
	 */
	private String createListSort(){

		String[] sortKey = new String[] {
			// ソート順を設定
			asc(camelize(TMailMagazineDetail.TERMINAL_KBN)),// 端末区分
			asc(camelize(TMailMagazineDetail.ID))			// ID
		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}
}
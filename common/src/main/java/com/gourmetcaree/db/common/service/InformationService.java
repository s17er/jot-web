package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TInformation;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * お知らせのサービスクラスです。
 * @version 1.0
 */
public class InformationService extends AbstractGroumetCareeBasicService<TInformation> {

	/**
	 * お知らせエンティティを取得します。
	 * データベース上で1レコードしか存在しないことは保証されているので1件に絞っています。
	 * データは1件に保証されていますが、複数件存在した場合は最新の方を1件のみ取得します。
	 * そのためidの降順によるソートを行っています。
	 * @param managementScreenKbn
	 * @param areaCd
	 * @return
	 * @throws WNoResultException
	 */
	public TInformation getInformationEntity(int managementScreenKbn, int areaCd) throws WNoResultException {

		Where where = getWhereInformationEntity(managementScreenKbn, areaCd);
		List<TInformation> resultList = findByCondition(where, "id DESC");
		return resultList.get(0);
	}

	/**
	 * お知らせエンティティのリストを取得します。
	 * エリアコードによる昇順ソートを行っています。
	 * @param managementScreenKbn
	 * @return
	 * @throws WNoResultException
	 */
	public List<TInformation> getInformationList(int managementScreenKbn) throws WNoResultException {
		return findByCondition(getWhereInformationList(managementScreenKbn), "areaCd ASC");
	}

	/**
	 * MyPage画面用のお知らせデータ取得条件を作成します。
	 * エリアと管理画面区分（MyPage）を取得条件とします。
	 * @param managementScreenKbn
	 * @param areaCd
	 * @return
	 */
	private SimpleWhere getWhereInformationEntity(int managementScreenKbn, int areaCd) {
		SimpleWhere where = new SimpleWhere()
							.eq("managementScreenKbn", managementScreenKbn)
							.eq("areaCd", areaCd)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		return where;
	}

	/**
	 * 汎用的なお知らせのリスト取得条件を作成します。
	 * 管理画面区分のみを取得条件とします。
	 * @param managementScreenKbn
	 * @return
	 */
	private SimpleWhere getWhereInformationList(int managementScreenKbn) {
		SimpleWhere where = new SimpleWhere()
							.eq("managementScreenKbn", managementScreenKbn)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		return where;
	}

	/**
	 * お知らせの本文を取得します。
	 * レコードの取得に失敗した場合はブランクを返します。
	 * @param managementScreenKbn
	 * @param areaCd
	 * @return
	 */
	public String getInformationBody(int managementScreenKbn, int areaCd) {

		Where where = getWhereInformationEntity(managementScreenKbn, areaCd);
		List<TInformation> resultList;
		try {
			resultList = findByCondition(where, "id DESC");
		} catch (WNoResultException e) {
			return "";
		}

		//1件に絞ることができるので1件目を取得。
		TInformation entity = resultList.get(0);

		return entity.body;
	}
}
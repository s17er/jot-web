package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TScoutConsideration;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * スカウト検討中のサービスクラスです。
 * @version 1.0
 */
public class ScoutConsiderationService extends AbstractGroumetCareeBasicService<TScoutConsideration> {


	/**
	 * キープBOXに既に登録されているかチェックする
	 * @param customerId 顧客ID
	 * @param memberId 会員ID
	 * @return 既に登録されている場合、trueを返す
	 */
	public boolean isAlredyKeep(int customerId, int memberId) {

		Long count = jdbcManager.from(TScoutConsideration.class)
										.innerJoin("mMember", "mMember.id = ? AND mMember.deleteFlg = ?", memberId, DeleteFlgKbn.NOT_DELETED)
										.where(new SimpleWhere()
										.eq("memberId", memberId)
										.eq("customerId", customerId)
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
										.getCount();

		return count > 0 ? true : false;
	}

	/**
	 * 検討中BOXの会員一覧を取得
 	 * @param pageNavi ページナビヘルパー
	 * @param customerId 顧客ID
	 * @return
	 * @throws WNoResultException
	 */
	public List<MMember> getKeepList(PageNavigateHelper pageNavi, int customerId, String sortKey, int targetPage) throws WNoResultException {

		List<MMember> entityList = null;
		// キープBOXの会員数を取得
		int count = getScoutConsideCount(customerId);
		pageNavi.changeAllCount(count);

		//表示予定のページ数を調整
		pageNavi.setPage(targetPage);

		// キープBOXIDリストを取得
		List<Integer> idList = getIdList(customerId, pageNavi, sortKey);

		// キープBOX一覧を取得
		entityList = getScoutConsideMemberList(idList, pageNavi);

		return entityList;
	}

	/**
	 * 検討中BOX内の件数を取得
	 * @param customerId 顧客ID
	 * @return
	 */
	private int getScoutConsideCount(int customerId) {

		long count = jdbcManager.from(MMember.class)
									.innerJoin("tScoutConsideration", "tScoutConsideration.deleteFlg = ? AND tScoutConsideration.customerId = ? ",
											DeleteFlgKbn.NOT_DELETED, customerId)
									.innerJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
									.where(new SimpleWhere()
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.getCount();

		return (int) count;
	}

//	/**
//	 * IDリストを取得
//	 * @param customerId
//	 * @param pageNavi
//	 * @return
//	 * @throws WNoResultException
//	 */
//	private List<Integer> getIdList(int customerId, PageNavigateHelper pageNavi, String sortKey) throws WNoResultException {
//
//			try {
//				List<MMember> entityList = jdbcManager.from(MMember.class)
//															.innerJoin("tScoutConsideration", "tScoutConsideration.deleteFlg = ? AND tScoutConsideration.customerId = ? ",
//																	DeleteFlgKbn.NOT_DELETED, customerId)
//															.innerJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//															.where(new SimpleWhere()
//															.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
//															.orderBy(sortKey)
//															.limit(pageNavi.limit)
//															.offset(pageNavi.offset)
//															.disallowNoResult()
//															.getResultList();
//
//				List<Integer> idList = new ArrayList<Integer>();
//				for (MMember entity : entityList) {
//					idList.add(entity.tScoutConsideration.id);
//				}
//				return idList;
//			} catch (SNoResultException e) {
//				throw new WNoResultException();
//			}
//	}

	/**
	 * IDリストを取得
	 * @param customerId
	 * @param pageNavi
	 * @return
	 * @throws WNoResultException
	 */
	private List<Integer> getIdList(int customerId, PageNavigateHelper pageNavi, String sortKey) throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		AutoSelect<MMember> select = jdbcManager.from(MMember.class)
				.innerJoin("tScoutConsideration", "tScoutConsideration.deleteFlg = ? AND tScoutConsideration.customerId = ? ",
						DeleteFlgKbn.NOT_DELETED, customerId)
				.innerJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
						.where(where)
						.offset(pageNavi.offset)
						.orderBy(sortKey);

		if (pageNavi.limit > 0) {
			select.limit(pageNavi.limit);
		}





		final List<Integer> idList = new ArrayList<Integer>();

		select.iterate(new IterationCallback<MMember, Void>() {
			@Override
			public Void iterate(MMember entity, IterationContext context) {
				if (entity != null) {
					idList.add(entity.tScoutConsideration.id);
				}
				return null;
			}
		});

		if (CollectionUtils.isEmpty(idList)) {
			throw new WNoResultException();
		}

		return idList;
	}

	/**
	 * 検討中BOXの会員一覧を取得
	 * @param idList 検討中BOXのIDリスト
	 * @return
	 * @throws WNoResultException
	 */
	private List<MMember> getScoutConsideMemberList(List<Integer> idList, PageNavigateHelper pageNavi) throws WNoResultException {

		try {
			return jdbcManager.from(MMember.class)
										.innerJoin("tScoutConsideration", "tScoutConsideration.deleteFlg = ? ",
												DeleteFlgKbn.NOT_DELETED)
										.innerJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.leftOuterJoin("vMemberHopeCityList")
										.leftOuterJoin("mMemberAttributeList", "mMemberAttributeList.deleteFlg = ?",
												DeleteFlgKbn.NOT_DELETED)
										.where(new SimpleWhere()
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
										.in("tScoutConsideration.id", idList.toArray()))
										.orderBy(pageNavi.sortKey + ", vMemberHopeCityList.prefecturesCd, vMemberHopeCityList.cityCd")
										.disallowNoResult()
										.getResultList();
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * キープBOXから削除
	 * @param customerId 顧客ID
	 * @param memberIdArray 会員ID配列
	 */
	public void deleteKeepData(List<Integer> idList) {

		for (int id : idList) {
			TScoutConsideration entity = new TScoutConsideration();
			entity.id = id;
			deleteIgnoreVersion(entity);
		}
	}
}
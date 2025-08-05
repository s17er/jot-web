package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MRRoute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * リニューアル後の路線マスタのサービスクラスです。
 * @version 1.0
 */
public class RRouteService extends AbstractGroumetCareeBasicService<MRRoute> {

	public List<MRRoute> findAllList() {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(MRRoute.class)
				.where(where)
				.getResultList();
	}

	public Map<Integer, String> getAllNameMap() {
		List<MRRoute> list = findAllList();

		Map<Integer, String> valueMap = new HashMap<>();
		list.stream().forEach(action -> valueMap.put(action.lineCd, action.lineName));

		return valueMap;
	}

	/**
	 * 路線コードから検索をする
	 * @param lineCd 路線コード
	 * @return
	 */
	public MRRoute findByLineCd(int lineCd) {

		return jdbcManager.from(MRRoute.class)
				.where(new SimpleWhere()
						.eq(toCamelCase(MRRoute.LINE_CD), lineCd)
						.eq(toCamelCase(MRRoute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						)
				.getSingleResult();
	}

	/**
	 * 鉄道事業者コードで路線一覧を取得する
	 * @param companyCd
	 * @return 路線一覧のエンティティ
	 * @throws WNoResultException
	 */
	public List<MRRoute> findByCompanyCd(int companyCd) throws WNoResultException {
		return findByCondition(
				createNotDeleteWhere()
				.eq(toCamelCase(MRRoute.COMPANY_CD), companyCd)
				.eq(toCamelCase(MRRoute.E_STATUS), MRRoute.IN_OPERATIONAL)	// 運用中
				, toCamelCase(MRRoute.E_SORT));
	}

	/**
	 * 路線コードの一覧から検索をする
	 * @param companyCd 鉄道会社コード
	 * @param lineCd 路線コード
	 * @return 路線のエンティティリスト
	 * @throws WNoResultException
	 */
	public List<MRRoute> findByLineCdList(int companyCd, List<Integer> lineCdList) throws WNoResultException {
		if (CollectionUtils.isEmpty(lineCdList)) {
			throw new WNoResultException();
		}
		return findByCondition(
				createNotDeleteWhere()
				.in(toCamelCase(MRRoute.COMPANY_CD), companyCd)
					.in(toCamelCase(MRRoute.LINE_CD), lineCdList)
				, toCamelCase(MRRoute.E_SORT)
				);
	}
}

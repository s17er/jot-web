package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MRRailroad;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * リニューアル後の鉄道会社マスタのサービスクラスです。
 * @version 1.0
 */
public class RRailroadService extends AbstractGroumetCareeBasicService<MRRailroad> {

	public List<MRRailroad> findAllList() {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

		return jdbcManager.from(MRRailroad.class)
				.where(where)
				.getResultList();
	}

	public Map<Integer, String> getAllNameMap() {
		List<MRRailroad> list = findAllList();

		Map<Integer, String> valueMap = new HashMap<>();
		list.stream().forEach(action -> valueMap.put(action.companyCd, action.companyName));

		return valueMap;
	}

	/**
	 * 事業者CDから検索をする
	 * @param companyCd 事業者CD
	 * @return
	 */
	public MRRailroad findByCompanyCd(int companyCd) {

		return jdbcManager.from(MRRailroad.class)
				.where(new SimpleWhere()
						.eq(toCamelCase(MRRailroad.COMPANY_CD), companyCd)
						.eq(toCamelCase(MRRailroad.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						)
				.getSingleResult();
	}


	/**
	 * 鉄道事業者コードの一覧から検索をする
	 * @param companyCd 鉄道事業者コード
	 * @return 鉄道会社のエンティティリスト
	 * @throws WNoResultException
	 */
	public List<MRRailroad> findByCompanyCdList(List<Integer> companyCdList) throws WNoResultException {
		if (CollectionUtils.isEmpty(companyCdList)) {
			throw new WNoResultException();
		}
		return findByCondition(
				createNotDeleteWhere()
					.in(toCamelCase(MRRailroad.COMPANY_CD), companyCdList)
				, toCamelCase(MRRailroad.E_SORT)
				);
	}
}

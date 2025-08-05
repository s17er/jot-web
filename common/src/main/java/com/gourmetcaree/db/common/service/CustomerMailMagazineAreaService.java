package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MCustomerMailMagazineArea;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 顧客メルマガエリアのサービスクラスです。
 * @version 1.0
 */
public class CustomerMailMagazineAreaService extends AbstractGroumetCareeBasicService<MCustomerMailMagazineArea> {


	/**
	 * 顧客IDをキーに顧客メルマガエリアを物理削除
	 * @param customerId 顧客ID
	 * @param areaList 対象エリア
	 */
	public void deleteInsert(int customerId, List<Integer> areaList) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(MCustomerMailMagazineArea.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(MCustomerMailMagazineArea.CUSTOMER_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(customerId)
				.execute();

		if (CollectionUtils.isEmpty(areaList)) {
			return;
		}

		for (Integer areaCd : areaList) {
			MCustomerMailMagazineArea insertEntity = new MCustomerMailMagazineArea();
			insertEntity.customerId = customerId;
			insertEntity.areaCd = areaCd;
			insertEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(insertEntity);
		}
	}

	/**
	 * メルマガエリアを取得します
	 * @param customerId 顧客ID
	 * @return メルマガエリアの一覧。なければ空のリストを返す
	 */
	public List<Integer> getAreaList(int customerId) {
		try {
			List<MCustomerMailMagazineArea> list = findByCondition(new SimpleWhere()
								.eq(toCamelCase(MCustomerMailMagazineArea.CUSTOMER_ID), customerId),
							toCamelCase(MCustomerMailMagazineArea.AREA_CD));
			return list.stream().map(s -> s.areaCd).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}
}
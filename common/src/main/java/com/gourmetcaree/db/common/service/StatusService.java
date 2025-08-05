package com.gourmetcaree.db.common.service;

import static org.seasar.framework.util.StringUtil.camelize;

import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MStatus;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * ステータスマスタのサービスクラスです。
 * @version 1.0
 */
public class StatusService extends AbstractGroumetCareeBasicService<MStatus> {

	/**
	 * ステータスマスタを取得します。
	 * @param statusKbn ステータス区分
	 * @param noDisplayValue 非表示値
	 * @return List<MStatus> ステータスマスタのリスト
	 * @throws WNoResultException
	 */
	public List<MStatus> getStatusList(String statusKbn, String statusCd, List<Integer> noDisplayValue) throws WNoResultException {

		// 検索条件の設定
		Where where = new SimpleWhere()
				.eq(camelize(MStatus.STATUS_KBN), statusKbn)
				.eq(camelize(MStatus.STATUS_CD), statusCd)
				.notIn(camelize(MStatus.STATUS_CD),  noDisplayValue.toArray())
				.eq(camelize(MStatus.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
				;

		// 表示順の設定
		String sortKey = camelize(MStatus.DISPLAY_ORDER);

		// 検索した値を返却
		return findByCondition(where, sortKey);

	}
}
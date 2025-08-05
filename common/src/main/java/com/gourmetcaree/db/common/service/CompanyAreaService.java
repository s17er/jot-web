package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.entity.MCompanyArea;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 会社エリアマスタのサービスクラスです。
 * @version 1.0
 */
public class CompanyAreaService extends AbstractGroumetCareeBasicService<MCompanyArea> {


	/**
	 * 会社IDを指定して、会社エリアマスタを取得します。
	 * @param companyId 会社ID
	 * @return 会社エリアマスタのリスト
	 * @throws WNoResultException データが無い場合はエラー
	 */
	public List<MCompanyArea> getCompanyArea(String companyId) throws WNoResultException {

		// 検索条件の設定
		Where where = new SimpleWhere()
									.eq(StringUtil.camelize(MCompanyArea.COMPANY_ID), companyId);
		// 表示順
		String sortKey = StringUtil.camelize(MCompanyArea.ID);

		// 検索結果を返す
		return findByCondition(where, sortKey);

	}

	/**
	 * 指定した会社IDのエリアを取得します。
	 * @param companyId 会社ID
	 * @return エリアコードがセットされたリスト
	 * @throws WNoResultException データが無い場合はエラー
	 */
	public List<Integer> getCompanyAreaCd(String companyId) throws WNoResultException {

		// データを取得
		List<MCompanyArea> entityList = getCompanyArea(companyId);

		List<Integer> areaCdList = new ArrayList<Integer>();

		// エリアのリストにセットする
		for (MCompanyArea entity : entityList) {
			areaCdList.add(entity.areaCd);
		}

		// エリアコードが登録されたリストを返却
		return areaCdList;
	}
}
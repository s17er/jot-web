package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TInterest;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 気になるのサービスクラス
 *
 * @author t_shiroumaru
 *
 */
public class InterestService extends AbstractGroumetCareeBasicService<TInterest> {


	/**
	 * 気になる件数を取得する
	 * @param webId
	 * @return
	 */
	public int getInterestCount(int webId) {
		return (int)jdbcManager.from(TInterest.class)
				.where(new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(TInterest.WEB_ID), webId)
				.eq(WztStringUtil.toCamelCase(TInterest.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.getCount();
	}

	/**
	 * 指定したWebデータの気になるリストを取得する
	 * @param webId
	 * @return
	 */
	public List<TInterest> selectInterestByWebId(int webId) {
		return jdbcManager.from(TInterest.class)
			.where(new SimpleWhere()
			.eq(WztStringUtil.toCamelCase(TInterest.WEB_ID), webId)
			.eq(WztStringUtil.toCamelCase(TInterest.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY)
			.eq(WztStringUtil.toCamelCase(TInterest.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
			.orderBy("interest_datetime DESC")
			.getResultList();
	}

	/**
	 * 気になる応募者検索プロパティ
	 * @author t_shiroumaru
	 *
	 */
	public static class InterestSearchProperty extends PagerProperty {

		/** ページナビ */
		public PageNavigateHelper pageNavi;
	}
}

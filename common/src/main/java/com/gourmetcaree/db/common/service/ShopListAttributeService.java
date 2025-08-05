package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 店舗一覧属性サービス
 * @author Takehiro Nakamori
 *
 */
public class ShopListAttributeService extends AbstractGroumetCareeBasicService<TShopListAttribute> {


	/**
	 * 店舗一覧IDからエンティティを検索します。
	 * @param shopListId
	 * @return
	 */
	public List<TShopListAttribute> findByShopListId(int shopListId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.SHOP_LIST_ID), shopListId);
		where.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		try {
			return findByCondition(where);
		} catch (WNoResultException e) {
			return new ArrayList<TShopListAttribute>();
		}
	}


	/**
	 * 店舗一覧IDをキーに、店舗一覧属性を物理削除
	 * @param shopListId 店舗一覧ID
	 */
	public void deleteByShopListId(int shopListId) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TShopListAttribute.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TShopListAttribute.SHOP_LIST_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(shopListId)
				.execute();
	}



	/**
	 * 属性値を指定のカンマでつなげた文字列を作成します。
	 * @param shopListId 店舗一覧ID
	 * @param attributeCd 属性コード
	 * @return 属性値を指定のデリミタでつなげた文字列
	 */
	public String createJoinedAttributeValue(int shopListId, String attributeCd) {
		return createJoinedAttributeValue(shopListId, attributeCd, GourmetCareeConstants.KANMA_STR);
	}


	/**
	 * 属性値を指定のデリミタでつなげた文字列を作成します。
	 * @param shopListId 店舗一覧ID
	 * @param attributeCd 属性コード
	 * @param delimiter デリミタ
	 * @return 属性値を指定のデリミタでつなげた文字列
	 */
	public String createJoinedAttributeValue(int shopListId, String attributeCd, final String delimiter) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.SHOP_LIST_ID), shopListId);
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.ATTRIBUTE_CD), attributeCd);
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		final StringBuilder sb = new StringBuilder(0);
		jdbcManager.from(TShopListAttribute.class)
					.where(where)
					.iterate(new IterationCallback<TShopListAttribute, Void>() {

						@Override
						public Void iterate(TShopListAttribute entity, IterationContext context) {
							if (entity == null) {
								return null;
							}

							if (sb.length() > 0) {
								sb.append(delimiter);
							}
							sb.append(entity.attributeValue);
							return null;
						}
					});

		return sb.toString();
	}


	/**
	 * 属性値リストを作成します。
	 * @param shopListId 店舗一覧ID
	 * @param attributeCd 属性コード
	 * @return 属性値リスト
	 */
	public List<Integer> createAttributeValueList(int shopListId, String attributeCd) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.SHOP_LIST_ID), shopListId);
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.ATTRIBUTE_CD), attributeCd);
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		List<Integer> valueList;

		valueList = jdbcManager.from(TShopListAttribute.class)
								.where(where)
								.iterate(new IterationCallback<TShopListAttribute, List<Integer>>() {
									List<Integer> list = new ArrayList<Integer>();
									@Override
									public List<Integer> iterate(TShopListAttribute entity, IterationContext context) {
										if (entity != null) {
											list.add(entity.attributeValue);
										}
										return list;
									}
								});

		if (valueList == null) {
			return new ArrayList<Integer>();
		}

		return valueList;
	}


	/**
	 * 属性区分をカウントします。
	 * @param shopListId 店舗一覧ID
	 * @param attributeCd 属性コード
	 * @return 属性区分のカウント
	 */
	public int countAttributeKbn(int shopListId, String attributeCd) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.SHOP_LIST_ID), shopListId);
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.ATTRIBUTE_CD), attributeCd);
		where.eq(WztStringUtil.toCamelCase(TShopListAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return (int) countRecords(where);
	}


	/**
	 * 店舗IDと属性コードでエンティティのリストを取得します。
	 * @param shopListId 店舗ID
	 * @param attributeCd 属性コード
	 * @return 店舗属性のリスト。取得できない場合は空のリストを返す
	 */
	public List<TShopListAttribute> findByShopListIdAndAttributeCd(int shopListId, String attributeCd) {
		try {
			return findByCondition(
					createNotDeleteWhere()
					.eq(WztStringUtil.toCamelCase(TShopListAttribute.SHOP_LIST_ID), shopListId)
					.eq(WztStringUtil.toCamelCase(TShopListAttribute.ATTRIBUTE_CD), attributeCd)
					, WztStringUtil.toCamelCase(TShopListAttribute.ATTRIBUTE_VALUE));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

}

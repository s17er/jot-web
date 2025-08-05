package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 区分マスタのサービスクラスです。
 * @version 1.0
 */
public class TypeService extends AbstractGroumetCareeBasicService<MType> {

	/**
	 * 区分コードから値を取得
	 * @param typeCd 区分コード
	 * @return 区分マスタのリスト
	 * @throws WNoResultException データが取得できなかった場合はエラー
	 */
	public List<MType> getMTypeList(String typeCd) throws WNoResultException {

		// 検索条件の作成
		Where where = new SimpleWhere()
							.eq(StringUtil.camelize(MType.TYPE_CD), typeCd)
							.eq(StringUtil.camelize(MType.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		// 値の取得して返却
		return findByCondition(where, StringUtil.camelize(MType.DISPLAY_ORDER));
	}

	/**
	 * タイプコードとタイプ値からタイプをセレクト
	 * @param typeCd タイプコード
	 * @param typeValues タイプ値
	 * @return 対応するタイプ
	 */
	public List<MType> findByTypeCdAndTypeValues(String typeCd, Collection<Integer> typeValues){
		if (StringUtils.isBlank(typeCd) || CollectionUtils.isEmpty(typeValues)) {
			return Lists.newArrayList();
		}
		SimpleWhere where = new SimpleWhere();
		where.eq(StringUtil.camelize(MType.TYPE_CD), typeCd)
				.in(StringUtil.camelize(MType.TYPE_VALUE), typeValues);
		SqlUtils.addNotDeleted(where);

		return jdbcManager.from(entityClass)
				.where(where)
				.orderBy(MType.DISPLAY_ORDER)
				.getResultList();

	}

	/**
	 * タイプの値リストを取得します。
	 * @param typeCd
	 * @return
	 */
	public List<Integer> getTypeValueList(String typeCd) {
		List<Integer> cdList = getTypeSelect(typeCd)
					.iterate(new IterationCallback<MType, List<Integer>>() {
						private List<Integer> list = new ArrayList<Integer>();
						@Override
						public List<Integer> iterate(MType entity, IterationContext context) {
							if (entity != null) {
								list.add(entity.typeValue);
							}
							return list;
						}
					});

		return cdList;
	}


	/**
	 * 名前のリストを取得
	 * @return
	 */
	public List<String> getTypeNameList(List<Integer> valueList, String typeCd) {
		if (CollectionUtils.isEmpty(valueList) || StringUtils.isBlank(typeCd)) {
			return new ArrayList<String>(0);
		}
		SimpleWhere where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
							.in(WztStringUtil.toCamelCase(MType.TYPE_VALUE), valueList)
							.eq(WztStringUtil.toCamelCase(MType.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		List<MType> entityList = jdbcManager.from(MType.class)
									.where(where)
									.orderBy(MType.DISPLAY_ORDER)
									.getResultList();

		List<String> nameList = new ArrayList<String>(entityList.size());
		for (MType entity : entityList) {
			nameList.add(entity.typeName);
		}
		return nameList;

	}

	/**
	 * 別名のリストを取得
	 * @return
	 */
	public List<String> getTypeOtherNameList(List<Integer> valueList, String typeCd) {
		if (CollectionUtils.isEmpty(valueList) || StringUtils.isBlank(typeCd)) {
			return new ArrayList<String>(0);
		}
		SimpleWhere where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
							.in(WztStringUtil.toCamelCase(MType.TYPE_VALUE), valueList)
							.eq(WztStringUtil.toCamelCase(MType.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		List<MType> entityList = jdbcManager.from(MType.class)
									.where(where)
									.orderBy(MType.DISPLAY_ORDER)
									.getResultList();

		List<String> nameList = new ArrayList<String>(entityList.size());
		for (MType entity : entityList) {
			nameList.add(entity.typeOtherName);
		}
		return nameList;

	}


	/**
	 * タイプのセレクトを行う
	 * @param typeCd
	 * @return
	 */
	private AutoSelect<MType> getTypeSelect(String typeCd) {
		return jdbcManager.from(MType.class)
					.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.orderBy(MType.DISPLAY_ORDER);
	}

	/**
	 * 区分コードからデータを取得して、区分値をキーに区分名をセットしたMapを返します。
	 * @param typeCd 区分コード
	 * @return 区分値をキーに区分名をセットしたMap
	 * @throws WNoResultException データが取得できなかった場合はエラー
	 */
	public Map<Integer, String> getMTypeValueMap(String typeCd) throws WNoResultException {

		// 区分マスタの区分値、区分名を保持するMap
		Map<Integer, String> valueMap = new HashMap<Integer, String>();

		// 区分マスタから値を取得
		List<MType> typeList = getMTypeList(typeCd);

		// Mapにセット
		for (MType mType : typeList) {
			valueMap.put(mType.typeValue, mType.typeName);
		}
		return valueMap;
	}

	/**
	 * 区分コードからデータを取得して、区分値をキーに区分名をセットしたMapを返します。
	 * @param typeCd 区分コード
	 * @return 区分値をキーに区分名をセットしたMap
	 */
	public Map<Integer, String> getMTypeValueMapWithoutThrow(String typeCd) {
		try {
			return getMTypeValueMap(typeCd);
		} catch (WNoResultException e) {
			return new HashMap<Integer, String>(0);
		}
	}

	/**
	 * 携帯メールアドレスのドメインをチェック
	 * @param mobileMail 携帯メールアドレス
	 * @return 携帯メールアドレスであればtrueを返す
	 */
	public boolean checkMobileType(String mobileMail) {

		String mobileDomainStr = StringUtils.substringAfter(mobileMail.trim(), "@");

		List<MType> entityList = jdbcManager.from(MType.class)
									.where(new SimpleWhere()
									.eq("typeCd", MTypeConstants.MobileDomainKbn.TYPE_CD)
									.eq("typeName", mobileDomainStr)
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.getResultList();

		if (entityList != null && entityList.size() > 0) {
			return true;
		} else
			return false;
	}

	/**
	 * 存在するタイプかどうか
	 * @param typeCd
	 * @param typeValue
	 * @return
	 */
	public boolean existType(String typeCd, int typeValue) {
		List<MType> entityList = jdbcManager.from(MType.class)
				.where(new SimpleWhere()
				.eq("typeCd", typeCd)
				.eq("typeValue", typeValue)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.getResultList();

		return CollectionUtils.isNotEmpty(entityList);
	}

	/**
	 * 存在するタイプかどうか
	 * @param typeCd
	 * @param typeValue
	 * @return
	 */
	public boolean existType(String typeCd, String typeValue) {
		return existType(typeCd, NumberUtils.toInt(typeValue));
	}

	/**
	 * タイプの値を文字列配列で取得します。
	 * @param typeCd
	 * @return
	 */
	public String[] getTypeValueStringArray(String typeCd) {
		try {
			List<MType> entityList = getMTypeList(typeCd);

			List<String> valueList = new ArrayList<String>();
			for (MType entity : entityList) {
				valueList.add(String.valueOf(entity.typeValue));
			}

			return valueList.toArray(new String[0]);
		} catch (WNoResultException e) {
			return new String[0];
		}
	}



	/**
	 * タイプコード、タイプ値からタイプ名を取得します。
	 * @param typeCd
	 * @param typeValue
	 * @return
	 */
	public String getTypeName(String typeCd, Integer typeValue) {
		if (typeValue == null) {
			return "";
		}

		return getTypeName(typeCd, typeValue.intValue());
	}
	/**
	 * タイプコード、タイプ値からタイプ名を取得します。
	 * @param typeCd
	 * @param typeValue
	 * @return
	 */
	public String getTypeName(String typeCd, int typeValue) {
		try {
			MType entity = jdbcManager.from(MType.class)
					.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
					.eq(WztStringUtil.toCamelCase(MType.TYPE_VALUE), typeValue)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.disallowNoResult()
					.getSingleResult();

			return entity.typeName;

		// 見つからない場合はブランクを返す
		} catch (SNoResultException e) {
			return "";

		// 複数選択された場合はブランクを返す。
		} catch (SNonUniqueResultException e) {
			return "";
		}
	}

	/**
	 * タイプの名前を取得
	 * @param delimiter 分割文字
	 * @param typeCd タイプコード
	 * @param typeValues タイプ値
	 */
	public String getTypeName(String delimiter, String typeCd, Collection<Integer> typeValues) {
		SimpleWhere where = new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
						.in(WztStringUtil.toCamelCase(MType.TYPE_VALUE), typeValues)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);

		List<MType> entityList = jdbcManager.from(MType.class)
							.where(where)
							.orderBy(MType.DISPLAY_ORDER)
							.getResultList();

		int index = 0;
		StringBuilder sb = new StringBuilder();
		for (MType entity : entityList) {
			if (index++ > 0) {
				sb.append(delimiter);
			}
			sb.append(entity.typeName);
		}
		return sb.toString();
	}


	/**
	 * ソートした値のリストを取得します
	 * @param typeCd タイプコード
	 * @param typeValue タイプ値
	 * @return ソートした値のリスト
	 */
	public List<Integer> getSortedTypeValueList(String typeCd, List<Integer> typeValue) {
		if (StringUtils.isBlank(typeCd) || CollectionUtils.isEmpty(typeValue)) {
			return new ArrayList<Integer>();
		}

		List<Integer> resultList
			= jdbcManager.from(MType.class)
				.where(new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
				.in(WztStringUtil.toCamelCase(MType.TYPE_VALUE), typeValue))
				.orderBy(MType.DISPLAY_ORDER)
				.iterate(new IterationCallback<MType, List<Integer>>() {
					List<Integer> list = new ArrayList<Integer>();
					@Override
					public List<Integer> iterate(MType entity, IterationContext context) {
						if (entity != null) {
							list.add(entity.typeValue);
						}
						return list;
					}
				});

		if (resultList == null) {
			return new ArrayList<Integer>();
		}

		return resultList;
	}

	/**
	 * ソートした値のリストを取得します
	 * @param typeCd タイプコード
	 * @param typeValue タイプ値
	 * @return ソートした値のリスト
	 */
	public List<String> getSortedTypeList(String typeCd, List<Integer> typeValue) {
		if (StringUtils.isBlank(typeCd) || CollectionUtils.isEmpty(typeValue)) {
			return new ArrayList<String>();
		}

		List<String> resultList
			= jdbcManager.from(MType.class)
				.where(new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
				.in(WztStringUtil.toCamelCase(MType.TYPE_VALUE), typeValue))
				.orderBy(MType.DISPLAY_ORDER)
				.iterate(new IterationCallback<MType, List<String>>() {
					List<String> list = new ArrayList<String>();
					@Override
					public List<String> iterate(MType entity, IterationContext context) {
						if (entity != null) {
							list.add(String.valueOf(entity.typeValue));
						}
						return list;
					}
				});

		if (resultList == null) {
			return new ArrayList<String>();
		}

		return resultList;
	}




	public List<Integer> getTypeListWhereContains(String whereStr, String typeCd) {
		if (StringUtils.isBlank(whereStr)) {
			return new ArrayList<Integer>();
		}
		List<Integer> valueList
			=jdbcManager.from(MType.class)
				.where(new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
				.contains(WztStringUtil.toCamelCase(MType.TYPE_NAME), whereStr))
				.iterate(new IterationCallback<MType, List<Integer>>() {
					List<Integer> retList = new ArrayList<Integer>();
					@Override
					public List<Integer> iterate(MType entity, IterationContext context) {
						if (entity != null) {
							retList.add(entity.typeValue);
						}
						return retList;
					}
				});

		if (valueList == null) {
			return new ArrayList<Integer>();
		}

		return valueList;
	}


	/**
	 * タイプ値からカウントを取得します。
	 * @param typeCd タイプコード
	 * @param typeValues タイプ値
	 * @return タイプ値が一致するカウント
	 */
	public int countTypeValues(String typeCd, Collection<Integer> typeValues) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd);
		where.in(WztStringUtil.toCamelCase(MType.TYPE_VALUE), typeValues);
		where.eq(WztStringUtil.toCamelCase(MType.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return (int) jdbcManager.from(MType.class)
							.where(where)
							.getCount();
	}


    /**
     * タイプコードと非表示タイプ値からタイプをセレクト
     * @param typeCd タイプコード
     * @param typeValues 表示しないタイプ値のコレクション
     * @return 検索結果
     */
    public List<MType> findByTypeCdAndNotInTypeValues(String typeCd, Collection<Integer> typeValues) {
        SimpleWhere where = new SimpleWhere();
        where.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd);
        where.notIn(WztStringUtil.toCamelCase(MType.TYPE_VALUE), typeValues);
        SqlUtils.addNotDeleted(where);
        return jdbcManager.from(entityClass)
                .where(where)
                .orderBy(MType.DISPLAY_ORDER)
                .getResultList();
    }
}
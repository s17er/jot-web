package com.gourmetcaree.common.util;

import static org.seasar.framework.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringUtil;

import com.google.common.collect.Lists;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztJaStringUtil;
import jp.co.whizz_tech.commons.WztKatakanaUtil;
import jp.co.whizz_tech.commons.WztStringUtil;


/**
 * SQL作成、使用時に使用するユーティリティクラス
 * @author Takahiro Ando
 *
 */
public class SqlUtils {

	/** ランダム */
	public static final String RANDOM = "random()";

	/**
	 * SQL発行による検索のための？マークをカンマ区切りで作成する。
	 * 主にIN（）句で使用することを想定している。
	 * @param arrayLength
	 */
	public static String getQMarks(int arrayLength) {

		StringBuilder sb = new StringBuilder("");

		for (int i=0; i<arrayLength; i++) {
			if (i!=0) {
				sb.append(", ?");
			} else {
				sb.append("?");
			}
		}

		return sb.toString();
	}

	/**
	 * 削除されていないレコードを対象にするBeanMapを生成して返します。
	 * @return BeanMapオブジェクト
	 */
	public static BeanMap createBeanMap() {
		return createBeanMap(true);
	}

	/**
	 * BeanMapを生成して返します。
	 * @param deleteFlg 削除されていないレコードを対象にする場合はtrue、すべてのレコードが対象の場合はfalse
	 * @return BeanMap
	 */
	public static BeanMap createBeanMap(boolean deleteFlg) {
		BeanMap beanMap = new BeanMap();
		if (deleteFlg) {
			beanMap.put("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		}

		return beanMap;
	}

	/**
	 * ソートキーにASCをつけて返す
	 * @param key ソートキー
	 * @return ASCを追加したソートキー
	 */
	public static String asc(String key) {

		StringBuilder sb = new StringBuilder("");

		if (!StringUtil.isEmpty(key)) {
			sb.append(key).append(" ASC");
		}

		return sb.toString();
	}

	/**
	 * ソートキーにDESCをつけて返す
	 * @param key ソートキー
	 * @return DESCを追加したソートキー
	 */
	public static String desc(String key) {

		StringBuilder sb = new StringBuilder("");

		if (!StringUtil.isEmpty(key)) {
			sb.append(key).append(" DESC");
		}

		return sb.toString();
	}

	/**
	 * ソートキーにNULLS LASTをつけて返す
	 * @param key ソートキー
	 * @return NULLS LASTを追加したソートキー
	 */
	public static String nullsLast(String key) {

		StringBuilder sb = new StringBuilder("");

		if (!StringUtil.isEmpty(key)) {
			sb.append(key).append(" NULLS LAST");
		}

		return sb.toString();
	}

	/**
	 * カンマ区切りにして返します。
	 * @param values
	 * @return カンマ区切りに変換した文字列
	 */
	public static String createCommaStr(String[] values) {

		StringBuffer sb = new StringBuffer("");

		if (values == null) {
			return sb.toString();
		}

		// カンマをセット
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
			if (i < (values.length - 1)) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}

	/**
	 * ドットでつないで返します。
	 * @param table テーブル名
	 * @param column カラム名
	 * @return "."でつないだ文字列
	 */
	public static String dot(String table, String column) {

		return table + "." + column;
	}

	/**
	 * 検索条件の文字列を連結します。
	 * @param conjunction 連結条件
	 * @param propertyName プロパティ名
	 * @param condition 条件
	 * @return 連結した文字列
	 */
	private static String setpropertyName(String conjunction, String propertyName, String condition) {
		StringBuffer sql = new StringBuffer();
		return sql.append(conjunction).append(propertyName).append(condition).toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 = ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 = ?」をセットした文字列
	 */
	public static String eq(String columnName) {
		return setpropertyName(" ", camelize(columnName), " = ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 = ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 = ?」をセットした文字列
	 */
	public static String eqNoCamelize(String columnName) {
		return setpropertyName(" ", columnName, " = ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 = ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 = ?」をセットした文字列
	 */
	public static String andEq(String columnName) {
		return setpropertyName(" AND ", camelize(columnName), " = ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 = ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 = ?」をセットした文字列
	 */
	public static String andEqNoCamelize(String columnName) {
		return setpropertyName(" AND ", columnName, " = ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 = ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 = ?」をセットした文字列
	 */
	public static String orEq(String columnName) {
		return setpropertyName(" OR ", camelize(columnName), " = ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 = ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 = ?」をセットした文字列
	 */
	public static String orEqNoCamelize(String columnName) {
		return setpropertyName(" OR ", columnName, " = ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 != ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 != ?」をセットした文字列
	 */
	public static String ne(String columnName) {
		return setpropertyName(" ", camelize(columnName), " != ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 != ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 != ?」をセットした文字列
	 */
	public static String andNe(String columnName) {
		return setpropertyName(" AND ", camelize(columnName), " != ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 != ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 != ?」をセットした文字列
	 */
	public static String orNe(String columnName) {
		return setpropertyName(" OR ", camelize(columnName), " != ?");
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 >= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 >= ?」をセットした文字列
	 */
	public static String ge(String columnName) {
		return setpropertyName(" ", camelize(columnName), " >= ?");
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 >= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 >= ?」をセットした文字列
	 */
	public static String andGe(String columnName) {
		return setpropertyName(" AND ", camelize(columnName), " >= ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 >= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 >= ?」をセットした文字列
	 */
	public static String andGeNoCamelize(String columnName) {
		return setpropertyName(" AND ", columnName, " >= ?");
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 >= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 >= ?」をセットした文字列
	 */
	public static String orGe(String columnName) {
		return setpropertyName(" OR ", camelize(columnName), " >= ?");
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 > ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 > ?」をセットした文字列
	 */
	public static String gt(String columnName) {
		return setpropertyName(" ", camelize(columnName), " > ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 > ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 > ?」をセットした文字列
	 */
	public static String andGt(String columnName) {
		return setpropertyName(" AND ", camelize(columnName), " > ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 > ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 > ?」をセットした文字列
	 */
	public static String orGt(String columnName) {
		return setpropertyName(" OR ", camelize(columnName), " > ?");
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 <= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 <= ?」をセットした文字列
	 */
	public static String le(String columnName) {
		return setpropertyName(" ", camelize(columnName), " <= ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 <= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 <= ?」をセットした文字列
	 */
	public static String andLe(String columnName) {
		return setpropertyName(" AND ", camelize(columnName), " <= ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 <= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 <= ?」をセットした文字列
	 */
	public static String andLeNoCamelize(String columnName) {
		return setpropertyName(" AND ", columnName, " <= ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 <= ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 <= ?」をセットした文字列
	 */
	public static String orLe(String columnName) {
		return setpropertyName(" OR ", camelize(columnName), " <= ?");
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 < ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 < ?」をセットした文字列
	 */
	public static String lt(String columnName) {
		return setpropertyName(" ", camelize(columnName), " < ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 < ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 < ?」をセットした文字列
	 */
	public static String andLt(String columnName) {
		return setpropertyName(" AND ", camelize(columnName), " < ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 < ?」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 < ?」をセットした文字列
	 */
	public static String orLt(String columnName) {
		return setpropertyName(" OR ", camelize(columnName), " < ?");
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 IS NOT NULL 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 IS NOT NULL 」をセットした文字列
	 */
	public static String isNotNull(String columnName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" ").append(camelize(columnName)).append(" IS NOT NULL ").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 IS NOT NULL 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 IS NOT NULL 」をセットした文字列
	 */
	public static String andIsNotNull(String columnName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" AND ").append(camelize(columnName)).append(" IS NOT NULL ").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 IS NOT NULL 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 IS NOT NULL 」をセットした文字列
	 */
	public static String orIsNotNull(String columnName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" OR ").append(camelize(columnName)).append(" IS NOT NULL ").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 IS NULL 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 IS NULL 」をセットした文字列
	 */
	public static String isNull(String columnName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" ").append(camelize(columnName)).append(" IS NULL ").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 IS NULL 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 AND 」「 IS NULL 」をセットした文字列
	 */
	public static String andIsNull(String columnName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" AND ").append(camelize(columnName)).append(" IS NULL ").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 IS NULL 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @return 「 OR 」「 IS NULL 」をセットした文字列
	 */
	public static String orIsNull(String columnName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" OR ").append(camelize(columnName)).append(" IS NULL ").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String in(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" ").append(camelize(columnName)).append(" IN ( ").append(getQMarks(length)).append(" )").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 AND 」「 IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String inNoCamelize(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" ").append(columnName).append(" IN ( ").append(getQMarks(length)).append(" )").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 AND 」「 IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String andIn(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" AND ").append(camelize(columnName)).append(" IN ( ").append(getQMarks(length)).append(" )").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 AND 」「 IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String andInNoCamelize(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" AND ").append(columnName).append(" IN ( ").append(getQMarks(length)).append(" )").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 OR 」「 IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String orIn(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" OR ").append(camelize(columnName)).append(" IN ( ").append(getQMarks(length)).append(" )").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 OR 」「 IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String orInNoCamelize(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" OR ").append(columnName).append(" IN ( ").append(getQMarks(length)).append(" )").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 NOT IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 NOT IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String notIn(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" ").append(camelize(columnName)).append(" NOT IN ( ").append(getQMarks(length)).append(" )").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「 NOT IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 NOT IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String notInNoCamelize(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" ").append(columnName).append(" NOT IN ( ").append(getQMarks(length)).append(" )").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 NOT IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 AND 」「 NOT IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String andNotIn(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" AND ").append(camelize(columnName)).append(" NOT IN ( ").append(getQMarks(length)).append(" )").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「 NOT IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 AND 」「 NOT IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String andNotInNoCamelize(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" AND ").append(columnName).append(" NOT IN ( ").append(getQMarks(length)).append(" )").toString();
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「 NOT IN 」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 OR 」「 NOT IN 」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String orNotIn(String columnName, int length) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" OR ").append(camelize(columnName)).append(" NOT IN ( ").append(getQMarks(length)).append(" )").toString();
	}

	/**
	 * DBのカラム名をプロパティ名に変換し、「  LIKE ?」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 LIKE ?」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String like(String columnName) {
		return setpropertyName(" ", camelize(columnName), " LIKE ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「  LIKE ?」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 AND 」「 LIKE ?」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String andLike(String columnName) {
		return setpropertyName(" AND ", camelize(columnName), " LIKE ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 AND 」「  LIKE ?」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 AND 」「 LIKE ?」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String andLikeNoCamelize(String columnName) {
		return setpropertyName(" AND ", columnName, " LIKE ?");
	}
	/**
	 * DBのカラム名をプロパティ名に変換し、「 OR 」「  LIKE ?」句と指定した数の「 ? 」をセットした文字列を返却します。
	 * @param columnName カラム名
	 * @param length ?を付ける数
	 * @return 「 OR 」「 LIKE ?」句と指定した数の「 ? 」をセットした文字列
	 */
	public static String orLike(String columnName) {
		return setpropertyName(" OR ", camelize(columnName), " LIKE ?");
	}

	/**
	 * DBのカラム名をプロパティ名に返し、「TRANSLATE(columnName)」をセットした文字列を返却します。
	 */
	public static String translate(String columnName) {
		return setpropertyName(" TRANSLATE(", camelize(columnName), ")");
	}

	/**
	 * DBのカラム名をプロパティ名に返し、「UPPER(columnName)」をセットした文字列を返却します。
	 */
	public static String upper(String columnName) {
		return setpropertyName(" UPPER(", camelize(columnName), ")");
	}

	/**
	 * DBのカラム名にテキストの正規化（senna）を追加して返却します。
	 * @param columnName カラム名
	 * @return
	 */
	public static String jaNormalize(String columnName) {
		return new StringBuffer().append(" ja_normalize( ").append(columnName).append(")").toString();
	}

	/**
	 * DBのカラム名にテキストの正規化（senna）を追加して返却します。
	 * @param columnName カラム名
	 * @return
	 */
	public static String orJaNormalize(String columnName) {
		return new StringBuffer().append(" OR ja_normalize( ").append(columnName).append(")").toString();
	}

	/**
	 * 値の前に「%」をセットした文字列を返却します。
	 * @param param セットする値
	 * @return 「%」をセットした文字列
	 */
	public static String startPercent(String param) {
		StringBuffer sql = new StringBuffer();
		return sql.append("%").append(param).toString();
	}
	/**
	 * 値の後ろに「%」をセットした文字列を返却します。
	 * @param param セットする値
	 * @return 「%」をセットした文字列
	 */
	public static String endPercent(String param) {
		StringBuffer sql = new StringBuffer();
		return sql.append(param).append("%").toString();
	}
	/**
	 * 値の前後に「%」をセットした文字列を返却します。
	 * @param param セットする値
	 * @return 「%」をセットした文字列
	 */
	public static String containPercent(String param) {
		StringBuffer sql = new StringBuffer();
		return sql.append("%").append(param).append("%").toString();
	}

	/**
	 * 削除されていない条件を追加
	 */
	public static void addNotDeleted(SimpleWhere where) {
		where.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG),
				DeleteFlgKbn.NOT_DELETED);
	}

	public static void appendFreewordQuery(String searchWord, String column, StringBuilder sql, List<Object> params) {
	    if (StringUtils.isBlank(searchWord)) {
	        return;
        }
        final String[] words = searchWord.split("[ 　]");
        List<String> queries = Lists.newArrayList();
        for (String word : words) {
            queries.add(String.format(" %s LIKE ? ", column));
            params.add(String.format("%%%s%%", word));
        }

        sql.append(StringUtils.join(queries, " AND "));

    }

	/**
	 * 文字の正規化を行い、SQLを生成する
	 * @param value 検索条件の値
	 * @param params 検索条件の値を保持する
	 * @param args 検索に使用するSQL（可変長引数）
	 * @return 生成された
	 */
	public static String createLikeSearchNormalize(String value, List<Object> params, String... args) {

		String[] values = value.trim().split("[ |　]");
		List<String> valueWhere = new ArrayList<String>();

		String sql = createDelimiter(Arrays.asList(args), " ");

		for (String s : values) {
			valueWhere.add(" ( " + sql + " OR " + sql + " )");
			for (String ss : args) {
				params.add(WztJaStringUtil.zenToHan(WztKatakanaUtil.zenkakuToHankaku(containPercent(s)), WztJaStringUtil.ALL));
				params.add(WztJaStringUtil.zenToHan(WztKatakanaUtil.hankakuToZenkaku(containPercent(s)), WztJaStringUtil.ALL));
			}
		}

		return createDelimiter(valueWhere, " OR ");
	}

	/**
	 * Sennaを置き換えるためのメソッド
	 * @param value 検索条件の値
	 * @param params 検索条件の値を保持する
	 * @param args 検索に使用するSQL（可変長引数）
	 * @return 生成された
	 */
	public static String createLikeSearch(String value, List<Object> params, String... args) {

		String[] values = value.trim().split("[ |　]");
		List<String> valueWhere = new ArrayList<>();

		String sql = String.join(" ", args);

		for (String s : values) {
			valueWhere.add(" " + sql + " ");
			for (String ss : args) {
				params.add(containPercent(s));
			}
		}

		return String.join(" OR ", valueWhere);
	}

	private static String createDelimiter(List<String> list, String str) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {

			if (i == 0) {
				sb.append(list.get(i));
			} else {
				sb.append(str);
				sb.append(list.get(i));
			}
		}

		return sb.toString();
	}
}

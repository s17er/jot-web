package com.gourmetcaree.common.util;

import static org.apache.commons.lang.StringUtils.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.util.RequestUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;

import jp.co.whizz_tech.common.web.useragent.MobileUserAgent;
import jp.co.whizz_tech.common.web.useragent.NetUserAgent;
import jp.co.whizz_tech.commons.WztDateUtil;
import jp.co.whizz_tech.commons.WztStringUtil;
import jp.co.whizz_tech.commons.WztValidateUtil;


/**
 * グルメキャリー関するユーティリティクラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class GourmetCareeUtil {

	private static final Logger log = Logger.getLogger(GourmetCareeUtil.class);

	/** スラッシュ */
	private static final String DELIMITER = "/";

	/** 正規表現：URL */
	private static final String REG_URL = "(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+";

	/** 正規表現：メールアドレス */
	private static final String REG_MAIL = "[\\w\\.\\-\\+]+@([\\w\\-]+\\.)+[\\w\\-]+";

	/** 正規表現：メールアドレス(@より前) */
	private static final String REG_FOEWARD_MAIL = "[\\w\\.\\-]+";

	/** 正規表現：電話番号 */
	private static final String REG_TEL = "[0-9]{2,4}-?[0-9]{2,4}-?[0-9]{3,4}";

	/** 正規表現：電話番号 */
	private static final Pattern REG_TEL_PATTERN = Pattern.compile(REG_TEL);

	/** Aタグ（通常のAタグ） */
	private static final String TAG_A = "<a href='$0'>$0</a>";

	/** Aタグ（mailto用）*/
	private static final String TAG_MAILTO = "<a href='mailto:$0'>$0</a>";

	/** Aタグ（tel用）*/
	private static final String TAG_TEL = "<a href='tel:$0'>$0</a>";

	/** Aタグ（telのコンバージョン用）*/
	private static final String TAG_TEL_CONVERSION = "<a href=\"tel:$0\" onClick=\"goog_report_conversion('tel:$0');yahoo_report_conversion(undefined);return false;\" >$0</a>";

	/** Aタグ（プレビューのtel用）*/
	private static final String TAG_TEL_FOR_PREVIEW = "<a href='#' onclick='return false;'>$0</a>";

	/** カタカナの正規表現 */
	private static final String KATAKANA_REGEX = "[ァ-ヶ・ーヽヾ]";

	/** 整数の正規表現 */
	private static final String INTEGER_REGEX = "[0-9]+";

	/** システムハッシュのソルトキー */
	private static final String SYSTEM_HASH_SOLT = "ginza";


	/** 市町村区リスト */
	private static List<String> MUNICIPALITY_LIST;
	static{
		MUNICIPALITY_LIST = new ArrayList<String>();
		MUNICIPALITY_LIST.add("市川市");
		MUNICIPALITY_LIST.add("市原市");
		MUNICIPALITY_LIST.add("四日市市");
		MUNICIPALITY_LIST.add("廿日市市");
		MUNICIPALITY_LIST.add("町田市");
		MUNICIPALITY_LIST.add("十日町市 ");
		MUNICIPALITY_LIST.add("大町市");
		MUNICIPALITY_LIST.add("東村山市");
		MUNICIPALITY_LIST.add("武蔵村山市");
		MUNICIPALITY_LIST.add("羽村市 ");
		MUNICIPALITY_LIST.add("村上市 ");
		MUNICIPALITY_LIST.add("大村市 ");
		Collections.unmodifiableList(MUNICIPALITY_LIST);
	}


	/** 生成できません */
	GourmetCareeUtil() { }

	/**
	 * エラーIDを生成します。
	 * @return エラーID
	 */
	public static String createErrorId() {
		StringBuilder sb = new StringBuilder();
		sb.append(WztDateUtil.currentDateString(GourmetCareeConstants.ERROR_ID_RADIX).toUpperCase());
		sb.append("-");
		sb.append(WztDateUtil.currentTimeString(GourmetCareeConstants.ERROR_ID_RADIX).toUpperCase());

		return sb.toString();
	}

	/**
	 * メールのアクセスコードを生成します。
	 * @return アクセスコード
	 */
	public static String createAccessCode() {
		return RandomStringUtils.randomAlphanumeric(GourmetCareeConstants.MAIL_ACCESS_CODE_LEN);
	}

	/**
	 * 基本となるパスとメソッド名を元に、スラッシュ区切りでパラメータを連結したパスを返します。
	 * basePath･･･スラッシュが両端に着いたAction名までのパス（例：/empAccount/empAccountSearch/）
	 * methodName･･･スラッシュ無しのメソッド名（例：search）
	 * params･･･可変長引数。スラッシュ区切りで設定するパラメータ全て。
	 * @param basePath
	 * @param methodName
	 * @param params
	 * @return
	 */
	public static String makePath(String basePath, String methodName, String... params) {

		StringBuilder sb = new StringBuilder("");
		sb.append(basePath);
		sb.append(methodName);

		if (params == null) {
			return sb.toString();
		}

		//パラメータを連結
		for (String str : params) {
			sb.append(DELIMITER);
			sb.append(str);
		}

		return sb.toString();
	}

	/**
	 * 文字列中のURLをリンクに変換します。
	 * 実際の処理はmakeLink()に委譲。
	 * @param targetText
	 * @return
	 */
	public static String makeUrlLink(String targetText) {
		return GourmetCareeUtil.makeLink(targetText, REG_URL, TAG_A);
	}

	/**
	 * 文字列中のURLをtarget属性付きのリンクに変換します。
	 * 実際の処理はmakeLink()に委譲。
	 * @param targetText
	 * @return
	 */
	public static String makeUrlLinkWithTarget(String targetText, String targetElement) {

		StringBuilder link = new StringBuilder("");
		link.append("<a href='$0' target='");
		link.append(targetElement);
		link.append("'>$0</a>");

		return GourmetCareeUtil.makeLink(targetText, REG_URL, link.toString());
	}

	/**
	 * 文字列中のメールアドレスをリンクに変換します。
	 * 実際の処理はmakeLink()に委譲。
	 * @param targetText
	 * @return
	 */
	public static String makeMailLink(String targetText) {
		return GourmetCareeUtil.makeLink(targetText, REG_MAIL, TAG_MAILTO);
	}

	/**
	 * 文字列中のメールアドレスをsubject付きのリンクに変換します。
	 * 実際の処理はmakeLink()に委譲。
	 * @param targetText
	 * @return
	 */
	public static String makeMailLink(String targetText, String subject) {

		StringBuilder link = new StringBuilder("");
		link.append("<a href='mailto:$0?subject=");
		link.append(subject);
		link.append("'>$0</a>");

		return GourmetCareeUtil.makeLink(targetText, REG_MAIL, link.toString());
	}

	/**
	 * 文字列中の電話番号をリンクに変換します。
	 * 実際の処理はmakeLink()に委譲。
	 * @param targetText
	 * @return
	 */
	public static String makeTelLink(String targetText) {
		return GourmetCareeUtil.makeLink(targetText, REG_TEL, TAG_TEL);
	}


	/**
	 * 文字列中の電話番号をコンバージョンタグのリンクに変換します。
	 *
	 * @param value 文字列
	 * @return
	 */
	public static String editTelConversionTag(String value) {
		return makeLink(value, REG_TEL_PATTERN, TAG_TEL_CONVERSION);
	}

	/**
	 * 文字列中の電話番号をリンクに変換します。
	 * プレビュー用のためリンク先はセットしません。
	 * 実際の処理はmakeLink()に委譲。
	 * @param targetText
	 * @return
	 */
	public static String makeTelLinkForPreview(String targetText) {
		return GourmetCareeUtil.makeLink(targetText, REG_TEL, TAG_TEL_FOR_PREVIEW);
	}

	/**
	 * 文字列中の最初に出現する電話番号フォーマットを取得します。
	 *
	 * @param targetText
	 * @return
	 */
	public static String findTelFirstElement(String targetText) {

		return findTelFirstElement(targetText, false);
	}

	/**
	 * 文字列中の最初に出現する電話番号フォーマットを取得します。
	 *
	 * @param targetText 指定文字列
	 * @param removeHyphenFlg ハイフンを取り除いて返すフラグ
	 *
	 * @return
	 */
	public static String findTelFirstElement(String targetText, boolean removeHyphenFlg) {

		Pattern pattern = Pattern.compile(REG_TEL, Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(targetText);

		if (match.find()) {
			String retStr = match.group();

			// ハイフンを取り除く
			if (removeHyphenFlg) {
				return retStr.replace("-", "");
			}

			return retStr;
		}

		return null;
	}

	/**
	 * 指定されたパターンを抽出し、指定された形式に全置換します。
	 * 例）URL、、メールをAタグのリンクにする。
	 * @param targetText
	 * @return
	 */
	public static String makeLink(String targetText, String regPattern, String linkTag){

		Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(targetText);
		return match.replaceAll(linkTag);
	}

	/**
	 * 指定されたパターンを抽出し、指定された形式に全置換します。
	 * 例）URL、、メールをAタグのリンクにする。
	 * @param targetText
	 * @return
	 */
	public static String makeLink(String targetText, Pattern pattern, String linkTag){

		Matcher match = pattern.matcher(targetText);
		return match.replaceAll(linkTag);
	}

	/**
	 * 指定されたメールアドレスが正しいのか判断します。
	 * @param mailAddress
	 * @return
	 */
	public static boolean checkMailAddress(String mailAddress) {

		if (Pattern.matches(REG_MAIL, mailAddress)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 指定されたメールアドレスの@より前が正しいのか判断します。
	 * @param mailAddress
	 * @return
	 */
	public static boolean checkForwardMailAddress(String mailAddress) {

		if (Pattern.matches(REG_FOEWARD_MAIL, mailAddress)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 下限年齢と上限年齢をもとに資格用の文字列を生成
	 * @param lowerAge
	 * @param upperAge
	 * @return
	 */
	public static String convertQualificationData(Integer lowerAge , Integer upperAge) {

		StringBuilder sb = new StringBuilder("");

		if (lowerAge != null && lowerAge != 0) {
			sb.append(lowerAge).append("才 ～ ");
		}

		if (upperAge != null && upperAge != 0) {
			sb.append(upperAge).append("才位まで");
		}

		return sb.toString();
	}

	/**
	 * URL,メールをdummyへ置き換える
	 * 置き換えた文字列をmapへ入れる
	 * @param target
	 * @param pattern
	 * @param linkPattern
	 * @param tag
	 * @param map
	 * @return
	 */
	public static String convertLink(String target, String pattern, String tag, Map<Integer, String> map) {

		int i = map.size();

		Pattern pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher match = pat.matcher(target);

		while (match.find()) {
			target = match.replaceFirst("\"\"dummy" + i + "\"\"");
			Matcher m = pat.matcher(match.group());
			map.put(i, m.replaceAll(tag));
			match = pat.matcher(target);
			i++;
		}

		return target;
	}

	/**
	 * URL,メールをdummyからリンク文字列へ置き換える
	 * @param target
	 * @param map
	 * @return
	 */
	public static String replaceLinkTag(String target, Map<Integer, String> map) {
		for (int k = 0; k < map.size(); k++) {
			Pattern pat = Pattern.compile("\"\"dummy" + k + "\"\"", Pattern.CASE_INSENSITIVE);
			Matcher match = pat.matcher(target);
			target = match.replaceAll(map.get(k));
		}

		return target;
	}

	/**
	 * 文字列中のURLをdummyへ置き換えてからリンクに変換します。
	 * 実際の処理はdummyへ置き換える処理はconvertLink()に委譲。
	 * dummyからリンクに変換する処理はreplaceLinkTag()に委譲。
	 * @param target
	 * @return
	 */
	public static String convertLinkAll(String target) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		// 文字中のURLをdummyへ置き換えます。
		String str = GourmetCareeUtil.convertLink(target, REG_URL, TAG_A, map);
		str = GourmetCareeUtil.convertLink(str, REG_MAIL, TAG_MAILTO, map);
		str = GourmetCareeUtil.convertLink(str, REG_TEL, TAG_TEL, map);
		// dummyからリンクに変換します。
		return GourmetCareeUtil.replaceLinkTag(str, map);
	}

	/**
	 *
	 * 文字列中のURL、メールアドレスをdummyへ置き換えてからリンクに変換します。
	 * 実際の処理はdummyへ置き換える処理はconvertLink()に委譲。
	 * dummyからリンクに変換する処理はreplaceLinkTag()に委譲。
	 * @param target
	 * @return
	 */
	public static String convertPcLink(String target) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		// 文字中のURLをdummyへ置き換えます。
		String str = GourmetCareeUtil.convertLink(target, REG_URL, TAG_A, map);
		str = GourmetCareeUtil.convertLink(str, REG_MAIL, TAG_MAILTO, map);
		// dummyからリンクに変換します。
		return GourmetCareeUtil.replaceLinkTag(str, map);
	}

	/**
	 * Timestampを数値化し、基数を32に変更したものを返す。
	 * Timestampに対して一意となる。
	 * @param insertDatetime
	 * @return
	 */
	public static String createUniqueKey(Timestamp timestamp) {

		final int radix = 32;
		return Long.toString(timestamp.getTime(), radix);
	}


	/**
	 * 公開側で使用しているユニークキーと同じもの
	 *
	 * @param webId
	 * @param materialKbn
	 * @return
	 */
	public static String createUniqueKeyForPublic(int... values) {
		String str = "";
		for (int value : values) {
			str = str + "_" + value;
		}

		return DigestUtils.md5Hex(str + "_" + SYSTEM_HASH_SOLT);
	}

	/**
	 * 画面表示用にパスワードを'*'に変換
	 */
	public static String convertPassword(String password) {

		// パスワードを'*'に変換する
		Pattern pat = Pattern.compile(GourmetCareeConstants.MASK_ALPHANUM, Pattern.CASE_INSENSITIVE);
		Matcher match = pat.matcher(password);
		return match.replaceAll(GourmetCareeConstants.MASK_FREE_CHAR);
	}

	/**
	 * 文字列が空かどうかを判別し、空の場合は初期値を返す。
	 * @param str 判別する文字列
	 * @param defaultStr 空の場合に返却する文字列
	 * @return 文字列が空で無い場合、strを返却。文字列が空の場合、defaultStrを返却。
	 */
	public static String checkStringEmpty(String str, String defaultStr) {
		return  StringUtil.isEmpty(str) ? defaultStr : str;
	}

	/**
	 * 文字列がnullかどうかを判別し、nullの場合は初期値を返す。
	 * @param str 判別する文字列
	 * @param defaultStr 空の場合に返却する文字列
	 * @return 文字列がnullで無い場合、strを返却。文字列がnullの場合、defaultStrを返却。
	 */
	public static String checkStringNull(String str, String defaultStr) {
		return str == null ? defaultStr : str;
	}
	/**
	 * 3つ区切りで入力する電話番号をチェックする<br />
	 * ・値が無い場合はチェックしない<br />
	 * ・値がすべて数値かどうか<br />
	 * ・1つ入力されている場合は、他の2つも入力されているかチェック
	 * @param phoneNo1
	 * @param phoneNo2
	 * @param phoneNo3
	 * @return 電話番号が全て数値で入力されている場合はtrue、それ以外はfalse
	 */
	public static boolean checkPhoneNo(String phoneNo1, String phoneNo2, String phoneNo3) {

		// 電話に入力があるかチェック
		if (StringUtils.isBlank(phoneNo1) && StringUtils.isBlank(phoneNo2) && StringUtils.isBlank(phoneNo3)) {
			return true;
		}
		// 3つすべてに値が入力されているかチェック
		if (StringUtils.isBlank(phoneNo1) || StringUtils.isBlank(phoneNo2) || StringUtils.isBlank(phoneNo3)) {
			return false;
		}
		// 3つすべて数値かどうかチェック
		if (!StringUtils.isNumeric(phoneNo1) || !StringUtils.isNumeric(phoneNo2) || !StringUtils.isNumeric(phoneNo3)) {
			return false;
		}

		return true;
 	}



	/**
	 * 電話番号を1つのテキストで入力する場合の電話番号チェック
	 * 電話番号形式は、定数「REG_TEL」参照
	 * @param phoneNo 電話番号
	 * @return 電話番号でない、あるいは空の場合にfalse。電話番号形式の場合にtrue
	 */
	public static boolean checkPhoneNo(String phoneNo) {
		if (StringUtils.isBlank(phoneNo)) {
			return false;
		}

		Matcher matcher = Pattern.compile(REG_TEL).matcher(phoneNo);

		return matcher.matches();
	}

	/**
	 * 生年月日から年齢へ変換して返す
	 * @param birthDate 生年月日
	 * @return 年齢
	 */
	public static int convertToAge(Date birthDate) {

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_NONSLASH);

		return (int) ((NumberUtils.toInt(sdf.format(now)) - NumberUtils.toInt(sdf.format(birthDate))) / 10000);
	}

	/**
	 * 日付が正しい日付かチェックする
	 * @param yearStr 年文字列
	 * @param monthStr 月文字列
	 * @param dayStr 日文字列
	 * @return 日付が正しい場合、trueを返す
	 */
	public static boolean checkDate(String yearStr, String monthStr, String dateStr) {

		try {
			int year = NumberUtils.toInt(yearStr);
			int month = NumberUtils.toInt(monthStr) - 1;
			int date = NumberUtils.toInt(dateStr);

			Calendar cal = Calendar.getInstance();
			cal.setLenient(false);
			cal.set(year, month, date);
			cal.getTime();
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

	/**
	 * 文字列のリストからカンマ区切りの文字列を生成して返す
	 * @param strList 文字列リスト
	 * @return カンマ区切り文字列
	 */
	public static String createDelimiterStr(List<String> strList) {

		if (strList == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strList.size(); i++) {

			if (i == 0) {
				sb.append(strList.get(i));
			} else {
				sb.append(",");
				sb.append(strList.get(i));
			}
		}

		return sb.toString();
	}

	/**
	 * 文字列のリストからカンマ＋スペース区切りの文字列を生成して返す
	 * @param strList 文字列リスト
	 * @return カンマ区切り文字列
	 */
	public static String createKanmaSpaceStr(List<String> strList) {

		if (strList == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strList.size(); i++) {

			if (i == 0) {
				sb.append(strList.get(i));
			} else {
				sb.append(", ");
				sb.append(strList.get(i));
			}
		}

		return sb.toString();
	}

	/**
	 * Integerのリストからカンマ＋スペース区切りの文字列を生成して返す。
	 * 値がnullの場合は、ブランクにする。
	 * @param integerList Integerリスト
	 * @return カンマ区切り文字列
	 */
	public static String createCommaSpace(List<Integer> integerList) {

		if (integerList == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < integerList.size(); i++) {

			// nullの場合はブランクをセットする
			if (i == 0) {
				sb.append(integerList.get(i) == null ? "" : integerList.get(i));
			} else {
				sb.append(", ");
				sb.append(integerList.get(i) == null ? "" : integerList.get(i));
			}
		}
		return sb.toString();
	}

	/**
	 * Stringの配列をintの配列に変換する
	 * @param strArray
	 * @return
	 * @throws NumberFormatException
	 */
	public static int[] toIntArray(String[] strArray) throws NumberFormatException {
		return ArrayUtils.toPrimitive(toIntegerArray(strArray));
	}

	/**
	 * Stringの配列をintの配列に変換する
	 * strArrayがない場合は、空配列を返す
	 * @param strArray
	 * @return
	 */
	public static int[] toIntDefaultArray(String[] strArray) {
		if (ArrayUtils.isEmpty(strArray)) {
			return new int[0];
		}

		try {
			return ArrayUtils.toPrimitive(toIntegerArray(strArray));
		} catch (NumberFormatException e) {
			return new int[0];
		}
	}

	/**
	 * integer配列をint[]に変換する
	 * @param integerArray
	 * @return
	 */
	public static Integer[] toIntegerArray(Integer... integerArray) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer intVal : integerArray) {
			if (intVal != null) {
				list.add(intVal);
			}
		}

		return list.toArray(new Integer[0]);
	}

	/**
	 * Stringの配列をIntegerの配列に変換する。
	 * NULLや空の配列の場合は空の配列に変換します。
	 * @param strArray Stringの配列
	 * @return 変換されたIntegerの配列
	 * @throws NumberFormatException intに変換できない場合はエラー
	 */
	public static Integer[] toIntegerArray(String[] strArray) throws NumberFormatException {

		if (ArrayUtils.isEmpty(strArray)) {
			return new Integer[0];
		}

		Integer[] intArray = new Integer[strArray.length];

		// intの配列に変換
		for (int i = 0; i < strArray.length; i++) {
			intArray[i] = Integer.parseInt(strArray[i]);
		}

		return intArray;
	}

	/**
	 * intの配列をStringの配列に変換する。
	 * NULLや空の配列の場合は空の配列に変換します。
	 * @param intArray intの配列
	 * @return 変換されたStringの配列
	 */
	public static String[] toIntToStringArray(int[] intArray) {

		if (ArrayUtils.isEmpty(intArray)) {
			return new String[0];
		}

		String[] strArray = new String[intArray.length];

		// Stringの配列に変換
		for (int i = 0; i < intArray.length; i++) {
			strArray[i] = Integer.toString(intArray[i]);
		}

		return strArray;
	}

	/**
	 * StringのリストをIntegerのリストに変換します。<br />
	 * リストがNULLや空の場合は空のリストを返します。
	 * @param strList Stringのリスト
	 * @return 変換されたIntegerのリスト
	 * @throws NumberFormatException 数値に変換できない場合はエラー
	 */
	public static List<Integer> toIntegerList(List<String> strList) throws NumberFormatException {

		if (strList == null || strList.isEmpty()) {
			return new ArrayList<Integer>();
		}

		List<Integer> intList = new ArrayList<Integer>(strList.size());

		for (String str : strList) {
			intList.add(Integer.parseInt(str));
		}

		return intList;
	}


	/**
	 * int配列を Integerのセットに変換します。
	 * @param intArray
	 * @return
	 */
	public static Set<Integer> toIntegerSet(int[] intArray) {
		if (ArrayUtils.isEmpty(intArray)) {
			return new HashSet<Integer>();
		}

		Set<Integer> set = new HashSet<Integer>();
		for (int intValue : intArray) {
			set.add(intValue);
		}

		return set;
	}


	/**
	 * IntegerのリストをStringのリストに変換します。<br />
	 * リストがNULLや空の場合は空のリストを返します。<br />
	 * リストの値がnullの場合はブランクに変換します。
	 * @param intList Integerのリスト
	 * @return 変換されたStringのリスト
	 */
	public static List<String> toStringList(List<Integer> intList) {

		if (intList == null || intList.isEmpty()) {
			return new ArrayList<String>();
		}

		List<String> strList = new ArrayList<String>(intList.size());

		for (int i = 0; i < intList.size(); i++) {
			strList.add(intList.get(i) == null ? "" : String.valueOf(intList.get(i)));
		}

		return strList;
	}



	/**
	 * int型とオブジェクトを比較します。
	 * オブジェクトが示す数値としての値がint型が示す値と一致すればtrueを返します。
	 * 主にintとIntegerの比較で使用されることを想定しています。
	 * @param intNum
	 * @param obj
	 * @return
	 */
	public static boolean eqInt(int intNum, Object obj) {

		int target = 0;

		if (obj == null) {
			return false;
		} else {
			try {
				target = Integer.parseInt(String.valueOf(obj));
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return (intNum == target) ? true : false;
	}

	/**
	 * String型とオブジェクトを比較します。
	 * String.equals() をする前のnullチェックを省くことを想定しています。
	 *
	 * @param str
	 * @param obj
	 * @return
	 */
	public static boolean eqString(String str, Object obj) {
		if (str == null || obj == null) {
			return false;
		}

		return str.equals(String.valueOf(obj));
	}

	/***
	 * リストの現在のサイズを返します。Nullであれば0を返します。
	 * ArrayListのinitialCapacityのセットなどに使用してください。
	 * @param argList
	 * @return
	 */
	public static int getArrayInitialCapacity(List<?> argList) {
		return (argList != null) ? argList.size() : 0;
	}

	/**
	 * 半角かどうか
	 * @param str 文字列
	 * @return 半角の場合にtrue
	 */
	public static boolean isHankakuStr(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}

		return Pattern.compile(GourmetCareeConstants.MASK_SINGLE_ALPHANUM).matcher(str).matches();
	}

	/**
	 * 半角数字かどうか
	 * @param str 文字列
	 * @return 半角数字の場合にtrue
	 */
	public static boolean isHankakuNumber(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}

		Matcher matcher = Pattern.compile("[0-9]+").matcher(str);
		return matcher.matches();
	}

	/**
	 * StringとString[]を重複なしでマージします。
	 * どちらも空の場合は空の配列を返します。
	 * @param strArg
	 * @param arrayArg
	 * @return
	 */
	public static String[] mergeStringAndArray(String strArg , String[] arrayArg) {
		Set<String> retSet = new HashSet<String>();

		if (isNotBlank(strArg)) {
			retSet.add(strArg);
		}

		if (!ArrayUtil.isEmpty(arrayArg)) {
			for (String tmpStr : arrayArg) {
				if (isNotBlank(tmpStr)) {
					retSet.add(tmpStr);
				}
			}
		}

		if (!retSet.isEmpty()) {
			return retSet.toArray(new String[0]);
		} else {
			return new String[0];
		}
	}

	/**
	 * 文字列をハイフン付きの郵便番号文字列に変換します。
	 * 既にハイフン付きである場合は加工せずに値を返します。
	 * ブランクの場合や不正な形式の場合は空文字を返します。
	 * @param zipCd
	 * @return
	 */
	public static String createHyphenZipCd(String zipCd) {

		if (StringUtil.isBlank(zipCd)) {
			return "";
		} else if (WztValidateUtil.isZipcode(zipCd)) {
			return zipCd;
		} else if(WztValidateUtil.isZipcodeIgnoreHyphen(zipCd)) {

			//ハイフンなしの郵便番号をハイフン付きに変換
			StringBuilder sb = new StringBuilder(zipCd.substring(0, 3));
			sb.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			sb.append(zipCd.substring(3, 7));
			return sb.toString();
		}

		return "";
	}

	/**
	 * StringをIntegerに変換します。
	 * 変換できない場合はnullを返します。
	 * @param value
	 * @return
	 */
	public static Integer convertStringToInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}


	/**
	 * String配列をIntegerリストに変換します。
	 * @param array String配列
	 * @return Integerリスト
	 */
	public static List<Integer> convertStringArrayToIntegerList(String[] array) {
		if (ArrayUtils.isEmpty(array)) {
			return new ArrayList<Integer>(0);
		}
		List<Integer> list = new ArrayList<Integer>(0);
		for (String str : array) {
			list.add(NumberUtils.toInt(str));
		}
		return list;
	}

	/**
	 * 文字列をIntegerリストに変換
	 * @param str
	 * @return
	 */
	public static List<Integer> convertStringToIntegerList(String str) {

		if (NumberUtils.isDigits(str)) {
			List<Integer> list = new ArrayList<Integer>(1);
			list.add(Integer.parseInt(str));
			return list;
		}

		return new ArrayList<Integer>(0);
	}


	/**
	 * String配列をint配列に変換します。
	 * @param array String配列
	 * @return int配列
	 */
	public static int[] convertStringArrayToIntArray(String[] array) {
		if (ArrayUtils.isEmpty(array)) {
			return new int[0];
		}
		int[] retArray = new int[array.length];
		int index = 0;
		for (String str : array) {
			retArray[index++] = NumberUtils.toInt(str);
		}
		return retArray;
	}

	/**
	 * 半角数字に変換します。
	 * @param value
	 * @return
	 */
	public static String convertToHankakuNumber(String value) {
		 if (StringUtils.isBlank(value)){
			return "";
		}
		StringBuilder sb = new StringBuilder(value);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if ('０' <= c && c <= '９') {
				sb.setCharAt(i, (char) (c - '０' + '0'));
			}
		}
		return sb.toString();
	}



	/**
	 * 改行文字をスペースにリプレース
	 * @param src 原文
	 */
	public static String replaceCrlfToSpace(String src) {
		return replaceCrlf(src, " ");
	}

	/**
	 * 改行文字をリプレース
	 * @param src 原文
	 * @param replace 改行をリプレースする文字
	 */
	public static String replaceCrlf(String src, String replace) {
		if (src == null) {
			return "";
		}

		return src.replaceAll("[\\r\\n]", replace);
	}

	/**
	 * モバイルの個体識別番号を取得します。
	 * 取得できない場合はブランクを返します。<br />
	 * Docomo, au , softbankの3キャリアのみ対応とする。
	 * 各キャリアの個別情報はヘッダの記載箇所が重複しない。
	 * @param request
	 * @return
	 */
	public static String getMopbileFixedIdentifiedCd(HttpServletRequest request) {

		String retStr = "";

		retStr = request.getHeader("X-DCMGUID");

		//Docomoの場合
		if (StringUtil.isNotBlank(retStr)) {
			return retStr;
		}

		//auの場合
		retStr = request.getHeader("X-UP-SUBNO");
		if (StringUtil.isNotBlank(retStr)) {
			return retStr;
		}

		//Softbank
		retStr = request.getHeader("X-JPHONE-UID");
		if (StringUtil.isNotBlank(retStr)) {
			return retStr;
		}

		return "";
	}

	/**
	 * キャリア名を文字列で取得します。
	 * @param agent
	 * @return
	 */
	public static String convertCareer(NetUserAgent agent) {

		MobileUserAgent mobileAgent = null;

		//モバイルのユーザエージェントのみ処理する
		if (agent instanceof MobileUserAgent) {
			mobileAgent = (MobileUserAgent) agent;
		} else {
			return "";
		}


		if (mobileAgent.isDocomo()) {
			return "Docomo";
		} else if (mobileAgent.isEzWeb()) {
			return "au";
		} else if (mobileAgent.isSoftbank()) {
			return "Softbank";
		} else {
			return "";
		}
	}

	/**
	 * 文字列をトリムします。<br />
	 * " aaa " return "aaa"<br />
	 * "aaa　　" return "aaa"<br />
	 * "aaa 　 " return "aaa"<br />
	 * "aaa 　 　 " return "aaa 　"<br />
	 * null return null
	 * @param str
	 * @return トリムした文字列
	 */
	public static String trimSpace(String str) {

		if(StringUtil.isNotEmpty(str)) {
			str = str.trim();
			str = StringUtil.rtrim(str, "　");
			str = str.trim();
		}
		return str;
	}

	/**
	 * 文字列から空白(半角全角スペース)をトリム(削除)します。
	 * @param str
	 * @return トリムした文字列
	 */
	public static String superTrim(String str) {

		if(StringUtil.isNotEmpty(str)) {
			// 半角スペース、または全角スペースを削除します。
			str = StringUtil.rtrim(str, " |　");
			str = StringUtil.ltrim(str, " |　");
		}
		return str;
	}

	/**
	 * 文字列の末尾直前が右記の場合は取り除きます。(半角スペース・全角スペース・改行)
	 * @param str
	 * @return トリムした文字列
	 */
	public static String trimSuffixLineBreak(String str) {

		if(StringUtil.isNotEmpty(str)) {
			// 文字列末尾の直前が右記の場合は削除します。(半角スペース・全角スペース・改行)
			Pattern pattern = Pattern.compile("[ 　\n\r]+$");
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				str = matcher.replaceAll("");
			}
		}
		return str;
	}

	/**
	 * すべてのスペース(半角・全角)を取り除きます。
	 * @param str
	 * @return
	 */
	public static String removeAllSpace(String str) {
		if (str == null) {
			return "";
		}
		return str.replaceAll("( |　)", "");
	}

	/**
	 * 市区町村までの住所に変換します。
	 * ○○市○○町XX-YY → ○○市
	 * @param address
	 * @return
	 */
	public static String toMunicipality(String address) {
		if (StringUtils.isBlank(address)) {
			return "";
		}

		address = WztStringUtil.trim(address);

		String result = null;

		// 市区町村名に「市」「町」「村」が含まれているものをチェック
		for (String municipalityName : MUNICIPALITY_LIST) {
			if (address.startsWith(municipalityName)) {
				result = municipalityName;
				break;
			}
		}

		// 市区町村とそれ以降を分割
		if (result == null) {
			int index;

			if ((index = address.indexOf("市")) != -1
			|| ((index = address.indexOf("区")) != -1)
			|| ((index = address.indexOf("町")) != -1)
			|| ((index = address.indexOf("村")) != -1)) {
				result = address.substring(0, index + 1);
			} else {
				result = address;
			}
		}

		return result;
	}


	/**
	 * オブジェクトをintに変換します。
	 * @param obj intに変換する値
	 * @return オブジェクトをintに変換して返す。 オブジェクトがnullか、変換できなかった場合は0を返す
	 */
	public static int toObjectToInt(Object obj) {
		return toObjectToInt(obj, 0);
	}

	/**
	 * オブジェクトをintに変換します。
	 * @param obj intに変換する値
	 * @param defaultValue デフォルト値
	 * @return オブジェクトをintに変換して返す。 オブジェクトがnullか、変換できなかった場合はデフォルト値を返す
	 */
	public static int toObjectToInt(Object obj, int defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		return NumberUtils.toInt(String.valueOf(obj), defaultValue);
	}

	/**
     * オブジェクトをIntegerに変換します。
     * @param obj Integerに変換する値
     * @return オブジェクトをIntegerに変換した値を返す。 引数がNULLの場合はnullを返す
     */
    public static Integer objectToInteger(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Integer) {
            return (Integer) obj;
        }

        return Integer.parseInt(obj.toString());
    }


	/**
	 * カタカナ文字列かどうか
	 * @param str
	 * @return
	 */
	public static boolean isKatakanaStr(String str) {
		if (StringUtils.isBlank(str)) {
			return true;
		}

		String replacedStr = Pattern.compile(KATAKANA_REGEX).matcher(str).replaceAll("");
		return StringUtils.isBlank(replacedStr);
	}

	/**
	 * 事前登録の登録状況を会員区分リストに変換します。
	 * @param statusKbnArray
	 * @return
	 */
	public static List<Integer> convertAdvancedStatusKbnToMemberKbn(String[] statusKbnArray) {

		if (ArrayUtils.isEmpty(statusKbnArray)) {
			return new ArrayList<Integer>();
		}

		List<Integer> list = new ArrayList<Integer>();
		for (String statusKbn : statusKbnArray) {
			if (GourmetCareeUtil.eqInt(MTypeConstants.AdvancedRegistrationStatusKbn.NO_MEMBER, statusKbn)) {
				if (!list.contains(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER)) {
					list.add(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER);
				}
				continue;
			}

			if (GourmetCareeUtil.eqInt(MTypeConstants.AdvancedRegistrationStatusKbn.ADVANCED_MEMBER, statusKbn)) {
				for (Integer memberKbn : MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_GOURMETCAREE_MEMBER_LIST){
					if (!list.contains(memberKbn)) {
						list.add(memberKbn);
					}
				}
				continue;
			}

			if (GourmetCareeUtil.eqInt(MTypeConstants.AdvancedRegistrationStatusKbn.NO_ADVANCED_MEMBER, statusKbn)) {
				for (Integer memberKbn : MTypeConstants.MemberKbn.ONLY_GOURMETCAREE_MEMBER_LIST){
					if (!list.contains(memberKbn)) {
						list.add(memberKbn);
					}
				}
				continue;
			}
		}

		return list;
	}

	/**
	 * 会員区分を事前登録の登録状況に変換します。
	 * @param memberKbn
	 * @return
	 */
	public static int convertMemberKbnToAdvancedStatusKbn(Integer memberKbn) {
		if (GourmetCareeUtil.eqInt(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER,
				memberKbn)) {
			return MTypeConstants.AdvancedRegistrationStatusKbn.NO_MEMBER;
		}

		if (MTypeConstants.MemberKbn.ONLY_GOURMETCAREE_MEMBER_LIST.contains(memberKbn)) {
			return MTypeConstants.AdvancedRegistrationStatusKbn.NO_ADVANCED_MEMBER;
		}

		if (MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_GOURMETCAREE_MEMBER_LIST.contains(memberKbn)) {
			return MTypeConstants.AdvancedRegistrationStatusKbn.ADVANCED_MEMBER;
		}

		return MTypeConstants.AdvancedRegistrationStatusKbn.NO_MEMBER;
	}



	/**
	 * 整数の文字列かどうか
	 * @param str
	 * @return
	 */
	public static boolean isIntegerStr(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}

		return Pattern.compile(INTEGER_REGEX).matcher(str).matches();
	}


	/**
	 * Integerを文字列に変換します。
	 * @param value Integerの値
	 * @return 文字列
	 */
	public static String convertIntegerToString(Integer value) {
		if (value == null) {
			return "";
		}

		return String.valueOf(value);
	}


	/**
	 * カンマを除去します。
	 * @param str 文字列
	 * @return カンマを除去した文字列
	 */
	public static String removeKanma(String str) {
		if (str == null) {
			return "";
		}

		return str.replaceAll(GourmetCareeConstants.KANMA_STR, "");
	}



	/**
	 * 引数の年+1年を引いた日付を返す
	 * @param year 年
	 * @return 日付(Timestamp)
	 */
	public static Timestamp convertToTimestampDivYear(int year, int addYear) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -(year + addYear));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());

	}


	/**
	 * CSVヘッダをresponseに付与します。
	 * @param response レスポンス
	 * @param fileName ファイル名
	 */
	public static void setResponseCsvHeader(HttpServletResponse response, String fileName) {
		response.setHeader("Content-Disposition","attachment; filename=" + fileName);
	}



	/**
	 * マップが空かどうか
	 * @param map マップ
	 * @return マップがnullか、空の場合にtrue
	 */
	public static boolean isEmptyMap(Map<?, ?> map) {
		if (map == null) {
			return true;
		}

		return map.isEmpty();
	}


	/**
	 * マップが空でないかどうか
	 * @param map マップ
	 * @return マップに値があればtrue
	 */
	public static boolean isNotEmptyMap(Map<?, ?> map) {
		return isEmptyMap(map) == false;
	}

	/**
	 * 数値をStringに変更します。
	 * @param number 数値
	 * @return Stringに変換された数値。 numberがnullの場合はブランク
	 */
	public static String convertNumberToString(Number number) {
		if (number == null) {
			return "";
		}

		return String.valueOf(number);
	}


	/**
	 * 空文字列が存在するかどうか
	 * @param strings 文字配列
	 * @return 存在すればtrue
	 */
	public static boolean existBlankStr(String...strings) {
		if (ArrayUtils.isEmpty(strings)) {
			return true;
		}

		for (String str : strings) {
			if (StringUtils.isBlank(str)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Stringに変換します。
	 * ※ null の場合に、 nullっていう文字が帰ってこない対策のため実装。
	 * @param obj オブジェクト
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}

		return obj.toString();
	}

	/**
	 * 空のエレメントを除去します。
	 * @param array
	 * @return
	 */
	public static String[]removeBlankElement(String[] array) {
		if (ArrayUtil.isEmpty(array)) {
			return new String[0];
		}

		List<String> list = new ArrayList<String>();
		for (String str : array) {
			if (StringUtils.isNotBlank(str)) {
				list.add(str);
			}
		}
		return list.toArray(new String[0]);
	}


	/**
	 * コンテキストまでのURLを作成
	 * http://～～/ctx/ URL
	 * @return
	 */
	public static String createContextUrl() {
		HttpServletRequest request = RequestUtil.getRequest();
		Map<?, ?> requestScope = (Map<?, ?>) SingletonS2ContainerFactory.getContainer().getComponent("requestScope");
		StringBuffer sb = new StringBuffer(request.getScheme())
		.append("://")
		.append(request.getServerName());
		sb.append(requestScope.get("javax.servlet.forward.context_path"));
		return sb.toString();
	}

	/**
	 * 文字列中の電話番号をリストに変換します。
	 *
	 * @param targetText
	 * @return
	 */
	public static List<String> findTelNoToArrayList(String targetText) {

		List<String> retList = new ArrayList<String>();

		Pattern pat = Pattern.compile(REG_TEL);
		Matcher match = pat.matcher(targetText);

		while(match.find()) {

			String matchStr = match.group();

			if (StringUtils.isNotBlank(matchStr)) {
				retList.add(matchStr);
			}

		}

		return retList;
	}



	/**
	 * 秒を 分秒 の文字に変換します。
	 * 例 90 → 1 分 30 秒
	 */
	public static String convertSecondsToMinuteSecondStr(Integer seconds) {

		final String secondFormat = "%d 秒";
		if (seconds == null) {
			return String.format(secondFormat, 0);
		}

		final int oneMinute = 60;
		int minute = seconds / oneMinute;
		int sec = seconds % oneMinute;

		if (minute == 0) {
			return String.format(secondFormat, sec);
		}

		return String.format("%d 分 %s 秒", minute, sec);
	}


	/**
	 * IP電話番号を切り分けます。
	 *
	 * 05012345678 → 050-1234-5678
	 *
	 * @param ipPhone
	 * @return
	 */
	public static String devideIpPhoneNumber(String ipPhone) {
		if (StringUtils.isBlank(ipPhone)) {
			return ipPhone;
		}

		StringBuilder sb = new StringBuilder(10);
		sb.append(StringUtils.substring(ipPhone, 0, 3));
		sb.append("-");
		sb.append(StringUtils.substring(ipPhone, 3, 7));
		sb.append("-");
		sb.append(StringUtils.substring(ipPhone, 7));

		return sb.toString();
	}


	/**
	 * 文字列の中から、電話番号を正規表現で抽出します。
	 * @param str
	 * @return
	 */
	public static List<String> extractPhoneNumber(String str) {
		if (str == null) {
			return new ArrayList<String>(0);
		}

		List<String> telList = new ArrayList<String>();
		Pattern pattern = Pattern.compile(REG_TEL);
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			telList.add(matcher.group());
		}

		return telList;
	}


	/**
	 * Mapの値をJoinします。
	 * @param map MAP
	 * @param keyList Mapから取得するキーのリスト
	 */
	public static String joinMapValues(Map<?, ?> map, List<?> keyList) {
		return joinMapValues(map, keyList, ",");
	}

	/**
	 * Mapの値をJoinします。
	 * @param map MAP
	 * @param keyList Mapから取得するキーのリスト
	 * @param delimiter デリミタ
	 */
	public static String joinMapValues(Map<?, ?> map, List<?> keyList, String delimiter) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Object key : keyList) {
			if (index++ > 0) {
				sb.append(delimiter);
			}
			sb.append(map.get(key));
		}
		return sb.toString();
	}


	/**
	 * URLからGETパラメータをMapで取得
	 * @param url
	 * @return
	 */
	public static Map<String, String> getUrlParams(String url) {
		int questionIndex = url.indexOf('?');
		if (questionIndex < 0) {
			return new HashMap<String, String>();
		}

		String paramQuery = url.substring(questionIndex + 1);
		return parseUrlParam(paramQuery);

	}

	/**
	 * GETパラメータのように A=1&B=2 のようなパラメータをマップに変換
	 */
	public static Map<String, String> parseUrlParam(String paramQuery) {
		Map<String, String> map = new HashMap<String, String>();
		for (String param : paramQuery.split("&")) {
			String[] p = param.split("=");
			String key = "";
			String value = "";
			if (p.length > 0) {
				key = p[0];
			}

			if (p.length > 1) {
				value = p[1];
			}

			if (StringUtils.isNotBlank(key)) {
				map.put(key, value);
			}
		}
		return map;
	}


	/**
	 * オブジェクトの結合
	 * 複数の同一クラスのオブジェクトを結合し、リストに変換する。
	 * オブジェクトがnullの場合はリストから除外する。
	 * @param clazz クラス
	 * @param objects 結合するオブジェクト(仮想配列)
	 */
	public static <E> List<E> joinObjects(Class<E> clazz, E... objects) {
		if (objects == null) {
			return new ArrayList<E>();
		}
		List<E> list = new ArrayList<E>(Arrays.asList(objects));
		list.remove(null);
		return list;
	}


	/**
	 * http://domain/context/までを作成する。
	 * @return
	 */
	public static String createContextPath() {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Map<?, ?> requestScope = (Map<?, ?>) container.getComponent("requestScope");
		HttpServletRequest request = (HttpServletRequest) container.getComponent(HttpServletRequest.class);
		StringBuilder sb = new StringBuilder("");
		sb.append(request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append(requestScope.get("javax.servlet.forward.context_path"));
		return sb.toString();
	}


	/**
	 * 年月日をTimestampへ変換
	 */
	public static Timestamp convertBirthDayData(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());
	}



	public static String createRequestedPath() {
		Map<?, ?> requestScope = (Map<?, ?>) SingletonS2ContainerFactory.getContainer().getComponent("requestScope");
		HttpServletRequest request = RequestUtil.getRequest();
		StringBuilder sb = new StringBuilder("");
		sb.append(request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append(requestScope.get("javax.servlet.forward.context_path"));

		sb.append(requestScope.get("javax.servlet.forward.servlet_path"));
		if (requestScope.containsKey("javax.servlet.forward.query_string")) {
			sb.append("?");
			sb.append(requestScope.get("javax.servlet.forward.query_string"));
		}

		return sb.toString();
	}

	/**
	 * メールの送信日時をメール一覧用の文字列に変換する
	 *
	 * ①受信日が現在と同じ年の場合　→　月日 + 時間を表示する
	 * ②受信日の年が現在の年よりも前の場合　→　年月日 + 時間を表示する。
	 *
	 * @param sendDatetime
	 * @return
	 */
	public static String convertSendDateTimeForMailList(Date sendDatetime) {
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(sendDatetime);

		if(nowCal.get(Calendar.YEAR) != cal.get(Calendar.YEAR)) {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(sendDatetime);
		}

		return new SimpleDateFormat("MM/dd HH:mm").format(sendDatetime);
	}


	/**
	 * CloseableをIOExceptionをスローせずにクローズ
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable == null) {
			return;
		}

		try {
			closeable.close();
		} catch (IOException e) {
			// doNothing
		}
	}


	/**
	 * UTF-8を使って URLエンコードを行います。
	 * @param str エンコードする文字列
	 */
	public static String urlEncode(String str) {
		return urlEncode(str, GourmetCareeConstants.ENCODING);
	}

	/**
	 * URLエンコードを行う。
	 * {@link UnsupportedEncodingException} が発生した場合は、srcを変換せずにリターンする。
	 * @param src エンコードする文字列
	 * @param encode 文字コード
	 */
	public static String urlEncode(String src, String encode) {
		if(StringUtils.isEmpty(src)) {
			return "";
		}

		try {
			return URLEncoder.encode(src, encode);
		} catch (UnsupportedEncodingException e) {
			log.warn(String.format("%s のURLエンコードに失敗しました。エンコードしていない文字列を返却します。 エンコード：[%s]", src, encode),
					e);
			return src;
		}
	}
}


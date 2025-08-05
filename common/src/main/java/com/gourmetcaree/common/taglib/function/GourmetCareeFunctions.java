package com.gourmetcaree.common.taglib.function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.taglib.S2Functions;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.env.EnvDto;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.PropertiesUtil;
import com.gourmetcaree.common.util.UserAgentUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;

import jp.co.whizz_tech.commons.WztCp932Util;
import jp.co.whizz_tech.commons.WztKatakanaUtil;

/**
 * グルメキャリー用のファンクションです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class GourmetCareeFunctions {

	public static final String SUBJECT_CHAR_SET = "Shift_JIS";

	/** SSLのドメイン(https://www.gourmetcaree.jp など) */
	private static String sslDomain;

	/**
	 * 文字列に含まれるURLをリンクに変更します。
	 * target属性を含みません。
	 * @param value
	 * @return
	 */
	public static String editUrlLink(String value) {
		return editUrlLink(value, null);
	}

	/**
	 * 文字列に含まれるURLをリンクに変更します。
	 * 第2引数はtarget属性にセットされます。
	 * @param value
	 * @return
	 */
	public static String editUrlLink(String value, String elementStr) {

		String retStr = null;

		//URLをリンクに変換
		if (elementStr == null) {
			retStr = GourmetCareeUtil.makeUrlLink(value);
		} else {
			retStr = GourmetCareeUtil.makeUrlLinkWithTarget(value, elementStr);
		}

		return retStr;
	}

	/**
	 * 文字列に含まれるメールアドレスをリンクに変更します。
	 * subjectを含みません。
	 * @param value
	 * @return
	 */
	public static String editMailLink(String value) {
		return editMailLink(value, null);
	}

	/**
	 * 文字列に含まれるメールアドレスをリンクに変更します。
	 * 第2引数はsubjectにセットされます。
	 * @param value
	 * @return
	 */
	public static String editMailLink(String value, String subject) {

		String retStr = null;

		if (subject != null) {

			//引数の文字列をShift-JISにエンコードし、URLエンコード
			String encodedSubject = null;
			try {
				String encodeTempStr = new String(subject.getBytes(SUBJECT_CHAR_SET), SUBJECT_CHAR_SET);
				encodedSubject = URLEncoder.encode(encodeTempStr, SUBJECT_CHAR_SET);
			} catch (UnsupportedEncodingException e) {
				//エンコード失敗時は空とする。
				encodedSubject = "";
			}

			//subject付きのメールリンクを作成
			retStr = GourmetCareeUtil.makeMailLink(value, encodedSubject);
		} else {
			//subjectなしのメールリンクを作成
			retStr = GourmetCareeUtil.makeMailLink(value);
		}

		return retStr;
	}

	/**
	 * モバイル用に文字列に含まれるURL、メールアドレス、電話番号をリンクに変更します。
	 * @param value
	 * @return
	 */
	public static String editMobileLink(String value) {

		//URLをdummyに変換してからリンクに変更します。
		String retStr = GourmetCareeUtil.convertLinkAll(value);

		return retStr;
	}

	/**
	 * PC用に、含まれるURL・メールアドレスをリンクに変更します。
	 * @param value
	 * @return
	 */
	public static String editPcLink(String value) {
		return GourmetCareeUtil.convertPcLink(value);
	}

	/**
	 * モバイル用に文字列に含まれる電話番号をリンクに変更します。
	 * linkFlgがfalseの場合はtel:のリンク先ではなく、リンクを無効にします。
	 * @param value
	 * @return
	 */
	public static String editTelLink(String value, boolean linkFlg) {

		String retStr = null;

		if (linkFlg) {
			retStr = GourmetCareeUtil.makeTelLink(value);
		} else {
			retStr = GourmetCareeUtil.makeTelLinkForPreview(value);
		}

		return retStr;
	}


	/**
	 * モバイル用に文字列に含まれる電話番号をリンクに変更します。
	 * @param value 文字列
	 */
	public static String editTelConversionTag(String value) {
		return GourmetCareeUtil.editTelConversionTag(value);
	}

	/**
	 * 指定文字列中の最初に出現する電話番号フォーマットを返します。
	 *
	 * @param value 指定文字列
	 * @param removeHyphenFlg ハイフンを取り除いて返すフラグ
	 * @return
	 */
	public static String findTelFirstElement(String value, boolean removeHyphenFlg) {

		return GourmetCareeUtil.findTelFirstElement(value, removeHyphenFlg);
	}


	/**
	 * 改行を除去します。
	 *
	 * @param input
	 *入力値
	 * @return 変換した結果
	 */
	public static String removeCr(String input) {
		if (StringUtil.isEmpty(input)) {
			return "";
		}

		return input.replaceAll("\r\n", "")
							.replaceAll("\r", "")
							.replaceAll("\n", "");
	}

	/**
	 * 改行を除去して半角スペースに置き換えます。
	 *
	 * @param input
	 *入力値
	 * @return 変換した結果
	 */
	public static String removeCrToSpace(String input) {
		if (StringUtil.isEmpty(input)) {
			return "";
		}

		return input.replaceAll("\r\n", " ")
							.replaceAll("\r", " ")
							.replaceAll("\n", " ");
	}

	/**
	 * HTMLタグを除去します。
	 * @param input
	 * @return
	 */
	public static String removeHtmlTags(String input) {
		if (StringUtil.isBlank(input)) {
			return "";
		}

		return Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>").matcher(input).replaceAll("");

	}

	/**
	 * 改行タグ以外のHTMLタグを除去します。
	 * @param input
	 * @return
	 */
	public static String removeHtmlTagsWithoutBr(String input) {
		if (StringUtil.isBlank(input)) {
			return "";
		}

		String dummyBr = "dummyBr";
		Matcher brMatcher = Pattern.compile("<br[^>]*>", Pattern.CASE_INSENSITIVE).matcher(input);
		Matcher matcher = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>").matcher(brMatcher.replaceAll(dummyBr));

		return matcher.replaceAll("").replaceAll(dummyBr, "<br />");
	}




	/**
	 * BRタグを半角スペースに変換。
	 *
	 * @param input 対象の文字列
	 * @return 変換した結果
	 */
	public static String brToSpace(String input) {
		if (StringUtil.isEmpty(input)) {
			return "";
		}

		Pattern pattern = Pattern.compile("<br[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(input);
		return match.replaceAll(" ");
	}

	/**
	 * 携帯用に文字を変換します。(全角カタカナ→半角カタカナ、CP932→SJIS)
	 * @param target 文字列
	 * @return 変換文字列
	 */
	public static String m(String target) {
		return WztCp932Util.toJIS(WztKatakanaUtil.zenkakuToHankaku(target));
	}

	/**
	 * 奇数と偶数を判定します。<br />
	 * 値が数値で無い場合は、ブランクを返却します。
	 * @param target 数値
	 * @param oddValue 奇数の場合の返却文字
	 * @param evenValue 偶数の場合の返却文字
	 * @return 奇数の場は合第1引数、偶数の場合は第2引数
	 */
	public static String odd(String target, String oddValue, String evenValue) {

		try {

			return Integer.parseInt(target)%2==0 ? evenValue : oddValue;

		} catch (NumberFormatException e) {

			// 数字でない場合は、ブランクを返す。
			return "";
		}
	}

	/**
	 * 文字数が指定した数より多い場合は、それ以降の文字を指定の文字に置き換えます。<br />
	 * 指定する数が数値で無い場合は、そのまま返却します。
	 * @param target 対象の文字列
	 * @param length 指定数字
	 * @param target 置き換え文字
	 * @return 変換した文字列
	 */
	public static String replaceStr(String target, String lengthStr, String replaceStr) {

		try {

			int lengthNum = Integer.parseInt(lengthStr);

			if(target.length() > lengthNum) {
				target = target.substring(0, lengthNum) + replaceStr;
			}
			return target;

		} catch (NumberFormatException e) {

			// 数字でない場合は、値をそのまま返す。
			return target;
		}
	}

	/**
	 * 文字列がnullまたは空文字列ならtrueを返します。
	 * @param target 文字列
	 * @return 文字列がnullまたは空文字列ならtrue
	 */
	public static boolean isEmpty(String target) {
		return StringUtil.isEmpty(target);
	}

	/**
	 * 引数で指定されたキーが、Mapに存在する場合trueを返します。
	 * @param map チェックするMap
	 * @param key マップのキー
	 * @return Mapに存在する場合true
	 */
	public static boolean isMapExsists(Map<String, Object> map, String key) {
		if (map == null) {
			return false;
		}
		return map.containsKey(key);
	}

	/**
	 * 引数を文字列として連結して取得する
	 * @param obj
	 * @param obj2
	 * @return
	 */
	public static String concat(Object obj, Object obj2) {
		StringBuilder sb = new StringBuilder("");

		if (obj != null) {
			sb.append(obj.toString());
		}

		if (obj2 != null) {
			sb.append(obj2.toString());
		}
		return sb.toString();
	}

	/**
	 * 引数を文字列として連結して取得する
	 * @param obj
	 * @param obj2
	 * @param obj3
	 * @return
	 */
	public static String concat(Object obj, Object obj2, Object obj3) {
		return concat(obj, concat(obj2, obj3));
	}

	/**
	 * 引数を文字列として連結して取得する
	 * @param obj
	 * @param obj2
	 * @param obj3
	 * @param obj4
	 * @return
	 */
	public static String concat(Object obj, Object obj2, Object obj3, Object obj4) {
		return concat(concat(obj, obj2), concat(obj3, obj4));
	}

	/**
	 * 引数を文字列として連結して取得する
	 * @param obj
	 * @param obj2
	 * @param obj3
	 * @param obj4
	 * @param obj5
	 * @return
	 */
	public static String concat(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
		return concat(concat(concat(obj, obj2), concat(obj3, obj4)), obj5);
	}

	/**
	 * 引数を文字列としてスラッシュ区切りで連結して取得する
	 * @param path
	 * @param obj2
	 * @return
	 */
	public static String makePathConcat(Object path, Object obj2) {
		StringBuilder sb = new StringBuilder("");

		if (path != null) {
			sb.append(path.toString());
		}

		sb.append(GourmetCareeConstants.SLASH_STR);

		if (obj2 != null) {
			sb.append(obj2.toString());
		}
		return sb.toString();
	}

	/**
	 * 引数を文字列としてスラッシュ区切りで連結して取得する
	 * @param path
	 * @param obj2
	 * @param obj3
	 * @return
	 */
	public static String makePathConcat(Object path, Object obj2, Object obj3) {
		return makePathConcat(makePathConcat(path, obj2), obj3);
	}

	/**
	 * 引数を文字列としてスラッシュ区切りで連結して取得する
	 * @param path
	 * @param obj2
	 * @param obj3
	 * @param obj4
	 * @return
	 */
	public static String makePathConcat(Object path, Object obj2, Object obj3, Object obj4) {
		return makePathConcat(makePathConcat(path, obj2), makePathConcat(obj3, obj4));
	}

	/**
	 * 引数を文字列としてスラッシュ区切りで連結して取得する
	 * @param path
	 * @param obj2
	 * @param obj3
	 * @param obj4
	 * @param obj5
	 * @return
	 */
	public static String makePathConcat(Object path, Object obj2, Object obj3, Object obj4, Object obj5) {
		return makePathConcat(makePathConcat(path, obj2), makePathConcat(obj3, obj4, obj5));
	}

	/**
	 * 引数の二つの数を足し算して返します。
	 * @param municipality
	 * @return
	 */
	public static String addNumber(String number1, String number2) {
		int result = NumberUtils.toInt(number1) + NumberUtils.toInt(number2);
		return Integer.toString(result);
	}

	/**
	 * 住所を市区群で切ります。
	 * @param municipality
	 * @return
	 */
	public static String toMunicipality(String municipality) {
		return GourmetCareeUtil.toMunicipality(municipality);
	}

	/**
	 * link rel="canonical"用のパスを作成します。
	 * @param realPath ドメインの絶対パス (http://www.gourmetcaree.jp など)
	 * @param path ${f:url()} で作成したパス
	 * @return
	 */
	public static String createCanonicalUrl(String domain, String path) {
		StringBuilder canonicalUrl = new StringBuilder("");
		canonicalUrl.append("http://");
		canonicalUrl.append(domain);
		canonicalUrl.append(path.replaceAll(";jsessionid=[a-zA-Z0-9]{32}", ""));
		return canonicalUrl.toString();
	}

	/**
	 * javascriptの引数用にエスケープ(",')を行います。
	 * @param value 値
	 * @return javascriptの引数に渡せる用にエスケープされた文字列
	 */
	public static String escapeForJSArg(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}
		return S2Functions.br(value.replaceAll("('|\")", "\\\\$0"));
	}

	/**
	 * 置換元を置換文字で置換する
	 * <P>
	 *  置換対象は区分コードと区分地で判断します。
	 * </P>
	 * @param replaceTarget 置換元
	 * @param replacement replaceTargetの対象文字
	 * @param typeCd 区分コード
	 * @param typeValue 区分値
	 * @param replaceFormat 置換文字
	 * @return
	 */
	public static String replaceHtml(String replaceTarget, String replacement, String typeCd, Object typeValue, String replaceFormat) {

		// 区分コードがNULLまたはブランク
		if (replacement == null || "".equals(replacement) || typeCd == null || "".equals(typeCd) || typeValue == null) {
			return replaceTarget;
		}

		// 置換文字がNULLまたはブランク
		if (replaceFormat == null || "".equals(replaceFormat)) {
			return replaceTarget.replace(replacement, String.valueOf(typeValue));
		}


		// 業態の場合
		if (GourmetCareeUtil.eqString(typeCd, MTypeConstants.IndustryKbn.TYPE_CD)) {

			return stringReplace(replaceTarget, replacement, typeCd, String.valueOf(typeValue), replaceFormat, MTypeConstants.IndustryKbn.B_LIST);
		}

		return replaceTarget;
	}
	private static String stringReplace(String replaceTarget, String replacement, String typeCd, String typeValue, String replaceFormat, List<Integer> bList) {

		String valueStr = typeValue;

		for (int value : bList) {

			if (GourmetCareeUtil.eqInt(value, typeValue)) {

				valueStr = String.format(replaceFormat, typeValue);

				break;
			}
		}

		return replaceTarget.replace(replacement, valueStr);
	}



	/**
	 * エリアURLを作成します。
	 *
	 * @param path パス
	 * @return エリアが仙台の場合は、/sendai + path<br />
	 * それ以外は path
	 */
	public static final String createAreaUrl(String path) {
		EnvDto envDto = (EnvDto) SingletonS2ContainerFactory.getContainer().getComponent(EnvDto.class);
		return S2Functions.url(MAreaConstants.Prefix.getPrefixPath(envDto.getAreaCd()).concat(path));
	}

	/**
	 * 仙台が非表示かどうか
	 * @return
	 */
	public static final boolean isInvisibleArea() {
		Properties prop = ResourceUtil.getProperties("visibleSendai.properties");
		String path = prop.getProperty("cg.inVisible.sendai.path");
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String val = reader.readLine();
			return "1".equals(val);
		} catch (IOException e) {
			//
			return false;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
	}



	/**
	 * 運営管理用のコンテントパスを取得
	 */
	public static final String createAdminContentPath(String path) {
		return String.format("%s/adminContent%s", getSslDomain(), path);
	}


	/**
	 * 運営管理用のコンテントパスを取得
	 */
	public static final String createShopContentPath(String path) {
		return String.format("%s/shopContent%s", getSslDomain(), path);
	}

	/**
	 * SSLドメインの取得
	 */
	private static String getSslDomain() {
		if (StringUtils.isEmpty(sslDomain)) {
			Properties p = ResourceUtil.getProperties("gourmetcaree.properties");
			if (p == null) {
				sslDomain = "https://www.gourmetcaree.jp";
			} else {
				String domain = p.getProperty("gc.sslDomain");
				if (StringUtils.isBlank(domain)) {
					sslDomain = "https://www.gourmetcaree.jp";
				} else {
					sslDomain = domain;
				}
			}
		}
		return sslDomain;
	}

	/**
	 * セッションIDの取得
	 */
	public static String getSessionId() {
		HttpSession session = SingletonS2Container.getComponent(HttpSession.class);
		if (session == null) {
			return "";
		}

		return session.getId();
	}

	/**
	 * GoogleMapApiKey を取得
	 */
	public static String getGoogleMapApiKey(String key) {
		return PropertiesUtil.getGeneralProperty(key);
	}

	/**
	 * md5に変換
	 * @param value
	 * @return
	 */
	public static String md5(String value) {
		return StringUtil.isEmpty(value) ? "" : DigestUtils.md5Hex(value);
	}

	/**
	 * クリテオ用のtypeを取得
	 * @return
	 */
	public static String getCriteoType() {
		return UserAgentUtils.getCriteoType();
	}
}

package com.gourmetcaree.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Cookieに関するユーティリティクラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public final class CookieUtil {

	/**
	 * 指定したキーでクッキーに情報を保存します。
	 * 保持期間は日数で指定。
	 * @param request
	 * @param response
	 * @param cookiePath
	 * @param cookieKey
	 * @param keepValue
	 * @param limit
	 */
	public static void bakeCookie(HttpServletRequest request, HttpServletResponse response, String cookiePath,
										String cookieKey, String keepValue, int limit) {

		String allValue = "";
		try {
			// クッキーに保存する文字列をURLエンコードします
			allValue = URLEncoder.encode(keepValue , "Shift_JIS");
		} catch (UnsupportedEncodingException e) {
			//未処理
		}

		// クッキーに有効期間を設定し、クッキーを発行
		Cookie cookie = new Cookie(cookieKey , allValue);
		cookie.setMaxAge(60 * 60 * 24 * limit);
		cookie.setPath(cookiePath);

		response.addCookie(cookie);
	}

	/**
	 * Cookieからキーを元に値を取得します。
	 * @param request
	 * @param cookieKey
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieKey) {

		// 保存されているすべてのクッキーの配列を取得
		Cookie[] cookies = request.getCookies();

		// 目的のクッキーを保存するためのクッキーオブジェクト
		Cookie targetCookie = null;

		if (cookies!=null) {
			for (int i=0;i<cookies.length;i++) {
				if (cookies[i].getName().equals(cookieKey)) {
					// 該当するクッキーを取得します
					targetCookie = cookies[i];
				}
			}
		}

		String message = "";

		if (targetCookie != null) {
			// クッキーに設定されてる値をURLデコードします
			try {
				message = URLDecoder.decode(targetCookie.getValue(),"Shift_JIS");
			} catch (UnsupportedEncodingException e) {
			}
		}

		return message;
	}

	/**
	 * Cookieにグルメキャリーログイン情報を書き込みます。
	 * @param request
	 * @param response
	 * @param memberId
	 * @param uuid
	 * @param limit
	 */
	public static void writeLoginInfo(HttpServletRequest request, HttpServletResponse response,
			int memberId, String uuid, int limit) {

		//自動ログイン情報をCookieに書き込む。
		bakeCookie(request, response, "/", "memberId", Integer.toString(memberId) , limit);
		bakeCookie(request, response, "/", "autoLoginCd", uuid , limit);
	}

	/**
	 * Cookieにグルメキャリーログイン情報を書き込みます。
	 * @param request
	 * @param response
	 * @param advancedRegistrationUserId
	 * @param uuid
	 * @param limit
	 */
	public static void writeAdvancedLoginInfo(HttpServletRequest request, HttpServletResponse response,
			int advancedRegistrationId, String uuid, int limit) {

		//自動ログイン情報をCookieに書き込む。
		bakeCookie(request, response, "/", "advancedRegistrationId", Integer.toString(advancedRegistrationId) , limit);
		bakeCookie(request, response, "/", "advancedAutoLoginCd", uuid , limit);
	}

	/**
	 * Cookieから自動ログイン情報を削除します。
	 * 実際の処理は空で上書きしています。
	 * @param request
	 * @param response
	 */
	public static void deleteLoginInfo(HttpServletRequest request, HttpServletResponse response) {
		//自動ログイン情報を削除する
		bakeCookie(request, response, "/", "memberId", "" , -1);
		bakeCookie(request, response, "/", "autoLoginCd", "" , -1);
	}

	/**
	 * Cookieから自動ログイン情報を削除します。
	 * 実際の処理は空で上書きしています。
	 * @param request
	 * @param response
	 */
	public static void deleteAdvancedLoginInfo(HttpServletRequest request, HttpServletResponse response) {
		//自動ログイン情報を削除する
		bakeCookie(request, response, "/", "advancedRegistrationId", "" , -1);
		bakeCookie(request, response, "/", "advancedAutoLoginCd", "" , -1);
	}
}

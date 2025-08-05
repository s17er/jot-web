package com.gourmetcaree.common.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.seasar.struts.util.RequestUtil;


/**
 * ユーザエージェントを扱うユーティリティクラス
 *
 * @author Katsutoshi Hasegawa
 */
public class UserAgentUtils {

    /**
     * docomoのユーザエージェントを判断する正規表現
     */
    private static final String USER_AGENT_FOR_DOCOME = "DoCoMo";

    /**
     * KDDIのユーザエージェントを判断する正規表現
     */
    private static final String USER_AGENT_FOR_KDDI = "KDDI\\-";

    /**
     * SoftBankのユーザエージェントを判断する正規表現
     */
    private static final String USER_AGENT_FOR_SOFTBANK = "J\\-PHONE|Vodafone|MOT\\-[CV]980|SoftBank";

    /**
     * iPhoneのユーザエージェントを判断する正規表現
     */
    private static final String USER_AGENT_FOR_IPHONE = "iPhone|ipod|iPod|iPad";

    /**
     * androidのユーザエージェントを判断する正規表現
     */
    private static final String USER_AGENT_FOR_ANDROID = "Android";

    /**
     * windows phoneのユーザエージェントを判断する正規表現
     */
    private static final String USER_AGENT_FOR_WIN_PHONE = "Windows( |　)Phone";

    /**
     * BlackBerryのユーザエージェントを判断する正規表現
     */
    private static final String USER_AGENT_FOR_BLACKBERRY = "BlackBerry";


    private static final Pattern MOBILE_PATTERN = Pattern.compile("Mobile");

    private static final Pattern IPAD_PATTERN = Pattern.compile("iPad");

    /**
     * ユーザエージェントヘッダ
     */
    public static final String HEADER_USER_AGENT = "User-Agent";


    /**
     * 指定したユーザエージェントの種類を判別します。
     *
     * @param userAgent ユーザエージェント
     * @return UserAgentKbn ユーザエージェントに適応した区分
     */
    public static UserAgentKbn getUserAgentKbn(String userAgent) {

        // ユーザエージェントが空の場合は分類不能
        if (StringUtils.isBlank(userAgent)) {
            return UserAgentKbn.OTHER;
        }

        if (isDocomo(userAgent)) {
            return UserAgentKbn.DOCOMO;
        }
        if (isKddi(userAgent)) {
            return UserAgentKbn.KDDI;
        }
        if (isSoftbank(userAgent)) {
            return UserAgentKbn.SOFTBANK;
        }
        if (isIphone(userAgent)) {
            return UserAgentKbn.IPHONE;
        }
        if (isAndroid(userAgent)) {
            return UserAgentKbn.ANDROID;
        }
        return UserAgentKbn.OTHER;
    }

    /**
     * ユーザエージェントを取得
     *
     * @return ユーザエージェント
     */
    public static String getUserAgent() {
        HttpServletRequest request = RequestUtil.getRequest();
        if (request == null) {
            return "";
        }

        return request.getHeader("User-Agent");
    }

    /**
     * userAgentがスマホの場合にtrueを返します。
     */
    public static boolean isSmartPhone(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return false;
        }

        return isSmartPhone(userAgent);

    }

    /**
     * userAgentがスマホの場合にtrueを返します。
     */
    public static boolean isSmartPhone(String userAgent) {
        if (isAndroid(userAgent)
                || isBlackBerry(userAgent)
                || isWindowsPhone(userAgent)
                || isIphone(userAgent)) {
            return true;
        }

        return false;
    }


    /**
     * userAgentがスマホで、且つタブレットでないの場合にtrueを返します。
     */
    public static boolean isSmartPhoneAndNotTablet(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        return isSmartPhoneAndNotTablet(request.getHeader(HEADER_USER_AGENT));

    }

    /**
     * userAgentがスマホで、且つタブレットでないの場合にtrueを返します。
     */
    public static boolean isSmartPhoneAndNotTablet(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }

        return isSmartPhone(userAgent) && !isTablet(userAgent);

    }

    /**
     * 指定したユーザエージェントがdocomoか判別します。
     */
    public static boolean isDocomo(String userAgent) {
        if (Pattern.compile(USER_AGENT_FOR_DOCOME).matcher(userAgent).find()) {
            return true;
        }
        return false;
    }

    /**
     * 指定したユーザエージェントがKDDIか判別します。
     */
    public static boolean isKddi(String userAgent) {
        if (Pattern.compile(USER_AGENT_FOR_KDDI).matcher(userAgent).find()) {
            return true;
        }
        return false;
    }

    /**
     * 指定したユーザエージェントがSoftBankか判別します。
     */
    public static boolean isSoftbank(String userAgent) {
        if (Pattern.compile(USER_AGENT_FOR_SOFTBANK).matcher(userAgent).find()) {
            return true;
        }
        return false;
    }

    /**
     * 指定したユーザエージェントがiPhone/iPodか判別します。
     */
    public static boolean isIphone(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }

        if (Pattern.compile(USER_AGENT_FOR_IPHONE).matcher(userAgent).find()) {
            return true;
        }
        return false;
    }

    /**
     * 指定したユーザエージェントがAndroidか判別します。
     */
    public static boolean isAndroid(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }

        if (Pattern.compile(USER_AGENT_FOR_ANDROID).matcher(userAgent).find()) {
            return true;
        }
        return false;
    }

    /**
     * 指定したユーザエージェントがwindows phoneか判別します。
     */
    public static boolean isWindowsPhone(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }

        if (Pattern.compile(USER_AGENT_FOR_WIN_PHONE).matcher(userAgent).find()) {
            return true;
        }

        return false;
    }

    /**
     * 指定したユーザエージェントがBlackBerryか判別します。
     *
     * @param userAgent ユーザエージェント
     * @return ブラックベリーの場合にtrue
     */
    public static boolean isBlackBerry(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }

        if (Pattern.compile(USER_AGENT_FOR_BLACKBERRY).matcher(userAgent).find()) {
            return true;
        }

        return false;
    }


    /**
     * タブレットからのアクセスかどうか
     *
     * @param request リクエスト
     * @return ユーザエージェントがタブレットの場合true
     */
    public static boolean isTablet(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return isTablet(request.getHeader(HEADER_USER_AGENT));
    }

    /**
     * UAがタブレットかどうか
     *
     * @param userAgent ユーザエージェント
     * @return ユーザエージェントがタブレットの場合true
     */
    public static boolean isTablet(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }
        if (isAndroid(userAgent)) {
            return !MOBILE_PATTERN.matcher(userAgent).find();
        } else if (isIphone(userAgent)) {
            return IPAD_PATTERN.matcher(userAgent).find();
        }

        return false;
    }


    /**
     * PCかどうか
     *
     * @param request リクエスト
     * @return ユーザエージェントがPCの場合true
     */
    public static boolean isPc(HttpServletRequest request) {
        if (request == null) {
            return isPc("");
        }

        return isPc(request.getHeader(HEADER_USER_AGENT));

    }

    /**
     * PCかどうか
     *
     * @param userAgent ユーザエージェント
     * @return ユーザエージェントがPCの場合true
     */
    public static boolean isPc(String userAgent) {
        if (StringUtils.isEmpty(userAgent)) {
            return false;
        }

        if (isKddi(userAgent)
                || isDocomo(userAgent)
                || isSoftbank(userAgent)
                || isSmartPhone(userAgent)) {
            return false;
        }

        return true;
    }

    /**
     * ガラケーかどうか
     *
     * @param userAgent ユーザエージェント
     * @return ユーザエージェントがKDDI/DOCOMO/SOFTBANKの場合にtrue
     */
    public static boolean isMobile(String userAgent) {
        if (StringUtils.isEmpty(userAgent)) {
            return false;
        }
        return (isKddi(userAgent)
                || isDocomo(userAgent)
                || isSoftbank(userAgent));
    }

    /**
     * ユーザエージェント区分の定義です。
     *
     * @author Katsutoshi Hasegawa
     * @version 1.0
     */
    public enum UserAgentKbn {

        /**
         * docomo(スマートフォン以外)
         */
        DOCOMO(1),

        /**
         * KDDI(スマートフォン以外)
         */
        KDDI(2),

        /**
         * SoftBank(スマートフォン以外)
         */
        SOFTBANK(3),

        /**
         * iPhone iPadなど
         */
        IPHONE(4),

        /**
         * androidなど
         */
        ANDROID(5),

        /**
         * windows phone
         */
        WINDOWS_PHONE(6),

        /**
         * black berry
         */
        BLACKBERRY(7),

        /**
         * その他
         */
        OTHER(99);

        /**
         * 値
         */
        private int value;

        /**
         * このクラスのオブジェクトを構築します。
         *
         * @param value 値
         */
        private UserAgentKbn(int value) {
            this.value = value;
        }

        /**
         * 値を返します。
         *
         * @return 値
         */
        public int value() {
            return value;
        }
    }

    /**
     * クリテオタグのtypeを取得する
     *
     * @return ガラケー、スマホの場合はm、タブレットの場合はt、PCの場合はd
     */
    public static String getCriteoType() {
        String userAgent = getUserAgent();
        return (isMobile(userAgent) || isSmartPhone(userAgent) || isTablet(userAgent)) ? CriteoType.CRITEO_TYPE_MOBILE
                : CriteoType.CRITEO_TYPE_PC;
    }

    /**
     * クリテオタイプの定数定義
     */
    public class CriteoType {
        /**
         * モバイルタイプ
         */
        public static final String CRITEO_TYPE_MOBILE = "m";
        /**
         * PCタイプ
         */
        public static final String CRITEO_TYPE_PC = "d";
    }
}

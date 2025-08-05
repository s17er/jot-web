package com.gourmetcaree.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字列ユーティリティ
 */
public class GCStringUtils {

    /**
     * 正規表現の特殊文字パターン
     */
    private static final Pattern ESPECIALLY_PATTERN = Pattern.compile("([\\[\\]\\(\\)\\.\\\\\\?\\+\\^\\$\\|\\*\\{\\}])");

    /**
     * 正規表現のエスケープ
     *
     * @return 正規表現の特殊文字がエスケープされた文字列
     */
    public static String escapeRegex(String str) {
        if (str == null) {
            return "";
        }
        Matcher matcher = ESPECIALLY_PATTERN.matcher(str);
        String result = str;
        while (matcher.find()) {
            result = matcher.replaceAll("\\\\$1");
        }
        return result;
    }

}

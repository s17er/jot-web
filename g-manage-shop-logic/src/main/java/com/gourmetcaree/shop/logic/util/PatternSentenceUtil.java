package com.gourmetcaree.shop.logic.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class PatternSentenceUtil {


	public static final String replacePattern(String body, String target, String replaceValue) {

		if (StringUtils.isBlank(body)) {
			return "";
		}
		Matcher mather = Pattern.compile(target).matcher(body);
		if (mather.find()) {
			return mather.replaceAll(replaceValue);
		}

		return body;
	}

}

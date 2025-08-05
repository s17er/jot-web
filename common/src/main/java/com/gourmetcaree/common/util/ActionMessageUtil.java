package com.gourmetcaree.common.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;

import com.google.common.collect.Lists;

public class ActionMessageUtil {

	private ActionMessageUtil() {}


	/**
	 * {@link ActionMessages} にアクションメッセージを追加
	 * @param errors {@link ActionMessages}
	 * @param key application.propertiesのキー
	 * @param messageKeys {0}などの置換箇所に代入する文言のapplication.propertiesのキー
	 */
	public static void addActionMessage(ActionMessages errors, String key, String... messageKeys) {
		errors.add("errors", createActionMessage(key, messageKeys));
	}

	/**
	 * アクションメッセージの作成
	 * @param key application.propertiesのキー
	 * @param messageKeys {0}などの置換箇所に代入する文言のapplication.propertiesのキー
	 * @return アクションメッセージ
	 */
	public static ActionMessage createActionMessage(String key, String... messageKeys) {
		Object[] values = createResourceMessages(messageKeys);
		return new ActionMessage(key, values);
	}



	/**
	 * {@link ActionMessagesException} をスロー
	 * @param key application.propertiesのキー
	 * @param messageKeys {0}などの置換箇所に代入する文言のapplication.propertiesのキー
	 */
	public static void throwActionMessagesException(String key, String... messageKeys) {
		throw createActionMessageException(key, messageKeys);
	}


	/**
	 * {@link ActionMessagesException} を作成します
	 * @param key application.propertiesのキー
	 * @param messageKeys {0}などの置換箇所に代入する文言のapplication.propertiesのキー
	 */
	public static ActionMessagesException createActionMessageException(String key, String... messageKeys) {
		return new ActionMessagesException(key, createResourceMessages(messageKeys));
	}
	/**
	 * application.propertiesから要素を取得し、メッセージの配列を生成
	 * @param messageKeys application.propertiesのキー
	 */
	private static Object[] createResourceMessages(String... messageKeys) {
		if (ArrayUtils.isEmpty(messageKeys)) {
			return null;
		}

		List<Object> list = Lists.newArrayList();
		for (String k : messageKeys) {
			list.add(MessageResourcesUtil.getMessage(k));
		}
		return list.toArray();
	}


	/**
	 * リクエストにアクションメッセージを追加
	 * @param key application.propertiesのキー
	 * @param messageKeys {0}などの置換箇所に代入する文言のapplication.propertiesのキー
	 */
	public static void setActionMessageToRequest(String key, String... messageKeys) {
		ActionMessages errors = new ActionMessages();
		errors.add("errors", createActionMessage(key, messageKeys));
		setActionMessageToRequest(errors);
	}

	/**
	 * リクエストにアクションメッセージを追加
	 * @param errors アクションメッセージ
	 */
	public static void setActionMessageToRequest(ActionMessages errors) {
		setActionMessageToRequest(RequestUtil.getRequest(), errors);
	}

	/**
	 * リクエストにアクションメッセージを追加
	 * @param request リクエスト
	 * @param errors アクションメッセージ
	 */
	public static void setActionMessageToRequest(HttpServletRequest request, ActionMessages errors) {
		ActionMessagesUtil.addErrors(request, errors);
	}
}

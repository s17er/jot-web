package com.gourmetcaree.common.util;

import org.seasar.struts.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * リクエストに関するユーティリティ
 * Created by ZRX on 2017/06/07.
 */
public class RequestUtils {


    /**
     * 301リダイレクトのステータスコードを設定。
     * XXX このリダイレクトをした場合、Action等のreturnはURLなどではなく、nullを返すこと。
     * @param uri リダイレクト先URL(?redirect=true はつけない)
     */
    public static void setStatusCode301(String uri) {
        HttpServletResponse response = ResponseUtil.getResponse();
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", uri);
    }
}

package com.gourmetcaree.common.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gourmetcaree.common.constants.Constants;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.UserAgentUtils;
import com.gourmetcaree.valueobject.OriginalRequestInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 本来のリクエスト情報を設定するためのフィルタ。
 * SAStrutsではリクエスト自体がごっそりと置き換わってしまっているため、
 * このフィルタで元のリクエスト情報を取れるように設定する。
 * 追加時はdiconファイルではなくweb.xmlに記述すること。
 * ※ RequestDumpFilterより前にこのFilterを動かさないと効果がない。
 * Created by ZRX on 2017/06/06.
 */
public class OriginalRequestInfoFilter implements Filter {

    private static Logger log = Logger.getLogger(OriginalRequestInfoFilter.class);



    /** モバイル用のアプリかどうか */
    private boolean isMobile;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String a = filterConfig.getInitParameter("isMobile");
        try {
            this.isMobile = Boolean.valueOf(a);
        } catch (Exception e) {
            this.isMobile = false;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String url = createUrl(request.getRequestURL());
        final String query = request.getQueryString();
        request.setAttribute(OriginalRequestInfo.ATTRIBUTE_KEY,
                new OriginalRequestInfo(request.getRequestURL().toString(), url, query, createQueryMap(query, request), createRawQueryMap(query, request)));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


    /**
     * URLを生成する。
     * jsessionidが付加されている場合は取り除く。
     *
     * @param url リクエストから取得したURL
     * @return URL
     */
    private String createUrl(final CharSequence url) {
        if (url == null) {
            return "";
        }

        Matcher m = GourmetCareeConstants.JSESSION_PATTERN.matcher(url);
        if (m.find()) {
            return m.replaceAll("");
        }
        return url.toString();
    }

    /**
     * GETクエリからMapを生成
     *
     * @param query GETキークエリ
     * @return GETキークエリをMapに変換したもの
     */
    private Map<String, List<String>> createRawQueryMap(final String query, HttpServletRequest request) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> map = Maps.newLinkedHashMap();
        String[] params = query.split("&");
        for (String p : params) {
            String[] data = p.split("=");
            final String key = data[0];
            List<String> list = map.get(key);
            if (list == null) {
                list = Lists.newArrayList();
                map.put(key, list);
            }
            list.add(getValueFromSplittedRaw(data, request));
        }
        return map;
    }

    /**
     * GETクエリからMapを生成
     *
     * @param query GETキークエリ
     * @return GETキークエリをMapに変換したもの
     */
    private Map<String, List<String>> createQueryMap(final String query, HttpServletRequest request) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> map = Maps.newLinkedHashMap();
        String[] params = query.split("&");
        for (String p : params) {
            String[] data = p.split("=");
            final String key = data[0];
            List<String> list = map.get(key);
            if (list == null) {
                list = Lists.newArrayList();
                map.put(key, list);
            }
            list.add(getValueFromSplitted(data, request));
        }
        return map;
    }

    /**
     * key=value のものを、=でsplitした配列からvalueに当たる部分を取得
     * URLデコードは行わない。
     *
     * @param data =でsplitした配列
     * @return 値
     */
    private String getValueFromSplittedRaw(String[] data, HttpServletRequest request) {
        if (data.length <= 1) {
            return "";
        }
        return data[1];
    }
    /**
     * key=value のものを、=でsplitした配列からvalueに当たる部分を取得
     * URLデコードをする
     *
     * @param data =でsplitした配列
     * @return 値
     */
    private String getValueFromSplitted(String[] data, HttpServletRequest request) {
        if (data.length <= 1) {
            return "";
        }
        final String value = data[1];
        final String encoding;
        if (isMobile) {
            encoding = UserAgentUtils.isSmartPhone(request.getHeader(UserAgentUtils.HEADER_USER_AGENT)) ?
                    Constants.ENCODING:
            "SHIFT_JIS";
        } else {
            encoding = Constants.ENCODING;
        }
        try {
            return URLDecoder.decode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            log.warn(String.format("リクエストクエリの値変換に失敗しました。 値：[%s] encoding:[%s]",
                    value,
                    encoding));
            return value;
        }
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

package com.gourmetcaree.valueobject;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 本来のリクエスト情報を保持するデータ
 * Created by ZRX on 2017/06/06.
 */
@Data
public class OriginalRequestInfo implements Serializable {


    private static final Logger log = Logger.getLogger(OriginalRequestInfo.class);

    private static final long serialVersionUID = -2955762714327256836L;

    /**
     * request属性に追加するときのキー
     */
    public static final String ATTRIBUTE_KEY = OriginalRequestInfo.class.getName();

    /**
     * http://～からのURL
     * GETキーは含まない
     */
    private final String url;

    /**
     * jsessionidを取り除いたURL
     */
    private final String urlTrimmedSessionId;

    /**
     * GETキーのクエリ
     */
    private final String query;

    /**
     * GETキーのクエリマップ
     */
    private final Map<String, List<String>> queryMap;

    /**
     * 生のGETキークエリマップ(URLデコードをしてない方
     */
    private final Map<String, List<String>> rawQueryMap;

    public boolean isNotEndWithSlash() {
        return !urlTrimmedSessionId.endsWith("/");
    }

    /**
     * URLの最後に「/」を付けたURLを取得
     */
    public String getSlashedUrl() {
       StringBuilder sb = new StringBuilder(urlTrimmedSessionId)
                                .append("/");

        Matcher m = GourmetCareeConstants.JSESSION_PATTERN.matcher(url);
        if (m.find()) {
            sb.append(";jsessionid=")
                    .append(m.group(1));
        }

        if (StringUtils.isNotBlank(query)) {
            sb.append("?");
            sb.append(query);

        }



        return sb.toString();

    }

    /**
     * リクエストからインスタンスを取得
     * @return インスタンス
     */
    public static OriginalRequestInfo getInstance() {
        return getInstance(RequestUtil.getRequest());
    }

    /**
     * リクエストからインスタンスを取得
     * @param request リクエスト
     * @return インスタンス
     */
    public static OriginalRequestInfo getInstance(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object obj = request.getAttribute(ATTRIBUTE_KEY);
        if (obj == null || !(obj instanceof OriginalRequestInfo)) {
            return null;
        }

        return (OriginalRequestInfo) obj;
    }

}

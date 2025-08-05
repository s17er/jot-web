package com.gourmetcaree.common.util;

import com.google.common.net.MediaType;
import com.gourmetcaree.common.exception.InternalGourmetCareeSystemErrorException;
import net.arnx.jsonic.JSON;
import org.seasar.struts.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * レスポンスに関するユーティリティ
 */
public class ResponseUtils {

    /**
     * レスポンスの返り値としてJSONを書き込み
     * @param value レスポンスとして返すオブジェクト
     */
    public static void writeJson(Object value) {
        HttpServletResponse response = ResponseUtil.getResponse();
        response.setContentType(MediaType.JSON_UTF_8.toString());

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            JSON.encode(value, out);
        } catch (IOException e) {
            throw new InternalGourmetCareeSystemErrorException("JSONの書き出しに失敗しました。", e);
        } finally {
            IOUtil.close(out);
        }
    }
}

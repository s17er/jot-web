package com.gourmetcaree.valueobject;

import lombok.Data;
import org.seasar.framework.beans.util.Beans;

import javax.servlet.http.HttpSession;
import java.io.Serializable;


/**
 * ログインフォームを保持するデータオブジェクト
 * 公開側PC/モバイルの事前登録ログイン用
 */
@Data
public class AdvancedLoginFormData implements Serializable {


    private static final long serialVersionUID = -1380881742800979732L;

    private static final String SESSION_KEY = AdvancedLoginFormData.class.getName();

    /** ログインID */
    public String loginMailAddress;

    /** パスワード */
    public String password;

    /** 自動ログイン */
    public String autoLogin;

    /** 事前登録ID */
    public String advancedRegistrationId;

    private AdvancedLoginFormData() {
    }

    /**
     * セッションにログインデータを保存
     * @param session セッション
     * @param loginForm ログインフォーム
     */
    public static void saveToSession(HttpSession session, Object loginForm) {
        AdvancedLoginFormData data = new AdvancedLoginFormData();
        Beans.copy(loginForm, data).execute();

        session.setAttribute(SESSION_KEY, data);
    }

    /**
     * セッションからインスタンスを取得し、セッション上から破棄する。
     * @return ログインフォームデータ
     */
    public static AdvancedLoginFormData getInstanceAndRemoveFromSession(HttpSession session) {
        Object obj = session.getAttribute(SESSION_KEY);
        if (obj == null || !(obj instanceof AdvancedLoginFormData)) {
            return null;
        }

        try {
            return (AdvancedLoginFormData) obj;
        } finally {
            session.removeAttribute(SESSION_KEY);
        }
    }
}

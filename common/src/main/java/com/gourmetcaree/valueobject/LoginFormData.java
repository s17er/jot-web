package com.gourmetcaree.valueobject;

import lombok.Data;
import org.seasar.framework.beans.util.Beans;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * ログインフォームを保持するデータオブジェクト
 * 公開側PC/モバイルのログイン用
 */
@Data
public class LoginFormData implements Serializable {


    private static final long serialVersionUID = 131072178376816206L;

    private static final String SESSION_KEY = LoginFormData.class.getName();

    /** ログインID */
    private String loginMailAddress;

    /** パスワード */
    private String password;

    /** 自動ログイン */
    private String autoLogin;

    /** WebデータID（応募からの遷移用） */
    private String webId;

    /** 店舗見学区分 */
    private String observationKbn;

    /** GCWコード（応募からの遷移用） */
    private String gcwCd;

    /** 応募のどの画面から遷移したかを保持する区分 */
    private String applicationMethodKbn;


    private LoginFormData() {
    }

    /**
     * セッションにログインデータを保存
     * @param session セッション
     * @param loginForm ログインフォーム
     */
    public static void saveToSession(HttpSession session, Object loginForm) {
        LoginFormData data = new LoginFormData();
        Beans.copy(loginForm, data).execute();

        session.setAttribute(SESSION_KEY, data);
    }

    /**
     * セッションからインスタンスを取得し、セッション上から破棄する。
     * @return ログインフォームデータ
     */
    public static LoginFormData getInstanceAndRemoveFromSession(HttpSession session) {
        Object obj = session.getAttribute(SESSION_KEY);
        if (obj == null || !(obj instanceof LoginFormData)) {
            return null;
        }

        try {
            return (LoginFormData) obj;
        } finally {
            session.removeAttribute(SESSION_KEY);
        }
    }
}

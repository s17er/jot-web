package com.gourmetcaree.admin.pc.sys.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AjaxTimeoutException;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.SessionTimeoutException;


/**
 * アクションへのアクセス制御を処理するインターセプターです。
 *
 * @author Takahiro Ando
 * @version 1.0
 */
public class AuthorizationInterceptor extends AbstractInterceptor {

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = -6227795669757973057L;

    /*
     * (非 Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = getTargetClass(invocation);
        log.trace(targetClass);

        if (targetClass.isAnnotationPresent(ManageLoginRequired.class)) {
            S2Container container = SingletonS2ContainerFactory.getContainer();
            Object obj = container.getComponent("userDto");
            Class<?> clazz = container.getComponentDef("userDto").getComponentClass();
            String userAuthLevel = FieldUtil.getString(ClassUtil.getField(clazz, "authLevel"), obj);
            String userId = FieldUtil.getString(ClassUtil.getField(clazz, "userId"), obj);
            int dtoCd = FieldUtil.getInt(ClassUtil.getField(clazz, "ADMIN_DTO"), obj);

            //Ajaxでのセッションタイムアウトは別対応とする
            String actionClassName = targetClass.getName();
            if (StringUtils.isNotEmpty(actionClassName)) {

                if (actionClassName.startsWith("com.gourmetcaree.admin.pc.ajax.action.ajax.")) {
                    // ユーザオブジェクトが存在しないか、中のデータがnullの場合はセッションタイムアウトとする
                    if (obj == null || userId == null) {
                        throw new AjaxTimeoutException("Ajaxの画面へのリクエスト時にユーザオブジェクトが存在しないか、中のデータがnullです。");
                    }
                }
            }

            // ユーザオブジェクトが存在しないか、中のデータがnullの場合はセッションタイムアウトとする
            if (obj == null || userId == null) {
                throw new SessionTimeoutException("ユーザオブジェクトが存在しないか、中のデータがnullです。");
            }

            // ユーザDtoが該当システムのものか判別する
            if (GourmetCareeConstants.ADMIN_DTO != dtoCd) {
                throw new AuthorizationException("ユーザDTOが該当のシステムのものではありませんでした。");
            }

            // アノテーションのユーザ区分とユーザオブジェクトのユーザ区分が一致しない場合はアクセスエラー
            ManageLoginRequired anno
                    = (ManageLoginRequired) targetClass.getAnnotation(ManageLoginRequired.class);

            ManageAuthLevel[] authLevelArray = anno.authLevel();
            boolean authMatchFlg = false;

            for (ManageAuthLevel auth : authLevelArray) {
                //許可する権限レベルとログインユーザの権限レベルが一致する、またはNONEを含む場合はOKとする
                if (ManageAuthLevel.NONE.value().equals(auth.value())) {
                    authMatchFlg = true;
                    break;
                } else if (auth.value().equals(userAuthLevel)) {
                    authMatchFlg = true;
                    break;
                }
            }

            // 権限が無い場合はエラー
            if (!authMatchFlg) {
                throw new AuthorizationException("ユーザの権限ではこのリクエスト先は許可されていません。");
            }

        }

        return invocation.proceed();
    }
}

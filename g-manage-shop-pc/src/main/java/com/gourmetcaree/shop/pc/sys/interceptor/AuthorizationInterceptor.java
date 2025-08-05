package com.gourmetcaree.shop.pc.sys.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.SessionTimeoutException;
import org.seasar.struts.annotation.Execute;

import java.lang.reflect.Method;

/**
 * アクションへのアクセス制御を処理するインターセプターです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AuthorizationInterceptor extends AbstractInterceptor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6227795669757973057L;

    private Logger logger = Logger.getLogger(this.getClass());

	/*
	 * (非 Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = getTargetClass(invocation);

        if (logger.isDebugEnabled()) {
            Method method = invocation.getMethod();
            if (method.isAnnotationPresent(Execute.class)) {
                logger.debug(String.format("actionClass:[%s] method:[%s]",
                        targetClass.getName(),
                        method.getName()));
            }
        }
		if (targetClass.isAnnotationPresent(ManageLoginRequired.class)) {
			S2Container container = SingletonS2ContainerFactory.getContainer();
			Object obj = container.getComponent("userDto");
			Class<?> clazz = container.getComponentDef("userDto").getComponentClass();
			String userId = FieldUtil.getString(ClassUtil.getField(clazz, "userId"), obj);
			int dtoCd = FieldUtil.getInt(ClassUtil.getField(clazz, "SHOP_DTO"), obj);

			// ユーザオブジェクトが存在しないか、中のデータがnullの場合はセッションタイムアウトとする
			if (obj == null || userId == null) {
				throw new SessionTimeoutException();
			}

			// ユーザDtoが該当システムのものか判別する
			if (GourmetCareeConstants.SHOP_DTO != dtoCd) {
				throw new AuthorizationException();
			}
		}

		return invocation.proceed();
	}
}

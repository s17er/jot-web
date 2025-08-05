package com.gourmetcaree.admin.pc.sys.interceptor;

import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.InternalGourmetCareeSystemErrorException;


/**
 * Executeアノテーションのついたメソッドのアクセス制御を処理するインターセプターです。
 * @author Takahiro Ando
 *
 */
public class MethodRoleInterceptor extends AbstractInterceptor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3988455120707929271L;

	/** ログイン画面へのパス */
	private static final String LOGIN_PAGE = "/login/login/";

	/*
	 * (非 Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
        return (!isExecuteMethod(invocation)
        		|| !hasMethodAccessAnnotation(invocation)
        		|| hasPermission(invocation))
        			? invocation.proceed() : LOGIN_PAGE;
    }

	/**
	 * MethodAccessアノテーションのついたメソッドかどうかを返します。
	 * @param invocation The method invocation joinpoint.
	 * @return MethodAccessアノテーションが付いている場合はtrue : 付いていない場合はfalse
	 */
	private boolean hasMethodAccessAnnotation(MethodInvocation invocation) {
		return invocation.getMethod().isAnnotationPresent(MethodAccess.class);
	}

	/**
	 * Executeアノテーションのついたメソッドかどうかを返します。
	 * @param invocation The method invocation joinpoint
	 * @return Executeアノテーションが付いている場合はtrue : 付いていない場合はfalse
	 */
    private boolean isExecuteMethod(MethodInvocation invocation) {
        return invocation.getMethod().isAnnotationPresent(Execute.class);
    }

    /**
     * ログインユーザがこのメソッドにアクセスする権限を持っているかを返します。
     * @param invocation The method invocation joinpoint
     * @return 権限がある場合はtrue: 権限がない場合はfalse
     */
	private boolean hasPermission(MethodInvocation invocation) {

    	String authLevel = getUserAuthLevel();

		//ロール別の権限SETを取得
    	Set<?> set = getRoleSet(authLevel);
    	MethodAccess ano = invocation.getMethod().getAnnotation(MethodAccess.class);

		//権限がなければアクセスを許可しない
    	if (!set.contains(ano.accessCode())) {
    		throw new AuthorizationException("メソッドにアクセスする権限がありません。：" + ano.accessCode());
    	}

    	return true;
    }

    /**
     * ログインユーザのロールを取得します。
     * @return ロール
     */
	private String getUserAuthLevel() {

		Object obj = null;
    	Class<?> clazz = null;
    	S2Container container = SingletonS2ContainerFactory.getContainer();

    	if (!container.hasComponentDef("userDto")) {
    		throw new AuthorizationException("userDtoコンポーネントの取得に失敗しました。");
    	}

		obj = container.getComponent("userDto");

        if (obj == null) {
    		throw new AuthorizationException("userDtoがnullでした。");
        }

		clazz = container.getComponentDef("userDto").getComponentClass();
		return FieldUtil.getString(ClassUtil.getField(clazz, "authLevel"), obj);
	}

    /**
     * ロールが持つ権限のSetを取得します。
     * @param role ロール
     * @return ロールが持つ権限Set
     */
    private Set<?> getRoleSet(String role) {

    	Map<String, Object> applicationScope = SingletonS2Container.getComponent("applicationScope");
        Map<?, ?> map = (Map<?, ?>) applicationScope.get("authRole");

        if (map == null) {
    		throw new InternalGourmetCareeSystemErrorException("ロール情報がアプリケーションに保持されていません。");
    	}

		//ロール別の権限SETを取得
    	Set<?> set = (Set<?>)map.get(role);

    	if (set == null) {
    		throw new InternalGourmetCareeSystemErrorException("ロール別の権限SETがアプリケーションに保持されていません。");
    	}

    	return set;
    }
}

package com.gourmetcaree.shop.pc.sys.interceptor;

import javax.persistence.OptimisticLockException;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.ThrowsInterceptor;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.InconsistentDataException;
import com.gourmetcaree.common.exception.NoHandlingPermissionException;
import com.gourmetcaree.common.exception.OutOfDateException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.exception.RegisteredDataException;
import com.gourmetcaree.common.exception.SessionTimeoutException;


/**
 * 例外処理を行うインターセプターです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class GourmetCareeThrowsInterceptor extends ThrowsInterceptor {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(GourmetCareeThrowsInterceptor.class);

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8852685548433729896L;

	/**
	 * 楽観的ロック例外が発生した場合のハンドリング処理です。
	 * @param e 例外
	 * @param invocation
	 * @return 遷移先
	 */
	public void handleThrowable(OptimisticLockException e, MethodInvocation invocation) {
		throw new ActionMessagesException("errors.optimisticLockException");
	}

	/**
	 * セッションタイムアウト例外が発生した場合のハンドリング処理です。
	 * @param e 例外
	 * @param invocation
	 * @return 遷移先
	 */
	public String handleThrowable(SessionTimeoutException e, MethodInvocation invocation) {
		return "/error/sessionTimeout?redirect=true";
	}

	public String handleThrowable(AuthorizationException e, MethodInvocation invocation) {
		return "/error/authorizationError?redirect=true";
	}

	public String handleThrowable(NoHandlingPermissionException e, MethodInvocation invocation) {
		return "/error/noHandlingPermissionError?redirect=true";
	}

	public String handleThrowable(InconsistentDataException e, MethodInvocation invocation) {
		log.warn(e);
		return "/error/inconsistentDataError?redirect=true";
	}

	public String handleThrowable(FraudulentProcessException e, MethodInvocation invocation) {
		log.warn(e);
		return "/error/fraudulentProcessError?redirect=true";
	}

	public String handleThrowable(PageNotFoundException e, MethodInvocation invocation) {
		log.warn(e);
		return "/error/pageNotFoundError?redirect=true";
	}

	public String handleThrowable(OutOfDateException e, MethodInvocation invocation) {
		log.warn(e);
		return "/error/outOfDateError?redirect=true";
	}


	public String handleThrowable(RegisteredDataException e, MethodInvocation invocation) {
		log.warn(e);
		return "/error/registeredDataError?redirect=true";
	}
//	public String handleThrowable(MailNotFoundException e, MethodInvocation invocation) {
//		return "/error/mailNotFoundError?redirect=true";
//	}
}

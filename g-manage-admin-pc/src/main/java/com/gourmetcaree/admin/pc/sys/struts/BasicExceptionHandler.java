package com.gourmetcaree.admin.pc.sys.struts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.seasar.struts.util.ServletContextUtil;

import com.gourmetcaree.common.util.GourmetCareeUtil;

import jp.co.whizz_tech.common.web.log.CookieLogBuilder;
import jp.co.whizz_tech.common.web.log.ExceptionLogBuilder;
import jp.co.whizz_tech.common.web.log.HttpServletRequestLogBuilder;
import jp.co.whizz_tech.common.web.log.HttpSessionLogBuilder;
import jp.co.whizz_tech.common.web.log.LogHelper;
import jp.co.whizz_tech.common.web.log.ServletContextLogBuilderForServlet3;
import jp.co.whizz_tech.common.web.log.SystemLogBuilder;

/**
 * 例外をロギングする例外ハンドラクラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class BasicExceptionHandler extends ExceptionHandler {

	/*
	 * (非 Javadoc)
	 * @see org.apache.struts.action.ExceptionHandler#execute(java.lang.Exception, org.apache.struts.config.ExceptionConfig, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(Exception ex, ExceptionConfig ae,
			ActionMapping mapping, ActionForm formInstance,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		// エラーID取得
		String errorId = GourmetCareeUtil.createErrorId();

		LogHelper logHelper = new LogHelper();

		logHelper.setHeader(String.format("!!!!!!!!!! System Error occurred (ID = %s) !!!!!!!!!!", errorId));
		logHelper.addLogBuilder(new ExceptionLogBuilder(ex));
		logHelper.addLogBuilder(new ServletContextLogBuilderForServlet3(ServletContextUtil.getServletContext()));
		logHelper.addLogBuilder(new HttpServletRequestLogBuilder(request));
		logHelper.addLogBuilder(new HttpSessionLogBuilder(request));
		logHelper.addLogBuilder(new CookieLogBuilder(request));
		logHelper.addLogBuilder(new SystemLogBuilder());
		logHelper.setFooter("--- End.");

		logHelper.fatal();

		request.setAttribute("errorId", errorId);
		return super.execute(ex, ae, mapping, formInstance, request, response);
	}
}

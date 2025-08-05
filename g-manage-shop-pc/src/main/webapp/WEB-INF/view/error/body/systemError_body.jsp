<%@page pageEncoding="UTF-8"%>
<%@page import="jp.co.whizz_tech.common.web.log.*" %>
<%@page import="com.gourmetcaree.common.util.GourmetCareeUtil"%>
<%@page import="org.apache.jasper.JasperException" %>
<%
String errorId = (String) request.getAttribute("errorId");
if (errorId == null) {
	Throwable throwable
	= (Throwable) request.getAttribute("javax.servlet.error.exception");

	if (throwable != null) {
		if (throwable instanceof JasperException) {
			JasperException jasperEx = (JasperException) throwable;
			throwable = jasperEx.getRootCause();
		}

		errorId = GourmetCareeUtil.createErrorId();
		LogHelper logHelper = new LogHelper();
		logHelper.setHeader(String.format("!!!!!!!!!! System Error occurred (ID = %s) !!!!!!!!!!", errorId));
		logHelper.addLogBuilder(new ExceptionLogBuilder(throwable));
		logHelper.addLogBuilder(new ServletContextLogBuilderForServlet3(application));
		logHelper.addLogBuilder(new HttpServletRequestLogBuilder(request));
		logHelper.addLogBuilder(new HttpSessionLogBuilder(request));
		logHelper.addLogBuilder(new CookieLogBuilder(request));
		logHelper.addLogBuilder(new SystemLogBuilder());
		logHelper.setFooter("--- End.");
		logHelper.fatal();
	}
}
%>

<div id="main">

	<h2 title="エラー" class="title" id="error" >エラー</h2>
	<br />

	<div id="errMes" class="error">
		<ul>
			<li>
				システムエラーが発生しました。お手数ですが、最初から操作をやり直してください。<br />
				[ エラーID = <%= errorId %> ]
			</li>
		</ul>
	</div>
	<br />
	<div class="wrap_btn">
		<input type="button" name="" value="ログインページへ" onclick="location.href='${f:url('/login/logout/')}'" id="btn_conf" />
	</div>
</div>
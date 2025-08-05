<%@page pageEncoding="UTF-8"%>
<%@page import="org.seasar.framework.container.factory.SingletonS2ContainerFactory" %>
<%@page import="org.seasar.framework.container.S2Container" %>
<%@page import="org.seasar.framework.util.ClassUtil" %>
<%@page import="org.seasar.framework.util.FieldUtil" %>
<%
	S2Container container = SingletonS2ContainerFactory.getContainer();
	Object obj = container.getComponent("userDto");
	Class clazz = container.getComponentDef("userDto").getComponentClass();
	String company = FieldUtil.getString(ClassUtil.getField(clazz, "company"), obj);
	String name = FieldUtil.getString(ClassUtil.getField(clazz, "name"), obj);
	pageContext.setAttribute("headerName", name);
	pageContext.setAttribute("headerCompany", company);
%>
<!-- #header# -->
<div id="header" class="clear">
	<h1><img src="${ADMIN_CONTENS}/images/cmn/logo.gif" width="213" height="45" alt="${SITE_NAME}" /></h1>
	<ul>
		<li id="mn_logout" title="ログアウト"><a href="${f:url('/login/logout/')}"><img src="${ADMIN_CONTENS}/images/cmn/mn_logout.gif" onclick="if(!confirm('ログアウトしてもよろしいですか?')) {return false;}" alt="ログアウト" /></a>|</li><!--
		--><li id="mn_gtop" title="グルメキャリートップ"><a href="https://www.gourmetcaree.jp/" target="_blank"><img src="${ADMIN_CONTENS}/images/cmn/mn_gtop.gif" alt="グルメキャリートップ" /></a></li>
	</ul>
</div>
<!-- #header# -->
<hr />
<!-- #login_info# -->
<div id="login_info" class="clear">
	<p id="info">${f:h(headerCompany)}&nbsp;&nbsp;${f:h(headerName)}&nbsp;様</p>
	<p id="date"><gt:todayDate /></p>
</div>
<!-- #login_info# -->
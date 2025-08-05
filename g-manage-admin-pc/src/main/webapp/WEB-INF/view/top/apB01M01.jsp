<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 管理画面トップ"/>
	<tiles:put name="content" value="/WEB-INF/view/top/body/apB01_menu_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="PAGE_MENU" />
	<tiles:put name="actionPath" value="" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
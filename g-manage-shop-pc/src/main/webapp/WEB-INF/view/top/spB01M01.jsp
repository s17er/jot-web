<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メニュー画面"/>
	<tiles:put name="content" value="/WEB-INF/view/top/body/spB01_menu_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="" />
	<tiles:put name="actionPath" value="" />
	<tiles:put name="pageTitle" value="メニュー" />
	<tiles:put name="pageTitleId" value="" />
	<tiles:put name="pageInfo" value="顧客管理" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
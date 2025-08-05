<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | お知らせ管理メニュー"/>
	<tiles:put name="content" value="/WEB-INF/view/information/body/apL01_menu_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_MENU}" />
	<tiles:put name="actionPath" value="" />
	<tiles:put name="pageTitle1" value="お知らせ管理メニュー" />
	<tiles:put name="pageTitleId1" value="title_informationMnt" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
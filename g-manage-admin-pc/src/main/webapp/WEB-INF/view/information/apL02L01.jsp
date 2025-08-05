<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<c:set var="ADMIN_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.ADMIN_SCREEN %>" scope="page" />
<c:set var="SHOP_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.SHOP_SCREEN %>" scope="page" />
<c:set var="MY_PAGE_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.MY_PAGE_SCREEN %>" scope="page" />

<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 運営側お知らせ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/information/body/apL_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<c:choose>
	<c:when test="${managementScreenKbn eq ADMIN_SCREEN}">
		<tiles:put name="pageKbn" value="${PAGE_LIST}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="運営側お知らせ一覧" />
		<tiles:put name="pageTitleId1" value="title_adminInformationList" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="defaultMsg" value="" />
	</c:when>
	<c:when test="${managementScreenKbn eq SHOP_SCREEN}">
		<tiles:put name="pageKbn" value="${PAGE_LIST}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="店舗側お知らせ一覧" />
		<tiles:put name="pageTitleId1" value="title_shopInformationList" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="defaultMsg" value="" />
	</c:when>
	<c:when test="${managementScreenKbn eq MY_PAGE_SCREEN}">
		<tiles:put name="pageKbn" value="${PAGE_LIST}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="Myページお知らせ一覧" />
		<tiles:put name="pageTitleId1" value="title_mypageInformationList" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="defaultMsg" value="" />
	</c:when>
	<c:otherwise>
		<tiles:put name="pageKbn" value="${PAGE_LIST}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="運営側お知らせ一覧" />
		<tiles:put name="pageTitleId1" value="title_adminInformationList" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="defaultMsg" value="" />
	</c:otherwise>
	</c:choose>

</tiles:insert>
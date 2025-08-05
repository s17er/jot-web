<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<c:set var="ADMIN_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.ADMIN_SCREEN %>" scope="page" />
<c:set var="SHOP_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.SHOP_SCREEN %>" scope="page" />
<c:set var="MY_PAGE_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.MY_PAGE_SCREEN %>" scope="page" />


<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 運営側お知らせ編集完了"/>
	<tiles:put name="content" value="/WEB-INF/view/information/body/apL_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<c:choose>
	<c:when test="${managementScreenKbn eq ADMIN_SCREEN}">
		<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="運営側お知らせ編集完了" />
		<tiles:put name="pageTitleId1" value="title_adminInformationEditComp" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="navigationText3" value="運営側お知らせ一覧へ" />
		<tiles:put name="navigationPath3" value="/manageInfo/list/index/${ADMIN_SCREEN}" />
		<tiles:put name="defaultMsg" value="登録しました。" />
	</c:when>
	<c:when test="${managementScreenKbn eq SHOP_SCREEN}">
		<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="店舗側お知らせ編集完了" />
		<tiles:put name="pageTitleId1" value="title_shopInformationEditComp" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="navigationText3" value="店舗側お知らせ一覧へ" />
		<tiles:put name="navigationPath3" value="/manageInfo/list/index/${SHOP_SCREEN}" />
		<tiles:put name="defaultMsg" value="登録しました。" />
	</c:when>
	<c:when test="${managementScreenKbn eq MY_PAGE_SCREEN}">
		<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="Myページお知らせ編集完了" />
		<tiles:put name="pageTitleId1" value="title_mypageInformationEditComp" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="navigationText3" value="Myページお知らせ一覧へ" />
		<tiles:put name="navigationPath3" value="/manageInfo/list/index/${MY_PAGE_SCREEN}" />
		<tiles:put name="defaultMsg" value="登録しました。" />
	</c:when>
	<c:otherwise>
		<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
		<tiles:put name="actionPath" value="/manageInfo/edit/" />
		<tiles:put name="pageTitle1" value="運営側お知らせ編集完了" />
		<tiles:put name="pageTitleId1" value="title_adminInformationEditComp" />
		<tiles:put name="navigationText1" value="管理画面トップへ" />
		<tiles:put name="navigationPath1" value="/top/menu/" />
		<tiles:put name="navigationText2" value="お知らせ管理メニューへ" />
		<tiles:put name="navigationPath2" value="/information/menu/" />
		<tiles:put name="navigationText3" value="運営側お知らせ一覧へ" />
		<tiles:put name="navigationPath3" value="/manageInfo/list/index/${ADMIN_SCREEN}" />
		<tiles:put name="defaultMsg" value="登録しました。" />
	</c:otherwise>
	</c:choose>
</tiles:insert>
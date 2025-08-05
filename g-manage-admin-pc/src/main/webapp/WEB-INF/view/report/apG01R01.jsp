<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | レポート詳細"/>
	<tiles:put name="content" value="/WEB-INF/view/report/body/apG01_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DETAIL}" />
	<tiles:put name="actionPath" value="/report/detail/" />
	<tiles:put name="pageTitle1" value="レポート詳細" />
	<tiles:put name="pageTitleId1" value="title_reportDetail" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="レポート一覧へ" />
	<tiles:put name="navigationPath2" value="/report/list/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
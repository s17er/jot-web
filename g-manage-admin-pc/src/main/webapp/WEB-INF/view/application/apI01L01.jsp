<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 応募データ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/apI01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/application/list/" />
	<tiles:put name="actionCsvPath" value="/application/list/output/" />
	<tiles:put name="actionMaxRowPath" value="/application/list/changeMaxRow" />
	<tiles:put name="pageTitle1" value="応募データ一覧" />
	<tiles:put name="pageTitleId1" value="title_applicationList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
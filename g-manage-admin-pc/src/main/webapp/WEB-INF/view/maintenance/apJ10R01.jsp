<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | いたずら応募条件詳細"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ10_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DETAIL}" />
	<tiles:put name="actionPath" value="/mischief/detail/" />
	<tiles:put name="deleteActionPath" value="/mischief/delete/" />
	<tiles:put name="pageTitle1" value="いたずら応募条件詳細" />
	<tiles:put name="pageTitleId1" value="title_mischiefApplicationConditionDetail" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メンテナンスメニューへ" />
	<tiles:put name="navigationPath2" value="/maintenance/menu/" />
	<tiles:put name="navigationText3" value="いたずら応募条件一覧へ" />
	<tiles:put name="navigationPath3" value="/mischief/list/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
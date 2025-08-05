<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 駅グループ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ08_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/terminal/list/" />
	<tiles:put name="pageTitle1" value="駅グループ一覧" />
	<tiles:put name="pageTitleId1" value="title_terminalList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メンテナンスメニューへ" />
	<tiles:put name="navigationPath2" value="/maintenance/menu/" />
	<tiles:put name="defaultMsg" value="" />

	<tiles:put name="editPath" value="/terminal/edit/index/" />
	<tiles:put name="deletePath" value="/terminal/delete/index/" />

</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 求職者一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionSearchPath" value="/member/list" />
	<tiles:put name="actionScoutPath" value="/member/list" />
	<tiles:put name="addKeepBoxAjaxPath" value="/member/list/addKeepBox/" />
	<tiles:put name="pageTitle" value="求職者一覧" />
	<tiles:put name="pageTitleId" value="memberSearch" />
	<tiles:put name="defaultMsg" value="条件を選んで「検索」ボタンを押してください。"/>
	<tiles:put name="memberListPath" value="/member/list" />
	<tiles:put name="scoutMailPath" value="/scoutMail/list" />
	<tiles:put name="keepBoxPath" value="/member/keepBox" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | スカウト一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC06_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/scoutMember/list" />
	<tiles:put name="pageTitle" value="スカウト送信者一覧" />
	<tiles:put name="pageTitleId" value="scoutList" />
	<tiles:put name="defaultMsg0" value="過去1年間のメール履歴一覧です。" />
	<tiles:put name="defaultMsg1" value="詳細をクリックすると、スカウト送信者の詳細がご覧いただけます。" />
	<tiles:put name="defaultMsg2" value="会員IDをクリックすると、会員のメール履歴がご覧いただけます。" />
	<tiles:put name="memberListPath" value="/member/list" />
	<tiles:put name="keepBoxPath" value="/member/keepBox" />

	<tiles:put name="pageNaviPath" value="/scoutMember/list/changePage/" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | スカウトメール一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE02_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/scoutMail/list" />
	<tiles:put name="sendBoxPath" value="/scoutMail/list/changeBox/1" />
	<tiles:put name="receiveBoxPath" value="/scoutMail/list/changeBox/2" />
	<tiles:put name="pageTitle" value="スカウトメール一覧" />
	<tiles:put name="pageTitleId" value="scoutMail" />
	<tiles:put name="defaultMsg1" value="過去1年間のメール履歴一覧です。"/>
	<tiles:put name="defaultMsg2" value="件名をクリックすると、スカウトメールの詳細がご覧いただけます。"/>
	<tiles:put name="memberListPath" value="/member/list" />
	<tiles:put name="scoutMailPath" value="/scoutMail/list" />
	<tiles:put name="keepBoxPath" value="/member/keepBox" />
	<tiles:put name="SEND" value="1" />
	<tiles:put name="UNOPENED" value="1" />
	<tiles:put name="OPENED" value="2" />
	<tiles:put name="REPLIED" value="3" />

	<tiles:put name="pageNaviPath" value="/scoutMail/list/changePage/" />
</tiles:insert>
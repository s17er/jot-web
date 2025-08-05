<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 質問メール一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC04_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/observateApplicationMail/list" />
	<tiles:put name="pageTitle" value="質問メール一覧" />
	<tiles:put name="pageTitleId" value="questionMail" />
	<tiles:put name="defaultMsg0" value="過去1年間のメール履歴一覧です。" />
	<tiles:put name="defaultMsg1" value="件名をクリックすると、質問メールの詳細がご覧いただけます。" />

	<tiles:put name="pageNaviPath" value="/observateApplicationMail/list/changePage/" />
</tiles:insert>
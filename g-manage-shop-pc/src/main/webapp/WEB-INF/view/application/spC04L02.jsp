<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 質問メール一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC04_observate_applicationlist_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/observateApplication/list" />
	<tiles:put name="pageTitle" value="質問者一覧" />
	<tiles:put name="pageTitleId" value="questionList" />
	<tiles:put name="defaultMsg0" value="過去1年間のメール履歴一覧です。" />
	<tiles:put name="defaultMsg1" value="質問者をクリックすると、質問者のメール履歴がご覧いただけます。" />

	<tiles:put name="pageNaviPath" value="/observateApplication/list/changePage/" />
</tiles:insert>

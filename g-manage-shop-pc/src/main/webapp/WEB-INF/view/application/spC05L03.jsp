<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 応募者一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC05_detail_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/arbeit/detailMailList/" />
	<tiles:put name="pageTitle" value="応募者一覧" />
	<tiles:put name="pageTitleId" value="beitEntryHistory" />
	<tiles:put name="defaultMsg0" value="過去1年間のメール履歴一覧です。" />
	<tiles:put name="defaultMsg1" value="詳細をクリックすると、応募者の詳細がご覧いただけます。"/>
	<tiles:put name="defaultMsg2" value="件名をクリックすると、応募メールの詳細がご覧いただけます。" />
	<tiles:put name="selectPatternAjaxPath" value="/util/patternSentence/addSentence" />
</tiles:insert>
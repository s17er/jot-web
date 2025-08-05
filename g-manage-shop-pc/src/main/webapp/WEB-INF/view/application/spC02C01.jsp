<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メール送信画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC02_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="input" />
	<tiles:put name="actionPath" value="/applicationMail/input" />
	<tiles:put name="pageTitle" value="メール送信" />
	<tiles:put name="pageTitleId" value="mailInput" />
	<tiles:put name="defaultMsg" value="下記の項目を全てご入力の上、「確認」ボタンを押して下さい。" />
	<tiles:put name="selectPatternAjaxPath" value="/util/patternSentence/addSentence" />
	<tiles:put name="selectTitleAjaxPath" value="/util/patternSentence/addTitle" />
</tiles:insert>
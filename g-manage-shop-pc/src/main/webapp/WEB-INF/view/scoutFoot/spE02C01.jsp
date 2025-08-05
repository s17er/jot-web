<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | スカウトメール入力画面"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE02_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="input" />
	<tiles:put name="actionPath" value="/scoutMail/input/submit" />
	<tiles:put name="selectPatternAjaxPath" value="/scoutMail/input/addSentence" />
	<tiles:put name="selectTitleAjaxPath" value="/scoutMail/input/addTitle" />
	<tiles:put name="addUrlAjaxPath" value="/scoutMail/input/addUrl" />
	<c:choose>
		<c:when test="${returnMailBlockDispFlg}">
			<tiles:put name="pageTitle" value="メール返信画面" />
			<tiles:put name="pageTitleId" value="mailInput" />
		</c:when>
		<c:otherwise>
			<tiles:put name="pageTitle" value="スカウトメール入力画面" />
			<tiles:put name="pageTitleId" value="scoutMailInput" />
		</c:otherwise>
	</c:choose>
	<tiles:put name="defaultMsg" value="下記の項目を入力の上、「確認」ボタンを押して下さい。"/>
</tiles:insert>
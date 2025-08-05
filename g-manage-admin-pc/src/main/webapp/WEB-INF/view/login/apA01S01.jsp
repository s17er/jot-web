<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/commonLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | ログイン画面"/>
	<tiles:put name="content" value="/WEB-INF/view/login/body/apA_login_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="" />
	<tiles:put name="actionPath" value="/login/login/login" />
	<tiles:put name="pageTitle1" value="" />
	<tiles:put name="pageTitleId1" value="" />
	<tiles:put name="defaultMsg" value="ログインID、パスワードを入力し、「ログイン」ボタンを押して下さい。" />
</tiles:insert>
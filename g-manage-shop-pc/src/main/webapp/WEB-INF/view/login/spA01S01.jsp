<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/commonLayout.jsp" flush="false">
	<tiles:put name="title" value="ログイン画面 | グルメキャリー顧客管理"/>
	<tiles:put name="content" value="/WEB-INF/view/login/body/spA01_login_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="" />
	<tiles:put name="actionPath" value="/login/login/login" />
	<tiles:put name="pageTitle" value="ログイン" />
	<tiles:put name="pageTitleId" value="" />
	<tiles:put name="pageInfo" value="顧客管理　ログイン画面" />
	<tiles:put name="defaultMsg" value="ログインID、パスワードを入力してください。" />
</tiles:insert>
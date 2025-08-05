<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/passwordReissueLayout.jsp" flush="false">
	<tiles:put name="title" value="パスワード再登録画面 | グルメキャリー顧客管理"/>
	<tiles:put name="content" value="/WEB-INF/view/passwordReissue/body/spI01C01_2_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/passwordReissue/edit/" />
	<tiles:put name="defaultMsg" value="新しいパスワードを入力し「登録」ボタンを押してください。" />
	<tiles:put name="pageTitle" value="パスワード再登録" />
	<tiles:put name="pageTitleId" value="title_reissue" />
	<tiles:put name="pageInfo" value="顧客管理　パスワード再登録画面" />
</tiles:insert>
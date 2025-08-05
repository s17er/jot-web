<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/passwordReissueLayout.jsp" flush="false">
	<tiles:put name="title" value="パスワード再登録画面 | グルメキャリー顧客管理"/>
	<tiles:put name="content" value="/WEB-INF/view/passwordReissue/body/spH01C_comp_body.jsp" />


	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="" />
	<tiles:put name="actionPath" value="/passwordReissue/edit/" />
	<tiles:put name="pageTitle" value="パスワード再登録完了" />
	<tiles:put name="pageTitleId" value="title_reissueComp" />
	<tiles:put name="pageInfo" value="顧客管理　パスワード再登録画面" />
	<tiles:put name="defaultMsg" value="パスワードの再登録が完了しました。" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | パスワード編集完了"/>
	<tiles:put name="content" value="/WEB-INF/view/changePassword/body/apM_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/changePassword/edit/" />
	<tiles:put name="pageTitle1" value="パスワード編集完了" />
	<tiles:put name="pageTitleId1" value="title_changePasswordEditComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="パスワードを変更しました。" />
</tiles:insert>
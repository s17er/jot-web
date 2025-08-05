<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | パスワード編集確認"/>
	<tiles:put name="content" value="/WEB-INF/view/changePassword/body/apM01_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/changePassword/edit/" />
	<tiles:put name="pageTitle1" value="パスワード編集確認" />
	<tiles:put name="pageTitleId1" value="title_changePasswordEditConf" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「登録」ボタンを押して下さい。" />
</tiles:insert>
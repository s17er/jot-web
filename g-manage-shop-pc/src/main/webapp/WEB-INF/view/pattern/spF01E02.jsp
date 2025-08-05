<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="定型文の確認 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/pattern/body/spF01_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="edit" />
	<tiles:put name="actionPath" value="/pattern/edit" />
	<tiles:put name="pageTitle" value="定型文の確認" />
	<tiles:put name="pageTitleId" value="patternEditConf" />
	<tiles:put name="defaultMsg" value="下記の項目を確認の上、「登録」ボタンを押して下さい。"/>
</tiles:insert>
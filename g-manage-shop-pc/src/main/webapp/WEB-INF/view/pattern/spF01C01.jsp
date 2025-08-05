<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="定型文の登録 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/pattern/body/spF01_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="input" />
	<tiles:put name="actionPath" value="/pattern/input" />
	<tiles:put name="pageTitle" value="定型文の登録" />
	<tiles:put name="pageTitleId" value="patternInput" />
	<tiles:put name="defaultMsg" value=""/>
</tiles:insert>
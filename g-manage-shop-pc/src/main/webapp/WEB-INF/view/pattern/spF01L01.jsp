<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="定型文の設定 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/pattern/body/spF01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/pattern/list" />
	<tiles:put name="pageTitle" value="定型文一覧" />
	<tiles:put name="pageTitleId" value="patternList" />
	<tiles:put name="defaultMsg" value=""/>
</tiles:insert>
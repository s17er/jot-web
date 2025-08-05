<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="定型文の詳細 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/pattern/body/spF01_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="detail" />
	<tiles:put name="actionPath" value="/pattern/detail" />
	<tiles:put name="deleteActionPath" value="/pattern/delete" />
	<tiles:put name="pageTitle" value="定型文の詳細" />
	<tiles:put name="pageTitleId" value="patternDetail" />
	<tiles:put name="defaultMsg" value=""/>
</tiles:insert>
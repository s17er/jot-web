<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メール削除完了"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE02_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="input" />
	<tiles:put name="actionPath" value="/scoutMail/delete" />
	<tiles:put name="pageTitle" value="メール削除完了" />
	<tiles:put name="pageTitleId" value="mailDelComp" />
	<tiles:put name="defaultMsg" value="削除しました。"/>
</tiles:insert>
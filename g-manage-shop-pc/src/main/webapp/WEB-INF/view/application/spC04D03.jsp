<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メール削除完了画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC02_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="delete" />
	<tiles:put name="actionPath" value="" />
	<tiles:put name="pageTitle" value="メール削除完了" />
	<tiles:put name="pageTitleId" value="mailDelComp" />
	<tiles:put name="defaultMsg" value="削除しました。" />
	<tiles:put name="backListPath" value="/observateApplicationMail/list/"/>
</tiles:insert>
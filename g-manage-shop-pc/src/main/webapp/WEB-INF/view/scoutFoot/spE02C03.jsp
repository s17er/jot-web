<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | スカウトメール入力完了"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE02_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="input" />
	<tiles:put name="actionPath" value="/scoutMail/input" />
	<tiles:put name="pageTitle" value="スカウトメール入力完了" />
	<tiles:put name="pageTitleId" value="scoutMailComp" />
	<tiles:put name="defaultMsg" value="送信しました。"/>
</tiles:insert>
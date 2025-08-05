<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メール返信確認画面"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="input" />
	<tiles:put name="actionPath" value="/scoutMail/input" />
	<tiles:put name="pageTitle" value="メール返信確認" />
	<tiles:put name="pageTitleId" value="mailInputConf" />
	<tiles:put name="defaultMsg" value="下記の項目を確認の上、「送信」ボタンを押して下さい。"/>
	<tiles:put name="memberListPath" value="/member/list" />
	<tiles:put name="scoutMailPath" value="/scoutMail/list" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | グルメdeバイトメール送信確認画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC05_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="conf" />
	<tiles:put name="actionPath" value="/arbeitMail/input/" />
	<tiles:put name="pageTitle" value="グルメdeバイトメール送信確認" />
	<tiles:put name="pageTitleId" value="mailInputConf" />
	<tiles:put name="defaultMsg" value="下記の内容をご確認の上、「送信」ボタンを押して下さい。" />
</tiles:insert>
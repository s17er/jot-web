<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メール送信完了画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC02_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/kansai.css" />
	<tiles:put name="pageKbn" value="comp" />
	<tiles:put name="actionPath" value="/arbeitMail/input/" />
	<tiles:put name="pageTitle" value="メール送信完了" />
	<tiles:put name="pageTitleId" value="mailInputComp" />
	<tiles:put name="defaultMsg" value="送信しました。" />
	<tiles:put name="backListPath" value="/arbeitMail/list/"/>
</tiles:insert>
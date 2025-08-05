<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%	/* タイルズ：変数のスコープをrequestに設定 */	 %>
<tiles:importAttribute scope="request" />
<c:set var="SSL_DOMAIN" value="${common['gc.sslDomain']}" />
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<meta http-equiv="content-style-type" content="text/css" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="Description" content="グルメキャリーweb 運営者側管理システム" />
	<meta name="Author" content="Joffice Inc." />

	<%	/* タイルズ：titleタグ */	 %>
	<title><tiles:getAsString name="title" /></title>

	<%	/* CSSファイルを設定 */	 %>
	<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/shared.css" />
	<link rel="shortcut icon" href="${ADMIN_CONTENS}/images/favicon.ico">

	<%	/* javascriptファイルを設定 */	 %>
	<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${ADMIN_CONTENS}/js/admin.js"></script>
	<script type="text/javascript" src="${ADMIN_CONTENS}/js/openAdminWindow.js"></script>
	<script type="text/javascript" src="${ADMIN_CONTENS}/js/btn.color.js"></script>
	<script type="text/javascript">
	//<![CDATA[
	    <%-- Ajaxの全体設定 --%>
		$.ajaxSetup({
			<%-- キャッシュをオフに。キャッシュが有効の場合、ブラウザにより同一URLへのリクエストが行われなくなる。 --%>
			cache : false
		});
		// ]]>
	</script>

    <%-- head部分に自由挿入 --%>
    <c:if test="${not empty headJsp}">
        <jsp:include page="${headJsp}" />
    </c:if>
</head>

<body>
	<!-- #all# -->
	<div id="all">
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<tiles:insert page="adminHeader.jsp" />
	<hr />
		<% /* タイルズ：コンテンツ部 */ %>
		<tiles:insert attribute="content" />
	<hr />
		<% /* タイルズ：フッター部 */ %>
		<tiles:insert page="commonFooter.jsp" />
	</div>
	<!-- #all# -->
</body>
</html>
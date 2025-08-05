<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%	/* タイルズ：変数のスコープをrequestに設定 */	 %>
	<tiles:importAttribute scope="request"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
<tiles:insert page="commonTracking.jsp" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<meta http-equiv="content-style-type" content="text/css" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="Description" content="グルメキャリーweb 運営者側管理システム" />
	<meta name="Author" content="Joffice Inc." />
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, user-scalable=yes">

	<%	/* javascriptファイルを設定 */	 %>
	<script type="text/javascript" src="${SHOP_CONTENS}/js/openShopWindow.js"></script>

	<%	/* CSSファイルを設定 */	 %>
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/shared.css" />
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/tokyoClient.css" />
	<title>
		<%	/* タイルズ：titleタグ */	 %>
		<tiles:getAsString name="title" />
	</title>
</head>
<body>
<!-- Google Tag Manager (noscript) --> <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NCVX9X2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript> <!-- End Google Tag Manager (noscript) -->
	<div id="all">
		<%	/* タイルズ：コンテンツ部 */	 %>
		<tiles:insert attribute="content" />
	</div>
</body>
</html>
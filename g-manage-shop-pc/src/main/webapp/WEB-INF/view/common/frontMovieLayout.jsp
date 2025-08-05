<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%	/* タイルズ：変数のスコープをrequestに設定 */	 %>
<tiles:importAttribute scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta name="Description" content="飲食 求人 転職 就職 業界専門の情報誌グルメキャリー。人材募集をお考えの企業様にもご活用頂けます。飲食業界専門の人材派遣･紹介もあり。${f:h(seoDescription)}" />
	<meta name="keywords" content="転職,求人,仕事,飲食 求人,仕事,飲食,人材募集,グルキャリ ${f:h(seoKeywords)}" />
	<title><tiles:getAsString name="title" /></title>
	<meta http-equiv="content-script-type" content="text/javascript" />
	<meta http-equiv="content-style-type" content="text/css" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="Author" content="Joffice Inc." />
	<link rel="stylesheet" type="text/css" href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/reset.css" />
	<link rel="stylesheet" type="text/css" href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/common.css" />
	<link rel="stylesheet" type="text/css" href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/preview.css" />
	<link href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/displayShop.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/jquery/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/openWindow.js"></script>
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/copyright.js"></script>
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/setAjax.js"></script>
	<script type="text/javascript">
	//<![CDATA[
	    <%-- Ajaxの全体設定 --%>
		$.ajaxSetup({
			<%-- キャッシュをオフに。キャッシュが有効の場合、ブラウザにより同一URLへのリクエストが行われなくなる。 --%>
			cache : false,
			<%-- 接続待ちをする最大時間 --%>
			timeout : 10000
		});
	// ]]>
	</script>

	<tiles:insert attribute="head" />
</head>
<body>
	<!-- #all# -->
	<div id="all">
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<tiles:insert page="frontHeader.jsp" />
	<hr />
		<% /* タイルズ：コンテンツ部 */ %>
		<tiles:insert attribute="content" />
	<hr />
		<% /* タイルズ：フッター部 */ %>
		<tiles:insert page="frontFooter.jsp" />
	</div>
	<!-- #all# -->
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%	/* タイルズ：変数のスコープをrequestに設定 */	 %>
<tiles:importAttribute scope="request" />
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
<tiles:insert page="commonTracking.jsp" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<meta http-equiv="content-style-type" content="text/css" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="robots" content="noindex" />
	<meta name="Description" content="グルメキャリーweb 店舗側管理システム" />
	<meta name="Author" content="Joffice Inc." />
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, user-scalable=yes">

	<%	/* タイルズ：titleタグ */	 %>
	<title><tiles:getAsString name="title" /></title>

	<%	/* CSSファイルを設定 */	 %>
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/mail.css" />
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/style.css" />
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/jquery/validationEngine.jquery.css" />
	<link href="https://fonts.googleapis.com/css?family=Material+Icons|Material+Icons+Outlined|Material+Icons+Round|Material+Icons+Sharp|Material+Icons+Two+Tone" rel="stylesheet">
	<link rel="shortcut icon" href="${SHOP_CONTENS}/images/favicon.ico">

	<%	/* javascriptファイルを設定 */	 %>
	<script type="text/javascript" src="${SHOP_CONTENS}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${SHOP_CONTENS}/js/openShopWindow.js"></script>
	<script type="text/javascript" src="${SHOP_CONTENS}/js/common.js"></script>
	<script type="text/javascript" src="${SHOP_CONTENS}/js/webdata.js"></script>
	<script type="text/javascript">
	//<![CDATA[
	    <%-- Ajaxの全体設定 --%>
		$.ajaxSetup({
			<%-- キャッシュをオフに。キャッシュが有効の場合、ブラウザにより同一URLへのリクエストが行われなくなる。 --%>
			cache : false,
			<%-- 接続待ちをする最大時間 --%>
			timeout : 5000
		});
	// ]]>
	</script>
</head>

<body>
<!-- Google Tag Manager (noscript) --> <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NCVX9X2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript> <!-- End Google Tag Manager (noscript) -->
	<!-- #all# -->
	<div id="all">

		<% /* タイルズ：ナビ部 */ %>
		<tiles:insert page="nav.jsp" />
		<% /* タイルズ：ローダー部 */ %>
		<tiles:insert page="loader.jsp" />

		<div id="content">
			<% /* タイルズ：ヘッダーメニュー部 */ %>
			<tiles:insert page="shopHeader.jsp" />
			<% /* タイルズ：コンテンツ部 */ %>
			<tiles:insert attribute="content" />
		</div>
		<div id="page_top">
			<a href="#all" title="ページトップへ"></a>
		</div>
		<script>
	      $('.menu_btn [aria-expanded]').on('click', function() {
	          let expanded = $(this).attr('aria-expanded') === 'true',
	              controls = $(this).attr('aria-controls');
	          $(this).attr('aria-expanded', !expanded);
	          $('#' + controls).attr('aria-hidden', expanded);
	        });
		</script>
	</div>
	<!-- #all# -->
</body>
</html>
<?xml version="1.0" encoding="UTF-8"?>
<tiles:importAttribute scope="request"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
<tiles:insert page="commonTracking.jsp" />
<%	/* タイルズ：titleタグ */	 %>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
	<meta name="Description" content="${f:h(seoDescription)} ｜ 飲食業界専門の求人情報誌。業種（イタリアン,フレンチ）、職種（パティシエ,バーテンダー,ソムリエ）、勤務地（東京・神奈川・千葉・埼玉・海外）による検索等。飲食店転職ならグルメキャリー! "/>
	<meta name="keywords" content="${f:h(seoKeywords)}, ｜ 飲食,求人,飲食求人,東京,埼玉,神奈川,千葉,海外,パティシエ,バーテンダー,ソムリエ,イタリアン,フレンチ,和食,洋食,飲食店" />
	<link rel="apple-touch-icon" href="/ipx${f:h(imgLocation)}/iconHome.png" />
	<title><tiles:getAsString name="title" />&nbsp; ｜ 飲食業界・フードビジネス業界の飲食求人情報 グルメキャリー首都圏版</title>
	<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/reset.css" />
	<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/common.css" />
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/ipx/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/ipx/js/setAjax.js"></script>
	<tiles:insert attribute="head" />
</head>


<body>
<!-- Google Tag Manager (noscript) --> <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NCVX9X2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript> <!-- End Google Tag Manager (noscript) -->
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<tiles:insert attribute="header" />

		<% /* タイルズ：コンテンツ部 */ %>
		<tiles:insert attribute="content" />

		<% /* タイルズ：フッター部 */ %>
		<tiles:insert attribute="footer" />

</body>
</html>
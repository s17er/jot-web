<%@page pageEncoding="UTF-8"%>
<tiles:importAttribute scope="request"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
<tiles:insert page="commonTracking.jsp" />
<%	/* タイルズ：titleタグ */	 %>
<title><tiles:getAsString name="title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="Keywords" content="飲食,求人,飲食求人,大阪,京都,神戸,ﾊﾟﾃｨｼｴ,ﾊﾞｰﾃﾝﾀﾞｰ,ｿﾑﾘｴ,ｲﾀﾘｱﾝ,ﾌﾚﾝﾁ,和食,洋食,中華" />
<meta name="Description" content="飲食業界専門の求人情報｡雇用形態､職種､勤務地による検索等｡ｸﾞﾙﾒｷｬﾘｰ本誌は毎月第1･3木曜日各ｺﾝﾋﾞﾆｴﾝｽｽﾄｱ･書店にて好評発売中!" />
<title>ｸﾞﾙﾒｷｬﾘｰ｜プレビュー</title>
<style type="text/css">
a:link{ color: ${SITE_COLOR}; }
a:visited{ color: ${SITE_COLOR}; }
</style>

</head>


<body style="background-color: #FFFFFF;">
<!-- Google Tag Manager (noscript) --> <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NCVX9X2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript> <!-- End Google Tag Manager (noscript) -->
<a name="top"></a>
<span style="font-size:xx-small;">
	<!-- #all# -->
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<tiles:insert page="frontMobileHeader.jsp" />

		<% /* タイルズ：コンテンツ部 */ %>
		<tiles:insert attribute="content" />

		<% /* タイルズ：フッター部 */ %>
		<tiles:insert page="frontMobileFooter.jsp" />

	<!-- #all# -->
</span>
</body>
</html>
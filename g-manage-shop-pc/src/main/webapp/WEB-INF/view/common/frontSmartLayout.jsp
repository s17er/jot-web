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
	<meta name="Description" content="${f:h(seoDescription)}&nbsp;求人｜ 飲食業界専門の求人情報誌。業種（イタリアン,フレンチ）、職種（パティシエ,バーテンダー,ソムリエ）、勤務地（東京・神奈川・千葉・埼玉・海外）による検索等。飲食店転職ならグルメキャリー! "/>
	<meta name="keywords" content="${f:h(seoKeywords)},求人, ｜ 飲食,求人,飲食求人,東京,埼玉,神奈川,千葉,海外,パティシエ,バーテンダー,ソムリエ,イタリアン,フレンチ,和食,洋食,飲食店" />
	<link rel="apple-touch-icon" href="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/iconHome.png" />
	<title><tiles:getAsString name="title" />&nbsp;求人&nbsp; ｜ 飲食業界・フードビジネス業界の飲食求人情報 グルメキャリー首都圏版</title>
	<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/reset.css" />
	<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/common.css" />
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/ipx/js/jquery-1.7.1.min.js"></script>
	<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/shoplist.css" />
	<script type="text/javascript" src="${f:h(frontHttpUrl)}/ipx/js/icon.js"></script>

	<script type="text/javascript">
$(function(){
	$("#menu").on("click", function() {
		$("#menuBox").fadeToggle(200);
		$("#menuListBg").fadeIn();
	});
	return false;
});

$(function(){
	$("#closeBtn").on("click", function() {
		$("#menuBox").fadeToggle(200);
		$("#menuListBg").fadeOut();
	});
	return false;
});
</script>
</head>


<body>
<!-- Google Tag Manager (noscript) --> <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NCVX9X2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript> <!-- End Google Tag Manager (noscript) -->
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<header id="top" class="clearfix">

			<div id="logo">
				<a href="${f:h(frontHttpUrl)}/ipx/index.html"><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/logo.png" width="200" height="44" alt="飲食店転職・飲食求人グルメキャリー首都圏版ロゴ"></a>
				<p>
					<span class="resetDate">xxxx/xx/xx 更新</span>&nbsp;<span class="resetText">毎週木曜更新</span>
				</p>
			</div>

			<div id="menu"><a href="#" class="menuIco">menu</a><p>MENU</p></div>

			<%-- カテゴリーリスト --%>
			<div id="menuListBg"></div>
			<div id="menuBox">
				<div class="menuTop clearfix">
					<p>MENU</p>
					<p id="closeBtn">×</p>
				</div>

				<nav id="menuList">
					<ul>
						<li><a href="#">新規会員登録</a></li>
						<li><a href="#">MYページログイン</a></li>
					</ul>
				</nav>
			</div>

		</header>

		<% /* タイルズ：コンテンツ部 */ %>
		<tiles:insert attribute="content" />

		<%@page pageEncoding="UTF-8"%>

	<!-- // 戻るボタン -->
	<!-- // シャドウバー -->
	<div class="sBar"></div>
		<!-- // トップへ戻る↑ -->
		<div class="clearfix">
			<div id="goTop"><a href="#top">ページトップへ↑</a></div>
			<!-- // ←戻る -->

			<div id="goBack">
				<a href="#">←戻る</a>
			</div>
	</div>

<!-- // フッター -->
<!-- // フッター -->
<footer>
	<a href="#">ホーム</a>｜<a href="#">掲載をお考えの企業様へ</a>｜<a href="#">ヘルプ</a><br>
	<a href="#/">このサイトを友達に教える</a>｜<a href="#" target="_blank">本誌購読はコチラ</a><br>
	<a href="#" >関西版</a>｜<a href="#" >東海版</a>
	<p>表示モード ： スマートフォン | <a href="#" >パソコン</a></p>
	<!-- // 会社アドレス -->
	<address>
		Copyright 2013 (C) J office Tokyo Co.,Ltd.
	</address>
</footer>


</body>
</html>
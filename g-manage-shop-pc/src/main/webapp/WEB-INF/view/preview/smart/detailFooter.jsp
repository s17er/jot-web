<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants.AreaCd"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants.AreaCdEnum"%>
<%@page pageEncoding="UTF-8"%>

<c:set var="SHUTOKEN_AREA" value="<%=AreaCd.SHUTOKEN_AREA%>" scope="page" />



	<!-- // 戻るボタン -->
	<!-- // シャドウバー -->
	<div class="sBar"></div>
		<!-- // トップへ戻る↑ -->
		<div class="clearfix">
			<div id="goTop"><a href="#top">ページトップへ↑</a></div>
			<!-- // ←戻る -->
		<div id="goBack"><a href="${f:url(backPath)}">←戻る</a></div>
	</div>

<!-- // フッター -->
<!-- // フッター -->
<footer>
<a href="#">ホーム</a>｜<a href="#">掲載をお考えの企業様へ</a>｜<a href="#">ヘルプ</a><br>
<a href="#">このサイトを友達に教える</a>｜<a href="#" target="_blank">本誌購読はコチラ</a><br>
<a href="#" >関西版</a>｜<a href="#" >東海版</a>
<p>表示モード ： スマートフォン | <a href="#" >パソコン</a></p>
<!-- // 会社アドレス -->
<address>
Copyright 2013 (C) J office Tokyo Co.,Ltd.
</address>
</footer>

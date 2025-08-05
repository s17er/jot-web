<%@page pageEncoding="UTF-8"%>

<c:choose>
	<c:when test="${userDto.areaCd eq AREA_KANSAI}">
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/kansai.css" />
	</c:when>
	<%-- ログイン前などエリアを特定できない場合 --%>
	<c:otherwise>
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/kansai.css" />
	</c:otherwise>
</c:choose>

<div id="header" class="clear">

<c:choose>
	<c:when test="${userDto.areaCd eq AREA_KANSAI}">
		<h1><a href="https://www.gourmetcaree.jp/" title="グルメキャリー首都圏">グルメキャリー首都圏</a></h1>
	</c:when>
	<%-- ログイン前などエリアを特定できない場合 --%>
	<c:otherwise>
		<h1><a href="https://www.gourmetcaree.jp/" title="グルメキャリー首都圏">グルメキャリー首都圏</a></h1>
	</c:otherwise>
</c:choose>

	<ul>
		<c:choose>
			<c:when test="${userDto.areaCd eq AREA_KANSAI}">
				<li id="mn_gtop" title="グルメキャリートップ"><a href="https://www.gourmetcaree.jp/">グルメキャリートップ</a></li>
			</c:when>
			<c:otherwise>
				<li id="mn_gtop" title="グルメキャリートップ"><a href="https://www.gourmetcaree.jp/">グルメキャリートップ</a></li>
			</c:otherwise>
		</c:choose>
	</ul>

</div>
<hr />

<!-- #login_info# -->
<div id="login_info" class="clear">
<p id="info">${pageInfo}</p>
<p id="date"><gt:todayDate /></p>
</div>
<!-- #login_info# -->

<hr />

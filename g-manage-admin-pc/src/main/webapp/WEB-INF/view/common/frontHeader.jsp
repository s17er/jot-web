<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<c:set var="AREA_CD_SHUTOKEN" value="<%=MAreaConstants.AreaCd.SHUTOKEN_AREA %>" />
	<div id="header" class="clear">

	<c:choose>
		<c:when test="${not empty h1Text}">
			<h1>${h1Text}</h1>
		</c:when>
		<c:when test="${areaCd eq AREA_CD_SHUTOKEN}">
			<h1>飲食求人・飲食店就職はグルメキャリー</h1>
		</c:when>
		<c:otherwise>
			<h1>飲食求人・飲食店就職はグルメキャリー</h1>
		</c:otherwise>
	</c:choose>

	<div id="head_left" class="clear">
		 <p>飲食・フードサービス専門求人情報<br />目と耳とココロで厳選した“働きたい飲食店”を掲載！</p>
		<h2 title="飲食求人 グルメキャリー"><a href="#"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/logo.gif" alt="飲食求人業界専門の情報誌グルメキャリー" /></a></h2>
	</div>
	<div id="head_right">
		<ul id="mn_site">
			<li><a href="#" title="首都圏の飲食店求人"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_kansai.gif" alt="首都圏版の飲食店求人" /></a></li>
			<li><a href="#" title="東海版"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_tokai.gif" alt="東海版" /></a></li>
			<%--
			<c:choose>
				<c:when test="${areaCd eq AREA_CD_SHUTOKEN}">
					<li><a href="#" title="関西版"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_kansai.gif" alt="関西版" /></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="#" title="東海版"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_tokai.gif" alt="東海版" /></a></li>
				</c:otherwise>
			</c:choose>
			--%>
			<li><a href="#" title="新卒"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_shinsotsu.gif" alt="新卒" /></a></li>
		</ul>
		<ul id="mn_sub">
			<li class="ssl">SSL<a href="#" title="セキュアモードとは、内容を暗号化して送信するモードです。"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/img_question.gif" alt="セキュアモードとは、内容を暗号化して送信するモードです。" /></a>
			&nbsp;<span id="ssl_btn"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_ssl_onoff.gif"  /><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_ssl_offon.gif" /></span>&nbsp;|</li><!--
			<li><a href="#" title="サイトマップ">サイトマップ</a>|</li><!--
		--><li><a href="#" title="初めての方へ">初めての方へ</a>|</li><!--
		--><li><a href="#" title="全路線・駅一覧">全路線・駅一覧</a></li>
		</ul>
	</div>
</div>
<hr />

<div id="navigation" class="clear">
	<ul id="mn_main">
	<li id="mm01"><a href="#" title="トップページ"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/mn_top.gif" alt="トップページ" /></a></li>

	<li id="mm02"><a href="#" title="MYページ"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/mn_mypage.gif" alt="MYページ" /></a></li>

	<li id="mm03"><a href="#" title="仕事を探す"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/mn_search.gif" alt="仕事を探す" /></a></li>
	<li id="mm04"><a href="#" title="巻頭企画紹介"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/mn_contents.gif" alt="巻頭企画紹介" /></a></li>
	<li id="mm05"><a href="#" title="転職の心得"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/mn_knowledge.gif" alt="転職の心得" /></a></li>
	</ul>

	<p>求人更新日&nbsp;XXXX/XX/XX&nbsp;&raquo;&nbsp;次回更新日&nbsp;XXXX/XX/XX</p>
</div>
<hr />
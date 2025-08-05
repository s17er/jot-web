<%@page pageEncoding="UTF-8"%>
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
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<header id="top" class="clearfix">
			<c:choose>
				<c:when test="${not empty h1Text}">
					<h1>${h1Text}</h1>
				</c:when>
				<c:otherwise>
					<h1>原稿名&nbsp;｜ <a href="#">飲食求人・レストラン求人・飲食店転職・就職　グルメキャリー首都圏版</a></h1>
				</c:otherwise>
			</c:choose>
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
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

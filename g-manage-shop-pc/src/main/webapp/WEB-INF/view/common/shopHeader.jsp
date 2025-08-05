<%@page pageEncoding="UTF-8"%>
<c:set var="SSL_DOMAIN" value="${common['gc.sslDomain']}" scope="request" />
<div id="header">
	<div class="logo pc_none">
		<a href="${f:url('/top/menu/')}" title="グルメキャリー 顧客管理画面"><span></span></a>
	</div>
	<ul>
		<li id="mn_guide" title="ご利用ガイド">
			<a href="${SSL_DOMAIN}/help/shop-sn/shopHelp.html" target="_blank"><span>ご利用ガイド</span><span class="material-icons">quiz</span></a>
		</li>
		<li id="mn_price" title="オプション料金">
			<a href="${f:url('/price')}"><span>オプション料金</span><span class="material-icons">currency_yen</span></a>
		</li>
		<li id="mn_help" title="お問い合わせ">
			<a href="${f:url('/contact')}"><span>お問い合わせ</span><span class="material-icons">mail</span></a>
		</li>
		<li id="mn_logout" title="ログアウト">
			<a href="${f:url('/login/logout/')}" onclick="if(!confirm('ログアウトしてもよろしいですか?')) {return false;}"><span>ログアウト</span><span class="material-icons">logout</span></a>
		</li>
	</ul>
</div>
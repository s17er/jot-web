<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<gt:informationTag name="information" areaCd="<%=MAreaConstants.AreaCd.SHUTOKEN_AREA%>" managementScreenKbn="<%=MTypeConstants.ManagementScreenKbn.ADMIN_SCREEN%>"/>

<!-- #main# -->
<div id="main">

	<!-- #wrap_information# -->
	<div id="wrap_information">
		<h2 title="グルメキャリーからのお知らせ" class="infoTitle">グルメキャリーからのお知らせ</h2>
		<hr />
		<div class="autoHeight">
			${information}&nbsp;
		</div>
	</div>
	<!-- #wrap_information# -->
	<hr />
	<!-- メニュー -->
	<div class="top_menu">
		<ul class="menu_list01 line2 clear">
			<li>
				<h2>WEBデータ管理</h2>
				<p>求人原稿データの登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/webdata/input/')}" title="WEBデータ登録">WEBデータ登録</a></li>
					<li><a href="${f:url('/webdata/list/')}" title="WEBデータ管理">WEBデータ管理</a></li>
				</ul>
			</li>
			<li>
				<h2 class="ico02">顧客データ</h2>
				<p>企業情報＆担当者データの登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
				<c:if test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
					<li class="regist"><a href="${f:url('/customer/input/')}" title="顧客データ登録">顧客データ登録</a></li>
				</c:if>
					<li><a href="${f:url('/customer/list/')}" title="顧客データ管理">顧客データ管理</a></li>
				</ul>
			</li>
			<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
			<li>
				<h2 class="ico05">応募データ</h2>
				<p>求人検索からの応募データを表示することが出来ます。</p>
				<ul class="btn">
					<li><a href="${f:url('/application/list/')}" title="応募データ管理">応募データ</a></li>
					<li><a href="${f:url('/preApplication/list/')}" title="プレ応募データ管理">プレ応募データ</a></li>
					<li><a href="${f:url('/ipPhoneHistory/list/')}" title="電話応募データ管理">電話応募データ</a></li>
				</ul>
			</li>
			</c:if>
		</ul>
		<ul class="menu_list01 line2 clear">
			<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALE or userDto.authLevel eq AUTH_LEVEL_SALES}">
			<li>
				<h2 class="ico04">会員データ</h2>
				<p>会員データの表示、処理を行うことが出来ます。</p>
				<ul class="btn">
					<li><a href="${f:url('/member/list/')}" title="会員データ管理">会員データ管理</a></li>
					<li><a href="${f:url('/tempMember/list/')}" title="仮会員データ管理">仮会員データ管理</a></li>
					<li><a href="${f:url('/juskillMember/list/')}" title="人材紹介サービス管理">人材紹介サービス管理</a></li>
				</ul>
			</li>
			</c:if>
			<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
			<li>
				<h2 class="ico05">その他応募データ</h2>
				<p>その他ページからの応募データを表示することが出来ます。</p>
				<ul class="btn">
					<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALES}">
						<li><a href="${f:url('/advancedRegistrationMember/list/')}" title="合同企業説明会エントリーデータ管理">合同企業説明会<br />エントリーデータ管理</a></li>
					</c:if>
					<li><a href="${f:url('/observateApplication/list/')}" title="質問・店舗見学データ管理">質問・店舗見学<br />データ管理</a></li>
				</ul>
			</li>
			</c:if>
			<li>
				<h2 class="ico05">設定</h2>
				<p>設定の変更はこちらから<br>&nbsp;</p>
				<ul class="btn">
					<li><a href="${f:url('/changePassword/edit/')}" title="パスワード変更">パスワード変更</a></li>
				</ul>
			</li>
		</ul>
		<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
		<div class="menu_list02">
			<ul class="clear">
				<li>
					<p class="btn"><a href="${f:url('/report/list/')}" title="レポート管理">レポート管理</a></p>
					<p>WEBデータの状態を表示することが出来ます。</p>
				</li>
				<li>
					<p class="btn"><a href="${f:url('/maintenance/menu')}" title="マスタメンテナンス">マスタメンテナンス</a></p>
					<p>号数データ、特集データ、営業担当者、会社の管理、登録、編集、削除が出来ます。</p>
				</li>
				<li>
					<p class="btn"><a href="${f:url('/mailMag/')}" title="メルマガ管理">メルマガ管理</a></p>
					<p>送信したメルマガを表示することが出来ます。</p>
				</li>
			</ul>
			<ul class="clear">
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
				<li>
					<p class="btn"><a href="${common['gc.shop-pc.top']}/masquerade/login/login/" title="なりすましログイン" target="_blank">なりすましログイン</a></p>
					<p>顧客になりすまして管理画面にログインすることが出来ます。</p>
				</li>
				</c:if>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_AGENCY}">
				<li>
					<p class="btn"><a href="${f:url('/shopMmt/')}" title="店舗データ管理">店舗データ管理</a></p>
					<p>掲載中の店舗データのCSV出力を行うことが出来ます。</p>
				</li>
				</c:if>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
 				<li>
					<p class="btn"><a href="${f:url('/information/menu/')}" title="お知らせ管理">お知らせ管理</a></p>
					<p>各画面に表示させるお知らせの編集が出来ます。</p>
				</li>
				</c:if>
			</ul>
		</div>
		</c:if>

		<c:if test="${userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALES}">
		<div class="menu_list02">
			<ul class="clear">
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALES}">
				<li>
					<p class="btn"><a href="${common['gc.shop-pc.top']}/masquerade/login/login/" title="なりすましログイン" target="_blank">なりすましログイン</a></p>
					<p>顧客になりすまして管理画面にログインすることが出来ます。</p>
				</li>
				</c:if>
			</ul>
		</div>
		</c:if>
	</div>
</div>
<!-- #main# -->

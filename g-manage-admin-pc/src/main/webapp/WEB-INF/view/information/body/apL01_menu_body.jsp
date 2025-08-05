<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_menu# -->
	<div class="wrap_menu">

		<table width="780" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<th>
					<a href="${f:url(adminInfoPath)}" title="運営側管理"><img src="${ADMIN_CONTENS}/images/information/btn_adminInformation.gif" alt="運営側管理" /></a></th>
				<td>運営側で表示されるお知らせの編集が出来ます。</td>
			</tr>
			<tr>
				<th><a href="${f:url(shopInfoPath)}" title="店舗側管理"><img src="${ADMIN_CONTENS}/images/information/btn_shopInformation.gif" alt="店舗側管理" /></a></th>
				<td>店舗側で表示されるお知らせの編集が出来ます。</td>
			</tr>
			<tr>
				<th><a href="${f:url(mypageInfoPath)}" title="MYページ管理"><img src="${ADMIN_CONTENS}/images/information/btn_mypageInformation.gif" alt="MYページ管理" /></a></th>
				<td>MYページで表示されるお知らせの編集が出来ます。</td>
			</tr>
		</table>
	</div>
	<!-- #wrap_menu# -->
	<hr />
</div>
<!-- #main# -->

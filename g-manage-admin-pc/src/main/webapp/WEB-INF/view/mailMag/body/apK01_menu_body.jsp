<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<!-- #mn_mailMag# -->
	<div id="mn_mailMag" class="wrap_menu">
		<h2 class="title application">メルマガ管理</h2>
		<hr />
		<table width="780" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<th><a href="${f:url('/mailMag/list/')}" title="メルマガ一覧"><img src="${ADMIN_CONTENS}/images/mailMag/btn_mailMagList.gif" alt="メルマガ一覧" /></a></th>
				<td>メルマガ一覧</td>
			</tr>
			<tr>
				<th><a href="${f:url('/mailMagOption/headerInput/')}" title="メルマガヘッダメッセージ"><img src="${ADMIN_CONTENS}/images/mailMag/btn_mailMagHeadMsg.gif" alt="メルマガヘッダメッセージ" /></a></th>
				<td>メルマガヘッダメッセージ</td>
			</tr>
			<tr>
				<th><a href="${f:url('/mailMagOption/list/')}" title="メルマガヘッダメッセージ一覧"><img src="${ADMIN_CONTENS}/images/mailMag/btn_mailMagHeadList.gif" alt="メルマガヘッダメッセージ一覧" /></a></th>
				<td>メルマガヘッダメッセージ一覧</td>
			</tr>
		</table>
	</div>
	<!-- #mn_mailMag# -->
	<hr />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

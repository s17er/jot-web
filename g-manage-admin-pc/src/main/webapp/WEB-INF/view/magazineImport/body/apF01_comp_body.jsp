<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle1)}" class="title" id="${f:h(pageTitleId1)}">${f:h(pageTitle1)}</h2>
	<hr />
	<p>${f:h(defaultMsg)}</p>
	<hr />
	<!-- #wrap_shift# -->
	<div id="wrap_shift">
		<table cellpadding="0" cellspacing="0" border="0" width="880" class="state_table">
			<tr>
				<th width="80">取込誌面号数</th>
				<td width="80" class="nobdr_left">${f:h(result.magazineVolume)}</td>
				<th width="80" class="nobdr_left">設定WEB号数</th>
				<td class="nobdr_left">
					<gt:volumeList name="volumeList" limitValue="1" />
					${f:label(result.webVolume, volumeList, 'value', 'label')}&nbsp;
				</td>
				<th width="80" class="nobdr_left">移行完了件数</th>
				<td width="80"class="nobdr_left">${f:h(result.migrationCount)}</td>
			</tr>
		</table>
		<hr />
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table btm_margin list_table stripe_table">
			<tr>
				<th width="15" class="posi_center">No.</th>
				<th width="80" class="posi_center">誌面原稿番号</th>
				<th width="60" class="posi_center">原稿番号</th>
				<th>原稿名</th>
				<th width="80">営業担当者名</th>
				<th width="110" class="posi_center bdrs_right">誌面最終更新日時</th>
			</tr>
			<c:forEach items="${result.infoList}" var="info" varStatus="idx">
			<tr>
				<td class="posi_center">${idx.index + 1}</td>
				<td class="posi_center">${info.magazineManuscriptNo}</td>
				<td class="posi_center">${info.id}</td>
				<td>${info.manuscriptName}</td>
				<td class="posi_center">${info.salesName}&nbsp;</td>
				<td class="posi_center bdrs_right"><fmt:formatDate value="${info.magazineLastUpdate}" pattern="yyyy/MM/dd HH:mm" /></td>
			</tr>
			</c:forEach>
		</table>
	</div>
	<!-- #wrap_shift# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->
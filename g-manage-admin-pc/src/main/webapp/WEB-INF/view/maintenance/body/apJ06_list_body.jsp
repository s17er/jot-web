<%@page pageEncoding="UTF-8"%>

<s:form action="/advancedRegistration/delete" styleId="deleteForm">
	<html:hidden property="id" styleId="deleteId" value=""/>
</s:form>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table btm_margin list_table">
		<tr>
			<th width="20" class="posi_center" rowspan="2">No.</th>
			<th width="370" class="bdrd_bottom">表示名</th>
			<th width="100" class="bdrd_bottom">開始日時</th>
			<th colspan="3" width="300" class="bdrd_bottom bdrs_right">アドレス</th>
		</tr>
		<tr>
			<th>開催年名(WEBデータ登録時の名称)</th>
			<th>終了日時</th>
			<th width="40" class="posi_center">CSV</th>
			<th width="40" class="posi_center">編集</th>
			<th width="40" class="posi_center bdrs_right">削除</th>
		</tr>

		<c:forEach var="dto" items="${registrationList}" varStatus="status">
			<tr>
				<%-- No --%>
				<td class="posi_center" rowspan="2">${f:h(status.count)}</td>
				<%-- 表示名 --%>
				<td class="bdrd_bottom">${f:h(dto.advancedRegistrationName)}</td>
				<%-- 開始日時 --%>
				<td class="bdrd_bottom">${f:h(dto.termStartDatetimeStr)}</td>
				<%-- アドレス --%>
				<td class="bdrd_bottom bdrs_right" colspan="3">${f:h(dto.inputPath)}<br />${f:h(dto.editPath)}</td>
			</tr>
			<tr>
				<%-- 開催年名(WEBデータ登録時の名称) --%>
				<td>${f:h(dto.advancedRegistrationShortName)}</td>
				<%-- 終了日時 --%>
				<td>${f:h(dto.termEndDatetimeStr)}</td>
				<%-- CSV --%>
				<td class="posi_center">
					<c:set var="csvUrl" value="/advancedRegistration/csv/tempSignIn/${dto.id}"></c:set>
					<html:button property="" value="CSV出力" onclick="outputCsv('${f:url(csvUrl)}', '${f:h(dto.advancedRegistrationShortName)}');" />
				</td>
				<%-- 編集 --%>
				<td class="posi_center"><a href="${f:url(gf:makePathConcat1Arg('/advancedRegistration/edit', dto.id))}">編集</a></td>
				<%-- 削除 --%>
				<td class="bdrs_right posi_center"><a href="${f:url(gf:makePathConcat1Arg('/advancedRegistration/delete', dto.id))}" >削除</a></td>
			</tr>
		</c:forEach>
	</table>
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>
<!-- #main# -->


<script type="text/javascript">
<!--
	var outputCsv = function(url, name) {
		if (confirm(name + " のCSVをダウンロードします。よろしいですか？")) {
			location.href=url;
		}
	};
//-->
</script>

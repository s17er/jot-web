<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.core.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.disableDoubleSubmit.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<script type="text/javascript">
<!--
/*@cc_on _d=document;eval('var document=_d')@*/
$(function() {
	$("#magazineMigrationForm").disableDoubleSubmit(30000);
});
// -->
</script>

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

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}" styleId="magazineMigrationForm">

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">取込誌面号数&nbsp;<span class="attention">※必須</span></th>
					<td class="release"><html:text property="magazineVolume" size="5" styleClass="disabled" styleId="firstFocus" /></td>
				</tr>
				<tr>
					<th class="bdrs_bottom">設定WEB号数&nbsp;<span class="attention">※必須</span></th>
					<td class="release bdrs_bottom">
					<gt:volumeList name="volumeList" limitValue="1" blankLineLabel="${common['gc.pullDown']}" />
					<html:select property="webVolume">
						<html:optionsCollection name="volumeList" />
					</html:select></td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit value="データ移行" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="importSubmitBtn"  />
			</div>
			<hr />
		</s:form>

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table btm_margin list_table stripe_table">
			<tr>
				<th width="80" class="posi_center">取込誌面号数</th>
				<th width="280">設定WEB号数</th>
				<th width="100" class="posi_center">移行日時</th>
				<th>移行担当者</th>
				<th width="80" class="posi_center bdrs_right">移行データ数</th>
			</tr>
		<gt:volumeList name="volumeList" limitValue="1" />
		<c:forEach items="${historyList}" var="history">
			<tr>
				<td class="posi_center">${f:h(history.magazineVolume)}</td>
				<td width="280">${f:label(history.volumeId, volumeList, 'value', 'label')}</td>
				<td class="posi_center"><fmt:formatDate value="${f:date(history.importDateTime, 'yyyy-MM-dd HH:mm')}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td>${f:h(history.importUserName)}</td>
				<td class="posi_center bdrs_right">${f:h(history.importCount)}</td>
			</tr>
		</c:forEach>
		</table>
	</div>
	<!-- #wrap_form# -->
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

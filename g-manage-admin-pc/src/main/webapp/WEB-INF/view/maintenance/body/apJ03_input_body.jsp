<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<script type="text/javascript">
<!--
	// 「DatePicker」の搭載
	$(function(){
		$("#postStartDate").datepicker();
		$("#postEndDate").datepicker();
	});
// -->
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //編集時、データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<html:hidden property="hiddenId" />
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">特集名&nbsp;<span class="attention">※必須</span></th>
					<td class="release"><html:text property="specialName" maxlength="35" size="62" styleId="firstFocus"/><br />
					<span class="attention">※35文字以内</span></td>
				</tr>
				<tr>
					<th width="150">表示名&nbsp;<span class="attention">※必須</span></th>
					<td class="release"><html:text property="displayName" size="80" /></td>
				</tr>
				<tr>
					<th>掲載開始日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="postStartDate" size="10" styleId="postStartDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="postStartHour" size="2" styleId="postStartHour" styleClass="disabled" maxlength="2" />&nbsp;：&nbsp;
						<html:text property="postStartMinute" size="2" styleId="postStartMinute" styleClass="disabled" maxlength="2" />
					</td>
				</tr>
				<tr>
					<th>掲載終了日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="postEndDate" size="10" styleId="postEndDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="postEndHour" size="2" styleId="postEndHour" styleClass="disabled" maxlength="2" />&nbsp;：&nbsp;
						<html:text property="postEndMinute" size="2" styleId="postEndMinute" styleClass="disabled" maxlength="2" />
					</td>
				</tr>
				<tr>
					<th>画面表示エリア&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<gt:areaList name="areaList" />
						<c:forEach items="${areaList}" var="t">
							<html:multibox property="areaCd" value="${t.value}" styleId="area_cd_${t.value}" />
							<label for="area_cd_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<c:choose>
					<c:when test="${pageKbn eq PAGE_INPUT}">
						<tr>
							<th class="bdrs_bottom">説明</th>
							<td class="bdrs_bottom"><html:textarea property="explanation" cols="60" rows="10" ></html:textarea></td>
						</tr>
					</c:when>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<tr>
							<th>説明</th>
							<td><html:textarea property="explanation" cols="60" rows="10" ></html:textarea></td>
						</tr>
						<tr>
							<th class="bdrs_bottom">アドレス</th>
							<td class="bdrs_bottom">${f:h(url)}</td>
						</tr>
					</c:when>
				</c:choose>

			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
				</c:choose>
			</div>

		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>
<!-- #main# -->

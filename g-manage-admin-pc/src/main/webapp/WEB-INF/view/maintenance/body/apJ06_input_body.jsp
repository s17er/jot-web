<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<script type="text/javascript">
<!--
	// 「DatePicker」の搭載
	$(function(){
		$("#termStartDate").datepicker();
		$("#termEndDate").datepicker();
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

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="230">開催エリア&nbsp;<span class="attention">※必須</span></th>
					<td>
						<gt:areaList name="areaList" authLevel="${userDto.authLevel}" />
						<html:select property="areaCd" styleId="areaCd">
							<html:optionsCollection name="areaList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th width="230">表示名&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="advancedRegistrationName" />&nbsp;</td>
				</tr>
				<tr>
					<th>開催年名(WEBデータ登録時の名称)&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="advancedRegistrationShortName" />&nbsp;</td>
				</tr>
				<tr>
					<th>開始日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="termStartDate" size="10" styleId="termStartDate" styleClass="disabled" maxlength="10"></html:text>&nbsp;
						<html:text property="termStartHour" size="2" styleId="termStartHour" styleClass="disabled" maxlength="2"></html:text>&nbsp;：&nbsp;
						<html:text property="termStartMinute" size="2" styleId="termStartMinute" styleClass="disabled" maxlength="2"></html:text>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">終了日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release bdrs_bottom">
						<html:text property="termEndDate" size="10" styleId="termEndDate" styleClass="disabled" maxlength="10"></html:text>&nbsp;
						<html:text property="termEndHour" size="2" styleId="termEndHour" styleClass="disabled" maxlength="2"></html:text>&nbsp;：&nbsp;
						<html:text property="termEndMinute" size="2" styleId="termEndMinute" styleClass="disabled" maxlength="2"></html:text>
					</td>
				</tr>
			</table>
			<hr />
			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
				<c:if test="${pageKbn eq PAGE_EDIT}">
					<input type="button" value="戻る"  onclick="location.href='${f:url(actionListPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
				</c:if>
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

</div>
<!-- #main# -->

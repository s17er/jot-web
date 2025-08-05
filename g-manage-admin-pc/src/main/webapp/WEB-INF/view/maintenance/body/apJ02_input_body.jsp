<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<script type="text/javascript">
<!--
	// 「DatePicker」の搭載
	$(function(){
		$("#deadlineDate").datepicker();
		$("#fixedDeadlineDate").datepicker();
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
			<c:choose>
				<c:when test="${pageKbn eq PAGE_EDIT}">
					<tr>
						<th width="150">号数</th>
						<td>${f:h(volume)}&nbsp;</td>
					</tr>
					<tr>
						<th>エリア</th>
						<td>${f:h(areaName)}&nbsp;</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<th width="150">エリア&nbsp;<span class="attention">※必須</span></th>
						<td>
							<gt:areaList name="area" />
							<html:select property="areaCd" styleId="firstFocus">
								<html:optionsCollection name="area" />
							</html:select>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
				<tr>
					<th>締切日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="deadlineDate" size="10" styleId="deadlineDate" styleClass="disabled" maxlength="10"></html:text>&nbsp;
						<html:text property="deadlineHour" size="2" styleId="deadlineHour" styleClass="disabled" maxlength="2"></html:text>&nbsp;：&nbsp;
						<html:text property="deadlineMinute" size="2" styleId="deadlineMinute" styleClass="disabled" maxlength="2"></html:text>
					</td>
				</tr>
				<tr>
					<th>確定締切日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="fixedDeadlineDate" size="10" styleId="fixedDeadlineDate" styleClass="disabled" maxlength="10"></html:text>&nbsp;
						<html:text property="fixedDeadlineHour" size="2" styleId="fixedDeadlineHour" styleClass="disabled" maxlength="2"></html:text>&nbsp;：&nbsp;
						<html:text property="fixedDeadlineMinute" size="2" styleId="fixedDeadlineMinute" styleClass="disabled" maxlength="2"></html:text>
					</td>
				</tr>
				<tr>
					<th>掲載開始日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="postStartDate" size="10" styleId="postStartDate" styleClass="disabled" maxlength="10"></html:text>&nbsp;
						<html:text property="postStartHour" size="2" styleId="postStartHour" styleClass="disabled" maxlength="2"></html:text>&nbsp;：&nbsp;
						<html:text property="postStartMinute" size="2" styleId="postStartMinute" styleClass="disabled" maxlength="2"></html:text>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">掲載終了日時&nbsp;<span class="attention">※必須</span></th>
					<td class="release bdrs_bottom">
						<html:text property="postEndDate" size="10" styleId="postEndDate" styleClass="disabled" maxlength="10"></html:text>&nbsp;
						<html:text property="postEndHour" size="2" styleId="postEndHour" styleClass="disabled" maxlength="2"></html:text>&nbsp;：&nbsp;
						<html:text property="postEndMinute" size="2" styleId="postEndMinute" styleClass="disabled" maxlength="2"></html:text>
					</td>
				</tr>
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

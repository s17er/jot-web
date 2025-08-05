<%@page pageEncoding="UTF-8"%>


<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE}">
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
	<c:if test="${existDataFlg}">

		<!-- #wrap_form# -->
		<div id="wrap_form">
			<s:form action="${f:h(actionPath)}">
				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
					<tr>
						<th width="230">開催エリア&nbsp;</th>
						<td>
							<gt:areaList name="areaList" authLevel="${userDto.authLevel}" />
							${f:label(areaCd, areaList, 'value', 'label')}&nbsp;
						</td>
					</tr>
					<tr>
						<th width="230">表示名&nbsp;</th>
						<td>${f:h(advancedRegistrationName)}&nbsp;</td>
					</tr>
					<tr>
						<th>開催年名(WEBデータ登録時の名称)&nbsp;</th>
						<td>${f:h(advancedRegistrationShortName)}&nbsp;</td>
					</tr>
					<tr>
						<th>開始日時&nbsp;</th>
						<td class="release">
							${f:h(termStartDatetimeStr)}
						</td>
					</tr>
					<tr>
						<th class="bdrs_bottom">終了日時&nbsp;</th>
						<td class="release bdrs_bottom">
							${f:h(termEndDatetimeStr)}
						</td>
					</tr>
				</table>
				<hr />
				<div class="wrap_btn">
					<c:choose>
						<c:when test="${pageKbn eq PAGE_DELETE}">
							<html:hidden property="id"/>
							<html:submit property="submit" value="削除" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
						</c:when>
						<c:otherwise>
							<html:submit property="submit" value="登録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
						</c:otherwise>
					</c:choose>
					<input type="button" value="戻る"  onclick="location.href='${f:url(actionBackPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
				</div>
			</s:form>
		</div>
		<!-- #wrap_form# -->
		<hr />
		<br />
	</c:if>

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

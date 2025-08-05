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

<% //削除時、データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<html:hidden property="hiddenId" />

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">特集名</th>
					<td>${f:h(specialName)}</td>
				</tr>
				<tr>
					<th>表示名</th>
					<td>${f:h(displayName)}</td>
				</tr>
				<tr>
					<th>掲載開始日時</th>
					<td>${f:h(postStartDatetimeStr)}</td>
				</tr>
				<tr>
					<th>掲載終了日時</th>
					<td>${f:h(postEndDatetimeStr)}</td>
				</tr>
				<tr>
					<th>画面表示エリア</th>
					<td>
						<gt:areaList name="areaList" />
						<c:forEach items="${areaCd}" var="t" varStatus="status">
							${f:label(t, areaList, 'value', 'label')}
							<c:if test="${!status.last}">,&nbsp;</c:if>
						</c:forEach>&nbsp;
					</td>
				</tr>
				<c:choose>
					<c:when test="${pageKbn eq PAGE_INPUT}">
						<tr>
							<th class="bdrs_bottom">説明</th>
							<td class="bdrs_bottom">${f:br(f:h(explanation))}&nbsp;</td>
						</tr>
					</c:when>
					<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE}">
						<tr>
							<th>説明</th>
							<td>${f:br(f:h(explanation))}&nbsp;</td>
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
				<c:choose>
					<c:when test="${pageKbn eq PAGE_INPUT or pageKbn eq PAGE_EDIT}">
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:when test="${pageKbn eq PAGE_DELETE}">
						<html:hidden property="id"/>
						<html:hidden property="version"/>
						<html:hidden property="processFlg"/>
						<html:submit property="submit" value="削 除" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
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
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>
<!-- #main# -->

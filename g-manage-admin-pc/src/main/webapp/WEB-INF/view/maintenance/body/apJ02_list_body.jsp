<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
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

	<s:form action="${f:h(actionPath)}showList" styleId="selectForm">
		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>エリア&nbsp;
					<gt:areaList name="area" />
					<html:select property="areaCd" onchange="changeMaxRow('selectForm');">
						<html:optionsCollection name="area" />
					</html:select>
				</td>
<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
				<td class="pull_down">
					<gt:maxRowList name="maxRowList" value="${common['gc.volume.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
					<html:select property="maxRowValue"  onchange="changeMaxRow('selectForm');">
						<html:optionsCollection name="maxRowList" />
					</html:select>
				</td>
</c:if>
			</tr>
		</table>
	</s:form>
	<!-- #pullDown# -->
	<hr />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table tb_margin list_table stripe_table">
		<tr>
			<th width="30" class="posi_center">号数</th>
			<th>締切日時</th>
			<th>確定締切日時</th>
			<th>掲載開始日時</th>
			<th>掲載終了日時</th>
			<th width="45" class="posi_center">編集</th>
			<th width="45" class="posi_center bdrs_right">削除</th>
		</tr>
		<% //ループ処理 %>
		<c:forEach var="m" varStatus="status" items="${list}">
			<tr>
				<td class="posi_center">${f:h(m.volume)}</td>
				<td><fmt:formatDate value="${f:date(m.deadlineDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td><fmt:formatDate value="${f:date(m.fixedDeadlineDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td><fmt:formatDate value="${f:date(m.postStartDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td><fmt:formatDate value="${f:date(m.postEndDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td class="posi_center"><a href="${f:url(m.editPath)}">編集</a></td>
				<td class="posi_center bdrs_right"><a href="${f:url(m.deletePath)}">削除</a></td>
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

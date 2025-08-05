<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title date">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<c:if test="${not empty reportList}">
		<!-- #pullDown# -->
		<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>エリア&nbsp;
					<%-- エリアも最大件数と同じメソッドを使用して変更しています。 --%>
					<gt:areaList name="areaList" />
					<html:select property="areaCd" styleId="areaCd" onchange="changeMaxRow('MaxRowSelect');">
						<html:optionsCollection name="areaList"/>
					</html:select>
				</td>
				<td class="pull_down">

				<gt:maxRowList name="maxRowList" value="${common['gc.report.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
				<html:select property="maxRow" onchange="changeMaxRow('MaxRowSelect');">
					<html:optionsCollection name="maxRowList" />
				</html:select>
				</td>
			</tr>
		</table>
		<!-- #pullDown# -->
		</s:form>
		<hr />

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table tb_margin list_table stripe_table">
			<tr>
				<th width="25" class="posi_center">号数</th>
				<th>掲載期間</th>
				<th width="40" class="posi_center">下書き</th>
				<th width="40" class="posi_center">承認中</th>
				<th width="50" class="posi_center">掲載待ち</th>
				<th width="40" class="posi_center">掲載中</th>
				<th width="50" class="posi_center">掲載終了</th>
				<th width="50" class="posi_center">合計</th>
				<th width="45" class="posi_center bdrs_right">詳細</th>
			</tr>
			<c:forEach var="dto" items="${reportList}">
				<tr>
					<td class="posi_center">${f:h(dto.volume)}</td>
					<td>
						<fmt:formatDate value="${dto.postStartDatetime}" pattern="yyyy/MM/dd HH:mm" />
						 ～
						<fmt:formatDate value="${dto.postEndDatetime}" pattern="yyyy/MM/dd HH:mm" />
					</td>
					<td class="posi_center">${f:h(dto.draftCount)}件</td>
					<td class="posi_center">${f:h(dto.reqApprovalCount)}件</td>
					<td class="posi_center">${f:h(dto.postWaitCount)}件</td>
					<td class="posi_center">${f:h(dto.postDuringCount)}件</td>
					<td class="posi_center">${f:h(dto.postEndCount)}件</td>
					<td class="posi_center">${f:h(dto.totalCount)}件</td>
					<td class="posi_center bdrs_right"><a href="${f:url(dto.detailPath)}">詳細</a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
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

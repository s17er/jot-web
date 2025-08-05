<%@page pageEncoding="UTF-8"%>
<gt:areaList name="areaList"/>
<gt:companyList name="companyList" />
<gt:salesList name="salesList" />

<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title date">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<c:if test="${not empty reportList}">
	<s:form action="${f:h(actionPath)}">

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
			<tr>
				<th width="40" class="posi_center">エリア</th>
				<th width="30" class="posi_center">号数</th>
				<th>締切日時</th>
				<th>確定締切日時</th>
				<th>掲載期間</th>
				<th width="50" class="posi_center bdrs_right">合計</th>
			</tr>
			<tr>
				<td class="posi_center">${f:label(areaCd, areaList, 'value', 'label')}&nbsp;</td>
				<td class="posi_center">${volume}</td>
				<td><fmt:formatDate value="${deadlineDatetime}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td><fmt:formatDate value="${fixedDeadlineDatetime}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td><fmt:formatDate value="${postStartDatetime}" pattern="yyyy/MM/dd HH:mm" />
					 ～
					 <fmt:formatDate value="${postEndDatetime}" pattern="yyyy/MM/dd HH:mm" />
				</td>
				<td class="posi_center bdrs_right">${f:h(totalCount)}件</td>
			</tr>
		</table>
		<hr />

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table top_margin list_table stripe_table">
			<tr>
				<th>会社</th>
				<th>営業担当者</th>
				<th width="50" class="posi_center">下書き</th>
				<th width="50" class="posi_center">承認中</th>
				<th width="50" class="posi_center">掲載待ち</th>
				<th width="50" class="posi_center">掲載中</th>
				<th width="50" class="posi_center">掲載終了</th>
				<th width="50" class="posi_center bdrs_right">合計</th>
			</tr>

			<c:forEach var="dto" items="${reportList}">
				<tr>
					<td>${f:label(dto.companyId, companyList, 'value', 'label')}&nbsp;</td>
					<td>
						<c:choose>
						<c:when test="${dto.salesId ne 0}">
							${f:label(dto.salesId, salesList, 'value', 'label')}&nbsp;
						</c:when>
						<c:otherwise>
							<span style="color:#CC0000;">(未登録)</span>
						</c:otherwise>
						</c:choose>
					</td>
					<td class="posi_center">${f:h(dto.draftCount)}件</td>
					<td class="posi_center">${f:h(dto.reqApprovalCount)}件</td>
					<td class="posi_center">${f:h(dto.postWaitCount)}件</td>
					<td class="posi_center">${f:h(dto.postDuringCount)}件</td>
					<td class="posi_center">${f:h(dto.postEndCount)}件</td>
					<td class="posi_center bdrs_right">${f:h(dto.totalCount)}件</td>
				</tr>
			</c:forEach>
		</table>
		<hr />

		<div class="wrap_btn">
			<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
		</div>

	</s:form>
	</c:if>

	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

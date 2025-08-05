<%@page pageEncoding="UTF-8"%>
<gt:areaList name="areaList" />
<gt:companyList name="companyList" />
<gt:salesList name="salesList" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript">
<!--
	//非表示要素の表示
	function showTbl(){
		$("#wrap_result").css("display","block");
	}

// -->
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle1)}" class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<s:form action="${f:h(actionPath)}">
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
			<tr>
				<th width="20" class="posi_center">選択</th>
				<th width="20" class="posi_center">No.</th>
				<th class="posi_center">顧客ID</th>
				<th>顧客名</th>
				<th>担当者</th>
				<th width="35" class="posi_center">エリア</th>
				<th>電話番号</th>
				<th>メールアドレス</th>
				<th class="bdrs_right">担当会社：営業担当者</th>
			</tr>

			<c:forEach var="dto" items="${list}" varStatus="status">
				<tr>
					<td class="posi_center"><input type="checkbox" name="" checked="checked" disabled="disabled" /></td>
					<td class="posi_center"><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>
					<td class="posi_center">${f:h(dto.id)}</td>
					<td>${f:h(dto.customerName)}</td>
					<td>${f:h(dto.contactName)}</td>
					<td class="posi_center">
						<c:forEach items="${dto.mailMagazineAreaCdList}" var="t" varStatus="s">
							${f:label(t, areaList, 'value', 'label')}
							<c:if test="${!s.last}"><br></c:if>
						</c:forEach></td>
					<td>${f:h(dto.phoneNo)}</td>
					<td class="${classStr}">
						${f:h(dto.mainMail)}
						<c:forEach items="${dto.subMailList}" var="subMail" varStatus="status">
							<c:if test="${status.first}"><br></c:if>${f:h(subMail)}<c:if test="${!status.last}"><br></c:if>
						</c:forEach>
					</td>
					<td class="bdrs_right">
						<c:forEach var="compSalesDto" items="${dto.companySalesDtoList}" varStatus="status">
							${f:label(compSalesDto.companyId, companyList, 'value', 'label')}：&nbsp;${f:label(compSalesDto.salesId, salesList, 'value', 'label')}
							<c:if test="${status.last ne true}">
								<br />
							</c:if>
						</c:forEach>
					</td>
				</tr>
			</c:forEach>
		</table>
		<hr />

		<div class="wrap_btn">
			<c:if test="${existDataFlg}">
				<html:submit property="conf" value="メルマガ作成" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
			</c:if>
			<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
		</div>
	</s:form>
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
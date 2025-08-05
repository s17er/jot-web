<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/tablesorter/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/tablesorter/jquery.tablesorter.widgets.min.js"></script>
<script>
$(function(){
	$('.shop_table').tablesorter({
		headers:{
			0:{sorter:false},
		},
	});
	// 店舗の全チェック
	$(document).on('change', '#allCheck', function() {
		$('input[name=shopListId]').prop('checked', this.checked);
	});

});
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:if test="${not empty navigationPath3}">
		<li class="noDisplay">&nbsp;</li>
		<li><a href="${f:url(navigationPath3)}" class="reverseColor" style="color: #FFF;">${f:h(navigationText3)}</a></li>
		</c:if>
	</ul>
	<!-- #navigation# -->
	<gt:convertToCustomerName customerId="${customerId}" name="customName" />
	<h2 title="${f:h(pageTitle1)}" class="title shop">${f:h(pageTitle1)}：${gf:replaceStr(customName, common['gc.shopList.customerName.trim.length'], common['gc.replaceStr'])}</h2>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div>
		<s:form action="${f:h(actionPath)}" styleId="${targetForm}">

		 <c:if test="${not empty shopListDtoList}">
			<table class="table table-bordered cmn_table list_table shop_table">
				<tr>
				<th width="150" class="bdrs_right bdrs_bottom">
					ラベル名
				</th>
				<td class="bdrs_right bdrs_bottom">
					<html:text property="labelName" size="80" />
				</td>
				</tr>
			</table>
			<br><br>
			<table class="table-bordered cmn_table list_table shop_table">
			    <thead>
					<tr>
						<th width="20"><input type="checkbox" id="allCheck"></th>
						<th>店舗名</th>
						<th>都道府県</th>
						<th>市区</th>
						<th width="140">業態1</th>
						<th width="140" class="bdrs_right bdrs_bottom">業態2</th>
					</tr>
			    </thead>
			    <tbody>
			    	<c:forEach items="${shopListDtoList}" var="shopListDto">
						<tr>
							 <td class="posi_center bdrs_right">
								<html:multibox property="shopListId" value="${f:h(shopListDto.id)}" />
							</td>
							<td>${f:h(shopListDto.shopName)}</td>
							<td>${f:h(shopListDto.prefecturesName)}</td>
							<td>${f:h(shopListDto.cityName)}</td>
							<td width="140">${f:h(shopListDto.industryKbn1Label)}</td>
							<td width="140" class="bdrs_right bdrs_bottom">${f:h(shopListDto.industryKbn2Label)}</td>
						</tr>
			    	</c:forEach>
			    </tbody>
			</table>

			<html:hidden property="customerId" value="${customerId}"/>
		</c:if>

			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="conf_btn" />
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="back_btn" />
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
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>
</div>
<!-- #main# -->
<nav class="webdate_menu">
<ul>
</ul>
</nav>

<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>
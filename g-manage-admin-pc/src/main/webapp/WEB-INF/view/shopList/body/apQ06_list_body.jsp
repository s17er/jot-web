<%@page pageEncoding="UTF-8"%>

<script type="text/javascript"
	src="${ADMIN_CONTENS}/js/preview.js"></script>

<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<gt:convertToCustomerName customerId="${customerId}" name="customName" />
	<h2 title="${f:h(pageTitle1)}" class="title shop">${f:h(pageTitle1)}：${gf:replaceStr(customName, common['gc.shopList.customerName.trim.length'], common['gc.replaceStr'])}</h2>
	<hr />
	<html:errors />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<s:form action="${f:h(deletePath)}" styleId="listForm">
		<table class="table-bordered cmn_table list_table">
			<tr>
				<th width="20"></th>
				<th>ラベル名</th>
				<th width="100" class="posi_center"></th>
			</tr>
			<c:forEach items="${tShopListLabelList}" var="tShopListLabel">
				<tr>
					<td class="posi_center"><html:checkbox property="shopListLabelId" value="${tShopListLabel.id}" /> </td>
					<td>${f:h(tShopListLabel.labelName)}</td>
					<td class="posi_center">
					<input type="button" name=""
						value="変更" data-labelid="${tShopListLabel.id}"
						class="labelEditButton" />
					</td>
				</tr>
			</c:forEach>
			<html:hidden property="id" value="" styleId="labelid" />
			<html:hidden property="customerId" value="${customerId}" styleId="labelCustomerId" />
		</table>

		<div class="wrap_btn">
			<html:submit property="delete" value="削　除" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="conf_btn" />
		</div>

	</s:form>


</c:if>

	<br />
	<br />
	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->

	<script type="text/javascript">
		$(".labelEditButton").on("click", function() {
			const id = $(this).data("labelid");

			$("#labelid").val(id);

			$('form').attr('action', "${f:url('/shopLabel/edit/')}");

			$("#listForm").submit();
		});
	</script>

</div>
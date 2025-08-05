<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq  PAGE_DELETE}">
				<li><a href="${f:url(gf:makePathConcat1Arg(navigationPath2, customerId))}">${f:h(navigationText2)}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle1)}" class="title shop">
	<c:if test="${not empty customerId}">
		<gt:convertToCustomerName customerId="${customerId}" name="customName"/>
		${gf:replaceStr(customName, common['gc.shopList.customerName.trim.length'], common['gc.replaceStr'])}
	</c:if>
	</h2>
	<hr />
	<p id="txt_comp">${f:h(defaultMsg)}</p>
	<hr />
	<div class="wrap_btn">
			<s:form action="${f:h(actionPath)}">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE}">
						<html:submit property="reShowList" value="検索結果へ戻る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:otherwise>
						<html:submit property="backToIndex" value="${f:h(LABEL_SHOPLIST)}へ戻る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:otherwise>
				</c:choose>
			</s:form>
	</div>
</div>
<!-- #main# -->
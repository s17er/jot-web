<%@page pageEncoding="UTF-8"%>
<p class="list_title">系列店舗一覧</p>
<div class="contentsLeft" id="shopListScrollDiv">

	<ul class="clear" >
		<c:forEach items="${relationShopList}" var="dto" varStatus="status">
			<c:choose>
				<c:when test="${status.index mod 2 eq 0}">
					<c:set var="className" value="" scope="page" />
				</c:when>
				<c:otherwise>
					<c:set var="className" value="leftShopBack" scope="page" />
				</c:otherwise>
			</c:choose>

			<li class="leftShoplist ${f:h(className)}" id="shopListLi_${f:h(dto.id)}">
				<div class="leftShopImg">
					<c:if test="${not empty dto.shopListImagePath}">
						<img src="${f:url(dto.shopListImagePath)}" width="95px" height="75px" />
					</c:if>
				</div>
				<div class="leftShopText">
					<a href="${f:url(dto.detailPath)}"><p class="leftShopTitle">${f:h(gf:replaceStr(dto.shopName, SHOPLIST_SHOPNAME_TRIM, REPLACE_TRIM_STR))}</p></a>
				</div>
			</li>
		</c:forEach>
	</ul>
</div>
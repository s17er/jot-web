<%@page pageEncoding="UTF-8"%>
<c:set var="SHOPLIST_MATERIALKBN_MAIN1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1 %>" scope="request" />
<c:set var="EXIST_JOB_OFFER_FLG" value="<%=MTypeConstants.JobOfferFlg.ARI %>" scope="request" />
<c:set var="ENUM_DRAFT_PREVIEW"  value="<%=ListForm.PreviewMethodKbn.DRAFT_PREVIEW %>" scope="request" />

<c:set var="SHOPLIST_SHOPNAME_TRIM" value="${common['gc.shopList.preview.shopName.trim.length']}" scope="request" />
<c:set var="SHOPLIST_ADDRESS_TRIM" value="${common['gc.shopList.preview.address.trim.length']}" scope="request" />
<c:set var="SHOPLIST_TRANSIT_TRIM" value="${common['gc.shopList.preview.transit.trim.length']}" scope="request" />
<c:set var="REPLACE_TRIM_STR" value="${common['gc.replaceStr']}" scope="request" />

<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/jquery/jquery.scrollto.js"></script>

<script type="text/javascript">
	$(function() {
		var shopListUlId = $("#shopListScrollDiv");
		shopListUlId.scrollTo("#shopListLi_${f:h(shopListId)}");

	});
</script>


<c:choose>
	<c:when test="${previewMethodKbn eq ENUM_DRAFT_PREVIEW }">
		<c:set var="RETURN_DETAIL_PATH" value="${gf:makePathConcat3Arg('/detailPreview/draftDetail/displayDetail', id, accessCd, siteAreaCd)}" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="RETURN_DETAIL_PATH" value="${gf:makePathConcat1Arg('/detailPreview/detail/displayDetail', id)}" scope="request" />
	</c:otherwise>
</c:choose>


<%
	//データが取得できなければ表示しない
%>
<c:if test="${existDataFlg eq true}">
<gt:typeList name="dbIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" scope="request"/>
	<div id="main" class="clear">
		<div id="main">

			<%-- 詳細ページ共通ヘッダを差し込む --%>
			<%@include file="/WEB-INF/view/preview/body/apO01_header.jsp"%>
			<div id="displayDetail">
				<div class="displayShop clear">
					<%-- 右側のメイン部分 --%>
					<jsp:include page="/WEB-INF/view/preview/include/fpB01R05_shopDetail.jsp"></jsp:include>

					<%-- 左側の一覧部分 --%>
					<jsp:include page="/WEB-INF/view/preview/include/fpB01R05_subList.jsp"></jsp:include>
				</div>
			</div>
		</div>
	</div>
</c:if>
<%@page pageEncoding="UTF-8"%>

<c:set var="SHOPLIST_MATERIALKBN_MAIN1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1%>" scope="request" />
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB%>" scope="request"/>
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION%>" scope="request" />
<c:set var="EXIST_JOB_OFFER_FLG" value="<%=MTypeConstants.JobOfferFlg.ARI%>" scope="request"/>

<c:set var="SHOPLIST_SHOPNAME_TRIM" value="${common['gc.shopList.preview.shopName.trim.length']}" scope="request" />
<c:set var="SHOPLIST_ADDRESS_TRIM" value="${common['gc.shopList.preview.address.trim.length']}" scope="request" />
<c:set var="SHOPLIST_TRANSIT_TRIM" value="${common['gc.shopList.preview.transit.trim.length']}" scope="request" />
<c:set var="REPLACE_TRIM_STR" value="${common['gc.replaceStr']}" scope="page" />

<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/jquery/jquery.scrollto.js"></script>
<script type="text/javascript">
	$(function() {
		var shopListUlId = $("#shopListScrollDiv");
		shopListUlId.scrollTo("#shopListLi_${f:h(shopListId)}");

	});
</script>


<c:choose>
	<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
		<c:set var="RETURN_DETAIL_BASE_PATH" value="/detailPreview/detail/displayDetail" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="RETURN_DETAIL_BASE_PATH" value="/detailPreview/dbDetail/displayDetail" scope="request" />
	</c:otherwise>
</c:choose>

<c:if test="${existDataFlg eq true}">
	<gt:typeList name="dbIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD%>" scope="request" />
	<div id="main" class="clear">
		<div id="main">

			<%-- 詳細ページ共通ヘッダを差し込む --%>
			<%@include file="/WEB-INF/view/preview/body/apO01_header.jsp"%>

			<!-- メイン画像表示切替用設定 -->
			<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/jquery.cycle.all.js"></script>
			<script type="text/javascript">
				$.fn.cycle.defaults.speed = 900;
				$.fn.cycle.defaults.timeout = 3000;

				$(function() {
					// run the code in the markup!
					$('#demos pre code').each(function() {
						eval($(this).text());
					});
				});
			</script>
			<style type="text/css">
				pre {
					display: none
				}
			</style>
			<!-- メイン画像表示切替用設定-ココまで -->

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
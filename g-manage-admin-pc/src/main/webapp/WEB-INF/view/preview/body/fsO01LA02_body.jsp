<%@page pageEncoding="UTF-8"%>

<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" scope="page"/>

<c:choose>
	<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
		<c:set var="NEXT_PATH" value="${gf:makePathConcat2Arg('/shopListPreview/smartList/nextSession', inputFormKbn, 'TARGET_PAGE')}" scope="page" />
		<c:set var="BACK_PATH" value="${gf:makePathConcat2Arg('/detailPreview/smartPhoneDetail/index', '0', inputFormKbn)}" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="NEXT_PATH" value="${gf:makePathConcat2Arg('/shopListPreview/smartList/next', webId, 'TARGET_PAGE')}" scope="page" />
		<c:set var="BACK_PATH" value="${gf:makePathConcat1Arg('/detailPreview/smartPhoneDbDetail/index', webId)}" scope="request" />
	</c:otherwise>
</c:choose>


<script type="text/javascript">


	var nextPageLink;

	var nextPageFlg = false;

	var currentPage;

	var lastPage;


	$(document).ready(function() {
		var pagetop = $('.pagetop');
		$(window).scroll(function () {
			if ($(this).scrollTop() > 700) {
				pagetop.fadeIn();
			} else {
				pagetop.fadeOut();
			}
		});
		pagetop.click(function () {
			$('body, html').animate({ scrollTop: 0 }, 500);
			return false;
		});
	});

	$(function() {
	    $(window).bottom({proximity: 0.05});
	    $(window).on('bottom', function() {

	        var obj = $(this);
			if (!nextPageFlg) {
				if (obj.data('loading')) {
					obj.data('loading', false);
					changeShopListLoadingImage(false);
				}
				return;
			}
	        if (!obj.data('loading')) {
	            obj.data('loading', true);
	            changeShopListLoadingImage(true);

	            	$.ajax({
	            		url:nextPageLink.replace('TARGET_PAGE', currentPage + 1),
	            		success:function(html) {


	            			$('#listStoreInfo').append(html);


	            			nextPageFlg = ++currentPage < lastPage;
			            	obj.data('loading', false);
			            	changeShopListLoadingImage(false);
	            		}

	            	})


	        }
	    });
	    $('html,body').animate({ scrollTop: 0 }, '1');
	});


	var changeShopListLoadingImage = function(flg) {
		var shopListLoadingImage = $("#shopListLoadingImage");
		if (flg) {
			shopListLoadingImage.css("visibility", "visible");
		} else {
			shopListLoadingImage.css("visibility", "hidden");
		}
	}
</script>





<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" scope="page"/>



<jsp:include page="/WEB-INF/view/preview/body/fsO01LA02_header.jsp" />
<c:choose>
	<c:when test="${existDataFlg eq true}">
		<script type="text/javascript">
			currentPage = ${f:h(pageNavi.currentPage)};
			lastPage = ${f:h(pageNavi.lastPage)}
			nextPageLink = "${f:url(NEXT_PATH)}";
			nextPageFlg = currentPage < lastPage;
		</script>
		<div id="contents">
			<p class="pagetop" style="display: none;">
				<a href="#top">▲</a>
			</p>

			<ul id="listStoreInfo">
				<jsp:include page="/WEB-INF/view/preview/body/fsO01LA02_list.jsp" />
			</ul>

			<div id="imgLoading">
				<img src="/ipx/img/loader.gif" id="shopListLoadingImage" style="visibility: hidden;" />
			</div>

		</div>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			nextPageFlg = false;
		</script>
	</c:otherwise>
</c:choose>
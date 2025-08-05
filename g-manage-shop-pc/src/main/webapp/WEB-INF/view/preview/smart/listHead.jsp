<%@page pageEncoding="UTF-8"%>

<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<c:set var="SIZE_TEXT_WEB" value="<%= MTypeConstants.SizeKbn.TEXT_WEB %>" scope="page" />

<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/detail.css">
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/shoplist.css">
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/shop_data.css" />




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







<script src="${f:h(frontHttpUrl)}/ipx/js/jquery.bottom-1.0.js"></script>
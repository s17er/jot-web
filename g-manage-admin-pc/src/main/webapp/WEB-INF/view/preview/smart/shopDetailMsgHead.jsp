<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>"/>
<script src="${f:h(frontHttpUrl)}/ipx/js/custom.js"></script>
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/other.css" />
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/movie.css" />
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/detailABCDE.css">

<c:if test="${sizeKbnDE and pluralMainImage}">
	<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/flexslider.css">
	<script src="${f:h(frontHttpUrl)}/ipx/js/jquery.flexslider.js"></script>
	<script type="application/javascript">
	<!--//
	$(window).load(function() {
		$('.flexslider').flexslider();
		$('.flexslider').css('width', '300');
		$('.flexslider').css('height', '227');
		$('.flexslider').css('margin-left','auto');
		$('.flexslider').css('margin-right','auto');
		$('.flexslider .slides li img').css('width', '300');
		$('.flexslider .slides li img').css('height', '227');
	});
	//-->
</script>
</c:if>
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/tel.css">


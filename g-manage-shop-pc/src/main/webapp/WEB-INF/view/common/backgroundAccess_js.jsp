<%@page pageEncoding="UTF-8"%>
<c:set var="background_access_time" value="${common['gc.bacground.accessTime']}"></c:set>
<script type="text/javascript">

var accessBackGroundInterval = function(url) {
	setInterval(function() {
		$.ajax({
			type: "GET",
			url : url
		})
	}, ${f:h(background_access_time)})
}
$(function() {
	accessBackGroundInterval("${f:url('/ajax/backGroundAccess')}");
});
</script>
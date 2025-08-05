<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/search.css" />
<link rel="stylesheet" type="text/css" href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/searchList.css" />
<link rel="stylesheet" type="text/css" href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/jobSearch.css" />
<link rel="stylesheet" type="text/css" href="${f:h(frontHttpUrl)}${f:h(cssLocation)}/list.css" />
<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/jquery/accordion.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#research").accordion({
				autoheight:false,
				active: "#02",
				alwaysOpen:false
			});
	});
</script>
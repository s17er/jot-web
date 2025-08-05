<%@page pageEncoding="UTF-8"%>

<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<c:set var="SIZE_TEXT_WEB" value="<%= MTypeConstants.SizeKbn.TEXT_WEB %>" scope="page" />

<c:choose>
	<c:when test="${sizeKbn eq SIZE_TEXT_WEB}">
		<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/detailTxt.css">
	</c:when>
	<c:otherwise>
		<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/detail.css">
	</c:otherwise>
</c:choose>

<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/tel.css">
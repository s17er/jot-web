<%@page pageEncoding="UTF-8"%>
<fmt:setLocale value="ja-JP" />
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />

	<%-- パスを生成 --%>
	<c:choose>
	<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
		<c:set var="LOGO_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_LOGO, idForDirName)}" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="LOGO_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_LOGO)}" scope="page" />
	</c:otherwise>
	</c:choose>


<div style="font-size: xx-small; text-align: center;">掲載:

<fmt:formatDate value="${postStartDatetime}" pattern="yyyy/MM/dd" />
 - <fmt:formatDate value="${postEndDatetime}" pattern="yyyy/MM/dd" />

</div>

<div style="font-size: small; text-align: center;">

<c:if test="${imageCheckMap[MATERIAL_KBN_LOGO]}">
	<img src="${f:url(LOGO_IMG_PATH)}" alt="${f:h(manuscriptName)}"  />
</c:if>

</div>
<div style="color: ${SITE_COLOR}; font-size: small; text-align: center;">
<img src="${f:h(frontHttpUrl)}/mobile${f:h(imagesLocation)}/search/bdr_dot.gif" alt="読み込み中..." /><br />
${f:h(manuscriptName)}<br />
<img src="${f:h(frontHttpUrl)}/mobile${f:h(imagesLocation)}/search/bdr_dot.gif" alt="読み込み中..." />
</div>


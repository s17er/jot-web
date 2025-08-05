<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />

	<%-- パスを生成 --%>
	<c:choose>
	<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
		<c:set var="BOSHU_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayRecruit', id, inputFormKbn)}#bosyu" scope="page" />
		<c:set var="TAIGU_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayRecruit', id, inputFormKbn)}#taigu" scope="page" />
		<c:set var="OUBO_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayRecruit', id, inputFormKbn)}#oubo" scope="page" />
		<c:set var="COMPANY_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayCompany', id, inputFormKbn)}" scope="page" />
		<c:set var="MAP_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayMap', id, inputFormKbn)}" scope="page" />

		<c:set var="LOGO_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_LOGO, idForDirName)}" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="BOSHU_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayRecruit', id)}#bosyu" scope="page" />
		<c:set var="TAIGU_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayRecruit', id)}#taigu" scope="page" />
		<c:set var="OUBO_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayRecruit', id)}#oubo" scope="page" />
		<c:set var="COMPANY_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayCompany', id)}" scope="page" />
		<c:set var="MAP_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayMap', id)}" scope="page" />

		<c:set var="LOGO_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_LOGO)}" scope="page" />
	</c:otherwise>
	</c:choose>


<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

<div style="font-size: xx-small; text-align: center;">
<a href="${f:url(BOSHU_PATH)}">募集DATA</a>
&nbsp;/&nbsp;
<a href="${f:url(TAIGU_PATH)}">待遇DATA</a>
&nbsp;/&nbsp;
<a href="${f:url(OUBO_PATH)}">応募DATA</a><br />

<a href="${f:url(COMPANY_PATH)}">お店･会社DATA</a>

<c:if test="${not empty mapAddress}">
  &nbsp;/&nbsp;
  <a href="${f:url(MAP_PATH)}">地図</a>
</c:if>

</div>
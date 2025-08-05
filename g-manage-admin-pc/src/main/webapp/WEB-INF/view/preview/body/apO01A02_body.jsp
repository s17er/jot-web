<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<c:set var="MATERIAL_KBN_FREE" value="<%=MTypeConstants.MaterialKbn.FREE %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />

<%-- パスを生成 --%>
<c:choose>
<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
	<c:set var="FREE_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_FREE, idForDirName)}" scope="page" />
	<c:set var="TAB_DETAIL_PATH" value="${gf:makePathConcat1Arg('/detailPreview/detail/displayDetail', id)}" scope="page" />
</c:when>
<c:otherwise>
	<c:set var="FREE_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_FREE)}" scope="page" />
	<c:set var="TAB_DETAIL_PATH" value="${gf:makePathConcat1Arg('/detailPreview/dbDetail/displayDetail', id)}" scope="page" />
</c:otherwise>
</c:choose>

<html:errors />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<div id="main">

		<%-- 詳細ページ共通ヘッダを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/apO01_header.jsp" %>

		<hr />

		<div id="spc_recruit" class="clear">
			<div id="spc_job">
				<strong>${f:br(f:h(recruitmentJob))}</strong>
			</div>
		</div>
		<hr />

		<div id="message">

			<c:choose>
			<c:when test="${imageCheckMap[MATERIAL_KBN_FREE]}">
				<img src="${f:url(FREE_IMG_PATH)}" alt="${f:h(manuscriptName)}"  />
			</c:when>
			<c:otherwise>
				メッセージは現在準備中です。
			</c:otherwise>
			</c:choose>

			<div class="wrap_btn">
				<a href="${f:url(TAB_DETAIL_PATH)}"><img src="${f:h(frontHttpUrl)}/ipx/${f:h(imagesLocation)}/search/btn_goDetail.gif" alt="詳細はこちら" /></a>
			</div>
			<hr />
		</div>

	</div>
</c:if>

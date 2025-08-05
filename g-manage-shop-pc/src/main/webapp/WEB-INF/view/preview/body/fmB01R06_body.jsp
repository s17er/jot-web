<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm"%>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey" %>

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="ENUM_DRAFT_PREVIEW"  value="<%=ListForm.PreviewMethodKbn.DRAFT_PREVIEW %>" scope="page" />

<c:set var="OBSERVATION_KBN_QUESTION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION%>" scope="page" />
<c:set var="OBSERVATION_KBN_QUESTION_OBSERVATION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION%>" scope="page" />
<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="page" />

<!-- #main# -->

	<%-- 詳細ページ共通ヘッダを差し込む --%>
	<%@include file="/WEB-INF/view/preview/body/fmB01R_header.jsp" %>

<%--
<c:if test="${!empty mapTitle}">
	<div style="text-align: center; font-size: xx-small;">
	${f:h(mapTitle)}&nbsp;
	</div>
</c:if>
--%>
<div style="text-align: center;">

<img src="https://maps.google.com/maps/api/staticmap?key=${gf:googleMapApiKey(GOOGLE_MAP_PROP_KEY)}&size=240x240&format=jpg-baseline&markers=${mapAddress}&center=${mapAddress}&zoom=${not empty zoom ? zoom : common['gourmetcaree.googleMap.zoom']}" width="240" height="240" />
<%--
	<br />
	<a href="${f:url(gf:makePathConcat2Arg('/detailPreview/mobileDetail/zoomInMap', id, zoom))}" accesskey="*">(*)拡大</a>&nbsp;|
	<a href="${f:url(gf:makePathConcat2Arg('/detailPreview/mobileDetail/zoomOutMap', id, zoom))}" accesskey="#">縮小(#)</a>
--%>
</div>
<br />
<c:if test="${not empty addressTraffic}">
<span style="font-size: xx-small;">
${f:br(f:h(addressTraffic))}
</span><br /><br />
</c:if>

<div style="text-align: center; font-size: xx-small;">
	<c:if test="${applicationButtonBlock}">
		<c:if test="${applicationOkFlg}">
			<a href="#"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆応募する◆</span></a><br />
		</c:if>

		<c:choose>
			<c:when test="${observationKbn eq OBSERVATION_KBN_QUESTION}">
				<a href="#" ><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆質問◆</span></a><br />
			</c:when>
			<c:when test="${observationKbn eq OBSERVATION_KBN_QUESTION_OBSERVATION}">
				<a href="#" ><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆店舗見学or質問◆</span></a><br />
			</c:when>
		</c:choose>
	</c:if>
	<a href="#"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆検討中BOXに追加◆</span></a>
</div><br />

		<%-- 詳細ページ共通メニューを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/fmB01R_menu.jsp" %>

<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

<div style="font-size: xx-small; text-align: right">
<a href="#top"><span style="color: #CC0000;">▲ﾍﾟｰｼﾞﾄｯﾌﾟに戻る</span></a>
</div><br />
<div style="font-size: xx-small;">
<a href="#">■企業ﾍﾟｰｼﾞﾄｯﾌﾟへ戻る</a><br />
<a href="#">■検索結果一覧へ戻る</a><br />
</div>

<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

	<div style="font-size: xx-small;">
		<img src="${f:h(frontHttpUrl)}${f:h(SITE_EMOJI)}134.gif" /><a href="#" accesskey="0">ﾄｯﾌﾟﾍﾟｰｼﾞへ戻る</a>
	</div>
<!-- #main# -->

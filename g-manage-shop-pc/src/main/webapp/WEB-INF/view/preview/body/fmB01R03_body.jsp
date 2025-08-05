<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm"%>

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="ENUM_DRAFT_PREVIEW"  value="<%=ListForm.PreviewMethodKbn.DRAFT_PREVIEW %>" scope="page" />
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />

<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />
<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="MATERIAL_KBN_RIGHT" value="<%=MTypeConstants.MaterialKbn.RIGHT %>" scope="page" />

<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>" scope="page" />
<c:set var="MAIN2_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN2)}" scope="page" />
<c:set var="MAIN3_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN3)}" scope="page" />
<c:set var="RIGHT_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_RIGHT)}" scope="page" />

<c:set var="OBSERVATION_KBN_QUESTION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION%>" scope="page" />
<c:set var="OBSERVATION_KBN_QUESTION_OBSERVATION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION%>" scope="page" />


	<c:choose>
	<c:when test="${previewMethodKbn eq ENUM_DRAFT_PREVIEW }">
		<c:set var="DETAIL4_PATH" value="${gf:makePathConcat3Arg('/detailPreview/mobileDraftDetail/displayDetail4', id, accessCd, areaCd)}" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="DETAIL4_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDetail/displayDetail4', id)}" scope="page" />
	</c:otherwise>
	</c:choose>





<!-- #main# -->

	<%-- 詳細ページ共通ヘッダを差し込む --%>
	<%@include file="/WEB-INF/view/preview/body/fmB01R_header.jsp" %>


	<c:if test="${blockRight eq true and blockRightExist}">
		<c:if test="${imageCheckMap[MATERIAL_KBN_RIGHT] and blockPhoto}">
			<img src="${f:url(RIGHT_IMG_PATH)}" width="100" height="139" style="float:right;margin-top:5px;margin-right:3px;margin-bottom:1px;" align="left" border="0"" />
		</c:if>
		<c:if test="${not empty sentence2}">
			<div style="text-align:left;">
				<div style="font-size:xx-small;">
					${f:br(sentence2)}
				</div>
			</div>
		</c:if>
		<div clear="all" style="clear:both;"></div>
		<br />
	</c:if>

	<c:if test="${blockCaption and blockCaptionExist}">
		<div style="text-align: center; font-size: xx-small;">
			<a href="${f:url(DETAIL4_PATH)}"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆他の写真･ﾒｯｾｰｼﾞを見る◆</span></a>
		</div><br />
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

	<a href=""><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆検討中BOXに追加◆</span></a>

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

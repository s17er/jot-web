<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm"%>

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="ENUM_DRAFT_PREVIEW"  value="<%=ListForm.PreviewMethodKbn.DRAFT_PREVIEW %>" scope="page" />
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />

<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />
<c:set var="MATERIAL_KBN_SUB1" value="<%=MTypeConstants.MaterialKbn.MAIN_1 %>" scope="page" />
<c:set var="MATERIAL_KBN_SUB2" value="<%=MTypeConstants.MaterialKbn.MAIN_2 %>" scope="page" />
<c:set var="MATERIAL_KBN_SUB3" value="<%=MTypeConstants.MaterialKbn.MAIN_3 %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_A" value="<%=MTypeConstants.MaterialKbn.PHOTO_A %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_B" value="<%=MTypeConstants.MaterialKbn.PHOTO_B %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_C" value="<%=MTypeConstants.MaterialKbn.PHOTO_C %>" scope="page" />
<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />

<c:set var="OBSERVATION_KBN_QUESTION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION%>" scope="page" />
<c:set var="OBSERVATION_KBN_QUESTION_OBSERVATION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION%>" scope="page" />

<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>" scope="page" />

<c:set var="PHOTO_A_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_A)}" scope="page" />
<c:set var="PHOTO_B_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_B)}" scope="page" />
<c:set var="PHOTO_C_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_C)}" scope="page" />


<!-- #main# -->

	<%-- 詳細ページ共通ヘッダを差し込む --%>
	<%@include file="/WEB-INF/view/preview/body/fmB01R_header.jsp" %>



<!-- 写真 -->
<c:if test="${blockCaption eq true}">
	<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_A]}">
	<div style="text-align: center;">
	<img src="${f:url(PHOTO_A_IMG_PATH)}" alt="キャプション１" width="180" height="130" />
	</div>
	<div style="text-align: center; font-size: xx-small;">
		${f:br(captionA)}
	</div><br /><br />
	</c:if>

	<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_B]}">
	<div style="text-align: center;">
	<img src="${f:url(PHOTO_B_IMG_PATH)}" alt="キャプション２" width="180" height="130" />
	</div>
	<div style="text-align: center; font-size: xx-small;">
		${f:br(captionB)}
	</div><br /><br />
	</c:if>

	<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_C]}">
	<div style="text-align: center;">
	<img src="${f:url(PHOTO_C_IMG_PATH)}" alt="キャプション３" width="180" height="130"/>
	</div>
	<div style="text-align: center; font-size: xx-small;">
		${f:br(captionC)}
	</div><br /><br />
	</c:if>
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

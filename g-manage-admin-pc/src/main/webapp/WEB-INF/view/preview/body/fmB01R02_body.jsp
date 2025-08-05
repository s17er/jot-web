<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />

<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN1" value="<%=MTypeConstants.MaterialKbn.MAIN_1 %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN2" value="<%=MTypeConstants.MaterialKbn.MAIN_2 %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN3" value="<%=MTypeConstants.MaterialKbn.MAIN_3 %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_A" value="<%=MTypeConstants.MaterialKbn.PHOTO_A %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_B" value="<%=MTypeConstants.MaterialKbn.PHOTO_B %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_C" value="<%=MTypeConstants.MaterialKbn.PHOTO_C %>" scope="page" />
<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="MATERIAL_KBN_ATTENTION_HERE" value="<%=MTypeConstants.MaterialKbn.ATTENTION_HERE %>" scope="page" />

<c:set var="OBSERVATION_KBN_QUESTION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION%>" scope="page" />
<c:set var="OBSERVATION_KBN_QUESTION_OBSERVATION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION%>" scope="page" />

<c:set var="SIZE_C" value="<%=MTypeConstants.SizeKbn.C %>" scope="page" />

	<c:choose>
	<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">

		<c:set var="DETAIL3_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayDetail3', id, inputFormKbn)}" scope="page" />
		<c:set var="DETAIL4_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayDetail4', id, inputFormKbn)}" scope="page" />
		<c:set var="COMPANY_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayCompany', id, inputFormKbn)}" scope="page" />
		<c:set var="MAP_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/displayMap', id, inputFormKbn)}" scope="page" />
		<c:set var="ATTENTION_HERE_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_ATTENTION_HERE, idForDirName)}" scope="page" />
		<c:set var="MAIN1_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_MAIN1, idForDirName)}" scope="page" />
		<c:set var="MAIN2_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_MAIN2, idForDirName)}" scope="page" />
		<c:set var="MAIN3_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_MAIN3, idForDirName)}" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="DETAIL3_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayDetail3', id)}#bosyu" scope="page" />
		<c:set var="DETAIL4_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayDetail4', id)}" scope="page" />
		<c:set var="COMPANY_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayCompany', id)}" scope="page" />
		<c:set var="MAP_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/displayMap', id)}" scope="page" />
		<c:set var="ATTENTION_HERE_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_ATTENTION_HERE)}" scope="page" />
		<c:set var="MAIN1_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN1)}" scope="page" />
		<c:set var="MAIN2_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN2)}" scope="page" />
		<c:set var="MAIN3_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN3)}" scope="page" />
	</c:otherwise>
	</c:choose>


<!-- #main# -->

		<%-- 詳細ページ共通ヘッダを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/fmB01R_header.jsp" %>

	<c:if test="${catchCopyExist}">
		<span style="font-size: small; color: #FF0000;">
			${f:br(catchCopy)}
		</span><br />
	</c:if>


	<%-- MAIN画像1 --%>
	<c:if test="${blockMainImage and blockMainImageExist}">
		<div style="text-align: center;">
			<c:choose>
				<c:when test="${sizeKbnABC}">
					<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
						<img src="${f:url(MAIN1_IMG_PATH)}" alt="${f:h(manuscriptName)}" alt="メイン" width="240" height="182" />
					</c:if>
				</c:when>
				<c:when test="${sizeKbnDE}">
					<c:choose>
						<c:when test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
							<img src="${f:url(MAIN1_IMG_PATH)}" alt="${f:h(manuscriptName)}" alt="メイン" width="240" height="182"/>
						</c:when>
						<c:when test="${imageCheckMap[MATERIAL_KBN_MAIN2]}">
							<img src="${f:url(MAIN2_IMG_PATH)}" alt="${f:h(manuscriptName)}" alt="メイン" width="240" height="182"/>
						</c:when>
						<c:when test="${imageCheckMap[MATERIAL_KBN_MAIN3]}">
							<img src="${f:url(MAIN3_IMG_PATH)}" alt="${f:h(manuscriptName)}" alt="メイン" width="240" height="182"/>
						</c:when>
					</c:choose>
				</c:when>
			</c:choose>
		</div>
	</c:if>


	<c:if test="${not empty sentence1}">
		<span style="font-size: xx-small;">
		${f:br(sentence1)}
		</span><br /><br />
	</c:if>

	<%-- 写真2、3用のページへのリンク  --%>
	<c:if test="${detail3DataExist or (blockCaption and blockCaptionExist)}">
		<div style="text-align: center; font-size: xx-small;">
			<c:choose>
				<c:when test="${detail3DataExist}">
					<a href="${f:url(DETAIL3_PATH)}"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆他の写真･ﾒｯｾｰｼﾞを見る◆</span></a>
				</c:when>
				<c:when test="${blockCaption and blockCaptionExist}">
					<a href="${f:url(DETAIL4_PATH)}"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆他の写真･ﾒｯｾｰｼﾞを見る◆</span></a>
				</c:when>
			</c:choose>
		</div><br />
	</c:if>

	<c:if test="${sizeKbn eq SIZE_C and not empty sentence2}">
		<span style="font-size: xx-small;">
			${f:br(sentence2)}
		</span>
	</c:if>
	<c:if test="${sizeKbnDE and  not empty sentence3}">
		<span style="font-size: xx-small;">
			${f:br(sentence3)}
		</span>
	</c:if>
	<br /><br />

	<c:if test="${blockAttentionHere and blockAttentionHereExist}">
		<div style="text-align: center; background:#FFFFCC;">
				<div style="text-align: center;"><br />
					<img src="${f:url(ATTENTION_HERE_IMG_PATH)}" alt="ここに注目" width="175" height="150" />
				</div>
			<div style="background:#FFFFCC;">
				<img src="${f:h(frontHttpUrl)}/mobile${f:h(imagesLocation)}/search/kokoni.jpg" alt="読み込み中..." width="240" height="20" />
			</div>

			<span style="font-size: xx-small;">
				${f:br(attentionHereSentence)}
			</span><br /><br />

		</div>
	</c:if>

<div style="text-align: right; font-size: xx-small;">&raquo;<a href="${f:url(COMPANY_PATH)}">お店･会社DATA</a></div><br />


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

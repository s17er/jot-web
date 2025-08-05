<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm"%>

<c:set var="ENUM_DRAFT_PREVIEW"  value="<%=ListForm.PreviewMethodKbn.DRAFT_PREVIEW %>" scope="page" />
<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="SIZE_TEXT_WEB"  value="<%=MTypeConstants.SizeKbn.TEXT_WEB %>"/>
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />

<c:set var="OBSERVATION_KBN_QUESTION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION%>" scope="page" />
<c:set var="OBSERVATION_KBN_QUESTION_OBSERVATION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION%>" scope="page" />

	<c:choose>
	<c:when test="${previewMethodKbn eq ENUM_DRAFT_PREVIEW }">
		<c:set var="DETAIL2_PATH" value="${gf:makePathConcat3Arg('/detailPreview/mobileDraftDetail/displayDetail2', id, accessCd, areaCd)}#bosyu" scope="page" />
		<c:set var="KINMUCHI_PATH" value="${gf:makePathConcat3Arg('/detailPreview/mobileDraftDetail/displayCompany', id, accessCd, areaCd)}#shop" scope="page" />
		<c:set var="MAP_PATH" value="${gf:makePathConcat3Arg('/detailPreview/mobileDraftDetail/displayMap', id, accessCd, areaCd)}" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="DETAIL2_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDetail/displayDetail2', id)}#bosyu" scope="page" />
		<c:set var="KINMUCHI_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDetail/displayCompany', id)}#shop" scope="page" />
		<c:set var="MAP_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDetail/displayMap', id)}" scope="page" />
	</c:otherwise>
	</c:choose>


<html:errors />
<!-- #main# -->

		<%-- 詳細ページ共通ヘッダを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/fmB01R_header.jsp" %>

<c:if test="${sizeKbn eq SIZE_TEXT_WEB}">
	<c:if test="${catchCopyExist}">
		<span style="font-size: small; color: #FF0000;">${f:br(catchCopy)}</span><br />
	</c:if>
	<c:if test="${not empty sentence1}">
		<span style="font-size: xx-small;">${f:br(sentence1)}</span>
	</c:if>
</c:if>


<span style="font-size: small; color: ${SITE_COLOR};">
	<c:if test="${sizeKbn eq SIZE_TEXT_WEB}"><br /><br /></c:if>
	◆業態
</span><br />
<span style="font-size: xx-small;">
<%--業種（業態） --%>
<c:forEach items="${industryKbnList}" var="industryValue" varStatus="status">
	${f:label(industryValue, dbIndustryKbnList, 'value', 'label')}
	<c:if test="${!status.last}">&nbsp;</c:if>
</c:forEach>
</span>

		<%-- 詳細ページ共通メニューを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/fmB01R_menu.jsp" %>


<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

<div style="text-align: center;">
<a name="bosyu"></a>
<img src="${f:h(frontHttpUrl)}/mobile${f:h(imagesLocation)}/search/title_bosyu.gif" alt="読み込み中..." />
</div>
<span style="font-size: small; color: ${SITE_COLOR};">◆募集職種</span><br />
<span style="font-size: xx-small;">

${f:br(f:h(recruitmentJob))}

</span><br /><br />

<c:if test="${not empty salary}">
<span style="font-size: small; color: ${SITE_COLOR};">◆給与</span><br />
<span style="font-size: xx-small;">
	${f:br(f:h(salary))}
</span>
<br /><br />
</c:if>

<c:if test="${not empty personHunting}">
<span style="font-size: small; color: ${SITE_COLOR};">◆求める人物像・資格</span><br />
<span style="font-size: xx-small;">
${f:br(f:h(personHunting))}
</span><br /><br />
</c:if>

<c:if test="${not empty workingPlace}">
<span style="font-size: small; color: ${SITE_COLOR};">◆勤務地ｴﾘｱ･最寄駅</span><br />
<span style="font-size: xx-small;">
${f:br(f:h(workingPlace))}
</span>
</c:if>

<div style="text-align: right; font-size: xx-small;">&raquo;<a href="${f:url(KINMUCHI_PATH)}">勤務地詳細</a></div><br />

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

<div style="text-align: center;">
<a name="taigu"></a>
<img src="${f:h(frontHttpUrl)}/mobile${f:h(imagesLocation)}/search/title_taigu.gif" alt="読み込み中..." />
</div>

<c:if test="${not empty treatment}">
<span style="font-size: small; color: ${SITE_COLOR};">◆待遇</span><br />
<span style="font-size: xx-small;">
${f:br(f:h(treatment))}
</span><br /><br />
</c:if>

<c:if test="${not empty workingHours}">
<span style="font-size: small; color: ${SITE_COLOR};">◆勤務時間</span><br />
<span style="font-size: xx-small;">
${f:br(f:h(workingHours))}
</span><br /><br />
</c:if>

<c:if test="${not empty holiday}">
<span style="font-size: small; color: ${SITE_COLOR};">◆休日休暇</span><br />
<span style="font-size: xx-small;">
${f:br(f:h(holiday))}
</span><br /><br />
</c:if>

<c:if test="${detail2DataExist}">
	<div style="text-align: center; font-size: xx-small;">
	<a href="${f:url(DETAIL2_PATH)}"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆求人内容を詳しく見る◆</span></a>
	</div><br />
</c:if>

<div style="text-align: center;">
<a name="oubo"></a>
<img src="${f:h(frontHttpUrl)}/mobile${f:h(imagesLocation)}/search/title_oubo.gif" alt="読み込み中..." />
</div>

<c:if test="${not empty applicationMethod}">
<span style="font-size: small; color: ${SITE_COLOR};">◆応募方法</span><br />
<span style="font-size: xx-small;">
${f:br(f:h(applicationMethod))}
</span><br /><br />
</c:if>

<c:if test="${not empty phoneReceptionist}">
<span style="font-size: small; color: ${SITE_COLOR};">◆電話番号/受付時間</span><br />
<span style="font-size: xx-small;">
${f:br(gf:editTelLink(f:h(phoneReceptionist), false))}
</span><br /><br />
</c:if>

<c:if test="${not empty addressTraffic}">
<span style="font-size: small; color: ${SITE_COLOR};">◆住所</span><br />
<span style="font-size: xx-small;">
${f:br(f:h(addressTraffic))}

<c:if test="${not empty mapAddress}">
	<div style="text-align: right; font-size: xx-small;">&raquo;<a href="${f:url(MAP_PATH)}">地図を見る</a></div>
</c:if>

</span><br />
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
	</c:if>	<a href="#"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">◆検討中BOXに追加◆</span></a>
</div><br />


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

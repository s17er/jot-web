<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<c:set var="employPtnMap" value="<%=MTypeConstants.EmployPtnKbn.previewEmployPtnSmallLabelMap %>"/>
<c:set var="treatmentKbnMap" value="<%=MTypeConstants.TreatmentKbn.previewTreatmentKbnSmallLabelMap %>"/>

<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>" scope="page" />
<c:set var="SIZE_B" value="<%=MTypeConstants.SizeKbn.B %>" scope="page" />
<c:set var="SIZE_C" value="<%=MTypeConstants.SizeKbn.C %>" scope="page" />
<c:set var="SIZE_D" value="<%=MTypeConstants.SizeKbn.D %>" scope="page" />
<c:set var="SIZE_E" value="<%=MTypeConstants.SizeKbn.E %>" scope="page" />
<c:set var="SIZE_TEXT_WEB" value="<%=MTypeConstants.SizeKbn.TEXT_WEB %>" scope="page" />
<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />

<gt:typeList name="dbWebAreaList" typeCd="<%=MTypeConstants.WebAreaKbn.getTypeCd((String)request.getAttribute(\"APPLICATION_AREA_CD\")) %>" noDisplayValue="<%=MTypeConstants.WebAreaKbn.getWebdataNoDispList((String)request.getAttribute(\"APPLICATION_AREA_CD\")) %>" />
<gt:typeList name="sizeKbnList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />
<gt:typeList name="dbJobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
<gt:typeList name="dbOtherConditionKbnList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD %>" />
<gt:typeList name="dbShopsKbnList" typeCd="<%=MTypeConstants.ShopsKbn.TYPE_CD %>" />

<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />

<div id="main">

<div style="font-size: xx-small;">
	<div style="font-size: xx-small;">以下の検索条件で <span style="color: #CC0000;">1</span> 件検索されました｡<br /></div>
	[検索条件は指定されていません]
	<br />
	<br />
	<c:if test="${existDataFlg eq true}">
		${pageNavi.currentPage} / ${pageNavi.lastPage} ﾍﾟｰｼﾞ
	</c:if>
</div>

<div style="font-size: xx-small; text-align: right;">
	<a href="#"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">→さらに絞りこみ</span></a>
</div>

<html:errors />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

	<div style="font-size: xx-small; text-align: center;">
		<img src="${f:h(frontHttpUrl)}${f:h(SITE_EMOJI)}128.gif" />&nbsp;<!--
		--><span style="color: #000000;">前へ&nbsp;</span><!--
		--><span style="color: #000000;">1&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">2</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">3</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">4</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">5</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#" accesskey="6">次へ</a>&nbsp;</span><!--
		--><img src="${f:h(frontHttpUrl)}${f:h(SITE_EMOJI)}130.gif" />
	</div>

	<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

	<c:forEach items="${dataList}" var="dto">
		<%-- パスを生成 --%>
		<c:choose>
			<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
				<c:set var="DETAIL_PREVIEW_PATH" value="${gf:makePathConcat2Arg('/detailPreview/mobileDetail/index', dto.id, inputFormKbn)}" scope="page" />
			</c:when>
			<c:otherwise>
				<c:set var="DETAIL_PREVIEW_PATH" value="${gf:makePathConcat1Arg('/detailPreview/mobileDbDetail/index', dto.id)}" scope="page" />
			</c:otherwise>
		</c:choose>

		<div style="font-size: small; text-align: center; background: ${SITE_COLOR};">
			<a href="${f:url(DETAIL_PREVIEW_PATH)}" style="color: #FFFFFF;">
			<span style="color: #FFFFFF;">${f:h(dto.manuscriptName)}</span></a>
		</div>

		<span style="font-size: xx-small; color: ${SITE_COLOR};"><!--
			<%-- 雇用形態 --%>
			<c:forEach items="${dto.employPtnList}" var="employValue">
				-->【${employPtnMap[employValue]}】<!--
			</c:forEach>
			<%--待遇検索条件 --%>
			<c:forEach items="${dto.treatmentKbnList}" var="treatmentValue">
				-->[${treatmentKbnMap[treatmentValue]}]&nbsp;<!--
			</c:forEach>
		--></span><br />

		<span style="font-size: xx-small; color: ${SITE_COLOR};">◆募集職種</span><br />
		<span style="font-size: xx-small;">
			${f:h(gf:removeCrToSpace(gf:replaceStr(dto.recruitmentJob, common['gc.webdata.common.trim.length'], common['gc.replaceStr2'])))}
		</span><br />

		<span style="font-size: xx-small; color: ${SITE_COLOR};">◆勤務地ｴﾘｱ･最寄駅</span><br />
		<span style="font-size: xx-small;">
			${f:h(gf:removeCrToSpace(gf:replaceStr(dto.workingPlace, common['gc.webdata.common.trim.length'], common['gc.replaceStr2'])))}
		</span><br />

		<span style="font-size: xx-small; color: ${SITE_COLOR};">◆給与</span><br />
		<span style="font-size: xx-small;">
			${f:h(gf:removeCrToSpace(gf:replaceStr(dto.salary, common['gc.webdata.common.trim.length'], common['gc.replaceStr2'])))}
		</span>

		<div style="font-size: xx-small; text-align: right;">
			<a href="#"><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};">→検討中BOXに追加</span></a>
		</div>

		<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

	</c:forEach>

	<div style="font-size: xx-small; text-align: center;">
		<img src="${f:h(frontHttpUrl)}${f:h(SITE_EMOJI)}128.gif" />&nbsp;<!--
		--><span style="color: #000000;">前へ&nbsp;</span><!--
		--><span style="color: #000000;">1&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">2</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">3</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">4</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#">5</a>&nbsp;</span><!--
		--><span style="color: ${f:h(SITE_TEXT_LINK_COLOR)};"><a href="#" accesskey="6">次へ</a>&nbsp;</span><!--
		--><img src="${f:h(frontHttpUrl)}${f:h(SITE_EMOJI)}130.gif" />
	</div>

</c:if>

<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

<div style="font-size: xx-small; text-align: right">
	<a href="#top"><span style="color: #CC0000;">▲ﾍﾟｰｼﾞﾄｯﾌﾟに戻る</span></a>
</div>

<hr style="border-color: ${SITE_COLOR}; background-color: ${SITE_COLOR}; height: 1px;" color="${SITE_COLOR}" />

<div style="font-size: xx-small;">
	<img src="${f:h(frontHttpUrl)}${f:h(SITE_EMOJI)}134.gif" /><a href="#" accesskey="0">ﾄｯﾌﾟﾍﾟｰｼﾞへ戻る</a>
</div>

</div>


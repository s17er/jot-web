<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>
<c:set var="employPtnMap" value="<%=MTypeConstants.EmployPtnKbn.employPtnSmallLabelMap %>"/>
<c:set var="treatmentKbnMap" value="<%=MTypeConstants.TreatmentKbn.treatmentKbnSmallLabelMap %>"/>

<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>" scope="page" />
<c:set var="SIZE_B" value="<%=MTypeConstants.SizeKbn.B %>" scope="page" />
<c:set var="SIZE_C" value="<%=MTypeConstants.SizeKbn.C %>" scope="page" />
<c:set var="SIZE_D" value="<%=MTypeConstants.SizeKbn.D %>" scope="page" />
<c:set var="SIZE_E" value="<%=MTypeConstants.SizeKbn.E %>" scope="page" />
<c:set var="SIZE_TEXT_WEB" value="<%=MTypeConstants.SizeKbn.TEXT_WEB %>" scope="page" />
<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />
<c:set var="ICON_SIZE" value="20" scope="page" />

<c:set var="catchCopyLength" value="47" scope="page" />
<c:set var="sentence1Length" value="35" scope="page" />
<c:set var="commonLength" value="19" scope="page" />
<c:set var="replaceOverLengthStr" value="${common['gc.replaceStr']}" scope="page" />

<gt:typeList name="dbWebAreaList" typeCd="<%=MTypeConstants.WebAreaKbn.getTypeCd((String)request.getAttribute(\"APPLICATION_AREA_CD\")) %>" noDisplayValue="<%=MTypeConstants.WebAreaKbn.getWebdataNoDispList((String)request.getAttribute(\"APPLICATION_AREA_CD\")) %>" />
<gt:typeList name="sizeKbnList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />
<gt:typeList name="dbJobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
<gt:typeList name="dbOtherConditionKbnList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD %>" />
<gt:typeList name="dbShopsKbnList" typeCd="<%=MTypeConstants.ShopsKbn.TYPE_CD %>" />
<gt:typeList name="dbForeignAreaList" typeCd="<%=MTypeConstants.ForeignAreaKbn.getTypeCd((String) request.getAttribute(\"APPLICATION_AREA_CD\")) %>" />

<c:set var="MATERIAL_KBN_MAIN1" value="<%=MTypeConstants.MaterialKbn.MAIN_1 %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_A" value="<%=MTypeConstants.MaterialKbn.PHOTO_A %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_B" value="<%=MTypeConstants.MaterialKbn.PHOTO_B %>" scope="page" />
<c:set var="MATERIAL_KBN_FREE" value="<%=MTypeConstants.MaterialKbn.FREE %>" scope="page" />

<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />


<!-- // 検索結果 -->
<div id="search">
	【検索条件】<br>
	検索条件は指定されていません：1件検索されました。
</div>

<html:errors/>


<div class="clearfix">
<div id="kensu">${f:h(currentDisplayNumStr)}</div>
<div id="refine" class="clearfix">
<html:link href="#" >→さらに絞込み</html:link>
</div>
</div>
<hr>



<!-- // ページ移動 -->

<div class="page clearfix">
	<ul>
		<li><span>前へ</span></li>
		<li><span>1</span></li>
		<li><span>次へ</span></li>
	</ul>
</div>
<hr>

<!-- // ページ移動 -->

<!-- // アイコン説明 -->
<div id="iconBox" class="clearfix">
<dl class="dropdown">
<dt><a href="#">アイコンの説明▼</a></dt>
<dd><ul>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy01.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;正社員</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy02.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;契約社員</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy03.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;アルバイト・パート</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy04.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;派遣社員</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy05.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;紹介</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy06.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;委託店長</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconTreatment01.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;社会保険完備</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconTreatment02.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;週休2日制（月8日以上）</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconTreatment03.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;独立支援制度有</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconTreatment04.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;借上住宅・寮完備</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconTreatment05.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;海外・研修</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconOtherCondition02.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;飲食業界未経験歓迎</li>
<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconOtherCondition03.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}">&nbsp;新卒者歓迎</li>
</ul></dd>
</dl>
</div>
<!-- // アイコン説明 -->

<c:forEach items="${dataList}" var="dto">

	<c:choose>
		<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION}">
			<c:set var="DETAIL_PREVIEW_PATH" value="${gf:makePathConcat2Arg('/detailPreview/smartPhoneDetail/displayDetailMsg', dto.id, inputFormKbn)}" scope="page"></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="DETAIL_PREVIEW_PATH" value="${gf:makePathConcat1Arg('/detailPreview/smartPhoneDbDetail/displayDetailMsg', dto.id)}" scope="page"></c:set>
		</c:otherwise>
	</c:choose>

	<h3>${f:h(dto.manuscriptName)}</h3>
	<div class="contents">
		<div class="industry">
			<span>【業態】</span>
			<c:forEach items="${dto.industryKbnList}" var="t" varStatus="status">
				${f:label(t, dbIndustryKbnList, 'value', 'label')}
				<c:if test="${not status.last}"> / </c:if>
			</c:forEach>
		</div>

		<ul class="icon clearfix">
			<c:forEach items="${dto.employPtnList}" var="t"  >
				<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy<fmt:formatNumber value="${f:h(t)}" pattern="00" />.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}"/></li>
			</c:forEach>
			<c:forEach items="${dto.treatmentKbnList}" var="t">
				<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconTreatment<fmt:formatNumber value="${f:h(t)}" pattern="00" />.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}"></li>
			</c:forEach>
			<c:forEach items="${dto.otherConditionKbnList}" var="t">
				<c:if test="${t ne 1}">
					<li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconOtherCondition<fmt:formatNumber value="${f:h(t)}" pattern="00" />.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}"></li>
				</c:if>
			</c:forEach>
		</ul>

        <c:if test="${not empty dto.topInterviewUrl}">
            <div class="storeInfo">
                <a href="${dto.topInterviewUrl}">インタビュー</a>
            </div>
        </c:if>

		<c:if test="${dto.sizeKbn ne SIZE_TEXT_WEB}">
			<div class="box clearfix">

				<c:if test="${dto.materialSearchListExistFlg eq true}">
					<c:choose>
						<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
							<c:set var="MAIN1_IMG_PATH"   value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_MAIN1,   dto.idForDirName)}" scope="page" />
							<c:set var="PHOTO_A_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_PHOTO_A, dto.idForDirName)}" scope="page" />
							<c:set var="PHOTO_B_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_PHOTO_B, dto.idForDirName)}" scope="page" />
							<c:set var="FREE_IMG_PATH"    value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_FREE,    dto.idForDirName)}" scope="page" />
						</c:when>
						<c:otherwise>
							<c:set var="MAIN1_IMG_PATH"      value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_MAIN1)}"    scope="page" />
							<c:set var="PHOTO_A_IMG_PATH"    value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_PHOTO_A)}"  scope="page" />
							<c:set var="PHOTO_B_IMG_PATH"    value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_PHOTO_B)}"  scope="page" />
							<c:set var="FREE_IMG_PATH"       value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_FREE)}"     scope="page" />
						</c:otherwise>
					</c:choose>

					<c:choose>
						<c:when test="${dto.sizeKbn eq  SIZE_E}">
							<c:set var="imageWidth" value="100%" scope="page" />
							<c:set var="sizeByDiv" value="photo03" scope="page"/>
						</c:when>
						<c:when test="${dto.sizeKbn eq SIZE_D}">
							<c:set var="imageWidth" value="100%" scope="page" />
							<c:set var="sizeByDiv" value="photo02" scope="page"/>
						</c:when>
						<c:otherwise>
							<c:set var="imageWidth" value="100%" scope="page" />
							<c:set var="sizeByDiv" value="photo01" scope="page"/>
						</c:otherwise>
					</c:choose>

					<div class="${f:h(sizeByDiv)}">
						<c:choose>
							<c:when test="${dto.sizeKbn eq  SIZE_E}">
								<a href="${f:url(DETAIL_PREVIEW_PATH)}"><img src="${f:url(FREE_IMG_PATH)}" width="${f:h(imageWidth)}" /></a>
							</c:when>
							<c:when test="${dto.sizeKbn eq SIZE_D}">
								<a href="${f:url(DETAIL_PREVIEW_PATH)}"><img src="${f:url(FREE_IMG_PATH)}" width="${f:h(imageWidth)}" /></a>
							</c:when>
							<c:otherwise>
								<a href="${f:url(DETAIL_PREVIEW_PATH)}"><img src="${f:url(MAIN1_IMG_PATH)}" width="${f:h(imageWidth)}" /></a>
							</c:otherwise>
						</c:choose>
					</div>

				</c:if>

				<h4>${gf:removeCrToSpace(dto.catchCopy)}</h4>
			</div>
		</c:if>
		<c:if test="${dto.sizeKbn eq SIZE_TEXT_WEB}">
			<div class="box clearfix">
				<h4>${gf:replaceStr(gf:removeCrToSpace(dto.catchCopy), catchCopyLength, common['gc.replaceStr'])}</h4>
			</div>
		</c:if>

		<div class="detail">
			<dl>
				<c:if test="${not empty dto.recruitmentJob}">
					<dt>◆募集職種</dt>
					<dd>
						${f:h(gf:replaceStr(gf:removeCrToSpace(dto.recruitmentJob), commonLength, common['gc.replaceStr']))}
					</dd>
				</c:if>
				<c:if test="${not empty dto.salary}">
					<dt>◆給与</dt>
					<dd>
						${f:h(gf:replaceStr(gf:removeCrToSpace(dto.salary), commonLength, common['gc.replaceStr']))}
					</dd>
				</c:if>
				<c:if test="${not empty dto.workingPlace}">
					<dt>◆勤務地エリア・最寄駅</dt>
					<dd>
						${f:h(gf:replaceStr(gf:removeCrToSpace(dto.workingPlace), commonLength, common['gc.replaceStr']))}
					</dd>
				</c:if>
			</dl>
		</div>

		<div class="more">
			<a href="${f:url(DETAIL_PREVIEW_PATH)}">詳細を見る</a>
		</div>
	</div>

</c:forEach>

<!-- // ページ移動 -->
<div class="page clearfix">
	<ul>
		<li><span>前へ</span></li>
		<li><span>1</span></li>
		<li><span>次へ</span></li>
	</ul>
</div>
<!-- // ページ移動 -->

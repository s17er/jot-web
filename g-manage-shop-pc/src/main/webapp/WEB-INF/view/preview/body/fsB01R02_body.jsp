<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />

<%-- フッター帯の部分をインクルード --%>
<jsp:include page="/WEB-INF/view/preview/smart/fsB01R01_foorter.jsp" />
<%-- 共通部分をインクルード --%>
<jsp:include page="fsB01R_header.jsp" />

<c:set var="MATERIAL_KBN_RIGHT" value="<%=MTypeConstants.MaterialKbn.RIGHT %>" scope="page"/>

<c:set var="MATERIAL_KBN_PHOTO_A" value="<%=MTypeConstants.MaterialKbn.PHOTO_A %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_B" value="<%=MTypeConstants.MaterialKbn.PHOTO_B %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_C" value="<%=MTypeConstants.MaterialKbn.PHOTO_C %>" scope="page" />
<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />

<c:set var="SIZE_B" value="<%=MTypeConstants.SizeKbn.B %>" scope="page" />
<c:set var="SIZE_C" value="<%=MTypeConstants.SizeKbn.C %>" scope="page" />
<c:set var="SIZE_D" value="<%=MTypeConstants.SizeKbn.D %>" scope="page" />
<c:set var="SIZE_E" value="<%=MTypeConstants.SizeKbn.E %>" scope="page" />
<c:set var="SIZE_TEXT_WEB" value="<%=MTypeConstants.SizeKbn.TEXT_WEB %>" scope="page" />

<c:set var="MATERIAL_KBN_MAIN1" value="<%=MTypeConstants.MaterialKbn.MAIN_1 %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN2" value="<%=MTypeConstants.MaterialKbn.MAIN_2 %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN3" value="<%=MTypeConstants.MaterialKbn.MAIN_3 %>" scope="page" />
<c:set var="MATERIAL_KBN_ATTENTION_HERE" value="<%=MTypeConstants.MaterialKbn.ATTENTION_HERE %>" scope="page" />

<jsp:include page="/WEB-INF/view/preview/smart/headerContents.jsp" />

<c:if test="${imageCheckMap[MATERIAL_KBN_LOGO]}">
	<div id="companyLogo">
		<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_LOGO))}" />
	</div>
</c:if>


<c:set var="detailPath" value="${gf:makePathConcat3Arg('/detailPreview/smartDraftDetail/index', id, accessCd, areaCd)}" scope="page" />


<c:if test="${catchCopyExist}">
		<h4>${catchCopy}</h4>
</c:if>

<c:if test="${blockMainImage and blockMainImageExist}">
	<c:choose>
		<c:when test="${sizeKbnDE}">
			<c:choose>
				<c:when test="${pluralMainImage}">
					<div id="mainImages" class="mainImageInit">
						<div class="flexslider">
							<ul class="slides">
								<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
								<li id="mainImage0">
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN1))}"   width="300" height="227"/>
								</li>
								</c:if>
								<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN2]}">
								<li id="mainImage1">
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN2))}"   width="300" height="227"/>
								</li>
								</c:if>
								<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN3]}">
								<li id="mainImage2">
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN3))}"   width="300" height="227"/>
								</li>
								</c:if>
							</ul>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div id="mainPict">
						<c:choose>
							<c:when test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
								<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN1))}"   width="280" height="212"/>
							</c:when>
							<c:when test="${imageCheckMap[MATERIAL_KBN_MAIN2]}">
								<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN2))}"   width="280" height="212"/>
							</c:when>
							<c:when test="${imageCheckMap[MATERIAL_KBN_MAIN3]}">
								<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN3))}"   width="280" height="212"/>
							</c:when>
						</c:choose>
					</div>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
				<div id="mainPict">
						<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN1))}"   width="280" height="212"/>
				</div>
			</c:if>
		</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${not empty sentence1}">
	<div id="freeMain">${f:br(sentence1)}</div>
</c:if>

<c:if test="${blockRightForSmart eq true and blockRightExistForSmart}">
	<div class="freeSub clearfix">
		<c:if test="${blockRight and imageCheckMap[MATERIAL_KBN_RIGHT]}">
			<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_RIGHT))}"  width="144" height="200"/>
		</c:if>
		<c:if test="${not empty sentence2}">
			${f:br(sentence2)}
		</c:if>
	</div>
</c:if>
	<c:if test="${sizeKbnDE eq true or sizeKbn eq SIZE_C}">
		<div class="pictBox">
			<div class="pictHeading">
				<ul class="pictTab">
					<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_A]}">
						<li><a href="#tab1" class="selected">1</a></li>
					</c:if>
					<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_B]}">
						<li><a href="#tab2">2</a></li>
					</c:if>
					<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_C]}">
						<li><a href="#tab3">3</a></li>
					</c:if>
				</ul>
			</div>
			<ul class="panel01">
				<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_A]}">
					<li id="tab1"><figure>
						<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_A))}"  width="235" height="170"/>
						<c:if test="${not empty captionA}"><figcaption>${f:br(captionA)}</figcaption></c:if>
					</figure></li>
				</c:if>
				<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_B]}">
					<li id="tab2"><figure>
						<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_B))}"  width="235" height="170"/>
						<c:if test="${not empty captionB}"><figcaption>${f:br(captionB)}</figcaption></c:if>
					</figure></li>
				</c:if>
				<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_C]}">
					<li id="tab3"><figure>
						<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_C))}"  width="235" height="170"/>
						<c:if test="${not empty captionC}"><figcaption>${f:br(captionC)}</figcaption></c:if>
					</figure></li>
				</c:if>
			</ul>
		</div>
	<c:if test="${not empty sentence3 and blockSentence3}">
		<div class="freeSub clearfix">
			${f:br(sentence3)}
		</div>
	</c:if>
	</c:if>

	<c:if test="${blockAttentionHere and blockAttentionHereExist}">
<div id="attention" class="clearfix">
<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_ATTENTION_HERE))}" alt="ここに注目" width="175" height="150" />
<div id="attentionTxt">
<img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/attention.png">
<h5>${attentionHereTitle}</h5>
${f:br(attentionHereSentence)}
</div>
</div>
	</c:if>

	<c:if test="${not empty movieUrl}">
	<!--動画で見るstart-->
	<div id="movie">
		<p class="movie_title">
			<img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/movie_title.png" alt="動画で分かるこのお店・会社の魅力">
		</p>
		<iframe class="movie_iframe" width="300" height="220" src="${movieUrl}?rel=0&showinfo=0" frameborder="0" allowfullscreen></iframe>
		<c:if test="${not empty movieComment}">
			<div class="movie_area">
				<p class="movie_text">${f:br(movieComment)}
			</div>
		</c:if>
	</div>
	<!--動画で見るend-->
</c:if>

<jsp:include page="../smart/detailFooterTab.jsp" />

	<%-- 応募の部分をインクルード --%>
	<jsp:include page="fsB01R_application.jsp" />










<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="SHOPLIST_MATERIALKBN_MAIN1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1%>" scope="page" />
<jsp:include page="/WEB-INF/view/preview/body/fsB01R_header.jsp" />
<gt:typeList name="industryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<c:set var="arbeitDetailUrl" value="${common['arbeit.shoplist.detailUrl']}" scope="page" />
<c:set var="JOB_OFFER_FLG_ARI" value="<%=MTypeConstants.JobOfferFlg.ARI %>" scope="page" />
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/shoplist.css">
<link rel="stylesheet" href="${f:h(frontHttpUrl)}/ipx${f:h(cssLocation)}/storeinfo.css">

<div id="contents">
	<div class="clearfix">
		<p class="shop_name">${f:h(shopName)}</p>
		<p class="shop_industry">
			<a href="#">${f:label(industryKbn1, industryKbnList, 'value', 'label')}</a>
		</p>
		<p class="baito_link">
			<c:if test="${JOB_OFFER_FLG_ARI eq jobOfferFlg}">
				<a target="_blank" href="${f:h(arbeitPreviewPath)}">
				<img width="40%" src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/genkou/btnGdeb_sp.png"></a>
			</c:if>
		</p>
	</div>

	<p>
		<c:if test="${not empty imagePath}">
			<img width="100%" src="${f:url(imagePath)}">
		</c:if>
	</p>
	<p class="main_article">${f:br(f:h(shopInformation))}</p>

	<dl class="accordionSlider tenpo_group">
		<dt class="tenpo_data open_tab">店舗DATA</dt>
		<dd class="accordion_group">
			<dl class="data_detail">
				<c:if test="${not empty address1}">
					<dt>住所</dt>
					<dd>${f:h(address1)}${f:h(address2)}</dd>
				</c:if>
				<c:if test="${not empty transit}">
					<dt>交通</dt>
					<dd>${f:br(f:h(transit))}</dd>
				</c:if>
				<c:if test="${existPhoneNo}">
					<dt>TEL</dt>
					<dd>${f:h(phoneNo1)}-${f:h(phoneNo2)}-${f:h(phoneNo3)}</dd>
				</c:if>
				<c:if test="${existFaxNo}">
					<dt>FAX</dt>
					<dd>${f:h(faxNo1)}-${f:h(faxNo2)}-${f:h(faxNo3)}</dd>
				</c:if>
				<c:if test="${not empty seating}">
					<dt>座席</dt>
					<dd>${f:br(f:h(seating))}</dd>
				</c:if>
				<c:if test="${not empty staff}">
					<dt>スタッフ</dt>
					<dd>${f:br(f:h(staff))}</dd>
				</c:if>
				<c:if test="${not empty unitPrice}">
					<dt>客単価</dt>
					<dd>${f:br(f:h(unitPrice))}</dd>
				</c:if>
				<c:if test="${not empty holiday}">
					<dt>定休日</dt>
					<dd>${f:br(f:h(holiday))}</dd>
				</c:if>
				<c:if test="${not empty businessHours}">
					<dt>営業時間</dt>
					<dd>${f:br(f:h(businessHours))}</dd>
				</c:if>
				<c:if test="${not empty url1}">
					<dt>URL</dt>
					<dd>
						<a target="_blank" href="${f:h(url1)}">${f:h(url1)}/</a>
					</dd>
				</c:if>
			</dl>
		</dd>
	</dl>
	<p class="job_list_title">この企業が募集している職種</p>
	<ul class="job_detail">
		<c:if test="${not empty web.recruitmentJob}">
			<li class="job_detail_01">募集職種</li>
			<li class="job_detail_02">${f:br(f:h(web.recruitmentJob))}</li>
		</c:if>
		<c:if test="${not empty web.salary}">
			<li class="job_detail_01">給与</li>
			<li class="job_detail_02">${f:br(f:h(web.salary))}</li>
		</c:if>
		<c:if test="${not empty web.treatment}">
			<li class="job_detail_01">待遇</li>
			<li class="job_detail_02">${f:br(f:h(web.treatment))}</li>
		</c:if>
		<c:if test="${not empty web.holiday}">
			<li class="job_detail_01">休日休暇</li>
			<li class="job_detail_02">${f:br(f:h(web.holiday))}</li>
		</c:if>
		<li><p class="link_btn_red">
				<a href="${f:url(webDetailPath)}">求人詳細はこちら</a>
			</p></li>
	</ul>
</div>

	<div class="shop_link_area">
		<ul>
			<c:if test="${not empty arbeitTodouhukenId and not empty arbeitAreaId}">
				<c:choose>
					<c:when test="${sendaiPkgFlg and not empty arbeitSubAreaId}">
						<ar:arbeitSubAreaList name="arbeitSubAreaList" areaId="${arbeitAreaId}" todouhukenId="${arbeitTodouhukenId}" />
						<c:set var="areaLabel" value="${f:label(arbeitSubAreaId, arbeitSubAreaList, 'value', 'label')}" />
					</c:when>
					<c:otherwise>
						<ar:arbeitAreaList name="arbeitAreaList"  todouhukenId="${arbeitTodouhukenId}" />
						<c:set var="areaLabel" value="${f:label(arbeitAreaId, arbeitAreaList, 'value', 'label')}" />
					</c:otherwise>
				</c:choose>
				<li>
					<a href="#">${f:h(areaLabel)}の飲食求人情報はこちら</a>
				</li>
			</c:if>
			<c:forEach items="${routeList}" var="route">
				<c:if test="${not empty route.railroadId and not empty route.routeId and not empty route.stationId}">
					<li>
						<ar:stationList name="stationList" routeId="${route.routeId}" todouhukenId="${not empty arbeitTodouhukenId ? arbeitTodouhukenId : TOKYO_ID}" />
						<ar:routeList name="routeList" railloadId="${route.railroadId}" todouhukenId="${not empty arbeitTodouhukenId ? arbeitTodouhukenId : TOKYO_ID}" />
						<a href="#">${f:label(route.routeId, routeList, 'value', 'label')}${f:label(route.stationId, stationList, 'value', 'label')}駅の飲食求人情報はこちら</a>
					</li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
<script src="${f:h(frontHttpUrl)}/ipx/js/accordionSlider.js" charset="utf-8"></script>






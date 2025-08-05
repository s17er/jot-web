<%@page pageEncoding="UTF-8"%>


<div class="contentsright">
	<div class="shopdetails">
		<div class="shopname">
			<h3>
				${f:h(shopName)}<br />
				<span>
					[&nbsp;<strong><a href="#">${f:label(shopListIndustryKbn1 , dbIndustryList, 'value', 'label')}</a></strong>&nbsp;]
				</span>
			</h3>
		</div>
		<div class="mark">
			<c:if test="${jobOfferFlg eq EXIST_JOB_OFFER_FLG}">
				<a href="${f:h(arbeitPreviewPath)}" target="_blank"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/btnGdeb.png" /></a>
			</c:if>
		</div>
		<div class="mark_detail">
			<c:choose>
				<c:when test="${webDataDisplayFlg eq true}">
					<c:set var="detailPath" value="${f:url(RETURN_DETAIL_PATH)}" />
				</c:when>
				<c:otherwise>
					<c:set var="detailPath" value="#" />
				</c:otherwise>
			</c:choose>
			<a href="${detailPath}"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_displayDetail.gif" alt="この企業が募集している求人はこちら" /></a>
		</div>
		<div class="shop clear">
			<c:if test="${shopListImageCheckMap[SHOPLIST_MATERIALKBN_MAIN1]}">
				<div class="shopphoto">
					<img src="${f:url(gf:makePathConcat3Arg('/util/imageUtility/displayShopListImageCache', shopListId, SHOPLIST_MATERIALKBN_MAIN1, shopListUniqueKeyMap[SHOPLIST_MATERIALKBN_MAIN1]))}" width="330px" height="250px" />
				</div>
			</c:if>
		</div>
	</div>
	<p class="data_title">店舗DATA</p>
	<div class="shopinfomaition">
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<th width="90px">住所</th>
				<td width="445px">${f:h(address1)}${f:h(address2)}</td>
			</tr>
			<c:if test="${not empty transit}">
				<tr>
					<th>交通</th>
					<td>${f:br(f:h(transit))}</td>
				</tr>
			</c:if>
			<c:if test="${phoneNoExist}">
				<tr>
					<th>TEL</th>
					<td>${f:h(phoneNo)}</td>
				</tr>
			</c:if>
			<c:if test="${faxNoExist}">
		        <tr>
		          <th>FAX</th>
		          <td>${f:h(faxNo)}</td>
		        </tr>
	        </c:if>
			<c:if test="${not empty seating}">
				<tr>
					<th>席数</th>
					<td>${f:br(f:h(seating))}</td>
				</tr>
			</c:if>
			<c:if test="${not empty staff}">
		        <tr>
		          <th>スタッフ</th>
		          <td>${f:br(f:h(staff))}</td>
		        </tr>
	        </c:if>
	        <c:if test="${not empty unitPrice}">
				<tr>
					<th>客単価</th>
					<td>${f:br(f:h(unitPrice))}</td>
				</tr>
	        </c:if>
	        <c:if test="${not empty shopHoliday}">
				<tr>
					<th>定休日</th>
					<td>${f:br(f:h(shopHoliday))}</td>
				</tr>
	        </c:if>
	        <c:if test="${not empty businessHours}">
				<tr>
					<th>営業時間</th>
					<td>${f:br(f:h(businessHours))}</td>
				</tr>
	        </c:if>
	        <c:if test="${not empty url1}">
				<tr>
					<th style="border: 0">URL</th>
					<td style="border: 0"><a href="${f:h(url1)}" target="_blank">${f:h(url1)}</a></td>
				</tr>
	        </c:if>
		</table>
	</div>
	<p class="shoptext">${f:br(f:h(shopInformation))}</p>

	<c:if test="${not empty recruitmentJob or not empty salary or not empty treatment or not empty holiday}">
		<div class="job_group">
			<p class="job_group_title">この企業が募集している職種</p>
			<ul class="job_data">
				<c:if test="${not empty recruitmentJob}">
					<li class="job_title">募集職種</li>
					<li class="job_detail">${f:br(f:h(recruitmentJob))}</li>
				</c:if>
				<c:if test="${not empty salary}">
					<li class="job_title">給与</li>
					<li class="job_detail">${f:br(f:h(salary))}</li>
				</c:if>
				<c:if test="${not empty treatment}">
					<li class="job_title">待遇</li>
					<li class="job_detail">${f:br(f:h(treatment))}</li>
				</c:if>
				<c:if test="${not empty holiday}">
					<li class="job_title">休日休暇</li>
					<li class="job_detail_bottom">${f:br(f:h(holiday))}</li>
				</c:if>
			</ul>
		</div>
	</c:if>

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
</div>
<%@page import="com.gourmetcaree.common.constants.StringLengthConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="JOB_OFFER_ARI" value="<%=MTypeConstants.JobOfferFlg.ARI %>" scope="page" />
<c:set var="arbeitDetailUrl" value="${common['arbeit.shoplist.detailUrl']}" scope="page" />
<c:set var="REPLACE_TRIM_STR" value="${common['gc.replaceStr']}" scope="request" />
<c:set var="SHOP_NAME_LENGTH" value="<%=StringLengthConstants.ShopList.SMART_LIST_SHOP_NAME %>" />
<c:set var="TEXT_LENGTH" value="<%=StringLengthConstants.ShopList.SMART_LIST_TEXT %>" />
<c:set var="ADDRESS_LENGTH" value="<%=StringLengthConstants.ShopList.SMART_LIST_ADDRESS %>" />
<c:set var="TRANSIT_LENGTH" value="<%=StringLengthConstants.ShopList.SMART_LIST_TRANSIT %>" />
<c:set var="arbeitDetailUrl" value="${common['arbeit.shoplist.detailUrl']}" scope="page" />
<gt:typeList name="industryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD%>" />

<c:if test="${existDataFlg}">
<c:forEach items="${relationDtoList}" var="dto">
	<div class="shop_data_wrapper">
		<div class="shop_data_detail">
			<a href="${f:url(gf:makePathConcat4Arg('/shopListPreview/smartDraftDetail', webId, dto.id, accessCd, areaCd))}">
				<h3 id="shop_title">${f:h(dto.shopName)}</h3>
			</a>
			<div class="info_top_box">
				<p class="catch">${f:h(gf:replaceStr(dto.shopInformation, TEXT_LENGTH, REPLACE_TRIM_STR))}</p>
				<div class="image_area">
					<a href="${f:url(gf:makePathConcat4Arg('/shopListPreview/smartDraftDetail', webId, dto.id, accessCd, areaCd))}">
					<c:choose>
						<c:when test="${dto.existImageFlg}">
							<img src="${f:url(dto.mobileImageUrl)}" />
						</c:when>
						<c:otherwise>
							<img src="${f:h(frontHttpUrl)}/ipx${f:h(imagesLocation)}/search/main.jpg" />
						</c:otherwise>
					</c:choose>
					</a>
				</div>
				<div class="info_area">
					<dl>
						<dt>
							<img src="${f:h(frontHttpUrl)}/ipx${f:h(imagesLocation)}/shoplist/icon_cook.jpg">
						</dt>
						<dd>
							${not empty fn:trim(recruitmentJob) ? f:h(gf:replaceStr(recruitmentJob, TRANSIT_LENGTH, REPLACE_TRIM_STR)) : '&nbsp;'}
						</dd>
						<dt>
							<img src="${f:h(frontHttpUrl)}/ipx${f:h(imagesLocation)}/shoplist/icon_yen.jpg">
						</dt>
						<dd>
							${not empty fn:trim(salary) ? f:h(gf:replaceStr(salary, TRANSIT_LENGTH, REPLACE_TRIM_STR)) : '&nbsp;'}
						</dd>
						<dt>
							<img src="${f:h(frontHttpUrl)}/ipx${f:h(imagesLocation)}/shoplist/icon_train.jpg">
						</dt>
						<dd>
							${not empty fn:trim(dto.transit) ? f:h(gf:replaceStr(dto.transit, TRANSIT_LENGTH, REPLACE_TRIM_STR)) : '&nbsp;'}
						</dd>
					</dl>
				</div>
			</div>
		</div>
		<p class="shop_industry">
			<a href="#">${f:label(dto.industryKbn1, industryKbnList, 'value', 'label')}</a>
		</p>
	</div>
</c:forEach>

</c:if>
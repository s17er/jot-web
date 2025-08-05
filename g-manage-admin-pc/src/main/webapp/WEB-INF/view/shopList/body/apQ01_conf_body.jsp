<%--
店舗データ登録・編集 確認
 --%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey" %>


<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" scope="request"/>
<c:set var="LAT_LNG_KBN_ADDRESS" value="<%=MTypeConstants.ShopListLatLngKbn.ADDRESS %>" scope="page" />
<c:set var="LAT_LNG_KBN_LAT_LNG" value="<%=MTypeConstants.ShopListLatLngKbn.LAT_LNG %>" scope="page" />
<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="page" />
<c:set var="DOMESTIC"  value="<%=MTypeConstants.DomesticKbn.DOMESTIC %>" scope="page"/>
<c:set var="ORVERSEAS"  value="<%=MTypeConstants.DomesticKbn.overseas %>" scope="page"/>

<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<gt:typeList name="salesPerCustomerKbnList" typeCd="<%=MTypeConstants.SalesPerCustomerKbn.TYPE_CD %>" />
<gt:typeList name="seatKbnList" typeCd="<%=MTypeConstants.SeatKbn.TYPE_CD %>" />
<gt:typeList name="WorkCharacteristicKbnList" typeCd="<%=MTypeConstants.WorkCharacteristicKbn.TYPE_CD %>"/>
<gt:typeList name="ShopCharacteristicKbnList" typeCd="<%=MTypeConstants.ShopCharacteristicKbn.TYPE_CD %>"/>

<%-- CSS,JSのインクルード --%>
<jsp:include page="/WEB-INF/view/shopList/resource/apQ01_common_resource.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/shopList/resource/apQ01_input_resource.jsp"></jsp:include>

<style>
	img.thumbnail_image {
		width:200px;
		object-fit: contain;
	}
</style>

<div id="main">
	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(gf:makePathConcat1Arg(navigationPath2, customerId))}">${f:h(navigationText2)}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:otherwise>
		</c:choose>

	</ul>
	<!-- #navigation# -->
	<gt:convertToCustomerName customerId="${customerId}" name="customName"/>
	<h2 title="${f:h(pageTitle1)}" class="shopListTitle" id="${f:h(pageTitleId1)}">${gf:replaceStr(customName, common['gc.shopList.customerName.trim.length'], common['gc.replaceStr'])}</h2>
	<hr />

	<s:form action="${f:h(actionPath)}">
		<html:hidden property="latitude" styleId="latitude"/>
		<html:hidden property="longitude" styleId="longitude"/>
		<c:if test="${pageKbn ne PAGE_INPUT}">
			<html:hidden property="id"/>
		</c:if>

		<h3 class="subtitle">基本情報</h3>

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
			<tr>
				<th width="155">店舗名&nbsp;</th>
				<td>
					${f:h(shopName)}
				</td>
			</tr>
			<tr>
				<th>業態【HP表示用】</th>
				<td>${f:h(industryText)}</td>
			</tr>
			<tr>
				<th>業態【検索用】</th>
				<td>
					業態1：${f:label(industryKbn1, selectIndustryList, 'value', 'label')}<br /><br />
					業態2：${f:label(industryKbn2, selectIndustryList, 'value', 'label')}
				</td>
			</tr>
			<tr>
				<th>キャッチコピー</th>
				<td>${f:h(catchCopy)}</td>
			</tr>
			<tr>
				<th>テキスト</th>
				<td>${f:br(f:h(shopInformation))}</td>
			</tr>
			<tr>
				<th>仕事の特徴</th>
				<td>
					<gt:typeList name="workCharacteristicKbnList" typeCd="<%=MTypeConstants.WorkCharacteristicKbn.TYPE_CD %>"/>
					<ul class="checklist_2col clear">
						<c:forEach items="${workCharacteristicKbnArray}" var="t" varStatus="status">
							<li>
								<label>${f:label(t, workCharacteristicKbnList, 'value', 'label')}</label>
							</li>
						</c:forEach>
					</ul>
				</td>
			</tr>
			<tr>
				<th>職場</th>
				<td>
					<gt:typeList name="shopCharacteristicKbnList" typeCd="<%=MTypeConstants.ShopCharacteristicKbn.TYPE_CD %>"/>
					<ul class="checklist_2col clear">
						<c:forEach items="${shopCharacteristicKbnArray}" var="t" varStatus="status">
							<li>
								<label>${f:label(t, shopCharacteristicKbnList, 'value', 'label')}</label>
							</li>
						</c:forEach>
					</ul>
				</td>
			</tr>
			<tr>
				<th>住所</th>
				<td>
					<c:if test="${domesticKbn == DOMESTIC}">
						<gt:prefecturesList name="prefList" />
						<gt:cityList name="cityList" prefecturesCd="${prefecturesCd}" />
						${f:label(prefecturesCd, prefList, 'value', 'label')}${f:label(cityCd, cityList, 'value', 'label')}${f:h(address)}
					</c:if>
					<c:if test="${domesticKbn == ORVERSEAS}">
						<gt:typeList name="foreignAreaList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>"/>
						${f:label(shutokenForeignAreaKbn, foreignAreaList, 'value', 'label')}<br>
						${f:h(foreignAddress)}
					</c:if>
				</td>
			</tr>
			<tr>
				<th>交通</th>
				<td>${f:br(f:h(transit))}</td>
			</tr>
			<tr class="station">
				<th>最寄駅【検索用】</th>
				<td>
					<c:if test="${not empty stationDtoList}">
						<div id="sortable">
						<gt:typeList name="transportationKbnList" typeCd="<%=MTypeConstants.TransportationKbn.TYPE_CD %>" />
						<c:forEach items="${stationDtoList}" var="stationDto" varStatus="status">
							<div class="wrapStation activeWrapStation">
								<table class="station_table sort-elements">
									<tr>
										<td class="dot">
											${stationDto.companyName}
										</td>
										<td style="width:200px; height:26px;">
											${stationDto.lineName}
										</td>
										<td>
											${stationDto.stationName}
										</td>
										<td>
											${f:label(stationDto.transportationKbn, transportationKbnList, 'value', 'label')}
											&nbsp;&nbsp;${f:h(stationDto.timeRequiredMinute)}&nbsp;分
										</td>
										<td></td>
									</tr>
								</table>
							</div>
						</c:forEach>
						</div>
					</c:if>
				</td>
			</tr>
			<tr>
				<th>TEL</th>
				<td>
					${f:h(csvPhoneNo)}
				</td>
			</tr>
			<tr>
				<th>席数</th>
				<td>${f:h(f:label(seatKbn,seatKbnList,'value','label'))}</td>
			</tr>
			<tr>
				<th>スタッフ</th>
				<td>${f:br(f:h(staff))}</td>
			</tr>
			<tr>
				<th>客単価</th>
				<td>${f:h(f:label(salesPerCustomerKbn,salesPerCustomerKbnList,'value','label'))}</td>
			</tr>
			<tr>
				<th>定休日</th>
				<td>${f:br(f:h(holiday))}</td>
			</tr>
			<tr>
				<th>営業時間</th>
				<td>${f:br(f:h(businessHours))}</td>
			</tr>
			<tr>
				<th>オープン日</th>
				<td>
					<c:if test="${not empty openDateYear}">
						${f:h(openDateYear)}年　${f:h(openDateMonth)}月&nbsp;&nbsp;
					</c:if>
					<c:if test="${not empty openDateNote}">
						備考:${f:h(openDateNote)}<br /><br />
					</c:if>
					<c:if test="${not empty openDateLimitDisplayDate}">
						公開期限:${f:h(openDateLimitDisplayDate)}
					</c:if>
				</td>
			</tr>
			<tr>
				<th>URL&nbsp;</th>
				<td>${f:h(url1)}</td>
			</tr>
			<tr>
				<th></th>
				<c:set var="MATERIAL_KBN_MAIN_1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1 %>" scope="request" />
				<td class="photo">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_DETAIL}">
						<c:if test="${materialExistsDto.isMain1ExistFlg}">
							<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" data-lightbox="main1" title="メイン1">
								<img class="thumbnail_image" alt="メイン1" src="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" /><br /><br />
							</a>
						</c:if>
					</c:when>
					<c:when test="${not empty mainImgSelect}">
						<c:forEach var="image" items="${viewDto.imageList}">
							<c:if test="${image.id eq mainImgSelect}">
								<a href="${f:url(image.filePath)}" title="メイン1">
									<img class="thumbnail_image" data-lightbox="main1" src="${f:url(image.filePath)}" />
								</a>
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:if test="${gf:isMapExsists(materialMap, MATERIAL_KBN_MAIN_1)}">
							<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" data-lightbox="main1" title="メイン1">
								<img class="thumbnail_image" alt="メイン1" src="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" /><br /><br />
							</a>
						</c:if>
					</c:otherwise>
				</c:choose>
				</td>
			</tr>
			<tr>
				<th>インディードタグ</th>
				<td>
				${f:h(tagList)}
				</td>
			</tr>
			<tr>
				<th>受動喫煙対策</th>
				<td>${f:h(preventSmoke)}</td>
			</tr>
		</table>

		<c:if test="${not empty displayConditionDtoList}">
			<h3 class="subtitle">表示条件変更</h3>
		</c:if>
		<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
		<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
		<gt:typeList name="saleryStructureList" typeCd="<%=MTypeConstants.SaleryStructureKbn.TYPE_CD %>" blankLineLabel="--" />
		<c:set var="SALERY_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>"></c:set>
		<c:set var="SALERY_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>"></c:set>

		<div class="ui-sortable">
			<c:forEach items="${displayConditionDtoList}" var="jobDto">
				<c:set var="wrapId" value="${jobDto.employPtnKbn}-${jobDto.jobKbn}" />
				<div class="wrapJob activeWrapJob">
					<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" style="margin-bottom: 10px">
						<tbody>
							<tr>
								<td width="100">
									${f:label(jobDto.employPtnKbn, employPtnList, 'value', 'label')}
								</td>
								<td>
									${f:label(jobDto.jobKbn, jobList, 'value', 'label')}
								</td>
								<td width="260" class="posi_center">
									<input type="button" name="occuNew" value="表示条件を確認" class="occuAddBtn" data-fancybox="modal" data-src="#modal${wrapId}">
								</td>
							</tr>
						</tbody>
					</table>
					<div id="modal${wrapId}" style="display: none;">
						<h4 class="occupation clear">【${f:label(jobDto.employPtnKbn, employPtnList, 'value', 'label')}】${f:label(jobDto.jobKbn, jobList, 'value', 'label')}</h4>
						<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
							<tbody>
							<tr>
								<th>給与</th>
								<td class="release">
									<c:if test="${not empty jobDto.lowerSalaryPrice || not empty jobDto.upperSalaryPrice}">
										${f:label(jobDto.saleryStructureKbn, saleryStructureList, 'value', 'label')}
										&nbsp;&nbsp;
										<c:out value="${jobDto.lowerSalaryPrice}"></c:out>&nbsp;${empty jobDto.lowerSalaryPrice ? '' : '円'}
										　～　
										<c:out value="${jobDto.upperSalaryPrice}"></c:out>&nbsp;${empty jobDto.upperSalaryPrice ? '' : '円'}
										<br><br>
										${f:br(f:h(jobDto.salaryDetail))}
									</c:if>
									<c:if test="${not empty jobDto.salary}">
										<br><br>
										${f:br(f:h(jobDto.salary))}
									</c:if>
								</td>
							</tr>
							<tr>
								<th>給与2<br>(想定初年度年収)</th>
								<td class="release">
									<c:if test="${not empty jobDto.annualLowerSalaryPrice || not empty jobDto.annualUpperSalaryPrice}">
										年収
										&nbsp;&nbsp;
										<c:out value="${jobDto.annualLowerSalaryPrice}"></c:out>&nbsp;${empty jobDto.annualLowerSalaryPrice ? '' : '円'}
										　～　
										<c:out value="${jobDto.annualUpperSalaryPrice}"></c:out>&nbsp;${empty jobDto.annualUpperSalaryPrice ? '' : '円'}
										<br><br>
										${f:br(f:h(jobDto.annualSalaryDetail))}
									</c:if>
									<c:if test="${not empty jobDto.annualSalary}">
										<br><br>
										${f:br(f:h(jobDto.annualSalary))}
									</c:if>
								</td>
							</tr>
							<tr>
								<th>給与3<br>(想定初年度月収)</th>
								<td class="release">
									<c:if test="${not empty jobDto.monthlyLowerSalaryPrice || not empty jobDto.monthlyUpperSalaryPrice}">
										月収
										&nbsp;&nbsp;
										<c:out value="${jobDto.monthlyLowerSalaryPrice}"></c:out>&nbsp;${empty jobDto.monthlyLowerSalaryPrice ? '' : '円'}
										　～　
										<c:out value="${jobDto.monthlyUpperSalaryPrice}"></c:out>&nbsp;${empty jobDto.monthlyUpperSalaryPrice ? '' : '円'}
										<br><br>
										${f:br(f:h(jobDto.monthlySalaryDetail))}
									</c:if>
									<c:if test="${not empty jobDto.monthlySalary}">
										<br><br>
										${f:br(f:h(jobDto.monthlySalary))}
									</c:if>
								</td>
							</tr>
							</tbody>
						</table>
						<div class="wrap_btn">
							<input type="button" name="conf" class="fcclose" value="閉じる" onclick="parent.jQuery.fancybox.close();">
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
		<br>

		<div class="wrap_btn">
			<c:choose>
				<c:when test="${pageKbn eq PAGE_INPUT}">
					<html:submit property="submit" value="登録" />
					<html:submit property="correct" value="訂正" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_DETAIL}">
					<html:button property="edit" onclick="location.href='${f:url(gf:makePathConcat2Arg('/shopList/edit/index', customerId, id))}'" value="編集" />
					<html:button property="copy" onclick="copyConf('processFlg', 'inputForm');" value="コピー" />
					<html:hidden property="customerId"/>
					<html:hidden property="version"/>
					<html:submit property="delete" onclick="return confirm('削除してよろしいですか？')?true:false" value="削除" />
					<html:submit property="backToList" value="戻る"/>
				</c:when>
				<c:when test="${pageKbn eq PAGE_EDIT}">
					<html:submit property="submit" value="更新" />
					<html:submit property="correct" value="訂正" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_OTHER}">
					<html:submit property="submitDetail" value="更新" />
					<html:submit property="correct" value="訂正" />
				</c:when>
			</c:choose>
		</div>
	</s:form>
	<s:form action="${f:h(copyActionPath)}" enctype="multipart/form-data" styleId="inputForm" >
		<html:hidden property="id" value="${f:h(id)}" />
		<html:hidden property="customerId" value="${customerId}" />
		<html:hidden property="processFlg" styleId="processFlg" />
	</s:form>
</div>
<%--
店舗データ登録・編集
 --%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.gourmetcaree.admin.pc.ajax.action.ajax.ArbeitAction"%>
<%@page import="com.gourmetcaree.admin.pc.shopList.action.shopList.ShopListBaseAction"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey" %>

<c:set var="SHUTOKEN_AREA" value="<%=MAreaConstants.AreaCd.SHUTOKEN_AREA%>" scope="request" />
<c:set var="LAT_LNG_KBN_ADDRESS" value="<%=MTypeConstants.ShopListLatLngKbn.ADDRESS%>" scope="request" />
<c:set var="AREA_BLANK" value="<%=ArbeitAction.AREA_BLANK_LABEL %>" scope="request" />
<c:set var="TRAIN_BLANK" value="<%=ArbeitAction.TRAIN_BLANK_LABEL %>" scope="request" />
<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="request" />
<c:set var="MONTH_LIST" value="1,2,3,4,5,6,7,8,9,10,11,12" scope="request"/>
<c:set var="DOMESTIC" value="<%=MTypeConstants.DomesticKbn.DOMESTIC %>" scope="request"/>
<c:set var="SALERY_STRUCTURE_KBN_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>" scope="page" />
<c:set var="SALERY_STRUCTURE_KBN_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>" scope="page" />
<jsp:useBean id="date" class="java.util.Date"/>
<fmt:formatDate var="openDateYear" value="${date}" pattern="yyyy" scope="request" />

<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD%>" />
<gt:typeList name="seatKbnList" typeCd="<%=MTypeConstants.SeatKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
<gt:typeList name="salesPerCustomerKbnList" typeCd="<%=MTypeConstants.SalesPerCustomerKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
<gt:typeList name="foreignAreaList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>"/>
<gt:typeList name="domesticKbnList" typeCd="<%=MTypeConstants.DomesticKbn.TYPE_CD %>"/>
<gt:typeList name="transportationKbnList" typeCd="<%=MTypeConstants.TransportationKbn.TYPE_CD %>" />

<%-- CSS,JSのインクルード --%>
<jsp:include page="/WEB-INF/view/shopList/resource/apQ01_common_resource.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/shopList/resource/apQ01_input_resource.jsp"></jsp:include>

<div id="main">
	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<gt:convertToCustomerName customerId="${customerId}" name="customName" />
	<h2 class="title shop">${f:h(pageTitle1)}<span>${gf:replaceStr(customName, common['gc.shopList.customerName.trim.length'], common['gc.replaceStr'])}</span></h2>
	<hr />
	<html:errors />
	<s:form action="${f:h(actionPath)}" styleId="shopListForm">
		<html:hidden property="hiddenMaterialKbn" styleId="hiddenMaterialKbn" />

		<input type="hidden" name="mediaType" id="mediaType" value="" />

		<h3 class="subtitle">基本情報</h3>

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
			<tr>
				<th width="140">店舗名&nbsp;&nbsp;</th>
				<th width="20"><span class="attention">必須</span></th>
				<td><html:text property="shopName" styleClass="txtInput validate[required]" /></td>
			</tr>
			<tr>
				<th>業態【HP表示用】</th>
				<th><span class="attention">必須</span></th>
				<td><span class="attention">※20文字以内</span><br><html:text property="industryText" maxlength="20" size="40" class="validate[required]" placeholder="例)創作フレンチ" /></td>
			</tr>
			<tr>
				<th>業態【検索用】</th>
				<th><span class="attention">必須</span></th>
				<td>
					<html:select property="industryKbn1" styleClass="validate[required]" data-prompt-position="topLeft">
						<html:option value="">業態1</html:option>
						<html:optionsCollection name="selectIndustryList" />
					</html:select>&nbsp;
					<html:select property="industryKbn2" data-prompt-position="topLeft">
						<html:option value="">業態2</html:option>
						<html:optionsCollection name="selectIndustryList" />
					</html:select>
				</td>
			</tr>
			<tr>
				<th>キャッチコピー</th>
				<th><span class="attention">必須</span></th>
				<td><span class="attention">※30文字以内</span>　<span id="catchCopySpan"></span><br><html:text property="catchCopy" styleId="catchCopyTextId" class="txtInput validate[required]" onkeyup="countTextChar(this);" onchange="countTextChar(this);" onblur="countTextChar(this);"/></td>
			</tr>
			<tr>
				<th>テキスト</th>
				<th></th>
				<td><span class="attention">※300字以内</span>　<span id="shopInformationSpan"></span><br /> <html:textarea property="shopInformation" styleId="shopInformationTextId" onkeyup="countTextChar(this);" onchange="countTextChar(this);" onblur="countTextChar(this);"></html:textarea></td>
			</tr>
			<tr>
				<th>仕事の特徴</th>
				<th></th>
				<td>
					<ul class="checklist_2col clear">
					<gt:typeList name="workCharacteristicKbnList" typeCd="<%=MTypeConstants.WorkCharacteristicKbn.TYPE_CD %>"/>
					<c:forEach items="${workCharacteristicKbnList}" var = "t">
						<li>
							<label for="workCharacteristicKbnArray_${f:h(t.value)}">
							<html:multibox property="workCharacteristicKbnArray" value="${f:h(t.value)}" styleId="workCharacteristicKbnArray_${f:h(t.value)}" />
							${f:h(t.label)}</label>
						</li>
					</c:forEach>
					</ul>
				</td>
			</tr>
			<tr>
				<th>職場</th>
				<th></th>
				<td>
					<ul class="checklist_2col clear">
					<gt:typeList name="shopCharacteristicKbnList" typeCd="<%=MTypeConstants.ShopCharacteristicKbn.TYPE_CD %>"/>
					<c:forEach items="${shopCharacteristicKbnList}" var = "t">
						<li>
							<label for="shopCharacteristicKbnArray_${f:h(t.value)}">
							<html:multibox property="shopCharacteristicKbnArray" value="${f:h(t.value)}" styleId="shopCharacteristicKbnArray_${f:h(t.value)}" />
							${f:h(t.label)}</label>
						</li>
					</c:forEach>
					</ul>
				</td>
			</tr>
			<tr>
				<th>住所</th>
				<th><span class="attention">必須</span></th>
				<td>
					<table class="nbr" width="600">
						<tbody>
							<tr>
								<th width="120">国内外：</th>
								<td>
								<html:select property="domesticKbn" styleId="domesticKbn" styleClass="validate[required]" onchange="changeDomesticKbn()">
									<html:optionsCollection name="domesticKbnList" />
								</html:select>
								</td>
							</tr>
							<tr class="japanArea">
								<th>都道府県：</th>
								<td>
									<gt:prefecturesList name="prefList" noDisplayValue="<%= Arrays.asList(MPrefectures.KAIGAI) %>" blankLineLabel="${common['gc.pullDown']}" />
									<html:select property="prefecturesCd" styleId="prefecturesCd" styleClass="validate[required]" data-prompt-position="topLeft">
										<html:optionsCollection name="prefList" />
									</html:select>
								</td>
							</tr>
							<tr class="japanArea">
								<th>市区町村：</th>
								<td>
								<html:select property="cityCd" styleId="cityCd" styleClass="validate[required] gmap" data-prompt-position="topLeft">
									<c:choose>
										<c:when test="${not empty prefecturesCd}">
											<gt:cityList name="cityList" prefecturesCd="${prefecturesCd}" blankLineLabel="${common['gc.pullDown']}" />
											<html:optionsCollection name="cityList" styleClass="gmap" />
										</c:when>
										<c:otherwise>
											<html:option value="">${common['gc.pullDown']}</html:option>
										</c:otherwise>
									</c:choose>
								</html:select>
								</td>
							</tr>
							<tr class="japanArea">
								<th>その他住所：</th>
								<td>
									<html:text property="address" styleId="address" styleClass="gmap" style="width:100%;" placeholder="例)中野5丁目61−10" />
								</td>
							</tr>
							<tr class="foreignArea">
	 							<th>地域：</th>
								<td>
									<html:select property="shutokenForeignAreaKbn" styleId="shutokenForeignAreaSelect" styleClass="validate[required]">
										<html:option value="">${common['gc.pullDown']}</html:option>
										<html:optionsCollection name="foreignAreaList" />
									</html:select>
								</td>
							</tr>
							<tr class="foreignArea">
								<th>海外住所：</th>
								<td>
									<html:text property="foreignAddress" styleId="fAddress" styleClass="validate[required] gmap" style="width:100%;" onchange="getAddressMap()"/>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>交通</th>
				<th></th>
				<td><html:textarea property="transit"></html:textarea></td>
			</tr>
			<tr class="station">
				<th>最寄駅【検索用】</th>
				<th></th>
				<td>
					<span class="example">
						※最寄り駅は1つ以上の設定をお願いします。
						<br />最初の1つ目の最寄り駅が検索結果に表示されます。
						<br />最寄り駅が複数ある場合は、利用度の高い駅より選択して下さい。
						<br />追加をした駅が登録されます。
					</span>
					<table class="station_table">
						<tr>
							<td>グループ選択</td>
							<td>
								<gt:terminalList name="terminalList" blankLineLabel="${common['gc.pullDown']}" />
								<select id="terminal">
									<c:forEach items="${terminalList}" var="t">
										<option value="${t.value}">${t.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input type="button" value="追加" id="addGroupBtn" />
							</td>
						</tr>
						<tr>
							<td>路線・最寄駅</td>
							<td>
								<select name="companyCd" id="companyCd">
									<option value="">${common['gc.pullDown']}</option>
								</select>
								<select name="lineCd" id="lineCd">
									<option value="">${common['gc.pullDown']}</option>
								</select>
								<select name="stationCd" id="stationCd">
									<option value="">${common['gc.pullDown']}</option>
								</select>
							</td>
							<td><input type="button" value="追加" id="addRouteBtn" disabled="disabled" /></td>
						</tr>
					</table>
					<input type="button" value="検索登録" id="searchRegistration" disabled data-fancybox="modal" data-src="#wrap_station_search">
					<div id="stationTitle" style="margin:20px 0 5px 0;">
						<h4 style="float: left">登録駅</h4> <input type="button" value="一括削除" id="allDeleteRouteBtn" style="float:right" />
					</div>
					<div id="sortable">
						<c:if test="${not empty stationDtoList}">
						<c:forEach items="${stationDtoList}" var="stationDto" varStatus="status">
							<c:set var="stationCd" value="${stationDto.stationCd}" />
							<div id="wrap${stationCd}" class="wrapStation activeWrapStation">
								<html:hidden property="stationDtoList[${status.index}].displayOrder" styleClass="displayOrder" />
								<table class="station_table sort-elements" id="occu${stationCd}">
									<tr>
										<td class="dot">
											${stationDto.companyName}
											<html:hidden property="stationDtoList[${status.index}].companyCd" />
											<html:hidden property="stationDtoList[${status.index}].companyName" />
										</td>
										<td style="width:200px; height:26px;">
											${stationDto.lineName}
											<html:hidden property="stationDtoList[${status.index}].lineCd" />
											<html:hidden property="stationDtoList[${status.index}].lineName" />
										</td>
										<td>
											${stationDto.stationName}
											<html:hidden property="stationDtoList[${status.index}].stationCd" class="stationCdHidden" />
											<html:hidden property="stationDtoList[${status.index}].stationName" />
										</td>
										<td>
											<html:select property="stationDtoList[${status.index}].transportationKbn">
												<html:optionsCollection name="transportationKbnList" />
											</html:select>&nbsp;&nbsp;
											<html:number min="0" property="stationDtoList[${status.index}].timeRequiredMinute"
												data-prompt-position="topLeft" style="width:30px" />
											&nbsp;分
										</td>
										<td><input type="button" class="occuDelete" value="削除" data-station-cd="${stationCd}" /></td>
									</tr>
								</table>
							</div>
						</c:forEach>
						</c:if>
					</div>
				</td>
			</tr>
			<tr>
				<th>TEL</th>
				<th></th>
				<%-- <td><html:text property="phoneNo1" size="5" style="ime-mode:disabled;" />&nbsp;-&nbsp; <html:text property="phoneNo2" size="5" style="ime-mode:disabled;" />&nbsp;-&nbsp; <html:text property="phoneNo3" size="5" style="ime-mode:disabled;" /></td> --%>
				<td><html:text property="csvPhoneNo" style="ime-mode:disabled;"/></td>
			</tr>
			<tr>
				<th>席数&nbsp;</th>
				<th></th>
				<td>
					<html:select property="seatKbn" id="seatKbn">
						<html:optionsCollection name="seatKbnList" />
					</html:select>
				</td>
			</tr>
			<tr>
				<th>スタッフ</th>
				<th></th>
				<td><html:textarea property="staff" styleClass="txtInput"></html:textarea></td>
			</tr>
			<tr>
				<th>客単価&nbsp;</th>
				<th></th>
				<td>
					<html:select property="salesPerCustomerKbn" id="salesPerCustomerKbn">
						<html:optionsCollection name="salesPerCustomerKbnList" />
					</html:select>
				</td>
			</tr>
			<tr>
				<th>定休日</th>
				<th></th>
				<td><html:textarea property="holiday" styleClass="txtInput"></html:textarea></td>
			</tr>
			<tr>
				<th>営業時間</th>
				<th></th>
				<td><html:textarea property="businessHours" styleClass="txtInput"></html:textarea></td>
			</tr>
			<tr>
				<th>オープン日</th>
				<th></th>
				<td>
					<html:select property="openDateYear" styleId="openDateYear">
						<html:option value="">--</html:option>
						<html:option value="${openDateYear}">${openDateYear}年</html:option>
						<html:option value="${openDateYear + 1}">${openDateYear + 1}年</html:option>
						<html:option value="${openDateYear + 2}">${openDateYear + 2}年</html:option>
					</html:select>&nbsp;&nbsp;

					<html:select property="openDateMonth" styleId="openDateMonth">
						<html:option value="">--</html:option>
						<c:forEach var="month" items="${MONTH_LIST}">
							<html:option value="${month}">${month}月</html:option>
						</c:forEach>
					</html:select>&nbsp;&nbsp;
					<html:text  property="openDateNote" placeholder="例)上旬予定" />&nbsp;&nbsp;
					公開期限
					<html:date property="openDateLimitDisplayDate" id="openDateLimitDisplayDate" readonly="true"></html:date>
				</td>
			</tr>
			<tr>
				<th>URL&nbsp;</th>
				<th></th>
				<td><html:text property="url1" styleClass="txtInput" /></td>
			</tr>
			<tr>
				<c:set var="TARGET_ANCHOR_IMAGE" value="<%=ShopListBaseAction.TARGET_ANCHOR_IMAGE%>" scope="page" />
				<th id="${f:h(TARGET_ANCHOR_IMAGE)}">画像&nbsp;</th>
				<th></th>
				<c:set var="MATERIAL_KBN_MAIN_1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1%>" scope="request" />
				<td class="photo">

					<%-- アップロード前 --%>
 					<span id="noUpload_${MATERIAL_KBN_MAIN_1}" class="noUpload_img">
						<span id="media_select_${MATERIAL_KBN_MAIN_1}"
								class="btn btn-primary select_media_btn"
								data-media-kbn="${MATERIAL_KBN_MAIN_1}" href="#media_modal_${MATERIAL_KBN_MAIN_1}" >メディアから選択</span>
						<br /><br />
						<input type="file" name="upImg_${MATERIAL_KBN_MAIN_1}" id="upImg_${MATERIAL_KBN_MAIN_1}" value="参照" />&nbsp;
						<input type="button" name="upMaterial" onclick="addMaterial(${MATERIAL_KBN_MAIN_1})" value="アップロード" />
					</span>

					<%-- メディアから選択した後 --%>
					<span id="select_uploaded_${MATERIAL_KBN_MAIN_1}" class="select_uploaded_img">
						<input type="hidden" name="mainImgSelect" id="mediaKbn${MATERIAL_KBN_MAIN_1}" />
						<span id="select_uploaded_img_${MATERIAL_KBN_MAIN_1}" data-alt-val="画像" >
							<c:if test="${not empty mainImgSelect}">
								<c:forEach var="image" items="${viewDto.imageList}">
									<c:if test="${image.id eq mainImgSelect}">
										<a href="${f:url(image.filePath)}" title="メイン1" data-lightbox="photo">
											<img class="thumbnail_image" src="${f:url(image.filePath)}" /><br />
										</a>
									</c:if>
								</c:forEach>
							</c:if>
						</span>
						<span id="delete_image_${MATERIAL_KBN_MAIN_1}"
								class="btn basic_button"
								onclick="delSelectMaterial(${MATERIAL_KBN_MAIN_1})">削除</span>
					</span>

					<%-- 参照してアップロード後 --%>
					<span id="uploaded_${MATERIAL_KBN_MAIN_1}" class="uploaded_img">
						<span id="uploaded_img_${MATERIAL_KBN_MAIN_1}" data-alt-val="画像" >
							<c:if test="${gf:isMapExsists(materialMap, MATERIAL_KBN_MAIN_1)}">
							<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" title="メイン1" data-lightbox="photo">
								<img class="thumbnail_image" alt="メイン1" src="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" /><br />
							</a>
							</c:if>
						</span>
						<span id="delete_image_${MATERIAL_KBN_MAIN_1}"
								class="btn basic_button"
								onclick="delMaterial(${MATERIAL_KBN_MAIN_1})">削除</span>
					</span>

					<%-- メディアモーダル ここから --%>
					<div id="media_modal_${MATERIAL_KBN_MAIN_1}" class="media_modal">
						<h2 class="cmnSttl">画像選択</h2>
						<ul class="flex_container tile">
							<c:forEach var="image" items="${viewDto.imageList}">
							<li data-media-id="${f:h(image.id)}">
								<img class="media_image" src="${f:url(image.filePath)}" />
								<span class="thumbnail_name">${f:h(image.fileName)}</span>
							</li>
							</c:forEach>
						</ul>
						<div id="media_close">
							<a class="modal_close btn basic_button">閉じる</a>
						</div>
					</div>
					<%-- メディアモーダル ここまで --%>

				</td>
			</tr>
			<tr>
				<th>インディードタグ</th>
				<th></th>
				<td>
				<ul id="freeword"></ul>
				<p style="margin:10px 0;font-size:100%;">タグから選択:</p>
				<div class="tg_list">
				<c:forEach items="${tagListDto}" var="dto" varStatus="status">
					<a href="javascript:void(0)" class="tg">${dto.tagName}</a>
				</c:forEach>
				</div>
				<html:hidden property="tagList" styleId="tagList"/>
				</td>
			</tr>
			<tr>
				<th class="bdrs_bottom">受動喫煙対策<br><span class="attention">※スタンバイ・求人ボックス用</span></th>
				<th></th>
				<td class="release bdrs_bottom"><html:text property="preventSmoke" styleClass="txtInput" />　　記入例) 屋内喫煙可　屋内禁煙</td>
			</tr>
		</table>

		<h3 class="subtitle">表示条件変更</h3>
		<h4 class="occupation">職種を選択してください</h4>
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
			<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
			<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
			<tr>
				<td>
					<c:forEach items="${employPtnList}" var="e" varStatus="status">
						<c:if test="${status.count == 3}">
							<div id="otherBox" style="display:none;">
						</c:if>
						<c:if test="${status.count %2 == 1}">
							<table class="nbr occu_table" width="100%">
								<tr>
						</c:if>
						<td width="50%">
							<h4>${f:h(e.label)}</h4>
							<ul class="checklist_2col clear">
								<c:forEach items="${jobList}" var="j">
									<c:set var="employJobValue" value="${f:h(e.value)}-${f:h(j.value)}" />
									<li>
										<label>
											<c:set var="contains" value="false" />
											<c:forEach var="employJobKbn" items="${employJobKbnList}">
												<c:if test="${employJobKbn eq employJobValue}">
													<c:set var="contains" value="true" />
												</c:if>
											</c:forEach>
											<input type="checkbox" name="employJobKbnList"
												value="${employJobValue}" id="job${employJobValue}"
												data-employ-value="${e.value}" data-employ-name="${e.label}"
												data-job-value="${j.value}" data-job-name="${j.label}" data-sort="${(e.value * 100) + j.sort}"
												<c:if test="${contains}">checked="checked"</c:if>
												 />${f:h(j.label)}
										</label>
									</li>
								</c:forEach>
							</ul>
						</td>
						<c:if test="${status.count %2 != 0 && status.last}">
							<td></td>
						</c:if>
						<c:if test="${status.count %2 == 0 || status.last}">
								</tr>
							</table>
						</c:if>
					</c:forEach>
					</div>
					<p id="occuOther"><a href="javascript:void(0);">その他の雇用形態<span></span></a></p>
				</td>
			</tr>
		</table>
		<br>
		<gt:typeList name="saleryStructureList" typeCd="<%=MTypeConstants.NewSaleryStructureKbn.TYPE_CD %>" blankLineLabel="--" />

		<h4 class="occupation">各職種の募集要項を設定してください</h4>
		<div id="sortable_occ">
			<c:if test="${not empty displayConditionDtoList}">
				<c:forEach items="${displayConditionDtoList}" var="jobDto" varStatus="status">
					<c:set var="wrapId" value="${jobDto.employPtnKbn}-${jobDto.jobKbn}" />
					<c:set var="employName" value="${f:label(jobDto.employPtnKbn, employPtnList, 'value', 'label')}" />
					<c:set var="jobName" value="${f:label(jobDto.jobKbn, jobList, 'value', 'label')}" />
					<div id="wrap${wrapId}" class="wrapJob activeWrapJob">
						<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table sort-elements" id="occu${wrapId}">
							<tr>
							<td width="100">
								${f:h(employName)}
								<html:hidden property="displayConditionDtoList[${status.index}].employPtnKbn" />
							</td>
							<td>
								${f:h(jobName)}
								<html:hidden property="displayConditionDtoList[${status.index}].jobKbn" />
							</td>
							<td width="260" class="posi_center">
								<input type="button" name="${jobDto.editFlg == 1 ? 'occuEdit' : 'occuNew'}" value="表示条件を設定"
									 class="occuAddBtn" id="occuBtn${wrapId}"
									 data-fancybox="modal" data-src="#modal${wrapId}"
									 data-wrap-id="${wrapId}">
								<html:hidden property="displayConditionDtoList[${status.index}].editFlg" styleId="editFlg${wrapId}" />
							</td>
							<td width="60">
								<input type="button" name="occuDelete" class="occuDelete" value="削除" data-employ-value="${jobDto.employPtnKbn}" data-job-value="${jobDto.jobKbn}">
							</td>
							</tr>
						</table>

						<div id="modal${wrapId}" style="display: none;">
							<h4 class="occupation clear" data-wrap-id="${wrapId}">【${f:h(employName)}】${f:h(jobName)}</h4>
							<div class="wrap_label">
								<select class="jobCopy" id="jobCopy${wrapId}"></select>
								<input type="button" value="コピー" class="jobCopyBtn" data-wrap-id="${wrapId}" />
							</div>
							<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
								<tr>
									<th>給与</th>
									<th></th>
									<td class="release">
										<html:select property="displayConditionDtoList[${status.index}].saleryStructureKbn" data-name="saleryStructureKbn" data-man-text="salary_man_text${status.index}">
											<html:optionsCollection name="saleryStructureList"/>
										</html:select>
										&nbsp;&nbsp;
										<html:text property="displayConditionDtoList[${status.index}].lowerSalaryPrice" style="width: 80px" min="0"
											id="lowerSalaryPrice${wrapId}"
											data-name="lowerSalaryPrice" />&nbsp;円
										　～　
										<html:text property="displayConditionDtoList[${status.index}].upperSalaryPrice" style="width: 80px" min="0"
											id="upperSalaryPrice${wrapId}"
											data-name="upperSalaryPrice" />&nbsp;円>
											<br></br>
										<html:textarea property="displayConditionDtoList[${status.index}].salaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。&#10;ただし、「時給」・「日給」が選択されている場合には、入力は必要なく、記載しても内容が表示されません。" data-name="salaryDetail"></html:textarea><br></br>
										<html:textarea property="displayConditionDtoList[${status.index}].salary" cols="60" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="salary"></html:textarea>
									</td>
								</tr>
								<tr>
									<th>給与2<br>(想定初年度年収)</th>
									<th></th>
									<td class="release">
										年収
										&nbsp;&nbsp;
										<html:text property="displayConditionDtoList[${status.index}].annualLowerSalaryPrice" style="width: 80px" min="0"
											id="annualLowerSalaryPrice${wrapId}"
											data-name="annualLowerSalaryPrice" />&nbsp;円
										　～　
										<html:text property="displayConditionDtoList[${status.index}].annualUpperSalaryPrice" style="width: 80px" min="0"
											id="annualUpperSalaryPrice${wrapId}"
											data-name="annualUpperSalaryPrice" />&nbsp;円>
											<br></br>
										<html:textarea property="displayConditionDtoList[${status.index}].annualSalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="annualSalaryDetail"></html:textarea><br></br>
										<html:textarea property="displayConditionDtoList[${status.index}].annualSalary" cols="60" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="annualSalary"></html:textarea>
									</td>
								</tr>
								<tr>
									<th>給与3<br>(想定初年度月収)</th>
									<th></th>
									<td class="release">
										月収
										&nbsp;&nbsp;
										<html:text property="displayConditionDtoList[${status.index}].monthlyLowerSalaryPrice" style="width: 80px" min="0"
											id="monthlyLowerSalaryPrice${wrapId}"
											data-name="monthlyLowerSalaryPrice" />&nbsp;円
										　～　
										<html:text property="displayConditionDtoList[${status.index}].monthlyUpperSalaryPrice" style="width: 80px" min="0"
											id="monthlyUpperSalaryPrice${wrapId}"
											data-name="monthlyUpperSalaryPrice" />&nbsp;円>
											<br></br>
										<html:textarea property="displayConditionDtoList[${status.index}].monthlySalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="monthlySalaryDetail"></html:textarea><br></br>
										<html:textarea property="displayConditionDtoList[${status.index}].monthlySalary" cols="60" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="monthlySalary"></html:textarea>
									</td>
								</tr>
							</table>
							<div class="wrap_btn">
								<input type="button" name="conf" class="fcclose" value="閉じる" onclick="parent.jQuery.fancybox.close();" data-employ-value="${jobDto.employPtnKbn}" data-job-value="${jobDto.jobKbn}" >
							</div>
						</div>
					</div>
				</c:forEach>
			</c:if>
		</div>

		<br>

		<div class="wrap_btn">
			<c:choose>
				<c:when test="${pageKbn eq PAGE_INPUT}">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="conf_btn" />
					<html:button property="back" styleId="back_btn" onclick="location.href='${f:url('/shopList/input/backToIndex')}'" value="戻る" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_EDIT}">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="conf_btn" />
					<html:submit property="back" styleId="back_btn" onclick="$('#shopListForm').validationEngine('detach');" value="戻る" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_OTHER}">
					<html:submit property="confDetail" value="確認" />
					<html:submit property="backToList" styleId="back_btn" onclick="$('#shopListForm').validationEngine('detach');" value="戻る" />
				</c:when>
			</c:choose>
		</div>
	</s:form>
</div>
<%-- 駅追加した際のテンプレート --%>
<div class="cloneStation" style="display:none">
	<div id="wrap@stationCd" class="wrapStation">
		<input type="hidden" name="stationDtoList[@index].displayOrder" class="displayOrder" />

		<table class="station_table sort-elements" id="occu@stationCd">
			<tr>
				<td class="dot">
					@companyName
					<input type="hidden" name="stationDtoList[@index].companyCd" value="@companyCd" />
					<input type="hidden" name="stationDtoList[@index].companyName" value="@companyName" />
				</td>
				<td style="width:200px; height:26px;">
					@lineName
					<input type="hidden" name="stationDtoList[@index].lineCd" value="@lineCd" />
					<input type="hidden" name="stationDtoList[@index].lineName" value="@lineName" />
				</td>
				<td>
					@stationName
					<input type="hidden" name="stationDtoList[@index].stationCd" class="stationCdHidden" value="@stationCd" />
					<input type="hidden" name="stationDtoList[@index].stationName" value="@stationName" />
				</td>
				<td>
					<select name="stationDtoList[@index].transportationKbn" id="">
						<c:forEach items="${transportationKbnList}" var="t">
							<option value="${t.value}">${f:h(t.label)}</option>
						</c:forEach>
					</select>&nbsp;&nbsp;
					<input type="number" min="0" name="stationDtoList[@index].timeRequiredMinute"
					 class="validate[required]" data-prompt-position="topLeft" value="@timeRequiredMinute" style="width:30px">&nbsp;分
				</td>
					<td><input type="button" class="occuDelete" value="削除" data-station-cd="@stationCd" /></td>
			</tr>
		</table>
	</div>
</div>

<!-- 駅の検索登録のモーダル -->
<div id="wrap_station_search" style="display: none; width:1000px; height:600px;" >
<h2 class="cmnSttl">駅の検索登録</h2>
<br>
	<div>
		<input type="text" id="stationSearchBox">　　　<input type="button" value="検索" id="stationSearch">
	</div>
	<br><br>

	<div class="error" style="display: none">
		<ul><li>該当するデータが見つかりませんでした。</li></ul>
	</div>

	<table class="table table-bordered cmn_table list_table shop_table" style="display: none" role="grid">
	    <thead>
			<tr>
				<th width="20"><input type="checkbox" id="allCheck"></th>
				<th>鉄道会社</th>
				<th>路線</th>
				<th>駅</th>
			</tr>
	    </thead>
	    <tbody id="searchStationTableBody" >
	    </tbody>
	</table>

	<hr />

	<div class="wrap_btn">
		<input type="button" name="conf" value="反 映"  id="reflection" onclick="parent.jQuery.fancybox.close();" style="display: none">
		<input type="button" name="back" value="戻 る"  id="back" onclick="parent.jQuery.fancybox.close();">
	</div>
</div>

<%-- 職種追加した際のテンプレート --%>
<div class="cloneJob" style="display:none">
	<div id="wrap@employValue-@jobValue" class="wrapJob" data-sort="@sort">
		<input type="hidden" name="displayConditionDtoList[@index].displayOrder" class="displayOrder" />
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table sort-elements" id="occu@employValue-@jobValue">
			<tr>
			<td width="100">
				@employName
				<input type="hidden" name="displayConditionDtoList[@index].employPtnKbn" value="@employValue" />
			</td>
			<td>
				@jobName
				<input type="hidden" name="displayConditionDtoList[@index].jobKbn" value="@jobValue" />
			</td>
			<td width="260" class="posi_center">
				<input type="button" name="occuNew" value="表示条件を設定"
					class="occuAddBtn" id="occuBtn@employValue-@jobValue"
					data-fancybox="modal" data-src="#modal@employValue-@jobValue"
					data-wrap-id="@employValue-@jobValue">
				<input type="hidden" name="displayConditionDtoList[@index].editFlg" id="editFlg@employValue-@jobValue" value="0" />
			</td>
			<td width="60">
				<input type="button" name="occuDelete" class="occuDelete" value="削除" data-employ-value="@employValue" data-job-value="@jobValue">
			</td>
			</tr>
		</table>

		<div id="modal@employValue-@jobValue" style="display: none;">
			<h4 class="occupation clear" data-wrap-id="@employValue-@jobValue">【@employName】@jobName</h4>
				<div class="wrap_label">
					<select class="jobCopy" id="jobCopy@employValue-@jobValue"></select>
					<input type="button" value="コピー" class="jobCopyBtn" data-wrap-id="@employValue-@jobValue" />
				</div>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th>給与</th>
					<th></th>
					<td class="release">
						<select name="displayConditionDtoList[@index].saleryStructureKbn" data-name="saleryStructureKbn" data-man-text="salary_man_text@index">
							<c:forEach items="${saleryStructureList}" var="t">
								<option value="${t.value}">${f:h(t.label)}</option>
							</c:forEach>
						</select>&nbsp;&nbsp;
						<input type="text" name="displayConditionDtoList[@index].lowerSalaryPrice" style="width: 80px" min="0" value=""
							id="lowerSalaryPrice@employValue-@jobValue"
							data-name="lowerSalaryPrice" />&nbsp;円
						　～　
						<input type="text" name="displayConditionDtoList[@index].upperSalaryPrice" style="width: 80px" min="0" value=""
							data-name="upperSalaryPrice" />&nbsp;円
						<br><br>
						<textarea name="displayConditionDtoList[@index].salaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。&#10;ただし、「時給」・「日給」が選択されている場合には、入力は必要なく、記載しても内容が表示されません。" data-name="salaryDetail"></textarea><br><br>
						<textarea name="displayConditionDtoList[@index].salary" cols="60" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="salary"></textarea>
					</td>
				</tr>
				<tr>
					<th>給与2<br>(想定初年度年収)</th>
					<th></th>
					<td class="release">
						年収
						&nbsp;&nbsp;
						<input type="text" name="displayConditionDtoList[@index].annualLowerSalaryPrice" style="width: 80px" min="0" value=""
							id="annualLowerSalaryPrice@employValue-@jobValue"
							data-name="annualLowerSalaryPrice" />&nbsp;円
						　～　
						<input type="text" name="displayConditionDtoList[@index].annualUpperSalaryPrice" style="width: 80px" min="0" value=""
							data-name="annualUpperSalaryPrice" />&nbsp;円
						<br><br>
						<textarea name="displayConditionDtoList[@index].annualSalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="annualSalaryDetail"></textarea><br><br>
						<textarea name="displayConditionDtoList[@index].annualSalary" cols="60" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="annualSalary"></textarea>
					</td>
				</tr>
				<tr>
					<th>給与3<br>(想定初年度月収)</th>
					<th></th>
					<td class="release">
						月収
						&nbsp;&nbsp;
						<input type="text" name="displayConditionDtoList[@index].monthlyLowerSalaryPrice" style="width: 80px" min="0" value=""
							id="monthlyLowerSalaryPrice@employValue-@jobValue"
							data-name="monthlyLowerSalaryPrice" />&nbsp;円
						　～　
						<input type="text" name="displayConditionDtoList[@index].monthlyUpperSalaryPrice" style="width: 80px" min="0" value=""
							data-name="monthlyUpperSalaryPrice" />&nbsp;円
						<br><br>
						<textarea name="displayConditionDtoList[@index].monthlySalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="monthlySalaryDetail"></textarea><br><br>
						<textarea name="displayConditionDtoList[@index].monthlySalary" cols="60" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="monthlySalary"></textarea>
					</td>
				</tr>
			</table>
			<div class="wrap_btn">
				<input type="button" name="conf" class="fcclose" value="閉じる" onclick="parent.jQuery.fancybox.close();" data-employ-value="@employValue" data-job-value="@jobValue" >
			</div>
		</div>
	</div>
</div>
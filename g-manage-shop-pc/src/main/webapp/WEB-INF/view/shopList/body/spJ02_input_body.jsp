<%--
店舗データ登録・編集
 --%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.gourmetcaree.shop.pc.ajax.action.ajax.ArbeitAction"%>
<%@page import="com.gourmetcaree.shop.pc.shopList.action.shopList.ShopListBaseAction"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey" %>

<c:set var="LAT_LNG_KBN_ADDRESS" value="<%=MTypeConstants.ShopListLatLngKbn.ADDRESS%>" scope="page" />

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
<jsp:include page="/WEB-INF/view/shopList/resource/spJ02_common_resource.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/shopList/resource/spJ02_input_resource.jsp"></jsp:include>

<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
			<div id="main">

				<div id="wrap_web-shoplist">
					<div class="page_back">
						<c:choose>
							<c:when test="${pageKbn eq PAGE_INPUT}">
								<a onclick="location.href='${f:url('/shopList/')}'" id="btn_back">戻る</a>
							</c:when>
							<c:when test="${pageKbn eq PAGE_EDIT}">
								<a onclick="location.href='${f:url('/shopList/detail/index/')}${id}'" id="btn_back">戻る</a>
							</c:when>
							<c:when test="${pageKbn eq PAGE_OTHER}">
								<a onclick="location.href='${f:url('/shopList/inputCsv/')}'" id="btn_back">戻る</a>
							</c:when>
							<c:otherwise>
								<a onclick="location.href='${f:url('/shopList/')}'" id="btn_back">戻る</a>
							</c:otherwise>
						</c:choose>
					</div>
					<h2>店舗一覧｜登録</h2>
					<p class="explanation">
						「<span style="color: #CC0000;font-weight: bold;">＊</span>」マークは<span
							style="color: red;">必須項目</span>となります。<br>
						各項目を入力の上、「確認」ボタンを押して確認画面へ進んで下さい。
					</p>
				    <html:errors/>
					<div class="menu_tab">
						<div class="menu_list">
							<ul>
								<li>
									<a href="${f:url('/webdata/list/')}">求人原稿</a>
								</li>
								<li class="tab_active">
									<a href="${f:url('/shopList/')}">店舗一覧</a>
								</li>
							</ul>
						</div>
					</div>

					<div id="wrap_masc_content">
						<div class="tab_area">
							<s:form action="${f:h(actionPath)}" enctype="multipart/form-data"  styleId="shopListForm" method="post">
							<div class="tab_contents tab_active" id="detail_Information">
								<div class="detail_area">

										<html:hidden property="hiddenMaterialKbn"  styleId="hiddenMaterialKbn" />
        								<html:hidden property="jobOfferFlg" />
        								<input type="hidden" name="mediaType" id="mediaType" value="" />
										<div class="det_shopprofile">
											<div class="l_title">
												<h3>基本情報</h3>
											</div>
											<c:if test="${pageKbn ne PAGE_INPUT}">
												<html:hidden property="id"/>
											</c:if>
											<div class="r_details">
												<table cellpadding="0" cellspacing="0" border="0" class="detail_table">
													<tbody>
														<tr>
															<th class="mandatory">店舗名</th>
															<td>
																<html:text property="shopName" styleClass="txtInput validate[required]" />
															</td>
														</tr>
														<tr>
															<th class="mandatory">業態【HP表示用】</th>
															<td>
																<span id="industryTextSpan"
																	style="display: inline; color: rgb(0, 0, 0); font-weight: normal;">残り&nbsp;<span
																		class="number">20</span>&nbsp;文字</span><br>
																<html:text property="industryText" styleId="industryTextTextId" maxlength="20" size="30" class="validate[required]" placeholder="例）創作フレンチ" onkeyup="countTextChar(this);" onchange="countTextChar(this);" onblur="countTextChar(this);"/>
															</td>
														</tr>
														<tr>
															<th class="mandatory">業態【検索用】</th>
															<td>
																<div class="selectbox">
																	<html:select property="industryKbn1" styleClass="validate[required]">
																		<html:option value="">業態1</html:option>
																		<html:optionsCollection name="selectIndustryList" />
																	</html:select>
																</div>&nbsp;
																<div class="selectbox">
																<html:select property="industryKbn2">
																	<html:option value="">業態2</html:option>
																	<html:optionsCollection name="selectIndustryList" />
																</html:select>
																</div>
															</td>
														</tr>
														<tr>
															<th class="mandatory">キャッチコピー</th>
															<td><span id="catchCopySpan"
																	style="display: inline; color: rgb(0, 0, 0); font-weight: normal;">残り&nbsp;<span
																		class="number">30</span>&nbsp;文字</span><br>
																<html:text property="catchCopy" class="txtInput validate[required]" maxlength="30" styleId="catchCopyTextId" onkeyup="countTextChar(this);" onchange="countTextChar(this);" onblur="countTextChar(this);"/>
															</td>
														</tr>
														<tr>
															<th>テキスト</th>
															<td><span id="shopInformationSpan"
																	style="display: inline; color: rgb(0, 0, 0); font-weight: normal;">残り&nbsp;<span
																		class="number">300</span>&nbsp;文字</span><br>
															<html:textarea property="shopInformation" styleId="shopInformationTextId" maxlength="300" onkeyup="countTextChar(this);" onchange="countTextChar(this);" onblur="countTextChar(this);"></html:textarea>
															</td>
														</tr>
														<tr>
															<th>仕事の特徴</th>
															<td>
																<ul class="checklist_2col clear">
																<gt:typeList name="workCharacteristicKbnList" typeCd="<%=MTypeConstants.WorkCharacteristicKbn.TYPE_CD %>"/>
																<c:forEach items="${workCharacteristicKbnList}" var = "t">
																	<li>
																		<html:multibox property="workCharacteristicKbnArray" value="${f:h(t.value)}" styleId="workCharacteristicKbnArray_${f:h(t.value)}" />
																		<label for="workCharacteristicKbnArray_${f:h(t.value)}">${f:h(t.label)}</label>
																	</li>
																</c:forEach>
																</ul>
															</td>
														</tr>
														<tr>
															<th>職場</th>
															<td>
																<ul class="checklist_2col clear">
																<gt:typeList name="shopCharacteristicKbnList" typeCd="<%=MTypeConstants.ShopCharacteristicKbn.TYPE_CD %>"/>
																<c:forEach items="${shopCharacteristicKbnList}" var = "t">
																	<li>
																		<html:multibox property="shopCharacteristicKbnArray" value="${f:h(t.value)}" styleId="shopCharacteristicKbnArray_${f:h(t.value)}" />
																		<label for="shopCharacteristicKbnArray_${f:h(t.value)}">${f:h(t.label)}</label>
																	</li>
																</c:forEach>
																</ul>
															</td>
														</tr>
														<tr>
															<th class="mandatory">住所</th>
															<td>
																<table class="stradd">
																	<tbody>
																		<tr>
																			<th>国内外：</th>
																			<td>
																				<div class="selectbox">
																					<html:select property="domesticKbn" styleId="domesticKbn" styleClass="validate[required]" onchange="changeDomesticKbn()">
																						<html:optionsCollection name="domesticKbnList" />
																					</html:select>
																				</div>
																			</td>
																		</tr>
																		<tr class="japanArea">
																			<th>都道府県：</th>
																			<td>
																				<div class="selectbox">
																					<gt:prefecturesList name="prefList" noDisplayValue="<%= Arrays.asList(MPrefectures.KAIGAI) %>" blankLineLabel="都道府県を選ぶ" />
																					<html:select property="prefecturesCd" styleId="prefecturesCd" styleClass="validate[required]" data-prompt-position="topLeft">
																						<html:optionsCollection name="prefList" />
																					</html:select>
																				</div>
																			</td>
																		</tr>
																		<tr class="japanArea">
																			<th>市区町村：</th>
																			<td>
																				<div class="selectbox">
																					<html:select property="cityCd" styleId="cityCd" styleClass="validate[required] gmap" data-prompt-position="topLeft">
																						<c:choose>
																							<c:when test="${not empty prefecturesCd}">
																								<gt:cityList name="cityList" prefecturesCd="${prefecturesCd}" blankLineLabel="市区町村を選ぶ" />
																								<html:optionsCollection name="cityList" styleClass="gmap" />
																							</c:when>
																							<c:otherwise>
																								<html:option value="">市区町村を選ぶ</html:option>
																							</c:otherwise>
																						</c:choose>
																					</html:select>
																				</div>
																			</td>
																		</tr>
																		<tr class="japanArea">
																			<th>住所：</th>
																			<td>
																				<html:text property="address" styleId="address" styleClass="gmap" style="width:100%;" placeholder="例）中野5丁目61−10" />
																			</td>
																		</tr>
																		<tr class="foreignArea">
																			<th>地域：</th>
																			<td>
																				<div class="selectbox">
																					<html:select property="shutokenForeignAreaKbn" styleId="shutokenForeignAreaSelect" styleClass="validate[required]">
																						<html:option value="">${common['gc.pullDown']}</html:option>
																						<html:optionsCollection name="foreignAreaList" />
																					</html:select>
																				</div>
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
																<td><html:textarea property="transit"></html:textarea></td>
														</tr>
														<tr class="station">
															<th>最寄駅【検索用】</th>
															<td>
																<table class="station_table">
																	<tbody>
																		<tr>
																			<td>路線・最寄駅</td>
																			<td>
																				<div class="selectbox">
																					<select name="companyCd" id="companyCd">
																						<option value="">${common['gc.pullDown']}</option>
																					</select>
																				</div>
																				<div class="selectbox">
																					<select name="lineCd" id="lineCd">
																						<option value="">${common['gc.pullDown']}</option>
																					</select>
																				</div>
																				<div class="selectbox">
																					<select name="stationCd" id="stationCd">
																						<option value="">${common['gc.pullDown']}</option>
																					</select>
																				</div>
																			</td>
																			<td><input type="button" value="追加" id="addRouteBtn" disabled="disabled" /></td>
																		</tr>
																	</tbody>
																</table>
																または
																<input type="button" value="駅名を検索する" onclick="openSpopup()" id="searchRegistration" disabled="disabled">
																<span class="example">
																	※最寄り駅は1つ以上の設定をお願いします。
																	<br>1つ目の最寄り駅が検索結果に表示されます。
																	<br>最寄り駅が複数ある場合は、利用度の高い駅より選択して下さい。
																</span>
																<div id="stationTitle" style="margin: 20px 0px 5px;">
																	<h4 style="float: left">登録駅</h4> <input
																		type="button" value="一括削除"
																		id="allDeleteRouteBtn" style="float:right">
																</div>
																<div id="sortable" class="ui-sortable">
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
																					<td style="height:26px;">
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
																						<div class="selectbox">
																							<html:select property="stationDtoList[${status.index}].transportationKbn">
																								<html:optionsCollection name="transportationKbnList" />
																							</html:select>
																						</div>&nbsp;&nbsp;
																						<html:number min="0" property="stationDtoList[${status.index}].timeRequiredMinute"
																							data-prompt-position="topLeft" />
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
															<td>
																<span
																	class="attention">※半角数字＋半角ハイフンでご入力ください</span><html:text property="csvPhoneNo" style="ime-mode:disabled;" placeholder="例）0123-456-789"/>
															</td>
														</tr>
														<tr>
															<th>席数</th>
															<td>
																<div class="selectbox">
																	<html:select property="seatKbn" id="seatKbn">
																		<html:optionsCollection name="seatKbnList" />
																	</html:select>
																</div>
															</td>
														</tr>
														<tr>
															<th>客単価</th>
															<td>
																<div class="selectbox">
																	<html:select property="salesPerCustomerKbn" id="salesPerCustomerKbn">
																		<html:optionsCollection name="salesPerCustomerKbnList" />
																	</html:select>
																</div>
															</td>
														</tr>
														<tr>
															<th>スタッフ</th>
															<td><html:textarea property="staff" styleClass="txtInput"></html:textarea></td>
														</tr>
														<tr>
															<th>定休日</th>
															<td><html:textarea property="holiday" styleClass="txtInput"></html:textarea></td>
														</tr>
														<tr>
															<th>営業時間</th>
															<td><html:textarea property="businessHours" styleClass="txtInput"></html:textarea></td>
														</tr>
														<tr>
															<th>オープン日</th>
															<td class="openDate">
																<div class="selectbox">
																	<html:select property="openDateYear" styleId="openDateYear">
																		<html:option value="">西暦</html:option>
																		<html:option value="${openDateYear}">${openDateYear}年</html:option>
																		<html:option value="${openDateYear + 1}">${openDateYear + 1}年</html:option>
																		<html:option value="${openDateYear + 2}">${openDateYear + 2}年</html:option>
																	</html:select>
																</div>&nbsp;&nbsp;
																<div class="selectbox">
																	<html:select property="openDateMonth" styleId="openDateMonth">
																		<html:option value="">月</html:option>
																		<c:forEach var="month" items="${MONTH_LIST}">
																			<html:option value="${month}">${month}月</html:option>
																		</c:forEach>
																	</html:select>
																</div>&nbsp;&nbsp;
																<html:text  property="openDateNote" placeholder="例）上旬予定" />
																<br><br>公開期限は<html:date property="openDateLimitDisplayDate" id="openDateLimitDisplayDate" readonly="true" style="width: 140px;"></html:date>までとなります。
															</td>
														</tr>
														<tr>
															<th>URL</th>
															<td><html:text property="url1" styleClass="txtInput" /></td>
														</tr>
														<tr>
															<c:set var="TARGET_ANCHOR_IMAGE" value="<%=ShopListBaseAction.TARGET_ANCHOR_IMAGE%>" scope="page" />
															<th id="${f:h(TARGET_ANCHOR_IMAGE)}">画像（330px ✕ 250px）</th>
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
                                                            <th>募集職種の条件変更</th>
                                                            <td>
                                                                <ul id="terms__radio">
                                                                    <li><input type="radio" name="terms__radio__judge" value="なし" id="terms__radio__judge1" <c:if test="${empty displayConditionDtoList}">checked</c:if>><label for="terms__radio__judge1">なし</label></li>
                                                                    <li><input type="radio" name="terms__radio__judge" value="あり" id="terms__radio__judge2" <c:if test="${not empty displayConditionDtoList}">checked</c:if>><label for="terms__radio__judge2">あり</label></li>
                                                                </ul>
                                                                <p class="gdbTxt">※チェック項目を「あり」にすることで、店舗ページ「募集職種」の条件を優先して設定できる項目です。<br>
                                                                例えば原稿内で「店長・店長候補」を募集し、この設定項目でも「店長・店長候補」を設定している場合、この店舗ページの設定を優先して表示します。</p>
                                                                <div id="terms__select" class="term_select_area">

                                                                    <gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
                                                                    <gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
																	<gt:typeList name="saleryStructureList" typeCd="<%=MTypeConstants.NewSaleryStructureKbn.TYPE_CD %>" blankLineLabel="--" />
																	<c:set var="SALERY_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>"></c:set>
																	<c:set var="SALERY_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>"></c:set>
                                                                    <p class="terms__infotext">Step.1 表示を変更したい職種を選択してください</p>

                                                                    <c:forEach items="${employPtnList}" var="e" varStatus="status">
																	<div id="terms__select__${e.value}" class="terms__select__blocks <c:if test="${status.count >= 3}">terms__select__hidden" style="display:none;" </c:if>">
																		<div class="terms__select__ttl">${f:h(e.label)}</div>
																		<ul class="terms__select__lists">
																			<c:forEach items="${jobList}" var="j">
																				<c:set var="employJobValue" value="${f:h(e.value)}-${f:h(j.value)}" />
																				<li class="terms__select__list">
																				<c:set var="contains" value="false" />
																				<c:forEach var="employJobKbn" items="${employJobKbnList}">
																					<c:if test="${employJobKbn eq employJobValue}">
																						<c:set var="contains" value="true" />
																					</c:if>
																				</c:forEach>
																				<input type="checkbox" name="employJobKbnList" value="${employJobValue}" id="job${employJobValue}" data-employ-value="${e.value}" data-employ-name="${e.label}" data-job-value="${j.value}" data-job-name="${j.label}" <c:if test="${contains}">checked="checked"</c:if>>
																				<label for="job${employJobValue}">${j.label}</label>
																				</li>
																			</c:forEach>
																		</ul>
																	</div>
                                                                    </c:forEach>
                                                                    <button id="view__termsmore" type="button">その他の雇用形態を表示</button>
                                                                </div>
                                                                <div id="terms__setting__area" class="term_select_area">
                                                                    <p class="terms__infotext">Step.2 各職種の募集要項を設定してください。</p>
                                                                    <ul class="term__setting__lists" id="term__setting__lists__area">
																		<c:if test="${not empty displayConditionDtoList}">
																			<c:forEach items="${displayConditionDtoList}" var="jobDto" varStatus="status">
																				<c:set var="wrapId" value="${jobDto.employPtnKbn}-${jobDto.jobKbn}" />
																				<li class="term__setting__list" data-employ-job-value-term-setting="${wrapId}">
																					<p class="term__icon">${f:label(jobDto.employPtnKbn, employPtnList, 'value', 'label')}</p>
																					<p class="term__name">${f:label(jobDto.jobKbn, jobList, 'value', 'label')}</p>
																					<ul class="term__edit__btns">
																						<li class="term__edit__btn term__edit__edit">
																							<button class="term__edit__btn term__edit__editbtn" type="button" data-employ-job-value="${wrapId}" data-employ-value="${jobDto.employPtnKbn}" data-job-value="${jobDto.jobKbn}">編集</button>
																						</li>
																						<li class="term__edit__btn term__edit__delete">
																							<button class="term__edit__btn term__edit__deletebtn" type="button" data-employ-job-value="${wrapId}" data-employ-value="${jobDto.employPtnKbn}" data-job-value="${jobDto.jobKbn}"><span class="material-icons">delete</span></button>
																						</li>
																					</ul>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].employPtnKbn" value="${jobDto.employPtnKbn}" />
																					<input type="hidden" name="displayConditionDtoList[${status.index}].jobKbn" value="${jobDto.jobKbn}" />
																					<input type="hidden" name="displayConditionDtoList[${status.index}].saleryStructureKbn" id="salaryStructureKbn${wrapId}" value="${jobDto.saleryStructureKbn}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].lowerSalaryPrice" id="lowerSalaryPrice${wrapId}" value="${jobDto.lowerSalaryPrice}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].upperSalaryPrice" id="upperSalaryPrice${wrapId}" value="${jobDto.upperSalaryPrice}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].salaryDetail" id="salaryDetail${wrapId}" value="${jobDto.salaryDetail}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].salary" id="salary${wrapId}" value="${jobDto.salary}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].annualLowerSalaryPrice" id="annualLowerSalaryPrice${wrapId}" value="${jobDto.annualLowerSalaryPrice}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].annualUpperSalaryPrice" id="annualUpperSalaryPrice${wrapId}" value="${jobDto.annualUpperSalaryPrice}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].annualSalaryDetail" id="annualSalaryDetail${wrapId}" value="${jobDto.annualSalaryDetail}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].annualSalary" id="annualSalary${wrapId}" value="${jobDto.annualSalary}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].monthlyLowerSalaryPrice" id="monthlyLowerSalaryPrice${wrapId}" value="${jobDto.monthlyLowerSalaryPrice}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].monthlyUpperSalaryPrice" id="monthlyUpperSalaryPrice${wrapId}" value="${jobDto.monthlyUpperSalaryPrice}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].monthlySalaryDetail" id="monthlySalaryDetail${wrapId}" value="${jobDto.monthlySalaryDetail}"/>
																					<input type="hidden" name="displayConditionDtoList[${status.index}].monthlySalary" id="monthlySalary${wrapId}" value="${jobDto.monthlySalary}"/>
																				</li>
																			</c:forEach>
																		</c:if>
                                                                    </ul>
                                                                </div>
                                                            </td>
                                                        </tr>
														<tr>
															<th>受動喫煙対策<br><span style="color: red;">※スタンバイ・求人ボックス用</span></th>
															<td><html:text property="preventSmoke" styleClass="txtInput" /><br>記入例) 屋内喫煙可　屋内禁煙</td>
														</tr>
													</tbody>
												</table>
											</div>
										</div>
								</div>

								<div class="wrap_btn">
								<c:choose>
									<c:when test="${pageKbn eq PAGE_INPUT}">
										<input type="button" name="back" value="戻る" onclick="location.href='${f:url('/shopList/')}'" id="btn_back">
										<input type="submit" name="conf" value="確認" id="enterButton">
									</c:when>
									<c:when test="${pageKbn eq PAGE_EDIT}">
										<html:submit property="back" value="戻る" onclick="$('#shopListForm').validationEngine('detach');" id="btn_back"/>
										<html:submit property="conf" value="確認" styleId="enterButton"/>
									</c:when>
									<c:when test="${pageKbn eq PAGE_OTHER}">
										<html:submit property="backToList" value="戻る" onclick="$('#shopListForm').validationEngine('detach');" id="btn_back"/>
										<html:submit property="confDetail" value="確認" styleId="enterButton"/>
									</c:when>
								</c:choose>
								</div>
							</div>
							</s:form>
						</div>
					</div>
				</div>

			</div>

<!-- 駅の検索登録のモーダル -->
		<!-- ▼ popup ▼ -->
		<div id="station-popup">
			<div class="wrap">
				<div class="pHeader">
                    <form id="serachStation">
                        <input id="sbox" type="text" placeholder="最寄りの駅名を入力" />
                        <input type="text" name="dummy" style="display:none;">
                        <input id="sbtn" type="button" value="検索" />
                    </form>
				</div>
				<div class="pContent">
					<ul style="display: none;"></ul>
					<!-- 空の場合 -->
					<p id="station-nosearch" class="stationError">上部の検索フォームから<br>最寄り駅名を検索</p>
				</div>
				<div class="pfooter">
					<button id="close" onclick="closeSpopup()">閉じる</button>
					<button id="submit" style="display: none;">決定</button>
				</div>
			</div>
		</div>
		<!-- ▲ popup ▲ -->

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
				<td style="height:26px;">
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
					<div class="selectbox">
						<select name="stationDtoList[@index].transportationKbn" id="">
						<c:forEach items="${transportationKbnList}" var="t">
							<option value="${t.value}">${f:h(t.label)}</option>
						</c:forEach>
						</select>
					</div>&nbsp;&nbsp;
					<input type="number" min="0" name="stationDtoList[@index].timeRequiredMinute" class="validate[required]" data-prompt-position="topLeft" value="@timeRequiredMinute">&nbsp;分
				</td>
				<td><input type="button" class="occuDelete" value="削除" data-station-cd="@stationCd" /></td>
			</tr>
		</table>
	</div>
</div>

<!-- ボタンを押すことで、1242行目の #terms__setting__modal に Class showmodal を追加 -->
<div id="term__setting__template" style="display: none;">
	<li class="term__setting__list" data-employ-job-value-term-setting="@employJobValue">
		<p class="term__icon">@EmployName</p>
		<p class="term__name">@JobName</p>
		<ul class="term__edit__btns">
			<li class="term__edit__btn term__edit__edit">
				<button class="term__edit__btn term__edit__editbtn" type="button" data-employ-job-value="@employJobValue" data-employ-value="@employValue" data-job-value="@jobValue">編集</button>
			</li>
			<li class="term__edit__btn term__edit__delete">
				<button class="term__edit__btn term__edit__deletebtn" type="button" data-employ-job-value="@employJobValue" data-employ-value="@employValue" data-job-value="@jobValue"><span class="material-icons">delete</span></button>
			</li>
		</ul>
		<input type="hidden" name="displayConditionDtoList[@index].employPtnKbn" value="@employValue" />
		<input type="hidden" name="displayConditionDtoList[@index].jobKbn" value="@jobValue" />
		<input type="hidden" name="displayConditionDtoList[@index].saleryStructureKbn" id="salaryStructureKbn@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].lowerSalaryPrice" id="lowerSalaryPrice@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].upperSalaryPrice" id="upperSalaryPrice@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].salaryDetail" id="salaryDetail@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].salary" id="salary@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].annualLowerSalaryPrice" id="annualLowerSalaryPrice@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].annualUpperSalaryPrice" id="annualUpperSalaryPrice@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].annualSalaryDetail" id="annualSalaryDetail@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].annualSalary" id="annualSalary@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].monthlyLowerSalaryPrice" id="monthlyLowerSalaryPrice@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].monthlyUpperSalaryPrice" id="monthlyUpperSalaryPrice@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].monthlySalaryDetail" id="monthlySalaryDetail@employJobValue"/>
		<input type="hidden" name="displayConditionDtoList[@index].monthlySalary" id="monthlySalary@employJobValue"/>
	</li>
</div>
<!-- 募集職種の条件制御モーダル -->
<div id="terms__setting__modal__template" style="display: none;">
	<div id="terms__setting__modal" class="terms__setting__modal" data-employ-job-value-modal="@employJobValue">
		<div class="blacklayer"></div>
		<div id="terms__setting__modal__box">
			<div class="terms__setting__modal__inner">
				<p id="terms__setting__modal__title">@JobName</p>
				<table id="terms__setting__table">
					<tr class="terms__setting__table__tr">
						<th class="terms__setting__table__th">給与</th>
						<td class="terms__setting__table__td">
							<div id="terms__modal__price__box">
								<div class="selectbox">
									<select id="terms__setting__price__select" name="displayConditionDtoList[@index].saleryStructureKbn" data-name="salaryStructureKbn" data-man-text="salary_man_text@index" class="copyOriginInput" data-salaryStructureKbn="@employJobValue" data-employ-job-value="@employJobValue">
										<c:forEach items="${saleryStructureList}" var="t">
											<option value="${t.value}">${f:h(t.label)}</option>
										</c:forEach>
									</select>
								</div>
								<div id="terms__setting__price__input">
									<input type="text" name="displayConditionDtoList[@index].lowerSalaryPrice" value="" class="terms__setting__price copyOriginInput" id="lowerSalaryPrice@employValue-@jobValue" data-name="lowerSalaryPrice" data-lowerSalaryPrice="@employJobValue" data-employ-job-value="@employJobValue">&nbsp;円
									　～　
									<input type="text" name="displayConditionDtoList[@index].upperSalaryPrice" value="" class="terms__setting__price copyOriginInput" id="upperSalaryPrice@employValue-@jobValue" data-name="upperSalaryPrice" data-upperSalaryPrice="@employJobValue" data-employ-job-value="@employJobValue">&nbsp;円
								</div>
							</div>
							<textarea id="terms__modal__text" name="displayConditionDtoList[@index].salaryDetail" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。&#10;ただし、「時給」・「日給」が選択されている場合には、入力は必要なく、記載しても内容が表示されません。" data-name="salaryDetail" class="copyOriginInput" data-salary="@employJobValue" data-employ-job-value="@employJobValue"></textarea><br><br>
							<textarea id="terms__modal__text" name="displayConditionDtoList[@index].salary" rows="10" data-name="salary" class="copyOriginInput" placeholder="【備考欄】記載内容が反映されます。" data-salary="@employJobValue" data-employ-job-value="@employJobValue"></textarea>
						</td>
					</tr>
					<tr class="terms__setting__table__tr">
						<th class="terms__setting__table__th">給与2<br>(想定初年度年収)</th>
						<td class="terms__setting__table__td">
							<div id="terms__modal__price__box">
								<div>
									年収
								</div>
								<div id="terms__setting__price__input">
									<input type="text" name="displayConditionDtoList[@index].annualLowerSalaryPrice" value="" class="terms__setting__price copyOriginInput"id="annualLowerSalaryPrice@employValue-@jobValue" data-name="annualLowerSalaryPrice" data-lowerSalaryPrice="@employJobValue" data-employ-job-value="@employJobValue">&nbsp;円
									　～　
									<input type="text" name="displayConditionDtoList[@index].annualUpperSalaryPrice" value="" class="terms__setting__price copyOriginInput" id="annualUpperSalaryPrice@employValue-@jobValue" data-name="annualUpperSalaryPrice" data-upperSalaryPrice="@employJobValue" data-employ-job-value="@employJobValue">&nbsp;円
								</div>
							</div>
							<textarea id="terms__modal__text" name="displayConditionDtoList[@index].annualSalaryDetail" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="annualSalaryDetail" class="copyOriginInput" data-salary="@employJobValue" data-employ-job-value="@employJobValue"></textarea><br><br>
							<textarea id="terms__modal__text" name="displayConditionDtoList[@index].annualSalary" rows="10" data-name="annualSalary" class="copyOriginInput" placeholder="【備考欄】記載内容が反映されます。" data-salary="@employJobValue" data-employ-job-value="@employJobValue"></textarea>
						</td>
					</tr>
					<tr class="terms__setting__table__tr">
						<th class="terms__setting__table__th">給与3<br>(想定初年度月収)</th>
						<td class="terms__setting__table__td">
							<div id="terms__modal__price__box">
								<div>
									月収
								</div>
								<div id="terms__setting__price__input">
									<input type="text" name="displayConditionDtoList[@index].monthlyLowerSalaryPrice" value="" class="terms__setting__price copyOriginInput"id="annualLowerSalaryPrice@employValue-@jobValue" data-name="monthlyLowerSalaryPrice" data-lowerSalaryPrice="@employJobValue" data-employ-job-value="@employJobValue">&nbsp;円
									　～　
									<input type="text" name="displayConditionDtoList[@index].monthlyUpperSalaryPrice" value="" class="terms__setting__price copyOriginInput" id="annualUpperSalaryPrice@employValue-@jobValue" data-name="monthlyUpperSalaryPrice" data-upperSalaryPrice="@employJobValue" data-employ-job-value="@employJobValue">&nbsp;円
								</div>
							</div>
							<textarea id="terms__modal__text" name="displayConditionDtoList[@index].monthlySalaryDetail" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="monthlySalaryDetail" class="copyOriginInput" data-salary="@employJobValue" data-employ-job-value="@employJobValue"></textarea><br><br>
							<textarea id="terms__modal__text" name="displayConditionDtoList[@index].monthlySalary" rows="10" data-name="monthlySalary" class="copyOriginInput" placeholder="【備考欄】記載内容が反映されます。" data-salary="@employJobValue" data-employ-job-value="@employJobValue"></textarea>
						</td>
					</tr>
				</table>
				<button id="terms__setting__modal__closebtn" type="button" class="terms__setting__modal__closebtn" data-employ-job-value="@employJobValue">閉じる</button>
			</div>
		</div>
	</div>
</div>
<c:if test="${not empty displayConditionDtoList}">
	<c:forEach items="${displayConditionDtoList}" var="jobDto" varStatus="status">
		<c:set var="wrapId" value="${jobDto.employPtnKbn}-${jobDto.jobKbn}" />
		<div id="terms__setting__modal" class="terms__setting__modal" data-employ-job-value-modal="${wrapId}">
			<div class="blacklayer"></div>
			<div id="terms__setting__modal__box">
				<div class="terms__setting__modal__inner">
					<p id="terms__setting__modal__title">${f:label(jobDto.jobKbn, jobList, 'value', 'label')}</p>
					<table id="terms__setting__table">
						<tr class="terms__setting__table__tr">
							<th class="terms__setting__table__th">給与</th>
							<td class="terms__setting__table__td">
								<div id="terms__modal__price__box">
									<div class="selectbox">
										<select id="terms__setting__price__select" name="displayConditionDtoList[${status.index}].saleryStructureKbn" data-name="salaryStructureKbn" data-man-text="salary_man_text${status.index}" class="copyOriginInput" data-salaryStructureKbn="${wrapId}" data-employ-job-value="${wrapId}">
											<c:set var="manDisplayFlg" value="false" />
											<c:if test="${jobDto.saleryStructureKbn eq SALERY_STRUCTURE_KBN_MONTHLY or jobDto.saleryStructureKbn eq SALERY_STRUCTURE_KBN_YEARLY}">
												<c:set var="manDisplayFlg" value="true" />
											</c:if>
											<c:forEach items="${saleryStructureList}" var="t">
												<option value="${t.value}" <c:if test="${t.value == jobDto.saleryStructureKbn}">selected</c:if>>${f:h(t.label)}</option>
											</c:forEach>
										</select>
									</div>
									<div id="terms__setting__price__input">
										<input type="text" name="displayConditionDtoList[${status.index}].lowerSalaryPrice" value="${jobDto.lowerSalaryPrice}" class="terms__setting__price copyOriginInput" id="lowerSalaryPrice${jobDto.employPtnKbn}-${jobDto.jobKbn}" data-name="lowerSalaryPrice" data-lowerSalaryPrice="${wrapId}" data-employ-job-value="${wrapId}">&nbsp;円
										　～　
										<input type="text" name="displayConditionDtoList[${status.index}].upperSalaryPrice" value="${jobDto.upperSalaryPrice}" class="terms__setting__price copyOriginInput" id="upperSalaryPrice${jobDto.employPtnKbn}-${jobDto.jobKbn}" data-name="upperSalaryPrice" data-upperSalaryPrice="${wrapId}" data-employ-job-value="${wrapId}">&nbsp;円
									</div>
								</div>
								<textarea id="terms__modal__text" name="displayConditionDtoList[${status.index}].salaryDetail" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。&#10;ただし、「時給」・「日給」が選択されている場合には、入力は必要なく、記載しても内容が表示されません。" data-name="salaryDetail" class="copyOriginInput" data-salaryDetail="${wrapId}" data-employ-job-value="${wrapId}">${jobDto.salaryDetail}</textarea><br><br>
								<textarea id="terms__modal__text" name="displayConditionDtoList[${status.index}].salary" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="salary" class="copyOriginInput" data-salary="${wrapId}" data-employ-job-value="${wrapId}">${jobDto.salary}</textarea>
							</td>
						</tr>
						<tr class="terms__setting__table__tr">
							<th class="terms__setting__table__th">給与2<br>(想定初年度年収)</th>
							<td class="terms__setting__table__td">
								<div id="terms__modal__price__box">
									<div>
										年収
									</div>
									<div id="terms__setting__price__input">
										<input type="text" name="displayConditionDtoList[${status.index}].annualLowerSalaryPrice" value="${jobDto.annualLowerSalaryPrice}" class="terms__setting__price copyOriginInput" id="annualLowerSalaryPrice${jobDto.employPtnKbn}-${jobDto.jobKbn}" data-name="annualLowerSalaryPrice" data-annualLowerSalaryPrice="${wrapId}" data-employ-job-value="${wrapId}">&nbsp;円
										　～　
										<input type="text" name="displayConditionDtoList[${status.index}].annualUpperSalaryPrice" value="${jobDto.annualUpperSalaryPrice}" class="terms__setting__price copyOriginInput" id="annualUpperSalaryPrice${jobDto.employPtnKbn}-${jobDto.jobKbn}" data-name="annualUpperSalaryPrice" data-annualUpperSalaryPrice="${wrapId}" data-employ-job-value="${wrapId}">&nbsp;円
									</div>
								</div>
								<textarea id="terms__modal__text" name="displayConditionDtoList[${status.index}].annualSalaryDetail" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="annualSalaryDetail" class="copyOriginInput" data-annualSalaryDetail="${wrapId}" data-employ-job-value="${wrapId}">${jobDto.annualSalaryDetail}</textarea><br><br>
								<textarea id="terms__modal__text" name="displayConditionDtoList[${status.index}].annualSalary" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="annualSalary" class="copyOriginInput" data-annualSalary="${wrapId}" data-employ-job-value="${wrapId}">${jobDto.annualSalary}</textarea>
							</td>
						</tr>
						<tr class="terms__setting__table__tr">
							<th class="terms__setting__table__th">給与3<br>(想定初年度月収)</th>
							<td class="terms__setting__table__td">
								<div id="terms__modal__price__box">
									<div>
										月収
									</div>
									<div id="terms__setting__price__input">
										<input type="text" name="displayConditionDtoList[${status.index}].monthlyLowerSalaryPrice" value="${jobDto.monthlyLowerSalaryPrice}" class="terms__setting__price copyOriginInput" id="monthlyLowerSalaryPrice${jobDto.employPtnKbn}-${jobDto.jobKbn}" data-name="monthlyLowerSalaryPrice" data-monthlyLowerSalaryPrice="${wrapId}" data-employ-job-value="${wrapId}">&nbsp;円
										　～　
										<input type="text" name="displayConditionDtoList[${status.index}].monthlyUpperSalaryPrice" value="${jobDto.monthlyUpperSalaryPrice}" class="terms__setting__price copyOriginInput" id="monthlyUpperSalaryPrice${jobDto.employPtnKbn}-${jobDto.jobKbn}" data-name="monthlyUpperSalaryPrice" data-monthlyUpperSalaryPrice="${wrapId}" data-employ-job-value="${wrapId}">&nbsp;円
									</div>
								</div>
								<textarea id="terms__modal__text" name="displayConditionDtoList[${status.index}].monthlySalaryDetail" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="monthlySalaryDetail" class="copyOriginInput" data-monthlySalaryDetail="${wrapId}" data-employ-job-value="${wrapId}">${jobDto.monthlySalaryDetail}</textarea><br><br>
								<textarea id="terms__modal__text" name="displayConditionDtoList[${status.index}].monthlySalary" rows="10" placeholder="【備考欄】記載内容が反映されます。" data-name="monthlySalary" class="copyOriginInput" data-monthlySalary="${wrapId}" data-employ-job-value="${wrapId}">${jobDto.monthlySalary}</textarea>
							</td>
						</tr>
					</table>
					<button id="terms__setting__modal__closebtn" type="button" class="terms__setting__modal__closebtn" data-employ-job-value="${wrapId}">閉じる</button>
				</div>
			</div>
		</div>
	</c:forEach>
</c:if>
<%--
店舗データ登録・編集 確認
 --%>
<%@page import="javassist.expr.Instanceof"%>
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
<gt:typeList name="transportationKbnList" typeCd="<%=MTypeConstants.TransportationKbn.TYPE_CD %>" />
<style>
	img.thumbnail_image {
		width:200px;
		object-fit: contain;
	}
</style>


<%-- CSS,JSのインクルード --%>
<jsp:include page="/WEB-INF/view/shopList/resource/spJ02_common_resource.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/shopList/resource/spJ02_input_resource.jsp"></jsp:include>

<div id="main">
<div id="wrap_web-shoplist">
	<div class="page_back">
		<c:choose>
			<c:when test="${pageKbn eq PAGE_INPUT}">
				<a onclick="location.href='${f:url('/shopList/input/')}'" id="btn_back">戻る</a>
			</c:when>
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<a onclick="location.href='${f:url('/shopList/list/reShowList')}'" id="btn_back">戻る</a>
			</c:when>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<a onclick="location.href='${f:url('/shopList/edit/index/')}${id}'" id="btn_back">戻る</a>
			</c:when>
			<c:when test="${pageKbn eq PAGE_OTHER}">
				<a onclick="location.href='${f:url('/shopList/inputCsv/edit/')}${id}'" id="btn_back">戻る</a>
			</c:when>
			<c:otherwise>
				<a onclick="location.href='${f:url('/shopList/')}'" id="btn_back">戻る</a>
			</c:otherwise>
		</c:choose>
	</div>
	<h2>店舗一覧｜登録</h2>
	<p class="explanation">
		${f:h(defaultMsg)}
	</p>
	<div class="menu_tab">
		<div class="menu_list"><ul>
			<li>
				<a href="${f:url('/webdata/list/')}">求人原稿</a>
			</li>
			<li class="tab_active">
				<a href="${f:url('/shopList/')}">店舗一覧</a>
			</li>
		</ul></div>
	</div>
	<div id="wrap_masc_content">
		<div class="tab_area">
			<div class="tab_contents tab_active" id="detail_Information">
				<s:form action="${f:h(actionPath)}" styleId="shopList_ConfirmForm" method="post">
				<div class="detail_area">

					<div class="det_shopprofile">
						<!-- <div class="error"><ul><li>電話番号を正しく入力してください。</li></ul></div> -->
						<div class="l_title"><h3>基本情報</h3></div>
						<div class="r_details">
							<table cellpadding="0" cellspacing="0" border="0" class="detail_table"><tbody>
								<tr>
									<th class="mandatory">店舗名</th>
									<td>${f:h(shopName)}</td>
								</tr>
								<tr>
									<th class="mandatory">業態【HP表示用】</th>
									<td>${f:h(industryText)}</td>
								</tr>
								<tr>
									<th class="mandatory">業態【検索用】</th>
									<td>業態1：${f:label(industryKbn1, selectIndustryList, 'value', 'label')}<br><br>業態2：${f:label(industryKbn2, selectIndustryList, 'value', 'label')}</td>
								</tr>
								<tr>
									<th class="mandatory">キャッチコピー</th>
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
									<th class="mandatory">住所</th>
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
														<td style="height:26px;">
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
									<td>${f:h(csvPhoneNo)}</td>
								</tr>
								<tr>
									<th>席数</th>
									<td>${f:h(f:label(seatKbn,seatKbnList,'value','label'))}</td>
								</tr>
								<tr>
									<th>客単価</th>
									<td>${f:h(f:label(salesPerCustomerKbn,salesPerCustomerKbnList,'value','label'))}</td>
								</tr>
								<tr>
									<th>スタッフ</th>
									<td>${f:br(f:h(staff))}</td>
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
									${f:h(openDateYear)}年&nbsp;${f:h(openDateMonth)}月&nbsp;&nbsp;
									<c:if test="${not empty openDateNote}">
									${f:h(openDateNote)}
									</c:if>
									<br><br>
									</c:if>
									<c:if test="${not empty openDateLimitDisplayDate}">
									公開期限は&nbsp;${ fn:replace(openDateLimitDisplayDate,"-","/") }&nbsp;までとなります。
									</c:if>
									</td>
								</tr>
								<tr>
									<th>URL</th>
									<td>${f:h(url1)}</td>
								</tr>
								<tr>
									<th>画像</th>
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
														<img class="thumbnail_image" src="${f:url(image.filePath)}" />
													</a>
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<c:if test="${gf:isMapExsists(materialMap, MATERIAL_KBN_MAIN_1)}">
												<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" title="メイン1">
													<img class="thumbnail_image" alt="メイン1" src="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, idForDirName))}" /><br /><br />
												</a>
											</c:if>
										</c:otherwise>
									</c:choose>
									</td>
								</tr>

								<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
								<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
								<gt:typeList name="saleryStructureList" typeCd="<%=MTypeConstants.SaleryStructureKbn.TYPE_CD %>" blankLineLabel="--" />
								<c:set var="SALERY_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>"></c:set>
								<c:set var="SALERY_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>"></c:set>

								<tr>
									<th>募集職種の条件変更</th>
									<td>
										<c:if test="${empty displayConditionDtoList}">
											なし
										</c:if>
										<c:if test="${not empty displayConditionDtoList}">
											<input type="radio" name="terms__radio__judge" value="あり" id="terms__radio__judge2" checked style="display: none;">
											<div id="terms__setting__area" class="term_select_area">
												<ul class="term__setting__lists" id="term__setting__lists__area">
													<c:forEach items="${displayConditionDtoList}" var="jobDto">
														<c:set var="wrapId" value="${jobDto.employPtnKbn}-${jobDto.jobKbn}" />
														<li class="term__setting__list term__setting__confirm__list" data-employ-job-value-term-setting="${wrapId}">
															<p class="term__icon">${f:label(jobDto.employPtnKbn, employPtnList, 'value', 'label')}</p>
															<p class="term__name">${f:label(jobDto.jobKbn, jobList, 'value', 'label')}</p>
															<ul class="term__edit__btns">
																<li class="term__edit__btn term__edit__edit term__edit__confirm__edit">
																	<button class="term__edit__btn term__edit__editbtn" type="button" data-employ-job-value="${wrapId}" data-employ-value="${jobDto.employPtnKbn}" data-job-value="${jobDto.jobKbn}">確認</button>
																</li>
															</ul>
														</li>
													</c:forEach>
												</ul>
											</div>
										</c:if>
									</td>
								</tr>

								<tr>
									<gt:typeList name="jobOfferFlgList" typeCd="<%=MTypeConstants.JobOfferFlg.TYPE_CD %>"/>
									<th class="mandatory">求人募集</th>
									<td>${f:label(jobOfferFlg, jobOfferFlgList, 'value', 'label')}</td>
								</tr>
								<tr>
									<th>受動喫煙対策</th>
									<td>${f:h(preventSmoke)}</td>
								</tr>
							</tbody></table>
						</div>
					</div>
				</div>

				<div class="wrap_btn shop__edit__btns">
					<c:choose>
						<c:when test="${pageKbn eq PAGE_INPUT}">
							<html:submit property="correct" value="訂正" styleId="btn_correct" />
							<html:submit property="submit" value="登録"  styleId="btn_regist"/>
						</c:when>
						<c:when test="${pageKbn eq PAGE_DETAIL}">

							<html:hidden property="id"/>
							<html:hidden property="deleteVersion" value="${f:h(version)}"/>

							<span id="doubleRowButton">
								<html:button property="edit" onclick="location.href='${f:url(gf:makePathConcat1Arg('/shopList/edit/index', id))}'" value="編集" styleId="btn_edit" />
								<html:button property="copy" onclick="copyConf('processFlg', 'inputForm');" value="コピー" styleId="btn_copy" />
								<br class="pc_none">
								<html:submit property="delete" onclick="return confirm('削除してよろしいですか？')?true:false" value="削除" styleId="btn_delete" />
								<html:submit property="backToList" value="戻る"></html:submit>
							</span>

						</c:when>
						<c:when test="${pageKbn eq PAGE_EDIT}">
							<html:submit property="correct" value="訂正" styleId="btn_correct" />
							<html:submit property="submit" value="登録" styleId="btn_regist" />
						</c:when>
						<c:when test="${pageKbn eq PAGE_OTHER}">
							<html:submit property="correct" value="訂正" styleId="btn_correct" />
							<html:submit property="submitDetail" value="登録" styleId="btn_regist" />
						</c:when>
					</c:choose>
				</div>
				</s:form>
				<c:if test="${pageKbn ne PAGE_INPUT}">
					<s:form action="${f:h(copyActionPath)}" enctype="multipart/form-data" styleId="inputForm" >
						<html:hidden property="id" value="${f:h(id)}" />
						<html:hidden property="customerId" value="${customerId}" />
						<html:hidden property="processFlg" styleId="processFlg" />
					</s:form>
				</c:if>
			</div>
		</div>
	</div>
</div>
</div>

<!-- 募集職種の条件制御モーダル -->
<c:forEach items="${displayConditionDtoList}" var="jobDto">
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
								<c:if test="${not empty jobDto.lowerSalaryPrice || not empty jobDto.upperSalaryPrice}">
									${f:label(jobDto.saleryStructureKbn, saleryStructureList, 'value', 'label')}
									<div id="terms__setting__price__input">
										<c:choose>
											<c:when test="${jobDto.saleryStructureKbn == SALERY_MONTHLY || jobDto.saleryStructureKbn == SALERY_YEARLY }">
												<c:out value="${jobDto.lowerSalaryPrice}"></c:out>&nbsp;${empty jobDto.lowerSalaryPrice ? '' : '万円'}
												　～　
												<c:out value="${jobDto.upperSalaryPrice}"></c:out>&nbsp;${empty jobDto.upperSalaryPrice ? '' : '万円'}
											</c:when>
											<c:otherwise>
												<c:out value="${jobDto.lowerSalaryPrice}"></c:out>&nbsp;${empty jobDto.lowerSalaryPrice ? '' : '円'}
												　～　
												<c:out value="${jobDto.upperSalaryPrice}"></c:out>&nbsp;${empty jobDto.upperSalaryPrice ? '' : '円'}
											</c:otherwise>
										</c:choose>
									</div>

								</c:if>
							</div>
							${f:br(f:h(jobDto.salaryDetail))}
							<c:if test="${not empty jobDto.salary}">
								<br><br>
								${f:br(f:h(jobDto.salary))}
							</c:if>
						</td>
					</tr>
					<tr class="terms__setting__table__tr">
						<th class="terms__setting__table__th">給与2<br><br>(想定初年度年収)</th>
						<td class="terms__setting__table__td">
							<div id="terms__modal__price__box">
								<c:if test="${not empty jobDto.annualLowerSalaryPrice || not empty jobDto.annualUpperSalaryPrice}">
									年収
									<div id="terms__setting__price__input">
										<c:out value="${jobDto.annualLowerSalaryPrice}"></c:out>&nbsp;${empty jobDto.annualLowerSalaryPrice ? '' : '円'}
										　～　
										<c:out value="${jobDto.annualUpperSalaryPrice}"></c:out>&nbsp;${empty jobDto.annualUpperSalaryPrice ? '' : '円'}
									</div>
								</c:if>
							</div>
							${f:br(f:h(jobDto.annualSalaryDetail))}
							<c:if test="${not empty jobDto.annualSalary}">
								<br><br>
								${f:br(f:h(jobDto.annualSalary))}
							</c:if>
						</td>
					</tr>
					<tr class="terms__setting__table__tr">
						<th class="terms__setting__table__th">給与3<br><br>(想定初年度月収)</th>
						<td class="terms__setting__table__td">
							<div id="terms__modal__price__box">
								<c:if test="${not empty jobDto.monthlyLowerSalaryPrice || not empty jobDto.monthlyUpperSalaryPrice}">
									月収
									<div id="terms__setting__price__input">
										<c:out value="${jobDto.monthlyLowerSalaryPrice}"></c:out>&nbsp;${empty jobDto.monthlyLowerSalaryPrice ? '' : '円'}
										　～　
										<c:out value="${jobDto.monthlyUpperSalaryPrice}"></c:out>&nbsp;${empty jobDto.monthlyUpperSalaryPrice ? '' : '円'}
									</div>
								</c:if>
							</div>
							${f:br(f:h(jobDto.monthlySalaryDetail))}
							<c:if test="${not empty jobDto.monthlySalary}">
								<br><br>
								${f:br(f:h(jobDto.monthlySalary))}
							</c:if>
						</td>
					</tr>
				</table>
				<button id="terms__setting__modal__closebtn" type="button" class="terms__setting__modal__closebtn" data-employ-job-value="${wrapId}">閉じる</button>
			</div>
		</div>
	</div>
</c:forEach>
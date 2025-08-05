<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn"%>
<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" scope="request"/>
<c:set var="SENDAI_AREA" value="<%= MAreaConstants.AreaCd.SENDAI_AREA %>" scope="request"/>

<jsp:include page="apC01_common_body_js.jsp"></jsp:include>
<jsp:include page="apC01_input_body_js.jsp"></jsp:include>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle1)}" class="title customer" id="customer">${f:h(pageTitle1)}</h2>
	<hr />
	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
		<tr>
			<th width="40" class="posi_center">顧客ID</th>
			<th>顧客名</th>
			<th>担当者名</th>
			<th>電話番号</th>
			<th>メールアドレス</th>
			<th class="bdrs_right">担当会社名：営業担当者名</th>
		</tr>
		<tr>
			<td class="posi_center" id="customerId">${f:h(customerDto.id)}&nbsp;</td>
			<td id="customerName">${f:h(customerDto.customerName)}&nbsp;</td>
			<td id="contactName">${f:h(customerDto.contactName)}&nbsp;</td>
			<td id="phoneNo">${f:h(customerDto.phoneNo)}&nbsp;</td>
			<td>
				<span id="mainMail">${f:h(customerDto.mainMail)}<c:if test="${not empty customerDto.subMailList}"><br></c:if></span>
				<span id="subMail">
				<c:forEach items="${customerDto.subMailList}" var="subMail" varStatus="status">
					${subMail}<c:if test="${!status.last}"><br></c:if>
				</c:forEach>
				</span>
			</td>
			<td class="bdrs_right"  id="companySalesName">${f:br(customerDto.companySalesName)}&nbsp;</td>
		</tr>
	</table>
	<hr />

	<div class="wrap_btn">
		<input type="button" name="" value="顧客検索" onclick="selectCustomer()" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
		<input type="button" name="" value="クリア" onclick="resetCustomer()" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
	</div>
	<hr />

	<h2 class="title shop" id="shop">対象店舗選択</h2>
	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
		<tr>
			<th width="400" class="posi_center">総店舗数</th>
			<th class="posi_center">選択店舗数</th>
			<th class="posi_center">インディード選択店舗数</th>
		</tr>
		<tr>
			<td class="posi_center">
				<span id="allShopCountText">0</span>店舗
			</td>
			<td class="posi_center">
				<span id="selectShopCountText">0</span>店舗
			</td>
			<td class="posi_center">
				<span id="selectShopCountTextForIndeed">0</span>店舗
			</td>
		</tr>
	</table>
	<hr />
	<div class="wrap_btn">
		<input type="button" name="" value="店舗検索" id="shopSelectBtn"
			<c:if test="${empty customerDto.id}">disabled</c:if>
			data-fancybox="modal" data-src="#wrap_shop_search"
		/>
		<input type="button" name="" value="インディード表示" id="indeedShopSelectBtn"
			<c:if test="${empty customerDto.id}">disabled</c:if>
			data-fancybox="modal" data-src="#wrap_shop_indeed_search"
		/>
	</div>
	<hr />

	<h2 title="${f:h(pageTitle2)}" class="title date">${f:h(pageTitle2)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}" styleId="webForm">
			<html:hidden property="hiddenId" />
			<html:hidden property="cursorPosition" styleId="cursorPosition" />
			<% //顧客情報を保持 %>
			<html:hidden property="customerDto.id" styleId="hiddenCustomerId" />
			<html:hidden property="customerDto.customerName" styleId="hiddenCustomerName" />
			<html:hidden property="customerDto.contactName" styleId="hiddenContactName" />
			<html:hidden property="customerDto.areaName" styleId="hiddenCustomerAreaName" />
			<html:hidden property="customerDto.areaCd" styleId="hiddenCustomerAreaCd" />
			<html:hidden property="customerDto.phoneNo" styleId="hiddenPhoneNo" />
			<html:hidden property="customerDto.mainMail" styleId="hiddenMainMail" />
			<c:set var="subMail" value="" />
			<c:forEach items="${customerDto.subMailList}" var="mail" varStatus="status">
				<c:set var="subMail" value="${subMail}${not empty subMail ? '<br>' : ''}${mail}" />
			</c:forEach>
			<input type="hidden" value="${subMail}" name="customerDto.subMailList" id="hiddenSubMail">
			<html:hidden property="customerDto.companySalesName" styleId="hiddenCompanySalesName" />

			<!-- #wrap_serch# -->
			<div id="wrap_shop_search" style="display: none">
				<div class="wrap_label">
					エリア選択：
					<select name="" id="selectArea">
						<option value="">選択してください</option>
					</select>
					ラベル選択：
					<select name="" id="selectLabel">
						<option value="">選択してください</option>
					</select>
				</div>

				<table class="table table-bordered cmn_table list_table shop_table">
				    <thead>
						<tr>
							<th width="20"><input type="checkbox" id="allCheck"></th>
							<th>店舗名</th>
							<th>都道府県</th>
							<th>市区町村</th>
							<th width="140">業態1</th>
							<th width="140">業態2</th>
						</tr>
				    </thead>
				    <tbody>
				    </tbody>
				</table>

				<hr />

				<div class="wrap_btn">
					<input type="button" name="conf" value="登 録" onclick="parent.jQuery.fancybox.close();" onmouseout="outBtn(this);" onmousedown="downBtn(this);" onmouseup="upBtn(this);">
				</div>
			</div>

			<div id="shopListIdValue" style="display: none">
				<c:if test="${not empty shopListIdList}">
					<c:forEach items="${shopListIdList}" var="shopListId">
						<input type="checkbox" checked="checked" value="${shopListId}" name="shopListIdList" />
					</c:forEach>
				</c:if>
			</div>

			<div id="wrap_shop_indeed_search" style="display: none">
				<div class="wrap_label">
					エリア選択：
					<select name="" id="selectAreaForIndeed">
						<option value="">選択してください</option>
					</select>
					ラベル選択：
					<select name="" id="selectLabelForIndeed">
						<option value="">選択してください</option>
					</select>
				</div>

				<table class="table table-bordered cmn_table list_table indeed_shop_table">
				    <thead>
						<tr>
							<th width="20"><input type="checkbox" id="indeedAllCheck"></th>
							<th>店舗名</th>
							<th>都道府県</th>
							<th>市区町村</th>
							<th width="140">業態1</th>
							<th width="140">業態2</th>
						</tr>
				    </thead>
				    <tbody>
				    </tbody>
				</table>

				<hr />

				<div class="wrap_btn">
					<input type="button" name="conf" value="登 録" onclick="parent.jQuery.fancybox.close();" onmouseout="outBtn(this);" onmousedown="downBtn(this);" onmouseup="upBtn(this);">
				</div>
			</div>

			<div id="shopListIdForIndeedValue" style="display: none">
				<c:if test="${not empty shopListIdListForIndeed}">
					<c:forEach items="${shopListIdListForIndeed}" var="shopListId">
						<input type="checkbox" checked="checked" value="${shopListId}" name="shopListIdListForIndeed" />
					</c:forEach>
				</c:if>
			</div>

			<!-- ################# 基本設定 ################# -->
			<a name="base" id="base"></a>
			<h3 class="subtitle">基本設定</h3>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="140">掲載エリア</th>
					<th width="20"><span class="attention">必須</span></th>
					<td>
						<gt:areaList name="areaList"  authLevel="${userDto.authLevel}" />
						<html:select property="areaCd" styleId="areaCd" onchange="areaLimitLoad(); return false;">
							<html:optionsCollection name="areaList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>掲載期間</th>
					<th></th>
					<td>
					<c:choose>
						<c:when test="${isEditVolumeFlg}">
							<div id="volumeAjax">
								<gt:volumeList name="volumeList" limitValue="${areaCd}" blankLineLabel="${common['gc.pullDown']}" authLevel="${userDto.authLevel}" />
								<html:select property="volumeId" styleId="volumeId">
									<html:optionsCollection name="volumeList" />
								</html:select>
							</div>
						</c:when>
						<c:otherwise>
								<gt:volumeList name="volumeList" limitValue="${areaCd}" blankLineLabel="${common['gc.pullDown']}" />
							${f:label(volumeId, volumeList, 'value', 'label')}&nbsp;
						</c:otherwise>
					</c:choose>
					</td>
				</tr>
				<tr>
					<th>連載</th>
					<th></th>
					<td>
						<html:textarea property="serialPublication" maxlength="9" style="width: 150px; height:40px;"></html:textarea>
					</td>
				</tr>
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<tr>
							<th>原稿番号</th>
							<th></th>
							<td>${f:h(id)}&nbsp;</td>
						</tr>
						<tr>
							<th>ステータス</th>
							<th></th>
							<td>
								<gt:statusList name="displayStatusList" statusKbn="<%=String.valueOf(MTypeConstants.StatusKbn.DIPLAY_STATUS_VALUE) %>" />
								${f:label(displayStatus, displayStatusList, 'value', 'label')}&nbsp;
								<c:if test="${not empty checkedStatusName}">
									(${f:h(checkedStatusName)})
								</c:if>
							</td>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<th>過去原版</th>
					<th></th>
					<td>${f:h(sourceWebId)}&nbsp;</td>
				</tr>
				<tr>
					<th><span class="fontBold">原稿名</span></th>
					<th width="20"><span class="attention">必須</span></th>
					<td><html:text property="manuscriptName" styleClass="txtInput validate[required]" /></td>
				</tr>
				<tr>
					<th>特集</th>
					<th></th>
					<td class="release">
						<span class="explain">▼登録する特集を選択してください。右の項目が登録されます。</span>
						<div id="special">
							<gt:specialList name="specialList" limitValue="${areaCd}"/>
							<html:select multiple="multiple" property="specialIdList" styleId="specialSelect">
								<html:optionsCollection name="specialList" />
							</html:select>
						</div>
					</td>
				</tr>
				<tr>
					<th>適職診断</th>
					<th></th>
					<td>
						<gt:typeList name="reasonableKbnList" typeCd="<%=MTypeConstants.ReasonableKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="reasonableKbn" styleId="reasonableKbn">
							<html:optionsCollection name="reasonableKbnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>インタビュー</th>
					<th></th>
					<td>
						<gt:typeList name="mtBlogIdList" typeCd="<%=MTypeConstants.MtBlogId.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="mtBlogId" styleId="mtBlogId">
							<html:optionsCollection name="mtBlogIdList"/>
						</html:select><br><br>
						<html:text property="topInterviewUrl" placeholder="記事No" styleId="topInterviewUrl" />
					</td>
				</tr>
				<tr>
					<th>合同企業説明会</th>
					<th><span class="attention">必須</span></th>
					<td>
						<ul class="checklist_3col clear">
						<gt:typeList name="briefingList" typeCd="<%=MTypeConstants.BriefingPresentKbn.TYPE_CD %>"/>
						<c:forEach items="${briefingList}" var="t">
							<li><label><html:radio property="briefingPresentKbn" value="${f:h(t.value)}" styleId="briefingPresentKbn_${f:h(t.value)}" styleClass="release validate[required]" />${f:h(t.label)}</label></li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>掲載終了後の表示</th>
					<th><span class="attention">必須</span></th>
					<td>
						<ul class="checklist_3col clear">
						<gt:typeList name="publicationEndDisplayFlgList" typeCd="<%=MTypeConstants.PublicationEndDisplayFlg.TYPE_CD %>"/>
						<c:forEach items="${publicationEndDisplayFlgList}" var="t">
							<li><label><html:radio property="publicationEndDisplayFlg" value="${f:h(t.value)}" styleId="publicationEndDisplayFlg_${f:h(t.value)}" styleClass="release validate[required]" />${f:h(t.label)}</label></li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>検索対象</th>
					<th><span class="attention">必須</span></th>
					<td>
						<ul class="checklist_3col clear">
						<gt:typeList name="searchTargetFlgList" typeCd="<%=MTypeConstants.SearchTargetFlg.TYPE_CD %>"/>
						<c:forEach items="${searchTargetFlgList}" var="t">
							<li><label><html:radio property="searchTargetFlg" value="${f:h(t.value)}" styleId="publicationEndDisplayFlg_${f:h(t.value)}" styleClass="release validate[required]" />${f:h(t.label)}</label></li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>所属</th>
					<th><span class="attention">必須</span></th>
					<td class="release ajaxWrap" >
						<div id="companyAjax">
							<c:choose>
								<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF  or userDto.authLevel eq AUTH_LEVEL_SALES}">
									<gt:assignedCompanyList name="companyList" authLevel="${f:h(userDto.authLevel)}" blankLineLabel="${common['gc.pullDown']}" />
								</c:when>
								<c:otherwise>
									<gt:assignedCompanyList name="companyList"  authLevel="${f:h(userDto.authLevel)}" companyId="${companyId}" />
								</c:otherwise>
							</c:choose>
							<html:select property="companyId" styleId="companyId" onchange="assignedCompanyLimitLoad(); return false;" styleClass="validate[required]">
								<html:optionsCollection name="companyList" />
							</html:select>&nbsp;
						</div>
					</td>
				</tr>
				<tr>
					<th>営業担当</th>
					<th><span class="attention">必須</span></th>
					<td class="release ajaxWrap" >
					<gt:assignedSalesList name="salesIdList" limitValue="${companyId}" blankLineLabel="${common['gc.pullDown']}" />
						<div id="salesAjax">
							<html:select property="salesId" styleId="salesId" styleClass="validate[required]">
								<html:optionsCollection name="salesIdList"/>
							</html:select>&nbsp;
						</div>
					</td>
				</tr>
				<tr>
					<th>求人識別番号</th>
					<th></th>
					<td>
						<html:text property="webNo" placeholder="求人識別番号" width="10" />
						<div class="explain">※引き継ぐ求人識別番号を入力してください</div>
					</td>
				</tr>
			</table>

			<!-- ################# 勤務地情報の設定 ################# -->
			<a name="working" id="working"></a>
			<h3 class="subtitle">勤務地情報の設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="140">勤務エリア・最寄駅</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<html:textarea property="workingPlace" cols="60" rows="10"></html:textarea>
						</ul>
					</td>
				</tr>
				<tr>
					<th width="140">勤務地詳細</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<html:textarea property="workingPlaceDetail" cols="60" rows="10"></html:textarea>
						</ul>
					</td>
				</tr>
			</table>

			<!-- ################# 店舗情報の設定 ################# -->
			<a name="shopInfo" id="shopInfo"></a>
			<h3 class="subtitle">店舗情報の設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="140">客席数</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<html:textarea property="seating" cols="60" rows="3"></html:textarea>
						</ul>
					</td>
				</tr>
				<tr>
					<th width="140">客単価</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<html:textarea property="unitPrice" cols="60" rows="3"></html:textarea>
						</ul>
					</td>
				</tr>
				<tr>
					<th width="140">営業時間</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<html:textarea property="businessHours" cols="60" rows="3"></html:textarea>
						</ul>
					</td>
				</tr>
				<tr>
					<th width="140">ホームページ</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<html:text property="homepage1"/>
						</ul>
					</td>
				</tr>
			</table>

			<!-- ################# 検索条件の設定 ################# -->
			<a name="search" id="search"></a>
			<h3 class="subtitle">検索条件の設定</h3>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="140">採用基準</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<gt:typeList name="companyCharacteristicList" typeCd="<%=MTypeConstants.CompanyCharacteristicKbn.TYPE_CD %>" />
							<c:set var="HIRING_REQUIREMENT_LIST" value="<%=MTypeConstants.CompanyCharacteristicKbn.HIRING_REQUIREMENT_LIST  %>" scope="page" />
							<c:forEach items="${companyCharacteristicList}" var="t">
								<c:if test="${HIRING_REQUIREMENT_LIST.contains(Integer.parseInt(t.value))}">
								<li><label><html:multibox property="companyCharacteristicKbnList" value="${f:h(t.value)}" styleId="companyCharacteristicKbn_${f:h(t.value)}" />${f:h(t.label)}</label></li>
								</c:if>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th width="140">企業の特徴</th>
					<th width="20"></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<gt:typeList name="companyCharacteristicList" typeCd="<%=MTypeConstants.CompanyCharacteristicKbn.TYPE_CD %>" />
							<c:set var="COMPANY_CHARACTERISTIC_LIST" value="<%=MTypeConstants.CompanyCharacteristicKbn.COMPANY_CHARACTERISTIC_LIST  %>" scope="page" />
							<c:forEach items="${companyCharacteristicList}" var="t">
								<c:if test="${COMPANY_CHARACTERISTIC_LIST.contains(Integer.parseInt(t.value))}">
								<li><label><html:multibox property="companyCharacteristicKbnList" value="${f:h(t.value)}" styleId="companyCharacteristicKbn_${f:h(t.value)}" />${f:h(t.label)}</label></li>
								</c:if>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<!--<tr>
					<th class="bdrs_bottom">フリーワード</th>
					<th></th>
					<td class="release bdrs_bottom">
					<ul id="freeword"></ul>
					<p style="margin:10px 0 10px;font-size:100%;">よく使われているタグから選択：</p>
					<c:forEach items="${tagListDto}" var="dto" varStatus="status">
						<a href="javascript:void(0)" class="tg">${dto.tagName}</a>
					</c:forEach>
					<html:hidden property="tagList" styleId="tagList"/>
					</td>
				</tr>-->
				</table>

				<!-- ################# 募集職種設定 ################# -->
				<a name="job" id="job"></a>
				<h3 class="subtitle">募集職種設定</h3>

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
												<li><label>
												<c:set var="contains" value="false" />
												<c:forEach var="employJobKbn" items="${employJobKbnList}">
													<c:if test="${employJobKbn eq employJobValue}">
														<c:set var="contains" value="true" />
													</c:if>
												</c:forEach>
												<input type="checkbox" name="employJobKbnList"
													value="${employJobValue}" id="job${employJobValue}"
													data-employ-value="${e.value}" data-employ-name="${e.label}"
													data-job-value="${j.value}" data-job-name="${j.label}"
													<c:if test="${contains}">checked="checked"</c:if>
													 />${f:h(j.label)}
												</label></li>
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
			<gt:typeList name="publicationList" typeCd="<%=MTypeConstants.PublicationFlg.TYPE_CD %>" />
			<gt:typeList name="saleryStructureList" typeCd="<%=MTypeConstants.NewSaleryStructureKbn.TYPE_CD %>" blankLineLabel="--" />
			<gt:typeList name="otherPersonHuntingConditionList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD %>"/>
			<gt:typeList name="treatmentList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />
			<c:set var="TWO_DAYS_OFF" value="<%=MTypeConstants.TreatmentKbn.TWO_DAYS_OFF%>"></c:set>

			<h4 class="occupation">各職種の募集要項を設定してください</h4>
			<div id="sortable_occ">
				<c:if test="${not empty webJobDtoList}">
				<c:forEach items="${webJobDtoList}" var="jobDto" varStatus="status">
					<c:set var="wrapId" value="${jobDto.employPtnKbn}-${jobDto.jobKbn}" />
					<c:set var="employName" value="${f:label(jobDto.employPtnKbn, employPtnList, 'value', 'label')}" />
					<c:set var="jobName" value="${f:label(jobDto.jobKbn, jobList, 'value', 'label')}" />
					<div id="wrap${wrapId}" class="wrapJob activeWrapJob">
						<html:hidden property="webJobDtoList[${status.index}].displayOrder" styleClass="displayOrder" />
						<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table sort-elements" id="occu${wrapId}">
							<tr>
							<td width="100">
								${f:h(employName)}
								<html:hidden property="webJobDtoList[${status.index}].employPtnKbn" />
							</td>
							<td>
								${f:h(jobName)}
								<html:hidden property="webJobDtoList[${status.index}].jobKbn" />
							</td>
							<td width="160">
								<gt:typeList name="" typeCd=""/>
								<ul class="checklist_2col clear">
								<c:forEach items="${publicationList}" var="t">
									<li><label>
										<html:radio property="webJobDtoList[${status.index}].publicationFlg" value="${t.value}" />${f:h(t.label)}
									</label></li>
								</c:forEach>
								</ul>
							</td>
							<td width="260" class="posi_center">
								<input type="button" name="${jobDto.editFlg == 1 ? 'occuEdit' : 'occuNew'}" value="募集要項を作成"
									 class="occuAddBtn" id="occuBtn${wrapId}"
									 data-fancybox="modal" data-src="#modal${wrapId}"
									 data-wrap-id="${wrapId}">
								<html:hidden property="webJobDtoList[${status.index}].editFlg" styleId="editFlg${wrapId}" />
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
								<th width="140">仕事内容</th>
								<th width="20"><span class="attention">必須</span></th>
								<td>
									<html:textarea property="webJobDtoList[${status.index}].workContents" cols="60" rows="10" data-name="workContents"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>給与</th>
								<th></th>
								<td class="release">
									<html:select property="webJobDtoList[${status.index}].saleryStructureKbn" data-name="saleryStructureKbn" data-man-text="salary_man_text${status.index}">
										<html:optionsCollection name="saleryStructureList"/>
									</html:select>
									&nbsp;&nbsp;
									<html:text property="webJobDtoList[${status.index}].lowerSalaryPrice" style="width: 80px" min="0"
										id="lowerSalaryPrice${wrapId}" data-name="lowerSalaryPrice" />&nbsp;円
									　～　
									<html:text property="webJobDtoList[${status.index}].upperSalaryPrice" style="width: 80px" min="0"
									id="upperSalaryPrice${wrapId}" data-name="upperSalaryPrice" />&nbsp;円
										<br>
										<font color="red">■掲載日 11/30（木）以降の号数に掲載する求人は、価格に加えて「万」を語尾に追加してください。<br> 例）25万1000、25万など</font>
										<br></br>
									<html:textarea property="webJobDtoList[${status.index}].salaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。&#10;ただし、「時給」・「日給」が選択されている場合には、入力は必要なく、記載しても内容が表示されません。" data-name="salaryDetail"></html:textarea><br><br>
									<html:textarea property="webJobDtoList[${status.index}].salary" cols="60" rows="10" data-name="salary" placeholder="【備考欄】記載内容が反映されます。"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>給与2<br>(初年度想定年収)</th>
								<th></th>
								<td class="release">
									年収
									&nbsp;&nbsp;
									<html:text property="webJobDtoList[${status.index}].annualLowerSalaryPrice" style="width: 80px" min="0"
										id="annualLowerSalaryPrice${wrapId}"
										data-name="annualLowerSalaryPrice" />&nbsp;円
									　～　
									<html:text property="webJobDtoList[${status.index}].annualUpperSalaryPrice" style="width: 80px" min="0"
										id="annualUpperSalaryPrice${wrapId}"
										data-name="annualUpperSalaryPrice" />&nbsp;円
										<br></br>
									<html:textarea property="webJobDtoList[${status.index}].annualSalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="annualSalaryDetail"></html:textarea><br><br>
									<html:textarea property="webJobDtoList[${status.index}].annualSalary" cols="60" rows="10" data-name="annualSalary" placeholder="【備考欄】記載内容が反映されます。"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>給与3<br>(初年度想定月収)</th>
								<th></th>
								<td class="release">
									月収
									&nbsp;&nbsp;
									<html:text property="webJobDtoList[${status.index}].monthlyLowerSalaryPrice" style="width: 80px" min="0"
										id="monthlyLowerSalaryPrice${wrapId}"
										data-name="monthlyLowerSalaryPrice" />&nbsp;円
									　～　
									<html:text property="webJobDtoList[${status.index}].monthlyUpperSalaryPrice" style="width: 80px" min="0"
										id="monthlyUpperSalaryPrice${wrapId}"
										data-name="monthlyUpperSalaryPrice" />&nbsp;円
										<br></br>
									<html:textarea property="webJobDtoList[${status.index}].monthlySalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="monthlySalaryDetail"></html:textarea><br><br>
									<html:textarea property="webJobDtoList[${status.index}].monthlySalary" cols="60" rows="10" data-name="monthlySalary" placeholder="【備考欄】記載内容が反映されます。"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>求める人物像・資格</th>
								<th></th>
								<td class="release">
									<span class="attention">※${common['gc.webdata.personHunting.maxLength']}文字以内</span>　<span id="personHuntingSpan${wrapId}">残り${common['gc.webdata.personHunting.maxLength']}文字</span><br />
									<html:textarea property="webJobDtoList[${status.index}].personHunting" cols="60" rows="10"
										styleId="webJobDtoList[${status.index}].personHunting"
										class="text_count"
										data-wrap-id="${wrapId}"
										data-max-length="${common['gc.webdata.personHunting.maxLength']}"
										data-name="personHunting"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>休日・休暇</th>
								<th></th>
								<td>
									<ul class="checklist_3col clear">
										<c:forEach items="${treatmentList}" var="t">
											<c:if test="${t.value eq TWO_DAYS_OFF}">
												<li><label>
													<html:multibox property="webJobDtoList[${status.index}].treatmentKbnList"
														value="${f:h(t.value)}"  data-name="treatmentKbnList" />
													${f:h(t.label)}
												</label></li>
											</c:if>
										</c:forEach>
									</ul>
									<br />
									<html:textarea property="webJobDtoList[${status.index}].holiday" cols="60" rows="10" data-name="holiday"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>勤務時間</th>
								<th></th>
								<td>
									<html:textarea property="webJobDtoList[${status.index}].workingHours" cols="60" rows="10" data-name="workingHours"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>待遇</th>
								<th></th>
								<td>
									<ul class="checklist_3col clear">
									<c:forEach items="${treatmentList}" var="t">
										<c:if test="${t.value ne TWO_DAYS_OFF}">
											<li><label>
												<html:multibox property="webJobDtoList[${status.index}].treatmentKbnList" value="${f:h(t.value)}" data-name="treatmentKbnList" />
												${f:h(t.label)}
											</label></li>
										</c:if>
									</c:forEach>
									</ul>
									<br />
									<html:textarea property="webJobDtoList[${status.index}].treatment" cols="60" rows="10" data-name="treatment"></html:textarea>
								</td>
							</tr>
							<tr>
								<th>加入保険<br><span class="attention">※スタンバイ・求人ボックス用</span></th>
								<th></th>
								<td><html:text property="webJobDtoList[${status.index}].insurance" styleClass="txtInput" data-name="insurance"/><br>記入例)<br>社会保険完備、雇用・労災保険<br>社会保険完備(条件規定あり)、労災保険</td>
							</tr>
							<tr>
								<th>契約期間<br><span class="attention">※スタンバイ・求人ボックス用</span></th>
								<th></th>
								<td><html:text property="webJobDtoList[${status.index}].contractPeriod" styleClass="txtInput" data-name="contractPeriod"/><br>記入例)<br>入社より●●年の既定の定めあり。<br>期間の定めなし<br>内定時までに開示します</td>
							</tr>
							<tr style="display:none" class="jobShopList">
								<th>店舗</th>
								<th class="allCheckboxArea">
									<input type="checkbox" data-index="${status.index}" class="jobShopListAllCheckbox">
								</th>
								<td>
									<ul class="checklist_3col clear jobShopCheckbox" data-index="${status.index}">
										<li>
											<label>
												<input type="checkbox" name="webJobDtoList[${status.index}].shopIdList" class="webJobShopList" data-name="webJobDtoList">
											</label>
										</li>
									</ul>
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

			<!-- ################# 連絡先・応募方法 ################# -->
			<a name="applicant" id="applicant"></a>
			<h3 class="subtitle">連絡先・応募方法</h3>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="140">電話番号/受付時間</th>
					<th width="20"></th>
					<td>
						<span class="attention">※電話番号は半角で入力</span><br>
						<html:textarea property="phoneReceptionist" cols="60" rows="10"></html:textarea>
					</td>
				</tr>
				<c:if test="${pageKbn ne PAGE_INPUT}">
					<c:choose>
						<c:when test="${not empty customerDto.id}">
							<c:set var="ipPhoneTrDisplay" value="" scope="page" />
						</c:when>
						<c:otherwise>
							<c:set var="ipPhoneTrDisplay" value="none" scope="page" />
						</c:otherwise>
					</c:choose>
					<tr id="ipPhoneTr" style="display: ${ipPhoneTrDisplay};">
						<th>IP電話</th>
						<th></th>
						<td id="phoneNo">
							<span class="attention">※3つまで入力可</span><br />
							<span class="attention">※10～11桁の半角数字で入力</span>
							<div class="IpPhone">1&nbsp;&nbsp;&nbsp;電話&nbsp;&nbsp;<html:text property="phoneNo1"  styleClass="inactive" />
							&nbsp;&nbsp;&nbsp;IP電話&nbsp;:&nbsp;<span id="spanIpPhoneNo1">${f:h(ipPhoneNo1)}</span><html:hidden property="ipPhoneNo1" /></div>
							<div class="IpPhone">2&nbsp;&nbsp;&nbsp;電話&nbsp;&nbsp;<html:text property="phoneNo2"  styleClass="inactive" />
							&nbsp;&nbsp;&nbsp;IP電話&nbsp;:&nbsp;<span id="spanIpPhoneNo2">${f:h(ipPhoneNo2)}</span><html:hidden property="ipPhoneNo2" /></div>
							<div class="IpPhone">3&nbsp;&nbsp;&nbsp;電話&nbsp;&nbsp;<html:text property="phoneNo3"  styleClass="inactive" />
							&nbsp;&nbsp;&nbsp;IP電話&nbsp;:&nbsp;<span id="spanIpPhoneNo3">${f:h(ipPhoneNo3)}</span><html:hidden property="ipPhoneNo3" /></div>
							<input type="button" id="ipPhoneButton" value="IP電話番号を取得する" onclick="getIPPhoneNo();" > <span class="attention">電話番号を修正した場合は必ずもう一度「IP電話番号を取得する」ボタンを押して下さい。</span>
						</td>
					</tr>
				</c:if>
				<tr>
					<th>応募フォーム</th>
					<th><span class="attention">必須</span></th>
					<td class="release">
						<ul class="checklist_3col clear">
						<gt:typeList name="applicationFormKbnList" typeCd="<%=MTypeConstants.ApplicationFormKbn.TYPE_CD %>" />
						<c:forEach items="${applicationFormKbnList}" var="t">
							<li><label><html:radio property="applicationFormKbn" value="${t.value}" styleId="applicationFormKbn_${t.value}" styleClass="release"  />${f:h(t.label)}</label></li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>質問・店舗見学フォーム</th>
					<th><span class="attention">必須</span></th>
					<td class="release">
						<ul class="checklist_3col clear">
						<gt:typeList name="observationKbnList" typeCd="<%=MTypeConstants.ObservationKbn.TYPE_CD %>" noDisplayValue="<%=MTypeConstants.ObservationKbn.getAdminNoDispList() %>" />
						<c:forEach items="${observationKbnList}" var="t">
							<li><label><html:radio property="observationKbn" value="${t.value}" styleId="observationKbn_${t.value}" styleClass="release"  />${f:h(t.label)}</label></li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>応募メール送信先<br />
						<span class="attention">※いずれかを選択</span></th>
					<th></th>
					<td class="release">
						<ul class="checklist clear">
						<c:set var="communicationMailKbnCustomer" value="<%=MTypeConstants.CommunicationMailKbn.CUSTOMER_MAIL %>" />
						<c:set var="communicationMailKbnInput" value="<%=MTypeConstants.CommunicationMailKbn.INPUT_MAIL %>" />
							<li>
								<label>
									<c:choose>
										<c:when test="${communicationMailKbn == null }">
											<html:radio property="communicationMailKbn" styleId="customer_mail" value="${communicationMailKbnCustomer}" checked="checked"/>&nbsp;
										</c:when>
										<c:otherwise>
											<html:radio property="communicationMailKbn" styleId="customer_mail" value="${communicationMailKbnCustomer}" />&nbsp;
										</c:otherwise>
									</c:choose>
									顧客データ登録時のアドレス
								</label>
							</li>
							<li>
								<label>
									<html:radio property="communicationMailKbn" styleId="input_mail" value="${communicationMailKbnInput}" />&nbsp;
									<html:text property="mail" size="50" styleClass="disabled" />
								</label>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<th>応募方法</th>
					<th></th>
					<td><html:textarea property="applicationMethod" cols="60" rows="10"></html:textarea></td>
				</tr>
				<tr>
					<th>面接地住所/交通</th>
					<th></th>
					<td><html:textarea property="addressTraffic" cols="60" rows="10"></html:textarea></td>
				</tr>
			</table>

			<!-- ################# 原稿設定 ################# -->
			<a name="setting" id="setting"></a>
			<h3 class="subtitle">原稿設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="140">サイズ</th>
					<th width="20"><span class="attention">必須</span></th>
					<td class="release">
						<gt:typeList name="sizeKbnList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" suffix="${common['gc.sizeKbn.suffix']}" />
						<ul class="checklist_6col clear">
						<c:forEach items="${sizeKbnList}" var="t">
							<li>
								<label>
									<html:radio property="sizeKbn" value="${t.value}" styleId="sizeKbn_${t.value}" styleClass="validate[required]" data-prompt-position="topLeft" />
									${f:h(t.label)}
								</label>
							</li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>ロゴ</th>
					<th></th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="logo_img" value="<%= MTypeConstants.MaterialKbn.LOGO %>" />
							<c:set var="logo_thumb_img" value="<%= MTypeConstants.MaterialKbn.LOGO_THUMB %>" />
							<a name="materialPosition${logo_img}" id="materialPosition${logo_img}"></a>
							<span>ロゴ&nbsp;：&nbsp;詳細ページ右上に表示。複数店舗の使用は不可。</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, logo_thumb_img)}">
									<input type="file" name="logoImg" id="upImg_${logo_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${logo_img}" onclick="addMaterial(${logo_img})" value="アップロード" />
									<span id="img_${logo_img}" data-alt-val="ロゴ"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${logo_img}" onclick="delMaterial(${logo_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="logoImg" id="upImg_${logo_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${logo_img}" onclick="addMaterial(${logo_img})" value="アップロード" style="display:none" />
									<span id="img_${logo_img}" data-alt-val="ロゴ">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, logo_img, idForDirName))}" title="ロゴ" data-lightbox="photo">
											<img alt="ロゴ" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, logo_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${logo_img}" onclick="delMaterial(${logo_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="main1Img_tr">
					<th>写真/メイン1<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>A</li><li>B</li><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="main1_img" value="<%= MTypeConstants.MaterialKbn.MAIN_1 %>" />
							<c:set var="main1_thumb_img" value="<%= MTypeConstants.MaterialKbn.MAIN_1_THUMB %>" />
							<a name="materialPosition${main1_img}" id="materialPosition${main1_img}"></a>
							<span>メイン1&nbsp;：&nbsp;本文1の左に表示する写真（330×250）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, main1_thumb_img)}">
									<input type="file" name="main1Img" id="upImg_${main1_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${main1_img}" onclick="addMaterial(${main1_img})" value="アップロード" />
									<span id="img_${main1_img}" data-alt-val="メイン1"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${main1_img}" onclick="delMaterial(${main1_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="main1Img" id="upImg_${main1_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${main1_img}" onclick="addMaterial(${main1_img})" value="アップロード" style="display:none" />
									<span id="img_${main1_img}" data-alt-val="メイン1">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, main1_img, idForDirName))}" title="メイン1" data-lightbox="photo">
											<img alt="メイン1" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, main1_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${main1_img}" onclick="delMaterial(${main1_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="main2Img_tr">
					<th>写真/メイン2<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="main2_img" value="<%= MTypeConstants.MaterialKbn.MAIN_2 %>" />
							<c:set var="main2_thumb_img" value="<%= MTypeConstants.MaterialKbn.MAIN_2_THUMB %>" />
							<a name="materialPosition${main2_img}" id="materialPosition${main2_img}"></a>
							<span>メイン2&nbsp;：&nbsp;本文1の左に表示する写真（330×250）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, main2_thumb_img)}">
									<input type="file" name="main2Img" id="upImg_${main2_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${main2_img}" onclick="addMaterial(${main2_img})" value="アップロード" />
									<span id="img_${main2_img}" data-alt-val="メイン2"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${main2_img}" onclick="delMaterial(${main2_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="main2Img" id="upImg_${main2_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${main2_img}" onclick="addMaterial(${main2_img})" value="アップロード" style="display:none" />
									<span id="img_${main2_img}" data-alt-val="メイン2">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, main2_img, idForDirName))}" title="メイン2" data-lightbox="photo">
											<img alt="メイン2" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, main2_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${main2_img}" onclick="delMaterial(${main2_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="main3Img_tr">
					<th>写真/メイン3<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>D</li><li>E</li></ul>
					</th>
					<th></th>
                   	<td class="release photo">
						<div class="photoWrap">
							<c:set var="main3_img" value="<%= MTypeConstants.MaterialKbn.MAIN_3 %>" />
							<c:set var="main3_thumb_img" value="<%= MTypeConstants.MaterialKbn.MAIN_3_THUMB %>" />
							<a name="materialPosition${main3_img}" id="materialPosition${main3_img}"></a>
							<span>メイン3&nbsp;：&nbsp;本文1の左に表示する写真（330×250）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, main3_thumb_img)}">
									<input type="file" name="main3Img" id="upImg_${main3_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${main3_img}" onclick="addMaterial(${main3_img})" value="アップロード" />
									<span id="img_${main3_img}" data-alt-val="メイン3"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${main3_img}" onclick="delMaterial(${main3_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="main3Img" id="upImg_${main3_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${main3_img}" onclick="addMaterial(${main3_img})" value="アップロード" style="display:none" />
									<span id="img_${main3_img}" data-alt-val="メイン3">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, main3_img, idForDirName))}" title="メイン3" data-lightbox="photo">
											<img alt="メイン3" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, main3_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${main3_img}" onclick="delMaterial(${main3_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="rightImg_tr">
					<th>写真/右画像<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="right_img" value="<%=MTypeConstants.MaterialKbn.RIGHT %>" />
							<c:set var="right_thumb_img" value="<%=MTypeConstants.MaterialKbn.RIGHT_THUMB %>" />
							<a name="materialPosition${right_img}" id="materialPosition${right_img}"></a>
							<span>右画像&nbsp;：&nbsp;詳細ページ右側に表示する写真（180×250）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, right_thumb_img)}">
									<input type="file" name="rightImg" id="upImg_${right_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${right_img}" onclick="addMaterial(${right_img})" value="アップロード" />
									<span id="img_${right_img}" data-alt-val="右画像"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${right_img}" onclick="delMaterial(${right_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="rightImg" id="upImg_${right_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${right_img}" onclick="addMaterial(${right_img})" value="アップロード" style="display:none" />
									<span id="img_${right_img}" data-alt-val="右画像">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, right_img, idForDirName))}" title="右画像" data-lightbox="photo">
											<img alt="右画像" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, right_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${right_img}" onclick="delMaterial(${right_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="photoAImg_tr">
					<th>写真/キャプション1<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="photoA_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_A %>" />
							<c:set var="photoA_thumb_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_A_THUMB %>" />
							<a name="materialPosition${photoA_img}" id="materialPosition${photoA_img}"></a>
							<span>キャプション1&nbsp;：&nbsp;詳細ページ中段。3点あるうちの左に表示する写真（235×170）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, photoA_thumb_img)}">
									<input type="file" name="photoAImg" id="upImg_${photoA_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${photoA_img}" onclick="addMaterial(${photoA_img})" value="アップロード" />
									<span id="img_${photoA_img}" data-alt-val="キャプション1"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${photoA_img}" onclick="delMaterial(${photoA_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="photoAImg" id="upImg_${photoA_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${photoA_img}" onclick="addMaterial(${photoA_img})" value="アップロード" style="display:none" />
									<span id="img_${photoA_img}" data-alt-val="キャプション1">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, photoA_img, idForDirName))}" title="キャプション1" data-lightbox="photo">
											<img alt="キャプション1" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, photoA_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${photoA_img}" onclick="delMaterial(${photoA_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="photoBImg_tr">
					<th>写真/キャプション2<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="photoB_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_B %>" />
							<c:set var="photoB_thumb_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_B_THUMB %>" />
							<a name="materialPosition${photoB_img}" id="materialPosition${photoB_img}"></a>
							<span>キャプション2&nbsp;：&nbsp;詳細ページ中段。3点あるうちの中央に表示する写真（235×170）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, photoB_thumb_img)}">
									<input type="file" name="photoBImg" id="upImg_${photoB_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${photoB_img}" onclick="addMaterial(${photoB_img})" value="アップロード" />
									<span id="img_${photoB_img}" data-alt-val="キャプション2"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${photoB_img}" onclick="delMaterial(${photoB_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="photoBImg" id="upImg_${photoB_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${photoB_img}" onclick="addMaterial(${photoB_img})" value="アップロード" style="display:none" />
									<span id="img_${photoB_img}" data-alt-val="キャプション2">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, photoB_img, idForDirName))}" title="キャプション2" data-lightbox="photo">
											<img alt="キャプション2" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, photoB_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${photoB_img}" onclick="delMaterial(${photoB_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="photoCImg_tr">
					<th>写真/キャプション3<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
 					<td class="release photo">
						<div class="photoWrap">
							<c:set var="photoC_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_C %>" />
							<c:set var="photoC_thumb_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_C_THUMB %>" />
							<a name="materialPosition${photoC_img}" id="materialPosition${photoC_img}"></a>
							<span>キャプション3&nbsp;：&nbsp;詳細ページ中段。3点あるうちの右に表示する写真（235×170）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, photoC_thumb_img)}">
									<input type="file" name="photoCImg" id="upImg_${photoC_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${photoC_img}" onclick="addMaterial(${photoC_img})" value="アップロード" />
									<span id="img_${photoC_img}" data-alt-val="キャプション3"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${photoC_img}" onclick="delMaterial(${photoC_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="photoCImg" id="upImg_${photoC_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${photoC_img}" onclick="addMaterial(${photoC_img})" value="アップロード" style="display:none" />
									<span id="img_${photoC_img}" data-alt-val="キャプション3">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, photoC_img, idForDirName))}" title="キャプション3" data-lightbox="photo">
											<img alt="キャプション3" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, photoC_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${photoC_img}" onclick="delMaterial(${photoC_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr id="attentionHereImg_tr">
					<th>写真/ここに注目<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>B</li><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
 					<td class="release photo">
						<div class="photoWrap">
							<c:set var="attention_here_img" value="<%= MTypeConstants.MaterialKbn.ATTENTION_HERE %>" />
							<c:set var="attention_here_thumb_img" value="<%= MTypeConstants.MaterialKbn.ATTENTION_HERE_THUMB %>" />
							<a name="materialPosition${attention_here_img}" id="materialPosition${attention_here_img}"></a>
							<span>ここに注目&nbsp;：&nbsp;詳細ページ下部に表示する写真（350×180）</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, attention_here_thumb_img)}">
									<input type="file" name="attentionHereImg" id="upImg_${attention_here_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${attention_here_img}" onclick="addMaterial(${attention_here_img})" value="アップロード" />
									<span id="img_${attention_here_img}" data-alt-val="ここに注目"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${attention_here_img}" onclick="delMaterial(${attention_here_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="attentionHereImg" id="upImg_${attention_here_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${attention_here_img}" onclick="addMaterial(${attention_here_img})" value="アップロード" style="display:none" />
									<span id="img_${attention_here_img}" data-alt-val="ここに注目">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, attention_here_img, idForDirName))}" title="ここに注目" data-lightbox="photo">
											<img alt="ここに注目" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, attention_here_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${attention_here_img}" onclick="delMaterial(${attention_here_img})" value="削除" />
								</c:otherwise>
							</c:choose>

						</div>
					</td>
				</tr>
				<tr id="freeImg_tr">
					<th>フリースペース<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="free_img" value="<%= MTypeConstants.MaterialKbn.FREE %>" />
							<c:set var="free_thumb_img" value="<%= MTypeConstants.MaterialKbn.FREE_THUMB %>" />
							<a name="materialPosition${free_img}" id="materialPosition${free_img}"></a>
							<span>フリースペース&nbsp;：&nbsp;検索リストに表示する写真</span>
							<span class="attention">※公開時の詳細ページに表示（プレビュー画面にて要確認）<br />
								(D:370×250,&nbsp;E:880×400)
							</span>
							<c:choose>
								<c:when test="${!gf:isMapExsists(materialMap, free_thumb_img)}">
									<input type="file" name="freeImg" id="upImg_${free_img}" value="参照" size="50"  />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${free_img}" onclick="addMaterial(${free_img})" value="アップロード" />
									<span id="img_${free_img}" data-alt-val="フリースペース"></span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${free_img}" onclick="delMaterial(${free_img})" value="削除" style="display:none" />
								</c:when>
								<c:otherwise>
									<input type="file" name="freeImg" id="upImg_${free_img}" value="参照" size="50" style="display:none" />&nbsp;
									<html:button property="upMaterial" styleId="uploadImgBtn_${free_img}" onclick="addMaterial(${free_img})" value="アップロード" style="display:none" />
									<span id="img_${free_img}" data-alt-val="フリースペース">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, free_img, idForDirName))}" title="フリースペース" data-lightbox="photo">
											<img alt="フリースペース" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, free_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</span>
									<html:button property="deleteMaterial" styleId="deleteImgBtn_${free_img}" onclick="delMaterial(${free_img})" value="削除" />
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr>
					<th>動画URL</th>
					<th></th>
					<td>
						<html:text property="movieUrl"  styleId="movieUrlId" />
					</td>
				</tr>
				<tr id="movieListDisplayFlg_tr">
					<th>検索一覧に動画を表示<br>
						<span class="attention">※以下のサイズのとき表示</span>
						<ul class="clear"><li>D</li><li>E</li></ul></th>
					<th></th>
					<td>
						<gt:typeList name="movieListDisplayFlgList" typeCd="<%=MTypeConstants.MovieListDisplayFlg.TYPE_CD %>" />
						<ul class="checklist_2col clear">
							<c:forEach items="${movieListDisplayFlgList}" var="t">
								<li><label><html:radio property="movieListDisplayFlg" value="${t.value}" />${f:h(t.label)}</label></li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>原稿内で動画を表示</th>
					<th></th>
					<td>
						<gt:typeList name="movieDetailDisplayFlgList" typeCd="<%=MTypeConstants.MovieDetailDisplayFlg.TYPE_CD %>" />
						<ul class="checklist_2col clear">
							<c:forEach items="${movieDetailDisplayFlgList}" var="t">
								<li><label><html:radio property="movieDetailDisplayFlg" value="${t.value}" />${f:h(t.label)}</label></li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>キャッチコピー</th>
					<th></th>
					<td>
						<html:text property="catchCopy1" maxlength="20" styleId="catchCopy1TextId" styleClass="txt_catchcopy" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);" />&nbsp;<span id="catchCopy1Span"></span><br />
						<html:text property="catchCopy2" maxlength="20" styleId="catchCopy2TextId" styleClass="txt_catchcopy" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);" />&nbsp;<span id="catchCopy2Span"></span><br />
						<html:text property="catchCopy3" maxlength="20" styleId="catchCopy3TextId" styleClass="txt_catchcopy" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);" />&nbsp;<span id="catchCopy3Span"></span><br />
						<span class="attention">※公開時の詳細ページ各20文字以内（プレビュー画面にて要確認）</span>
					</td>
				</tr>
				<tr id="sentence1_tr">
					<th>本文1</th>
					<th></th>
					<td>
					<span id="sentence1Span" class="span8line"></span><br />
					<html:textarea property="sentence1" cols="60" rows="10"  styleClass="textarea8line" styleId="sentence1TextId" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"></html:textarea><br />
						<span class="attention">※公開時の詳細ページに表示（プレビュー画面にて要確認）<br />
						(テキストWEB：200文字、Ａ～Ｃタイプ：296文字、Ｄ・Ｅタイプ：444文字以内）
						</span>
					</td>
				</tr>
				<tr id="sentence2_tr">
					<th>本文2<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td>
						<span id="sentence2Span" class="span8line"></span><br />
						<html:textarea property="sentence2" cols="60" rows="10"  styleId="sentence2TextId" styleClass="textarea8line"  onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"></html:textarea><br />
						<span class="attention">※公開時の詳細ページに表示（プレビュー画面にて要確認）<br />
							(Ｃタイプ：603文字、Ｄ・Ｅタイプ：550文字以内）
						</span>
					</td>
				</tr>
				<tr id="sentence3_tr">
					<th>本文3<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td>
						<span id="sentence3Span" class="span8line"></span><br />
						<html:textarea property="sentence3" cols="60" rows="10"  styleId="sentence3TextId" styleClass="textarea8line"  onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"></html:textarea><br />
						<span class="attention">※公開時の詳細ページに表示（プレビュー画面にて要確認）<br />
							(Ｄ・Ｅタイプ：670文字以内）
						</span>
					</td>
				</tr>
				<tr id="captionA_tr">
					<th>キャプション1.文<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td>
						<span id="captionASpan" class="areaCaptionSpan"></span><br />
						<html:textarea property="captionA" cols="60" rows="10" styleId="captionATextId" styleClass="areaCaption" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"></html:textarea><br />
						<span class="attention">※公開時の詳細ページ19文字 3行以内</span>
					</td>
				</tr>
				<tr id="captionB_tr">
					<th>キャプション2.文<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td>
						<span id="captionBSpan" class="areaCaptionSpan"></span><br />
						<html:textarea property="captionB" cols="60" rows="10" styleId="captionBTextId" styleClass="areaCaption" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"></html:textarea><br />
						<span class="attention">※公開時の詳細ページ19文字 3行以内</span>
					</td>
				</tr>
				<tr id="captionC_tr">
					<th>キャプション3.文<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td>
						<span id="captionCSpan" class="areaCaptionSpan"></span><br />
						<html:textarea property="captionC" cols="60" rows="10" styleId="captionCTextId" styleClass="areaCaption" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"></html:textarea><br />
						<span class="attention">※公開時の詳細ページ19文字 3行以内</span>
					</td>
				</tr>
				<tr id="attentionHereTitle">
					<th>タイトル/ここに注目<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>B</li><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th></th>
					<td>
						<span id="attentionHereTitleSpan" class="catchcopySpan"></span><br />
						<html:text property="attentionHereTitle" maxlength="15" styleId="attentionHereTitleTextId" styleClass="txt_catchcopy"  onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"/><br />
						<span class="attention">※公開時の詳細ページ15文字以内（プレビュー画面にて要確認）</span>
					</td>
				</tr>
				<tr id="attentionHereSentence_tr">
					<th class="bdrs_bottom">文章/ここに注目<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>B</li><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<th class="bdrs_bottom"></th>
					<td class="bdrs_bottom">
						<span id="attentionHereSentenceSpan" style="float:right"></span><br />
						<html:textarea property="attentionHereSentence" cols="60" rows="10" styleId="attentionHereSentenceTextId" styleClass="textarea6line" onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);"></html:textarea><br />
						<span class="attention">※公開時の詳細ページ48文字 6行以内（プレビュー画面にて要確認）</span>
					</td>
				</tr>
				<% //素材アップロードor削除時のパラメータ用 %>
				<html:hidden property="hiddenMaterialKbn" styleId="hiddenMaterialKbn" />
			</table>
			<hr />
			<br />

			<!-- ################# ピックアップ求人設定 ################# -->
			<a name="pickup" id="pickup"></a>
			<h3 class="subtitle">ピックアップ求人設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="140">ピックアップ求人</th>
					<th width="20"></th>
					<td class="release">
						<% //管理者のみ入力可 %>
						<ul class="checklist_3col clear">
						<c:choose>
							<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
								<gt:typeList name="attentionShopFlgList" typeCd="<%=MTypeConstants.AttentionShopFlg.TYPE_CD %>" />
								<c:forEach items="${attentionShopFlgList}" var="t">
									<li><label><html:radio property="attentionShopFlg" value="${t.value}" styleId="attentionShopFlg_${t.value}" styleClass="release"  />${f:h(t.label)}</label></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<gt:typeList name="attentionShopFlgList" typeCd="<%=MTypeConstants.AttentionShopFlg.TYPE_CD %>" />
								<li>${f:label(attentionShopFlg, attentionShopFlgList, 'value', 'label')}</li>
							</c:otherwise>
						</c:choose>
						</ul>
						<br>
						<html:text property="attentionShopStartDate" styleId="attentionShopStartDate" placeholder="開始日"/>　～　<html:text property="attentionShopEndDate" styleId="attentionShopEndDate" placeholder="終了日"/>
					</td>
				</tr>
				<tr>
					<th>検索上位表示</th>
					<th></th>
					<td class="release">
						<ul class="checklist_3col clear">
							<gt:typeList name="searchPreferentialFlgList" typeCd="<%=MTypeConstants.SearchPreferentialFlg.TYPE_CD %>" />
							<c:forEach items="${searchPreferentialFlgList}" var="t">
								<li><label><html:radio property="searchPreferentialFlg" value="${t.value}" styleId="searchPreferentialFlg_${t.value}" styleClass="release"  />${f:h(t.label)}</label></li>
							</c:forEach>
						</ul>
						<br>
						<html:text property="searchPreferentialStartDate" styleId="searchPreferentialStartDate" placeholder="開始日"/>　～　<html:text property="searchPreferentialEndDate" styleId="searchPreferentialEndDate" placeholder="終了日"/>
					</td>
				</tr>
				<tr>
					<th>ピックアップ求人名</th>
					<th></th>
					<td class="release">
						<span id="attentionShopNameSpan" class="textSpan"></span><br />
						<html:text property="attentionShopName" size="89" maxlength="30" styleId="attentionShopNameTextId"  onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);" /><br />
						<span class="attention">※30字以内</span>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">ピックアップ求人文章</th>
					<th></th>
					<td class="release">
						<span id="attentionShopSentenceSpan" class="textSpan"></span><br />
						<html:textarea property="attentionShopSentence" cols="60" rows="5" maxlength="93" styleId="attentionShopSentenceTextId"  onkeyup="countTextChar(this);" onfocusout="countTextChar(this);" onchange="countTextChar(this);" /><br />
						<span class="attention">※93字以内</span>
					</td>
				</tr>
			</table>
			<hr />
			<br />

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th class="bdrs_bottom" width="140">メモ</th>
					<th width="20"></th>
					<td class="bdrs_bottom release"><html:textarea property="memo" cols="60" rows="10" style="width:500px;height:70px"></html:textarea></td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="conf_btn" />
				<html:button property="previewPc" value="PCプレビュー" onclick="preview('PC_PREVIEW', this);" styleId="prev_pc_btn" />
				<html:button property="previewSmartPhone" value="スマホプレビュー" onclick="preview('SMARTPHONE_PREVIEW', this);" styleId="prev_sp_btn" />
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" styleId="back_btn" />
					</c:when>
				</c:choose>
			</div>

		</s:form>

		<%-- 職種追加した際のテンプレート --%>
		<div class="cloneJob" style="display:none">
			<div id="wrap@employValue-@jobValue" class="wrapJob">
				<input type="hidden" name="webJobDtoList[@index].displayOrder" class="displayOrder" />
				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table sort-elements" id="occu@employValue-@jobValue">
					<tr>
					<td width="100">
						@employName
						<input type="hidden" name="webJobDtoList[@index].employPtnKbn" value="@employValue" />
					</td>
					<td>
						@jobName
						<input type="hidden" name="webJobDtoList[@index].jobKbn" value="@jobValue" />
					</td>
					<td width="160">
						<gt:typeList name="" typeCd=""/>
						<ul class="checklist_2col clear">
						<c:forEach items="${publicationList}" var="t">
							<li><label>
							<input type="radio" name="webJobDtoList[@index].publicationFlg" value="${t.value}"
							<c:if test="${t.value == 1}">checked="checked"</c:if> />
							${f:h(t.label)}
							</label></li>
						</c:forEach>
						</ul>
					</td>
					<td width="260" class="posi_center">
						<input type="button" name="occuNew" value="募集要項を作成"
							class="occuAddBtn" id="occuBtn@employValue-@jobValue"
							data-fancybox="modal" data-src="#modal@employValue-@jobValue"
							data-wrap-id="@employValue-@jobValue">
						<input type="hidden" name="webJobDtoList[@index].editFlg" id="editFlg@employValue-@jobValue" value="0" />
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
						<th width="140">仕事内容</th>
						<th width="20"><span class="attention">必須</span></th>
						<td><textarea name="webJobDtoList[@index].workContents" cols="60" rows="10" data-name="workContents"></textarea></td>
					</tr>
					<tr>
						<th>給与</th>
						<th></th>
						<td class="release">
							<select name="webJobDtoList[@index].saleryStructureKbn" data-name="saleryStructureKbn" data-man-text="salary_man_text@index">
								<c:forEach items="${saleryStructureList}" var="t">
									<option value="${t.value}">${f:h(t.label)}</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							<input type="text" name="webJobDtoList[@index].lowerSalaryPrice" style="width: 80px" min="0" value=""
								id="lowerSalaryPrice@employValue-@jobValue"
								data-name="lowerSalaryPrice" />&nbsp;円
							　～　
							<input type="text" name="webJobDtoList[@index].upperSalaryPrice" style="width: 80px" min="0" value=""
							data-name="upperSalaryPrice" />&nbsp;円
							<br>
							<font color="red">■掲載日 11/30（木）以降の号数に掲載する求人は、価格に加えて「万」を語尾に追加してください。<br> 例）25万1000、25万など</font>
							<br><br>
							<textarea name="webJobDtoList[@index].salaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。&#10;ただし、「時給」・「日給」が選択されている場合には、入力は必要なく、記載しても内容が表示されません。" data-name="salaryDetail"></textarea><br><br>
							<textarea name="webJobDtoList[@index].salary" cols="60" rows="10" data-name="salary" placeholder="【備考欄】記載内容が反映されます。"></textarea>
						</td>
					</tr>
					<tr>
						<th>給与2<br>(初年度想定年収)</th>
						<th></th>
						<td class="release">
							年収&nbsp;&nbsp;
							<input type="text" name="webJobDtoList[@index].annualLowerSalaryPrice" style="width: 80px" min="0" value=""
								id="annualLowerSalaryPrice@employValue-@jobValue"
								data-name="annualLowerSalaryPrice" />&nbsp;円
							　～　
							<input type="text" name="webJobDtoList[@index].annualUpperSalaryPrice" style="width: 80px" min="0" value=""
								data-name="annualUpperSalaryPrice" />&nbsp;円
							<br><br>
							<textarea name="webJobDtoList[@index].annualSalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="annualSalaryDetail"></textarea><br><br>
							<textarea name="webJobDtoList[@index].annualSalary" cols="60" rows="10" data-name="annualSalary" placeholder="【備考欄】記載内容が反映されます。"></textarea>
						</td>
					</tr>
					<tr>
						<th>給与3<br>(初年度想定月収)</th>
						<th></th>
						<td class="release">
							月収&nbsp;&nbsp;
							<input type="text" name="webJobDtoList[@index].monthlyLowerSalaryPrice" style="width: 80px" min="0" value=""
								id="monthlyLowerSalaryPrice@employValue-@jobValue"
								data-name="monthlyLowerSalaryPrice" />&nbsp;円
							　～　
							<input type="text" name="webJobDtoList[@index].monthlyUpperSalaryPrice" style="width: 80px" min="0" value=""
								data-name="monthlyUpperSalaryPrice" />&nbsp;円
							<br><br>
							<textarea name="webJobDtoList[@index].monthlySalaryDetail" cols="60" rows="10" placeholder="【詳細欄】上の金額フォームを入力したら、入力必須項目になります。" data-name="monthlySalaryDetail"></textarea><br><br>
							<textarea name="webJobDtoList[@index].monthlySalary" cols="60" rows="10" data-name="monthlySalary" placeholder="【備考欄】記載内容が反映されます。"></textarea>
						</td>
					</tr>
					<tr>
						<th>求める人物像・資格</th>
						<th></th>
						<td class="release">
							<span class="attention">※${common['gc.webdata.personHunting.maxLength']}文字以内</span>　<span id="personHuntingSpan@employValue-@jobValue">残り${common['gc.webdata.personHunting.maxLength']}文字</span><br />
							<textarea name="webJobDtoList[@index].personHunting" cols="60" rows="10"
								class="text_count"
								id="webJobDtoList[@index].personHuntingTextId"
								data-wrap-id="@employValue-@jobValue"
								data-max-length="${common['gc.webdata.personHunting.maxLength']}"
								data-name="personHunting"></textarea>
						</td>
					</tr>
					<tr>
						<th>休日・休暇</th>
						<th></th>
						<td>
							<ul class="checklist_3col clear">
								<c:forEach items="${treatmentList}" var="t">
									<c:if test="${t.value eq TWO_DAYS_OFF}">
										<li><label><input type="checkbox" name="webJobDtoList[@index].treatmentKbnList"
											 value="${t.value}" data-name="treatmentKbnList">${f:h(t.label)}</label></li>
									</c:if>
								</c:forEach>
							</ul>
							<br />
							<textarea name="webJobDtoList[@index].holiday" cols="60" rows="10" class="" data-name="holiday"></textarea>
						</td>
					</tr>
					<tr>
						<th>勤務時間</th>
						<th></th>
						<td>
							<textarea name="webJobDtoList[@index].workingHours" cols="60" rows="10" class="" data-name="workingHours"></textarea>
						</td>
					</tr>
					<tr>
						<th>待遇</th>
						<th></th>
						<td>
							<ul class="checklist_3col clear">
							<c:forEach items="${treatmentList}" var="t">
								<c:if test="${t.value ne TWO_DAYS_OFF}">
									<li><label><input type="checkbox" name="webJobDtoList[@index].treatmentKbnList"
									value="${t.value}" data-name="treatmentKbnList">${f:h(t.label)}</label></li>
								</c:if>
							</c:forEach>
							</ul>
							<br />
							<textarea name="webJobDtoList[@index].treatment" cols="60" rows="10" data-name="treatment"></textarea>
						</td>
					</tr>
					<tr>
						<th>加入保険<br><span class="attention">※スタンバイ・求人ボックス用</span></th>
						<th></th>
						<td><input type="text" name="webJobDtoList[@index].insurance" class="txtInput" data-name="insurance"/><br>記入例)<br>社会保険完備、雇用・労災保険<br>社会保険完備(条件規定あり)、労災保険</td></td>
					</tr>
					<tr>
						<th>契約期間<br><span class="attention">※スタンバイ・求人ボックス用</span></th>
						<th></th>
						<td><input type="text" name="webJobDtoList[@index].contractPeriod" class="txtInput" data-name="contractPeriod" value="期間の定めなし"/><br>記入例)<br>入社より●●年の既定の定めあり。<br>期間の定めなし<br>内定時までに開示します</td>
					</tr>
					<tr style="display:none" class="jobShopList">
						<th>店舗</th>
						<th class="allCheckboxArea">
							<input type="checkbox" data-index="@index" class="jobShopListAllCheckbox">
						</th>
						<td>
							<ul class="checklist_3col clear jobShopCheckbox" data-index="@index">
								<li>
									<label>
										<input type="checkbox" name="webJobDtoList[@index].shopIdList" class="webJobShopList" data-name="webJobDtoList">
									</label>
								</li>
							</ul>
						</td>
					</tr>
					</table>
					<div class="wrap_btn">
						<input type="button" name="conf" class="fcclose" value="閉じる" onclick="parent.jQuery.fancybox.close();" data-employ-value="@employValue" data-job-value="@jobValue" >
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
	<div id="sizeHiddenDiv" style="display:none;"><c:if test="${not empty sizeKbn}">${f:h(sizeKbn)}</c:if></div>
</c:if>
</div>
<!-- #main# -->
<nav class="webdate_menu">
<ul>
	<li><a href="#customer">顧客選択</a></li>
	<li><a href="#shop">店舗選択</a></li>
	<li><a href="#base">基本設定</a></li>
	<li><a href="#search">検索条件</a></li>
	<li><a href="#job">募集職種</a></li>
	<li><a href="#applicant">応募方法</a></li>
	<li><a href="#setting">原稿設定</a></li>
	<li><a href="#pickup">ピックアップ</a></li>
	<li class="bt"><a href="javascript:void(0)" onclick="$('#prev_pc_btn').click();">PC プレ</a></li>
	<li class="bt"><a href="javascript:void(0)" onclick="$('#prev_sp_btn').click();">スマホ プレ</a></li>
	<li class="bt2"><a href="javascript:void(0)" onclick="$('#conf_btn').click();">確認画面</a></li>
</ul>
</nav>

<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>
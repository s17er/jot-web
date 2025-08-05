<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>

<jsp:include page="apC01_common_body_js.jsp"></jsp:include>

<script>
/*@cc_on _d=document;eval('var document=_d')@*/

	function setMailto() {
		location.href="mailto:?subject=${f:h(mailerSubject)}&body=${f:h(mailerBody)}";
	}

</script>

<c:if test="${pageKbn eq PAGE_EDIT}">
	<script type="text/javascript">
		$(function() {
			$("#shopListEditConfButton").on("click", function() {
				$("#indexWebDetailHIdden").val("店舗を見る");
				$("form#indexWebDetailForm").submit();
			});
		});
	</script>
</c:if>


<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
		<tr>
			<th width="40" class="posi_center">顧客ID</th>
			<th>顧客名</th>
			<th>担当者名</th>
			<th>電話番号</th>
			<th>メールアドレス</th>
			<c:choose>
				<c:when test="${pageKbn eq PAGE_DETAIL}">
					<th>担当会社名：営業担当者名</th>
					<th class="posi_center bdrs_right">詳細</th>
				</c:when>
				<c:otherwise>
					<th class="bdrs_right">担当会社名：営業担当者名</th>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="posi_center">${f:h(customerDto.id)}&nbsp;</td>
			<td>${f:h(customerDto.customerName)}&nbsp;</td>
			<td>${f:h(customerDto.contactName)}&nbsp;</td>
			<td>${f:h(customerDto.phoneNo)}&nbsp;</td>
			<td>
				${f:h(customerDto.mainMail)}
				<c:if test="${not empty customerDto.subMailList}"><br></c:if>
				<c:forEach items="${customerDto.subMailList}" var="subMail" varStatus="status">
					${subMail}<c:if test="${!status.last}"><br></c:if>
				</c:forEach>
			</td>
			<c:choose>
				<c:when test="${pageKbn eq PAGE_DETAIL}">
					<td>${f:br(customerDto.companySalesName)}&nbsp;</td>
					<td class="posi_center bdrs_right">
						<c:if test="${not empty customerDto.id}">
							<a href="${f:url(gf:makePathConcat2Arg(customerDetailPath, customerDto.id, id))}">詳細</a>
						</c:if>
					</td>
				</c:when>
				<c:otherwise>
					<td class="bdrs_right">${f:br(customerDto.companySalesName)}&nbsp;</td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>
	<hr />

	<h2 class="title shop" id="shop">対象店舗選択</h2>
	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
		<tr>
			<th width="400" class="posi_center">総店舗数</th>
			<th class="posi_center">選択店舗数</th>
			<th class="posi_center">インディード選択店舗数</th>
		</tr>
		<tr>
			<td class="posi_center"><span id="allShopCount">${allShopCount}</span>店舗</td>
			<td class="posi_center"><span id="selectShopCount">${fn:length(shopListDtoList)}</span>店舗</td>
			<td class="posi_center"><span id="selectIndeedShopCount">${fn:length(shopListIdListForIndeed)}</span>店舗</td>
		</tr>
	</table>
	<div class="wrap_btn">
		<input type="button" name="" value="店舗確認" id="shopSelectBtn"
		 <c:if test="${empty shopListDtoList}">
			disabled
		 </c:if>
		 <c:if test="${not empty shopListDtoList}">
		 	data-fancybox="modal" data-src="#wrap_shop_search"
		 </c:if> />
	</div>

	 <c:if test="${not empty shopListDtoList}">
		<div id="wrap_shop_search" style="display: none">
			<table class="table table-bordered cmn_table list_table shop_table">
			    <thead>
					<tr>
						<th>店舗名</th>
						<th>都道府県</th>
						<th>市区町村</th>
						<th width="140">業態1</th>
						<th width="140">業態2</th>
					</tr>
			    </thead>
			    <tbody>
			    	<c:forEach items="${shopListDtoList}" var="shopListDto">
						<tr>
							<td>${f:h(shopListDto.shopName)}</td>
							<td>${f:h(shopListDto.prefecturesName)}</td>
							<td>${f:h(shopListDto.cityName)}</td>
							<td width="140">${f:h(shopListDto.industryKbn1Label)}</td>
							<td width="140">${f:h(shopListDto.industryKbn2Label)}</td>
						</tr>
			    	</c:forEach>
			    </tbody>
			</table>
			<div class="wrap_btn">
				<input type="button" name="conf" value="閉じる" onclick="parent.jQuery.fancybox.close();">
			</div>
		</div>
	</c:if>
	<hr />

	<c:if test="${customerDtoExist}">
		<div class="wrap_btn">
			<c:choose>
				<c:when test="${pageKbn eq PAGE_EDIT}">
					<input type="button" name="" value="${f:h(LABEL_SHOPLIST)}"  id="shopListEditConfButton"
							onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:when>
				<c:when test="${pageKbn eq PAGE_INPUT}">
					<%-- 表示しない --%>
				</c:when>
				<c:otherwise>
					<html:button property="" value="${f:h(LABEL_SHOPLIST)}" onclick="location.href='${f:url(gf:makePathConcat2Arg('/shopList/index/indexWebDatail', customerDto.id, id))}';" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>

	<h2 class="title date">${f:h(pageTitle2)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}" enctype="multipart/form-data">

		<c:choose>
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<div class="wrap_detailBtn">
					<c:if test="${controlDto.editFlg}">
						<input type="button" name="" value="編 集" onclick="location.href='${f:url(editPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
					</c:if>
					<c:if test="${controlDto.fixedFlg}">
						<input type="button" value="掲載確定" onclick="changeStatusConf('掲載確定をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.FIXED_VALUE %>);"
								onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					<c:if test="${controlDto.cancelFlg}">
						<input type="button" name="" value="確定取消" onclick="changeStatusConf('確定取消をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.CANCEL_VALUE %>);"
								onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					<c:if test="${controlDto.postRequestFlg}">
						<input type="button" name="" value="掲載依頼" onclick="changeStatusConf('掲載依頼をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.POST_REQUEST_VALUE %>);"
								onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					<c:if test="${controlDto.postEndFlg}">
						<input type="button" name="" value="掲載終了" onclick="changeStatusConf('掲載終了をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.POST_END_VALUE %>);"
								onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
					</c:if>
					<c:if test="${controlDto.copyFlg}">
						<input type="button" name="" value="コピー" onclick="copyConf('processFlg', 'inputForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					<input type="button" name="" value="メール送信" onclick="setMailto()" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
 					<c:if test="${controlDto.fixedAndAppTest eq false and controlDto.appTestFlg}">
						<input type="button" name="" value="応募テスト" onclick="location.href='${f:url(appTestPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
					</c:if>
					<html:hidden property="id"/>
					<c:set var="PREVIEW" value="${listPreviewUrl}" scope="page" />
					<input type="button" value="PC確認" onclick="window.open('${f:h(PREVIEW)}', 'preview', 'width=1280,height=600,scrollbars=yes'); return false;"><!--
					--><input type="button" value="スマホ確認" onclick="window.open('${f:h(PREVIEW)}', 'preview', 'width=400,height=600,scrollbars=yes'); return false;"><!--
					--> <c:if test="${(controlDto.fixedAndNotApptest or controlDto.fixedAndAppTest or controlDto.notFixedAndAppTest) and controlDto.deleteFlg eq true}">
						<input type="button" value="削 除" onclick="deleteConf('delProcessFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					<c:if test="${controlDto.fixedAndAppTest and controlDto.appTestFlg}">
						<input type="button" name="" value="応募テスト" onclick="location.href='${f:url(appTestPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
					</c:if>

					<c:if test="${controlDto.hiddenFlg}">
						<input type="button" value="WEB非表示" onclick="changePublicationFlgConf('WEB非表示をしてもよろしいですか？', 'changeForm', <%=MTypeConstants.ChangeStatusKbn.WEB_HIDDEN_VALUE %>, <%=MTypeConstants.PublicationEndDisplayFlg.DISPLAY_NG %>);"
							onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>

					<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />

					<c:if test="${controlDto.notFixedAndNotAppTest and controlDto.deleteFlg and userDto.authLevel eq AUTH_LEVEL_ADMIN}">
						<input type="button" value="削 除" onclick="deleteConf('delProcessFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
				</div>
			</c:when>
		</c:choose>

			<html:hidden property="hiddenId" />

			<!-- ################# 基本設定 ################# -->
			<a name="base" id="base"></a>
			<h3 class="subtitle">基本設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="155">エリア</th>
					<td>
						<gt:areaList name="areaList" />
						${f:label(areaCd, areaList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>掲載期間</th>
					<td>
						<gt:volumeList name="volumeList" limitValue="${areaCd}" />
						${f:label(volumeId, volumeList, 'value', 'label')}&nbsp;
					</td>
				</tr>
				<tr>
					<th>連載</th>
					<td>
						<c:if test="${not empty serialPublication}">
							${f:br(f:h(serialPublication))}&nbsp;
						</c:if>
					</td>
				</tr>
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
						<tr>
							<th>原稿番号</th>
							<td>${f:h(id)}</td>
						</tr>
						<tr>
							<th>ステータス</th>
							<gt:statusList name="displayStatusList" statusKbn="<%=String.valueOf(MTypeConstants.StatusKbn.DIPLAY_STATUS_VALUE) %>" />
							<td>
								${f:label(displayStatus, displayStatusList, 'value', 'label')}
								<c:if test="${not empty checkedStatusName}">
									(${f:h(checkedStatusName)})
								</c:if>
							</td>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<th>過去原版</th>
					<td>${f:h(sourceWebId)}&nbsp;</td>
				</tr>
				<tr>
					<th><span class="fontBold">原稿名</span></th>
					<td>${f:h(manuscriptName)}&nbsp;</td>
				</tr>
				<tr>
					<th>特集</th>
					<td class="release">
						<gt:specialList name="specialList" limitValue="${areaCd}"/>
						<c:forEach items="${specialIdList}" var="t" varStatus="status">
							${f:label(t, specialList, 'value', 'label')}
							<c:if test="${!status.last}"><br /></c:if>
						</c:forEach>&nbsp;
					</td>
				</tr>
				<tr>
					<th>適職診断</th>
					<td>
						<gt:typeList name="reasonableKbnList" typeCd="<%=MTypeConstants.ReasonableKbn.TYPE_CD %>" />
						${f:label(reasonableKbn, reasonableKbnList, 'value', 'label')}&nbsp;
					</td>
				</tr>
				<tr>
					<th>インタビュー</th>
					<td>
						<gt:typeList name="mtBlogIdList" typeCd="<%=MTypeConstants.MtBlogId.TYPE_CD %>" />
						${f:label(mtBlogId, mtBlogIdList, 'value', 'label')}<br><br>
						${f:h(topInterviewUrl)}
					</td>
				</tr>
				<tr>
					<th>合同企業説明会</th>
					<gt:typeList name="briefingList" typeCd="<%=MTypeConstants.BriefingPresentKbn.TYPE_CD %>"/>
					<td>
						${f:label(briefingPresentKbn, briefingList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>掲載終了後の表示</th>
					<gt:typeList name="publicationEndDisplayList" typeCd="<%=MTypeConstants.PublicationEndDisplayFlg.TYPE_CD %>"/>
					<td>
						${f:label(publicationEndDisplayFlg, publicationEndDisplayList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>検索対象</th>
					<gt:typeList name="searchTargetList" typeCd="<%=MTypeConstants.SearchTargetFlg.TYPE_CD %>"/>
					<td>
						${f:label(searchTargetFlg, searchTargetList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>所属</th>
					<td>
						<gt:assignedCompanyList name="companyList"  companyId="${companyId}" />
						${f:label(companyId, companyList, 'value', 'label')}&nbsp;
					</td>
				</tr>
				<tr>
					<th>営業担当</th>
					<td>
						<gt:assignedSalesList name="salesList" limitValue="${companyId}" />
						${f:label(salesId, salesList, 'value', 'label')}&nbsp;
					</td>
				</tr>
				<tr>
					<th>求人識別番号</th>
					<td>${f:h(webNo)}&nbsp;</td>
				</tr>
			</table>

			<!-- ################# 勤務地情報の設定 ################# -->
			<a name="working" id="working"></a>
			<h3 class="subtitle">勤務地情報の設定</h3>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="155">勤務地エリア・最寄駅</th>
					<td>${f:br(f:h(workingPlace))}&nbsp;</td>
				</tr>
				<tr>
					<th width="155">勤務地詳細</th>
					<td>${f:br(f:h(workingPlaceDetail))}&nbsp;</td>
				</tr>
			</table>

			<!-- ################# 店舗情報の設定 ################# -->
			<a name="shopInfo" id="shopInfo"></a>
			<h3 class="subtitle">店舗情報の設定</h3>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="155">客席数</th>
					<td>${f:br(f:h(seating))}&nbsp;</td>
				</tr>
				<tr>
					<th width="155">客単価</th>
					<td>${f:br(f:h(unitPrice))}&nbsp;</td>
				</tr>
				<tr>
					<th width="155">営業時間</th>
					<td>${f:br(f:h(businessHours))}&nbsp;</td>
				</tr>
				<tr>
					<th width="155">ホームページ</th>
					<td>${f:br(f:h(homepage1))}&nbsp;</td>
				</tr>
			</table>

			<!-- ################# 検索条件の設定 ################# -->
			<a name="search" id="search"></a>
			<h3 class="subtitle">検索条件の設定</h3>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="155">採用基準</th>
					<td class="release">
						<ul class="shoplist clear">
							<gt:typeList name="companyCharacteristicList" typeCd="<%=MTypeConstants.CompanyCharacteristicKbn.TYPE_CD %>" />
							<c:set var="HIRING_REQUIREMENT_LIST" value="<%=MTypeConstants.CompanyCharacteristicKbn.HIRING_REQUIREMENT_LIST  %>" scope="page" />
							<c:forEach items="${companyCharacteristicKbnList}" var="t">
								<c:if test="${HIRING_REQUIREMENT_LIST.contains(Integer.parseInt(t))}">
								<li>${f:label(t, companyCharacteristicList, 'value', 'label')}</li>
								</c:if>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th width="155">企業の特徴</th>
					<td class="release">
						<ul class="shoplist clear">
							<gt:typeList name="companyCharacteristicList" typeCd="<%=MTypeConstants.CompanyCharacteristicKbn.TYPE_CD %>" />
							<c:set var="COMPANY_CHARACTERISTIC_LIST" value="<%=MTypeConstants.CompanyCharacteristicKbn.COMPANY_CHARACTERISTIC_LIST  %>" scope="page" />
							<c:forEach items="${companyCharacteristicKbnList}" var="t">
								<c:if test="${COMPANY_CHARACTERISTIC_LIST.contains(Integer.parseInt(t))}">
								<li>${f:label(t, companyCharacteristicList, 'value', 'label')}</li>
								</c:if>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>フリーワード</th>
					<td>
					${f:h(tagList)}
					</td>
				</tr>
			</table>

			<!-- ################# 募集職種設定 ################# -->
			<a name="job" id="job"></a>
			<h3 class="subtitle">募集職種設定</h3>
			<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
			<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
			<gt:typeList name="publicationList" typeCd="<%=MTypeConstants.PublicationFlg.TYPE_CD %>" />
			<gt:typeList name="saleryStructureList" typeCd="<%=MTypeConstants.SaleryStructureKbn.TYPE_CD %>" blankLineLabel="--" />
			<gt:typeList name="otherPersonHuntingConditionList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD %>"/>
			<gt:typeList name="treatmentList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />
			<c:set var="TWO_DAYS_OFF" value="<%=MTypeConstants.TreatmentKbn.TWO_DAYS_OFF%>"></c:set>
			<c:set var="SALERY_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>"></c:set>
			<c:set var="SALERY_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>"></c:set>

			<div class="ui-sortable">
				<c:forEach items="${webJobDtoList}" var="jobDto">
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
								<td width="160">
									<ul class="checklist_2col clear">
										<li>
											${f:label(jobDto.publicationFlg, publicationList, 'value', 'label')}
										</li>
									</ul>
								</td>
								<td width="260" class="posi_center">
									<input type="button" name="occuNew" value="募集要項を確認" class="occuAddBtn" data-fancybox="modal" data-src="#modal${wrapId}">
								</td>
							</tr>
							</tbody>
						</table>

						<div id="modal${wrapId}" style="display: none;">
							<h4 class="occupation clear">【${f:label(jobDto.employPtnKbn, employPtnList, 'value', 'label')}】${f:label(jobDto.jobKbn, jobList, 'value', 'label')}</h4>
							<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
							<tbody><tr>
								<th width="150">仕事内容</th>
								<td>${f:br(f:h(jobDto.workContents))}</td>
							</tr>
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
							<tr>
								<th>求める人物像・資格</th>
								<td class="release">
									<c:if test="${not empty jobDto.otherConditionKbnList}">
										<ul class="shoplist clear">
											<c:forEach items="${jobDto.otherConditionKbnList}" var="t">
												<li>${f:label(t, otherPersonHuntingConditionList, 'value', 'label')}</li>
											</c:forEach>
										</ul>
										<br>
									</c:if>
									${f:br(f:h(jobDto.personHunting))}
								</td>
							</tr>
							<tr>
								<th>休日・休暇</th>
								<td>
									<c:if test="${not empty jobDto.treatmentKbnList}">
										<c:forEach items="${jobDto.treatmentKbnList}" var="t">
											<c:if test="${t eq TWO_DAYS_OFF}">
												<ul class="shoplist clear">
													<li>${f:label(t, treatmentList, 'value', 'label')}</li>
												</ul>
												<br>
											</c:if>
										</c:forEach>
									</c:if>
									${f:br(f:h(jobDto.holiday))}
								</td>
							</tr>
							<tr>
								<th>勤務時間</th>
								<td>
									${f:br(f:h(jobDto.workingHours))}
								</td>
							</tr>
							<tr>
								<th>待遇</th>
								<td>
									<c:if test="${not empty jobDto.treatmentKbnList}">
										<ul class="shoplist clear">
											<c:forEach items="${jobDto.treatmentKbnList}" var="t">
												<c:if test="${value ne TWO_DAYS_OFF}">
													<li>${f:label(t, treatmentList, 'value', 'label')}</li>
												</c:if>
											</c:forEach>
										</ul>
										<br>
									</c:if>
									${f:br(f:h(jobDto.treatment))}
								</td>
							</tr>
							<tr>
								<th>加入保険</th>
								<td>
									${f:br(f:h(jobDto.insurance))}
								</td>
							</tr>
							<tr>
								<th>契約期間</th>
								<td>
									${f:br(f:h(jobDto.contractPeriod))}
								</td>
							</tr>
							<c:if test="${not empty jobDto.shopIdList}">
								<tr>
									<th>店舗</th>
									<td>
										<ul class="shoplist clear">
											<c:forEach items="${jobDto.shopIdList}" var="t">
												<li>${f:label(t, shopListDtoList, 'id', 'shopName')}</li>
											</c:forEach>
										</ul>
									</td>
								</tr>
							</c:if>
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

			<!-- ################# 連絡先・応募方法 ################# -->
			<a name="applicant" id="applicant"></a>
			<h3 class="subtitle">連絡先・応募方法</h3>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="155">電話番号/受付時間<br />
						<span class="attention">※電話番号は半角で入力</span>
					</th>
					<td>${f:br(f:h(phoneReceptionist))}&nbsp;</td>
				</tr>
				<c:if test="${pageKbn ne PAGE_INPUT and customerDto ne null and not empty customerDto.id and areaCd <= 2}">
					<tr>
						<th>IP電話</th>
						<td>
							<c:choose>
								<c:when test="${empty phoneNo1 and empty phoneNo2 and empty phoneNo3}">
									&nbsp;
								</c:when>
								<c:otherwise>
									<c:if test="${!empty phoneNo1}">
										<div class="IpPhone">1&nbsp;電話：&nbsp;${f:h(phoneNo1)}
										&nbsp;&nbsp;IP電話：&nbsp;<span id="spanIpPhoneNo1">${f:h(ipPhoneNo1)}</span></div>
									</c:if>
									<c:if test="${!empty phoneNo2}">
										<div class="IpPhone">2&nbsp;電話：&nbsp;${f:h(phoneNo2)}
										&nbsp;&nbsp;IP電話：&nbsp;<span id="spanIpPhoneNo2">${f:h(ipPhoneNo2)}</span></div>
									</c:if>
									<c:if test="${!empty phoneNo3}">
										<div class="IpPhone">3&nbsp;電話：&nbsp;${f:h(phoneNo3)}
										&nbsp;&nbsp;IP電話：&nbsp;<span id="spanIpPhoneNo3">${f:h(ipPhoneNo3)}</span></div>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<tr>
					<th>応募フォーム</th>
					<td class="release">
						<gt:typeList name="applicationFormKbnList" typeCd="<%=MTypeConstants.ApplicationFormKbn.TYPE_CD %>" />
						${f:label(applicationFormKbn, applicationFormKbnList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>質問・店舗見学フォーム</th>
					<td class="release">
						<gt:typeList name="observationKbnList" typeCd="<%=MTypeConstants.ObservationKbn.TYPE_CD %>" />
						${f:label(observationKbn, observationKbnList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>応募メール送信先</th>
					<td class="release">
					<c:set var="communicationMailKbnCustomer" value="<%=MTypeConstants.CommunicationMailKbn.CUSTOMER_MAIL %>" />
						<c:choose>
							<c:when test="${communicationMailKbn eq communicationMailKbnCustomer}">
								[顧客のメインアドレス]<br>${f:h(customerDto.mainMail)}
								<c:forEach items="${customerDto.subMailList}" var="subMail" varStatus="status">
									<c:if test="${status.first}"><br>[顧客のサブアドレス]<br></c:if>${subMail}<c:if test="${!status.last}"><br></c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								${f:h(mail)}&nbsp;
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>応募方法</th>
					<td>${f:br(f:h(applicationMethod))}&nbsp;</td>
				</tr>
				<tr>
					<th>面接地住所/交通</th>
					<td>${f:br(f:h(addressTraffic))}&nbsp;</td>
				</tr>
			</table>

			<!-- ################# 原稿設定 ################# -->
			<a name="setting" id="setting"></a>
			<h3 class="subtitle">原稿設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th width="155">サイズ</th>
					<td class="release">
						<gt:typeList name="sizeKbnList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" suffix="${common['gc.sizeKbn.suffix']}" />
						${f:label(sizeKbn, sizeKbnList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>ロゴ</th>
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="logo_img" value="<%= MTypeConstants.MaterialKbn.LOGO %>" />
							<c:set var="logo_thumb_img" value="<%= MTypeConstants.MaterialKbn.LOGO_THUMB %>" />
							<span>ロゴ&nbsp;：&nbsp;詳細ページ右上に表示。複数店舗の使用は不可。</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isLogoThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, logo_img))}" title="ロゴ" data-lightbox="photo">
											<img alt="ロゴ" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, logo_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, logo_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, logo_img, idForDirName))}" title="ロゴ" data-lightbox="photo">
											<img alt="ロゴ" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, logo_thumb_img, idForDirName))}" />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="main1_img" value="<%= MTypeConstants.MaterialKbn.MAIN_1 %>" />
							<c:set var="main1_thumb_img" value="<%= MTypeConstants.MaterialKbn.MAIN_1_THUMB %>" />
							<span>メイン1&nbsp;：&nbsp;文章1の左に表示する写真（330×250）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isMain1ThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, main1_img))}" title="メイン1" data-lightbox="photo">
											<img alt="メイン1" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, main1_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, main1_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, main1_img, idForDirName))}" title="メイン1" data-lightbox="photo">
											<img alt="メイン1" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, main1_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="main2_img" value="<%= MTypeConstants.MaterialKbn.MAIN_2 %>" />
							<c:set var="main2_thumb_img" value="<%= MTypeConstants.MaterialKbn.MAIN_2_THUMB %>" />
							<span>メイン2&nbsp;：&nbsp;本文1の左に表示する写真（330×250）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isMain2ThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, main2_img))}" title="メイン2" data-lightbox="photo">
											<img alt="メイン2" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, main2_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, main2_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, main2_img, idForDirName))}" title="メイン2" data-lightbox="photo">
											<img alt="メイン2" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, main2_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="main3_img" value="<%= MTypeConstants.MaterialKbn.MAIN_3 %>" />
							<c:set var="main3_thumb_img" value="<%= MTypeConstants.MaterialKbn.MAIN_3_THUMB %>" />
							<span>メイン3&nbsp;：&nbsp;本文1の左に表示する写真（330×250）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isMain3ThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, main3_img))}" title="メイン3" data-lightbox="photo">
											<img alt="メイン3" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, main3_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, main3_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, main3_img, idForDirName))}" title="メイン3" data-lightbox="photo">
											<img alt="メイン3" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, main3_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="right_img" value="<%=MTypeConstants.MaterialKbn.RIGHT %>" />
							<c:set var="right_thumb_img" value="<%=MTypeConstants.MaterialKbn.RIGHT_THUMB %>" />
							<span>右画像&nbsp;：&nbsp;詳細ページ右側に表示する写真（180×250）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isRightExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, right_img))}" title="右画像" data-lightbox="photo">
											<img alt="右画像" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, right_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, right_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, right_img, idForDirName))}" title="右画像" data-lightbox="photo">
											<img alt="右画像" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, right_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="photoA_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_A %>" />
							<c:set var="photoA_thumb_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_A_THUMB %>" />
							<span>キャプション1&nbsp;：&nbsp;詳細ページ中段。3点あるうちの左に表示する写真（235×170）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isPhotoAThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, photoA_img))}" title="キャプション1" data-lightbox="photo">
											<img alt="キャプション1" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, photoA_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, photoA_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, photoA_img, idForDirName))}" title="キャプション1" data-lightbox="photo">
											<img alt="キャプション1" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, photoA_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="photoB_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_B %>" />
							<c:set var="photoB_thumb_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_B_THUMB %>" />
							<span>キャプション2&nbsp;：&nbsp;詳細ページ中段。3点あるうちの中央に表示する写真（235×170）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isPhotoBThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, photoB_img))}" title="キャプション2" data-lightbox="photo">
											<img alt="キャプション2" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, photoB_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, photoB_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, photoB_img, idForDirName))}" title="キャプション2" data-lightbox="photo">
											<img alt="キャプション2" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, photoB_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="photoC_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_C %>" />
							<c:set var="photoC_thumb_img" value="<%= MTypeConstants.MaterialKbn.PHOTO_C_THUMB %>" />
							<span>キャプション3&nbsp;：&nbsp;詳細ページ中段。3点あるうちの右に表示する写真（235×170）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isPhotoCThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, photoC_img))}" title="キャプション3" data-lightbox="photo">
											<img alt="キャプション3" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, photoC_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, photoC_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, photoC_img, idForDirName))}" title="キャプション3" data-lightbox="photo">
											<img alt="キャプション3" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, photoC_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="attention_here_img" value="<%= MTypeConstants.MaterialKbn.ATTENTION_HERE %>" />
							<c:set var="attention_here_thumb_img" value="<%= MTypeConstants.MaterialKbn.ATTENTION_HERE_THUMB %>" />
							<span>ここに注目&nbsp;：&nbsp;詳細ページ下部に表示する写真（350×180）</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isAttentionHereExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, attention_here_img))}" title="ここに注目" data-lightbox="photo">
											<img alt="ここに注目" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, attention_here_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, attention_here_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, attention_here_img, idForDirName))}" title="ここに注目" data-lightbox="photo">
											<img alt="ここに注目" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, attention_here_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
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
					<td class="release photo">
						<div class="photoWrap">
							<c:set var="free_img" value="<%= MTypeConstants.MaterialKbn.FREE %>" />
							<c:set var="free_thumb_img" value="<%= MTypeConstants.MaterialKbn.FREE_THUMB %>" />
							<span>フリースペース&nbsp;：&nbsp;検索リストに表示する写真</span>
							<span class="attention">※公開時の詳細ページに表示（プレビュー画面にて要確認）<br />
								(D:370×250,&nbsp;E:880×400)
							</span>
							<c:choose>
								<c:when test="${pageKbn eq PAGE_DETAIL}">
									<c:if test="${materialExistsDto.isFreeThumbExistFlg}">
										<a href="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, free_img))}" title="フリースペース" data-lightbox="photo">
											<img alt="フリー" src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, free_thumb_img))}" />
										</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<% // 画像があれば、表示 %>
									<c:if test="${gf:isMapExsists(materialMap, free_thumb_img)}">
										<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, free_img, idForDirName))}" title="フリースペース" data-lightbox="photo">
											<img alt="フリー" src="${f:url(gf:makePathConcat2Arg(actionThumbMaterialPath, free_thumb_img, idForDirName))}" /><br /><br />
										</a>
									</c:if>
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr>
					<th>動画URL</th>
					<td>
						${f:h(movieUrl)}
					</td>
				</tr>
				<tr id="movieListDisplayFlg_tr">
					<th>検索一覧に動画を表示<br>
						<span class="attention">※以下のサイズのとき表示</span>
						<ul class="clear"><li>D</li><li>E</li></ul></th>
					<td>
						<gt:typeList name="movieListDisplayFlgList" typeCd="<%=MTypeConstants.MovieListDisplayFlg.TYPE_CD %>" />
							${f:label(movieListDisplayFlg, movieListDisplayFlgList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>原稿内で動画を表示</th>
					<td>
						<gt:typeList name="movieDetailDisplayFlgList" typeCd="<%=MTypeConstants.MovieDetailDisplayFlg.TYPE_CD %>" />
						${f:label(movieDetailDisplayFlg, movieDetailDisplayFlgList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>キャッチコピー</th>
					<td>
						<c:if test="${catchCopyExist}">
							${f:br(catchCopy)}&nbsp;
						</c:if>
					</td>
				</tr>
				<tr id="sentence1_tr">
					<th>本文1</th>
					<td>${f:br(sentence1)}&nbsp;</td>
				</tr>
				<tr id="sentence2_tr">
					<th>本文2<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<td>${f:br(sentence2)}&nbsp;</td>
				</tr>
				<tr id="sentence3_tr">
					<th>本文3<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>D</li><li>E</li></ul>
					</th>
					<td>${f:br(sentence3)}&nbsp;</td>
				</tr>
				<tr id="captionA_tr">
					<th>キャプション1.文<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>

					<td>${f:br(captionA)}&nbsp;</td>
				</tr>
				<tr id="captionB_tr">
					<th>キャプション2.文<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<td>${f:br(captionB)}&nbsp;</td>
				</tr>
				<tr id="captionC_tr">
					<th>キャプション3.文<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<td>${f:br(captionC)}&nbsp;</td>
				</tr>
				<tr id="attentionHereTitle">
					<th>タイトル/ここに注目<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>B</li><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<td>${f:h(attentionHereTitle)}</td>
				</tr>
				<tr id="attentionHereSentence_tr">
					<th class="bdrs_bottom">文章/ここに注目<br />
						<span class="attention">※以下のサイズのとき表示</span><br />
						<ul class="clear"><li>B</li><li>C</li><li>D</li><li>E</li></ul>
					</th>
					<td>${f:br(attentionHereSentence)}</td>
				</tr>
			</table>
			<hr />
			<br />

			<!-- ################# ピックアップ求人設定 ################# -->
			<a name="pickup" id="pickup"></a>
			<h3 class="subtitle">ピックアップ求人設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="155">ピックアップ求人</th>
					<td class="release">
						<gt:typeList name="attentionShopFlgList" typeCd="<%=MTypeConstants.AttentionShopFlg.TYPE_CD %>" />
						${f:label(attentionShopFlg, attentionShopFlgList, 'value', 'label')}
						<br>
						${f:h(attentionShopStartDate)}　～　${f:h(attentionShopEndDate)}
					</td>
				</tr>
				<tr>
					<th>検索上位表示</th>
					<td class="release">
						<gt:typeList name="searchPreferentialFlgList" typeCd="<%=MTypeConstants.SearchPreferentialFlg.TYPE_CD %>" />
						${f:label(searchPreferentialFlg, searchPreferentialFlgList, 'value', 'label')}
						<br>
						${f:h(searchPreferentialStartDate)}　～　${f:h(searchPreferentialEndDate)}
					</td>
				</tr>
				<tr>
					<th>ピックアップ求人名</th>
					<td class="release">
						${f:h(attentionShopName)}&nbsp;
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">ピックアップ求人文章</th>
					<td class="bdrs_bottom">
						${f:h(attentionShopSentence)}&nbsp;
					</td>
				</tr>
			</table>
			<hr />
			<br />


			<c:if test="${pageKbn eq PAGE_DETAIL}">
				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
					<tr>
						<th class="bdrs_right">グルメキャリーweb 原稿内容の確認用URL</th>
					</tr>
					<tr>
						<td class="bdrs_bottom">
							${f:br(previewMessage)}
						</td>
					</tr>
				</table>
				<hr />
				<br />
			</c:if>

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th class="bdrs_bottom" width="155">メモ</th>
					<td class="bdrs_bottom release">${f:br(f:h(memo))}&nbsp;</td>
				</tr>
			</table>
			<hr />
			<br />

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th class="bdrs_bottom" width="155">応募画面　QRコード</th>
					<td class="bdrs_bottom release"><c:if test="${!empty id}">
						<a href="${f:url(gf:makePathConcat3Arg('/qr/qr/application', '600', '600', id))}" data-lightbox="qr" title="${id}">
							<img alt="QRコード" src="${f:url(gf:makePathConcat3Arg('/qr/qr/application', '200', '200', id))}" />
						</a>
						</c:if>&nbsp;
					</td>
				</tr>
			</table>
			<hr />
			<br />

			<c:choose>
				<c:when test="${pageKbn eq PAGE_DETAIL}">
					<div class="wrap_detailBtn">
						<c:if test="${controlDto.editFlg}">
							<input type="button" name="" value="編 集" onclick="location.href='${f:url(editPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
						</c:if>
						<c:if test="${controlDto.fixedFlg}">
							<input type="button" value="掲載確定" onclick="changeStatusConf('掲載確定をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.FIXED_VALUE %>);"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<c:if test="${controlDto.cancelFlg}">
							<input type="button" name="" value="確定取消" onclick="changeStatusConf('確定取消をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.CANCEL_VALUE %>);"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<c:if test="${controlDto.postRequestFlg}">
							<input type="button" name="" value="掲載依頼" onclick="changeStatusConf('掲載依頼をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.POST_REQUEST_VALUE %>);"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<c:if test="${controlDto.postEndFlg}">
							<input type="button" name="" value="掲載終了" onclick="changeStatusConf('掲載終了をしてもよろしいですか？', 'editForm', <%=MTypeConstants.ChangeStatusKbn.POST_END_VALUE %>);"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
						</c:if>
						<c:if test="${controlDto.copyFlg}">
							<input type="button" name="" value="コピー" onclick="copyConf('processFlg', 'inputForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<input type="button" name="" value="メール送信" onclick="setMailto()" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<c:if test="${controlDto.fixedAndAppTest eq false and controlDto.appTestFlg}">
						<input type="button" name="" value="応募テスト" onclick="location.href='${f:url(appTestPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
					</c:if>
					<input type="button" value="PC確認" onclick="window.open('${f:h(PREVIEW)}', 'preview', 'width=1280,height=600,scrollbars=yes'); return false;"><!--
					--><input type="button" value="スマホ確認" onclick="window.open('${f:h(PREVIEW)}', 'preview', 'width=400,height=600,scrollbars=yes'); return false;"><!--
					--> <c:if test="${(controlDto.fixedAndNotApptest or controlDto.fixedAndAppTest or controlDto.notFixedAndAppTest) and controlDto.deleteFlg eq true}">
						<input type="button" value="削 除" onclick="deleteConf('delProcessFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					<c:if test="${controlDto.fixedAndAppTest and controlDto.appTestFlg}">
						<input type="button" name="" value="応募テスト" onclick="location.href='${f:url(appTestPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
					</c:if>

					<c:if test="${not empty lightPreviewUrl}">
						<input type="button" value="ライト版" onclick="window.open('${f:h(lightPreviewUrl)}', 'preview', 'width=400,height=600,scrollbars=yes'); return false;">
					</c:if>

					<c:if test="${controlDto.hiddenFlg}">
						<input type="button" value="WEB非表示" onclick="changePublicationFlgConf('WEB非表示をしてもよろしいですか？', 'changeForm', <%=MTypeConstants.ChangeStatusKbn.WEB_HIDDEN_VALUE %>, <%=MTypeConstants.PublicationEndDisplayFlg.DISPLAY_NG %>);"
							onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>

					<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					<c:if test="${controlDto.notFixedAndNotAppTest and controlDto.deleteFlg and userDto.authLevel eq AUTH_LEVEL_ADMIN}">
						<input type="button" value="削 除" onclick="deleteConf('delProcessFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					</div>
				</c:when>
				<c:otherwise>
					<div class="wrap_btn">
						<script type="text/javascript">
							var webDataSubmitBtnClickFlg = false;
							var onclickWebDataSbumitBtn = function() {
								if (webDataSubmitBtnClickFlg === false) {
									webDataSubmitBtnClickFlg = true;
									return true;
								}

								return false;
							}
						</script>
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" onclick="onclickWebDataSbumitBtn();" />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</div>
				</c:otherwise>
			</c:choose>
		</s:form>

		<c:if test="${pageKbn eq PAGE_EDIT}">
			<s:form action="${f:h(actionPath)}indexWebDetail" styleId="indexWebDetailForm">
			</s:form>
		</c:if>
		<c:if test="${pageKbn eq PAGE_DETAIL}">
			<% //コピーは画面表示データのみ行うことができる（アドレス直打ちの対応）  %>
			<s:form action="${f:h(copyActionPath)}" enctype="multipart/form-data" styleId="inputForm" >
				<html:hidden property="id" value="${f:h(id)}" />
				<html:hidden property="processFlg" styleId="processFlg" />
			</s:form>
			<% //削除の場合は、確認のためにフラグを立ててから画面遷移  %>
			<s:form action="${f:h(deleteActionPath)}" enctype="multipart/form-data" styleId="deleteForm" >
				<html:hidden property="id" value="${f:h(id)}" />
				<html:hidden property="processFlg" styleId="delProcessFlg" />
				<html:hidden property="version" value="${f:h(version)}" />
			</s:form>
			<% //ステータス変更の場合は、確認のためフラグを立ててから画面遷移  %>
			<s:form action="${f:h(editActionPath)}" enctype="multipart/form-data" styleId="editForm">
				<html:hidden property="id" value="${f:h(id)}" />
				<html:hidden property="displayStatus" value="${f:h(displayStatus)}" />
				<html:hidden property="processFlg" styleId="checkStatusFlg" />
				<html:hidden property="changeStatusKbn" styleId="changeStatusKbn" value="" />
				<html:hidden property="version" value="${f:h(version)}" />
			</s:form>
			<% //WEB非表示に変更の場合は、確認のためフラグを立ててから画面遷移  %>
			<s:form action="${f:h(editPubilicationActionPath)}" enctype="multipart/form-data" styleId="changeForm">
				<html:hidden property="id" value="${f:h(id)}" />
				<html:hidden property="displayStatus" value="${f:h(displayStatus)}" />
				<html:hidden property="processFlg" styleId="changeStatusFlg" />
				<html:hidden property="changeStatusKbn" styleId="changeDisplayKbn" value="" />
				<html:hidden property="publicationEndDisplayFlg" styleId="publicationEndDisplayFlg" />
				<html:hidden property="version" value="${f:h(version)}" />
			</s:form>
		</c:if>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>
<!-- #main# -->
<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>

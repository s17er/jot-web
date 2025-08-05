<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>

<gt:areaList name="areaList" />
<gt:assignedCompanyList name="assignedCompany" limitValue="${where_areaCd}" authLevel="${f:h(authLevel)}" blankLineLabel="${common['gc.pullDown']}" />
<gt:assignedCompanyList name="assignedCompanyList" limitValue="${where_areaCd}" authLevel="${f:h(authLevel)}" companyId="${assignedCompanyId}" blankLineLabel="${common['gc.pullDown']}" />
<gt:assignedSalesList name="assignedSales" limitValue="${assignedCompanyId}" blankLineLabel="${common['gc.pullDown']}" />
<gt:companyList name="companyList" />
<gt:salesList name="salesList" />
<gt:typeList name="industryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<gt:typeList name="shutokenForeignAreaKbnList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>" />
<gt:prefecturesList name="todoufukenList" noDisplayValue="<%= Arrays.asList(MPrefectures.KAIGAI) %>"/>

<c:set var="CUSTOMER_MAIL_MAG_FLG" value="<%=MTypeConstants.CustomerMailMagazineReceptionFlg.RECEIVE %>" scope="page" />
<link rel="stylesheet" href="${ADMIN_CONTENS}/css/web_data_accordion.css" type="text/css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript">
<!--


	$(function() {
		$("#outputCsvButton").on("click", function() {
			var form = $("#searchForm");
			var hidden = $("#propertyHidden");
			hidden.attr("name", "outputCsv");
			hidden.val("output");<%-- 値があればなんでもいい --%>
			form.submit();
		});


		$('#allcheck').click(function(){
		    if($(this).is(':checked')) { //全選択・全解除がcheckedだったら
		        $("input[name='customerIdList']").prop('checked', true); //アイテムを全部checkedにする
		    } else { //全選択・全解除がcheckedじゃなかったら
		    	$("input[name='customerIdList']").prop('checked', false); //アイテムを全部checkedはずす
		    }
		});
	})
	// optionの初期値
	var initOption = '<option value="">--</option>';

	//Ajaxの設定
	function areaLimitLoad () {
		window.focus();
		if ($("#areaId").val() == "") {
			// 会社の初期化
			$('#assignedCompanyId').children().remove();
			$('#assignedCompanyId').append(initOption);
			// 営業担当者の初期化
			$('#assignedSalesId').children().remove();
			$('#assignedSalesId').append(initOption);
			$('#pathId').css('display', 'none');
		} else {
			// 会社の連動
			$('#assignedCompanyAjax').load('${f:url(assignedCompanyAjaxPath)}',{'limitValue': $("#areaId").val()},
				// 営業担当者の初期化
				function(responseText, status, XMLHttpRequest) {
					$('#assignedSalesAjax').load('${f:url(assignedSalesAjaxPath)}',{'limitValue': ''});
				}
			);
			$('#pathId').css('display', 'inline');

		}
	}

	function assignedCompanyLimitLoad () {
		window.focus();
		if ($("#assignedCompanyId").val() == "") {
			// 営業担当者の初期化
			$('#assignedSalesId').children().remove();
			$('#assignedSalesId').append(initOption);
		} else {
			setAjaxParts('${f:url(assignedSalesAjaxPath)}', 'assignedSalesAjax', $("#assignedCompanyId").val());
		}
	}

	//ページ読み込み時にのみセットされる
	var changedFlg = false;

	/**
	 * 表示件数の変更後はフォーカスを外し、2度のサブミットをフラグで禁止する。
	 */
	function changeMaxRow() {

		if (!changedFlg) {

			changedFlg = true;

			window.focus();
			$("form#MaxRowSelect").submit();
		}
	}

	/** 指定したIDの操作ボタン押下時に対象のチェックボックスを全選択、全解除する */
	function handleAll(selfId, targetName){

		if ($("#" + selfId).attr('checked')) {
			$("input[name='"+targetName+"']").attr('checked', true);
		} else {
			$("input[name='"+targetName+"']").attr('checked', false);
		}
	}

// -->
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div id="wrap_search">
		<s:form action="${f:h(actionPath)}" styleId="searchForm">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">顧客検索</th>
				</tr>

				<tr>
					<th>顧客ID</th>
					<td><html:text property="where_customerId" /></td>
					<th>メールアドレス</th>
					<td><html:text property="where_mailAddress" /></td>
				</tr>

				<tr>
					<th>担当会社名</th>

					<td class="release ajaxWrap" >
					<div id="assignedCompanyAjax">
					<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALES}">
						<html:select property="assignedCompanyId" styleId="assignedCompanyId" onchange="assignedCompanyLimitLoad(); return false;">
							<html:optionsCollection name="assignedCompany"/>
						</html:select>
						&nbsp;
					</c:if>
					<c:if test="${userDto.authLevel eq AUTH_LEVEL_AGENCY or userDto.authLevel eq AUTH_LEVEL_OTHER}">
						${f:label(assignedCompanyId, companyList, 'value', 'label')}&nbsp;
					</c:if>
					</div>
					</td>

					<th>営業担当者名</th>
					<td class="release ajaxWrap" >
						<div id="assignedSalesAjax">
						<c:choose>
								<c:when test="${assignedCompanyId eq null || assignedCompanyId eq ''}" >
									<html:select property="assignedSalesId" styleId="assignedSalesId">
										<html:option value="">--</html:option>
									</html:select>
									<html:hidden property="assignedSalesId" value="" />
								</c:when>
								<c:otherwise>
									<html:select property="assignedSalesId" styleId="assignedSalesId">
										<html:optionsCollection name="assignedSales"/>
									</html:select>
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>

				<tr>
					<th>顧客名</th>
					<td><html:text property="where_customerName" /></td>
					<th>担当者名</th>
					<td><html:text property="where_contactName" /></td>
				</tr>
				<tr>
					<th>メールマガジンエリア</th>
					<td class="release" colspan="3">
					<div class="autoDispWrap3">
						<ul>
						<c:forEach items="${areaList}" var="t">
							<c:set var="areaCd" value="areaCd_${t.value}" />
							<li style="height:100%">
								<html:multibox property="where_mailMagazineAreaCdList" value="${f:h(t.value)}" styleId="${f:h(areaCd)}" />
								<label for="${f:h(areaCd)}">&nbsp;${f:h(t.label)}</label>
							</li>
						</c:forEach>
						</ul>
						</div>
					</td>
				</tr>
				<tr>
					<th style="width:90px !important;"><a name="detailAreaPosition" id="detailAreaPosition"></a>系列店舗項目</th>
					<td class="release" colspan="3">
                    	<div class="autoDispWrap1">
							<label class="area_acc_btn" for="Panel1">店舗数<span class="ac_caution">クリックすると店舗数検索が表示されます。</span></label>
							<input type="checkbox" id="Panel1" class="on-off on-box8" />
							<ul>
								<li><html:number property="where_lowerShopListCount" min="0" /> 件 ～ <html:number property="where_upperShopListCount"  min="0" /> 件</li>
							</ul>
						</div>
						<div class="autoDispWrap3">
							<label class="area_acc_btn" for="Panel2">エリア<span class="ac_caution">クリックするとエリアが表示されます。</span></label>
							<input type="checkbox" id="Panel2" class="on-off on-box10" />
							<ul>
								<c:forEach items="${todoufukenList}" var="t">
									<c:set var="todouhukenId" value="todouhukenId_${t.value}" />
									<li>
										<html:multibox property="where_shopListPrefecturesCdList" value="${f:h(t.value)}" styleId="${f:h(todouhukenId)}" />
										<label for="${f:h(todouhukenId)}">&nbsp;${f:h(t.label)}</label>
									</li>
								</c:forEach>
								<li></li>
								<c:forEach items="${shutokenForeignAreaKbnList}" var="t">
									<c:set var="shutokenForeignAreaKbn" value="shutokenForeignAreaKbn_${t.value}" />
									<li>
										<html:multibox property="where_shopListShutokenForeignAreaKbnList" value="${f:h(t.value)}" styleId="${f:h(shutokenForeignAreaKbn)}" />
										<label for="${f:h(shutokenForeignAreaKbn)}">&nbsp;${f:h(t.label)}</label>
									</li>
								</c:forEach>
							</ul>
						</div>
						<div class="autoDispWrap3">
							<label class="area_acc_btn" for="Panel3">業態<span class="ac_caution">クリックすると業態が表示されます。</span></label>
							<input type="checkbox" id="Panel3" class="on-off on-box9" />
							<ul>
								<c:forEach items="${industryKbnList}" var="t">
									<c:set var="industryKbn" value="industryKbn_${t.value}" />
									<li>
										<html:multibox property="where_shopListIndustryKbnList" value="${f:h(t.value)}" styleId="${f:h(industryKbn)}" />
										<label for="${f:h(industryKbn)}">&nbsp;${f:h(t.label)}</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">

				 <html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

			<input type="hidden" id="propertyHidden" />
		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

<c:if test="${existDataFlg}">

	<!-- #wrap_result# -->

		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>${pageNavi.allCount}件検索されました。</td>
				<td class="pull_down">
				<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
					<gt:maxRowList name="maxRowList" value="${common['gc.customer.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
						<html:select property="maxRow" onchange="changeMaxRow('selectForm');">
						<html:optionsCollection name="maxRowList" />
						<html:option value="">全件</html:option>
						</html:select>
				</s:form>
				</td>
			</tr>
		</table>
		<!-- #pullDown# -->
		<hr />

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/customer/list/changePage/${dto.pageNum}" />
				--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
			</c:if>
			<c:if test="${dto.linkFlg ne true}">
				--><span>${dto.label}</span><!--
			</c:if>
			</gt:PageNavi>
			--></td>

			</tr>
		</table>
		<!-- #page# -->
		<hr />
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
				<tr>
					<th width="20" class="posi_center" rowspan="2">選択<br />
					<input type="checkbox" id="allcheck" /></th>
					<th width="20" class="posi_center" rowspan="2">No.</th>
					<th width="40" class="posi_center">顧客ID</th>
					<th width="200">顧客名</th>
					<th width="100">担当者</th>
					<th>電話番号</th>
					<th>メールアドレス<br>サブメールアドレス[受信]</th>
					<th width="25" class="posi_center bdrs_right" rowspan="2">詳細</th>
				</tr>
				<tr>
					<th>メルマガ</th>
					<th colspan="2">店舗エリア</th>
					<th>店舗数</th>
					<th>担当会社：営業担当者</th>
				</tr>

				<c:forEach var="dto" items="${customerInfoDtoList}" varStatus="status">
					<% //テーブルの背景色を変更するためのCSSをセット %>
					<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
					<tr>
						<td class="posi_center ${classStr}" rowspan="2">
							<c:if test="${CUSTOMER_MAIL_MAG_FLG eq dto.mailMagazineReceptionFlg}">
								<html:multibox property="customerIdList" value="${dto.id}" />
							</c:if>
						</td>
						<td class="posi_center ${classStr}" rowspan="2"><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>
						<td class="posi_center ${classStr}">${f:h(dto.id)}</td>
						<td class="${classStr}">${f:h(dto.customerName)}</td>
						<td class="${classStr}">${f:h(dto.contactName)}</td>
						<td class="${classStr}">${f:h(dto.phoneNo)}</td>
						<td class="${classStr}">
							${f:h(dto.mainMail)}
							<c:forEach items="${dto.subMailList}" var="subMail" varStatus="status">
								<c:if test="${status.first}"><br></c:if>${f:h(subMail)}<c:if test="${!status.last}"><br></c:if>
							</c:forEach>
						</td>
						<td class="posi_center bdrs_right ${classStr}" rowspan="2"><a href="${f:url(dto.detailPath)}">詳細</a></td>
					</tr>
					<tr>
						<td class="${classStr}">
							<c:forEach items="${dto.mailMagazineAreaCdList}" var="t" varStatus="s">
								${f:label(t, areaList, 'value', 'label')}
								<c:if test="${!s.last}"><br></c:if>
							</c:forEach>
						</td>
						<td class="${classStr}" colspan="2">
							<c:forEach items="${dto.shopListPrefecturesCdList}" var="t" varStatus="s">
								${f:label(t, todoufukenList, 'value', 'label')}
								<c:if test="${!s.last}">,&nbsp;</c:if>
							</c:forEach>
							<if test="${not empty dto.shopListPrefecturesCdList && not empty dto.shopListShutokenForeignAreaKbnList}">
							<br>
							</if>
							<c:forEach items="${dto.shopListShutokenForeignAreaKbnList}" var="t" varStatus="s">
								${f:label(t, shutokenForeignAreaKbnList, 'value', 'label')}
								<c:if test="${!s.last}">,<br></c:if>
							</c:forEach>
						</td>
						<td class="${classStr}"><fmt:formatNumber value="${dto.shopCount}" pattern="###,###" />件</td>
						<td class="${classStr}">
							<c:forEach var="compSalesDto" items="${dto.companySalesDtoList}" varStatus="status">
								${f:label(compSalesDto.companyId, companyList, 'value', 'label')}：&nbsp;${f:label(compSalesDto.salesId, salesList, 'value', 'label')}
								<c:if test="${status.last ne true}">
									<br />
								</c:if>
							</c:forEach>
						</td>
					</tr>
				</c:forEach>
			</table>
			<hr />

			<!-- #page# -->
			<table cellpadding="0" cellspacing="0" border="0" class="page">
				<tr>
				<td><!--
				<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
				<c:if test="${dto.linkFlg eq true}">
					<%// vt:PageNaviのpathはc:setで生成する。 %>
					<c:set var="pageLinkPath" scope="page" value="/customer/list/changePage/${dto.pageNum}" />
					--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
				</c:if>
				<c:if test="${dto.linkFlg ne true}">
					--><span>${dto.label}</span><!--
				</c:if>
				</gt:PageNavi>
				--></td>

				</tr>
			</table>
			<!-- #page# -->
			<hr />

			<div class="wrap_btn">
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
					<html:submit property="mailMagazine" value="メルマガ作成" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					<html:button property="outputCsv" value="CSV出力" styleId="outputCsvButton"/>
				</c:if>
			</div>
		</s:form>
	</c:if>
	<!-- #wrap_result# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

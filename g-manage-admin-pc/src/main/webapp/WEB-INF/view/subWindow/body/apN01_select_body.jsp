<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<gt:assignedCompanyList name="assignedCompany"     limitValue="1" authLevel="${f:h(authLevel)}" blankLineLabel="${common['gc.pullDown']}" />
<gt:assignedCompanyList name="assignedCompanyList" limitValue="${where_areaCd}" authLevel="${f:h(authLevel)}" companyId="${assignedCompanyId}" blankLineLabel="${common['gc.pullDown']}" />
<gt:assignedSalesList name="assignedSales" limitValue="${assignedCompanyId}" blankLineLabel="${common['gc.pullDown']}" />
<gt:companyList name="companyList" blankLineLabel="${common['gc.pullDown']}" />
<gt:salesList name="salesList" />

<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript">


<!--

$(document).ready(function(){

	var userAuthLevel = "${f:h(userDto.authLevel)}";
	var agencyLevel = "${AUTH_LEVEL_AGENCY}";

	if(userAuthLevel !== agencyLevel) {
		areaLimitLoad();
	}


});

	window.name="selectSub";
	// optionの初期値
	var initOption = '<option value="">--</option>';

	//Ajaxの設定
	function areaLimitLoad () {


	}

	function assignedCompanyLimitLoad () {

		if ($("#assignedCompanyId").val() == "") {
			// 営業担当者の初期化
			$('#assignedSalesId').children().remove();
			$('#assignedSalesId').append(initOption);

		} else {
			setAjaxParts('${f:url(assignedSalesAjaxPath)}', 'assignedSalesAjax', $("#assignedCompanyId").val());
		}
	}

	function send_parent(num) {

		customerIdStr = "#customerId" + num;
		customerNameStr = "#customerName" + num;
		contactNameStr = "#contactName" + num;
		areaNameStr = "#areaName" + num;
		areaCdStr = "#areaCd" + num;
		phoneNoStr = "#phoneNo" + num;
		mainMailStr = "#mainMail" + num;
		subMailStr = "#subMail" + num;
		companySalesNameStr = "#companySalesName" + num;

		window.opener.callbackCustomer(($(customerIdStr).text())
				, ($(customerNameStr).text())
				, ($(contactNameStr).text())
				, ($(areaNameStr).text())
				, ($(areaCdStr).text())
				, ($(phoneNoStr).text())
				, ($(mainMailStr).text())
				, ($(subMailStr).html())
				, ($(companySalesNameStr).text()));

		window.close();
	}

	/**
	 * 表示件数の変更後はフォーカスを外し、2度のサブミットをフラグで禁止する。
	 */
	function changeMaxRow() {

		$("form#MaxRowSelect").submit();

	}


// -->
</script>
<!-- #main# -->
<div id="main">

	<h2 title="${f:h(pageTitle)}" class="title customer">${f:h(pageTitle)}</h2>
	<hr />

	<!-- #wrap_serch# -->
	<div id="wrap_search">

	<s:form action="${f:h(actionPath)}" target="selectSub">

	<html:errors />
	<table cellpadding="0" cellspacing="1" border="0" class="search_table">
		<tr>
			<th colspan="4" class="td_title">顧客検索</th>
		</tr>
		<tr>
			<th>顧客ID</th>
			<td colspan="3"><html:text property="where_customerId" /></td>
		</tr>
		<tr>
			<th>担当会社名</th>
			<td  class="release ajaxWrap" >
			<div id="assignedCompanyAjax">
				<!-- 代理店以外の場合 -->
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALES}">
					<html:select property="assignedCompanyId" styleId="assignedCompanyId" onchange="assignedCompanyLimitLoad(); return false;">
						<html:optionsCollection name="companyList"/>
					</html:select>
				</c:if>
				<!-- 代理店の場合 -->
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_AGENCY}">
					${f:label(assignedCompanyId, companyList, 'value', 'label')}&nbsp;
				</c:if>
				</div>
				</td>
			<th>営業担当者名</th>
			<td  class="release ajaxWrap" >
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
	</table>
	<hr />

	<div class="wrap_btn">
	<html:submit property="search" value="検 索"  onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
	</div>

	</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

	<!-- #wrap_result# -->
	<c:if test="${existDataFlg}">

	<table cellpadding="0" cellspacing="0" border="0" class="number_table">
		<tr>
			<td>${pageNavi.allCount}件検索されました。</td>
			<td class="pull_down">
				<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect" target="selectSub">
					<gt:maxRowList name="maxRowList" value="${common['gc.customer.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
						<html:select property="maxRow" onchange="changeMaxRow('selectForm');">
						<html:optionsCollection name="maxRowList" />
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
				<c:set var="pageLinkPath" scope="page" value="/customerSearch/select/changePage/${dto.pageNum}" />
				--><span><a href="${f:url(pageLinkPath)}"  target="selectSub">${dto.label}</a></span><!--
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

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
		<tr>
			<th width="20" class="posi_center">No.</th>
			<th width="40" class="posi_center">顧客ID</th>
			<th>顧客名</th>
			<th>担当者名</th>
			<th>電話番号</th>
			<th>メールアドレス</th>
			<th>担当会社：営業担当者</th>
			<th width="40" class="posi_center bdrs_right">選択</th>
		</tr>
		<c:forEach var="dto" varStatus="status" items="${customerInfoList}">
			<tr>
				<td class="posi_center">${status.index + 1}</td>
				<td class="posi_center"  id="customerId${status.index + 1}">${f:h(dto.id)}</td>
				<td id="customerName${status.index + 1}">${f:h(dto.customerName)}</td>
				<td id="contactName${status.index + 1}">${f:h(dto.contactName)}</td>
				<td id="phoneNo${status.index + 1}">${f:h(dto.phoneNo)}</td>
				<td>
					<span id="mainMail${status.index + 1}">${f:h(dto.mainMail)}</span>
					<c:if test="${not empty dto.subMailList}"><br></c:if>
					<span id="subMail${status.index + 1}">
						<c:forEach items="${dto.subMailList}" var="subMail" varStatus="mailStatus">
							${f:h(subMail)}<c:if test="${!mailStatus.last}"><br></c:if>
						</c:forEach>
					</span>
				</td>
				<td id="companySalesName${status.index + 1}">${f:br(dto.companySalesName)}</td>
				<html:hidden property="area" styleId="areaCd${status.index + 1}" value="${f:h(dto.areaCd)}"/>
				<td class="posi_center bdrs_right"><input value="選択" type="button" onclick="send_parent(${status.index + 1})" /></td>
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
				<c:set var="pageLinkPath" scope="page" value="/customerSearch/select/changePage/${dto.pageNum}" />
				--><span><a href="${f:url(pageLinkPath)}" target="selectSub">${dto.label}</a></span><!--
			</c:if>
			<c:if test="${dto.linkFlg ne true}">
				--><span>${dto.label}</span><!--
			</c:if>
			</gt:PageNavi>
			--></td>

			</tr>
		</table>
		<!-- #page# -->

	</c:if>
	<!-- #wrap_result# -->
	<hr />

</div>
<!-- #main# -->
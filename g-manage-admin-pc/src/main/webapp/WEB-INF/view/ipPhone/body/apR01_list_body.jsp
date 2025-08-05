<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList" />
<gt:prefecturesList name="prefecturesList" blankLineLabel="--" />
<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="juskillList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>"  blankLineLabel="--"  />


<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--

var tempCompanyId = "${f:h(where_companyId)}";

	// 「DatePicker」の搭載
	$(function(){
		$("#searchEndDate").datepicker();
		$("#searchStartDate").datepicker();
	});


/**
 * 所属会社選択時の連動を行う
 */
function assignedCompanyLimitLoad () {

    window.focus();

    var value = $("#companyId").val();

    // 同じ場合は処理しない
    if (tempCompanyId == value) {
        return;
    }

    fetchSales(value);
    tempCompanyId = value;
}

var fetchSales = function(companyId) {
    $.ajax({
            "url" : "${f:url('/ajax/json/salesList')}",
            "type" : "POST",
            "data" : "companyId=" + companyId + "&blankLineLabel=--",
            "success" : function(result) {
                $("#salesId > option").remove();
                for (var i in result) {
                    var val = result[i];
                    $('#salesId').append($('<option>').html(val.label).val(val.value));
                }

            }
        }

    )
};

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

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

				<html:messages id="msg" message="true">
				<div class="message">
					<ul>
						<li>
			  				<bean:write name="msg" ignore="true"   />
			  			</li>
					</ul>
				</div>
				</html:messages>


	<!-- #wrap_serch# -->
	<div id="wrap_search">
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">検索</th>
				</tr>
				<tr>
					<th>顧客ID</th>
    				<td class="release"><html:text property="customerId" size="30" styleClass="disabled" /></td>
					<th>顧客名</th>
					<td class="release"><html:text property="customerName" size="30" /></td>
				</tr>
				<tr>
					<th >IP電話番号(顧客電話)</th>
					<td  class="release">
						<html:text property="ipPhoneTel" size="30" />
					</td>
					<th>応募者電話番号</th>
					<td class="release">
						<html:text property="memberTel" size="30" />
					</td>
				</tr>
				<tr>
					<th>原稿番号</th>
					<td class="release">
						<html:text property="webId" size="30" />
					</td>
					<th>原稿名</th>
					<td class="release">
						<html:text property="manuscriptName" size="30" />
					</td>
				</tr>
				<tr>
					<th>応募日時</th>
					<td colspan="3" class="release">
						<html:text property="ipPhoneStartDate" size="10" styleId="searchStartDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="ipPhoneStartHour" size="2" styleClass="disabled" maxlength="2" />：
						<html:text property="ipPhoneStartMinute" size="2" styleClass="disabled" maxlength="2" />&nbsp;～&nbsp;
						<html:text property="ipPhoneEndDate" size="10" styleId="searchEndDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="ipPhoneEndHour" size="2" styleClass="disabled" maxlength="2" />：
						<html:text property="ipPhoneEndMinute" size="2" styleClass="disabled" maxlength="2" />
					</td>
				</tr>
				<tr>
					<th>担当会社名</th>
					<td class="release">
						<div id="companyAjax">
							<c:choose>
								<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
									<gt:assignedCompanyList name="where_companyList" blankLineLabel="${common['gc.pullDown']}" />
								</c:when>
								<c:otherwise>
									<gt:assignedCompanyList name="where_companyList" authLevel="${f:h(userDto.authLevel)}" companyId="${where_companyId}" />
								</c:otherwise>
							</c:choose>
							<html:select property="where_companyId" styleId="companyId" onchange="assignedCompanyLimitLoad();">
								<html:optionsCollection name="where_companyList" />
							</html:select>&nbsp;
						</div>
					</td>
					<th>営業担当者名</th>
					<td class="release">
						<gt:assignedSalesList name="where_salesList" limitValue="${where_companyId}" blankLineLabel="${common['gc.pullDown']}" />
						<div id="salesAjax">
							<html:select property="where_salesId" styleId="salesId">
								<html:optionsCollection name="where_salesList"/>
							</html:select>&nbsp;
						</div>
					</td>
				</tr>
			</table>
			<hr />
			<div class="wrap_btn">
				<html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>
		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

<c:set var="existDataFlg" value="${fn:length(dtoList) gt 0}" scope="page" />
<c:if test="${existDataFlg}">
	<!-- #wrap_result# -->

		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>${pageNavi.allCount}件検索されました。</td>
				<td class="pull_down">
					<s:form action="${actionMaxRowPath}" styleId="selectForm" >
						<gt:maxRowList name="maxRowList" value="${common['gc.member.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
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
				<c:set var="pageLinkPath" scope="page" value="${actionChangePagePath}${dto.pageNum}" />
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

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
			<tr>
				<th rowspan="3" width="15" class="posi_center">No.</th>
				<th width="15" class="posi_center bdrd_bottom">顧客ID.</th>
				<th width="170" class="posi_center bdrd_bottom">原稿名</th>
				<th width="100" class="posi_center bdrd_bottom">応募者番号</th>
				<th width="80" class="posi_center bdrs_right bdrd_bottom">応募日時</th>
			</tr>
			<tr>
				<th class="posi_center bdrd_bottom">原稿番号</th>
				<th class="posi_center bdrd_bottom">顧客名</th>
				<th class="posi_center bdrd_bottom">IP 番号</th>
				<th class="posi_center bdrd_bottom bdrs_right">通話時間</th>
			</tr>
			<tr>
				<th><%--空欄--%></th>
				<th colspan="2" class="posi_center">通話切断理由</th>
				<th class="posi_center bdrs_right">通話ステータス</th>
			</tr>

			<c:forEach var="dto" items="${dtoList}" varStatus="status">
			<% //テーブルの背景色を変更するためのCSSをセット %>
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
			<tr>
				<%-- No. --%>
				<td rowspan="3" class="posi_center ${classStr}">${f:h(status.count)}</td>
				<%-- 顧客ID 、現行番号--%>
				<td class="bdrd_bottom posi_center ${classStr}" ><fmt:formatNumber value="${dto.customerId}" pattern="##0000" />&nbsp;</td>
				<%-- 原稿名 --%>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.manuscriptName)}&nbsp;</td>
				<%-- 応募者番号 --%>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.memberTel)}&nbsp;</td>
				<%-- 応募日時 --%>
				<td class="bdrd_bottom bdrs_right ${classStr}"><fmt:formatDate value="${dto.callNoteCaughtMemberTelDatetime}" pattern="yyyy/MM/dd HH:mm"/> &nbsp;</td>
			</tr>
			<tr>
				<%-- 原稿番号 --%>
				<td class="bdrd_bottom posi_center ${classStr}">${f:h(dto.webId)}</td>
				<%-- 顧客名 --%>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.customerName)}&nbsp;</td>
				<%-- IP 番号 --%>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.ipPhoneTel)}&nbsp;</td>
				<%-- 通話時間 --%>
				<td class="posi_center bdrd_bottom bdrs_right ${classStr}">${f:h(dto.telTime)}</td>
			</tr>
			<tr>
				<%-- 空欄 --%>
				<td class=" ${classStr}"></td>
				<%-- 通話切断理由 --%>
				<td colspan="2" class="${classStr}">${f:h(dto.telStatusDetail)}</td>
				<%-- 通話ステータス --%>
				<td class="bdrs_right posi_center ${classStr}">${f:h(dto.telStatusName)}</td>
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
				<c:set var="pageLinkPath" scope="page" value="${actionChangePagePath}${dto.pageNum}" />
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


	<!-- #wrap_result# -->
	<hr />
	<br />
	<div class="wrap_btn">
		<input type="button" name="" value="CSV出力" onclick="if(!confirm('CSVを出力しますか?')) {return false;}; location.href='${f:url(actionCsvPath)}'" />
	</div>
</c:if>

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

<%@page pageEncoding="UTF-8"%>
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<c:set var="SELECTION_TARGET" value="<%=MTypeConstants.SelectionFlg.SELECTION_TARGET %>" />
<c:set var="SELECTION_OUT_TARGET" value="<%=MTypeConstants.SelectionFlg.OUT_TARGET %>" />

<c:set var="SEISYAIN"  value="<%=MTypeConstants.EmployPtnKbn.SEISYAIN %>" />
<c:set var="KEIYAKU_SYAIN"  value="<%=MTypeConstants.EmployPtnKbn.KEIYAKU_SYAIN %>" />
<c:set var="ARUBAITO_PART"  value="<%=MTypeConstants.EmployPtnKbn.ARUBAITO_PART %>" />
<c:set var="HAKEN"  value="<%=MTypeConstants.EmployPtnKbn.HAKEN %>" />


<script type="text/javascript">
//<![CDATA[

// ]]>
</script>


<!-- #main# -->
<div id="main">


<h2 title="${f:h(pageTitle)}" class="title"  id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>

<hr />

<p>${f:h(defaultMsg0)}<br />${f:h(defaultMsg1)}<br />${f:h(defaultMsg2)}</p>

<hr />

<tiles:insert template="/WEB-INF/view/application/spc01_change_tab.jsp" flush="false"/>
<hr />

<html:errors />


<c:if test="${existDataFlg}">

<table cellpadding="0" cellspacing="0" border="0" id="select_table">
	<tr>
		<td style="text-align:left;">
			<select id="selectPage" onChange="location.href=this.options[this.selectedIndex].value">
				<option value="${f:url('/observateApplication/list/')}"  selected>求職者ごとに並べる</option>
				<option value="${f:url('/observateApplicationMail/list/')}">日付順に並べる</option>
			</select>
		</td>
	</tr>

</table>

<!-- #page# -->
<table cellpadding="0" cellspacing="0" border="0" class="page">
	<tr>
		<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
				<c:choose>
					<c:when test="${dto.linkFlg eq true}">
						--><span><a href="${f:url(gf:concat2Str(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
					</c:when>
					<c:otherwise>
						--><span>${dto.label}</span><!--
					</c:otherwise>
				</c:choose>
			</gt:PageNavi>
		--></td>
	</tr>
</table>
<!-- #page# -->
<hr />

<!-- #listTable# -->

<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">

	<tr>
		<th width="10" class="posi_center">No</th>
		<th width="35" class="posi_center">メール</th>
		<th width="90" class="">質問者</th>
		<th width="100" class="">掲載店名</th>
		<th width="120" class="">メモ</th>
		<th width="75" class="bdrs_right">質問日時</th>
	</tr>

	<c:forEach var="dto" items="${dataList}" varStatus="status">

		<c:set var="classStr" value="${gf:odd(status.index, 'alternate', '')}" scope="page" />

		<s:form action="${f:h(actionPath)}">
			<tr id="trId${dto.id}_01">
				<%-- No --%>
				<td class="posi_center ${classStr}">${status.index + 1}</td>
				<%-- メール --%>
				<td class="posi_center ${classStr}">
					<c:choose>
						<c:when test="${dto.unopenedMailFlg eq true}">
							<img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" />
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<%-- 質問者 --%>
				<td class=" ${classStr}">
					<img src="${SHOP_CONTENS}${(dto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(dto.sexKbn, sexList, 'value', 'label')}" />
						&nbsp;<a href="${f:url(gf:concat2Str('/observateApplication/detailMailList/index/', dto.id))}">${f:h(dto.name)}</a>(${f:h(dto.age)})
				</td>
				<%-- 掲載店名 --%>
				<td class=" ${classStr}">
					${f:h(dto.applicationName)}
				</td>
				<%-- メモ --%>
				<td class=" ${classStr}">
					<html:text property="memo" value="${f:h(dto.memo)}" size="20" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
					<html:hidden property="id" value="${dto.id}" />
					<html:hidden property="version" value="${dto.version}" />
					<html:hidden property="pageNum" value="${pageNum}" />
				</td>
				<%-- 質問日時 --%>
				<td class="bdrs_right ${classStr}">
					<fmt:formatDate value="${dto.applicationDatetime}" pattern="yyyy/MM/dd HH:mm" />
				</td>
			</tr>
		</s:form>

	</c:forEach>

</table>

<!-- #listTable# -->
<hr />


<div class="wrap_btn">
	<html:button property="CSV" value="　" styleId="btn_csv" onclick="location.href='${f:url('/observateApplication/list/outputCsv')}'" />
</div>


<!-- #page# -->
<table cellpadding="0" cellspacing="0" border="0" class="page">
	<tr>
		<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
				<c:choose>
					<c:when test="${dto.linkFlg eq true}">
						--><span><a href="${f:url(gf:concat2Str(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
					</c:when>
					<c:otherwise>
						--><span>${dto.label}</span><!--
					</c:otherwise>
				</c:choose>
			</gt:PageNavi>
		--></td>
	</tr>
</table>
<!-- #page# -->


</c:if>


<hr />

<p class="go_top"><a href="#all">▲このページのトップへ</a></p>


</div>
<!-- #main# -->
<hr />

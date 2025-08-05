<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember.ListForm"%>
<c:set var="SELECTION_TARGET" value="<%=MTypeConstants.SelectionFlg.SELECTION_TARGET %>" />
<c:set var="SELECTION_OUT_TARGET" value="<%=MTypeConstants.SelectionFlg.OUT_TARGET %>" />

<c:set var="SEISYAIN"  value="<%=MTypeConstants.EmployPtnKbn.SEISYAIN %>" />
<c:set var="KEIYAKU_SYAIN"  value="<%=MTypeConstants.EmployPtnKbn.KEIYAKU_SYAIN %>" />
<c:set var="ARUBAITO_PART"  value="<%=MTypeConstants.EmployPtnKbn.ARUBAITO_PART %>" />
<c:set var="HAKEN"  value="<%=MTypeConstants.EmployPtnKbn.HAKEN %>" />
<c:set var="OTHER"  value="<%=MTypeConstants.EmployPtnKbn.OTHER %>" />

<c:set var="SEND_KBN_SEND" value="<%=MTypeConstants.SendKbn.SEND %>" scope="page"/>
<c:set var="SEND_KBN_RECEIVE" value="<%=MTypeConstants.SendKbn.RECEIVE %>" scope="page"/>
<c:set var="FROM_MENU_KBN_MAIL" value="<%=ListForm.FromMenuKbn.MAIL_BOX %>" scope="page" />

<script type="text/javascript">
//<![CDATA[

	$(function() {
		$("#checkAll").on("click", function() {
			$(".lumpSendIdArray").attr('checked', this.checked);
		});
	});

    <%-- 退避保持用Id --%>
    var varApplicationId = "";

	<%-- メモを追加します。 --%>
	var addMemo = function(mailLogId) {

		var memo = $("#scoutMailMemo_" + mailLogId).val();
		var data = {
				'memo':memo,
				'mailLogId':mailLogId,
				'pageNum' : '${f:h(pageNum)}'
				};

		$.ajax({
			type: "POST",
			url: "${f:url('/scoutMember/list/addMemo')}",
			data : data,
			success:function() {
				location.href="${f:url(gf:makePathConcat1Arg('/scoutMember/list/changePage', pageNum))}";
			}
		});
	};


	var onchangeSelectionKbn = function(mailLogId) {
		var selectionElm = $("#selectionKbn_" + mailLogId).val();

		var data = {
				"selectionFlg" : selectionElm,
				'mailLogId':mailLogId,
				'pageNum' : '${f:h(pageNum)}'
		};

		$.ajax({
			type: "POST",
			url: "${f:url('/scoutMember/list/changeSelectionFlg')}",
			data : data,
			success:function() {
				location.href="${f:url(gf:makePathConcat1Arg('/scoutMember/list/changePage', pageNum))}";
			}
		});

	};

// ]]>
</script>
<style>
	<%-- グレイアウト用スタイル --%>
	td.grayout {background-color: #CCC !important;}
</style>


<!-- #main# -->
<div id="main">


<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>

<hr />

<p>${f:h(defaultMsg0)}<br />${f:h(defaultMsg1)}<br />${f:h(defaultMsg2)}</p>

<hr />
<hr />


<html:errors />


<c:if test="${existDataFlg}">

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
<s:form action="${f:h(actionPath)}">

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">

		<tr>
			<th width="10" class="posi_center" rowspan="2">No</th>
			<th width="70"  class="posi_center" rowspan="2">会員ID (年齢)</th>
			<th width="200" class="posi_center" rowspan="2">住所</th>
			<th width="35" class="posi_center" rowspan="2">雇用<br />形態</th>
			<th width="10" class="bdrd_bottom">希望業態</th>
			<th width="90" class="posi_center" rowspan="2">気になる登録日</th>
			<th width="30" class="posi_center bdrs_right" rowspan="2">詳細</th>
		</tr>
		<tr>
			<th >希望業種</th>
		</tr>

		<%-- スカウト会員一覧 --%>
		<c:forEach var="dto" items="${targetList}" varStatus="status">

		<c:set var="classString" value="${gf:odd(status.index, 'alternate', '')}" scope="page" />
		<c:choose>
			<c:when test="${dto.selectionFlg eq SELECTION_OUT_TARGET}">
				<c:set var="classStr" value="${classString} grayout" scope="page" />
			</c:when>
			<c:otherwise>
				<c:set var="classStr" value="${classString}" scope="page" />
			</c:otherwise>
		</c:choose>

		<tr>
			<%-- No --%>
			<td width="10" class="posi_center ${f:h(classStr)}" rowspan="2">${status.index + 1}</td>

			<%-- 会員ID --%>
			<td class="posi_center ${f:h(classStr)}" rowspan="2">
				<img src="${SHOP_CONTENS}${(dto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(dto.sexKbn, sexList, 'value', 'label')}" />
				&nbsp;
				${f:h(dto.memberId)}
				&nbsp;(${f:h(dto.age)})
			</td>

			<%-- 住所 --%>
			<td class="posi_center  ${f:h(classStr)}" rowspan="2">
				<gt:prefecturesList name="prefecturesList"  />
				${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}
				${f:h(gf:toMunicipality(dto.municipality))}
				&nbsp;
			</td>

			<%-- 雇用形態 --%>
			<td width="35" class="posi_center ${f:h(classStr)}" rowspan="2">
				<c:forEach items="${dto.employPtnKbnList}" var="employPtnKbn">
					<c:choose>
						<c:when test="${employPtnKbn eq '1'}"><p class="employ_icon regular">正社員</p></c:when>
						<c:when test="${employPtnKbn eq '2'}"><p class="employ_icon contract">契約社員</p></c:when>
						<c:when test="${employPtnKbn eq '3'}"><p class="employ_icon parttimer">アルバイト・パート</p></c:when>
						<c:when test="${employPtnKbn eq '4' or employPtnKbn eq '5'}"><p class="employ_icon dispatch">派遣</p><p class="employ_icon introduce">紹介</p></c:when>
						<c:when test="${employPtnKbn eq '6'}"><p class="employ_icon entrust">委託店長</p></c:when>
						<c:when test="${employPtnKbn eq '7'}"><p class="employ_icon manage">経営者</p></c:when>
						<c:otherwise>&nbsp;</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>

			<%-- 希望業態 --%>
			<td width="130" class="bdrd_bottom  ${f:h(classStr)}"><gt:convertToJobName items="${dto.jobKbnList}"/></td>
			<%-- スカウト日 --%>
			<td width="90" class="posi_center  ${f:h(classStr)}" rowspan="2"><fmt:formatDate value="${dto.sendDatetime }" pattern="<%=GourmetCareeConstants.DATE_FORMAT_SLASH %>"/> </td>

			<%-- 詳細 --%>
			<td class="posi_center bdrs_right  ${f:h(classStr)}" rowspan="2">
				<c:choose>
					<c:when test="${fromMenu eq  FROM_MENU_KBN_MAIL}">
						<a href="${f:url(gf:concat2Str('/member/detail/indexFromMailBoxScoutMail/', dto.memberId))}">詳細</a>
					</c:when>
					<c:otherwise>
						<a href="${f:url(gf:concat2Str('/member/detail/indexFromScoutMail/', dto.memberId))}">詳細</a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="${f:h(classStr)}"><gt:convertToIndustryName items="${dto.industryKbnList}"/></td>
		</tr>

		</c:forEach>
	</table>
<!-- #listTable# -->
</s:form>
<hr />

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

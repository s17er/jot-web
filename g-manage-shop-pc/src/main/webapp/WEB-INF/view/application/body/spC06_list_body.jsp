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
<c:choose>
	<c:when test="${fromMenu eq  FROM_MENU_KBN_MAIL}">
		<tiles:insert template="/WEB-INF/view/application/spc01_change_tab.jsp" flush="false"/>
	</c:when>
	<c:otherwise>
		<div id="wrap_tab" class="clear">
			<ul id="mn_tab" class="clear">
				<li id="mn_noMemberList"><a href="${f:url(memberListPath)}" title="求職者一覧に切替">求職者一覧に切替</a></li>
				<li id="mn_scoutMail"><a href="${f:url('/scoutMember/list/')}" title="スカウトメール・気になる通知に切替" class="scout_a">スカウトメール・気になる通知に切替</a></li>
				<li id="mn_noKeepBox"><a href="${f:url(keepBoxPath)}" title="キープBOXに切替">キープBOXに切替</a></li>
			</ul>
		</div>
	</c:otherwise>
</c:choose>
<hr />


<html:errors />


<c:if test="${existDataFlg}">

<table cellpadding="0" cellspacing="0" border="0" id="select_table">
	<tr>
		<td style="text-align:left;">
			<select id="selectPage" onChange="location.href=this.options[this.selectedIndex].value">
				<c:choose>
					<c:when test="${fromMenu eq  FROM_MENU_KBN_MAIL}">
						<option value="${f:url('/scoutMember/list/mailBox/')}"  selected="selected" >求職者ごとに並べる</option>
						<option value="${f:url('/scoutMail/list/mailBox/')}">日付順に並べる</option>
					</c:when>
					<c:otherwise>
						<option value="${f:url('/scoutMember/list/')}" selected="selected" >求職者ごとに並べる</option>
						<option value="${f:url('/scoutMail/list/')}" >日付順に並べる</option>
					</c:otherwise>
				</c:choose>
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
<s:form action="${f:h(actionPath)}">

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">

		<tr>
			<th width="10" class="posi_center bdrd_bottom">No</th>
			<th width="70" colspan="2" class="bdrd_bottom">会員ID</th>
			<th width="200" class="bdrd_bottom">住所</th>
			<th width="35" class="posi_center " rowspan="2">雇用<br />形態</th>
			<th width="10" class="bdrd_bottom">希望業態</th>
			<th width="90" class="bdrd_bottom">スカウト日</th>
			<th width="30" class="posi_center bdrs_right" rowspan="2">詳細</th>
		</tr>
		<tr>
			<th><input type="checkbox" id="checkAll"></th>
			<th width="35" class="posi_center">メール</th>
			<th width="35" class="posi_center">状態</th>
			<th>メモ</th>
			<th >希望業種</th>
			<th >ステータス</th>
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
			<td width="10" class="bdrd_bottom posi_center ${f:h(classStr)}" >${status.index + 1}</td>

			<%-- 会員ID --%>
			<td colspan="2" class="bdrd_bottom ${f:h(classStr)}">
				<img src="${SHOP_CONTENS}${(dto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(dto.sexKbn, sexList, 'value', 'label')}" />
				&nbsp;
				<c:choose>
					<c:when test="${ fromMenu eq FROM_MENU_KBN_MAIL}">
						<a href="${f:url(gf:concat2Str('/scoutMember/detailMailList/indexFromMail/', dto.id))}">${f:h(dto.memberId)}</a>
					</c:when>
					<c:otherwise>
						<a href="${f:url(gf:concat2Str('/scoutMember/detailMailList/index/', dto.id))}">${f:h(dto.memberId)}</a>
					</c:otherwise>
				</c:choose>
				&nbsp;(${f:h(dto.age)})
			</td>

			<%-- 住所 --%>
			<td class="bdrd_bottom  ${f:h(classStr)}">
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
			<td width="90" class="bdrd_bottom  ${f:h(classStr)}"><fmt:formatDate value="${dto.sendDatetime }" pattern="<%=GourmetCareeConstants.DATE_FORMAT_SLASH %>"/> </td>

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
			<td width="10" class="posi_center ${f:h(classStr)}" ><html:multibox property="lumpSendIdArray" value="${dto.id}" styleClass="lumpSendIdArray" /> </td>
			<%-- メール --%>
			<td class="posi_center ${classStr}">

				<c:choose>
				<c:when test="${dto.unopenedMailFlg eq true}">
					<a href="${f:url('/scoutMail/list/')}"><img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" /></a>
				</c:when>
				<c:otherwise>
					&nbsp;
				</c:otherwise>
				</c:choose>

			</td>

			<%-- 状態 --%>
			<td width="50" class="posi_center  ${f:h(classStr)}">
				<c:if test="${dto.memberStatusDto.scoutFlg eq 1}">
					<c:choose>
						<c:when test="${dto.memberStatusDto.scoutReceiveKbn eq 2}">
							<img src="${SHOP_CONTENS}/images/scout/icon_receive.gif" alt="スカウト受け取る" width="20" height="20" />&nbsp;
						</c:when>
						<c:when test="${dto.memberStatusDto.scoutReceiveKbn eq 3}">
							<img src="${SHOP_CONTENS}/images/scout/icon_turndown.gif" alt="スカウト断る" width="20" height="20" />&nbsp;
						</c:when>
						<c:otherwise>
							<img src="${SHOP_CONTENS}/images/scout/icon_scout.gif" alt="スカウト済" width="20" height="20" />&nbsp;
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${dto.memberStatusDto.footprintFlg eq 1}"><img src="${SHOP_CONTENS}/images/scout/icon_interest.gif" alt="気になる" width="20" height="20" />&nbsp;<br></c:if>
				<c:if test="${dto.memberStatusDto.applicationFlg eq 1}"><img src="${SHOP_CONTENS}/images/scout/icon_entry.gif" alt="応募あり" width="20" height="20" />&nbsp;<br></c:if>
				&nbsp;
			</td>
			<td class="${f:h(classStr)}">
				<%-- 要メモフォームの追加
				<html:text property="memo" value="${f:h(dto.memo)}" size="20" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
				<html:hidden property="id" value="${dto.id}" />
				<html:hidden property="version" value="${dto.version}" />
				 --%>
				 <input type="text" value="${f:h(dto.memo)}" id="scoutMailMemo_${f:h(dto.id)}" size="17" />&nbsp;<input type="button" class="btn_entry" size="20" onclick="addMemo(${f:h(dto.id)});"/>
			</td>
			<%-- メモ --%>
			<td class="${f:h(classStr)}"><gt:convertToIndustryName items="${dto.industryKbnList}"/></td>
			<%-- ステータス --%>
			<td class="${f:h(classStr)}">
				<c:set var="selectionFlgMap" value="<%=MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP %>" scope="page" />
				<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>"  />
				<select name="selectionKbn" id="selectionKbn_${dto.id}" style="background-color: ${dto.selectionFlgColor}" onchange="onchangeSelectionKbn('${dto.id}');">
					<c:forEach items="${selectionList}" var="i">
						<c:set var="selected" value="${(i.value eq dto.selectionFlg) ? 'selected' : ''}" />
						<option value="${f:h(i.value)}" style="background-color: ${f:h(selectionFlgMap[i.value])}"  ${f:h(selected)}>${f:h(i.label)}</option>
					</c:forEach>
				</select>
			</td>
		</tr>

		</c:forEach>
	</table>

<div class="wrap_btn" style="height:35px;">
	<c:choose>
		<c:when test="${ fromMenu eq FROM_MENU_KBN_MAIL}">
			<html:submit property="lumpSendFromMail" value="　"  styleId="btn_send"  style="float:left;" />
		</c:when>
		<c:otherwise>
			<html:submit property="lumpSend" value="　"  styleId="btn_send"  style="float:left;" />
		</c:otherwise>
	</c:choose>
	<html:submit property="outputCsv" value="　" styleId="btn_csv"   style="float:right;"/>
</div>
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

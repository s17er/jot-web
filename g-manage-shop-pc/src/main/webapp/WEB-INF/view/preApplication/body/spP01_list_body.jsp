<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<c:set var="SELECTION_TARGET" value="<%=MTypeConstants.SelectionFlg.SELECTION_TARGET %>" />
<c:set var="SELECTION_OUT_TARGET" value="<%=MTypeConstants.SelectionFlg.OUT_TARGET %>" />

<c:set var="SEISYAIN"  value="<%=MTypeConstants.EmployPtnKbn.SEISYAIN %>" />
<c:set var="KEIYAKU_SYAIN"  value="<%=MTypeConstants.EmployPtnKbn.KEIYAKU_SYAIN %>" />
<c:set var="ARUBAITO_PART"  value="<%=MTypeConstants.EmployPtnKbn.ARUBAITO_PART %>" />
<c:set var="HAKEN"  value="<%=MTypeConstants.EmployPtnKbn.HAKEN %>" />
<c:set var="OTHER"  value="<%=MTypeConstants.EmployPtnKbn.OTHER %>" />
<gt:typeList name="jobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>

<script type="text/javascript">
//<![CDATA[

    <%-- 退避保持用Id --%>
    var varApplicationId = "";


	/** 指定したIDの操作ボタン押下時に対象のチェックボックスを全選択、全解除する */
	function handleAll(){

		if ($("#allcheck").attr('checked')) {

			$(".lumpSendCheck").attr('checked', true);
		} else {
			$(".lumpSendCheck").attr('checked', false);
		}
	}

	/**
	 * ステータスの色を変更し、DBを更新する
	 */
	 var onchangeSelectionKbn = function(selectId, id) {

		var kbn = $("#" + selectId);
		var search = $("input[name='search']");

		var selectionJson = {
				id : id,
				selectionKbn : kbn.val()
		};

		$.ajax({
			url: '${f:url("/preApplication/list/ajaxSelectionFlg/")}',
			data: selectionJson,
			type: 'post',
			success: function(json) {
				var jsonData = JSON.parse(json);

				$.each(jsonData, function() {
					// DEBUG
					// console.log(jsonData[kbn.val()]);
					$(kbn.css("background-color", jsonData[kbn.val()]));
					location.href="${f:url(gf:makePathConcat1Arg('/preApplication/list/changePage', pageNum))}";
				});

			}
		});

	};

	/**
	 * チェックボックスの内容をカンマ区切りにする.
	 */
	var jointLumpSendCheckbox = function() {

		var checkIdArray = [];

		$('[class="lumpSendCheck"]:checked').each(function(){
			checkIdArray.push($(this).val());
		});

		$('#lumpCheckId').val(checkIdArray.join(","));
	};

// ]]>



</script>
<style>
	<%-- グレイアウト用スタイル --%>
	td.grayout {background-color: #CCC !important;}
</style>

<!-- #main# -->
<div id="main">


<h2 title="${f:h(pageTitle)}" class="title"  id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>

<hr />

<p>${f:h(defaultMsg0)}<br />${f:h(defaultMsg1)}<br />${f:h(defaultMsg2)}<br />${f:h(defaultMsg3)}</p>

<hr />

<tiles:insert template="/WEB-INF/view/application/spc01_change_tab.jsp" flush="false"/>
<hr />

<html:errors />


<c:if test="${existDataFlg}">

<table cellpadding="0" cellspacing="0" border="0" id="select_table">
	<tr>
		<td style="text-align:left;">
			<select onChange="location.href=this.options[this.selectedIndex].value">
				<option value="${f:url('/preApplication/list/')}" selected>応募者ごとに並べる</option>
				<option value="${f:url('/preApplicationMail/list/')}" >日付順に並べる</option>
			</select>
		</td>
	</tr>

</table>

<!-- #page# -->
<table cellpadding="0" cellspacing="0" border="0" class="page">
	<tr>
		<td style="text-align:left;">
			<s:form action="${f:h(actionPath)}">
				フリーワード検索&nbsp;&nbsp;<html:text property="search" value="${f:h(search)}" size="40" />&nbsp;<html:submit property="doSearch" value="&nbsp;" styleClass="btn_search" />
			</s:form>
		</td>
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

<c:forEach var="dto" items="${dataList}" varStatus="status">

<c:set var="classString" value="${gf:odd(status.index, 'alternate', '')}" scope="page" />
<c:set var="classStr" value="${classString}" scope="page" />


<s:form action="${f:h(actionPath)}">
	<tr id="trId${dto.id}_01">
		<%-- No --%>
		<td class="posi_center bdrd_bottom ${classStr}" >${status.index + 1}</td>

		<%-- 会員ID --%>
		<td colspan="2" class="bdrd_bottom ${classStr}">
			<img src="${SHOP_CONTENS}${(dto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(dto.sexKbn, sexList, 'value', 'label')}" />
			&nbsp;<a href="${f:url(gf:concat2Str('/preApplication/detailMailList/index/', dto.id))}">${f:h(dto.memberId)}</a>(${f:h(dto.age)})
		</td>

		<%-- 住所 --%>
		<td class="bdrd_bottom ${classStr}">
			<gt:prefecturesList name="prefecturesList"  />
			${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}
			${f:h(dto.municipality)}
			${f:h(dto.address)}&nbsp;
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
		<td width="90" class="bdrd_bottom  ${f:h(classStr)}"><fmt:formatDate value="${dto.applicationDatetime }" pattern="<%=GourmetCareeConstants.DATE_FORMAT_SLASH %>"/> </td>
			<%-- 詳細 --%>
			<td class="posi_center bdrs_right  ${f:h(classStr)}" rowspan="2">
				<a href="${f:url(gf:concat2Str('/member/detail/indexFromMailBoxPreApplicationMail/', dto.memberId))}">詳細</a>
			</td>
		</tr>
		<tr>
			<td class="posi_center ${classStr}">
				<c:choose>
					<c:when test="${dto.containsCheckedFlg}">
						<input type="checkbox" class="lumpSendCheck" value="${dto.id}"  checked="checked"/>
					</c:when>
					<c:otherwise>
						<input type="checkbox" class="lumpSendCheck" value="${dto.id}" />
					</c:otherwise>
				</c:choose>
			</td>

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
				 <html:hidden property="id" value="${dto.id}" />
				 <html:hidden property="version" value="${dto.version}" />
				 <html:text property="memo" value="${f:h(dto.memo)}" size="17" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
			</td>
			<td class="${f:h(classStr)}"><gt:convertToIndustryName items="${dto.industryKbnList}"/></td>
			<%-- ステータス --%>
			<td class="${f:h(classStr)}">
				<c:set var="selectionFlgMap" value="<%=MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP %>" scope="page" />
				<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>"  />
				<select name="selectionKbn" id="selectionKbn_${dto.id}" style="background-color: ${dto.selectionFlgColor}" onchange="onchangeSelectionKbn('selectionKbn_${dto.id}', '${dto.id}');">
					<c:forEach items="${selectionList}" var="i">
						<c:set var="selected" value="${(i.value eq dto.selectionFlg) ? 'selected' : ''}" />
						<option value="${f:h(i.value)}" style="background-color: ${f:h(selectionFlgMap[i.value])}"  ${f:h(selected)}>${f:h(i.label)}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
</s:form>
</c:forEach>

</table>



<!-- #listTable# -->
<hr />

<%--
<!-- 注釈文字 -->
<div class="wrap_text">
	<font size="2">${f:h(defaultMsg4)}<br />${f:h(defaultMsg5)}</font>
</div>
<!-- /注釈文字 -->
--%>

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

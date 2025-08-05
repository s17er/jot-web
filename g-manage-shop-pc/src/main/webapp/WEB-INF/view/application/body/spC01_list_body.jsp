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
				selectionKbn : kbn.val(),
				search : search.val()
		};

		$.ajax({
			url: '${f:url("/application/list/ajaxSelectionFlg/")}',
			data: selectionJson,
			type: 'post',
			success: function(json) {
				var jsonData = JSON.parse(json);

				$.each(jsonData, function() {
					// DEBUG
					// console.log(jsonData[kbn.val()]);
					$(kbn.css("background-color", jsonData[kbn.val()]));
					location.href="${f:url(gf:makePathConcat1Arg('/application/list/changePage', pageNum))}";
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
				<option value="${f:url('/application/list/')}" selected>応募者ごとに並べる</option>
				<option value="${f:url('/applicationMail/list/')}" >日付順に並べる</option>
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
		<th colspan="2" class="bdrd_bottom">応募者</th>
		<th class="bdrd_bottom">住所</th>
		<th width="190" class="bdrd_bottom">応募職種</th>
		<th width="90" class="bdrd_bottom">応募日時</th>
		<th width="35" class="posi_center bdrs_right" rowspan="2">詳細</th>
	</tr>
	<tr>
		<th class="posi_center">
			<input type="checkbox" id="allcheck" onclick="handleAll()"/>
		</th>
		<th width="35" class="posi_center">メール</th>
		<th width="50" class="posi_center">原稿番号</th>
		<th colspan="1">掲載店名</th>
		<th>メモ</th>
		<th colspan="1">ステータス</th>
	</tr>

<c:forEach var="dto" items="${dataList}" varStatus="status">

<c:set var="classString" value="${gf:odd(status.index, 'alternate', '')}" scope="page" />
<c:choose>
	<c:when test="${dto.selectionFlg eq SELECTION_OUT_TARGET}">
		<c:set var="classStr" value="${classString} grayout" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="classStr" value="${classString}" scope="page" />
	</c:otherwise>
</c:choose>

<s:form action="${f:h(actionPath)}">
	<tr id="trId${dto.id}_01">
		<td class="posi_center bdrd_bottom ${classStr}" >${status.index + 1}</td>
		<td colspan="2" class="bdrd_bottom ${classStr}">

			<img src="${SHOP_CONTENS}${(dto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(dto.sexKbn, sexList, 'value', 'label')}" />
			&nbsp;<a href="${f:url(gf:concat2Str('/application/detailMailList/index/', dto.id))}">${f:h(dto.name)}</a>(${f:h(dto.age)})

		</td>
		<td class="bdrd_bottom ${classStr}">
			<gt:prefecturesList name="prefecturesList"  />
			${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}
			${f:h(dto.municipality)}
			${f:h(dto.address)}&nbsp;
		</td>
		<td class="bdrd_bottom ${classStr}">
			<c:if test="${not empty dto.jobKbn}">
				${f:label(dto.jobKbn, jobKbnList, 'value', 'label')}
			</c:if>
			<c:if test="${not empty dto.hopeJob}">
				${f:h(dto.hopeJob)}
			</c:if>
		</td>
		<td class="bdrd_bottom ${classStr}">
			<fmt:formatDate value="${dto.applicationDatetime}" pattern="yyyy/MM/dd HH:mm" />
		</td>
		<td class="posi_center bdrs_right ${classStr}" rowspan="2"><a href="${f:url(gf:concat2Str('/application/detail/index/', dto.id))}">詳細</a></td>
	</tr>
	<tr id="trId${dto.id}_02">
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
		<td class="posi_center ${classStr}">

			<c:choose>
			<c:when test="${dto.unopenedMailFlg eq true}">
				<a href="${f:url('/applicationMail/list/')}"><img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" /></a>
			</c:when>
			<c:otherwise>
				&nbsp;
			</c:otherwise>
			</c:choose>

		</td>
		<td class="posi_center ${classStr}">
			<%-- GCWが空の場合はWEBからの応募 --%>
			${(empty dto.gcwCd) ? dto.webId : '雑誌'}
		</td>
		<td class=" ${classStr}"  colspan="1">${f:h(dto.applicationName)}</td>
		<td class=" ${classStr}">
			<html:text property="memo" value="${f:h(dto.memo)}" size="17" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
			<html:hidden property="id" value="${dto.id}" />
			<html:hidden property="version" value="${dto.version}" />
		</td>
		<td class=" ${classStr}">

			<c:set var="selectionFlgMap" value="<%=MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP %>" scope="page" />
			<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>" />
			<select name="selectionKbn" id="selectionKbn_${status.index}" style="background-color: ${dto.selectionFlgColor}" onchange="onchangeSelectionKbn('selectionKbn_${status.index}', '${dto.id}');">
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

<s:form action="${f:h(actionPath)}">
	<div class="wrap_btn" style="height:35px;">
		<html:submit property="lumpSend" value="　"  styleId="btn_send" onclick="jointLumpSendCheckbox();"  style="float:left;" />
		<html:submit property="outputCsv" styleId="btn_csv" style="float:right;" value="　" />
		<html:hidden property="lumpSendIds" styleId="lumpCheckId"/>
	</div>
</s:form>



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

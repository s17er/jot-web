<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember.DetailMailListForm" %>

<c:set var="SELECTION_TARGET" value="<%=MTypeConstants.SelectionFlg.SELECTION_TARGET %>" />
<c:set var="SELECTION_OUT_TARGET" value="<%=MTypeConstants.SelectionFlg.OUT_TARGET %>" />

<c:set var="SEISYAIN"  value="<%=MTypeConstants.EmployPtnKbn.SEISYAIN %>" />
<c:set var="KEIYAKU_SYAIN"  value="<%=MTypeConstants.EmployPtnKbn.KEIYAKU_SYAIN %>" />
<c:set var="ARUBAITO_PART"  value="<%=MTypeConstants.EmployPtnKbn.ARUBAITO_PART %>" />
<c:set var="HAKEN"  value="<%=MTypeConstants.EmployPtnKbn.HAKEN %>" />
<c:set var="OTHER"  value="<%=MTypeConstants.EmployPtnKbn.OTHER %>" />

<c:set scope="page" var="RECEIVE" value="<%=MTypeConstants.SendKbn.RECEIVE %>" />
<c:set scope="page" var="SEND" value="<%=MTypeConstants.SendKbn.SEND %>" />

<c:set scope="page" var="UNOPENED" value="<%=MTypeConstants.MailStatus.UNOPENED%>" />
<c:set scope="page" var="OPENED" value="<%=MTypeConstants.MailStatus.OPENED%>" />
<c:set scope="page" var="REPLIED" value="<%=MTypeConstants.MailStatus.REPLIED%>" />

<c:set scope="page" var="FROM_MENU_MAIL" value="<%=DetailMailListForm.FromMenuKbn.MAIL_BOX%>" />

<script type="text/javascript" src="${SHOP_CONTENS}/js/setAjax.js"></script>

<script type="text/javascript">
//<![CDATA[

    <%-- 退避保持用Id --%>
    var varApplicationId = "";

	/**
	 * 会員詳細を別ウィンドウで開く
	 */
	var windowArray = new Object();
	function detailWindow(url, id) {
		windowArray[id] = window.open(url, id,'width=800,height=700,scrollbars=yes');
	}

	/**
	 * 画面のunload時に開いている会員詳細ウィンドウを閉じるイベント待ちfunction
	 */
	$(function(){
		$(window).unload(function(ev){
			for(var key in  windowArray){
				windowArray[key].close();
			}
		});
	});

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
			url: "${f:url('/scoutMember/detailMailList/addMemo')}",
			data : data,
			success:function() {
				location.href="${f:url(gf:makePathConcat2Arg('/scoutMember/detailMailList/changePage', mailLogId, pageNum))}";
			}
		});
	};

	/**
	 * ステータスの色を変更し、DBを更新する
	 */

	var onchangeSelectionKbn = function(selectId, mailLogId) {
		var selectionElm = $("#" + selectId + "_" + mailLogId);

		var data = {
				"selectionFlg" : selectionElm.val(),
				'mailLogId':mailLogId,
				'pageNum' : '${f:h(pageNum)}'
		};

		$.ajax({
			type: "POST",
			url: "${f:url('/scoutMember/detailMailList/changeSelectionFlg')}",
			data : data,
			success:function() {
				location.href="${f:url(gf:makePathConcat2Arg('/scoutMember/detailMailList/changePage', mailLogId, pageNum))}";
			}
		});

	};


// ]]>
</script>

<!-- #main# -->
<div id="main">


<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>

<hr />

<p>${f:h(defaultMsg0)}<br />${f:h(defaultMsg1)}<br />${f:h(defaultMsg2)}</p>

<hr />

<c:choose>
	<c:when test="${ fromMenu eq FROM_MENU_MAIL }">
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

<table cellpadding="0" cellspacing="0" border="0" id="select_table">
	<tr>
		<td style="text-align:left;">
		</td>
		<td class="posi_right">
		<img src="${SHOP_CONTENS}/images/cmn/icon_noread.gif" alt="未読" />&nbsp;…未読
		<img src="${SHOP_CONTENS}/images/cmn/icon_read.gif" alt="既読" />&nbsp;…既読
		<img src="${SHOP_CONTENS}/images/cmn/icon_reply.gif" alt="返信済" />&nbsp;…返信済
		<img src="${SHOP_CONTENS}/images/cmn/icon_submit.gif" alt="送信メール" />&nbsp;…送信メール
		</td>
	</tr>

</table>
<hr />

<html:errors />

<c:if test="${existDataFlg}">


<!-- #page# -->
<table cellpadding="0" cellspacing="0" border="0" class="page">
	<tr>
		<td class="pulldown"></td>
		<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}" prevLabel="前へ" nextLabel="次へ">
				<c:choose>
					<c:when test="${dto.linkFlg eq true}">
						--><span><a href="${f:url(gf:makePathConcat2Arg(pageNaviPath, scoutMailTargetDto.id, dto.pageNum))}">${dto.label}</a></span><!--
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

<!-- #listTable# -->
<div id="member_profile" class="wrap_detail">
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">

	<tr>
		<th width="10" class="posi_center " rowspan="2">No</th>
		<th width="70" colspan="2" class="bdrd_bottom">会員ID</th>
		<th width="200" class="bdrd_bottom">住所</th>
		<th width="35" class="posi_center " rowspan="2">雇用<br />形態</th>
		<th width="10" class="bdrd_bottom">希望業態</th>
		<th width="90" class="bdrd_bottom">スカウト日</th>
		<th width="30" class="posi_center bdrs_right" rowspan="2">詳細</th>
	</tr>
	<tr>
		<th width="35" class="posi_center">メール</th>
		<th width="35" class="posi_center">状態</th>
		<th>メモ</th>
		<th >希望業種</th>
		<th >ステータス</th>
	</tr>


<s:form action="${f:h(actionPath)}">
	<tr id="trId${applicationDto.id}_01">
		<td rowspan="2" class="posi_center ">1</td>

		<%-- 会員情報 --%>
		<td colspan="2" class="bdrd_bottom ">
			<img src="${SHOP_CONTENS}${(scoutMailTargetDto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(scoutMemberInfoDto.sexKbn, sexList, 'value', 'label')}" />
			&nbsp;${f:h(scoutMailTargetDto.memberId)}&nbsp;(${f:h(scoutMailTargetDto.age)})
		</td>

		<%-- 住所 --%>
		<td class="bdrd_bottom  ${f:h(classStr)}">
			<gt:prefecturesList name="prefecturesList"  />
			${f:label(scoutMailTargetDto.prefecturesCd, prefecturesList, 'value', 'label')}
			${f:h(gf:toMunicipality(scoutMailTargetDto.municipality))}
			&nbsp;
		</td>

		<%-- 雇用形態 --%>
		<td width="35" class="posi_center ${f:h(classStr)}" rowspan="2">
			<c:forEach items="${scoutMailTargetDto.employPtnKbnList}" var="employPtnKbn">
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
		<td width="130" class="bdrd_bottom  ${f:h(classStr)}"><gt:convertToJobName items="${scoutMailTargetDto.jobKbnList}"/></td>

		<%-- スカウト日 --%>
		<td width="90" class="bdrd_bottom  ${f:h(classStr)}"><fmt:formatDate value="${scoutMailTargetDto.sendDatetime }" pattern="<%=GourmetCareeConstants.DATE_FORMAT_SLASH %>"/></td>

		<td class="posi_center bdrs_right " rowspan="2">
			<c:set var="url" value="${f:url(gf:makePathConcat1Arg( '/member/subMemberDetail/index', scoutMailTargetDto.memberId))}" scope="page" />
			<a href="#" onclick="detailWindow('${url}', '${f:h(scoutMailTargetDto.memberId)}')">
				詳細
			</a>
		</td>
	</tr>
	<tr id="trId${applicationDto.id}_02">

		<%-- メール --%>
		<td class="posi_center ${classStr}">

			<c:choose>
			<c:when test="${scoutMailTargetDto.unopenedMailFlg eq true}">
				<a href="${f:url('/scoutMail/list/')}"><img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" /></a>
			</c:when>
			<c:otherwise>
				&nbsp;
			</c:otherwise>
			</c:choose>

		</td>

		<%-- 状態 --%>
		<td width="50" class="posi_center  ${f:h(classStr)}">
			<c:choose>
				<c:when test="${scoutMailTargetDto.scoutReceiveKbn eq 2}">
					<img src="${SHOP_CONTENS}/images/scout/icon_receive.gif" alt="スカウト受け取る" width="20" height="20" />&nbsp;
				</c:when>
				<c:when test="${scoutMailTargetDto.scoutReceiveKbn eq 3}">
					<img src="${SHOP_CONTENS}/images/scout/icon_turndown.gif" alt="スカウト断る" width="20" height="20" />&nbsp;
				</c:when>
			</c:choose>
			<c:if test="${scoutMailTargetDto.memberStatusDto.footprintFlg eq 1}"><img src="${SHOP_CONTENS}/images/scout/icon_interest.gif" alt="気になる" width="20" height="20" />&nbsp;<br></c:if>
			<c:if test="${scoutMailTargetDto.memberStatusDto.applicationFlg eq 1}"><img src="${SHOP_CONTENS}/images/scout/icon_entry.gif" alt="応募あり" width="20" height="20" />&nbsp;<br></c:if>
			&nbsp;
		</td>

		<%-- メモ --%>
		<td class="${f:h(classStr)}">
			<%-- 要メモフォームの追加
			<html:text property="memo" value="${f:h(dto.memo)}" size="20" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
			<html:hidden property="id" value="${dto.id}" />
			<html:hidden property="version" value="${dto.version}" />
			 --%>
			 <input type="text" value="${f:h(scoutMailTargetDto.memo)}" id="scoutMailMemo_${f:h(scoutMailTargetDto.id)}" size="17" />&nbsp;<input type="button" class="btn_entry" size="20" onclick="addMemo(${f:h(scoutMailTargetDto.id)});"/>
		</td>

		<%--業態 --%>
		<td class="${f:h(classStr)}"><gt:convertToIndustryName items="${scoutMailTargetDto.industryKbnList}"/></td>

		<%-- ステータス --%>
		<td class="${f:h(classStr)}">
			<c:set var="selectionFlgMap" value="<%=MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP %>" scope="page" />
			<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>"  />
			<select name="selectionKbn" id="selectionKbn_${scoutMailTargetDto.id}" style="background-color: ${scoutMailTargetDto.selectionFlgColor}" onchange="onchangeSelectionKbn('selectionKbn', '${scoutMailTargetDto.id}');">
				<c:forEach items="${selectionList}" var="i">
					<c:set var="selected" value="${(i.value eq scoutMailTargetDto.selectionFlg) ? 'selected' : ''}" />
					<option value="${f:h(i.value)}" style="background-color: ${f:h(selectionFlgMap[i.value])}"  ${f:h(selected)}>${f:h(i.label)}</option>
				</c:forEach>
			</select>
		</td>

	</tr>
</s:form>

</table>
</div>
<br/>

<div id="entry_profile" class="wrap_detail">
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
	<tr>
		<th width="25" class="posi_center">状態</th>
		<th width="60" class="posi_center">送・受</th>
		<th>件名</th>
		<th width="100" class="bdrs_right">送受信日</th>
	</tr>
	<c:forEach var="dto" items="${dataList}">
	<tr>
		<%-- 状態 --%>
		<td class="posi_center">

			<c:choose>
				<c:when test="${dto.sendKbn eq SEND}">
					<c:choose>
						<c:when test="${dto.receiveReadFlg}">
							<img src="${SHOP_CONTENS}/images/cmn/icon_read.gif" alt="既読" />
						</c:when>
						<c:otherwise>
							<img src="${SHOP_CONTENS}/images/cmn/icon_submit.gif" alt="送信メール" />
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${dto.mailStatus eq UNOPENED}">
							<img src="${SHOP_CONTENS}/images/cmn/icon_noread.gif" alt="未読" />
						</c:when>
						<c:when test="${dto.mailStatus eq OPENED}">
							<img src="${SHOP_CONTENS}/images/cmn/icon_read.gif" alt="既読" />
						</c:when>
						<c:when test="${dto.mailStatus eq REPLIED}">
							<img src="${SHOP_CONTENS}/images/cmn/icon_reply.gif" alt="返信済" />
						</c:when>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</td>
		<%-- 送・受 --%>
		<td>
			<c:choose>
				<c:when test="${dto.sendKbn eq SEND}">
					送信
				</c:when>
				<c:otherwise>
					受信
				</c:otherwise>
			</c:choose>
		</td>
		<%-- 件名 --%>
		<td>
			<c:choose>
				<c:when test="${ fromMenu eq FROM_MENU_MAIL }">
					<a href="${f:url(gf:makePathConcat2Arg('/scoutMail/detail/indexFromDetailListFromMail', dto.id, dto.sendKbn)) }">${f:h(dto.subject)}</a>
				</c:when>
				<c:otherwise>
					<a href="${f:url(gf:makePathConcat2Arg('/scoutMail/detail/indexFromDetailList', dto.id, dto.sendKbn)) }">${f:h(dto.subject)}</a>
				</c:otherwise>
			</c:choose>
			&nbsp;
		</td>
		<%-- 送受信日 --%>
		<td class="bdrs_right"><fmt:formatDate value="${dto.sendDatetime}" pattern="yyyy/MM/dd HH:mm" />&nbsp;</td>
	</tr>
	</c:forEach>
</table>
<hr />

<!-- #page# -->
<table cellpadding="0" cellspacing="0" border="0" class="page">
	<tr>

		<td class="pulldown"></td>
		<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}" prevLabel="前へ" nextLabel="次へ">
				<c:choose>
					<c:when test="${dto.linkFlg eq true}">
						--><span><a href="${f:url(gf:makePathConcat2Arg(pageNaviPath, scoutMailTargetDto.id, dto.pageNum))}">${dto.label}</a></span><!--
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

<div class="wrap_btn">
	<c:choose>
		<c:when test="${hideHeaderFlg eq true}">
			<html:button property="" value="　"  onclick="location.href='${f:url('/scoutMail/list/searchAgain/')}'" styleId="btn_back" />
		</c:when>
		<c:when test="${empty applicationId}">
			<html:button property="" value="　"  onclick="location.href='${f:url('/scoutMember/list/showList/')}'" styleId="btn_back" />
		</c:when>
	</c:choose>
</div>

</div>

<p class="go_top"><a href="#all">▲このページのトップへ</a></p>


</div>
<!-- #main# -->
<hr />

<%@page pageEncoding="UTF-8"%>
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<c:set var="SELECTION_TARGET" value="<%=MTypeConstants.SelectionFlg.SELECTION_TARGET %>" />
<c:set var="SELECTION_OUT_TARGET" value="<%=MTypeConstants.SelectionFlg.OUT_TARGET %>" />

<c:set var="SEISYAIN"  value="<%=MTypeConstants.EmployPtnKbn.SEISYAIN %>" />
<c:set var="KEIYAKU_SYAIN"  value="<%=MTypeConstants.EmployPtnKbn.KEIYAKU_SYAIN %>" />
<c:set var="ARUBAITO_PART"  value="<%=MTypeConstants.EmployPtnKbn.ARUBAITO_PART %>" />
<c:set var="HAKEN"  value="<%=MTypeConstants.EmployPtnKbn.HAKEN %>" />

<c:set scope="page" var="RECEIVE" value="<%=MTypeConstants.SendKbn.RECEIVE %>" />
<c:set scope="page" var="SEND" value="<%=MTypeConstants.SendKbn.SEND %>" />

<c:set scope="page" var="UNOPENED" value="<%=MTypeConstants.MailStatus.UNOPENED%>" />
<c:set scope="page" var="OPENED" value="<%=MTypeConstants.MailStatus.OPENED%>" />
<c:set scope="page" var="REPLIED" value="<%=MTypeConstants.MailStatus.REPLIED%>" />

<script type="text/javascript" src="${SHOP_CONTENS}/js/setAjax.js"></script>

<!-- #main# -->
<div id="main">


<h2 title="${f:h(pageTitle)}" class="title"  id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>

<hr />

<p>${f:h(defaultMsg0)}<br />${f:h(defaultMsg1)}<br />${f:h(defaultMsg2)}</p>

<hr />


<tiles:insert template="/WEB-INF/view/application/spc01_change_tab.jsp" flush="false"/>

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

<c:set var="pageNaviPath" value="/observateApplication/detailMailList/changePage/${f:h(id)}" scope="page" />

<!-- #page# -->
<table cellpadding="0" cellspacing="0" border="0" class="page">
	<tr>
		<td class="pulldown"></td>
		<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}" prevLabel="前へ" nextLabel="次へ">
				<c:choose>
					<c:when test="${dto.linkFlg eq true}">
						--><span><a href="${f:url(gf:makePathConcat1Arg(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
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
		<th width="10" class="posi_center">No</th>
		<th width="35" class="posi_center">メール</th>
		<th width="90" class="">質問者</th>
		<th width="100" class="">掲載店名</th>
		<th width="120" class="">メモ</th>
		<th width="75" class="bdrs_right">質問日時</th>
	</tr>


	<c:set var="classStr" value="${gf:odd(status.index, 'alternate', '')}" scope="page" />

	<s:form action="${f:h(actionPath)}">
		<tr id="trId${dto.id}_01">
			<td class="posi_center ${classStr}">${status.index + 1}</td>
			<td class="posi_center ${classStr}">
				<c:choose>
					<c:when test="${observateApplicationListDto.unopenedMailFlg eq true}">
						<img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" />
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class=" ${classStr}">
				<img src="${SHOP_CONTENS}${(observateApplicationListDto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(observateApplicationListDto.sexKbn, sexList, 'value', 'label')}" />
					&nbsp;${f:h(observateApplicationListDto.name)}&nbsp;(${f:h(observateApplicationListDto.age)})
			</td>
			<td class=" ${classStr}">
				${f:h(observateApplicationListDto.applicationName)}
			</td>
			<td class=" ${classStr}">
				<html:text property="memo" value="${f:h(observateApplicationListDto.memo)}" size="20" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
				<html:hidden property="id" value="${observateApplicationListDto.id}" />
				<html:hidden property="version" value="${observateApplicationListDto.version}" />
				<html:hidden property="pageNum" value="${pageNum}" />
			</td>
			<td class=" bdrs_right ${classStr}">
				<fmt:formatDate value="${observateApplicationListDto.applicationDatetime}" pattern="yyyy/MM/dd HH:mm" />
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
		<td><a href="${f:url(gf:concat4Str('/observateApplicationMail/detail/index/', dto.id, '/', observateApplicationListDto.id)) }">${f:h(dto.subject)}</a>&nbsp;</td>
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
						--><span><a href="${f:url(gf:makePathConcat1Arg(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
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

<div class="wrap_btn">
	<c:if test="${empty applicationId}">
		<html:button property="" value="　"  onclick="location.href='${f:url('/observateApplication/list/showList/')}'" styleId="btn_back" />
	</c:if>
</div>


</c:if>



</div>

<p class="go_top"><a href="#all">▲このページのトップへ</a></p>

<!-- #main# -->
<hr />

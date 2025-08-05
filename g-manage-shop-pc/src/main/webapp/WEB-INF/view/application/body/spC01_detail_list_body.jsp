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

<c:set scope="page" var="RECEIVE" value="<%=MTypeConstants.SendKbn.RECEIVE %>" />
<c:set scope="page" var="SEND" value="<%=MTypeConstants.SendKbn.SEND %>" />

<c:set scope="page" var="UNOPENED" value="<%=MTypeConstants.MailStatus.UNOPENED%>" />
<c:set scope="page" var="OPENED" value="<%=MTypeConstants.MailStatus.OPENED%>" />
<c:set scope="page" var="REPLIED" value="<%=MTypeConstants.MailStatus.REPLIED%>" />
<gt:typeList name="jobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>

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

	/**
	 * ステータスの色を変更し、DBを更新する
	 */
	var onchangeSelectionKbn = function(selectId, id) {

		var kbn = $("#" + selectId);

//		console.log(kbn);


		var selectionJson = {
				id : id,
				selectionKbn : kbn.val()
		};

		$.ajax({
			url: '${f:url("/application/detailMailList/ajaxSelectionFlg/")}',
			data: selectionJson,
			type: 'post',
			success: function(json) {
				var jsonData = JSON.parse(json);

				$.each(jsonData, function() {
					// DEBUG
					// console.log(jsonData[kbn.val()]);
					$(kbn.css("background-color", jsonData[kbn.val()]));
					location.href="${f:url(gf:makePathConcat2Arg('/application/detailMailList/changePage', id, pageNum))}";
				});

			}
		});

	};

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
						--><span><a href="${f:url(gf:makePathConcat2Arg(pageNaviPath, id, dto.pageNum))}">${dto.label}</a></span><!--
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
		<th width="10" rowspan="2" class="posi_center">No</th>
		<th colspan="2" class="bdrd_bottom">応募者</th>
		<th class="bdrd_bottom">住所</th>
		<th width="190" class="bdrd_bottom">応募職種</th>
		<th width="90" class="bdrd_bottom">応募日時</th>
		<th class="posi_center bdrs_right" rowspan="2">詳細</th>
	</tr>
	<tr>
		<th width="35" class="posi_center">メール</th>
		<th width="50" class="posi_center">原稿番号</th>
		<th colspan="1">掲載店名</th>
		<th>メモ</th>
		<th colspan="1">ステータス</th>
	</tr>

<s:form action="${f:h(actionPath)}">
	<tr id="trId${applicationDto.id}_01">
		<td rowspan="2" class="posi_center ">1</td>
		<td colspan="2" class="bdrd_bottom ">

			<img src="${SHOP_CONTENS}${(applicationDto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(applicationDto.sexKbn, sexList, 'value', 'label')}" />
			&nbsp;${f:h(applicationDto.name)}(${f:h(applicationDto.age)})

		</td>
		<td class="bdrd_bottom ">
			<gt:prefecturesList name="prefecturesList"  />
			${f:label(applicationDto.prefecturesCd, prefecturesList, 'value', 'label')}
			${f:h(applicationDto.municipality)}
			${f:h(applicationDto.address)}&nbsp;
		</td>
		<td class="bdrd_bottom ">
			<c:if test="${not empty applicationDto.jobKbn}">
				${f:label(applicationDto.jobKbn, jobKbnList, 'value', 'label')}
			</c:if>
			<c:if test="${not empty applicationDto.hopeJob}">
				${f:h(applicationDto.hopeJob)}
			</c:if>
		</td>
		<td class="bdrd_bottom ">
			<fmt:formatDate value="${applicationDto.applicationDatetime}" pattern="yyyy/MM/dd HH:mm" />
		</td>
		<td class="posi_center bdrs_right " rowspan="2">
			<c:set var="url" value="${f:url(gf:makePathConcat1Arg( '/application/detail/subApplicationDetail', applicationDto.id))}" scope="page" />
			<a href="#" onclick="detailWindow('${url}', '${f:h(applicationDto.id)}')">
				詳細
			</a>
		</td>
	</tr>
	<tr id="trId${applicationDto.id}_02">
		<td class="posi_center ">

			<c:choose>
			<c:when test="${applicationDto.unopenedMailFlg eq true}">
				<a href="${f:url('/applicationMail/list/')}"><img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" /></a>
			</c:when>
			<c:otherwise>
				&nbsp;
			</c:otherwise>
			</c:choose>

		</td>
		<td class="posi_center ">
			<%-- GCWが空の場合はWEBからの応募 --%>
			${(empty applicationDto.gcwCd) ? applicationDto.webId : '雑誌'}
		</td>
		<td class=" " colspan="1">${f:h(applicationDto.applicationName)}</td>
		<td class=" ">
			<html:text property="memo" value="${f:h(applicationDto.memo)}" size="17" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
			<html:hidden property="id" value="${applicationDto.id}" />
			<html:hidden property="version" value="${applicationDto.version}" />
		</td>
		<td class=" ">
			<c:set var="selectionFlgMap" value="<%=MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP %>" scope="page" />
			<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>" />
			<select name="selectionKbn" id="selectionKbn" style="background-color: ${applicationDto.selectionFlgColor}" onchange="onchangeSelectionKbn('selectionKbn', '${applicationDto.id}');">
				<c:forEach items="${selectionList}" var="i">
					<c:set var="selected" value="${(i.value eq applicationDto.selectionFlg) ? 'selected' : ''}" />
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
		<td><a href="${f:url(gf:makePathConcat2Arg('/applicationMail/detail/indexFromDetailList', dto.id, dto.applicationId)) }">${f:h(dto.subject)}</a>&nbsp;</td>
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
						--><span><a href="${f:url(gf:makePathConcat2Arg(pageNaviPath, id, dto.pageNum))}">${dto.label}</a></span><!--
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
			<html:button property="" value="　"  onclick="location.href='${f:url('/applicationMail/list/showList/')}'" styleId="btn_back" />
		</c:when>
		<c:when test="${empty applicationId}">
			<html:button property="" value="　"  onclick="location.href='${f:url('/application/list/showList/')}'" styleId="btn_back" />
		</c:when>
	</c:choose>
</div>
</div>

<p class="go_top"><a href="#all">▲このページのトップへ</a></p>


</div>
<!-- #main# -->
<hr />

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

		$(window).load(function(){



		});

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

		// console.log(kbn);


		var selectionJson = {
				id : id,
				selectionKbn : kbn.val()
		};

		$.ajax({
			url: '${f:url("/arbeit/detailMailList/ajaxSelectionFlg/")}',
			data: selectionJson,
			type: 'post',
			success: function(json) {
				var jsonData = JSON.parse(json);

				$.each(jsonData, function() {
					// DEBUG
					 console.log(jsonData[kbn.val()]);
					$(kbn.css("background-color", jsonData[kbn.val()]));
					location.href="${f:url(gf:makePathConcat2Arg('/arbeit/detailMailList/changePage', id, pageNum))}";
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

<%-- ヘッダの隠しフラグがtrueでない場合にヘッダを表示する。 --%>
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
<c:set var="pageNaviPath" value="/arbeit/detailMailList/changePage/${f:h(id)}" scope="page" />

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
		<th width="10" class="posi_center " rowspan="2">No</th>
		<th colspan="2" class="bdrd_bottom">応募者</th>
		<th width="180" class="bdrd_bottom">住所</th>
		<th width="195" class="bdrd_bottom">応募職種</th>
		<th width="90" class="bdrd_bottom">応募日時</th>
		<th width="25" class="posi_center bdrs_right" rowspan="2">詳細</th>
	</tr>
	<tr>
		<%--
		<th class="posi_center">
			<input type="checkbox" id="allcheck" onclick="handleAll()"/>
		</th>
		--%>
		<th width="35" class="posi_center">メール</th>
		<th width="50" class="posi_center">店舗ID</th>
		<th >掲載店名</th>
		<th >メモ</th>
		<th >ステータス</th>
	</tr>


<s:form action="${f:h(actionPath)}">
	<tr id="trId${arbeitApplicationListDto.id}_01">
		<%-- No --%>
		<td rowspan="2" class="posi_center ${classStr}">${status.index + 1}</td>
		<%-- 応募者 --%>
		<td colspan="2" class="bdrd_bottom ${classStr}">

			<img src="${SHOP_CONTENS}${(arbeitApplicationListDto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(arbeitApplicationListDto.sexKbn, sexList, 'value', 'label')}" />
			&nbsp;${f:h(arbeitApplicationListDto.name)}(${f:h(arbeitApplicationListDto.age)})

		</td>
		<%-- 住所 --%>
		<td class="bdrd_bottom ${classStr}">
			<gt:prefecturesList name="prefecturesList"  />
			${f:label(arbeitApplicationListDto.prefecturesCd, prefecturesList, 'value', 'label')}
			${f:h(arbeitApplicationListDto.municipality)}
			${f:h(arbeitApplicationListDto.address)}&nbsp;
		</td>
		<%-- 応募職種 --%>
		<td class="bdrd_bottom ${classStr}">${f:h(arbeitApplicationListDto.applicationJob)}</td>
		<%-- 応募日時 --%>
		<td class="bdrd_bottom ${classStr}">
			<fmt:formatDate value="${arbeitApplicationListDto.applicationDatetime}" pattern="yyyy/MM/dd HH:mm" />
		</td>
		<%-- 詳細 --%>
		<td class="posi_center bdrs_right " rowspan="2">
			<c:set var="url" value="${f:url(gf:makePathConcat1Arg( '/arbeit/detail/subApplicationDetail', arbeitApplicationListDto.id))}" scope="page" />
			<a href="#" onclick="detailWindow('${url}', '${f:h(arbeitApplicationListDto.id)}')">
				詳細
			</a>
		</td>
	</tr>
	<tr id="trId${arbeitApplicationListDto.id}_02">

		<%-- メール --%>
		<td class="posi_center ${classStr}">

			<c:choose>
			<c:when test="${arbeitApplicationListDto.unopenedMailFlg eq true}">
				<img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" />
			</c:when>
			<c:otherwise>
				&nbsp;
			</c:otherwise>
			</c:choose>

		</td>
		<%-- 店舗ID --%>
		<td class="posi_center ${classStr}">
			<%-- GCWが空の場合はWEBからの応募 --%>
			${f:h(arbeitApplicationListDto.shopListId)}
		</td>
		<%-- 掲載店名 --%>
		<td class=" ${classStr}"  colspan="1">${f:h(arbeitApplicationListDto.applicationName)}</td>
		<%-- メモ --%>
		<td class=" ${classStr}" colspan="1">
			<html:text property="memo" value="${f:h(arbeitApplicationListDto.memo)}" size="17" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
			<html:hidden property="id" />
			<html:hidden property="pageNum"/>

			<html:hidden property="version" value="${arbeitApplicationListDto.version}" />
		</td>
		<%-- ステータス --%>
		<td class=" ${classStr}">

			<c:set var="selectionFlgMap" value="<%=MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP %>" />
			<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>" />
			<select name="selectionKbn" id="selectionKbn" style="background-color: ${arbeitApplicationListDto.selectionFlgColor}" onchange="onchangeSelectionKbn('selectionKbn', '${arbeitApplicationListDto.id}');">
				<c:forEach items="${selectionList}" var="i">
					<c:set var="selected" value="${(i.value eq arbeitApplicationListDto.selectionFlg) ? 'selected' : ''}" />
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
		<td><a href="${f:url(gf:makePathConcat1Arg('/arbeitMail/detail/indexFromDetailList/', dto.id)) }">${f:h(dto.subject)}</a>&nbsp;</td>
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

</c:if>
<div class="wrap_btn">
	<c:choose>
		<c:when test="${hideHeaderFlg eq true}">
			<html:button property="" value="　"  onclick="location.href='${f:url('/arbeitMail/list/showList/')}'" styleId="btn_back" />
		</c:when>
		<c:when test="${empty applicationId}">
			<html:button property="" value="　"  onclick="location.href='${f:url('/arbeit/list/showList/')}'" styleId="btn_back" />
		</c:when>
	</c:choose>
</div>
</div>

<p class="go_top"><a href="#all">▲このページのトップへ</a></p>


</div>
<!-- #main# -->
<hr />

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

<script type="text/javascript" src="${SHOP_CONTENS}/js/setAjax.js"></script>

<script type="text/javascript">
//<![CDATA[

    <%-- 退避保持用Id --%>
    var varApplicationId = "";

    <%--
    /**
	 * 選考対象から外す。
	 * @param path Actionパス
	 */
	 --%>
	function doTargetOff(id) {

		varApplicationId = id;
		var path = $("#selectionPath" + id + ":hidden").val();

		$.get(path, {'id': id, 'checkKbn': '${SELECTION_OUT_TARGET}'}, afterLoadOff);
	}

	<%--
	/**
	 * 選考対象に戻す
	 * @param path Actionパス
	 */
	 --%>
	function doTargetOn(id) {

		varApplicationId = id;
		var path = $("#selectionPath" + id + ":hidden").val();

		$.get(path, {'id': id, 'checkKbn': '${SELECTION_TARGET}'}, afterLoadOn);
	}

	<%--
	/**
	 * 戻すボタン押下時の事後処理
	 */
	 --%>
	function afterLoadOn() {

		<%-- onclick属性の入れ替えだけではJSの関数を変更できないので、inputタグ毎入れ替える --%>
		$("#selectionButton" + varApplicationId + ":button")
			.replaceWith("<input id='selectionButton" +varApplicationId +  "' type='button' value='外す' onclick=\"doTargetOff('"+varApplicationId+"');\"/>");

		<%-- 2行で1単位なのでID属性がtrId{ID}_01, trId{ID}_02の2タグの子要素TDを指定して操作 --%>
		$("tr[id^='trId" + varApplicationId +"_']>td").removeClass("end");

		varApplicationId = "";
	}

	<%--
	/**
	 * 外すボタン押下時の事後処理
	 */
	 --%>
	function afterLoadOff() {

		<%-- onclick属性の入れ替えだけではJSの関数を変更できないので、inputタグ毎入れ替える --%>
		$("#selectionButton" + varApplicationId + ":button")
			.replaceWith("<input id='selectionButton" +varApplicationId +  "' type='button' value='戻す' onclick=\"doTargetOn('"+varApplicationId+"');\"/>");

		<%-- 2行で1単位なのでID属性がtrId{ID}_01, trId{ID}_02の2タグの子要素TDを操作 --%>
		$("tr[id^='trId" + varApplicationId +"_']>td").addClass("end");

		varApplicationId = "";
	}

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
			<select onChange="location.href=this.options[this.selectedIndex].value">
				<option value="${f:url('/application/list/')}">応募者</option>
				<option value="${f:url('/applicationMail/list/')}">日付</option>
			</select>
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

<!-- #listTable# -->
<div id="member_profile" class="wrap_detail">
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">

	<tr>
		<th width="10" rowspan="2" class="posi_center">No</th>
		<th colspan="2" class="bdrd_bottom">応募者</th>
		<th class="bdrd_bottom">住所</th>
		<th width="35" class="posi_center bdrd_bottom">雇用<br />形態</th>
		<th width="130" class="bdrd_bottom">希望職種</th>
		<th width="90" class="bdrd_bottom">応募日時</th>
		<th width="30" class="posi_center bdrd_bottom bdrs_right">選考<br />対象</th>
	</tr>
	<tr>
		<th width="35" class="posi_center">メール</th>
		<th width="50" class="posi_center">原稿番号</th>
		<th colspan="2">掲載店名</th>
		<th colspan="2">メモ</th>
		<th class="posi_center bdrs_right">詳細</th>
	</tr>

	<c:set var="classStr" value="${gf:odd(status.index, 'alternate', '')} ${(applicationDto.selectionTarget eq true) ? '' : 'end'}" scope="page" />

	<tr id="trId${applicationDto.id}_01">
		<td rowspan="2" class="posi_center ${classStr}">1</td>
		<td colspan="2" class="bdrd_bottom ${classStr}">

			<img src="${SHOP_CONTENS}${(applicationDto.applicantMale eq true) ? '/images/cmn/icon_man.gif' : '/images/cmn/icon_woman.gif'}" alt="${f:label(applicationDto.sexKbn, sexList, 'value', 'label')}" />
			&nbsp;${f:h(applicationDto.name)}(${f:h(applicationDto.age)})

		</td>
		<td class="bdrd_bottom ${classStr}">
			<gt:prefecturesList name="prefecturesList"  />
			${f:label(applicationDto.prefecturesCd, prefecturesList, 'value', 'label')}
			${f:h(applicationDto.municipality)}
			${f:h(applicationDto.address)}&nbsp;
		</td>
		<td class="posi_center bdrd_bottom ${classStr}">
			<c:choose>
			<c:when test="${applicationDto.employPtnKbn eq SEISYAIN}">
			    <p class="employ_icon regular"></p>
			</c:when>
			<c:when test="${applicationDto.employPtnKbn eq ARUBAITO_PART}">
			    <p class="employ_icon parttimer"></p>
			</c:when>
			<c:when test="${applicationDto.employPtnKbn eq KEIEISYA}">
			    <p class="employ_icon manage"></p>
			</c:when>
			<c:when test="${applicationDto.employPtnKbn eq OTHER}">
			    <p class="employ_icon entrust"></p>
			</c:when>
			<c:when test="${applicationDto.employPtnKbn eq HAKEN}">
			    <p class="employ_icon dispatch"></p>
			</c:when>
			<c:when test="${applicationDto.employPtnKbn eq KEIYAKU_SYAIN}">
			    <p class="employ_icon contract"></p>
			</c:when>
			</c:choose>
		</td>
		<td class="bdrd_bottom ${classStr}">${f:h(applicationDto.hopeJob)}</td>
		<td class="bdrd_bottom ${classStr}">
			<fmt:formatDate value="${applicationDto.applicationDatetime}" pattern="yyyy/MM/dd HH:mm" />
		</td>
		<td class="posi_center bdrd_bottom bdrs_right ${classStr}">

			<%-- ajax用のパス生成。 --%>
			<c:set var="selectionPath" value="/application/list/check" scope="page" />
			<input type="hidden" id="selectionPath${applicationDto.id}" value="${f:url(selectionPath)}" />

			<c:choose>
			<c:when test="${applicationDto.selectionTarget eq true}">
				<input id="selectionButton${applicationDto.id}" type="button" value="外す" onclick="doTargetOff('${applicationDto.id}');" />
			</c:when>
			<c:otherwise>
				<input id="selectionButton${applicationDto.id}" type="button" value="戻す" onclick="doTargetOn('${applicationDto.id}');"/>
			</c:otherwise>
			</c:choose>

		</td>
	</tr>
	<tr id="trId${applicationDto.id}_02">
		<td class="posi_center ${classStr}">

			<c:choose>
			<c:when test="${applicationDto.unopenedMailFlg eq true}">
				<a href="${f:url('/applicationMail/list/')}"><img src="${SHOP_CONTENS}/images/cmn/icon_mail.gif" alt="メール確認" /></a>
			</c:when>
			<c:otherwise>
				&nbsp;
			</c:otherwise>
			</c:choose>

		</td>
		<td class="posi_center ${classStr}">
			<%-- GCWが空の場合はWEBからの応募 --%>
			${(empty applicationDto.gcwCd) ? applicationDto.webId : '雑誌'}
		</td>
		<td class=" ${classStr}" colspan="2">${f:h(applicationDto.applicationName)}</td>
		<td class=" ${classStr}" colspan="2">
			<html:text property="memo" value="${f:h(applicationDto.memo)}" size="25" />&nbsp;<html:submit property="editMemo" value="&nbsp;" styleClass="btn_entry" />
			<html:hidden property="id" value="${applicationDto.id}" />
			<html:hidden property="version" value="${applicationDto.version}" />
		</td>
		<td class="posi_center bdrs_right ${classStr}">
			<c:set var="url" value="${f:url(gf:makePathConcat1Arg( '/application/detail/subApplicationDetail', applicationDto.id))}" scope="page" />
			<a href="javascript:void(0);" onclick="detailWindow('${url}', '${applicationDto.id}')">詳細</a>
		</td>
	</tr>
</table>
</div>
<br/>

<div id="entry_profile" class="wrap_detail">
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
	<tr>
		<th width="25" class="posi_center">状態</th>
		<th width="60" class="posi_center">送信か受信</th>
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
		<td><a href="${f:url(gf:concat4Str('/applicationMail/detail/index/', dto.id, '/', dto.applicationId)) }">${f:h(dto.subject)}</a>&nbsp;</td>
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

</div>

<p class="go_top"><a href="#all">▲このページのトップへ</a></p>


</div>
<!-- #main# -->
<hr />

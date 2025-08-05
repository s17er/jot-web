<%@page import="com.gourmetcaree.db.common.entity.MAdvancedRegistration"%>
<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:prefecturesList name="prefecturesList" blankLineLabel="--" />
<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="juskillList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>"  blankLineLabel="--"  />
<gt:advancedRegistrationList name="advancedList" nameKbn="<%=MAdvancedRegistration.NameKbn.SHORT_NAME %>"/>
<gt:typeList name="attendedStatusList" typeCd="<%=MTypeConstants.AdvancedRegistrationAttendedStatus.TYPE_CD%>" blankLineLabel="--" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--
	var printWindow = null;

	// 「DatePicker」の搭載
	$(function(){
		$("#registrationStartDate").datepicker();
		$("#registrationEndDate").datepicker();
	});

	var openPrintOutWindow = function() {
		printWindow = window.open("${f:url(actionPrintOutPath)}", "PRINT_OUT", "width=1000, height=700, toolbar=yes,  scrollbars=yes, resizable=yes, location=yes, menubar=yes");
	};

	 /**
	* 画面のunload時にプレビュー用のwindowを閉じるイベント待ちfunction
	*/

	$(function() {
		$(window).unload(function(ev) {
			if (printWindow != null) {
				printWindow.close();
			}
		});
	});


	var onClickAttenedStatusButton = function(id) {

//	    var buttonId = "attendedStatusBtn_" + id;
	    var url = "${f:url('/advancedRegistrationMember/list/toggle')}?toggleId=" + id;
	    location.href = url;
	};
// -->
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

				<html:messages id="msg" message="true">
				<div class="message">
					<ul>
						<li>
			  				<bean:write name="msg" ignore="true"   />
			  			</li>
					</ul>
				</div>
				</html:messages>


	<!-- #wrap_serch# -->
	<div id="wrap_search">
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">検索</th>
				</tr>
				<tr>
					<th>ID</th>
    				<td class="release">
    					<html:text property="id" size="10" styleClass="disabled" />
    				</td>
					<th>都道府県</th>
					<td>
						<html:select property="prefecturesCd"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>来場ステータス</th>
					<td colspan="3">
						<html:select property="attendedStatus">
							<html:optionsCollection name="attendedStatusList" />
						</html:select>
					</td>
				</tr>
				<tr>

					<th>フリガナ</th>
					<td><html:text property="furigana" size="40" /></td>

					<th>氏名</th>
					<td><html:text property="name" size="40" /></td>

				</tr>
				<tr>
					<th>開催年</th>
					<td colspan="3">
						<c:forEach items="${advancedList}" var="t">
							<html:multibox property="advancedRegistrationIdArray" value="${f:h(t.value)}" styleId="advancedRegistrationIdArray_${f:h(t.value)}" style="width:20px" />
							<label for="advancedRegistrationIdArray_${f:h(t.value)}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>登録状況</th>
					<td>
						<gt:typeList name="advancedStatusKbnList" typeCd="<%=MTypeConstants.AdvancedRegistrationStatusKbn.TYPE_CD %>"/>
						<c:forEach items="${advancedStatusKbnList}" var="t">
							<html:multibox property="statusKbnArray" value="${f:h(t.value)}" styleId="advancedStatusKbn_${f:h(t.value)}" style="width:20px;" />
							<label for="advancedStatusKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}&nbsp;</label>
						</c:forEach>
					</td>
					<th>メルマガ</th>
					<td>
						<gt:typeList name="mailMagazineFlgList" typeCd="<%=MTypeConstants.AdvancedMailMagReceptionFlg.TYPE_CD %>" blankLineLabel="--"/>
						<html:select property="advancedMailMagazineReceptionFlg"  >
							<html:optionsCollection name="mailMagazineFlgList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>性別</th>
					<td>
						<html:select property="sexKbn"  >
							<html:optionsCollection name="sexList"/>
						</html:select>
					</td>
					<th>年齢</th>
					<td class="release">
						<html:text property="minAge" size="3" maxlength="2" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="maxAge" size="3" maxlength="2" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td class="release" ><html:text property="mailAddress" size="45" styleClass="disabled" /></td>
					<th>端末</th>
					<td class="release">
						<gt:typeList name="teminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>" blankLineLabel="--"/>
						<html:select property="terminalKbn">
							<html:optionsCollection name="teminalKbnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>登録日時</th>
					<td class="release" colspan="3">
						<html:text property="registrationStartDate" size="10" styleId="registrationStartDate" />&nbsp;
						<html:text property="registrationStartHour" size="3" styleClass="disabled" />&nbsp;：&nbsp;
						<html:text property="registrationStartMinute" size="3" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="registrationEndDate" size="10" styleId="registrationEndDate" />&nbsp;
						<html:text property="registrationEndHour" size="3" styleClass="disabled" />&nbsp;：&nbsp;
						<html:text property="registrationEndMinute" size="3" styleClass="disabled" />
					</td>
				</tr>
			</table>
			<hr />
			<div class="wrap_btn">
				<html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>
		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

<c:if test="${fn:length(list) gt 0}">
	<!-- #wrap_result# -->

		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>${pageNavi.allCount}件検索されました。</td>
				<td class="pull_down">
					<s:form action="${actionMaxRowPath}" styleId="selectForm" >
						<gt:maxRowList name="maxRowList" value="${common['gc.member.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
							<html:select property="maxRow" onchange="changeMaxRow('selectForm');">
							<html:optionsCollection name="maxRowList" />
							</html:select>
					</s:form>
				</td>
			</tr>
		</table>
		<!-- #pullDown# -->
		<hr />

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/advancedRegistrationMember/list/changePage/${dto.pageNum}" />
				--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
			</c:if>
			<c:if test="${dto.linkFlg ne true}">
				--><span>${dto.label}</span><!--
			</c:if>
			</gt:PageNavi>
			--></td>

			</tr>
		</table>
		<!-- #page# -->
		<hr />

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
			<tr>
				<th rowspan="2" width="15" class="posi_center">No.</th>
				<th width="40" class="bdrd_bottom posi_center">登録ID</th>
				<th colspan="2" width="170" class="bdrd_bottom">氏名</th>
				<th width="80" class="bdrd_bottom">都道府県</th>
				<th width="200" class="bdrd_bottom" colspan="2">住所</th>
				<th width="200" class="bdrd_bottom">メールアドレス</th>
				<th width="60" class="posi_center bdrd_bottom bdrs_right">詳細</th>
			</tr>
			<tr>
				<th class="posi_center">来場<br />ステータス</th>
				<th class="posi_center">性別</th>
				<th class="posi_center">年齢</th>
				<th>事前登録</th>
				<th>メルマガ</th>
				<th>端末</th>
				<th>開催年</th>
				<%-- ※事前登録用登録日。グルメ会員用ではない --%>
				<th class="posi_center bdrs_right">登録日</th>
			</tr>

			<c:forEach var="dto" items="${list}" varStatus="status">
			<% //テーブルの背景色を変更するためのCSSをセット %>
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
			<tr>
				<%-- No --%>
				<td rowspan="2" class="posi_center ${classStr}"><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>

				<%-- 登録ID --%>
				<td class="bdrd_bottom posi_center ${classStr}" >${f:h(dto.id)}&nbsp;</td>
				<%-- 氏名 --%>
				<td colspan="2" class="bdrd_bottom ${classStr}">
					${f:h(dto.memberName)}&nbsp;<br />
					${f:h(dto.memberNameKana)}&nbsp;
				</td>

				<%-- 都道府県 --%>
				<td class="bdrd_bottom ${classStr}">${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}&nbsp;</td>

				<%-- 住所 --%>
				<td class="bdrd_bottom ${classStr}" colspan="2">${f:h(dto.municipality)}&nbsp;</td>

				<%-- メールアドレス --%>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.loginId)}&nbsp;</td>

				<%-- 詳細 --%>
				<td class="posi_center bdrd_bottom bdrs_right ${classStr}"><a href="${f:url(gf:makePathConcat1Arg('/advancedRegistrationMember/detail/index', dto.id))}">詳細</a></td>
			</tr>
			<tr>
				<%-- 来場ステータス --%>
				<td class="posi_center ${classStr}">
					<c:set var="btnName" value="${f:label(dto.attendedStatus, attendedStatusList, 'value', 'label')}" />
					<c:set var="btnId" value="attendedStatusBtn_${dto.id}" />
					<html:button property="" value="${f:h(btnName)}" styleId="${f:h(btnId)}"  onclick="onClickAttenedStatusButton(${dto.id});"/>
				</td>
				<%-- 性別 --%>
				<td class="posi_center ${classStr}">${f:label(dto.sexKbn, sexList, 'value', 'label')}&nbsp;</td>

				<%-- 年齢 --%>
				<td class="posi_center ${classStr}">${f:h(dto.age)}&nbsp;</td>

				<%-- 事前登録 --%>
				<td class="${classStr}">
					${f:label(dto.memberStatus , advancedStatusKbnList, 'value', 'label')}&nbsp;
				</td>

				<%-- メルマガ --%>
				<td class="${classStr}">
					${f:label(dto.advancedMailMagazineReceptionFlg , mailMagazineFlgList, 'value', 'label') }&nbsp;
				</td>
				<%-- 端末 --%>
				<td class="${classStr}">
					${f:label(dto.terminalKbn, teminalKbnList, 'value', 'label')}&nbsp;
				</td>

				<%-- 空です。 --%>
				<td class="${classStr}">${f:label(dto.advancedRegistrationId, advancedList, 'value', 'label') }</td>

				<%-- 登録日(事前登録) --%>
				<td class="bdrs_right ${classStr}"><fmt:formatDate value="${dto.registrationDatetime}" pattern="<%=GourmetCareeConstants.DATE_FORMAT_SLASH %>"/>&nbsp;</td>
			</tr>
			</c:forEach>

		</table>
		<hr />

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/advancedRegistrationMember/list/changePage/${dto.pageNum}" />
				--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
			</c:if>
			<c:if test="${dto.linkFlg ne true}">
				--><span>${dto.label}</span><!--
			</c:if>
			</gt:PageNavi>
			--></td>

			</tr>
		</table>
		<!-- #page# -->
		<hr />

		<div class="wrap_btn">
			<s:form action="${f:h(actionPath)}">
				<html:submit property="mailMagazine" value="メルマガ作成" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<c:if test="${userDto.authLevel ne AUTH_LEVEL_SALES}">
				<input type="button" name="" value="CSV出力" onclick="location.href='${f:url('/advancedRegistrationMember/list/output')}';" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:if>
				<input type="button" name="" value="印刷" onclick="openPrintOutWindow();" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</s:form>
		</div>

	<!-- #wrap_result# -->
</c:if>
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

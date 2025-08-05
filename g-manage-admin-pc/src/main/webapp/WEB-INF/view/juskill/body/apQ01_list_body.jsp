<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList"  />
<gt:prefecturesList name="prefecturesList" blankLineLabel="--"  />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" scope="request" />
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="memberRegisteredList" typeCd="<%=MTypeConstants.MemberRegisteredFlg.TYPE_CD %>"  blankLineLabel="--" scope="request" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--
	// 「DatePicker」の搭載
	$(function(){
		$("#start_ymd").datepicker();
		$("#end_ymd").datepicker();
	});
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

	<h2 class="title member">${f:h(pageTitle1)}</h2>
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
					<td colspan="3"><html:text property="where_id" size="10" styleClass="disabled" /></td>
				</tr>
				<tr>
					<th>人材紹介登録者No</th>
    				<td class="release"><html:text property="where_juskillMemberNo" size="10" styleClass="disabled" /></td>
					<th>氏名</th>
					<td><html:text property="where_juskillMemberName" size="40" /></td>
				</tr>
				<tr>
					<th>都道府県</th>
					<td>
						<html:select property="where_prefecturesCd"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select>
					</td>
					<th>登録日</th>
					<td class="release">
						<html:text property="where_fromJuskillEntryDate" size="12" styleId="start_ymd" maxlength="10" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="where_toJuskillEntryDate" size="12" styleId="end_ymd" maxlength="10" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>希望業態</th>
					<td><html:text property="where_hopeIndustry" size="40" /></td>
					<th>希望職種</th>
					<td><html:text property="where_hopeJob" size="40" /></td>
				</tr>
				<tr>
					<th>性別</th>
					<td>
						<html:select property="where_sexKbn"  >
							<html:optionsCollection name="sexList"/>
						</html:select>
					</td>
					<th>年齢</th>
					<td class="release">
						<html:text property="where_lowerAge" size="3" maxlength="2" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="where_upperAge" size="3" maxlength="2" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>会員登録</th>
				    <td  colspan="3"><html:select property="where_memberRegisteredFlg"  >
							<html:optionsCollection name="memberRegisteredList"/>
						</html:select>
				     </td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td class="release"  colspan="3"><html:text property="where_mail" size="45" styleClass="disabled" /></td>
				</tr>
				<tr>
					<th>フリーワード</th>
					<td class="release"  colspan="3"><html:text property="free_word" size="45" styleClass="disabled" /></td>
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
<c:if test="${not empty juskillMemberList}">
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
				<c:set var="pageLinkPath" scope="page" value="/juskillMember/list/changePage/${dto.pageNum}" />
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
				<th width="70" class="posi_center bdrd_bottom">No.</th>
				<th width="70" class="posi_center bdrd_bottom">人材紹介登録者No</th>
				<th width="70" class="posi_center bdrd_bottom">氏名</th>
				<th width="70" class="posi_center bdrd_bottom">最寄駅</th>
				<th width="170" class="posi_center bdrd_bottom">希望業態</th>
				<th width="150" class="posi_center bdrd_bottom">希望給与(月給)</th>
				<th width="150" class="posi_center bdrd_bottom">希望休日</th>
				<th width="70" class="posi_center bdrd_bottom bdrs_right">詳細</th>
			</tr>
			<tr>
				<th class="posi_center">担当</th>
				<th class="posi_center">登録日</th>
				<th class="posi_center">性別</th>
				<th class="posi_center">年齢</th>
				<th class="posi_center">希望職種</th>
				<th colspan="2" class="posi_center">転職状況</th>
				<th class="posi_center bdrs_right">履歴書PDF</th>
			</tr>

			<c:forEach var="dto" items="${juskillMemberList}" varStatus="status">
			<% //テーブルの背景色を変更するためのCSSをセット %>
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
			<tr>
				<td class="posi_center bdrd_bottom ${classStr}"><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>
				<td class="posi_center bdrd_bottom ${classStr}" >${f:h(dto.juskillMemberNo)}&nbsp;</td>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.familyName)}&nbsp;<br />
				<td class="bdrd_bottom ${classStr}">${f:h(dto.route)}&nbsp;</td>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.hopeIndustry)}&nbsp;</td>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.closestStation)}&nbsp;</td>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.hopeJob2)}&nbsp;</td>
				<td class="posi_center bdrd_bottom bdrs_right ${classStr}">
                  <c:set var="detailPath" value="/juskillMember/detail/index/${dto.id}" />
                  <a href="${f:url(detailPath)}" class="btn btn-warning btn-xs">詳細</a>
				</td>
			</tr>
			<tr>
				<td class="posi_center ${classStr}">${f:h(dto.hearingRep)}&nbsp;</td>
				<td class="${classStr}">
					<fmt:parseDate var="juskillEntryDate" pattern="yyyy-MM-dd" value="${f:h(dto.juskillEntryDate)}" parseLocale="en"/>
					<fmt:formatDate value="${juskillEntryDate}" pattern="yyyy/MM/dd" />&nbsp;
				</td>
				<c:if test="${dto.sexKbn != 3 }">
					<td class="posi_center ${classStr}">${f:label(dto.sexKbn, sexList, 'value', 'label')}性&nbsp;</td>
				</c:if>
				<c:if test="${dto.sexKbn == 3 }">
					<td class="posi_center ${classStr}">${f:label(dto.sexKbn, sexList, 'value', 'label')}&nbsp;</td>
				</c:if>
				<td class="posi_center ${classStr}">${f:h(dto.age)}歳&nbsp;</td>
				<td class="${classStr}">${f:h(dto.hopeJob)}&nbsp;</td>
				<td class="${classStr}"colspan="2">${f:h(dto.jobChangeStatus)}&nbsp;</td>
				<td class="posi_center bdrs_right ${classStr}">
					<c:if test="${resumeMap.get(dto.id) != null }">
						<a href="${resumeMap.get(dto.id)}" target="_blank">履歴書</a>
					</c:if>
				</td>
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
				<c:set var="pageLinkPath" scope="page" value="/juskillMember/list/changePage/${dto.pageNum}" />
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
				<input type="button" name="" value="CSV出力" onclick="if(!confirm('CSVを出力しますか?')) {return false;}; location.href='${f:url(actionCsvPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:if>
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

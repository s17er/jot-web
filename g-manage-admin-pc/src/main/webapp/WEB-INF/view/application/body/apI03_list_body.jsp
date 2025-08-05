<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList" />
<gt:prefecturesList name="prefecturesList" blankLineLabel="--"/>
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="terminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="currentList" typeCd="<%=MTypeConstants.CurrentJob.TYPE_CD %>" blankLineLabel="--"/>
<gt:typeList name="foodExpList" typeCd="<%=MTypeConstants.AriNashiKbn.TYPE_CD %>" blankLineLabel="--"/>
<ar:industoryList name="arbeitIndstryList" blankLineLabel="--"/>


<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--

// 「DatePicker」の搭載
	$(function(){
		$("#applicationStartDate").datepicker();
		$("#applicationEndDate").datepicker();
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

	<h2 class="title application">${f:h(pageTitle1)}</h2>
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
	<div class="wrap_search">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">検索</th>
				</tr>
				<tr>
					<th width="15%">エリア</th>
					<td width="35%">
						<html:select property="areaCd">
							<html:optionsCollection name="areaList"/>
						</html:select>
					</td>
					<th width="15%">都道府県</th>
					<td width="35%">
						<html:select property="prefecturesCd"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>性別</th>
					<td >
						<html:select property="sexKbn"  >
							<html:optionsCollection name="sexKbnList"/>
						</html:select>
					</td>
					<th>年齢</th>
					<td class="release">
						<html:text property="lowerAge" size="2" maxlength="2" styleClass="disabled" />&nbsp;～&nbsp;
						<html:text property="upperAge" size="2" maxlength="2" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>応募ID</th>
					<td>
						<html:text property="applicationId" size="10" styleClass="disabled" />
					</td>
					<th>現在の職業</th>
					<td class="release">
						<html:select property="currentJobKbn">
							<html:optionsCollection name="currentList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>経験の有無</th>
					<td>
						<html:select property="foodExpKbn">
							<html:optionsCollection name="foodExpList"/>
						</html:select>
					</td>
					<th>応募先名(店舗名)</th>
					<td class="release">
						<html:text property="applicationName" size="40" />
					</td>
				</tr>
				<tr>
					<th>端末</th>
					<td>
						<html:select property="terminalKbn">
							<html:optionsCollection name="terminalKbnList"/>
						</html:select>
					</td>
					<th>顧客名</th>
					<td class="release"><html:text property="customerName" size="40" /></td>
				</tr>
			<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
				<tr>
					<th>氏名</th>
					<td class="release">
						<html:text property="name" size="40" />
					</td>
					<th>フリガナ</th>
					<td class="release">
						<html:text property="nameKana" size="40" />
					</td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td class="release">
						<html:text property="mailAddress" size="40" styleClass="disabled" />
					</td>
					<th></th>
					<td></td>
				</tr>
			</c:if>
				<tr>
					<th>業態</th>
					<td>
						<html:select property="arbeitGyotaiId">
							<html:optionsCollection name="arbeitIndstryList"/>
						</html:select>
					</td>
					<th>応募日時</th>
					<td class="release">
						<html:text property="applicationStartDate" size="10" styleId="applicationStartDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="applicationStartHour" size="2" styleClass="disabled" maxlength="2" />：
						<html:text property="applicationStartMinute" size="2" styleClass="disabled" maxlength="2" />&nbsp;～&nbsp;
						<html:text property="applicationEndDate" size="10" styleId="applicationEndDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="applicationEndHour" size="2" styleClass="disabled" maxlength="2" />：
						<html:text property="applicationEndMinute" size="2" styleClass="disabled" maxlength="2" />
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

	<c:if test="${existDataFlg}">
		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>${pageNavi.allCount}件検索されました。</td>
				<td class="pull_down">
					<s:form action="${actionMaxRowPath}" styleId="selectForm" >
							<gt:maxRowList name="maxRowList" value="${common['gc.application.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
								<html:select property="maxRow" onchange="changeMaxRow('selectForm');">
								<html:optionsCollection name="maxRowList" />
								</html:select>
					</s:form>
				</td>
			</tr>
		</table>

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/arbeitApplication/list/changePage/${dto.pageNum}" />
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
				<th width="50" class="posi_center bdrd_bottom">応募ID</th>
				<th class="bdrd_bottom" colspan="4">店舗名</th>
				<th width="110" class="bdrd_bottom">業態</th>
				<th width="70" class="bdrd_bottom posi_center">職業</th>
				<th colspan="2" class="bdrd_bottom bdrs_right">希望職種</th>
			</tr>
			<tr>
				<th class="posi_center">性別</th>
				<th class="posi_center" width="50" >エリア</th>
				<th class="posi_center" width="35">年齢</th>
				<th>氏名</th>
				<th>住所</th>
				<th width="100">応募日時</th>
				<th width="60" class="posi_center">経験</th>
				<th width="45" class="posi_center">端末</th>
				<th width="30" class="posi_center bdrs_right">詳細</th>
			</tr>


			<c:forEach items="${searchList}" var="dto" varStatus="status">
			<% //テーブルの背景色を変更するためのCSSをセット %>
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
				<tr>
					<%-- No. --%>
					<td rowspan="2" class="posi_center ${classStr}">
						${f:h(status.count)}
					</td>

					<%-- 応募ID --%>
					<td class="posi_center bdrd_bottom ${classStr}">
						${f:h(dto.id)}
					</td>

					<%-- 店舗名 --%>
					<td class="bdrd_bottom ${classStr}" colspan="4">
						${f:h(dto.applicationName)}
					</td>

					<%-- バイト業態 --%>
					<td class="bdrd_bottom ${classStr}">
						${f:label(dto.arbeitGyotaiId, arbeitIndstryList, 'value', 'label') }
					</td>

					<%-- 職業(現在の職業) --%>
					<td width="60" class="bdrd_bottom posi_center ${classStr}">
						${f:label(dto.currentJobKbn, currentList, 'value', 'label')}&nbsp;
					</td>

					<%-- 希望職種 --%>
					<td colspan="2" class="bdrd_bottom bdrs_right ${classStr}">
						${f:br(f:h(dto.applicationJob))}&nbsp;
					</td>
				</tr>
				<tr>
					<%-- 性別 --%>
					<td class="posi_center ${classStr}">${f:label(dto.sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>

					<%-- エリア --%>
					<td class="posi_center ${classStr}">${f:label(dto.areaCd, areaList, 'value', 'label')}&nbsp;</td>

					<%-- 年齢 --%>
					<td class="posi_center ${classStr}">${f:h(dto.age)}&nbsp;</td>

					<%-- 氏名 --%>
					<td class="${classStr}"><c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">${f:h(dto.name)}&nbsp;</c:if>&nbsp;</td>

					<%-- 住所 --%>
					<td class="${classStr}">${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}${f:h(dto.municipality)}&nbsp;</td>

					<%-- 応募日時 --%>
					<td class="${classStr}"><fmt:formatDate value="${dto.applicationDatetime}" pattern="<%=GourmetCareeConstants.DATE_TIME_FORMAT %>"/> </td>

					<%-- 経験 --%>
					<td class="posi_center ${classStr}">${f:label(dto.foodExpKbn, foodExpList, 'value', 'label')}&nbsp;</td>

					<%-- 端末 --%>
					<td class="posi_center ${classStr}">${f:label(dto.terminalKbn, terminalKbnList, 'value', 'label')}&nbsp;</td>

					<%-- 詳細 --%>
					<td class="posi_center bdrs_right ${classStr}"><a href="${f:url(gf:makePathConcat1Arg('/arbeitApplication/detail', dto.id)) }">詳細</a></td>
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
				<c:set var="pageLinkPath" scope="page" value="/arbeitApplication/list/changePage/${dto.pageNum}" />
				<--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
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
			<input type="button" name="" value="CSV出力" onclick="if(!confirm('CSVを出力しますか?')) {return false;}; location.href='${f:url(actionCsvPath)}'"  onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
		</div>
	</c:if>
	<hr />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

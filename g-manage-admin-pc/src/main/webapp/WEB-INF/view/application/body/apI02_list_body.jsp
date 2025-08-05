<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList" />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="memberFlgList" typeCd="<%=MTypeConstants.MemberFlg.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="terminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="observateKbnList" typeCd="<%=MTypeConstants.ObservationKbn.TYPE_CD %>" noDisplayValue="<%=MTypeConstants.ObservationKbn.getFrontNoDispList() %>" blankLineLabel="--"/>
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<gt:typeList name="selectIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" blankLineLabel="--"/>


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
					<th width="15%">エリア&nbsp;<span class="attention">※必須</span></th>
					<td width="35%">
						<html:select property="where_areaCd"  >
							<html:optionsCollection name="areaList"/>
						</html:select>
					</td>
					<th >業態</th>
					<td>
						<html:select property="where_industryKbn">
							<html:optionsCollection name="selectIndustryKbnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>年齢</th>
					<td class="release">
						<html:text property="where_lowerAge" size="2" maxlength="2" styleClass="disabled" />&nbsp;～&nbsp;
						<html:text property="where_upperAge" size="2" maxlength="2" styleClass="disabled" />
					</td>
					<th>性別</th>
					<td>
						<html:select property="where_sexKbn"  >
							<html:optionsCollection name="sexKbnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>応募ID</th>
					<td class="release"><html:text property="where_observateApplicationId" size="10" styleClass="disabled" /></td>
					<th>会員登録</th>
					<td>
						<html:select property="where_memberFlg"  >
							<html:optionsCollection name="memberFlgList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>端末</th>
					<td>
						<html:select property="where_terminalKbn">
							<html:optionsCollection name="terminalKbnList"/>
						</html:select>
					</td>
					<th>お問い合わせの種類</th>
					<td>
						<html:select property="where_observationKbn">
							<html:optionsCollection name="observateKbnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>顧客名</th>
					<td class="release"><html:text property="where_customerName" size="40" /></td>
					<th></th>
					<td></td>
				</tr>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
					<tr>
						<th>氏名</th>
						<td class="release"><html:text property="where_name" size="40" /></td>
						<th>フリガナ</th>
						<td class="release"><html:text property="where_nameKana" size="40" /></td>
					</tr>
					<tr>
						<th>メールアドレス(PC)</th>
						<td class="release"><html:text property="where_pcMail" size="40" styleClass="disabled" /></td>
						<th>メールアドレス(携帯)</th>
						<td class="release"><html:text property="where_mobileMail" size="40" styleClass="disabled" /></td>
					</tr>
				</c:if>
				<tr>
					<th >応募先名(原稿名)</th>
					<td class="release"><html:text property="where_applicationName" size="40" /></td>
					<th>原稿番号</th>
					<td class="release"><html:text property="where_webId" /></td>
				</tr>
				<tr>
					<th>応募日時</th>
					<td colspan="3" class="release">
						<html:text property="where_applicationStartDate" size="10" styleId="applicationStartDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="where_applicationStartHour" size="2" styleClass="disabled" maxlength="2" />：
						<html:text property="where_applicationStartMinute" size="2" styleClass="disabled" maxlength="2" />&nbsp;～&nbsp;
						<html:text property="where_applicationEndDate" size="10" styleId="applicationEndDate" styleClass="disabled" maxlength="10" />&nbsp;
						<html:text property="where_applicationEndHour" size="2" styleClass="disabled" maxlength="2" />：
						<html:text property="where_applicationEndMinute" size="2" styleClass="disabled" maxlength="2" />
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
	<!-- #wrap_result# -->

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
		<!-- #pullDown# -->
		<hr />

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/observateApplication/list/changePage/${dto.pageNum}" />
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
				<th width="35" class="posi_center bdrd_bottom">エリア</th>
				<th class="bdrd_bottom">原稿名</th>
				<th width="110" class="bdrd_bottom">業態</th>
				<th colspan="3" class="bdrd_bottom bdrs_right">お問い合わせの種類</th>
			</tr>
			<tr>
				<th class="posi_center">性別</th>
				<th class="posi_center">年齢</th>
				<th>氏名</th>
				<th width="110">応募日時</th>
				<th width="70" class="posi_center">会員登録</th>
				<th width="45" class="posi_center">端末</th>
				<th width="30" class="posi_center bdrs_right">詳細</th>
			</tr>

			<c:forEach var="dto" items="${applicationList}" varStatus="status">
			<% //テーブルの背景色を変更するためのCSSをセット %>
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
				<tr>
					<td rowspan="2" class="posi_center ${classStr}"><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>
					<td class="posi_center bdrd_bottom ${classStr}">${f:h(dto.id)}&nbsp;</td>
					<td class="posi_center bdrd_bottom ${classStr}">${f:label(dto.areaCd, areaList, 'value', 'label')}&nbsp;</td>
					<td class="bdrd_bottom ${classStr}">${f:h(dto.manuscriptName)}&nbsp;</td>
					<td class="bdrd_bottom ${classStr}">
						<c:forEach var="industryKbn" items="${dto.industryKbnList}" varStatus="status">
							<c:if test="${status.first eq false}">&nbsp;/&nbsp;</c:if>
							${f:label(industryKbn, dbIndustryKbnList, 'value', 'label') }
						</c:forEach>
					</td>
					<td colspan="3" class="bdrd_bottom bdrs_right ${classStr}">${f:label(dto.observationKbn, observateKbnList, 'value', 'label') }&nbsp;</td>
				</tr>
				<tr>
					<td class="posi_center ${classStr}">${f:label(dto.sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
					<td class="posi_center ${classStr}">${f:h(dto.age)}&nbsp;</td>
					<td class="${classStr}"><c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">${dto.name}</c:if>&nbsp;</td>
					<td class="${classStr}">${f:h(dto.applicationDateTime)}</td>
					<td class="posi_center ${classStr}">${f:label(dto.memberFlg, memberFlgList, 'value', 'label')}&nbsp;</td>
					<td class="posi_center ${classStr}">${f:label(dto.terminalKbn, terminalKbnList, 'value', 'label')}&nbsp;</td>
					<td class="posi_center bdrs_right ${classStr}"><a href="${f:url(dto.detailPath)}">詳細</a></td>
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
				<c:set var="pageLinkPath" scope="page" value="/observateApplication/list/changePage/${dto.pageNum}" />
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

	<!-- #wrap_result# -->
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

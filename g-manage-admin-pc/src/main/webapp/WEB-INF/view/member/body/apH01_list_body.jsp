<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList"  />
<gt:prefecturesList name="prefecturesList" blankLineLabel="--"  />
<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"  blankLineLabel="--"  scope="request" />
<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" scope="request" />
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="juskillList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="juskillContactFlgList" typeCd="<%=MTypeConstants.JuskillContactFlg.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="gourmetMagazineReceptionList" typeCd="<%=MTypeConstants.gourmetMagazineReceptionFlg.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="deliveryStatusList" typeCd="<%=MTypeConstants.deliveryStatus.TYPE_CD %>"  blankLineLabel="--" scope="request" />


<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--

	// 「DatePicker」の搭載
	$(function(){
		$("#where_fromUpdateDate").datepicker();
		$("#where_toUpdateDate").datepicker();
		$("#where_fromInsertDate").datepicker();
		$("#where_toInsertDate").datepicker();
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
    				<td class="release"><html:text property="where_id" size="10" styleClass="disabled" /></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th>氏名</th>
					<td><html:text property="where_name" size="40" /></td>
					<th>フリガナ</th>
					<td><html:text property="where_nameKana" size="40" /></td>
				</tr>
				<tr>
					<th>プレゼント希望</th>
					<td class="release">
						<html:select property="where_gourmetMagazineReceptionFlg"  >
							<html:optionsCollection name="gourmetMagazineReceptionList"/>
						</html:select>
					</td>
					<th>発送状態</th>
					<td class="release">
						<html:select property="where_delivery_status"  >
							<html:optionsCollection name="deliveryStatusList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>エリア</th>
					<td>
						<gt:areaList name="areaList" authLevel="${userDto.authLevel}" blankLineLabel="--"/>
						<html:select property="where_areaCd">
							<html:optionsCollection name="areaList"/>
						</html:select>
					</td>
					<th>希望勤務地</th>
					<td>
						<html:select property="where_hope_area"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>希望業種</th>
					<td>
						<html:select property="where_industryCd"  >
							<html:optionsCollection name="industryList"/>
						</html:select>
					</td>
					<th>都道府県</th>
					<td>
						<html:select property="where_prefecturesCd"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>性別</th>
					<td>
						<html:select property="where_sexKbn"  >
							<html:optionsCollection name="sexList"/>
						</html:select>
					</td>
					<th>雇用形態</th>
					<td>
						<html:select property="where_empPtnKbn"  >
							<html:optionsCollection name="employPtnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>更新日</th>
					<td class="release">
						<html:text property="where_fromUpdateDate" size="12" styleId="where_fromUpdateDate" maxlength="10" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="where_toUpdateDate" size="12" styleId="where_toUpdateDate" maxlength="10" styleClass="disabled" />
					</td>
					<th>年齢</th>
					<td class="release">
						<html:text property="where_lowerAge" size="3" maxlength="2" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="where_upperAge" size="3" maxlength="2" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td class="release"><html:text property="where_mail" size="45" styleClass="disabled" /></td>
					 <th>ジャスキル登録</th>
				    <td>
				    	<html:select property="where_juskillFlg"  >
							<html:optionsCollection name="juskillList"/>
						</html:select>
				     </td>
				</tr>
				<tr>
					<th>登録日</th>
					<td class="release">
						<html:text property="where_fromInsertDate" size="12" styleId="where_fromInsertDate" maxlength="10" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="where_toInsertDate" size="12" styleId="where_toInsertDate" maxlength="10" styleClass="disabled" />
					</td>
					<th>転職相談窓口</th>
				    <td class="release">
				    	<html:select property="where_juskillContactFlg"  >
							<html:optionsCollection name="juskillContactFlgList"/>
						</html:select>
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
				<c:set var="pageLinkPath" scope="page" value="/member/list/changePage/${dto.pageNum}" />
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
				<th width="40" class="posi_center bdrd_bottom">会員ID</th>
				<th colspan="2" width="170" class="bdrd_bottom">氏名</th>
				<th width="80" class="bdrd_bottom">都道府県</th>
				<th width="200" class="bdrd_bottom">住所</th>
				<th width="200" class="bdrd_bottom">メールアドレス</th>
				<th width="60" class="posi_center bdrd_bottom bdrs_right">詳細</th>
			</tr>
			<tr>
				<th class="posi_center">希望勤務地</th>
				<th class="posi_center">性別</th>
				<th class="posi_center">年齢</th>
				<th>雇用形態</th>
				<th>希望業種</th>
				<th>希望職種</th>
				<th class="posi_center bdrs_right">更新日</th>
			</tr>

			<c:forEach var="dto" items="${memberInfoDtoList}" varStatus="status">
			<% //テーブルの背景色を変更するためのCSSをセット %>
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
			<tr>
				<td rowspan="2" class="posi_center ${classStr}"><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>
				<td class="posi_center bdrd_bottom ${classStr}" >${f:h(dto.id)}&nbsp;</td>
				<td colspan="2" class="bdrd_bottom ${classStr}">${f:h(dto.memberName)}&nbsp;<br />
				${f:h(dto.memberNameKana)}&nbsp;</td>
				<td class="bdrd_bottom ${classStr}">${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}&nbsp;</td>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.municipality)}&nbsp;</td>
				<td class="bdrd_bottom ${classStr}">${f:h(dto.loginId)}&nbsp;</td>
				<td class="posi_center bdrd_bottom bdrs_right ${classStr}"><a href="${f:url(dto.detailPath)}">詳細</a></td>
			</tr>
			<tr>
				<td class="posi_center ${classStr}">
					<% // 希望地域　表示幅の問題で改行区切りで表示 %>
					<c:forEach var="area" items="${dto.memberAreaList}" varStatus="status">
						<c:if test="${status.index ge 1}"><br /></c:if>
							${f:label(area, prefecturesList, 'value', 'label')}
					</c:forEach>
				</td>
				<td class="posi_center ${classStr}">${f:label(dto.sexKbn, sexList, 'value', 'label')}&nbsp;</td>
				<td class="posi_center ${classStr}">${f:h(dto.age)}&nbsp;</td>

				<td class="${classStr}">
					<c:forEach items="${dto.employPtnKbnList}" var="employPtnKbn">
						${f:label(employPtnKbn, employPtnList, 'value', 'label')}&nbsp;
					</c:forEach>
				</td>

				<td class="${classStr}">
					<c:forEach var="industryDto" items="${dto.industryList}" varStatus="istatus" >
						${f:label(industryDto.attributeValue, industryList, 'value', 'label')}
						<c:if test="${istatus.last ne true}">,</c:if>
					</c:forEach>&nbsp;
				</td>
				<td class="${classStr}">
					<c:forEach var="jobDto" items="${dto.jobList}" varStatus="jstatus" >
						${f:label(jobDto.attributeValue, jobList, 'value', 'label')}
						<c:if test="${jstatus.last ne true}">,</c:if>
					</c:forEach>&nbsp;
				</td>
				<td class="bdrs_right ${classStr}">${f:h(dto.lastUpdateDate)}&nbsp;</td>
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
				<c:set var="pageLinkPath" scope="page" value="/member/list/changePage/${dto.pageNum}" />
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

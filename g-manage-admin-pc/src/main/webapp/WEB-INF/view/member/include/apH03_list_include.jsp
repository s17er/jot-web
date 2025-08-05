<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page pageEncoding="UTF-8"%>

<gt:prefecturesList name="prefecturesList" blankLineLabel="--"  />
<gt:areaList name="areaList"  />
<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"  blankLineLabel="--"  scope="request" />
<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" scope="request" />
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="juskillList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>"  blankLineLabel="--" scope="request" />

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
				<c:set var="pageLinkPath" scope="page" value="/tempMember/list/changePage/${dto.pageNum}" />
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
				<th class="posi_center">エリア</th>
				<th class="posi_center">性別</th>
				<th class="posi_center">年齢</th>
				<th>雇用形態</th>
				<th>希望業種</th>
				<th>希望職種</th>
				<th class="posi_center bdrs_right">登録日</th>
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
							${f:label(area, areaList, 'value', 'label')}
					</c:forEach>
				</td>
				<td class="posi_center ${classStr}">${f:label(dto.sexKbn, sexList, 'value', 'label')}&nbsp;</td>
				<td class="posi_center ${classStr}">${f:h(dto.age)}&nbsp;</td>

				<td class="${classStr}">
					${f:label(dto.employPtnKbn, employPtnList, 'value', 'label')}&nbsp;
				</td>

				<td class="${classStr}">
					<c:forEach var="industry" items="${dto.industryList}" varStatus="istatus" >
						${f:label(industry, industryList, 'value', 'label')}
						<c:if test="${istatus.last ne true}">,</c:if>
					</c:forEach>&nbsp;
				</td>
				<td class="${classStr}">
					<c:forEach var="job" items="${dto.jobList}" varStatus="jstatus" >
						${f:label(job, jobList, 'value', 'label')}
						<c:if test="${jstatus.last ne true}">,</c:if>
					</c:forEach>&nbsp;
				</td>
				<td class="bdrs_right ${classStr}"><fmt:formatDate value="${dto.insertDatetime}" pattern="yyyy/MM/dd" />&nbsp;</td>
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
				<c:set var="pageLinkPath" scope="page" value="/tempMember/list/changePage/${dto.pageNum}" />
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


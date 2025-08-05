<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
	<tiles:insert page="../common/commonTracking.jsp" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<meta http-equiv="content-style-type" content="text/css" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="robots" content="noindex" />
	<meta name="Description" content="グルメキャリーweb 店舗側管理システム" />
	<meta name="Author" content="Joffice Inc." />

	<%	/* CSSファイルを設定 */	 %>
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/shared.css" />

<c:choose>
	<c:when test="${userDto.areaCd eq AREA_KANSAI}">
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/kansai.css" />
	</c:when>
	<%-- ログイン前などエリアを特定できない場合 --%>
	<c:otherwise>
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/kansai.css" />
	</c:otherwise>
</c:choose>
</head>

<body>
<!-- Google Tag Manager (noscript) --> <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NCVX9X2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript> <!-- End Google Tag Manager (noscript) -->
<gt:prefecturesList name="prefecturesList" />
<gt:typeList name="qualificationKbnList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"  />
<gt:typeList name="foodExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>"  />
<gt:typeList name="foreignWorkFlgList" typeCd="<%=MTypeConstants.ForeignWorkFlg.TYPE_CD %>"   />
<gt:typeList name="mailMagazineReceptionFlgList" typeCd="<%=MTypeConstants.MailMagazineReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"   />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="transferFlgList" typeCd="<%=MTypeConstants.TransferFlg.TYPE_CD %>" />
<gt:typeList name="midnightShiftFlgList" typeCd="<%=MTypeConstants.MidnightShiftFlg.TYPE_CD %>"  />
<gt:typeList name="salaryKbnList" typeCd="<%=MTypeConstants.SalaryKbn.TYPE_CD %>"  />
<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>"  />
<gt:typeList name="expManagerKbnList" typeCd="<%=MTypeConstants.ExpManagerKbn.TYPE_CD %>"/>
<gt:typeList name="expManagerYearKbnList" typeCd="<%=MTypeConstants.ExpManagerYearKbn.TYPE_CD %>"/>
<gt:typeList name="expManagerPersonsKbnList" typeCd="<%=MTypeConstants.ExpManagerPersonsKbn.TYPE_CD %>"/>

<!-- #member_profile# -->
<div id="member_profile" class="wrap_detail">
<h3 title="求職者プロフィール" id="profileScout">求職者プロフィール</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th width="150">会員ID</th>
		<td>${f:h(dto.id)}&nbsp;</td>
	</tr>
	<tr>
		<th>最終ログイン日</th>
		<td>${f:h(dto.lastLoginDatetime)}&nbsp;</td>
	</tr>
	<tr>
		<th>住所</th>
		<td>${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}${f:h(dto.municipality)}&nbsp;</td>
	</tr>
	<tr>
		<th>年齢</th>
		<td>${f:h(dto.age)}</td>
	</tr>
	<tr>
		<th>性別</th>
		<td>${f:label(dto.sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
	</tr>
	<tr>
		<th>取得資格</th>
		<td>
			<gt:convertToQualificationName items="${dto.qualification}"/>&nbsp;
			<c:if test="${not empty dto.qualificationOther}">
				${f:h(dto.qualificationOther)}
			</c:if>
		</td>
	</tr>
	<tr>
		<th>飲食業界経験年数<br />
		(アルバイト含む)</th>
		<td>${f:label(dto.foodExpKbn, foodExpKbnList, 'value', 'label')}&nbsp;</td>
	</tr>
	<tr>
		<th>海外勤務経験</th>
		<td>${f:label(dto.foreignWorkFlg, foreignWorkFlgList, 'value', 'label')}&nbsp;</td>
	</tr>
	<tr>
		<th>自己PR</th>
		<td>${f:br(f:h(dto.scoutSelfPr))}&nbsp;</td>
	</tr>
	<tr>
		<th class="bdrs_bottom">マネジメント経験</th>
		<td class="bdrs_bottom">
			役職：
			<c:choose>
				<c:when test="${not empty dto.expManagerKbn}">
					${f:label(dto.expManagerKbn, expManagerKbnList, 'value', 'label')} &nbsp;<br/>
				</c:when>
				<c:otherwise>
					-- &nbsp;<br/>
				</c:otherwise>
			</c:choose>
			経験年数：
			<c:choose>
				<c:when test="${not empty dto.expManagerYearKbn}">
					${f:label(dto.expManagerYearKbn, expManagerYearKbnList, 'value', 'label')} &nbsp;<br/>
				</c:when>
				<c:otherwise>
					-- &nbsp;<br/>
				</c:otherwise>
			</c:choose>
			人数：
			<c:choose>
				<c:when test="${not empty dto.expManagerPersonsKbn}">
					${f:label(dto.expManagerPersonsKbn, expManagerPersonsKbnList, 'value', 'label')} &nbsp;<br/>
				</c:when>
				<c:otherwise>
					-- &nbsp;<br/>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
</div>
<!-- #member_profile# -->
<hr />

<!-- #member_term# -->
<div id="member_term" class="wrap_detail">
<h3 title="希望条件" id="term">希望条件</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th width="150">希望職種</th>
		<td><gt:convertToJobName items="${dto.job}"/>&nbsp;</td>
	</tr>
	<tr>
		<th>希望業種</th>
		<td><gt:convertToIndustryName items="${dto.industry}"/>&nbsp;</td>
	</tr>
	<tr>
		<th width="150">希望勤務地</th>
		<td>
			<c:forEach var="city" items="${dto.hopeCityMap}" varStatus="prefStatus">
				<c:if test="${!prefStatus.first}"><br></c:if>
				${f:label(city.key, prefecturesList, 'value', 'label')}&nbsp;-
				<c:forEach items="${city.value}" var="cityName" varStatus="status">
					${f:h(cityName)}
					<c:if test="${!status.last}">,&nbsp;</c:if>
				</c:forEach>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<th>希望雇用形態</th>
		<td>
			<c:forEach items="${dto.employPtnKbnList}" var="employPtnKbn" varStatus="status">
				${f:label(employPtnKbn, employPtnKbnList, 'value', 'label')}
				<c:if test="${!status.last}">,&nbsp;</c:if>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<th>転勤の可・不可</th>
		<td>${f:label(dto.transferFlg, transferFlgList, 'value', 'label')}&nbsp;</td>
	</tr>
	<tr>
		<th>深夜勤務の可・不可</th>
		<td>${f:label(dto.midnightShiftFlg, midnightShiftFlgList, 'value', 'label')}&nbsp;</td>
	</tr>
	<tr>
		<th class="bdrs_bottom">希望年収</th>
		<td class="bdrs_bottom">${f:label(dto.salaryKbn, salaryKbnList, 'value', 'label')}&nbsp;</td>
	</tr>
</table>
</div>
<!-- #member_term# -->
<hr />


<!-- #member_career# -->
<div id="member_career" class="wrap_detail">
<h3 title="最終学歴" id="career">最終学歴</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th width="150">学校名</th>
		<td>${f:h(dto.schoolName)}&nbsp;</td>
	</tr>
	<tr>
		<th>学部・学科名</th>
		<td>${f:h(dto.department)}&nbsp;</td>
	</tr>
	<tr>
		<th class="bdrs_bottom">状況</th>
		<td class="bdrs_bottom">${f:label(dto.graduationKbn, graduationKbnList, 'value', 'label')}&nbsp;</td>
	</tr>
</table>
</div>
<!-- #member_career# -->
<hr />

<!-- #member_work# -->

<c:if test="${careerListExist}">
<div id="member_work" class="wrap_detail">
<h3 title="職務履歴" id="work">職務履歴</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">

	<c:forEach var="careerDto" items="${dto.careerList}" varStatus="status">
		<tr>
			<th colspan="2" class="bdrs_right">職歴<fmt:formatNumber value="${status.index + 1}" pattern="0" /></th>
		</tr>
		<tr>
			<th width="150">会社名</th>
			<td>${f:h(careerDto.companyName)}&nbsp;</td>
		</tr>
		<tr>
			<th>在籍期間</th>
			<td>${f:h(careerDto.workTerm)}&nbsp;</td>
		</tr>
		<tr>
			<th>職種</th>
			<td><gt:convertToJobName items="${careerDto.job}"/>&nbsp;</td>
		</tr>
		<tr>
			<th>業務内容</th>
			<td>${f:br(careerDto.businessContent)}&nbsp;</td>
		</tr>
		<tr>
			<th>客席数・坪数</th>
			<td>${f:h(careerDto.seat)}&nbsp;</td>
		</tr>
		<c:if test="${status.last ne true}">
			<tr>
				<th><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
				<td>${f:h(careerDto.monthSales)}&nbsp;</td>
			</tr>
		</c:if>
		<c:if test="${status.last}">
			<tr>
				<th class="bdrs_bottom"><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
				<td class="bdrs_bottom">${f:h(careerDto.monthSales)}&nbsp;</td>
			</tr>
		</c:if>
	</c:forEach>
</table>
</div>
</c:if>
<!-- #member_work# -->

</body>
</html>
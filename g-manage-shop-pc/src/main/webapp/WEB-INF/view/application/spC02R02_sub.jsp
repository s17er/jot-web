<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm"%>
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
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"/>
<gt:typeList name="jobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>
<gt:typeList name="expManagerKbnList" typeCd="<%=MTypeConstants.ExpManagerKbn.TYPE_CD %>"/>
<gt:typeList name="expManagerYearKbnList" typeCd="<%=MTypeConstants.ExpManagerYearKbn.TYPE_CD %>"/>
<gt:typeList name="expManagerPersonsKbnList" typeCd="<%=MTypeConstants.ExpManagerPersonsKbn.TYPE_CD %>"/>
<gt:typeList name="foogExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>" />
<gt:typeList name="foreignWorkFlgKbnList" typeCd="<%=MTypeConstants.ForeignWorkFlg.TYPE_CD %>" />
<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"/>

<c:set scope="page" var="FROM_PAGE_KBN" value="<%=ApplicationMailForm.FROM_APPLICANT_DETAIL %>" />

<html:errors />
<c:if test="${existDataFlg}">
<!-- #entry_profile# -->
<div id="entry_profile" class="wrap_detail">
<h3 title="応募者プロフィール" id="profile">応募者プロフィール</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th>応募日時</th>
		<td><fmt:formatDate value="${applicationDatetime}" pattern="yyyy/MM/dd HH:mm" /></td>
	</tr>
	<tr>
		<th width="150">応募ID</th>
		<td>${f:h(id)} &nbsp;</td>
	</tr>
	<tr>
		<th>氏名(漢字）</th>
		<td>${f:h(name)} &nbsp;</td>
	</tr>
	<tr>
		<th>氏名(カナ）</th>
		<td>${f:h(nameKana)} &nbsp;</td>
	</tr>
		<tr>
		<th>性別</th>
		<td>${f:label(sexKbn, sexList, 'value', 'label')} &nbsp;</td>
	</tr>
		<tr>
		<th>年齢</th>
		<td>
			<c:if test="${not empty birthday}">
				<fmt:formatDate value="${birthday}" pattern="yyyy/MM/dd"/>&nbsp;
			</c:if>${f:h(age)}才 &nbsp;</td>
	</tr>
	<tr>
		<th>郵便番号</th>
		<td>${f:h(zipCd)} &nbsp;</td>
	</tr>
	<tr>
		<th>住所</th>
		<td>
		<gt:prefecturesList name="prefecturesList"  />
		${f:label(prefecturesCd, prefecturesList, 'value', 'label')}
		${f:h(municipality)}
		${f:h(address)} &nbsp;
		</td>
	</tr>
	<tr>
		<th>電話番号</th>
		<td>${f:h(phoneNo1)}-${f:h(phoneNo2)}-${f:h(phoneNo3)} &nbsp;</td>
	</tr>
	<tr>
		<th>メールアドレス</th>
		<td>${f:h(pcMail)} &nbsp;</td>
	</tr>
	<c:if test="${not empty mobileMail}">
		<tr>
			<th>メールアドレス(携帯)</th>
			<td>${f:h(mobileMail)} &nbsp;</td>
		</tr>
	</c:if>
	<tr>
		<th>希望連絡時間・連絡方法</th>
		<td>${f:h(connectTime)} &nbsp;</td>
	</tr>
	<tr>
		<th>自己PR</th>
		<td>${f:br(f:h(applicationSelfPr))} &nbsp;</td>
	</tr>
	<tr>
		<th>取得資格</th>
			<td>
				<c:forEach var="qualificationKbn" items="${qualificationKbnList}" varStatus="status">
					${f:label(qualificationKbn, qualificationList, 'value', 'label')}
					<c:if test="${status.last ne true}">
						,&nbsp;
					</c:if>
				</c:forEach>
			</td>
	</tr>
	<tr>
		<th>海外勤務経験</th>
		<td>${f:label(foreignWorkFlg, foreignWorkFlgKbnList, 'value', 'label')}&nbsp;</td>
	</tr>
	<tr>
		<th>飲食業界経験年数</th>
		<td>${f:label(foodExpKbn, foogExpKbnList, 'value', 'label')}&nbsp;</td>
	</tr>
	<tr>
		<th class="bdrs_bottom">マネジメント経験</th>
		<td class="bdrs_bottom">
			役職：
			<c:choose>
				<c:when test="${not empty expManagerKbn}">
					${f:label(expManagerKbn, expManagerKbnList, 'value', 'label')} &nbsp;<br/>
				</c:when>
				<c:otherwise>
					-- &nbsp;<br/>
				</c:otherwise>
			</c:choose>
			経験年数：
			<c:choose>
				<c:when test="${not empty expManagerYearKbn}">
					${f:label(expManagerYearKbn, expManagerYearKbnList, 'value', 'label')} &nbsp;<br/>
				</c:when>
				<c:otherwise>
					-- &nbsp;<br/>
				</c:otherwise>
			</c:choose>
			人数：
			<c:choose>
				<c:when test="${not empty expManagerPersonsKbn}">
					${f:label(expManagerPersonsKbn, expManagerPersonsKbnList, 'value', 'label')} &nbsp;<br/>
				</c:when>
				<c:otherwise>
					-- &nbsp;<br/>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
</div>
<!-- #entry_profile# -->
<hr />

<!-- #entry_term# -->
<div id="entry_term" class="wrap_detail">
<h3 title="希望条件" id="term">希望条件</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th width="150">応募先</th>
		<td>${f:h(applicationName)} &nbsp;</td>
	</tr>
	<c:if test="${not empty employPtnKbn}">
		<tr>
			<th>希望雇用形態</th>
			<td>${f:label(employPtnKbn, employPtnKbnList, 'value', 'label')} &nbsp;</td>
		</tr>
	</c:if>
	<tr>
		<th>希望職種</th>
		<td>
			<c:if test="${not empty jobKbn}">
				${f:label(jobKbn, jobKbnList, 'value', 'label')}
			</c:if>
			<c:if test="${not empty hopeJob}">
				${f:h(hopeJob)} &nbsp;
			</c:if>
		</td>
	</tr>
	<tr>
		<th>希望業態　希望ブランド</th>
		<td>${f:h(hopeIndustry)}</td>
	</tr>
	<tr>
		<th>その他の希望連絡方法</th>
		<td>${f:h(contactMethod)}</td>
	</tr>
	<tr>
		<th>希望面接日</th>
		<td>
			第一希望：${f:h(firstDesired)}&nbsp;<br/>
			第二希望：${f:h(secondDesired)}&nbsp;<br/>
			第三希望：${f:h(thirdDesired)}&nbsp;<br/>
		</td>
	</tr>
	<tr>
		<th class="bdrs_bottom">その他</th>
		<td class="bdrs_bottom">${f:br(f:h(note))}</td>
	</tr>

</table>
</div>

<c:if test="${applicationSchoolHistoryExist}">
	<hr />
	<div class="wrap_detail">
		<h3 title="最終学歴" id="career">最終学歴</h3>
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
			<tr>
				<th width="150">学校名</th>
				<td>${f:h(applicationSchoolHistory.schoolName)}</td>
			</tr>
			<tr>
				<th>学部・学科</th>
				<td>${f:h(applicationSchoolHistory.department)}</td>
			</tr>
			<tr>
				<th class="bdrs_bottom">状況</th>
				<td class="bdrs_bottom">
					<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>"/>
					${f:label(applicationSchoolHistory.graduationKbn, graduationKbnList, 'value', 'label')}
				</td>
			</tr>
		</table>
	</div>
</c:if>

<c:if test="${careerHistoryListExist}">
	<hr />
	<div class="wrap_detail">
		<h3 title="職務履歴" id="work">職務履歴</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:forEach items="${careerHistoryList}" var="dto" varStatus="status">
					<tr>
						<th colspan="2">職歴${f:h(status.count)}</th>
					</tr>
					<tr>
						<th width="150">会社名</th>
						<td>${f:h(dto.companyName)}</td>
					</tr>
					<tr>
						<th>在籍期間</th>
						<td>${f:br(f:h(dto.workTerm))}</td>
					</tr>
					<tr>
						<th>職種</th>
						<td>${f:h(dto.jobKbnName)}</td>
					</tr>
					<tr>
						<th>業態</th>
						<td>${f:h(dto.industryKbnName)}</td>
					</tr>
					<tr>
						<th>業務内容</th>
						<td>${f:br(f:h(dto.businessContent))}</td>
					</tr>
					<tr>
						<th>客席数・坪数</th>
						<td>${f:br(f:h(dto.seat))}</td>
					</tr>
					<tr>
						<th class="bdrs_bottom"><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
						<td class="bdrs_bottom">${f:br(f:h(dto.monthSales))}</td>
					</tr>
				</c:forEach>
			</table>
	</div>
</c:if>

<!-- #entry_term# -->

</c:if>
</body>
</html>
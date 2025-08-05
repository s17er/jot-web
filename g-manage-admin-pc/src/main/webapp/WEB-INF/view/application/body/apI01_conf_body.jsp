<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>


<gt:areaList name="areaList"  />
<gt:prefecturesList name="prefecturesList" />
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"  />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="memberFlgList" typeCd="<%=MTypeConstants.MemberFlg.TYPE_CD %>"  />
<gt:typeList name="terminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>" />
<gt:typeList name="mischiefFlgList" typeCd="<%=MTypeConstants.MischiefFlg.TYPE_CD %>" />
<gt:typeList name="currentEmployedSituationKbnList" typeCd="<%=MTypeConstants.CurrentEmployedSituationKbn.TYPE_CD %>"/>
<gt:typeList name="possibleEntryTermKbnList" typeCd="<%=MTypeConstants.PossibleEntryTermKbn.TYPE_CD %>"/>
<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"/>
<gt:typeList name="applicationKbnList" typeCd="<%=MTypeConstants.ApplicationKbn.TYPE_CD %>" />
<gt:typeList name="jobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
<gt:typeList name="foogExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>" />
<gt:typeList name="expManagerKbnList" typeCd="<%=MTypeConstants.ExpManagerKbn.TYPE_CD %>" />
<gt:typeList name="expManagerYearKbnList" typeCd="<%=MTypeConstants.ExpManagerYearKbn.TYPE_CD %>" />
<gt:typeList name="expManagerPersonsKbnList" typeCd="<%=MTypeConstants.ExpManagerPersonsKbn.TYPE_CD %>" />
<gt:typeList name="foreignWorkFlgKbnList" typeCd="<%=MTypeConstants.ForeignWorkFlg.TYPE_CD %>" />

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<h2>${f:label(applicationKbn, applicationKbnList, 'value', 'label')}&nbsp;</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
		<c:if test="${existDataFlg eq true}">
			<h3 title="応募者プロフィール" class="title"><img src="${ADMIN_CONTENS}/images/application/title_profile.gif" alt="応募者プロフィール" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">応募ID</th>
					<td>${f:h(id)}</td>
				</tr>
				<tr>
					<th>応募日時</th>
					<td>${f:h(applicationDateTime)}&nbsp;</td>
				</tr>
				<tr>
					<th>エリア</th>
					<td>${f:label(areaCd, areaList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>顧客名</th>
					<td>${f:h(customerName)}&nbsp;</td>
				</tr>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
					<tr>
						<th>氏名(漢字)</th>
						<td>${f:h(name)}&nbsp;</td>
					</tr>
					<tr>
						<th>氏名(カナ)</th>
						<td>${f:h(nameKana)}&nbsp;</td>
					</tr>
				</c:if>
				<tr>
					<th>性別</th>
					<td>${f:label(sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>年齢</th>
					<td>
						<c:if test="${not empty birthday}">
							<fmt:formatDate value="${f:date(birthday, 'yyyy-MM-dd')}" pattern="yyyy/MM/dd"/>&nbsp;
						</c:if>
						<c:if test="${not empty age}">
						${f:h(age)}歳&nbsp;
						</c:if>
					</td>
				</tr>
				<tr>
					<th>郵便番号</th>
					<td>${f:h(zipCd)}&nbsp;</td>
				</tr>
				<tr>
					<th>住所</th>
					<td>${f:label(prefecturesCd, prefecturesList, 'value', 'label')}${f:h(municipality)}<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">${f:h(address)}</c:if>&nbsp;</td>
				</tr>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
					<tr>
						<th>電話番号</th>
						<td>${f:h(phoneNo)}&nbsp;</td>
					</tr>
					<tr>
						<th>メールアドレス</th>
						<td>${f:h(pcMail)}&nbsp;</td>
					</tr>
					<tr>
						<th>メールアドレス(携帯)</th>
						<td>${f:h(mobileMail)}&nbsp;</td>
					</tr>
				</c:if>
				<tr>
					<th>その他希望連絡方法</th>
					<td>${f:h(contactMethod)}</td>
				</tr>
				<tr>
					<th>希望連絡時間・連絡方法</th>
					<td>${f:h(connectTime)}&nbsp;</td>
				</tr>
				<tr>
					<th>現在の状況</th>
					<td>${f:label(currentEmployedSituationKbn, currentEmployedSituationKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>入社可能時期</th>
					<td>${f:label(possibleEntryTermKbn, possibleEntryTermKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>飲食業界経験年数</th>
					<td>${f:label(foodExpKbn, foogExpKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>マネジメント経験</th>
					<td>
						<dl>
							<dt>役職</dt>
							<dd>${f:label(expManagerKbn, expManagerKbnList, 'value', 'label')}&nbsp;</dd>
						</dl>
						<dl>
							<dt>経験年数</dt>
							<dd>${f:label(expManagerYearKbn, expManagerYearKbnList, 'value', 'label')}&nbsp;</dd>
						</dl>
						<dl>
							<dt>人数</dt>
							<dd>${f:label(expManagerPersonsKbn, expManagerPersonsKbnList, 'value', 'label')}&nbsp;</dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th>海外勤務経験</th>
					<td>${f:label(foreignWorkFlg, foreignWorkFlgKbnList, 'value', 'label')}&nbsp;</td>
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
					<th class="bdrs_bottom">自己PR</th>
					<td class="bdrs_bottom">${f:br(f:h(applicationSelfPr))}&nbsp;</td>
				</tr>
			</table>
			<hr />

		<c:if test="${schoolHistory ne null}">
			<h3 title="最終学歴" class="title"><img src="${ADMIN_CONTENS}/images/application/title_career.gif" alt="最終学歴" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">学校名</th>
					<td>${f:h(schoolHistory.schoolName)}</td>
				</tr>
				<tr>
					<th>学部・学科</th>
					<td>${f:h(schoolHistory.department)}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">状況</th>
					<td class="bdrs_bottom">
						<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>"/>
						${f:label(schoolHistory.graduationKbn, graduationKbnList, 'value', 'label')}
					</td>
				</tr>
			</table>
		</c:if>

		<c:if test="${careerHistoryList ne null}">
			<h3 title="職務履歴" class="title"><img src="${ADMIN_CONTENS}/images/application/title_work.gif" alt="職務履歴" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:forEach items="${careerHistoryList}" var="dto" varStatus="status">
					<tr>
						<th colspan="2" class="bdrs_right">職歴${f:h(status.count)}</th>
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
		</c:if>

			<h3 title="希望条件" class="title"><img src="${ADMIN_CONTENS}/images/application/title_term.gif" alt="希望条件" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">応募先名（原稿名）</th>
					<td>${f:h(applicationName)}</td>
				</tr>
				<tr>
					<th>希望雇用形態</th>
					<td>${f:label(employPtnKbn, employPtnKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>希望職種</th>
					<td>
						<c:if test="${not empty jobKbn}">
							${f:label(jobKbn, jobKbnList, 'value', 'label')}&nbsp;
						</c:if>
						<c:if test="${not empty hopeJob}">
							${f:h(hopeJob)}&nbsp;
						</c:if>
						</td>
				</tr>
				<tr>
					<th>希望業態　希望ブランド</th>
					<td>${f:h(hopeIndustry)}</td>
				</tr>
				<tr>
					<th>希望面接日</th>
					<td>
						<dl>
							<dt>第一希望</dt>
							<dd>${f:h(firstDesired)}&nbsp;</dd>
							<dt>第二希望</dt>
							<dd>${f:h(secondDesired)}&nbsp;</dd>
							<dt>第三希望</dt>
							<dd>${f:h(thirdDesired)}&nbsp;</dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">その他</th>
					<td class="bdrs_bottom">${f:br(f:h(note))}</td>
				</tr>
			</table>
			<hr />

			<h3 title="応募情報" class="title"><img src="${ADMIN_CONTENS}/images/application/title_applicationInfo.gif" alt="応募情報" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">会員登録</th>
					<td>${f:label(memberFlg, memberFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>端末</th>
					<td>${f:label(terminalKbn, terminalKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>いたずら応募</th>
					<td>${f:label(mischiefFlg, mischiefFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">応募遷移</th>
					<c:if test="${shopName != null}">
						<td class="bdrs_bottom">${f:h(shopName)}&nbsp;</td>
					</c:if>
					<c:if test="${shopName == null}">
						<td class="bdrs_bottom">${f:h(customerName)}&nbsp;</td>
					</c:if>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

		</c:if>
		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:typeList name="deleteReasonKbnList" typeCd="<%=MTypeConstants.DeleteReasonKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:prefecturesList name="prefecturesList" />
<gt:typeList name="foodExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>"  />
<gt:typeList name="foreignWorkFlgList" typeCd="<%=MTypeConstants.ForeignWorkFlg.TYPE_CD %>"   />
<gt:typeList name="jobInfoReceptionFlgList" typeCd="<%=MTypeConstants.JobInfoReceptionFlg.TYPE_CD %>"   />
<gt:typeList name="advancedMailMagazineReceptionFlgList" typeCd="<%=MTypeConstants.AdvancedMailMagReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="scoutMailReceptionFlgList" typeCd="<%=MTypeConstants.ScoutMailReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"   />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="transferFlgList" typeCd="<%=MTypeConstants.TransferFlg.TYPE_CD %>" />
<gt:typeList name="midnightShiftFlgList" typeCd="<%=MTypeConstants.MidnightShiftFlg.TYPE_CD %>"  />
<gt:typeList name="salaryKbnList" typeCd="<%=MTypeConstants.SalaryKbn.TYPE_CD %>"  />
<gt:typeList name="juskillFlgList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>" />
<gt:typeList name="pcMailStopFlgList" typeCd="<%=MTypeConstants.PcMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="mobileMailStopFlgList" typeCd="<%=MTypeConstants.MobileMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>"  />
<gt:typeList name="memberKbnList" typeCd="<%=MTypeConstants.MemberKbn.TYPE_CD %>" />
<gt:typeList name="attendedStatusList" typeCd="<%=MTypeConstants.AdvancedRegistrationAttendedStatus.TYPE_CD%>" />

<c:choose>
	<c:when test="${areaCd eq SHUTOKEN_AREA_CD}">
		<gt:typeList name="foreignLocationList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>"/>
	</c:when>
	<c:otherwise>
		<gt:typeList name="foreignLocationList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>"/>
	</c:otherwise>
</c:choose>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}" style="width: 300px;">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
		<c:if test="${existDataFlg eq true}">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_DELETE}">
						<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
							<tr>
								<th width="150" class="bdrs_bottom">削除理由&nbsp;<span class="attention">※必須</span></th>
								<td class="bdrs_bottom">
									<html:select property="deleteReasonKbn"  >
										<html:optionsCollection name="deleteReasonKbnList"/>
									</html:select>
								</td>
							</tr>
						</table>
					</c:when>
				</c:choose>
			<h3 title="基本情報" class="title"><img src="${ADMIN_CONTENS}/images/member/title_basicData.gif" alt="基本情報" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">登録ID</th>
					<td>${f:h(id)}&nbsp;</td>
				</tr>
				<tr>
					<th width="150">来場ステータス</th>
					<td>${f:h(f:label(attendedStatus, attendedStatusList, 'value', 'label'))}&nbsp;</td>
				</tr>
				<tr>
					<th>登録日時</th>
					<td>${f:h(registrationDatetime)}&nbsp;</td>
				</tr>
				<tr>
					<th>最終ログイン日時</th>
					<td>${f:h(lastLoginDatetime)}&nbsp;</td>
				</tr>
				<tr>
					<th>氏名(漢字)</th>
					<td>${f:h(memberName)}&nbsp;</td>
				</tr>
				<tr>
					<th>氏名(カナ)</th>
					<td>${f:h(memberNameKana)}&nbsp;</td>
				</tr>
                <tr>
                    <th>ログインメールアドレス</th>
                    <td>${f:h(loginId)}&nbsp;</td>
                </tr>
                <c:if test="${not empty mobileMail}">
					<tr>
						<th>携帯メールアドレス</th>
						<td>${f:h(mobileMail)}&nbsp;</td>
					</tr>
				</c:if>
				<c:if test="${pageKbn eq PAGE_EDIT}">
					<tr>
						<th>パスワード</th>
						<td>●●●●●●●●&nbsp;</td>
					</tr>
				</c:if>

				<tr>
					<th>性別</th>
					<td>${f:label(sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>生年月日</th>
					<td>
						<c:choose>
							<c:when test="${pageKbn eq PAGE_EDIT}">
								${f:h(parsedBirthDay)}
							</c:when>
							<c:otherwise>
								${f:h(birthday)}
							</c:otherwise>
						</c:choose>
						&nbsp;
					</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td>${f:h(phoneNo)}&nbsp;</td>
				</tr>
				<tr>
					<th>郵便番号</th>
					<td>${f:h(zipCd)}&nbsp;</td>
				</tr>
				<tr>
					<th>住所1</th>
					<td>${f:label(prefecturesCd, prefecturesList, 'value', 'label')}${f:h(municipality)}&nbsp;</td>
				</tr>
				<tr>
					<th>住所2</th>
					<td>${f:h(address)}&nbsp;</td>
				</tr>
				<tr>
					<th>経歴や資格など、PRがあればご自由にご記入ください。</th>
					<td>${f:br(f:h(advancedRegistrationSelfPr))}&nbsp;</td>
				</tr>
				<tr>
					<th>転職先に望むことがあればご自由にご記入ください。</th>
					<td>${f:br(f:h(hopeCareerChangeText))}&nbsp;</td>
				</tr>
				<tr>
					<th>現在&#40;前職&#41;の年収</th>
					<td>${f:h(workSalary)}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">取得資格</th>
					<td class="bdrs_bottom">
						<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"/>
						<c:forEach items="${qualificationKbnList}" var="t" varStatus="status">
							${f:label(t, qualificationList, 'value', 'label')}
							<c:if test="${status.last eq false}">
								,&nbsp;
							</c:if>
						</c:forEach>
						<c:if test="${not empty qualificationOther}">
							：${f:h(qualificationOther)}
						</c:if>
					</td>
				</tr>
			</table>
			<hr />


			<h3 title="現在の状況" class="title"><img src="${ADMIN_CONTENS}/images/member/title_currentSituation.gif" alt="現在の状況" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th class="bdrs_bottom" width="150">
						現在の状況
					</th>
					<td class="bdrs_bottom">
						<gt:typeList name="currentKbnList" typeCd="<%=MTypeConstants.CurrentSituationKbn.TYPE_CD %>"/>
						${f:label(currentSituationKbn, currentKbnList, 'value', 'label') }
					</td>
				</tr>
			</table>

			<h3 title="登録状況" class="title"><img src="${ADMIN_CONTENS}/images/member/title_circumstance.gif" alt="登録状況" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th class="bdrs_bottom" width="150">
						事前登録
					</th>
					<td class="bdrs_bottom">
						<gt:typeList name="advancedKbnList" typeCd="<%=MTypeConstants.AdvancedRegistrationStatusKbn.TYPE_CD %>"/>
						${f:label(advancedRegistrationKbn, advancedKbnList, 'value', 'label')}
					</td>
				</tr>
			</table>
			<hr />

			<h3 title="各メール設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_mailSet.gif" alt="各メール設定" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
			<%--
				<tr>
					<th width="150">新着求人情報</th>
					<td>${f:label(jobInfoReceptionFlg, jobInfoReceptionFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
			--%>
				<tr>
					<th  width="150" class="bdrs_bottom">メルマガ</th>
					<td class="bdrs_bottom">${f:label(advancedMailMagazineReceptionFlg, advancedMailMagazineReceptionFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
			</table>
			<hr />

			<%--
			<h3 title="スカウトメール設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_scoutMailSet.gif" alt="スカウトメール設定" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150" class="bdrs_bottom">スカウトメール</th>
					<td class="bdrs_bottom">${f:label(scoutMailReceptionFlg, scoutMailReceptionFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
			</table>
			<hr />
			--%>


			<h3 title="希望条件" class="title"><img src="${ADMIN_CONTENS}/images/member/title_termsDesired.gif" alt="希望条件" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
				<tr>
					<th width="150" >希望する業態</th>
					<td>
						<c:forEach items="${industryKbnList}" var="t" varStatus="status">
							${f:label(t, industryList, 'value', 'label')}
							<c:if test="${status.last eq false}">
								,&nbsp;
							</c:if>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th width="150" >希望する職種・ポジション</th>
					<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>
					<td>
						<c:forEach items="${jobKbnList}" var="t" varStatus="status">
							${f:label(t, jobList, 'value', 'label')}
							<c:if test="${status.last eq false}">
								,&nbsp;
							</c:if>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th width="150" class="bdrs_bottom">転職希望時期</th>
					<td class="bdrs_bottom">
						${f:h(hopeCareerChangeStr)}
						<c:if test="${not empty hopeCareerChangeTermOther}">
							：${f:h(hopeCareerChangeTermOther)}
						</c:if>
					</td>
				</tr>
			</table>


			<h3 title="最終学歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_school.gif" alt="最終学歴" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">学校名</th>
					<td>${f:h(schoolName)}&nbsp;</td>
				</tr>
				<tr>
					<th>学部・学科</th>
					<td>${f:h(department)}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">状況</th>
					<td class="bdrs_bottom">${f:label(graduationKbn, graduationKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
			</table>


			<h3 title="職務経歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_work.gif" alt="職務経歴" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">

			<c:forEach var="dto" items="${careerList}" varStatus="status">

				<tr>
				<th colspan="2" class="bdrs_right">職歴<fmt:formatNumber value="${status.index + 1}" pattern="0" /></th>
				</tr>

				<tr>
					<th width="150">会社名</th>
					<td>${f:h(dto.companyName)}&nbsp;</td>
				</tr>
				<tr>
					<th>在籍期間</th>
					<td>${f:h(dto.workTerm)}&nbsp;</td>
				</tr>
				<tr>
					<th>職種</th>
					<td>
						<gt:convertToJobName items="${dto.job}"/>
						&nbsp;
					</td>
				</tr>
				<tr>
					<th>業態</th>
					<td>
						<c:forEach items="${dto.industry}" var="t" varStatus="indStatus">
							${f:label(t, industryList, 'value', 'label')}
							<c:if test="${indStatus.last eq false}">
								,&nbsp;
							</c:if>
						</c:forEach>
						&nbsp;
					</td>
				</tr>
				<tr>
					<th>業務内容</th>
					<td>${f:br(f:h(dto.businessContent))}&nbsp;</td>
				</tr>
				<tr>
					<th>客席数・坪数</th>
					<td>${f:h(dto.seat)}&nbsp;</td>
				</tr>
				<c:if test="${status.last ne true}">
					<tr>
					<th><c:out value="<%=LabelConstants.Member.EARNINGS %>"></c:out></th>
					<td>${f:h(dto.monthSales)}&nbsp;</td>
				</tr>
				</c:if>
				<c:if test="${status.last}">
					<tr>
					<th class="bdrs_bottom"><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
					<td class="bdrs_bottom">${f:h(dto.monthSales)}&nbsp;</td>
				</tr>
				</c:if>

			</c:forEach>

			</table>
			<hr />


			<h3 title="管理者設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_admin.gif" alt="管理者設定" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">PCメール配信</th>
					<td>${f:label(pcMailStopFlg, pcMailStopFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th width="150">モバイルメール配信</th>
					<td>${f:label(mobileMailStopFlg, mobileMailStopFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">会員区分</th>
					<td class="bdrs_bottom">${f:label(memberKbn, memberKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_DETAIL}">
						<c:set var="editPath" value="/advancedRegistrationMember/edit/${id}" />
						<html:button property="edit" value="修 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" onclick="location.href='${f:url(editPath)}';"  />
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:otherwise>
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}" style="width: 300px;">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

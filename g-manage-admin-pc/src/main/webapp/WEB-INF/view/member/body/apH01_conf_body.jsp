<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:typeList name="deleteReasonKbnList" typeCd="<%=MTypeConstants.DeleteReasonKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:prefecturesList name="prefecturesList" />
<gt:typeList name="qualificationKbnList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"  />
<gt:typeList name="foodExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>"  />
<gt:typeList name="foreignWorkFlgList" typeCd="<%=MTypeConstants.ForeignWorkFlg.TYPE_CD %>"   />
<gt:typeList name="jobInfoReceptionFlgList" typeCd="<%=MTypeConstants.JobInfoReceptionFlg.TYPE_CD %>"   />
<gt:typeList name="mailMagazineReceptionFlgList" typeCd="<%=MTypeConstants.MailMagazineReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="scoutMailReceptionFlgList" typeCd="<%=MTypeConstants.ScoutMailReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"   />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="transferFlgList" typeCd="<%=MTypeConstants.TransferFlg.TYPE_CD %>" />
<gt:typeList name="midnightShiftFlgList" typeCd="<%=MTypeConstants.MidnightShiftFlg.TYPE_CD %>"  />
<gt:typeList name="salaryKbnList" typeCd="<%=MTypeConstants.SalaryKbn.TYPE_CD %>"  />
<gt:typeList name="juskillFlgList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>" />
<gt:typeList name="juskillContactFlgList" typeCd="<%=MTypeConstants.JuskillContactFlg.TYPE_CD %>" />
<gt:typeList name="pcMailStopFlgList" typeCd="<%=MTypeConstants.PcMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="mobileMailStopFlgList" typeCd="<%=MTypeConstants.MobileMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>"  />
<gt:typeList name="memberKbnList" typeCd="<%=MTypeConstants.MemberKbn.TYPE_CD %>" />
<gt:areaList name="areaCdList" />
<gt:allArea name="allAreaList"/>
<gt:typeList name="attendedStatusList" typeCd="<%=MTypeConstants.AdvancedRegistrationAttendedStatus.TYPE_CD%>" />
<gt:typeList name="advancedMailMagazineReceptionFlgList" typeCd="<%=MTypeConstants.AdvancedMailMagReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="juskillMigrationFlgList" typeCd="<%=MTypeConstants.JuskillMigrationFlg.TYPE_CD %>"  />
<gt:typeList name="gourmetMagazineReceptionList" typeCd="<%=MTypeConstants.gourmetMagazineReceptionFlg.TYPE_CD %>"  blankLineLabel="--" scope="request" />
<gt:typeList name="deliveryStatusList" typeCd="<%=MTypeConstants.deliveryStatus.TYPE_CD %>"  blankLineLabel="--" scope="request" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery-ui.min.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery-ui.min.js"></script>
<script type="text/javascript">
$(function() {
	$( "#accordion").accordion({
		collapsible: true,
		active: false
	});
	$('#tabs').tabs();
});
</script>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:if test="${interestListFlg}">
			<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
		</c:if>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title member">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form" class="member_wrap_form">
		<s:form action="${f:h(actionPath)}">
		<c:if test="${interestListFlg}">
			<html:hidden property="interestListFlg" value ="true"/>
		</c:if>
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

			<div id="tabs">
				<ul>
					<li><a href="#base">基本情報</a></li>
					<c:if test="${advanceRegistrationFlg eq true && pageKbn eq PAGE_DETAIL}">
					<li><a href="#gousetsu">合同説明会履歴</a></li>
					</c:if>
					<c:if test="${juskillInfoFlg eq true && pageKbn eq PAGE_DETAIL}">
					<li><a href="#juskill">ジャスキル</a></li>
					</c:if>
				</ul>
				<div id="base">
					<h3 title="基本情報" class="title"><img src="${ADMIN_CONTENS}/images/member/title_basicData.gif" alt="基本情報" /></h3>
					<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
						<tr>
							<th width="150">会員ID</th>
							<td>${f:h(id)}&nbsp;</td>
						</tr>
						<c:if test="${gourmetMagazineReceptionDisplayFlg}">
							<tr>
								<th>プレゼント希望</th>
								<td>
									${f:label(gourmetMagazineReceptionFlg, gourmetMagazineReceptionList, 'value', 'label')}&nbsp;
									<c:if test="${deliveryStatus != null}">
										発送状況 ： ${f:label(deliveryStatus, deliveryStatusList, 'value', 'label')}
									</c:if>
								</td>
							</tr>
						</c:if>
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
						<tr>
							<th>サブメールアドレス</th>
							<td>${f:h(subMailAddress)}&nbsp;</td>
						</tr>
						<tr>
							<th>性別</th>
							<td>${f:label(sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
						</tr>
						<tr>
							<th>生年月日</th>
							<td>${f:h(birthYear)}年${f:h(birthMonth)}月${f:h(birthDay)}日&nbsp;</td>
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
							<th>取得資格</th>
							<td>
								<gt:convertToQualificationName items="${qualification}"/>&nbsp;
								<c:if test="${not empty qualificationOther}">
									${f:h(qualificationOther)}
								</c:if>
							</td>
						</tr>
						<tr>
							<th>飲食業界経験年数</th>
							<td>${f:label(foodExpKbn, foodExpKbnList, 'value', 'label')}&nbsp;</td>
						</tr>
						<tr>
							<th>海外勤務経験</th>
							<td>${f:label(foreignWorkFlg, foreignWorkFlgList, 'value', 'label')}&nbsp;</td>
						</tr>
						<tr>
							<th>スカウト用自己PR</th>
							<td>${f:br(f:h(scoutSelfPr))}&nbsp;</td>
						</tr>
						<tr>
							<th class="bdrs_bottom">応募用自己PR</th>
							<td class="bdrs_bottom">${f:br(f:h(applicationSelfPr))}&nbsp;</td>
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
							<td class="bdrs_bottom">${f:label(mailMagazineReceptionFlg, mailMagazineReceptionFlgList, 'value', 'label')}&nbsp;</td>
						</tr>
					</table>
					<hr />

					<h3 title="スカウトメール設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_scoutMailSet.gif" alt="スカウトメール設定" /></h3>
					<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
						<tr>
							<th width="150" class="bdrs_bottom">スカウトメール</th>
							<td class="bdrs_bottom">${f:label(scoutMailReceptionFlg, scoutMailReceptionFlgList, 'value', 'label')}&nbsp;</td>
						</tr>
					</table>
					<hr />

					<h3 title="希望条件" class="title"><img src="${ADMIN_CONTENS}/images/member/title_term.gif" alt="希望条件" /></h3>
					<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
						<tr>
							<th width="150">希望職種</th>
							<td>
								<gt:convertToJobName items="${job}"/>
								&nbsp;
							</td>
						</tr>
						<tr>
							<th>希望業種</th>
							<td>
								<gt:convertToIndustryName items="${industry}"/>
								&nbsp;
							</td>
						</tr>
						<tr>
							<th>希望勤務地</th>
							<td>
								<c:forEach items="${hopeCityListMap}" var="pref" varStatus="pStatus">
									${f:label(pref.key, prefecturesList, 'value', 'label')}
									<gt:cityList name="cityList" prefecturesCd="${pref.key}" />
									<c:forEach items="${pref.value}" var="city" varStatus="cStatus">
										<c:if test="${cStatus.first}"> - </c:if>
										${f:label(city, cityList, 'value', 'label')}
										<c:if test="${!cStatus.last}">、</c:if>
									</c:forEach>
									<c:if test="${!pStatus.last}"><br></c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<th>希望雇用形態</th>
							<td>
								<c:forEach items="${employPtnKbns}" var="employPtn" varStatus="status">
									${f:label(employPtn, employPtnKbnList, 'value', 'label')}
									<c:if test="${!status.last}">,&nbsp;</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<th>転勤</th>
							<td>${f:label(transferFlg, transferFlgList, 'value', 'label')}&nbsp;</td>
						</tr>
						<tr>
							<th>深夜勤務</th>
							<td>${f:label(midnightShiftFlg, midnightShiftFlgList, 'value', 'label')}&nbsp;</td>
						</tr>
						<tr>
							<th class="bdrs_bottom">希望年収</th>
							<td class="bdrs_bottom">${f:label(salaryKbn, salaryKbnList, 'value', 'label')}&nbsp;</td>
						</tr>
					</table>
					<hr />

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
								<gt:convertToIndustryName items="${dto.industry}"/>
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
							<th><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
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

					<h3 title="ジャスキル設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_juskill.gif" alt="ジャスキル設定" /></h3>
					<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
						<tr>
							<th width="150">ジャスキル登録</th>
							<td>
								<c:choose>
									<c:when test="${entryJuskillFlg eq 1}">
										<label class="entryJuskill">&nbsp;ジャスキル登録を行う</label>
									</c:when>
									<c:otherwise>
										${f:label(juskillFlg, juskillFlgList, 'value', 'label')}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th class="bdrs_bottom">転職相談窓口からの求人情報</th>
							<td class="bdrs_bottom">${f:label(juskillContactFlg, juskillContactFlgList, 'value', 'label')}&nbsp;</td>
						</tr>
					</table>
					<hr />

					<h3 title="管理者設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_admin.gif" alt="管理者設定" /></h3>
					<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
						<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
						<tr>
							<th width="150">管理者用パスワード&nbsp;</th>
							<td>${f:h(adminPassword)}</td>
						</tr>
						</c:if>
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
				</div>

				<c:if test="${advanceRegistrationFlg eq true && pageKbn eq PAGE_DETAIL}">
				<div id="gousetsu">
					<div id="accordion">
					<c:forEach var="advancedInfoDto" items="${advancedRegistrationList}" varStatus="status">
					<c:set var="advancedMemberInfoDto" value="${advancedRegistrationDtoMemberMap[advancedInfoDto.id]}" />
							<h4 class="cmn_table detail_table">${f:h(advancedInfoDto.advancedRegistrationName)}（募集開始日時：<fmt:formatDate value="${advancedInfoDto.termStartDatetime}" pattern="yyyy/MM/dd HH:mm" />）</h4>
							<div>
								<h3 title="基本情報" class="title"><img src="${ADMIN_CONTENS}/images/member/title_basicData.gif" alt="基本情報" /></h3>
								<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
									<tr>
										<th width="150">登録ID</th>
										<td>${f:h(advancedMemberInfoDto.id)}&nbsp;</td>
									</tr>
									<tr>
										<th width="150">来場ステータス</th>
										<td>${f:h(f:label(advancedMemberInfoDto.attendedStatus, attendedStatusList, 'value', 'label'))}&nbsp;</td>
									</tr>
									<tr>
										<th>登録日時</th>
										<td>${f:h(advancedMemberInfoDto.registrationDatetime)}&nbsp;</td>
									</tr>
									<tr>
										<th>最終ログイン日時</th>
										<td>${f:h(advancedMemberInfoDto.lastLoginDatetime)}&nbsp;</td>
									</tr>
									<tr>
										<th>氏名(漢字)</th>
										<td>${f:h(advancedMemberInfoDto.memberName)}&nbsp;</td>
									</tr>
									<tr>
										<th>氏名(カナ)</th>
										<td>${f:h(advancedMemberInfoDto.memberNameKana)}&nbsp;</td>
									</tr>

									<c:choose>
										<c:when test="${advancedMemberInfoDto.terminalKbn eq '1'}">
											<tr>
												<th>ログインメールアドレス<br />
													（PCメールアドレス）</th>
												<td>${f:h(advancedMemberInfoDto.loginId)}&nbsp;</td>
											</tr>
											<tr>
												<th>携帯メールアドレス</th>
												<td>${f:h(advancedMemberInfoDto.mobileMail)}&nbsp;</td>
											</tr>
										</c:when>
										<c:when test="${advancedMemberInfoDto.terminalKbn eq '2'}">
											<tr>
												<th>ログインメールアドレス<br />
													（携帯メールアドレス）</th>
												<td>${f:h(advancedMemberInfoDto.loginId)}&nbsp;</td>
											</tr>
											<tr>
												<th>PCメールアドレス</th>
												<td>${f:h(advancedMemberInfoDto.pcMail)}&nbsp;</td>
											</tr>
										</c:when>

									</c:choose>

									<tr>
										<th>性別</th>
										<td>${f:label(advancedMemberInfoDto.sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
									</tr>

									<tr>
										<th>生年月日</th>
										<td>${f:h(advancedMemberInfoDto.birthday)}&nbsp;</td>
									</tr>
									<tr>
										<th>電話番号</th>
										<td>${f:h(advancedMemberInfoDto.phoneNo)}&nbsp;</td>
									</tr>
									<tr>
										<th>郵便番号</th>
										<td>${f:h(advancedMemberInfoDto.zipCd)}&nbsp;</td>
									</tr>
									<tr>
										<th>住所1</th>
										<td>${f:label(advancedMemberInfoDto.prefecturesCd, prefecturesList, 'value', 'label')}${f:h(advancedMemberInfoDto.municipality)}&nbsp;</td>
									</tr>
									<tr>
										<th>住所2</th>
										<td>${f:h(advancedMemberInfoDto.address)}&nbsp;</td>
									</tr>
									<tr>
										<th>経歴や資格など、PRがあればご自由にご記入ください。</th>
										<td>${f:br(f:h(advancedMemberInfoDto.advancedRegistrationSelfPr))}&nbsp;</td>
									</tr>
									<tr>
										<th>転職先に望むことがあればご自由にご記入ください。</th>
										<td>${f:br(f:h(advancedMemberInfoDto.hopeCareerChangeText))}&nbsp;</td>
									</tr>
									<tr>
										<th class="bdrs_bottom">現在&#40;前職&#41;の年収</th>
										<td class="bdrs_bottom">${f:h(advancedMemberInfoDto.workSalary)}&nbsp;</td>
									</tr>
								</table>
								<hr>

								<h3 title="現在の状況" class="title"><img src="${ADMIN_CONTENS}/images/member/title_currentSituation.gif" alt="現在の状況" /></h3>
								<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
									<tr>
										<th class="bdrs_bottom" width="150">現在の状況</th>
										<td class="bdrs_bottom">
											<gt:typeList name="currentKbnList" typeCd="<%=MTypeConstants.CurrentSituationKbn.TYPE_CD %>"/>
											${f:label(advancedMemberInfoDto.currentSituationKbn, currentKbnList, 'value', 'label') }
										</td>
									</tr>
								</table>

								<h3 title="登録状況" class="title"><img src="${ADMIN_CONTENS}/images/member/title_circumstance.gif" alt="登録状況" /></h3>
								<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
									<tr>
										<th class="bdrs_bottom" width="150">事前登録</th>
										<td class="bdrs_bottom">
											<gt:typeList name="advancedKbnList" typeCd="<%=MTypeConstants.AdvancedRegistrationStatusKbn.TYPE_CD %>"/>
											${f:label(advancedMemberInfoDto.advancedRegistrationKbn, advancedKbnList, 'value', 'label')}
										</td>
									</tr>
								</table>
								<hr>

								<h3 title="各メール設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_mailSet.gif" alt="各メール設定" /></h3>
								<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
									<tr>
										<th width="150" class="bdrs_bottom">メルマガ</th>
										<td class="bdrs_bottom">${f:label(advancedMemberInfoDto.advancedMailMagazineReceptionFlg, advancedMailMagazineReceptionFlgList, 'value', 'label')}&nbsp;</td>
									</tr>
								</table>
								<hr>

								<h3 title="希望条件" class="title"><img src="${ADMIN_CONTENS}/images/member/title_termsDesired.gif" alt="希望条件" /></h3>
								<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
									<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
									<tr>
										<th width="150">希望する業態</th>
										<td>
											<c:forEach items="${advancedMemberInfoDto.industryKbnList}" var="t" varStatus="status">
												${f:label(t, industryList, 'value', 'label')}
												<c:if test="${status.last eq false}">
													,&nbsp;
												</c:if>
											</c:forEach>
										</td>
									</tr>
									<tr>
										<th width="150">希望する職種・ポジション</th>
										<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>
										<td>
											<c:forEach items="${advancedMemberInfoDto.jobKbnList}" var="t" varStatus="status">
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
											${f:h(advancedMemberInfoDto.hopeCareerChangeStr)}
										</td>
									</tr>
								</table>

								<h3 title="最終学歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_school.gif" alt="最終学歴" /></h3>
								<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
									<tr>
										<th width="150">学校名</th>
										<td>${f:h(advancedMemberInfoDto.schoolName)}&nbsp;</td>
									</tr>
									<tr>
										<th class="bdrs_bottom">状況</th>
										<td class="bdrs_bottom">${f:label(advancedMemberInfoDto.graduationKbn, graduationKbnList, 'value', 'label')}&nbsp;</td>
									</tr>
								</table>

								<h3 title="職務経歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_work.gif" alt="職務経歴" /></h3>
								<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">

								<c:forEach var="dto" items="${advancedMemberInfoDto.careerList}" varStatus="status">

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
										<td><gt:convertToJobName items="${dto.job}"/>&nbsp;</td>
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
										<td>${f:label(advancedMemberInfoDto.pcMailStopFlg, pcMailStopFlgList, 'value', 'label')}&nbsp;</td>
									</tr>
									<tr>
										<th width="150">モバイルメール配信</th>
										<td>${f:label(advancedMemberInfoDto.mobileMailStopFlg, mobileMailStopFlgList, 'value', 'label')}&nbsp;</td>
									</tr>
									<tr>
										<th class="bdrs_bottom">会員区分</th>
										<td class="bdrs_bottom">${f:label(advancedMemberInfoDto.memberKbn, memberKbnList, 'value', 'label')}&nbsp;</td>
									</tr>
								</table>
								<hr />

							</div>

						</c:forEach>
					</div>
				</div>
				</c:if>

				<c:if test="${juskillInfoFlg eq true && pageKbn eq PAGE_DETAIL}">
				<div id="juskill">
					<h3 title="基本情報" class="title"><img src="${ADMIN_CONTENS}/images/member/title_basicData.gif" alt="基本情報" /></h3>
					<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
						<tr>
							<th width="150">ID</th>
							<td>${f:h(juskillMemberForm.id)}&nbsp;</td>
						</tr>
						<tr>
							<th width="150">ジャスキルNo</th>
							<td>${f:h(juskillMemberForm.juskillMemberNo)}&nbsp;</td>
						</tr>
						<tr>
							<th>ジャスキルデータ移行会員</th>
							<td>${f:label(juskillMemberForm.juskillMigrationFlg, juskillMigrationFlgList, 'value', 'label')}&nbsp;</td>
						</tr>
						<tr>
							<th>登録日</th>
							<td>
								${f:h(juskillMemberForm.juskillEntryDate)}&nbsp;
							</td>
						</tr>
						<tr>
							<th>氏名</th>
							<td>${f:h(juskillMemberForm.juskillMemberName)}&nbsp;</td>
						</tr>
						<tr>
							<th>性別</th>
							<td>${f:label(juskillMemberForm.sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
						</tr>

						<tr>
							<th>生年月日</th>
							<td>
								<c:if test="${not empty birthYear}">
									${f:h(juskillMemberForm.birthYear)}年${f:h(juskillMemberForm.birthMonth)}月${f:h(juskillMemberForm.birthDate)}日 (${f:h(juskillMemberForm.age)}歳)&nbsp;
								</c:if>
							</td>
						</tr>
						<tr>
							<th>メール希望</th>
							<td>${f:h(juskillMemberForm.mailHope)}&nbsp;</td>
						<tr>
						<tr>
							<th>転職時期</th>
							<td>${f:h(juskillMemberForm.transferTiming)}&nbsp;</td>
						<tr>
						<tr>
							<th>希望業態</th>
							<td>${f:h(juskillMemberForm.hopeIndustry)}&nbsp;</td>
						<tr>
						<tr>
							<th>希望職種</th>
							<td>${f:h(juskillMemberForm.hopeJob)}&nbsp;</td>
						<tr>
						<tr>
							<th>経験</th>
							<td>${f:h(juskillMemberForm.experience)}&nbsp;</td>
						<tr>
						<tr>
							<th>希望年収</th>
							<td>${f:h(juskillMemberForm.hopeSalary)}&nbsp;</td>
						<tr>
						<tr>
							<th>郵便番号</th>
							<td>${f:h(juskillMemberForm.zipCd)}&nbsp;</td>
						<tr>
						<tr>
							<th>住所1</th>
							<td>${f:label(juskillMemberForm.prefecturesCd, prefecturesList, 'value', 'label')}${f:h(juskillMemberForm.address)}&nbsp;</td>
						</tr>
						<tr>
							<th>ビル名</th>
							<td>${f:h(juskillMemberForm.buildingName)}&nbsp;</td>
						<tr>
						<tr>
							<th>路線</th>
							<td>${f:h(juskillMemberForm.route)}&nbsp;</td>
						<tr>
						<tr>
							<th>最寄駅</th>
							<td>${f:h(juskillMemberForm.closestStation)}&nbsp;</td>
						<tr>
						<tr>
							<th>電話1</th>
							<td>${f:h(juskillMemberForm.phoneNo1)}&nbsp;</td>
						<tr>
						<tr>
							<th>電話2</th>
							<td>${f:h(juskillMemberForm.phoneNo2)}&nbsp;</td>
						<tr>
						<tr>
							<th>連絡先3（ＰＣメール）</th>
							<td>${f:h(juskillMemberForm.pcMail)}&nbsp;</td>
						<tr>
						<tr>
							<th>メール</th>
							<td>${f:h(juskillMemberForm.mail)}&nbsp;</td>
						<tr>
						<tr>
							<th>PW</th>
							<td>${f:h(juskillMemberForm.pw)}&nbsp;</td>
						<tr>
						<tr>
							<th>最終学歴</th>
							<td>${f:h(juskillMemberForm.finalSchoolHistory)}&nbsp;</td>
						<tr>
						<tr>
							<th>学歴備考</th>
							<td>${f:br(f:h(juskillMemberForm.schoolHistoryNote))}&nbsp;</td>
						<tr>
						<tr>
							<th>最終職種</th>
							<td>${f:h(juskillMemberForm.finalCareerHistory)}&nbsp;</td>
						<tr>
						<tr>
							<th>希望職種2</th>
							<td>${f:h(juskillMemberForm.hopeJob2)}&nbsp;</td>
						<tr>
						<tr>
							<th>職歴</th>
							<td>
								<c:if test="${not empty juskillMemberForm.careerHistoryList}">
									<c:forEach var="career" items="${juskillMemberForm.careerHistoryList}" varStatus="status">
										<div <c:if test="${!status.last}">style="margin-bottom:5px"</c:if>>
										<fmt:formatNumber value="${status.count}" pattern="0" />：
										${f:h(career)}
										</div>
									</c:forEach>
								</c:if>
							</td>
						</tr>
						<tr>
							<th>取得資格</th>
							<td>${f:h(juskillMemberForm.qualification)}&nbsp;</td>
						<tr>
						<tr>
							<th>登録経路</th>
							<td>${f:h(juskillMemberForm.entryPath)}&nbsp;</td>
						<tr>
						<tr>
							<th class="bdrs_bottom">特記事項</th>
							<td class="bdrs_bottom">${f:br(f:h(juskillMemberForm.notice))}&nbsp;</td>
						<tr>
					</table>
				</div>
				</c:if>
			</div>
			<hr />

			<div class="wrap_btn">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:when test="${pageKbn eq PAGE_DETAIL}">
						<input type="button" name="edit" value="編 集" onclick="location.href='${f:url(editPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
						<input type="button" name="delete" value="削 除" onclick="location.href='${f:url(deletePath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:when test="${pageKbn eq PAGE_DELETE}">
						<html:hidden property="id"/>
						<html:hidden property="deleteVersion" value="${f:h(version)}"/>
						<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
						<html:submit property="submit" value="削 除" onclick="if(!confirm('削除してもよろしいですか?')) {return false;};" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
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
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="org.apache.commons.lang3.StringUtils" %>

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
<gt:typeList name="pcMailStopFlgList" typeCd="<%=MTypeConstants.PcMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="mobileMailStopFlgList" typeCd="<%=MTypeConstants.MobileMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>"  />
<gt:typeList name="memberKbnList" typeCd="<%=MTypeConstants.MemberKbn.TYPE_CD %>" />
<gt:areaList name="areaCdList" />
<gt:allArea name="allAreaList"/>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
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
					<th width="150">会員ID</th>
					<td>${f:h(id)}&nbsp;</td>
				</tr>
				<tr>
					<th>希望地域</th>
					<td>
						<% // 希望地域　カンマ区切りで表示 %>
						<c:forEach var="area" items="${areaList}" varStatus="status">
							<c:if test="${status.index ge 1}">,&nbsp;</c:if>
								${f:label(area, areaCdList, 'value', 'label')}
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>登録日時</th>
					<td>${f:h(insertDatatime)}&nbsp;</td>
				</tr>
				<c:if test="${pageKbn eq PAGE_DETAIL}">
					<gt:typeList name="mRegisterFlgList" typeCd="<%=MTypeConstants.MemberRegisteredFlg.TYPE_CD %>" />
					<tr>
						<th>登録状況</th>
						<td>${f:h(f:label(memberRegisteredFlg, mRegisterFlgList, 'value', 'label'))}</td>
					</tr>
				</c:if>
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
						<gt:convertToQualificationName items="${qualification}"/>
						&nbsp;
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
					<th>応募用自己PR</th>
					<td>${f:br(f:h(applicationSelfPr))}&nbsp;</td>
				</tr>

				<c:choose>
					<c:when test="${StringUtils.isNotBlank(registrationUrl)}">
						<tr>
							<th>事前登録用自己PR</th>
							<td>${f:br(f:h(advancedRegistrationSelfPr))}</td>
						</tr>
						<tr>
							<th class="bdrs_bottom">本登録URL</th>
							<td class="bdrs_bottom"><a href="${registrationUrl}" target="_blank">${f:h(registrationUrl)}</a>&nbsp;</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<th class="bdrs_bottom">事前登録用自己PR</th>
							<td class="bdrs_bottom">${f:br(f:h(advancedRegistrationSelfPr))}</td>
						</tr>
					</c:otherwise>
				</c:choose>
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
						<c:forEach items="${workLocation}" var="t" varStatus="status">
							<c:if test="${status.index ge 1}">,&nbsp;</c:if>
							${f:label(t, allAreaList, 'value', 'label')}
						</c:forEach>
						&nbsp;
					</td>
				</tr>
				<tr>
					<th>希望雇用形態</th>
					<td>${f:label(employPtnKbn, employPtnKbnList, 'value', 'label')}&nbsp;</td>
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


			<hr />

			<div class="wrap_btn">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:when test="${pageKbn eq PAGE_DETAIL}">
						<c:if test="${isEditable}">
							<input type="button" name="edit" value="編 集" onclick="location.href='${f:url(editPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
							<input type="button" name="signIn" value="承 認 " onclick="location.href='${f:url(signInPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
<!-- 						<input type="button" name="delete" value="削 除" onclick="deleteTempMember();" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  /> -->
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

<c:if test="${pageKbn eq PAGE_DETAIL}">
	<script type="text/javascript">
		var deleteTempMember = function() {
			if (confirm('削除してもよろしいですか?')) {
				var form = $("#deleteForm");
				form.submit();
			} else {
				// nothing to do
			}
		};
	</script>

	<s:form styleId="deleteForm" action="${actionPath}">
		<html:hidden property="id" />
		<html:hidden property="delete" value="delete"/>
	</s:form>
</c:if>
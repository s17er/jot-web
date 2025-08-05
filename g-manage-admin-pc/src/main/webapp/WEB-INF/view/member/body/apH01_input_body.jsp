<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>

<gt:prefecturesList name="prefecturesList" />
<gt:birthYearList name="birthYearList" blankLineLabel="--" />
<gt:birthMonthList name="birthMonthList" blankLineLabel="--" />
<gt:birthDayList name="birthDayList" blankLineLabel="--" />
<gt:typeList name="domainKbnList" typeCd="<%=MTypeConstants.MobileDomainKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:typeList name="qualificationKbnList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"  />
<gt:typeList name="foodExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:typeList name="foreignWorkFlgList" typeCd="<%=MTypeConstants.ForeignWorkFlg.TYPE_CD %>"   />
<gt:typeList name="jobInfoReceptionFlgList" typeCd="<%=MTypeConstants.JobInfoReceptionFlg.TYPE_CD %>"   />
<gt:typeList name="mailMagazineReceptionFlgList" typeCd="<%=MTypeConstants.MailMagazineReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="scoutMailReceptionFlgList" typeCd="<%=MTypeConstants.ScoutMailReceptionFlg.TYPE_CD %>"  />
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="transferFlgList" typeCd="<%=MTypeConstants.TransferFlg.TYPE_CD %>" />
<gt:typeList name="midnightShiftFlgList" typeCd="<%=MTypeConstants.MidnightShiftFlg.TYPE_CD %>"  />
<gt:typeList name="salaryKbnList" typeCd="<%=MTypeConstants.SalaryKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:typeList name="jobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"  />
<gt:typeList name="industryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"  />
<gt:typeList name="juskillFlgList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>" />
<gt:typeList name="juskillContactFlgList" typeCd="<%=MTypeConstants.JuskillContactFlg.TYPE_CD %>" />
<gt:typeList name="pcMailStopFlgList" typeCd="<%=MTypeConstants.PcMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="mobileMailStopFlgList" typeCd="<%=MTypeConstants.MobileMailStopFlg.TYPE_CD %>"  />
<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:typeList name="memberKbnList" typeCd="<%=MTypeConstants.MemberKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:areaList name="areaCdList" />
<gt:typeList name="gourmetMagazineReceptionList" typeCd="<%=MTypeConstants.gourmetMagazineReceptionFlg.TYPE_CD %>"/>
<gt:typeList name="deliveryStatusList" typeCd="<%=MTypeConstants.deliveryStatus.TYPE_CD %>"/>

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
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">

			<html:hidden property="hiddenId" />

			<c:if test="${existDataFlg eq true}">

			<h3 title="基本情報" class="title"><img src="${ADMIN_CONTENS}/images/member/title_basicData.gif" alt="基本情報" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">会員ID</th>
					<td>${f:h(id)}&nbsp;</td>
				</tr>
				<c:if test="${gourmetMagazineReceptionDisplayFlg}">
					<tr>
						<th>プレゼント希望</th>
						<td class="release">
							<html:select property="gourmetMagazineReceptionFlg"  >
								<html:optionsCollection name="gourmetMagazineReceptionList"/>
							</html:select>&nbsp;
							発送状況 :
							<c:forEach items="${deliveryStatusList}" var="v">
								<html:radio property="deliveryStatus" value="${v.value}" styleId="deliveryStatusList_${v.value}" styleClass="release"  />
								<label for="deliveryStatusList_${v.value}">${f:h(v.label)}</label>
							</c:forEach>
						</td>
					</tr>
				</c:if>
				<tr>
					<th>登録日時</th>
					<td>${f:h(insertDatatime)}&nbsp;</td>
				</tr>
				<tr>
					<th>最終ログイン日時</th>
					<td>${f:h(lastLoginDatetime)}&nbsp;</td>
				</tr>
				<tr>
					<th>氏名(漢字)&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="memberName"  /></td>
				</tr>
				<tr>
					<th>氏名(カナ)&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="memberNameKana" styleClass="active"  /></td>
				</tr>
				<tr>
					<th>ログインメールアドレス</th>
					<td>${f:h(loginId)}&nbsp;</td>
				</tr>
				<tr>
					<th>パスワード</th>
					<td>
						<span class="explain">▼任意のパスワードを入力して下さい。</span><br />
						<html:password property="password" styleClass="disabled" /><br />
						<span class="explain">▼確認のため再度入力して下さい。</span><br />
						<html:password property="rePassword" styleClass="disabled" /><br />
						<span class="attention">※半角英数字（0～9、A～Z）、6～20文字で入力して下さい。</span>
					</td>
				</tr>
				<tr>
					<th>サブメールアドレス</th>
					<td>
						<html:text property="subMailAddress" styleClass="disabled" />
						<span class="attention">※必ず半角英数字で入力して下さい。</span>
					</td>
				</tr>
				<tr>
					<th>性別&nbsp;</th>
					<td class="release">
						<c:forEach items="${sexKbnList}" var="t">
							<html:radio property="sexKbn" value="${t.value}" styleId="sexKbn_${t.value}" styleClass="release"  />
							<label for="sexKbn_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>生年月日&nbsp;<span class="attention">※必須</span></th>
					<td>
						<html:select property="birthYear"  >
							<html:optionsCollection name="birthYearList"/>
						</html:select>&nbsp;年
						<html:select property="birthMonth"  >
							<html:optionsCollection name="birthMonthList"/>
						</html:select>&nbsp;月
						<html:select property="birthDay"  >
							<html:optionsCollection name="birthDayList"/>
						</html:select>&nbsp;日
					</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td class="release">
						<html:text property="phoneNo1" size="5" styleClass="disabled" />
						&nbsp;-&nbsp;
						<html:text property="phoneNo2" size="5" styleClass="disabled" />
						&nbsp;-&nbsp;
						<html:text property="phoneNo3" size="5" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>郵便番号</th>
					<td class="release">
						<html:text property="zipCd"  styleClass="disabled" size="6"  />
						<span class="zipCd">例：123-1234</span>
					</td>
				</tr>
				<tr>
					<th>住所1&nbsp;<span class="attention">※必須</span></th>
					<td>
						<html:select property="prefecturesCd"  >
							<option value="" >--</option>
							<html:optionsCollection name="prefecturesList"/>
						</html:select><br />
						<span class="explain">▼市区町村まで入力</span><br />
						<html:text property="municipality" /><br />
						<span class="explain">(例)大阪市北区</span>
					</td>
				</tr>
				<tr>
					<th>住所2</th>
					<td>
						<span class="explain">▼町名番地・ビル名を入力</span><br />
						<html:text property="address" /><br />
						<span class="explain">(例)太融寺町8-8 日進ビル6階</span>
					</td>
				</tr>
				<tr>
					<th>取得資格</th>
					<td class="release">
						<div class="autoDispWrap4">
							<c:forEach items="${qualificationKbnList}" var="t">
								<span>
									<html:multibox property="qualification" value="${f:h(t.value)}" styleId="qualificationKbn_${f:h(t.value)}" />
									<label for="qualificationKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
								</span>
							</c:forEach>
						</div>
						<html:text property="qualificationOther" />
					</td>
				</tr>
				<tr>
					<th>飲食業界経験年数<br />(アルバイト含む)</th>
					<td>
						<html:select property="foodExpKbn"  >
							<html:optionsCollection name="foodExpKbnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>海外勤務経験</th>
					<td class="release">
						<c:forEach items="${foreignWorkFlgList}" var="t">
							<html:radio property="foreignWorkFlg" value="${t.value}" styleId="foreignWorkFlg_${t.value}" styleClass="release"  />
							<label for="foreignWorkFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>スカウト用自己PR
					</th>
					<td><html:textarea property="scoutSelfPr" cols="60" rows="10"></html:textarea></td>
				</tr>
				<tr>
					<th class="bdrs_bottom">応募用自己PR
					</th>
					<td class="bdrs_bottom"><html:textarea property="applicationSelfPr" cols="60" rows="10"></html:textarea></td>

				</tr>
			</table>
			<hr />

			<h3 title="各メール設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_mailSet.gif" alt="メール設定" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
			<%--
				<tr>
					<th width="150">新着求人情報</th>
					<td class="release">
						<c:forEach items="${jobInfoReceptionFlgList}" var="t">
							<html:radio property="jobInfoReceptionFlg" value="${t.value}" styleId="jobInfoReceptionFlg_${t.value}" styleClass="release"  />
							<label for="jobInfoReceptionFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
			--%>
				<tr>
					<th  width="150" class="bdrs_bottom">メルマガ&nbsp;<span class="attention">※必須</span></th>
					<td class="release bdrs_bottom">
						<c:forEach items="${mailMagazineReceptionFlgList}" var="t">
							<html:radio property="mailMagazineReceptionFlg" value="${t.value}" styleId="mailMagazineReceptionFlg_${t.value}" styleClass="release"  />
							<label for="mailMagazineReceptionFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
			</table>
			<hr />

			<h3 title="スカウトメール設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_scoutMailSet.gif" alt="スカウトメール設定" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150" class="bdrs_bottom">スカウトメール&nbsp;<span class="attention">※必須</span></th>
					<td class="release bdrs_bottom">
						<c:forEach items="${scoutMailReceptionFlgList}" var="t">
							<html:radio property="scoutMailReceptionFlg" value="${t.value}" styleId="scoutMailReceptionFlg_${t.value}" styleClass="release"  />
							<label for="scoutMailReceptionFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
			</table>
			<hr />

			<h3 title="希望条件" class="title"><img src="${ADMIN_CONTENS}/images/member/title_term.gif" alt="希望条件" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">希望職種<br /></th>
					<td class="release">
						<table cellpadding="0" cellspacing="0" border="0" class="select_table">
							<tr>
								<td  class="release">
								<div class="autoDispWrap3">
									<c:forEach items="${jobKbnList}" var="t">
									<span>
										<html:multibox property="job" value="${f:h(t.value)}" styleId="jobKbn_${f:h(t.value)}" />
										<label for="jobKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
									</span>
								</c:forEach>
								</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th>希望業種<br /></th>
					<td class="release">
						<table cellpadding="0" cellspacing="0" border="0" class="select_table">
							<tr>
								<td  class="release">
								<div class="autoDispWrap4">
									<c:forEach items="${industryKbnList}" var="t">
									<span>
										<html:multibox property="industry" value="${f:h(t.value)}" styleId="industryKbn_${f:h(t.value)}" />
										<label for="industryKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
									</span>
								</c:forEach>
								</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th>希望勤務地</th>
					<td  class="release">
						<table cellpadding="0" cellspacing="0" class="select_table">
						<c:forEach items="${prefecturesList}" var="pref" varStatus="pStatus">
							<c:if test="${!pStatus.last}">
							<tr>
								<th style="border-bottom:1px solid #AAA;">${f:h(pref.label)}</th>
								<td style="border-bottom:1px solid #AAA;">
									<gt:cityList name="cityList" prefecturesCd="${pref.value}"/>
									<div class="autoDispWrap4">
										<c:forEach items="${cityList}" var="t" varStatus="cStatus">
											<span>
												<html:multibox property="hopeCityCdList" value="${f:h(t.value)}" styleId="hopeCityCd_${f:h(t.value)}" />
												<label for="hopeCityCd_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
											</span>
										</c:forEach>
									</div>
								</td>
							</tr>
							</c:if>
						</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<th>希望雇用形態&nbsp;</th>
					<td  class="release">
						<div class="autoDispWrap3">
							<c:forEach items="${employPtnKbnList}" var="t" >
							<span>
								<html:multibox property="employPtnKbns" value="${f:h(t.value)}" styleId="employPtnKbn_${f:h(t.value)}" />
								<label for="employPtnKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
							</span>
						</c:forEach>
						</div>
					</td>
				</tr>
				<tr>
					<th>転勤</th>
					<td class="release">
						<c:forEach items="${transferFlgList}" var="t">
							<html:radio property="transferFlg" value="${t.value}" styleId="transferFlg_${t.value}" styleClass="release"  />
							<label for="transferFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>深夜勤務</th>
					<td class="release">
						<c:forEach items="${midnightShiftFlgList}" var="t">
							<html:radio property="midnightShiftFlg" value="${t.value}" styleId="midnightShiftFlg_${t.value}" styleClass="release"  />
							<label for="midnightShiftFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">希望年収</th>
					<td class="bdrs_bottom">
						<html:select property="salaryKbn"  >
							<html:optionsCollection name="salaryKbnList"/>
						</html:select>
					</td>
				</tr>
			</table>
			<hr />

			<h3 title="最終学歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_school.gif" alt="最終学歴" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">学校名&nbsp;<br />
					<span class="attention">※学歴を入力する場合、必須</span></th>
					<td><html:text property="schoolName" /></td>
				</tr>
				<tr>
					<th>学部・学科</th>
					<td><html:text property="department" /></td>
				</tr>
				<tr>
					<th class="bdrs_bottom">状況</th>
					<td class="bdrs_bottom"><html:select property="graduationKbn"  >
							<html:optionsCollection name="graduationKbnList"/>
						</html:select>
					</td>
				</tr>
			</table>
			<hr />

			<h3 title="職務経歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_work.gif" alt="職務経歴" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">

				<c:forEach var="dto" items="${careerList}" varStatus="i">
				<tr>
					<th colspan="2" class="bdrs_right">職歴<fmt:formatNumber value="${i.index + 1}" pattern="0" /></th>
				</tr>
				<tr>
					<th width="150">会社名&nbsp;<br />
					<span class="attention">※職歴を入力する場合、必須</span></th>
					<td><html:text property="careerList[${i.index}].companyName" /></td>
				</tr>
				<tr>
					<th>在籍期間</th>
					<td><html:text property="careerList[${i.index}].workTerm"  /></td>
				</tr>
				<tr>
					<th>職種</th>
					<td  class="release">
						<div class="autoDispWrap3">
							<c:forEach items="${jobKbnList}" var="t" >
							<span>
								<html:multibox property="careerList[${i.index}].job" value="${f:h(t.value)}" styleId="careerJobKbn_${i.index}_${f:h(t.value)}" />
								<label for="careerJobKbn_${i.index}_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
							</span>
						</c:forEach>
						</div>
					</td>
				</tr>
				<tr>
					<th>業態</th>
					<td  class="release">
						<div class="autoDispWrap3">
							<c:forEach items="${industryKbnList}" var="t" >
							<span>
								<html:multibox property="careerList[${i.index}].industry" value="${f:h(t.value)}" styleId="careerIndustryKbn_${i.index}_${f:h(t.value)}" />
								<label for="careerIndustryKbn_${i.index}_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
							</span>
						</c:forEach>
						</div>
					</td>
				</tr>
				<tr>
					<th>業務内容</th>
					<td><html:textarea property="careerList[${i.index}].businessContent" cols="60" rows="10" ></html:textarea></td>
				</tr>
				<tr>
					<th>客席数・坪数</th>
					<td><html:text property="careerList[${i.index}].seat" /></td>
				</tr>

				<c:if test="${i.last ne true}">
					<tr>
						<th><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
						<td><html:text property="careerList[${i.index}].monthSales"  /></td>
					</tr>
				</c:if>
				<c:if test="${i.last}">
					<tr>
					<th class="bdrs_bottom"><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
					<td class="bdrs_bottom"><html:text property="careerList[${i.index}].monthSales"  /></td>
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
						${f:label(juskillFlg, juskillFlgList, 'value', 'label')}&nbsp;
						<c:if test="${juskillFlg eq 0 }">
							<html:multibox property="entryJuskillFlg" value="1" id="entryJuskillFlg"/>
							<label for="entryJuskillFlg">&nbsp;ジャスキル登録を行う</label>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">転職相談窓口からの求人情報</th>
					<td class="bdrs_bottom">
						<c:forEach items="${juskillContactFlgList}" var="t">
							<html:radio property="juskillContactFlg" value="${t.value}" styleId="juskillContactFlg_${t.value}" styleClass="release"  />
							<label for="juskillContactFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
			</table>
			<hr />

			<h3 title="管理者設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_admin.gif" alt="管理者設定" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
				<tr>
					<th width="150">管理者用パスワード&nbsp;</th>
					<td class="release"><html:text property="adminPassword" /></td>
				</tr>
				</c:if>
				<tr>
					<th width="150">PCメール配信&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<c:forEach items="${pcMailStopFlgList}" var="t">
							<html:radio property="pcMailStopFlg" value="${t.value}" styleId="pcMailStopFlg_${t.value}" styleClass="release"  />
							<label for="pcMailStopFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th width="150">モバイルメール配信&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<c:forEach items="${mobileMailStopFlgList}" var="t">
							<html:radio property="mobileMailStopFlg" value="${t.value}" styleId="mobileMailStopFlg_${t.value}" styleClass="release"  />
							<label for="mobileMailStopFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">会員区分&nbsp;<span class="attention">※必須</span></th>
					<td class="release bdrs_bottom">
						<html:select property="memberKbn">
							<html:optionsCollection name="memberKbnList"/>
						</html:select>
					</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
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

	<hr />

</div>
<!-- #main# -->

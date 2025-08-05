<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:typeList name="deleteReasonKbnList" typeCd="<%=MTypeConstants.DeleteReasonKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:prefecturesList name="prefecturesList" blankLineLabel="--" />
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
<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>" blankLineLabel="--"  />
<gt:typeList name="memberKbnList" typeCd="<%=MTypeConstants.MemberKbn.TYPE_CD %>" blankLineLabel="--" />
<gt:birthYearList name="birthYearList" blankLineLabel="--" />
<gt:birthMonthList name="birthMonthList" blankLineLabel="--" />
<gt:birthDayList name="birthDayList" blankLineLabel="--" />
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
                <h3 title="基本情報" class="title"><img src="${ADMIN_CONTENS}/images/member/title_basicData.gif" alt="基本情報" /></h3>
                <table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
                    <tr>
                        <th width="150">登録ID</th>
                        <td>${f:h(id)}&nbsp;</td>
                    </tr>
                    <tr>
                        <th width="150">来場ステータス</th>
                        <td>
                            <html:select property="attendedStatus">
                                <html:optionsCollection name="attendedStatusList" />
                            </html:select>
                        </td>
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
                        <th>氏名(漢字)&nbsp;<span class="attention">※必須</span></th>
                        <td><html:text property="memberName" /> &nbsp;</td>
                    </tr>
                    <tr>
                        <th>氏名(カナ)&nbsp;<span class="attention">※必須</span></th>
                        <td><html:text property="memberNameKana" />&nbsp;</td>
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
                        <th>性別&nbsp;<span class="attention">※必須</span></th>
                        <td class="release">
                            <div class="autoDispWrap2">
                                <c:forEach items="${sexKbnList}" var="t">
                                    <c:set var="styleId" value="sex_${t.value}" />
                                    <html:radio property="sexKbn" value="${t.value}" styleId="${styleId}" /><label for="${styleId}">${f:h(t.label)}</label>
                                </c:forEach>
                            </div>
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
                            <html:select property="birthDayDay"  >
                                <html:optionsCollection name="birthDayList"/>
                            </html:select>&nbsp;日
                        </td>
                    </tr>
                    <tr>
                        <th>電話番号</th>
                        <td class="release">
                            <html:text property="phoneNo1" size="5" />
                            -
                            <html:text property="phoneNo2" size="5" />
                            -
                            <html:text property="phoneNo3" size="5" />&nbsp;</td>
                    </tr>
                    <tr>
                        <th>郵便番号</th>
                        <td><html:text property="zipCd" />&nbsp;</td>
                    </tr>
                    <tr>
                        <th>住所1&nbsp;<span class="attention">※必須</span></th>
                        <td>
                            <html:select property="prefecturesCd">
                                <html:optionsCollection name="prefecturesList" />
                            </html:select><br />
                            <html:text property="municipality" />&nbsp;</td>
                    </tr>
                    <tr>
                        <th>住所2</th>
                        <td><html:text property="address" />&nbsp;</td>
                    </tr>
                    <tr>
                        <th>経歴や資格など、PRがあればご自由にご記入ください。</th>
                        <td><html:textarea property="advancedRegistrationSelfPr" />&nbsp;</td>
                    </tr>
                    <tr>
                        <th>転職先に望むことがあればご自由にご記入ください。</th>
                        <td><html:textarea property="hopeCareerChangeText" />&nbsp;</td>
                    </tr>
                    <tr>
                        <th>現在&#40;前職&#41;の年収</th>
                        <td><html:text property="workSalary" />&nbsp;</td>
                    </tr>
					<tr>
						<th class="bdrs_bottom">取得資格</th>
                        <td class="release bdrs_bottom">
                            <div class="autoDispWrap2">
								<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"/>
                                <c:forEach items="${qualificationList}" var="t">
                                    <c:set var="styleId" value="qualification_${t.value}" />
                                    <span>
                                        <html:multibox property="qualificationKbnList" value="${t.value}" styleId="${styleId}" />
                                        <label for="${styleId}">${f:h(t.label)}</label>
                                    </span>
                                </c:forEach>
                                <html:text property="qualificationOther" />&nbsp;
                            </div>
                        </td>
					</tr>
                </table>
                <hr />


                <h3 title="現在の状況" class="title"><img src="${ADMIN_CONTENS}/images/member/title_currentSituation.gif" alt="現在の状況" /></h3>
                <table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
                    <tr>
                        <th class="bdrs_bottom" width="150">
                            現在の状況&nbsp;
                        </th>
                        <td class="bdrs_bottom">
                            <gt:typeList name="currentKbnList" typeCd="<%=MTypeConstants.CurrentSituationKbn.TYPE_CD %>" blankLineLabel="--" />
                            <html:select property="currentSituationKbn">
                                <html:optionsCollection name="currentKbnList" />
                            </html:select>
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
                        <td class="release bdrs_bottom">
                            <div class="autoDispWrap2">
                                <c:forEach items="${advancedMailMagazineReceptionFlgList}" var="t">
                                    <c:set var="styleId" value="adMailMag_${t.value}" />
                                    <html:radio property="advancedMailMagazineReceptionFlg" value="${t.value}" styleId="${styleId}" />
                                    <label for="${styleId}">${f:h(t.label)}</label>
                                </c:forEach>
                            </div>
                        </td>
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
                        <td class="release">
                            <div class="autoDispWrap2">
                                <c:forEach items="${industryList}" var="t">
                                    <c:set var="styleId" value="industry_${t.value}" />
                                    <span>
                                        <html:multibox property="industryKbnList" value="${t.value}" styleId="${styleId}" />
                                        <label for="${styleId}">${f:h(t.label)}</label>
                                    </span>
                                </c:forEach>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th width="150" >希望する職種・ポジション</th>
                        <gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>
                        <td class="release">
                            <div class="autoDispWrap2">
                                <c:forEach items="${jobList}" var="t">
                                    <c:set var="styleId" value="job_${t.value}" />
                                    <span>
                                        <html:multibox property="jobKbnList" value="${t.value}" styleId="${styleId}" />
                                        <label for="${styleId}">${t.label}</label>
                                    </span>
                                </c:forEach>
                            </div>
                        </td>
                    </tr>
                    <tr>

                        <th width="150" class="bdrs_bottom">転職希望時期&nbsp;<span class="attention">※必須</span></th>
                        <td class="bdrs_bottom release">
                            <html:text property="hopeCareerChangeYear" styleClass="yearText"/>年 <html:text property="hopeCareerChangeMonth" styleClass="monthText" />月

                            <gt:typeList name="hopeList" typeCd="<%=MTypeConstants.HopeCareerChangeTerm.TYPE_CD %>"/>
                            <c:forEach items="${hopeList}" var="t">
                                <html:multibox property="hopeCareerTermList" value="${t.value}" styleId="hope_${t.value}" styleClass="checkClass"/>
                                <label for="hope_${t.value}"> ${f:h(t.label)}</label>
                            </c:forEach>
                            <html:text property="hopeCareerChangeTermOther" />
                        </td>
                    </tr>
                </table>


                <h3 title="最終学歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_school.gif" alt="最終学歴" /></h3>
                <table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
                    <tr>
                        <th width="150">学校名</th>
                        <td><html:text property="schoolName" />&nbsp;</td>
                    </tr>
					<tr>
						<th>学部・学科</th>
						<td><html:text property="department" />&nbsp;</td>
					</tr>
                    <tr>
                        <th class="bdrs_bottom">状況</th>
                        <td class="bdrs_bottom">
                            <html:select property="graduationKbn">
                                <html:optionsCollection name="graduationKbnList" />
                            </html:select>
                        </td>
                    </tr>
                </table>


                <h3 title="職務経歴" class="title"><img src="${ADMIN_CONTENS}/images/member/title_work.gif" alt="職務経歴" /></h3>
                <table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">

                    <c:forEach var="dto" items="${careerList}" varStatus="status">
                        <c:set var="i" value="${status.index}" />
                        <tr>
                            <th colspan="2" class="bdrs_right">職歴<fmt:formatNumber value="${status.index + 1}" pattern="0" /></th>
                        </tr>

                        <tr>
                            <th width="150">会社名</th>
                            <td><html:text property="careerList[${i}].companyName" />&nbsp;</td>
                        </tr>
                        <tr>
                            <th>在籍期間</th>
                            <td><html:text property="careerList[${i}].workTerm" />&nbsp;</td>
                        </tr>
                        <tr>
                            <th>職種</th>
                            <td class="release">
                                <div class="autoDispWrap2">
                                    <c:forEach items="${jobList}" var="t">
                                        <c:set var="styleId" value="careerJob_${i}_${t.value}" />
                                        <span>
                                            <html:multibox property="careerList[${i}].job" value="${t.value}" styleId="${styleId}" />
                                            <label for="${styleId}">${f:h(t.label)}</label>
                                        </span>
                                    </c:forEach>
                                </div>
                                &nbsp;
                            </td>
                        </tr>
                        <tr>
                            <th>業態</th>
                            <td class="release">
                                <div class="autoDispWrap2">
                                    <c:forEach items="${industryList}" var="t">
                                        <c:set var="styleId" value="careerIndustry_${i}_${t.value}" />
                                        <span>
                                            <html:multibox property="careerList[${i}].industry" value="${t.value}" styleId="${styleId}" />
                                            <label for="${styleId}">${f:h(t.label)}</label>
                                        </span>
                                    </c:forEach>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>業務内容</th>
                            <td>
                                <html:textarea property="careerList[${i}].businessContent" />
                            </td>
                        </tr>
                        <tr>
                            <th>客席数・坪数</th>
                            <td><html:text property="careerList[${i}].seat" /></td>
                        </tr>
                        <c:choose>
                            <c:when test="${status.last}">
                                <c:set var="bottomClass" value="bdrs_bottom" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="bottomClass" value="" />
                            </c:otherwise>
                        </c:choose>
                        <tr>
                            <th class="${bottomClass}"><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
                            <td class="${bottomClass}">
                                <html:text property="careerList[${i}].monthSales" /></td>
                        </tr>

                    </c:forEach>

                </table>
                <hr />


                <h3 title="管理者設定" class="title"><img src="${ADMIN_CONTENS}/images/member/title_admin.gif" alt="管理者設定" /></h3>
                <table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
                    <tr>
                        <th width="150">PCメール配信</th>
                        <td class="release">
                            <div class="autoDispWrap2">
                                <c:forEach items="${pcMailStopFlgList}" var="t">
                                    <c:set var="styleId" value="pcMailStopFlg_${t.value}" />
                                    <html:radio property="pcMailStopFlg" value="${t.value}" styleId="${styleId}" />
                                    <label for="${styleId}">${f:h(t.label)}</label>
                                </c:forEach>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th width="150">モバイルメール配信</th>
                        <td class="release">
                            <div class="autoDispWrap2">
                                <c:forEach items="${mobileMailStopFlgList}" var="t">
                                    <c:set var="styleId" value="mobileMailStopFlg_${t.value}" />
                                    <html:radio property="mobileMailStopFlg" value="${t.value}" styleId="${styleId}" />
                                    <label for="${styleId}">${f:h(t.label)}</label>
                                </c:forEach>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="bdrs_bottom">会員区分</th>
                        <td class="bdrs_bottom">
                            ${f:label(memberKbn, memberKbnList, 'value', 'label')}
                        </td>
                    </tr>
                </table>
                <hr />

                <div class="wrap_btn">
                    <c:choose>
                        <c:when test="${pageKbn eq PAGE_EDIT}">
                            <html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
                            <html:button property="back" value="戻 る" onclick="location.href='${f:url(backUrl)}'" />
                        </c:when>
                        <c:otherwise>
                            <html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
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

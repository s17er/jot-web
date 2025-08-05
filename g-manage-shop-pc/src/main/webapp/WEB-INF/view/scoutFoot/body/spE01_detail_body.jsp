<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.shop.pc.scoutFoot.action.member.DetailAction"%>
<%@page import="com.gourmetcaree.shop.pc.scoutFoot.action.member.DetailAction.FromPage"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%
	String areaCd = String.valueOf(request.getAttribute("APPLICATION_AREA_CD"));
%>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />
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
<gt:areaList name="areaList" />
<gt:allArea name="allAreaList" />

<%
	Object obj = session.getAttribute(DetailAction.FROM_PAGE_SESSION_KEY);
	boolean fromKeepboxFlg;
	boolean fromScoutMailFlg;
	boolean fromMailBoxScoutMailFlg;
	boolean buttomDispFlg;
	boolean fromPreApplicationMailFlg;
	if (obj == null) {
		fromKeepboxFlg = false;
		fromScoutMailFlg = false;
		fromMailBoxScoutMailFlg = false;
		buttomDispFlg = true;
		fromPreApplicationMailFlg = false;
	} else if (obj instanceof FromPage) {
		FromPage from = (FromPage) obj;
		if (from == FromPage.KEEP_BOX) {
			fromKeepboxFlg = true;
			fromScoutMailFlg = false;
			fromMailBoxScoutMailFlg = false;
			buttomDispFlg = true;
			fromPreApplicationMailFlg = false;
		} else if (from == FromPage.SCOUT_MAIL) {
			fromKeepboxFlg = false;
			fromScoutMailFlg = true;
			fromMailBoxScoutMailFlg = false;
			buttomDispFlg = false;
			fromPreApplicationMailFlg = false;
		} else if (from == FromPage.MAIL_BOX_SCOUT_MAIL) {
			fromKeepboxFlg = false;
			fromScoutMailFlg = false;
			fromMailBoxScoutMailFlg = true;
			buttomDispFlg = false;
			fromPreApplicationMailFlg = false;
		} else if (from == FromPage.PRE_APPLICATION_MAIL) {
			fromKeepboxFlg = false;
			fromScoutMailFlg = false;
			fromMailBoxScoutMailFlg = false;
			buttomDispFlg = false;
			fromPreApplicationMailFlg = true;
		} else {
			fromKeepboxFlg = false;
			fromScoutMailFlg = false;
			fromMailBoxScoutMailFlg = false;
			buttomDispFlg = true;
			fromPreApplicationMailFlg = false;
		}
	} else {
		fromKeepboxFlg = false;
		fromScoutMailFlg = false;
		fromMailBoxScoutMailFlg = false;
		buttomDispFlg = true;
		fromPreApplicationMailFlg = false;
	}

	pageContext.setAttribute("fromKeepboxFlg", fromKeepboxFlg, PageContext.REQUEST_SCOPE);
	pageContext.setAttribute("fromScoutMailFlg", fromScoutMailFlg, PageContext.REQUEST_SCOPE);
	pageContext.setAttribute("fromMailBoxScoutMailFlg", fromMailBoxScoutMailFlg, PageContext.REQUEST_SCOPE);
	pageContext.setAttribute("buttomDispFlg", buttomDispFlg, PageContext.REQUEST_SCOPE);
	pageContext.setAttribute("fromPreApplicationMailFlg", fromPreApplicationMailFlg, PageContext.REQUEST_SCOPE);
%>
<!-- #main# -->
<div id="main">
	<div id="wrap_mail">
		<div id="wrap_mail_content">
			<html:messages id="msg" message="true">
				<div id="message">
					<ul>
						<li><bean:write name="msg" ignore="true"/></li>
					</ul>
				</div>
			</html:messages>
			<div>
			<c:choose>
				<c:when test="${fromKeepboxFlg}">
					<c:set var="backButtonPath" value="${backKeepBoxPath}" scope="page" />
				</c:when>
				<c:when test="${fromScoutMailFlg}">
					<c:set var="backButtonPath" value="${backScoutMailPath}" scope="page" />
				</c:when>
				<c:when test="${fromMailBoxScoutMailFlg}">
					<c:set var="backButtonPath" value="${backScoutMailPath}" scope="page" />
				</c:when>
				<c:when test="${fromPreApplicationMailFlg}">
					<c:set var="backButtonPath" value="${backPreApplicationMailPath}" scope="page" />
				</c:when>
				<c:otherwise>
					<c:set var="backButtonPath" value="${backListPath}" scope="page" />
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${buttomDispFlg}">
					<div class="wrap_btn clear footmark">
						<div id="scout_head_btn">
				            <html:button property="#" value="一覧に戻る" onclick="location.href='${f:url(backButtonPath)}'; return false;" styleClass="btn_backList" />
							<c:choose>
								<c:when test="${dto.scoutUseFlg and dto.scoutMailFlg and dto.scoutCount > 0}">
									<html:button property="#" value="スカウトメール送信" onclick="location.href='${f:url(gf:makePathConcat1Arg(scoutMailInputPath, id))}'; return false;" styleClass="btn_scoutSend" />
								</c:when>
								<c:otherwise>
									<html:button property="" value="スカウトメール送信"  styleClass="btn_scoutNoSend" disabled="true" />
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${!dto.keepFlg}">
									<html:button property="#" value="キープする" onclick="location.href='${f:url(gf:makePathConcat1Arg(keepPath, id))}'; return false;"  styleClass="btn_keepBox" />
								</c:when>
								<c:otherwise>
									<html:button property="#" value="キープを外す" onclick="location.href='${f:url(gf:makePathConcat1Arg(deleteKeepPath, id))}'; return false;"  styleClass="btn_deletekeepBox" />
								</c:otherwise>
							</c:choose>
							<c:if test="${!dto.footPrintFlg}">
								<html:button property="#" value="気になる" onclick="location.href='${f:url(gf:makePathConcat1Arg(footPrintPath, id))}'; return false;" styleClass="btn_footmark" />
							</c:if>

						</div>
				</c:when>
				<c:otherwise>
					<div class="wrap_btn">
						<html:button property="#" value="　" onclick="location.href='${f:url(backButtonPath)}'; return false;" styleClass="btn_backList" />
				</c:otherwise>
			</c:choose>
				<!-- プロフィール -->
				<div class="tab_contents" id="detail_Information">
					<div class="detail_area">
						<div class="det_profile">
							<div class="l_title"><h3>求職者プロフィール</h3></div>
							<div class="r_details">
								<table cellpadding="0" cellspacing="0" border="0" class="detail_table">
									<tbody>
									<tr>
										<th>会員ID</th>
										<td>${f:h(dto.id)}</td>
									</tr>
									<tr>
										<th>最終ログイン日</th>
										<td>${f:h(dto.lastLoginDatetime)}</td>
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
											<gt:convertToQualificationName items="${dto.qualification}"/>
											<c:if test="${not empty dto.qualificationOther}">
												${f:h(dto.qualificationOther)}
											</c:if>
										</td>
									</tr>
									<tr>
										<th>飲食業界経験年数</th>
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
										<th>マネジメント経験</th>
										<td>
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
								</tbody></table>
							</div>
						</div>
						<div class="det_pr">
							<div class="l_title"><h3>希望条件</h3></div>
							<div class="r_details">
								<table cellpadding="0" cellspacing="0" border="0" class="detail_table">
									<tbody>
										<tr>
											<th>希望職種</th>
											<td><gt:convertToJobName items="${dto.job}"/></td>
										</tr>
										<tr>
											<th>希望業種</th>
											<td><gt:convertToIndustryName items="${dto.industry}"/></td>
										</tr>
										<tr>
											<th>希望勤務地</th>
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
											<td>${f:label(dto.transferFlg, transferFlgList, 'value', 'label')}</td>
										</tr>
										<tr>
											<th>深夜勤務の可・不可</th>
											<td>${f:label(dto.midnightShiftFlg, midnightShiftFlgList, 'value', 'label')}</td>
										</tr>
										<tr>
											<th>希望年収</th>
											<td>${f:label(dto.salaryKbn, salaryKbnList, 'value', 'label')}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>

						<div class="det_jobcareer">
							<div class="l_title"><h3>WEB履歴書</h3></div>
							<div class="r_details">
								<table cellpadding="0" cellspacing="0" border="0" class="detail_table"><tbody>
									<tr>
										<th>学校名</th>
										<td>${f:h(dto.schoolName)}</td>
									</tr>
									<tr>
										<th>学部・学科</th>
										<td>${f:h(dto.department)}</td>
									</tr>
									<tr>
										<th>状況</th>
										<td>${f:label(dto.graduationKbn, graduationKbnList, 'value', 'label')}</td>
									</tr>
								</tbody></table>
							</div>
						</div>

						<c:if test="${careerListExist}">
						<c:forEach var="careerDto" items="${dto.careerList}" varStatus="status">
							<div class="det_jobcareer">
								<div class="l_title"><h3>職務経歴${status.count}</h3></div>
								<div class="r_details">
									<table cellpadding="0" cellspacing="0" border="0" class="detail_table"><tbody>
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
											<th>業態</th>
											<td><gt:convertToIndustryName items="${careerDto.industry}"/>&nbsp;</td>
										</tr>
										<tr>
											<th>業務内容</th>
											<td>${f:br(careerDto.businessContent)}&nbsp;</td>
										</tr>
										<tr>
											<th>客席数・坪数</th>
											<td>${f:h(careerDto.seat)}&nbsp;</td>
										</tr>
										<tr>
											<th><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
											<td>${f:h(careerDto.monthSales)}&nbsp;</td>
										</tr>
									</tbody></table>
								</div>
							</div>
						</c:forEach>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- #main# -->

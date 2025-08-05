<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<gt:typeList name="submailReceptionFlgList" typeCd="<%=MTypeConstants.SubmailReceptionFlg.TYPE_CD %>" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/pattern.css" />
<!-- #main# -->

	<div id="main">
		<div class="page_back shopat_page_back pc_none">
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT }">
				<a onclick="location.href='${f:url('/shop/edit/')}'" id="btn_back">戻る</a>
			</c:when>
			<c:otherwise>
				<a onclick="location.href='${f:url('/top/menu/')}'" id="btn_back">戻る</a>
			</c:otherwise>
		</c:choose>
		</div>
		<div id="wrap_company">
			<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>
			<p class="explanation">${defaultMsg}</p>
			<html:errors />
			<div id="wrap_comp_content">
				<div class="tab_area">
					<div class="tab_contents tab_active" id="company_detail">
						<div class="company_detail_area">
							<table cellpadding="0" cellspacing="0" border="0" class="detail_table comp_table">
								<tbody>
									<tr>
										<th>企業名</th>
										<td>${f:h(customerName)}</td>
									</tr>
									<tr>
										<th>担当者名</th>
										<td>${f:h(contactName)}&nbsp;&nbsp;様</td>
									</tr>
									<tr>
										<th>担当者名（カナ）</th>
										<td>${f:h(contactNameKana)}&nbsp;&nbsp;様</td>
									</tr>
									<tr>
										<th>ログインID</th>
										<td>${f:h(loginId)}</td>
									</tr>
									<tr>
										<th>メインメールアドレス</th>
										<td>${f:h(mainMail)}</td>
									</tr>
									<tr>
										<th>サブメールアドレス</th>
										<td>
											<c:choose>
												<c:when test="${not empty subMailDtoList}">
													<c:forEach items="${subMailDtoList}" var="dto" varStatus="status">
														<c:if test="${not empty dto.subMail}">
															${f:h(dto.subMail)}&nbsp;&nbsp;
															[&nbsp;${f:label(dto.submailReceptionFlg, submailReceptionFlgList, 'value', 'label')}&nbsp;]
															<c:if test="${!status.last}"><br></c:if>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													&nbsp;
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<th>電話番号</th>
										<td>${f:h(phoneNo1)}-${f:h(phoneNo2)}-${f:h(phoneNo3)}</td>
									</tr>
									<c:if test="${not empty newPassword}">
										<tr>
											<th class="bdrs_bottom">パスワード</th>
											<td class="bdrs_bottom">
												${f:h(dispPassword)}
											</td>
										</tr>
									</c:if>
									<tr>
										<th>メールマガジン</th>
										<td>
											<gt:typeList name="mailMagazineReceptionFlgList" typeCd="<%=MTypeConstants.CustomerMailMagazineReceptionFlg.TYPE_CD %>"/>
											${f:label(mailMagazineReceptionFlg, mailMagazineReceptionFlgList, 'value', 'label')}
										</td>
									</tr>
								</tbody>
							</table>
							<s:form action="${f:h(actionPath)}">
								<div class="wrap_btn">
									<c:choose>
										<c:when test="${pageKbn eq PAGE_DETAIL }">
											<html:submit property="index" value="編集" styleId="btn_edit" />
										</c:when>
										<c:otherwise>
											<html:submit property="collect" class="btn__white" value="戻る" />
											<html:submit property="submit" class="btn__orenge" value="登録" />
										</c:otherwise>
									</c:choose>

								</div>
							</s:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<!-- #main# -->
<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>
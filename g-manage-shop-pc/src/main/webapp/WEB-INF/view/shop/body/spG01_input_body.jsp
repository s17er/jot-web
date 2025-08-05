<%@page pageEncoding="UTF-8"%>
<gt:typeList name="submailReceptionFlgList" typeCd="<%=MTypeConstants.SubmailReceptionFlg.TYPE_CD %>" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/pattern.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />

<!-- #main# -->

<div id="main">
	<div class="page_back shopat_page_back pc_none">
		<a onclick="location.href='${f:url('/shop/')}'" id="btn_back">戻る</a>
	</div>
	<div id="wrap_company">
		<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>
		<p class="explanation">${defaultMsg}<br>「＊」マークは必須項目となります。</p>
		<html:errors />
		<div id="wrap_comp_content">
			<div class="tab_area">
				<div class="tab_contents tab_active" id="company_detail">
					<div class="company_detail_area">
						<s:form action="${f:h(actionPath)}">
							<div id="wrap_form">
								<table cellpadding="0" cellspacing="0" border="0"
									class="detail_table comp_table">
									<tbody>
										<tr>
											<th>企業名</th>
											<td>${f:h(customerName)}</td>
										</tr>
										<tr>
											<th class="mandatory">担当者名</th>
											<td><html:text property="contactName" styleClass="text"></html:text></td>
										</tr>
										<tr>
											<th class="mandatory">担当者名（カナ）</th>
											<td><html:text property="contactNameKana" /></td>
										</tr>
										<tr>
											<th>ログインID</th>
											<td>${f:h(loginId)}</td>
										</tr>
										<tr>
											<th class="mandatory">メインメールアドレス</th>
											<td><html:text property="mainMail"></html:text></td>
										</tr>
										<tr>
											<th>サブメールアドレス</th>
											<td>
											<c:forEach items="${subMailDtoList}" var="dto" varStatus="status">
												<div>
												<html:text property="subMailDtoList[${status.index}].subMail" styleClass="disabled hankaku" styleId="subMail_${status.index}" />&nbsp;
												<br>
												<div>
												<c:forEach items="${submailReceptionFlgList}" var="t">
													<html:radio property="subMailDtoList[${status.index}].submailReceptionFlg" value="${t.value}"
														styleId="submailReceptionFlg_${t.value}_${status.index}" styleClass="release validate[condRequired[subMail_${status.index}]]" data-prompt-position="topLeft" />&nbsp;<label for="submailReceptionFlg_${t.value}_${status.index}">${f:h(t.label)}</label>&nbsp;&nbsp;&nbsp;
												</c:forEach>
												</div>
												</div>
												<c:if test="${!status.last}">
												<br>
												</c:if>
											</c:forEach>
											</td>
										</tr>
										<tr>
											<th>メールマガジン</th>
											<td class="select bdrs_bottom" style="text-align: left;">
											<gt:typeList name="mailMagazineReceptionFlgList" typeCd="<%=MTypeConstants.CustomerMailMagazineReceptionFlg.TYPE_CD %>"/>
											<c:forEach items="${mailMagazineReceptionFlgList}" var="t">
												<html:radio property="mailMagazineReceptionFlg" value="${f:h(t.value)}" styleId="${f:h(t.value)}"/>
												<label for="${f:h(t.value)}">${f:h(t.label)}</label>&nbsp;
											</c:forEach>
											</td>
										</tr>
										<tr>
											<th>電話番号</th>
											<td><html:text property="phoneNo1" styleClass="disabled tel" /> - <html:text property="phoneNo2" size="5" styleClass="disabled tel" /> - <html:text property="phoneNo3" size="5" styleClass="disabled tel" /></td>
										</tr>
										<tr>
											<th>現在のパスワード</th>
											<td><html:password property="nowPassword" /></td>
										</tr>
										<tr>
											<th>新しいパスワード</th>
											<td>
												<html:password property="newPassword" /><br>
												<span class="attention">※半角英数字（0～9、A～Z）、6～20文字で入力してください。</span>
											</td>
										</tr>
										<tr>
											<th>新しいパスワード（確認用）</th>
											<td>
												<html:password property="rePassword" /><br>
												<span class="attention">※新しいパスワードと同じパスワードを入力してください。</span>
											</td>
										</tr>
									</tbody>
								</table>
								<div class="wrap_btn">
									<html:submit property="conf" value="確認"/>
									<html:hidden property="version" value="${f:h(version)}" />
								</div>
							</div>
						</s:form>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>

<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>
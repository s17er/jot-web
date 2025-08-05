<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<gt:authLevelList name="authLevelList"/>
<gt:companyList name="companyList"/>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
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

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<tr>
							<th width="150">営業担当者ID</th>
							<td>${f:h(id)}&nbsp;</td>
						</tr>
						<tr>
							<th>登録日</th>
							<td>${f:h(registrationDatetime)}&nbsp;</td>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<th width="150">会社名&nbsp;<span class="attention">※必須</span></th>
					<td>
					<html:select property="companyId" styleId="firstFocus">
						<html:option value="" >--</html:option>
						<html:optionsCollection name="companyList" />

					</html:select>
						</td>
				</tr>
				<tr>
					<th>氏名&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="salesName" /></td>
				</tr>
				<tr>
					<th>氏名（カナ）&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="salesNameKana" styleClass="active" /></td>
				</tr>
				<tr>
					<th>携帯電話</th>
					<td class="release">
						<html:text property="mobileNo1" size="5" styleClass="disabled" />&nbsp;-&nbsp;
						<html:text property="mobileNo2" size="5" styleClass="disabled" />&nbsp;-&nbsp;
						<html:text property="mobileNo3" size="5" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>権限&nbsp;<span class="attention">※必須</span></th>
					<td><html:select property="authorityCd">
							<html:option value="" >--</html:option>
							<html:optionsCollection name="authLevelList" />

						</html:select><span class="attention">&nbsp;※会社名で代理店を選択している場合は、代理店を選択してください。</span>
					</td>
				</tr>
				<tr>
					<th>メインアドレス&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="mainMail" styleClass="disabled" /></td>
				</tr>
				<tr>
					<th>サブアドレス</th>
					<td>
						<html:text property="subMail" styleClass="disabled" />&nbsp;
						<html:radio property="submailReceptionFlg" value="0" styleClass="release"  />&nbsp;受信否
						<html:radio property="submailReceptionFlg" value="1" styleClass="release" />&nbsp;受信可
					</td>
				</tr>
				<tr>
					<th>所属部署</th>
					<td><html:text property="department" /></td>
				</tr>
				<tr>
					<th>ログインID&nbsp;<span class="attention">※必須</span></th>
					<td>
						<html:text property="loginId" styleClass="disabled" /><br />
						<span class="attention">※半角英数字（0～9、a～z、A～Z）、6文字以上で入力して下さい。</span>
					</td>
				</tr>
				<tr>
					<c:if test="${pageKbn eq PAGE_INPUT}">
					<th>パスワード&nbsp;<span class="attention">※必須</span></th>
					</c:if>
					<c:if test="${pageKbn eq PAGE_EDIT}">
					<th>パスワード</th>
					</c:if>
					<td>
						<html:password property="password" styleClass="disabled"  maxlength="20"/><br />
						<span class="explain">▼確認のため再度入力して下さい。</span><br />
						<html:password property="rePassword" styleClass="disabled" maxlength="20" /><br />
						<span class="attention">※半角英数字（0～9、a～z、A～Z）、6～20文字で入力して下さい。</span>
					</td>
				</tr>
				<tr>
					<th>備考</th>
					<td><html:textarea property="note" cols="60" rows="10" ></html:textarea></td>
				</tr>
				<tr>
					<th class="bdrs_bottom">旧システム担当者ID</th>
					<td class="release bdrs_bottom"><html:text property="oldSystemSalesId" styleClass="disabled" size="8" /></td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
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
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

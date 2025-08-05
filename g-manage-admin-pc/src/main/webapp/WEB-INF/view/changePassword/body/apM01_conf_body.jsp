<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:companyList name="companyList" />
<gt:typeList name="submailReceptionFlgList" typeCd="<%=MTypeConstants.SubmailReceptionFlg.TYPE_CD %>" />
<gt:authLevelList name="authorityCdList"/>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
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

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">営業担当者ID</th>
					<td>${f:h(id)}&nbsp;</td>
				</tr>
				<tr>
					<th>会社名</th>
					<td>${f:label(companyId, companyList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>氏名</th>
					<td>${f:h(salesName)}&nbsp;</td>
				</tr>
				<tr>
					<th>氏名（カナ）</th>
					<td>${f:h(salesNameKana)}&nbsp;</td>
				</tr>
				<tr>
					<th>携帯電話</th>
					<td>${f:h(mobileNo)}&nbsp;</td>
				</tr>
				<tr>
					<th>権限</th>
					<td>${f:label(authorityCd, authorityCdList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>メインアドレス</th>
					<td>${f:h(mainMail)}&nbsp;</td>
				</tr>
				<tr>
					<th>サブアドレス</th>
					<td>
						<c:choose>
							<c:when test="${not empty subMail}">
							${f:h(subMail)}&nbsp;&nbsp;[&nbsp;${f:label(submailReceptionFlg, submailReceptionFlgList, 'value', 'label')}&nbsp;]
							</c:when>
							<c:otherwise>
								&nbsp;
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>所属部署</th>
					<td>${f:h(department)}&nbsp;</td>
				</tr>
				<tr>
					<th>ログインID</th>
					<td>${f:h(loginId)}</td>
				</tr>
				<tr>
					<th>パスワード</th>
					<td>${f:h(dispPassword)}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">備考</th>
					<td class="bdrs_bottom">${f:br(f:h(note))}&nbsp;</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

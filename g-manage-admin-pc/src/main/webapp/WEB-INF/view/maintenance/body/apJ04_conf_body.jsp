<%@page pageEncoding="UTF-8"%>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
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
		<c:if test="${existDataFlg eq true}">
		<html:hidden property="hiddenId" />

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
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
					<th  width="150">会社名</th>
					<td>${f:h(companyName)}&nbsp;</td>
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
					<td><c:if test="${f:h(mobileNo1) ne ''}">${f:h(mobileNo1)}&nbsp;-&nbsp;${f:h(mobileNo2)}&nbsp;-&nbsp;${f:h(mobileNo3)}</c:if>&nbsp;</td>
				</tr>
				<tr>
					<th>権限</th>
					<td>${f:h(authorityName)}&nbsp;</td>
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
								${f:h(subMail)}&nbsp;&nbsp;[&nbsp;${f:h(submailReceptionFlgName)}&nbsp;]&nbsp;
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
					<td>${f:h(loginId)}&nbsp;</td>
				</tr>
				<c:if test="${pageKbn ne PAGE_DETAIL}" >
				<tr>
					<th>パスワード</th>
					<td>${f:h(dispPassword)}&nbsp;</td>
				</tr>
				</c:if>
				<tr>
					<th>備考</th>
					<td>${f:br(f:h(note))}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">旧システム担当者ID</th>
					<td class="release bdrs_bottom">${f:h(oldSystemSalesId)}&nbsp;</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_INPUT or pageKbn eq PAGE_EDIT}">
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:when test="${pageKbn eq PAGE_DETAIL}">
						<input type="button" name="edit" value="編 集" onclick="location.href='${f:url(editPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<input type="button" name="delete" value="削 除" onclick="if(!confirm('削除してもよろしいですか?')) {return false;}; location.href='${f:url(deletePath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
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
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

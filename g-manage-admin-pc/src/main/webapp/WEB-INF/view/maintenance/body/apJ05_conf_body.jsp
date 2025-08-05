<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/checkConfFlg.js"></script>

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

	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<html:hidden property="hiddenId" />
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
						<tr>
							<th width="150">会社ID</th>
							<td>${f:h(id)}</td>
						</tr>
						<tr>
							<th>登録日</th>
							<td><fmt:formatDate value="${f:date(registrationDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd" /></td>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<th width="150">社名</th>
					<td>${f:h(companyName)}</td>
				</tr>
				<tr>
					<th>社名（カナ）</th>
					<td>${f:h(companyNameKana)}</td>
				</tr>
				<tr>
					<th>担当者名</th>
					<td>${f:h(contactName)}</td>
				</tr>
				<tr>
					<th>担当者（カナ）</th>
					<td>${f:h(contactNameKana)}</td>
				</tr>
				<tr>
					<th>代理店フラグ</th>
					<td>
						<gt:typeList name="agencyFlgList" typeCd="<%=MTypeConstants.AgencyFlg.TYPE_CD %>" />
						${f:label(agencyFlg, agencyFlgList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>エリア</th>
					<td>
						<gt:areaList name="areaList" />
						<c:forEach items="${areaCd}" var="t" varStatus="status">
							${f:label(t, areaList, 'value', 'label')}
							<c:if test="${!status.last}">,&nbsp;</c:if>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>郵便番号</th>
					<td>${f:h(zipCd)}</td>
				</tr>
				<tr>
					<th>住所</th>
					<td>
						<gt:prefecturesList name="prefecturesList" />
						${f:label(prefecturesCd, prefecturesList, 'value', 'label')}
						${f:h(municipality)}
						${f:h(address)}
					</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td>${f:h(phoneNo1)}-${f:h(phoneNo2)}-${f:h(phoneNo3)}</td>
				</tr>
				<tr>
					<th>FAX番号</th>
					<td>
						<c:if test="${!empty faxNo1}">
							${f:h(faxNo1)}-${f:h(faxNo2)}-${f:h(faxNo3)}
						</c:if>&nbsp;
					</td>
				</tr>
				<tr>
					<th>メインアドレス</th>
					<td>${f:h(mainMail)}</td>
				</tr>
				<tr>
					<th>サブアドレス</th>
					<td>
						${f:h(subMail)}&nbsp;&nbsp;
						<gt:typeList name="submailReceptionFlgList" typeCd="<%=MTypeConstants.SubmailReceptionFlg.TYPE_CD %>" />
						[&nbsp;${f:label(submailReceptionFlg, submailReceptionFlgList, 'value', 'label')}&nbsp;]
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">備考</th>
					<td class="bdrs_bottom">${f:br(f:h(note))}&nbsp;</td>
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
						<input type="button" name="delete" value="削 除" onclick="deleteConf('processFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
				</c:choose>
			</div>
		</s:form>
	<c:if test="${pageKbn eq PAGE_DETAIL}">
		<% //削除の場合は、確認のためにフラグを立てる  %>
		<s:form action="${f:h(deleteActionPath)}" styleId="deleteForm" >
			<html:hidden property="id" value="${f:h(id)}" />
			<html:hidden property="processFlg" styleId="processFlg" />
			<html:hidden property="version" value="${f:h(version)}" />
		</s:form>
	</c:if>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

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
</c:if>
</div>
<!-- #main# -->

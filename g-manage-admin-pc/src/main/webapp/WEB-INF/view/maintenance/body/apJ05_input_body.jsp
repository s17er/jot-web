<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
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
					<c:when test="${pageKbn eq PAGE_EDIT}">
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
					<th width="150">社名&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="companyName" styleId="setFocus"/></td>
				</tr>
				<tr>
					<th>社名（カナ）&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="companyNameKana" /></td>
				</tr>
				<tr>
					<th>担当者名&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="contactName" /></td>
				</tr>
				<tr>
					<th>担当者名（カナ）&nbsp;<span class="attention">※必須</span></th>
					<td><html:text property="contactNameKana" /></td>
				</tr>
				<tr>
					<th>代理店フラグ&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<gt:typeList name="agencyFlgList" typeCd="<%=MTypeConstants.AgencyFlg.TYPE_CD %>" />
						<c:forEach items="${agencyFlgList}" var="t">
							<html:radio property="agencyFlg" value="${t.value}" styleId="agencyFlg_${t.value}" />
							<label for="agencyFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
			        </td>
				</tr>
				<tr>
					<th>エリア&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<gt:areaList name="areaList" />
						<c:forEach items="${areaList}" var="t">
							<html:multibox property="areaCd" value="${t.value}" styleId="area_cd_${t.value}" />
							<label for="area_cd_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>郵便番号<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="zipCd" size="10" styleClass="disabled" maxlength="8" /><br />
						<span class="explain">(例)530-0051</span>
					</td>
				</tr>
				<tr>
					<th>住所1&nbsp;<span class="attention">※必須</span></th>
					<td>
						<gt:prefecturesList name="prefecturesList" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="prefecturesCd">
							<html:optionsCollection name="prefecturesList" />
						</html:select>
						<br />
						<span class="explain">▼市区町村まで入力</span><br />
						<html:text property="municipality" /><br />
						<span class="explain">(例)大阪市北区</span>
					</td>
				</tr>
				<tr>
					<th>住所2&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<span class="explain">▼町名番地・ビル名を入力</span><br />
						<html:text property="address" size="80" /><br />
						<span class="explain">(例)太融寺町8-8 日進ビル6階</span>
					</td>
				</tr>
				<tr>
					<th>電話番号&nbsp;<span class="attention">※必須</span></th>
					<td class="release">
						<html:text property="phoneNo1" size="5" maxlength="5" styleClass="disabled" />&nbsp;-&nbsp;
						<html:text property="phoneNo2" size="5" maxlength="5"  styleClass="disabled" />&nbsp;-&nbsp;
						<html:text property="phoneNo3" size="5" maxlength="5"  styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>FAX番号</th>
					<td class="release">
						<html:text property="faxNo1" size="5" maxlength="5"  styleClass="disabled" />&nbsp;-&nbsp;
						<html:text property="faxNo2" size="5" maxlength="5"  styleClass="disabled" />&nbsp;-&nbsp;
						<html:text property="faxNo3" size="5" maxlength="5"  styleClass="disabled" />
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
						<gt:typeList name="submailReceptionFlgList" typeCd="<%=MTypeConstants.SubmailReceptionFlg.TYPE_CD %>" />
						<c:forEach items="${submailReceptionFlgList}" var="t">
							<html:radio property="submailReceptionFlg" value="${t.value}" styleId="submailReceptionFlg_${t.value}" styleClass="release"  />
							<label for="submailReceptionFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
			        </td>
				</tr>
				<tr>
					<th class="bdrs_bottom">備考</th>
					<td class="bdrs_bottom"><html:textarea property="note" cols="60" rows="10"></html:textarea></td>
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
</c:if>

</div>
<!-- #main# -->

<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div id="wrap_serch">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">会社検索</th>
				</tr>
				<tr>
					<th>エリア</th>
					<td>
						<gt:areaList name="areaList" />
						<html:select property="areaCd">
							<html:optionsCollection name="areaList" />
						</html:select>
					</td>
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
					<th>社名</th>
					<td colspan="3"><html:text property="companyName" /></td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<%-- IEでエンターキーによるsubmitを有効にするためのダミーのテキストエリア --%>
				<input type="text" name="dummy" style="display:none;">
				<html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_result# -->
	<div id="wrap_result" >
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
			<tr>
				<th width="15" class="posi_center">No.</th>
				<th width="40" class="posi_center">会社ID</th>
				<th>社名</th>
				<th width="90">担当者</th>
				<th width="80" class="posi_center">エリア</th>
				<th width="150">メインアドレス</th>
				<th width="90">電話番号</th>
				<th width="45" class="posi_center bdrs_right">詳細</th>
			</tr>

		<% //ループ処理 %>
		<c:forEach items="${list}" var="m" varStatus="status">
			<tr>
				<td width="15" class="posi_center"><fmt:formatNumber value="${status.index + 1}" /></td>
				<td width="40" class="posi_center">${f:h(m.id)}</td>
				<td>
					${f:h(m.companyName)}<br />
					${f:h(m.companyNameKana)}
				</td>
				<td width="90">${f:h(m.contactName)}&nbsp;</td>
				<td width="80" class="posi_center">
					<c:forEach items="${m.areaCd}" var="t" varStatus="areaStatus">
						${f:label(t, areaList, 'value', 'label')}
						<c:if test="${!areaStatus.last}">,&nbsp;</c:if>
					</c:forEach>
				</td>
				<td width="150">${f:h(m.mainMail)}</td>
				<td width="90">${f:h(m.phoneNo1)}-${f:h(m.phoneNo2)}-${f:h(m.phoneNo3)}</td>
				<td width="45" class="posi_center bdrs_right"><a href="${f:url(m.detailPath)}">詳細</a></td>
			</tr>
		</c:forEach>

		</table>
		<hr />
	</div>
	<!-- #wrap_result# -->
	<hr />
</c:if>
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

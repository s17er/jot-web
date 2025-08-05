<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>

<% /* CSSファイルを設定 */ %>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery.crossSelect.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery.lightbox.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.css" media="screen" />
<gt:prefecturesList name="prefecturesList" noDisplayValue="<%= Arrays.asList(MPrefectures.KAIGAI) %>" blankLineLabel="${common['gc.pullDown']}" />
<gt:typeList name="memberFlgList" typeCd="<%=MTypeConstants.MemberFlg.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
<gt:typeList name="terminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}"/>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/checkConfFlg.js"></script>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle1)}" class="title date">${f:h(pageTitle1)}</h2>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<html:hidden property="hiddenId" />
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="140">氏名</th>
					<td>${name}</td>
				</tr>
				<tr>
					<th width="140">氏名(カナ)</th>
					<td>${nameKana}</td>
				</tr>
				<tr>
					<th>住所1</th>
					<td>${f:label(prefecturesCd, prefecturesList, 'value', 'label')}${f:h(municipality)}&nbsp;</td>
				</tr>
				<tr>
					<th>住所2</th>
					<td>${f:h(address)}&nbsp;</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td>${f:h(phoneNo1)}&nbsp;${f:h(phoneNo2)}&nbsp;${f:h(phoneNo3)}&nbsp;</td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td>${f:h(mailAddress)}&nbsp;</td>
				</tr>
				<tr>
					<th>会員/非会員</th>
					<td>${f:label(memberFlg, memberFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>端末</th>
					<td>${f:label(terminalKbn, terminalKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">自己PR</th>
					<td class="bdrs_bottom">${f:br(f:h(applicationSelfPr))}</td>
				</tr>
			</table>
			<br><br>
			<hr>
			<div class="wrap_btn">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_INPUT}">
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:when test="${pageKbn eq PAGE_DETAIL}">
						<input type="button" name="edit" value="編 集" onclick="location.href='${f:url(editPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
						<input type="button" name="delete" value="削 除" onclick="deleteConf('processFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
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
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>

<!-- #main# -->

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

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="140">氏名&nbsp;</th>
					<td><input type="text" value="${name}" id="name" name="name"></td>
				</tr>
				<tr>
					<th width="140">氏名(カナ)&nbsp;</th>
					<td><input type="text" value="${nameKana}" id="nameKana" name="nameKana"></td>
				</tr>
				<tr>
					<th>住所1&nbsp;</th>
					<td>
						<html:select property="prefecturesCd"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select><br />
						<span class="explain">▼市区町村まで入力</span><br />
						<html:text property="municipality" /><br />
					</td>
				</tr>
				<tr>
					<th>住所2</th>
					<td>
						<span class="explain">▼町名番地・ビル名を入力</span><br />
						<html:text property="address" /><br />
					</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td class="release">
						<html:text property="phoneNo1" size="5" styleClass="disabled" />
						&nbsp;-&nbsp;
						<html:text property="phoneNo2" size="5" styleClass="disabled" />
						&nbsp;-&nbsp;
						<html:text property="phoneNo3" size="5" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td>
						<html:text property="mailAddress" styleClass="disabled" />
						<span class="attention">※必ず半角英数字で入力して下さい。</span>
					</td>
				</tr>
				<tr>
					<th>会員/非会員</th>
					<td>
						<html:select property="memberFlg"  >
							<html:optionsCollection name="memberFlgList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>端末</th>
					<td>
						<html:select property="terminalKbn"  >
							<html:optionsCollection name="terminalKbnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">自己PR
					</th>
					<td class="bdrs_bottom"><html:textarea property="applicationSelfPr" cols="60" rows="10"></html:textarea></td>
				</tr>
			</table>
			<br><br>
			<hr>
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
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>

<!-- #main# -->

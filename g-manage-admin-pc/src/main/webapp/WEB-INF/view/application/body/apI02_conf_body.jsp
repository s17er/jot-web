<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>


<gt:areaList name="areaList"  />
<gt:prefecturesList name="prefecturesList" />
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"  />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="memberFlgList" typeCd="<%=MTypeConstants.MemberFlg.TYPE_CD %>"  />
<gt:typeList name="terminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>" />
<gt:typeList name="mischiefFlgList" typeCd="<%=MTypeConstants.MischiefFlg.TYPE_CD %>" />
<gt:typeList name="possibleEntryTermKbnList" typeCd="<%=MTypeConstants.PossibleEntryTermKbn.TYPE_CD %>"/>
<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"/>
<gt:typeList name="observateKbnList" typeCd="<%=MTypeConstants.ObservationKbn.TYPE_CD %>" noDisplayValue="<%=MTypeConstants.ObservationKbn.getFrontNoDispList() %>" blankLineLabel="--"/>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}"  class="observateApplicationListBtn">${f:h(navigationText2)}</a></li>
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
		<c:if test="${existDataFlg eq true}">
			<h3 title="質問者プロフィール" class="title"><img src="${ADMIN_CONTENS}/images/application/title_profile02.gif" alt="質問者者プロフィール" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">質問ID</th>
					<td>${f:h(id)}</td>
				</tr>
				<tr>
					<th>質問日時</th>
					<td>${f:h(applicationDateTime)}&nbsp;</td>
				</tr>
				<tr>
					<th>エリア</th>
					<td>${f:label(areaCd, areaList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>顧客名</th>
					<td>${f:h(customerName)}&nbsp;</td>
				</tr>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
					<tr>
						<th>氏名(漢字)</th>
						<td>${f:h(name)}&nbsp;</td>
					</tr>
					<tr>
						<th>氏名(カナ)</th>
						<td>${f:h(nameKana)}&nbsp;</td>
					</tr>
				</c:if>
				<tr>
					<th>性別</th>
					<td>${f:label(sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>年齢</th>
					<td>
						<c:if test="${not empty birthday}">
							<fmt:formatDate value="${f:date(birthday, 'yyyy-MM-dd')}" pattern="yyyy/MM/dd"/>&nbsp;
						</c:if>
						<c:if test="${not empty age}">
						${f:h(age)}歳&nbsp;
						</c:if>
					</td>
				</tr>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
					<tr>
						<th>電話番号</th>
						<td>${f:h(phoneNo)}&nbsp;</td>
					</tr>
					<tr>
						<th>メールアドレス</th>
						<td>${f:h(pcMail)}&nbsp;</td>
					</tr>
					<tr>
						<th>メールアドレス(携帯)</th>
						<td>${f:h(mobileMail)}&nbsp;</td>
					</tr>
				</c:if>
				<tr>
					<th class="bdrs_bottom">内容</th>
					<td class="bdrs_bottom">${f:br(f:h(contents))}&nbsp;</td>
				</tr>
			</table>
			<hr />

			<h3 title="希望条件" class="title"><img src="${ADMIN_CONTENS}/images/application/title_term.gif" alt="希望条件" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">質問先</th>
					<td>${f:h(applicationName)}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">お問い合わせの種類</th>
					<td class="bdrs_bottom">${f:label(observationKbn, observateKbnList, 'value', 'label')}</td>
				</tr>
			</table>
			<hr />

			<h3 title="質問情報" class="title"><img src="${ADMIN_CONTENS}/images/application/title_profile03.gif" alt="質問情報" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">会員登録</th>
					<td>${f:label(memberFlg, memberFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>端末</th>
					<td>${f:label(terminalKbn, terminalKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">いたずら応募</th>
					<td class="bdrs_bottom">${f:label(mischiefFlg, mischiefFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

		</c:if>
		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}"  class="observateApplicationListBtn">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

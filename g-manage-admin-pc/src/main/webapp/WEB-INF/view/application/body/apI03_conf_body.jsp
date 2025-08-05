<%@page import="com.gourmetcaree.admin.pc.application.form.arbeitApplication.DetailForm"%>
<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList"  />
<gt:prefecturesList name="prefecturesList" />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="terminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>" />
<gt:typeList name="possibleEntryTermKbnList" typeCd="<%=MTypeConstants.PossibleEntryTermKbn.TYPE_CD %>"/>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
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
			<h3 title="応募者プロフィール" class="title"><img src="${ADMIN_CONTENS}/images/application/title_profile.gif" alt="応募者プロフィール" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">応募ID</th>
					<td>${f:h(id)}</td>
				</tr>
				<tr>
					<th>応募日時</th>
					<td><fmt:formatDate value="${applicationDatetime}" pattern="<%=GourmetCareeConstants.DATE_TIME_FORMAT %>"/> &nbsp;</td>
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
					<td>${f:h(age)}&nbsp;</td>
				</tr>
				<tr>
					<th>住所</th>
					<td>${f:label(prefecturesCd, prefecturesList, 'value', 'label')}${f:h(municipality)}<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">${f:h(address)}</c:if>&nbsp;</td>
				</tr>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
					<tr>
						<th>電話番号</th>
						<td>${f:h(phoneNo)}&nbsp;</td>
					</tr>
					<tr>
						<th>メールアドレス</th>
						<td>${f:h(mailAddress)}&nbsp;</td>
					</tr>
				</c:if>
				<tr>
					<th>希望連絡時間・連絡方法</th>
					<td>${f:br(f:h(connectionTime))}&nbsp;</td>
				</tr>
				<tr>
					<th>勤務可能時期</th>
					<td>${f:label(possibleEntryTermKbn, possibleEntryTermKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>現在の職業</th>
					<gt:typeList name="currentList" typeCd="<%=MTypeConstants.CurrentJob.TYPE_CD %>"/>
					<td>${f:label(currentJobKbn, currentList, 'value', 'label')}</td>
				</tr>
				<tr>
					<th>飲食店の経験</th>
					<gt:typeList name="foodExpList" typeCd="<%=MTypeConstants.AriNashiKbn.TYPE_CD %>"/>
					<td>${f:label(foodExpKbn, foodExpList, 'value', 'label')}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">自己PR</th>
					<td class="bdrs_bottom">${f:br(f:h(applicationSelfPr))}&nbsp;</td>
				</tr>
			</table>
			<hr />

			<h3 title="希望条件" class="title"><img src="${ADMIN_CONTENS}/images/application/title_term.gif" alt="希望条件" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">応募先名（店舗名）</th>
					<td>${f:h(applicationName)}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">希望職種</th>
					<td class="bdrs_bottom">${f:h(applicationJob)}</td>
				</tr>
			</table>
			<hr />

			<h3 title="応募情報" class="title"><img src="${ADMIN_CONTENS}/images/application/title_applicationInfo.gif" alt="応募情報" /></h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th class="bdrs_bottom" width="150">端末</th>
					<td class="bdrs_bottom">${f:label(terminalKbn, terminalKbnList, 'value', 'label')}&nbsp;</td>
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
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

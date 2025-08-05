<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"/>

<c:set scope="page" var="FROM_PAGE_KBN" value="<%=ApplicationMailForm.FROM_APPLICANT_DETAIL %>" />


<!-- #main# -->
<div id="main">


<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>

<hr />

<div id="wrap_tab" class="clear">
<ul id="mn_tab" class="clear">
	<li id="mn_noentryMail"><a href="${f:url('/applicationMail/list/')}"  title="応募メールに切替">応募メールに切替</a></li>
	<li id="mn_noPreEntryMail"><a href="${f:url('/preApplicationMail/list/')}"  title="プレ応募メールに切替">プレ応募メールに切替</a></li>
	<li id="mn_noScoutMail"><a href="${f:url('/scoutMail/list/mailBox/')}" title="スカウトメール・気になる通知に切替" class="scout_a">スカウトメール・気になる通知に切替</a></li>
	<li id="mn_noobservationMail"><a href="${f:url('/observateApplicationMail/list/')}" title="質問メールに切替">質問メールに切替</a></li>
	<li id="mn_arbeitMail"><a href="${f:url('/arbeitMail/list/')}" title="グルメdeバイトメールに切替">グルメdeバイトメールに切替</a></li>
</ul>
</div>
<hr />


<div class="wrap_btn">
<s:form action="${f:h('/applicationMail/input/index/')}">

	<input type="button" value="　" onclick="location.href='${f:url('/arbeit/list/showList/')}'" class="btn_backList" />

	<html:hidden property="originalMailId" value="${f:h(firstMailId)}" />
	<html:hidden property="applicationId" value="${f:h(id)}" />
	<html:hidden property="fromPageKbn" value="${FROM_PAGE_KBN}" />
</s:form>

</div>
<hr />

<html:errors />

<c:if test="${existDataFlg}">

<!-- #member_profile# -->
<div id="member_profile" class="wrap_detail">
	<s:form action="${f:h(actionPath)}">
	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
		<tr>
			<th width="150" class="bdrs_bottom">メモ</th>
			<td class="bdrs_bottom">応募者に対するメモを登録できます。<br />

			<html:hidden property="id" />
			<html:hidden property="version" />
			<html:text property="memo" size="45" />&nbsp;<html:submit property="editMemo"  value="　"  styleClass="btn_entry"  /></td>
		</tr>
	</table>
	</s:form>
</div>
<hr />

<!-- #entry_profile# -->
<div id="entry_profile" class="wrap_detail">
<h3 title="応募者プロフィール" id="profile">応募者プロフィール</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th>応募日時</th>
		<td><fmt:formatDate value="${applicationDatetime}" pattern="yyyy/MM/dd HH:mm" /></td>
	</tr>
	<tr>
		<th width="150">応募ID</th>
		<td>${f:h(id)} &nbsp;</td>
	</tr>
	<tr>
		<th>氏名(漢字）</th>
		<td>${f:h(name)} &nbsp;</td>
	</tr>
	<tr>
		<th>氏名(カナ）</th>
		<td>${f:h(nameKana)} &nbsp;</td>
	</tr>
		<tr>
		<th>性別</th>
		<td>${f:label(sexKbn, sexList, 'value', 'label')} &nbsp;</td>
	</tr>
		<tr>
		<th>年齢</th>
		<td>${f:h(age)}才 &nbsp;</td>
	</tr>
	<tr>
		<th>郵便番号</th>
		<td>${f:h(zipCd)} &nbsp;</td>
	</tr>
	<tr>
		<th>住所</th>
		<td>
		<gt:prefecturesList name="prefecturesList"  />
		${f:label(prefecturesCd, prefecturesList, 'value', 'label')}
		${f:h(municipality)}
		${f:h(address)} &nbsp;
		</td>
	</tr>
	<tr>
		<th>電話番号</th>
		<td>${f:h(phoneNo)} &nbsp;</td>
	</tr>

	<tr>
		<th>現在の職業</th>
		<gt:typeList name="currentJobList" typeCd="<%=MTypeConstants.CurrentJob.TYPE_CD %>" />
		<td>${f:label(currentJobKbn, currentJobList, 'value', 'label') }</td>
	</tr>
	<tr>
		<th>勤務可能時期</th>
		<gt:typeList name="possibleList" typeCd="<%=MTypeConstants.PossibleWorkTermKbn.TYPE_CD %>" />
		<td>${f:label(possibleEntryTermKbn, possibleList, 'value', 'label')}</td>
	</tr>
	<tr>
		<th>飲食店勤務の経験</th>
		<gt:typeList name="ariNashiList" typeCd="<%=MTypeConstants.AriNashiKbn.TYPE_CD %>"/>
		<td>${f:label(foodExpKbn, ariNashiList, 'value', 'label')}</td>
	</tr>


	<tr>
		<th>希望連絡時間・連絡方法</th>
		<td>${f:h(connectionTime)} &nbsp;</td>
	</tr>

	<tr>
		<th class="bdrs_bottom">自己PR</th>
		<td class="bdrs_bottom">${f:br(f:h(applicationSelfPr))} &nbsp;</td>

	</tr>
</table>
</div>
<!-- #entry_profile# -->
<hr />

<!-- #entry_term# -->


</c:if>

<!-- #entry_term# -->
<div id="entry_term" class="wrap_detail">
<h3 title="希望条件" id="term">希望条件</h3>
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th width="150">応募先</th>
		<td>${f:h(applicationName)} &nbsp;</td>

	</tr>
	<tr>
		<th class="bdrs_bottom">希望職種</th>
		<td class="bdrs_bottom">${f:h(applicationJob)} &nbsp;</td>
	</tr>

</table>
</div>
<hr />

<p class="go_top top_margin"><a href="#all">▲このページのトップへ</a></p>
</div>
<!-- #main# -->
<hr />



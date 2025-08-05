<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.entity.VJuskillMemberList"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.db.common.service.JuskillMemberListService"%>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title member">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div class="wrap_search">
		<table cellpadding="0" cellspacing="1" border="0" class="search_table btm_margin">
			<tr>
				<th class="td_title">メルマガ配信条件</th>
			</tr>
			<tr>
				<td>
					<c:set var="ID" value="<%= VJuskillMemberList.ID %>" />
					<c:if test="${!empty whereMap[ID]}">
						<span class="attention">ID：</span>
						${f:h(whereMap[ID])}&nbsp;&nbsp;
					</c:if>

					<c:set var="JUSKILL_MEMBER_NO" value="<%= VJuskillMemberList.JUSKILL_MEMBER_NO %>" />
					<c:if test="${!empty whereMap[JUSKILL_MEMBER_NO]}">
						<span class="attention">人材紹介登録者No：</span>
						${f:h(whereMap[JUSKILL_MEMBER_NO])}&nbsp;&nbsp;
					</c:if>

					<c:set var="JUSKILL_MEMBER_NAME" value="<%= VJuskillMemberList.JUSKILL_MEMBER_NAME %>" />
					<c:if test="${!empty whereMap[JUSKILL_MEMBER_NAME]}">
						<span class="attention">氏名：</span>
						${f:h(whereMap[JUSKILL_MEMBER_NAME])}&nbsp;&nbsp;
					</c:if>

					<c:set var="PREFECTURES_CD" value="<%= VJuskillMemberList.PREFECTURES_CD %>" />
					<c:if test="${!empty whereMap[PREFECTURES_CD]}">
						<gt:prefecturesList name="prefecturesList" />
						<span class="attention">都道府県：</span>
						${f:label(whereMap[PREFECTURES_CD], prefecturesList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="FROM_INSERT_DATE" value="<%= JuskillMemberListService.FROM_INSERT_DATE %>" />
					<c:set var="TO_INSERT_DATE" value="<%= JuskillMemberListService.TO_INSERT_DATE %>" />
					<c:if test="${!empty whereMap[FROM_INSERT_DATE] or !empty whereMap[TO_INSERT_DATE]}">
						<span class="attention">登録日：</span>
							<c:if test="${!empty whereMap[FROM_INSERT_DATE]}">
								${f:h(whereMap[TO_INSERT_DATE])}
							</c:if>
							～
							<c:if test="${!empty whereMap[FROM_INSERT_DATE]}">
								${f:h(whereMap[TO_INSERT_DATE])}
							</c:if>&nbsp;&nbsp;
					</c:if>

					<c:set var="HOPE_INDUSTRY" value="<%= VJuskillMemberList.HOPE_INDUSTRY %>" />
					<c:if test="${!empty whereMap[HOPE_INDUSTRY]}">
						<span class="attention">希望業態：</span>
						${f:h(whereMap[HOPE_INDUSTRY])}&nbsp;&nbsp;
					</c:if>

					<c:set var="HOPE_JOB" value="<%= VJuskillMemberList.HOPE_JOB %>" />
					<c:if test="${!empty whereMap[HOPE_JOB]}">
						<span class="attention">雇用形態：</span>
						${f:h(whereMap[HOPE_JOB])}&nbsp;&nbsp;
					</c:if>

					<c:set var="SEX_KBN" value="<%= VJuskillMemberList.SEX_KBN %>" />
					<c:if test="${!empty whereMap[SEX_KBN]}">
						<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
						<span class="attention">性別：</span>
						${f:label(whereMap[SEX_KBN], sexList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="LOWER_AGE" value="<%= JuskillMemberListService.LOWER_AGE %>" />
					<c:set var="UPPER_AGE" value="<%= JuskillMemberListService.UPPER_AGE %>" />
					<c:if test="${!empty whereMap[LOWER_AGE] or !empty whereMap[UPPER_AGE]}">
						<span class="attention">年齢：</span>
							<c:if test="${!empty whereMap[LOWER_AGE]}">
								${f:h(whereMap[LOWER_AGE])}
							</c:if>
							～
							<c:if test="${!empty whereMap[UPPER_AGE]}">
								${f:h(whereMap[UPPER_AGE])}
							</c:if>&nbsp;&nbsp;
					</c:if>

					<c:set var="MEMBER_REGISTERED_FLG" value="<%= VJuskillMemberList.MEMBER_REGISTERED_FLG %>" />
					<c:if test="${!empty whereMap[MEMBER_REGISTERED_FLG]}">
						<gt:typeList name="memberRegisteredFlgList" typeCd="<%=MTypeConstants.MemberRegisteredFlg.TYPE_CD %>" />
						<span class="attention">会員登録：</span>
						${f:label(whereMap[MEMBER_REGISTERED_FLG], memberRegisteredFlgList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="MAIL" value="<%= VJuskillMemberList.MAIL %>" />
					<c:if test="${!empty whereMap[MAIL]}">
						<span class="attention">メールアドレス：</span>
						${f:h(whereMap[MAIL])}&nbsp;&nbsp;
					</c:if>
				&nbsp;</td>
			</tr>
		</table>
		<hr />
	</div>
	<!-- #wrap_serch# -->

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table btm_margin">
				<tr>
					<th colspan="2" class="bdrs_right">PC版メルマガ</th>
				</tr>
				<tr>
					<th width="150">タイトル</th>
					<td>${f:h(pcMailMagazineTitle)}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">本文</th>
					<td class="bdrs_bottom">${f:br(f:h(pcBody))}</td>
				</tr>
			</table>
			<hr />

<!-- 			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table"> -->
<!-- 				<tr> -->
<!-- 					<th colspan="2" class="bdrs_right">モバイル版メルマガ</th> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<th width="150">タイトル</th> -->
<%-- 					<td>${f:h(mbMailMagazineTitle)}</td> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<th class="bdrs_bottom">本文</th> -->
<%-- 					<td class="bdrs_bottom">${f:br(f:h(mbBody))}</td> --%>
<!-- 				</tr> -->
<!-- 			</table> -->
<!-- 			<hr /> -->

			<div class="wrap_btn">
				<html:submit property="submit" value="送 信" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
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
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

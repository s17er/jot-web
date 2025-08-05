<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.entity.MMember"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.db.common.service.MemberService"%>

<gt:typeList name="deliveryTypeKbnList" typeCd="<%=MTypeConstants.deliveryTypeKbn.TYPE_CD %>"   />
<c:set var="HTML" value="<%=MTypeConstants.deliveryTypeKbn.HTML%>" />
<c:set var="TEXT" value="<%=MTypeConstants.deliveryTypeKbn.TEXT%>" />

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
					<c:set var="ID" value="<%= MMember.ID %>" />
					<c:if test="${!empty whereMap[ID]}">
						<span class="attention">ID：</span>
						${f:h(whereMap[ID])}&nbsp;&nbsp;
					</c:if>

					<c:set var="MEMBER_NAME" value="<%= MMember.MEMBER_NAME %>" />
					<c:if test="${!empty whereMap[MEMBER_NAME]}">
						<span class="attention">氏名：</span>
						${f:h(whereMap[MEMBER_NAME])}&nbsp;&nbsp;
					</c:if>

					<c:set var="AREA_CD" value="<%= MMember.AREA_CD %>" />
					<c:if test="${!empty whereMap[AREA_CD]}">
						<gt:areaList name="areaList" authLevel="${userDto.authLevel}"/>
						<span class="attention">エリア：</span>
						${f:label(whereMap[AREA_CD], areaList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<gt:prefecturesList name="prefecturesList" />
					<c:set var="HOPE_AREA" value="hope_area" />
					<c:if test="${!empty whereMap[HOPE_AREA]}">
						<span class="attention">希望勤務地：</span>
						${f:label(whereMap[HOPE_AREA], prefecturesList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="PREFECTURES_CD" value="<%= MMember.PREFECTURES_CD %>" />
					<c:if test="${!empty whereMap[PREFECTURES_CD]}">
						<span class="attention">都道府県：</span>
						${f:label(whereMap[PREFECTURES_CD], prefecturesList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="INDUSTRY_KBN" value="<%= MTypeConstants.IndustryKbn.TYPE_CD %>" />
					<c:if test="${!empty whereMap[INDUSTRY_KBN]}">
						<gt:typeList name="industryList" typeCd="${INDUSTRY_KBN}" />
						<span class="attention">希望業態：</span>
						${f:label(whereMap[INDUSTRY_KBN], industryList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="EMPLOY_PTN_KBN" value="<%= MMember.EMPLOY_PTN_KBN %>" />
					<c:if test="${!empty whereMap[EMPLOY_PTN_KBN]}">
						<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
						<span class="attention">雇用形態：</span>
						${f:label(whereMap[EMPLOY_PTN_KBN], employPtnList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="SEX_KBN" value="<%= MMember.SEX_KBN %>" />
					<c:if test="${!empty whereMap[SEX_KBN]}">
						<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
						<span class="attention">性別：</span>
						${f:label(whereMap[SEX_KBN], sexList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="LOWER_AGE" value="<%= MemberService.LOWER_AGE %>" />
					<c:set var="UPPER_AGE" value="<%= MemberService.UPPER_AGE %>" />
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

					<c:set var="FROM_UPDATE_DATE" value="<%= MemberService.FROM_UPDATE_DATE %>" />
					<c:set var="TO_UPDATE_DATE" value="<%= MemberService.TO_UPDATE_DATE %>" />
					<c:if test="${!empty whereMap[FROM_UPDATE_DATE] or !empty whereMap[TO_UPDATE_DATE]}">
						<span class="attention">更新日：</span>
							<c:if test="${!empty whereMap[FROM_UPDATE_DATE]}">
								${f:h(whereMap[FROM_UPDATE_DATE])}
							</c:if>
							～
							<c:if test="${!empty whereMap[TO_UPDATE_DATE]}">
								${f:h(whereMap[TO_UPDATE_DATE])}
							</c:if>&nbsp;&nbsp;
					</c:if>

					<c:set var="JUSKILL_FLG" value="<%= MMember.JUSKILL_FLG %>" />
					<c:if test="${!empty whereMap[JUSKILL_FLG]}">
						<gt:typeList name="juskillList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>" />
						<span class="attention">ジャスキル登録：</span>
						${f:label(whereMap[JUSKILL_FLG], juskillList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="JUSKILL_CONTACT_FLG" value="<%= MMember.JUSKILL_CONTACT_FLG %>" />
					<c:if test="${!empty whereMap[JUSKILL_CONTACT_FLG]}">
						<gt:typeList name="juskillContactList" typeCd="<%=MTypeConstants.JuskillContactFlg.TYPE_CD %>" />
						<span class="attention">転職相談窓口：</span>
						${f:label(whereMap[JUSKILL_CONTACT_FLG], juskillContactList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="LOGIN_ID" value="<%= MMember.LOGIN_ID %>" />
					<c:if test="${!empty whereMap[LOGIN_ID]}">
						<span class="attention">メールアドレス：</span>
						${f:h(whereMap[LOGIN_ID])}&nbsp;&nbsp;
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
					<th colspan="2" class="bdrs_right">会員版メルマガ</th>
				</tr>
				<tr>
					<th width="150">タイトル</th>
					<td>${f:h(mailMagazineTitle)}</td>
				</tr>
				<tr>
					<th width="150">配信形式</th>
					<td>${f:label(deliveryType, deliveryTypeKbnList, 'value', 'label')}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">本文</th>
					<td class="bdrs_bottom">
					<c:choose>
						<c:when test="${deliveryType eq HTML }">
							${htmlBody}
						</c:when>
						<c:when test="${deliveryType eq TEXT }">
							${f:br(f:h(textBody))}
						</c:when>
					</c:choose>
					</td>
				</tr>
			</table>
			<hr />

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

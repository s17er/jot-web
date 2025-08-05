<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<gt:typeList name="otherConditionList" typeCd="<%=MTypeConstants.OtherBackConditionKbn.TYPE_CD %>" />
<gt:typeList name="treatmentList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />

<% /* CSSファイルを設定 */ %>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery.lightbox.css" />
<% /* javascriptファイルを設定 */ %>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.core.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.lightbox.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/checkConfFlg.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/preview.js"></script>


<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle2)}" class="title date">${f:h(pageTitle2)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}" enctype="multipart/form-data">

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
				<tr>
					<th>タイトル</th>
					<td>${terminalTitle}</td>
				</tr>
				<tr>
					<th>都道府県</th>
					<td>
						<gt:prefecturesList name="prefecturesList"/>
						${f:label(prefecturesCd, prefecturesList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th>路線・最寄駅</th>
					<td>
						<gt:rRailroadList name="railroadList" limitValue="${prefecturesCd}" />
						<c:forEach items="${sendJson}" var="dto" varStatus="status">
							<gt:rRouteList name="routeList" limitValue="${dto['companyCd']}" />
							<gt:rStationList name="stationList" limitValue="${dto['lineCd']}" />
							${f:label(dto['companyCd'], railroadList, 'value', 'label')}&nbsp;：&nbsp;
							${f:label(dto['lineCd'], routeList, 'value', 'label')}&nbsp;：&nbsp;
							${f:label(dto['stationCd'], stationList, 'value', 'label')}&nbsp;&nbsp;&nbsp;
							<br>
							<br>
						</c:forEach>
					</td>
				</tr>

			</table>
			<div class="wrap_btn">
				<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" onclick="onclickWebDataSbumitBtn();" />
				<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>
		</s:form>

		<c:if test="${pageKbn eq PAGE_EDIT}">
			<s:form action="${f:h(actionPath)}" styleId="editForm">
			</s:form>
		</c:if>
		<c:if test="${pageKbn eq PAGE_DETAIL}">
			<% //コピーは画面表示データのみ行うことができる（アドレス直打ちの対応）  %>
			<s:form action="${f:h(copyActionPath)}" enctype="multipart/form-data" styleId="inputForm" >
				<html:hidden property="id" value="${f:h(id)}" />
				<html:hidden property="processFlg" styleId="processFlg" />
			</s:form>
			<% //削除の場合は、確認のためにフラグを立ててから画面遷移  %>
			<s:form action="${f:h(deleteActionPath)}" enctype="multipart/form-data" styleId="deleteForm" >
				<html:hidden property="id" value="${f:h(id)}" />
				<html:hidden property="processFlg" styleId="delProcessFlg" />
				<html:hidden property="version" value="${f:h(version)}" />
			</s:form>
			<% //ステータス変更の場合は、確認のためフラグを立ててから画面遷移  %>
			<s:form action="${f:h(editActionPath)}" enctype="multipart/form-data" styleId="editForm">
				<html:hidden property="id" value="${f:h(id)}" />
				<html:hidden property="displayStatus" value="${f:h(displayStatus)}" />
				<html:hidden property="processFlg" styleId="checkStatusFlg" />
				<html:hidden property="changeStatusKbn" styleId="changeStatusKbn" value="" />
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
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />


</div>
<!-- #main# -->
<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>
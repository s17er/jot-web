<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
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
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
				<tr>
					<th width="60" class="posi_center">メルマガID</th>
					<th>配信日時</th>
					<th class="bdrs_right">配信先区分</th>
				</tr>
				<tr>
					<td class="posi_center">${id}</td>
					<td>
						<c:set var="DELIVERYFLG_NON" value="<%= MTypeConstants.DeliveryFlg.NON %>" />
						<c:choose>
							<c:when test="${DELIVERYFLG_NON eq deliveryFlg}">
								<% // 未配信の場合は文言を表示 %>
								<gt:typeList name="deliveryFlgList" typeCd="<%=MTypeConstants.DeliveryFlg.TYPE_CD %>" />
								${f:label(deliveryFlg, deliveryFlgList, 'value', 'label')}
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${f:date(deliveryDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" />&nbsp;
							</c:otherwise>
						</c:choose>
					</td>
					<td class="bdrs_right">
						<gt:typeList name="deliveryKbnList" typeCd="<%=MTypeConstants.DeliveryKbn.TYPE_CD %>" />
						${f:label(deliveryKbn, deliveryKbnList, 'value', 'label')}
					</td>
				</tr>
			</table>
			<hr />

			<gt:typeList name="TerminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>" />
			<c:forEach var="entity" varStatus="status" items="${detailList}">
				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table top_margin detail_table btm_margin">
						<tr>
							<th colspan="2" class="bdrs_right">${f:label(entity.terminalKbn, TerminalKbnList, 'value', 'label')}版メルマガ</th>
						</tr>
						<tr>
							<th width="150">タイトル</th>
							<td>${f:h(entity.mailMagazineTitle)}&nbsp;</td>
						</tr>
						<tr>
							<th class="bdrs_bottom">本文</th>
							<td class="bdrs_bottom">${f:br(f:h(entity.body))}&nbsp;</td>
						</tr>
				</table>
				<hr />
			</c:forEach>

			<div class="wrap_btn">
				<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<html:submit property="outPut" value="CSV出力" onclick="if(!confirm('CSVを出力しますか?')) {return false;};" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<html:hidden property="id" />
			</div>
		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />
	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

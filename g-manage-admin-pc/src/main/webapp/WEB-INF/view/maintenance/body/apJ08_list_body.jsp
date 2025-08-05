<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<gt:prefecturesList name="prefecturesList"/>
<script type="text/javascript">

var deleteConfirm = function(id) {

	if (confirm('本当に削除してもよろしいですか？')) {
		 location.href='${f:url(deletePath)}' + id;
	}
}

</script>


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

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<hr />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_result# -->
	<div id="wrap_result" >

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
			<tr>
				<th class="posi_center">グループ名</th>
				<th class="posi_center">都道府県コード</th>
				<th class="posi_center">鉄道会社</th>
				<th class="posi_center">路線</th>
				<th class="posi_center">駅</th>
				<th class="posi_center bdrs_right"></th>
			</tr>
			<% //ループ処理 %>
			<c:forEach items="${terminalIdList}" var="id" varStatus="status">
				<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
				<c:forEach items="${terminalDetailMap[id]}" var="terminalInfo" varStatus="infoStatus">
					<c:if test="${infoStatus.first}">
						<tr>
							<td class="posi_center ${classStr}" rowspan="${fn:length(terminalDetailMap[id]) + 1}">${terminalTitleMap[id]}</td>
						</tr>
					</c:if>
					<tr>
						<gt:rRailroadList name="railroadList" limitValue="${terminalInfo.prefecturesCd}" />
						<gt:rRouteList name="routeList" limitValue="${terminalInfo.companyCd}"/>
						<gt:rStationList name="stationList" limitValue="${terminalInfo.lineCd}"/>
						<td class="posi_center ${classStr}">${f:label(terminalInfo.prefecturesCd, prefecturesList, 'value', 'label')}</td>
						<td class="posi_center ${classStr}">${f:label(terminalInfo.companyCd, railroadList, 'value', 'label')}</td>
						<td class="posi_center ${classStr}">${f:label(terminalInfo.lineCd, routeList, 'value', 'label')}</td>
						<td class="posi_center ${classStr}">${f:label(terminalInfo.stationCd, stationList, 'value', 'label')}</td>
						<c:if test="${infoStatus.first}">
							<td class="posi_center bdrs_right ${classStr}" rowspan="${fn:length(terminalDetailMap[id]) + 1}">
								<input type="button" name="edit" value="編 集" onclick="location.href='${f:url(editPath)}${id}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />&nbsp;
								<input type="button" name="delete" value="削 除" onclick="deleteConfirm(${id});" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
							</td>
						</c:if>
					</tr>
				</c:forEach>
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

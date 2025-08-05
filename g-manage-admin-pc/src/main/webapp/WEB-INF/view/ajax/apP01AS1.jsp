<%@page pageEncoding="UTF-8"%>
<% // 駅のプルダウン %>
<gt:stationList name="stationList" limitValue="${limitValue}" blankLineLabel="${common['gc.pullDown']}" />

<c:if test="${disabledFlg eq false}" >
	<select name="stationId" id="stationId">
		<c:forEach items="${stationList}" var="s">
			<option value="${f:h(s.value)}">${f:h(s.label)}</option>
		</c:forEach>
	</select>&nbsp;
</c:if>
<c:if test="${disabledFlg eq true}" >
	<select name="stationId" id="stationId" >
		<c:forEach items="${stationList}" var="s">
			<option value="${f:h(s.value)}">${f:h(s.label)}</option>
		</c:forEach>
	</select>&nbsp;
</c:if>
<%@page pageEncoding="UTF-8"%>
<% // 鉄道会社のプルダウン  %>
<gt:railroadList name="railroadList" limitValue="${limitValue}" blankLineLabel="${common['gc.pullDown']}" />

<c:if test="${disabledFlg eq false}" >
	<select name="railroadId" id="railroadId" onchange="railroadLimitLoad(); return false;" >
		<c:forEach items="${railroadList}" var="r">
			<option value="${f:h(r.value)}">${f:h(r.label)}</option>
		</c:forEach>
	</select>&nbsp;
</c:if>
<c:if test="${disabledFlg eq true}" >
	<select name="railroadId" id="railroadId" onchange="railroadLimitLoad(); return false;">
		<c:forEach items="${railroadList}" var="r">
			<option value="${f:h(r.value)}">${f:h(r.label)}</option>
		</c:forEach>
	</select>&nbsp;
</c:if>
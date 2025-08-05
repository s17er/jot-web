<%@page pageEncoding="UTF-8"%>
<% // 路線のプルダウン  %>
<gt:routeList name="routeList" limitValue="${limitValue}" blankLineLabel="${common['gc.pullDown']}" />

<c:if test="${disabledFlg eq false}" >
	<select name="routeId" id="routeId" onchange="routeLimitLoad(); return false;">
		<c:forEach items="${routeList}" var="l" >
			<option value="${f:h(l.value)}">${f:h(l.label)}</option>
		</c:forEach>
	</select>&nbsp;
</c:if>
<c:if test="${disabledFlg eq true}" >
	<select name="routeId" id="routeId" onchange="routeLimitLoad(); return false;" >
		<c:forEach items="${routeList}" var="l">
			<option value="${f:h(l.value)}">${f:h(l.label)}</option>
		</c:forEach>
	</select>&nbsp;
</c:if>
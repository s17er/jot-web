<%@page pageEncoding="UTF-8"%>
<% //号数のプルダウン  %>
<gt:volumeList name="volumeList" limitValue="${limitValue}" blankLineLabel="${common['gc.pullDown']}" authLevel="${authLevel}" />

<c:if test="${disabledFlg eq false}" >
	<select name="volumeId" id="volumeId">
		<c:forEach items="${volumeList}" var="v">
			<option value="${f:h(v.value)}">${f:h(v.label)}</option>
		</c:forEach>
	</select>
</c:if>

<c:if test="${disabledFlg eq true}" >
	<select name="volumeId" id="volumeId" disabled="disabled">
		<c:forEach items="${volumeList}" var="v">
			<option value="${f:h(v.value)}">${f:h(v.label)}</option>
		</c:forEach>
	</select>
</c:if>
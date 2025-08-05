<%@page pageEncoding="UTF-8"%>
<gt:specialList name="specialList" blankLineLabel="${common['gc.pullDown']}" limitValue="${limitValue}"/>

<select name="specialIdList" id="specialSelect" style="min-width:270px">
	<c:forEach items="${specialList}" var="s">
		<option value="${f:h(s.value)}">${f:h(s.label)}</option>
	</c:forEach>
</select>

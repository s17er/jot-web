<%@page pageEncoding="UTF-8"%>
<gt:assignedSalesList name="salesList" limitValue="${limitValue}" blankLineLabel="${common['gc.pullDown']}" />

<c:if test="${disabledFlg eq false}" >
	<select name="salesId" id="salesId">
		<c:forEach items="${salesList}" var="s">
			<option value="${f:h(s.value)}">${f:h(s.label)}</option>
		</c:forEach>
	</select>
</c:if>
<c:if test="${disabledFlg eq true}" >
	<select name="salesId" id="salesId">
		<c:forEach items="${salesList}" var="s">
			<option value="${f:h(s.value)}">${f:h(s.label)}</option>
		</c:forEach>
	</select>
	<input type="hidden" name="salesId" value="" />
</c:if>
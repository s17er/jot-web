<%@page pageEncoding="UTF-8"%>
<gt:assignedSalesList name="assignedSales" limitValue="${limitValue}" blankLineLabel="--" />

<c:if test="${disabledFlg eq false}" >
				<select name="assignedSalesId" id="assignedSalesId">
					<c:forEach items="${assignedSales}" var="s">
						<option value="${f:h(s.value)}">${f:h(s.label)}</option>
					</c:forEach>
				</select>&nbsp;
</c:if>
<c:if test="${disabledFlg eq true}" >
				<select name="assignedSalesId" id="assignedSalesId">
					<c:forEach items="${assignedSales}" var="s">
						<option value="${f:h(s.value)}">${f:h(s.label)}</option>
					</c:forEach>
				</select>&nbsp;
				<input type="hidden" name="assignedSalesId" value=""/>
</c:if>
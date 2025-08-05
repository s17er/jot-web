<%@page pageEncoding="UTF-8"%>
<gt:assignedCompanyList name="assignedCompany" limitValue="${limitValue}" authLevel="${userDto.authLevel}" blankLineLabel="--" />

<c:if test="${disabledFlg eq false}" >
				<select name="assignedCompanyId" id="assignedCompanyId" onchange="assignedCompanyLimitLoad(); return false;" >
					<c:forEach items="${assignedCompany}" var="l" >
						<option value="${f:h(l.value)}">${f:h(l.label)}</option>
					</c:forEach>
				</select>&nbsp;
</c:if>
<c:if test="${disabledFlg eq true}" >
				<select name="assignedCompanyId" id="assignedCompanyId"  onchange="assignedCompanyLimitLoad(); return false;" >
					<c:forEach items="${assignedCompany}" var="l">
						<option value="${f:h(l.value)}">${f:h(l.label)}</option>
					</c:forEach>
				</select>&nbsp;
</c:if>
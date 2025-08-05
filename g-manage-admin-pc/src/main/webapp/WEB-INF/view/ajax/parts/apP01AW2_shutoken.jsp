<%@page pageEncoding="UTF-8" %>
<% // 勤務地エリア(WEBエリアから名称変更)のチェックボックス %>
<gt:typeList name="webAreaList" typeCd="${typeCd}" noDisplayValue="${noDisplayList}"/>
<gt:typeList name="foreignAreaList" typeCd="${typeCd2}"/>
<c:if test="${disabledFlg eq false}">
    <c:forEach items="${webAreaList}" var="t">
				<span>
					<input type="checkbox" name="webAreaKbnList" value="${f:h(t.value)}"
                           id="webAreaKbn_${f:h(t.value)}"/>
					<label for="webAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
    </c:forEach>
    <c:forEach items="${foreignAreaList}" var="t">
				<span>
					<input type="checkbox" name="foreignAreaKbnList" value="${f:h(t.value)}"
                           id="foreignAreaKbn_${f:h(t.value)}"/>
					<label for="foreignAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
    </c:forEach>
</c:if>
<c:if test="${disabledFlg eq true}">
    <c:forEach items="${webAreaList}" var="t">
				<span>
					<input type="checkbox" name="webAreaKbnList" value="${f:h(t.value)}" id="webAreaKbn_${f:h(t.value)}"
                           disabled="disabled"/>
					<label for="webAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
    </c:forEach>
    <c:forEach items="${foreignAreaList}" var="t">
				<span>
					<input type="checkbox" name="foreignAreaKbnList" value="${f:h(t.value)}"
                           id="foreignAreaKbn_${f:h(t.value)}" disabled="disabled"/>
					<label for="foreignAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
    </c:forEach>
</c:if>



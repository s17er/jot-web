<%@page pageEncoding="UTF-8"%>
<% // 主要駅のチェックボックス %>
<c:choose>
	<c:when test="${empty typeCd}">
		<input type="checkbox" name="mainStationKbnList" value="" id="mainStationKbn" disabled="disabled" />
		<label for="mainStationKbn" style="color: #999">エリアを選択して下さい。</label>
	</c:when>
	<c:otherwise>
		<gt:typeList name="mainStationList" typeCd="${typeCd}"/>

		<c:if test="${disabledFlg eq false}" >
			<c:forEach items="${mainStationList}" var="t">
				<span>
					<input type="checkbox" name="mainStationKbnList" value="${f:h(t.value)}" id="mainStationKbn_${f:h(t.value)}" />
					<label for="mainStationKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
			</c:forEach>
		</c:if>

		<c:if test="${disabledFlg eq true}" >
			<c:forEach items="${mainStationList}" var="t">
				<span>
					<input type="checkbox" name="mainStationKbnList" value="${f:h(t.value)}" id="mainStationKbn_${f:h(t.value)}" disabled="disabled" />
					<label for="mainStationKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
			</c:forEach>
		</c:if>
	</c:otherwise>
</c:choose>

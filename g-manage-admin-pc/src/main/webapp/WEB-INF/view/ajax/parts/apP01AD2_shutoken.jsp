<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<% // WEBエリアのチェックボックス %>
<c:choose>
	<c:when test="${empty typeCd}">
		<input type="checkbox" name="detailAreaKbnList" value="" id="webAreaKbn" disabled="disabled" />
		<label for="detailAreaKbn" style="color: #999">エリアを選択して下さい。</label>
	</c:when>
	<c:otherwise>
		<gt:typeList name="detailAreaList" typeCd="${typeCd}" noDisplayValue="${noDisplayList}" />
		<c:if test="${disabledFlg eq false}" >
			<c:forEach items="${detailAreaList}" var="t">
				<span>
					<input type="checkbox" name="detailAreaKbnList" value="${f:h(t.value)}" id="detailAreaKbn_${f:h(t.value)}" />
					<label for="detailAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
			</c:forEach>
		</c:if>
		<c:if test="${disabledFlg eq true}" >
			<c:forEach items="${detailAreaList}" var="t">
				<span>
					<input type="checkbox" name="detailAreaKbnList" value="${f:h(t.value)}" id="detailAreaKbn_${f:h(t.value)}" disabled="disabled" />
					<label for="detailAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
				</span>
			</c:forEach>
		</c:if>
	</c:otherwise>
</c:choose>



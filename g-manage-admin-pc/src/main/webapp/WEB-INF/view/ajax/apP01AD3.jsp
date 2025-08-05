<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<% // 勤務地エリア(WEBエリアから名称変更)のチェックボックス %>
<c:choose>
	<c:when test="${empty typeCd}">
		<input type="checkbox" name="webAreaKbnList" value="" id="webAreaKbn" disabled="disabled" />
		<label for="webAreaKbn" style="color: #999">エリアを選択して下さい。</label>
	</c:when>
	<c:otherwise>
		<gt:typeList name="webAreaList" typeCd="${typeCd}" noDisplayValue="${noDisplayList}" />
		<gt:typeList name="webAreaDescriptionList" typeCd="<%=MTypeConstants.ShutokenWebAreaDescriptionKbn.TYPE_CD %>" noDisplayValue="<%=MTypeConstants.ShutokenWebAreaKbn.getWebdataNoDispList() %>" />
		<c:if test="${disabledFlg eq false}" >
			<c:forEach items="${webAreaList}" var="t">
				<span>
					<input type="checkbox" name="webAreaKbnList" value="${f:h(t.value)}" id="webAreaKbn_${f:h(t.value)}" />
					<label for="webAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
					<c:set var="webAreaDescription" value="${f:label(t.value, webAreaDescriptionList, 'value', 'label')}" scope="page"/>
					<c:if test="${not empty webAreaDescription}">
						<br />
						&nbsp;&nbsp;&nbsp;&nbsp;(${f:h(webAreaDescription)})
					</c:if>
				</span>
			</c:forEach>
		</c:if>
		<c:if test="${disabledFlg eq true}" >
			<c:forEach items="${webAreaList}" var="t">
				<span>
					<input type="checkbox" name="webAreaKbnList" value="${f:h(t.value)}" id="webAreaKbn_${f:h(t.value)}" disabled="disabled" />
					<label for="webAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
					<c:set var="webAreaDescription" value="${f:label(t.value, webAreaDescriptionList, 'value', 'label')}" scope="page"/>
					<c:if test="${not empty webAreaDescription}">
						<br />
						&nbsp;&nbsp;&nbsp;&nbsp;(${f:h(webAreaDescription)})
					</c:if>
				</span>
			</c:forEach>
		</c:if>
	</c:otherwise>
</c:choose>



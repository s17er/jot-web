<%@ page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<%@page pageEncoding="UTF-8"%>
<% // 勤務地エリア(WEBエリアから名称変更)のチェックボックス %>
<c:set var="SENDAI" value="<%=MAreaConstants.AreaCd.SENDAI_AREA%>" />
<c:choose>
	<c:when test="${empty typeCd || disabledFlg eq true}">
		<select name="webAreaKbn" id="webAreaKbn" disabled="disabled">
			<option value="">--</option>
		</select>&nbsp;
	</c:when>
	<c:otherwise>
		<gt:typeList name="webAreaList" typeCd="${typeCd}" noDisplayValue="${noDisplayList}" blankLineLabel="${common['gc.pullDown']}" />
		<gt:typeList name="foreignAreaList" typeCd="${typeCd2}"/>



        <c:choose>
            <c:when test="${limitValue eq SENDAI}">
                <select name="webAreaKbn" id="webAreaKbn">
                    <option value="">${f:h(common['gc.pullDown'])}</option>
                    <gt:webAreaKbnPrefectureList name="prefList" areaCd="${limitValue}" scope="page"/>
                    <c:forEach items="${prefList}" var="pref">
                        <c:forEach items="${pref.typeList}" var="area">
                            <c:choose>
                                <c:when test="${fn:length(area.childrenTypeList) eq 0}">
                                    <option value="${f:h(area.typeValue)}">${f:h(area.typeName)}</option>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${area.childrenTypeList}" var="child">
                                        <option value="${f:h(child.typeValue)}">${f:h(child.typeName)}</option>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>

                        </c:forEach>
                    </c:forEach>
                </select>
            </c:when>
            <c:otherwise>
                <select name="webAreaKbn" id="webAreaKbn">
                    <c:forEach items="${webAreaList}" var="dto">
                        <option value="${f:h(dto.value)}">${f:h(dto.label)}</option>
                    </c:forEach>
                    <c:forEach items="${foreignAreaList}" var="dto">
                        <option value="${f:h(dto.value)}">${f:h(dto.label)}</option>
                    </c:forEach>
                </select>
            </c:otherwise>
        </c:choose>

		&nbsp;
	</c:otherwise>
</c:choose>



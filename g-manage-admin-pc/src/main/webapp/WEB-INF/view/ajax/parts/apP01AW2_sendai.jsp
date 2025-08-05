<%@page pageEncoding="UTF-8" %>
<% // 勤務地エリア(WEBエリアから名称変更)のチェックボックス %>


<gt:webAreaKbnPrefectureList name="prefList" areaCd="${limitValue}" scope="page"/>

<c:forEach items="${prefList}" var="pref">
    <c:if test="${pref.prefectureCd gt 0}">
        <h3>【${f:h(pref.prefectureName)}】</h3>
    </c:if>
    
    <c:forEach items="${pref.typeList}" var="area">
        <c:set var="hasChildren" value="${fn:length(area.childrenTypeList) gt 0}" />
        <c:set var="id" value="webAreaKbn_${area.typeValue}" />
        <c:choose>
            <c:when test="${hasChildren eq false}">
                <span><input type="checkbox" name="webAreaKbnList" value="${f:h(area.typeValue)}" id="${f:h(id)}" />
                <label for="${f:h(id)}">&nbsp;${f:h(area.typeName)}</label></span>
            </c:when>
            <c:otherwise>
                <dl>
                    <dt>&nbsp;&nbsp;${f:h(area.typeName)}</dt>
                    <dd>
                        <c:forEach items="${area.childrenTypeList}" var="child">
                            <c:set var="cId" value="webAreaKbn_${child.typeValue}" />
                            <span><input type="checkbox" name="webAreaKbnList" value="${f:h(child.typeValue)}" id="${f:h(cId)}">
                            <label for="${f:h(cId)}">&nbsp;${f:h(child.typeName)}</label></span>
                        </c:forEach>
                    </dd>
                </dl>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</c:forEach>
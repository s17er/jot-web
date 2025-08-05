<%@page pageEncoding="UTF-8" %>
<%@ page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%
    String areaCd = String.valueOf(request.getAttribute("areaCd"));
    pageContext.setAttribute("typeCd", MTypeConstants.DetailAreaKbn.getTypeCd(areaCd));

%>
<gt:typeList name="detailAreaList" typeCd="${typeCd}"/>
<c:forEach items="${detailAreaList}" var="t">
    <span>
        <html:multibox property="detailAreaKbnList" value="${f:h(t.value)}" styleId="detailAreaKbn_${f:h(t.value)}"/>
        <label for="detailAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
    </span>
</c:forEach>
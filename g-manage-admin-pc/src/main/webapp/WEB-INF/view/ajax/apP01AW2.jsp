<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<% // 勤務地エリア(WEBエリアから名称変更)のチェックボックス %>
<c:set var="SENDAI_AREA" value="<%=MTypeConstants.SendaiWebAreaKbn.TYPE_CD%>" />

<c:choose>
    <c:when test="${typeCd eq SENDAI_AREA}" >
        <jsp:include page="parts/apP01AW2_sendai.jsp" />
    </c:when>
    <c:otherwise>
        <jsp:include page="parts/apP01AW2_shutoken.jsp" />
    </c:otherwise>
</c:choose>

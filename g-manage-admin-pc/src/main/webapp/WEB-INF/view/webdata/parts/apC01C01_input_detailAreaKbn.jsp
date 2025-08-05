<%@page pageEncoding="UTF-8" %>
<%@ page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<c:if test="${not empty areaCd}">
    <c:set var="SENDAI" value="<%=MAreaConstants.AreaCd.SENDAI_AREA%>" />
    <c:choose>
        <c:when test="${areaCd eq SENDAI}">
            <jsp:include page="apC01C01_input_detailAreaKbn_sendai.jsp" />
        </c:when>
        <c:otherwise>
            <jsp:include page="apC01C01_input_detailAreaKbn_shutoken.jsp" />
        </c:otherwise>
    </c:choose>
</c:if>
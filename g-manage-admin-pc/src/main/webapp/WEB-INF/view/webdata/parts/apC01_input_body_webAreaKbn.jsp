<%@page pageEncoding="UTF-8" %>
<%@ page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>

<c:set var="SENDAI_AREA" value="<%= MAreaConstants.AreaCd.SENDAI_AREA %>" scope="request"/>


<div id="webAreaAjax" class="autoDispWrap1">
    <c:if test="${not empty areaCd}">
        <c:choose>
            <c:when test="${areaCd eq SENDAI_AREA}">
                <jsp:include page="apC01_input_body_webAreaKbn_sendai.jsp" />
            </c:when>
            <c:otherwise>
                <jsp:include page="apC01_input_body_webAreaKbn_shutoken.jsp" />
            </c:otherwise>
        </c:choose>

    </c:if>
</div>
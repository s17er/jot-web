<%@page pageEncoding="UTF-8" %>
<%@ page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<c:set var="SENDAI" value="<%=MAreaConstants.AreaCd.SENDAI_AREA%>" />
<% // WEBエリアのチェックボックス %>
<c:choose>
    <c:when test="${empty typeCd}">
        <input type="checkbox" name="detailAreaKbnList" value="" id="detailAreaKbn" disabled="disabled"/>
        <label for="detailAreaKbn" style="color: #999">エリアを選択して下さい。</label>
    </c:when>
    <c:when test="${limitValue eq SENDAI}" >
        <jsp:include page="parts/apP01AD2_sendai.jsp" />
    </c:when>
    <c:otherwise>
        <jsp:include page="parts/apP01AD2_shutoken.jsp" />
    </c:otherwise>
</c:choose>



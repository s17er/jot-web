<%@page pageEncoding="UTF-8"%>

<%-- ヘッダの部分(掲載期間、店舗名) --%>

<!-- // 掲載期間 -->
<div id="kikan">掲載期間：
<fmt:formatDate value="${postStartDatetime}" pattern="yyyy/MM/dd" />
 ～  <fmt:formatDate value="${postEndDatetime}" pattern="yyyy/MM/dd" />
</div>

<c:if test="${not empty detailHeaderTab}">
    <jsp:include page="${detailHeaderTab}" />
</c:if>
<!-- // 店舗・企業ネーム -->
<h3>${f:h(manuscriptName)}</h3>
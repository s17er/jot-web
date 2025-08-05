<%@page pageEncoding="UTF-8"%>



<c:choose>
    <c:when test="${messagePageFlg}">
        <c:set var="messageSelectOff" value="" />
        <c:set var="detailSelectOff" value=" select_off" />
    </c:when>
    <c:otherwise>
        <c:set var="messageSelectOff" value=" select_off" />
        <c:set var="detailSelectOff" value="" />
    </c:otherwise>
</c:choose>
<div id="moreDetail_top">
    <div class="message_link_top ${f:h(messageSelectOff)}">
        <a href="${f:url(messageDetailPath)}">メッセージ</a>
    </div>
    <div class="detail_link_top ${f:h(detailSelectOff)}">
        <a href="${f:url(jobDetailPath)}">求人詳細情報</a>
    </div>
</div>
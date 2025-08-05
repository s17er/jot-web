<%@ page import="com.gourmetcaree.common.constants.LabelConstants" %>
<%@ page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm" %><%--
ヘッダ部のcontentsを表示するためのパーツJSP
掲載期間とかのやつは body/fsB01R_header.jsp を参照。
--%>
<%@page pageEncoding="UTF-8"%>
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" scope="page"/>

<c:choose>
    <c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
        <c:set var="SHOP_LIST_PATH" value="${gf:makePathConcat1Arg('/shopListPreview/smartList/sessionIndex', inputFormKbn)}" scope="page" />
    </c:when>
    <c:otherwise>
        <c:set var="SHOP_LIST_PATH" value="${gf:makePathConcat1Arg('/shopListPreview/smartList/index', id)}" scope="page" />
    </c:otherwise>
</c:choose>

<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<c:set var="ICON_SIZE" value="20" scope="page" />

<div id="industry">
    <span>【業態】</span>
    ${f:h(recruitmentIndustry)}
</div>
<div id="contents" class="clearfix">
    <ul id="icon">
        <c:forEach items="${employPtnList}" var="t"  >
            <li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconEmploy<fmt:formatNumber value="${f:h(t)}" pattern="00" />.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}"/></li>
        </c:forEach>
        <c:forEach items="${treatmentKbnList}" var="t">
            <li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconTreatment<fmt:formatNumber value="${f:h(t)}" pattern="00" />.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}"></li>
        </c:forEach>
        <c:forEach items="${otherConditionKbnList}" var="t">
            <c:if test="${t ne 1}">
                <li><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/search/iconOtherCondition<fmt:formatNumber value="${f:h(t)}" pattern="00" />.png" width="${f:h(ICON_SIZE)}" height="${f:h(ICON_SIZE)}"></li>
            </c:if>
        </c:forEach>
    </ul>


    <c:if test="${shopListDisplayFlg eq true or not empty topInterviewUrl}">
        <div class="storeInfo">
            <c:if test="${not empty topInterviewUrl}">
                <a class="interview" href="${topInterviewUrl}">インタビュー</a>
            </c:if>

            <c:if test="${shopListDisplayFlg eq true}">
                <a href="${f:url(SHOP_LIST_PATH)}">${f:h(LABEL_SHOPLIST)}</a>
            </c:if>
        </div>
    </c:if>

</div>
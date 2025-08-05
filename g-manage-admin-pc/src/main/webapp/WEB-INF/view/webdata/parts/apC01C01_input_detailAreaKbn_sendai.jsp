<%@page pageEncoding="UTF-8"%>

<gt:detailAreaKbnPrefectureList name="detailList" areaCd="${areaCd}" />

<c:forEach items="${detailList}" var="detail" varStatus="status">
    <div class="autoDispWrap3">
        <c:choose>
            <c:when test="${detail.prefectureCd gt 0}">
                <c:set var="panel" value="Panel${status.count}" />
                <label class="area_acc_btn" for="${f:h(panel)}">${detail.prefectureName}<span class="ac_caution">クリックすると詳細エリアが表示されます。</span></label>
                <input type="checkbox" id="${f:h(panel)}" class="on-off on-box${status.count}" />

                <ul>
                    <c:forEach items="${detail.typeList}" var="t">
                        <c:set var="label" value="detailAreaKbn_${t.typeValue}" />
                        <li>
                            <html:multibox property="detailAreaKbnList" value="${f:h(t.typeValue)}" styleId="${f:h(label)}" />
                            <label for="${f:h(label)}">&nbsp;${f:h(t.typeName)}</label>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <c:forEach items="${detail.typeList}" var="t">
                    <c:set var="label" value="detailAreaKbn_${t.typeValue}" />
                    <html:multibox property="detailAreaKbnList" value="${f:h(t.typeValue)}" styleId="${f:h(label)}" />
                    <label for="${f:h(label)}">&nbsp;${f:h(t.typeName)}</label>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

</c:forEach>
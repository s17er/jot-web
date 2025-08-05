<%@ page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page pageEncoding="UTF-8" %>

<gt:typeList name="webAreaList" typeCd="<%=MTypeConstants.ShutokenWebAreaKbn.TYPE_CD %>"
             noDisplayValue="<%=MTypeConstants.ShutokenWebAreaKbn.getWebdataNoDispList() %>"/>
<gt:typeList name="foreignAreaList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>"/>

<c:forEach items="${webAreaList}" var="t">
<span>
<html:multibox property="webAreaKbnList" value="${f:h(t.value)}"
               styleId="webAreaKbn_${f:h(t.value)}"/>
<label for="webAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
</span>
</c:forEach>
<c:forEach items="${foreignAreaList}" var="t">
<span>
<html:multibox property="foreignAreaKbnList" value="${f:h(t.value)}"
               styleId="foreignAreaKbn_${f:h(t.value)}"/>
<label for="foreignAreaKbn_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
</span>
</c:forEach>
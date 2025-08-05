<%@page import="com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.InputForm.FromPageKbn"%>
<%@page import="com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.InputForm"%>
<%@page pageEncoding="UTF-8"%>

<%
	Object fromObj = session.getAttribute(InputForm.FROM_PAGE_SESSION_KEY);
	InputForm.FromPageKbn fromPageKbn;
	if (fromObj == null) {
		fromPageKbn = InputForm.FromPageKbn.MEMBER;
	} else {
		fromPageKbn = (InputForm.FromPageKbn) fromObj;
	}

	pageContext.setAttribute("fromPageKbn", fromPageKbn);
%>
<c:set var="FROM_PAGE_KEEP_BOX" value="<%=InputForm.FromPageKbn.KEEP_BOX %>" scope="page" />
<c:choose>
	<c:when test="${fromPageKbn eq FROM_PAGE_KEEP_BOX}">
		<c:set var="backPath" value="/member/keepBox/searchAgainFromSendScoutMail" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="backPath" value="/member/list/searchAgainFromSendScoutMail" scope="page" />
	</c:otherwise>
</c:choose>
<!-- #main# -->
<div id="main">

<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>

<hr />

<p id="txt_comp">${f:h(defaultMsg) }</p>

<br />

<html:button property="" value="　" style="background-image:url('${SHOP_CONTENS}/images/tokyo/btn_scout_backlist.gif'); width: 135px; height:35px; border:none;" onclick="location.href='${f:url(backPath)}'"></html:button>

</div>
<!-- #main# -->
<hr />

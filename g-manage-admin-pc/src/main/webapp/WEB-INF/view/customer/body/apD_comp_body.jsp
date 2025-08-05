<%@page import="com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm"%>
<%@page pageEncoding="UTF-8"%>
<%
	// WEBIDを取得
	String webId = (String) session.getAttribute(CustomerForm.SESSION_KEY.WEB_ID);
	pageContext.setAttribute("webId", webId);
%>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE or pageKbn eq PAGE_OTHER or pageKbn eq PAGE_INPUT}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />
	<p id="txt_comp">${f:h(defaultMsg)}</p>
	<hr />
	<div class="wrap_btn">
		<c:if test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE}">
			<c:choose>
				<c:when test="${not empty webId && pageKbn eq PAGE_EDIT}">
					<input type="button" value="顧客詳細へ戻る"  onclick="location.href='${f:url(gf:makePathConcat2Arg(customerDetailPath, id, webId))}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:when>
				<c:when test="${not empty webId && pageKbn eq PAGE_DELETE}">
					<input type="button" value="検索結果へ戻る"  onclick="location.href='${f:url(navigationPath2)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:when>
				<c:otherwise>
					<s:form action="${f:h(actionPath)}">
						<html:submit property="backList" value="検索結果へ戻る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</s:form>
				</c:otherwise>
			</c:choose>
		</c:if>
	</div>
	<hr />
</div>
<!-- #main# -->
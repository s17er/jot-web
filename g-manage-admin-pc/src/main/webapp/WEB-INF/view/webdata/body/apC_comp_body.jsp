<%@page pageEncoding="UTF-8"%>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<%// 登録は入力画面へ、それ以外は一覧へ遷移有  %>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_INPUT or pageKbn eq PAGE_EDIT or pageKbn eq PAGE_OTHER}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title date">${f:h(pageTitle1)}</h2>
	<hr />
	<p id="txt_comp">${f:h(defaultMsg)}</p>
	<hr />
	<div class="wrap_btn">
		<c:if test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE or pageKbn eq PAGE_OTHER}">
			<s:form action="${f:h(actionPath)}">
				<html:submit property="backList" value="検索結果へ戻る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</s:form>
		</c:if>
	</div>
</div>
<!-- #main# -->
<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<%// 編集は一覧へ遷移有  %>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DELETE or pageKbn eq PAGE_INPUT}">
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />
	<p id="txt_comp">${f:h(defaultMsg)}</p>
	<hr />

</div>
<!-- #main# -->
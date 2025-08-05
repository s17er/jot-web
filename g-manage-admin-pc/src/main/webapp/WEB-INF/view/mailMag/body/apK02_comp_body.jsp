<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:if test="${not empty navigationPath2}">
			<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		</c:if>
	</ul>

	<hr />
	<html:errors/>

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />
	<p id="txt_comp">${f:h(defaultMsg)}</p>
	<hr />

	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:if test="${not empty navigationPath2}">
			<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		</c:if>
	</ul>

</div>
<!-- #main# -->
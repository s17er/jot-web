<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}" style="width: 300px;">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />
	<p id="txt_comp">${f:br(f:h(defaultMsg))}</p>
	<hr />

</div>
<!-- #main# -->
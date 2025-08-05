<%@page pageEncoding="UTF-8"%>
<meta http-equiv="refresh" content="2;URL=${f:url('/magazineImport/import/waitImport')}" />
<!-- #main# -->
<div id="main">

	<h2 title="${f:h(pageTitle1)}" class="title" id="${f:h(pageTitleId1)}">${f:h(pageTitle1)}</h2>
	<hr />

	<html:errors/>

	<div id="wrap_wait">
		<p>${f:h(defaultMsg)}</p>
	</div>

</div>
<!-- #main# -->

<%@page pageEncoding="UTF-8"%>
<!-- #main# -->
<div id="main">

<h2 title="${f:h(pageTitle) }" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle) }</h2>

<hr />

<p id="txt_comp">${f:h(defaultMsg)}</p>

<input type="button" value="　" onclick="location.href='${f:url(backListPath)}'" class="btn_backList" />

</div>
<!-- #main# -->
<hr />

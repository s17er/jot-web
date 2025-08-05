<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">

	<br />
	<p>${f:br(defaultMsg)}</p>
	<hr />

	<html:errors/>
	<br />
	<input type="button" name="" value="ログインページへ" onclick="location.href='${f:url('/login/logout/')}'" />
	<br /><br /><br /><br />
</div>
<!-- #main# -->
<hr />

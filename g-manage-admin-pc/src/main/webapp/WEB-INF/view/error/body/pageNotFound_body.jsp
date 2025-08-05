<%@page pageEncoding="UTF-8"%>

<div id="main">

	<h2 title="エラー" class="errorTitle" id="title_error">エラー</h2>
	<hr />
	<br />

	<div id="errMes" class="error">
		<ul>
			<li>ページが見つかりませんでした。</li>
		</ul>
	</div>
	<br />
	<div class="wrap_btn">
		<input type="button" value="トップメニューへ" onclick="location.href='${f:url('/top/menu/')}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" />
	</div>
	<br />

</div>
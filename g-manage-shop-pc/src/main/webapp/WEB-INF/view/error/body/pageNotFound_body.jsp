<%@page pageEncoding="UTF-8"%>

<div id="main">

	<h2 title="エラー" class="title" id="error">エラー</h2>
	<br />

	<div id="errMes" class="error">
		<ul>
			<li>ページが見つかりませんでした。</li>
		</ul>
	</div>
	<br />
	<div class="wrap_btn">
		<c:choose>
			<c:when test="${userDto eq null or userDto.userId eq null}">
				<input type="button" name="" value="ログインページへ" onclick="location.href='${f:url('/login/logout/')}'" id="btn_conf" />
			</c:when>
			<c:otherwise>
				<input type="button" value="トップメニューへ" onclick="location.href='${f:url('/top/menu/')}'" id="btn_conf" />
			</c:otherwise>
		</c:choose>
	</div>
	<br />

</div>
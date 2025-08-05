<%@page pageEncoding="UTF-8"%>

<div id="main">

	<h2 title="エラー" class="errorTitle" id="title_error" >エラー</h2>
	<hr />
	<br />
	<div id="errMes" class="error">
		<html:errors />
	</div>

	<html:messages id="msg" message="true">
		<bean:write name="msg" ignore="true" />
	</html:messages>
	<br />
	<div class="wrap_btn">
		<c:choose>
			<c:when test="${pageFlg eq 'sessionTimeout'}">
				<input type="button" name="" value="ログインページへ" onclick="location.href='${f:url('/login/logout/')}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</c:when>
			<c:otherwise>
				<input type="button" name="" value="トップメニューへ" onclick="location.href='${f:url('/top/menu/')}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</c:otherwise>
		</c:choose>
	</div>
	<br />
</div>
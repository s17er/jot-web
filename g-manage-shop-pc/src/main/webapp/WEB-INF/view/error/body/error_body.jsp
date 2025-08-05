<%@page pageEncoding="UTF-8"%>

<div id="main">

	<h2 title="エラー" class="title" id="error" >エラー</h2>
	<br />

	<html:errors />

	<html:messages id="msg" message="true">
	<div class="error">
		<ul>
			<li>
				<bean:write name="msg" ignore="true" />
  			</li>
		</ul>
	</div>
	</html:messages>
	<br />
	<div class="wrap_btn">
		<c:choose>
			<c:when test="${pageFlg eq 'sessionTimeout'}">
				<input type="button" name="" value="ログインページへ" onclick="location.href='${f:url('/login/logout/')}'" id="btn_conf"/>
			</c:when>
			<c:otherwise>
				<input type="button" name="" value="トップメニューへ" onclick="location.href='${f:url('/top/menu/')}'" id="btn_conf"/>
			</c:otherwise>
		</c:choose>
	</div>
	<br />
</div>
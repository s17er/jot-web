<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<!-- #main# -->
<div id="main">

<div class="loginError">
	<html:errors/>
</div>
	<s:form action="${f:h(actionPath)}">
		<!-- #wrap_login# -->
		<div id="wrap_login">
			<p>${f:h(defaultMsg)}</p>
			<hr />

			<dl class="clear">
				<dt>ログインID&nbsp;<span class="attention">※必須</span></dt>
				<dd><html:text property="loginId" size="50"  styleClass="disabled" styleId="firstFocus"/></dd>
				<dt>パスワード&nbsp;<span class="attention">※必須</span></dt>
				<dd><html:password property="password" size="50"  styleClass="disabled" redisplay="false"/></dd>
			</dl>
		</div>
		<!-- #wrap_login# -->
		<hr />

		<div class="wrap_btn">
			<html:submit styleClass="button" value="　" styleId="btn_login" />
		</div>
	</s:form>
	<hr />
</div>
<!-- #main# -->

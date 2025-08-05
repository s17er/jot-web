<%@page pageEncoding="UTF-8"%>

<link href="${f:h(SHOP_CONTENS)}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${f:h(SHOP_CONTENS)}/css/login.css" rel="stylesheet" type="text/css" />

	<div id="all">
		<!-- #main# -->
		<div id="main">
			<!-- #wrap_login# -->
			<div id="wrap_login">
				<div class="login_titlelogo">
            		<img src="${f:h(SHOP_CONTENS)}/images/logo.png" class="login_logo" alt="グルメキャリー 顧客管理画面">
					<p>顧客管理 ログイン画面</p>
				</div>
				<hr />
				<s:form action="${f:h(actionPath)}">
					<p>${f:h(defaultMsg)}</p>
					<html:text property="customerLoginId" placeholder="顧客ログインID"/>
					<html:text property="loginId" placeholder="営業ログインID"/>
					<html:password property="password" placeholder="営業パスワード"/>
					<div class="loginError">
						<html:errors />
					</div>
					<div class="wrap_btn">
					<html:submit value="ログイン"  styleId="btn_login"  />
					</div>
				</s:form>
				<footer>
					<p><a href="http://www.joffice-tokyo.co.jp/" target="_blank">Copyright © All rights reserved.</a>
					</p>
				</footer>
			</div>
			<!-- #wrap_login# -->
		</div>
		<!-- #main# -->
	</div>

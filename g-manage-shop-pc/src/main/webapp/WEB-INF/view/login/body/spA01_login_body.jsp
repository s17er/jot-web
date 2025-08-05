<%@page import="com.gourmetcaree.shop.pc.sys.util.ShopPcUtil"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="includeValue" value="<%=ShopPcUtil.readIndexIncludeFile() %>" scope="page" />

<link href="${f:h(SHOP_CONTENS)}/css/login.css" rel="stylesheet" type="text/css" />
<link href="${f:h(SHOP_CONTENS)}/css/style.css" rel="stylesheet" type="text/css" />
<link rel="manifest" href="${f:h(SHOP_CONTENS)}/manifest.json">
<script>
  window.addEventListener('load', function() {
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker.register("${SHOP_CONTENS}/sw.js")
        .then(function(registration) {
          console.log("serviceWorker registed.");
        }).catch(function(error) {
          console.warn("serviceWorker error.", error);
        });
    }
  });
  </script>

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
					<html:text property="loginId" placeholder="ログインID"/>
					<html:password property="password" placeholder="パスワード"/>
					<div class="loginError">
						<html:errors />
					</div>
					<div class="wrap_btn">
					<html:submit value="ログイン"  styleId="btn_login"  />
						${includeValue}
					</div>
					<a href="${f:url('/passwordReissue/input/')}" class="passwordReissue">※パスワードを忘れた方はこちら</a>
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

<%@page import="com.gourmetcaree.shop.pc.sys.util.ShopPcUtil"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="includeValue" value="<%=ShopPcUtil.readIndexIncludeFile() %>" scope="page" />

<link href="${f:h(SHOP_CONTENS)}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${f:h(SHOP_CONTENS)}/css/login.css" rel="stylesheet" type="text/css" />

	<div id="all">
		<!-- #main# -->
		<div id="main">
			<!-- #wrap_login# -->
			<div id="wrap_login">
				<div class="login_titlelogo">
            		<img src="${f:h(SHOP_CONTENS)}/images/logo.png" class="login_logo" alt="グルメキャリー 顧客管理画面">
					<p>顧客管理　パスワード再登録画面</p>
				</div>
				<hr />

				<c:if test="${existDataFlg}">
				<s:form action="${f:h(actionPath)}" styleClass="sslForm">
					<p>${defaultMsg}</p>
					<html:text property="loginId" placeholder="ログインID"/>
					<div class="loginError">
						<html:errors />
					</div>
					<div class="wrap_btn">
						<html:submit value="次へ"  styleId="btn_login" property="conf"/>
						${includeValue}
					</div>
				</s:form>
				</c:if>

				<footer>
					<p><a href="http://www.joffice-tokyo.co.jp/" target="_blank">Copyright © All rights reserved.</a>
					</p>
				</footer>
			</div>
			<!-- #wrap_login# -->
		</div>
		<!-- #main# -->
	</div>

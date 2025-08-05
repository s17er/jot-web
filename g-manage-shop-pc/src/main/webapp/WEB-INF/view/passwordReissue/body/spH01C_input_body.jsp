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

				<s:form action="${f:h(actionPath)}" focus="mail" styleClass="sslForm">
					<p>${defaultMsg}</p>
					<html:text property="loginId" placeholder="ログインID"/>
					<div class="loginError">
						<html:errors />
					</div>
					<div class="wrap_btn">
		                <%-- IEでエンターキーによるsubmitを有効にするためのダミーのテキストエリア --%>
                		<input type="text" name="dummy" style="display:none;">
						<html:submit value="送信"  styleId="btn_login" property="submit"/>
						${includeValue}
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
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

				<form>
					<p>${defaultMsg}</p>
					<div class="wrap_btn">
						<c:choose>
							<c:when test="${pageKbn eq PAGE_EDIT}">
								<input type="button" value="ログイン画面へ" onclick="location.href='${f:url('/login/login/')}'" id=btn_login />
							</c:when>
							<c:otherwise>
								<input type="button" value="管理画面トップへ" onclick="location.href='${f:url('/')}'" id="btn_login" />
							</c:otherwise>
						</c:choose>
					</div>
				</form>
				<footer>
					<p><a href="http://www.joffice-tokyo.co.jp/" target="_blank">Copyright © All rights reserved.</a>
					</p>
				</footer>
			</div>
			<!-- #wrap_login# -->
		</div>
		<!-- #main# -->
	</div>
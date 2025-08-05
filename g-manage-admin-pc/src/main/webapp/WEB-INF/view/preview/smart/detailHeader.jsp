<%@page pageEncoding="UTF-8"%>
		<% /* タイルズ：ヘッダーメニュー部 */ %>
		<header id="top" class="clearfix">
			<h1>
				<c:choose>
					<c:when test="${not empty h1Text}">
						${h1Text}
					</c:when>
					<c:otherwise>
						原稿名&nbsp;｜ <a href="#">飲食求人・レストラン求人・飲食店転職・就職　グルメキャリー首都圏版</a>
					</c:otherwise>
				</c:choose>
			</h1>
			<div id="logo">
				<a href="/ipx/index.html"><img src="${f:h(frontHttpUrl)}/ipx${f:h(imgLocation)}/logo.png" width="200" height="44" alt="飲食店転職・飲食求人グルメキャリー首都圏版ロゴ"></a>
				<p>
					<span class="resetDate">xxxx/xx/xx 更新</span>&nbsp;<span class="resetText">毎週木曜更新</span>
				</p>
			</div>

			<jsp:include page="frontLoginHeader.jsp" ></jsp:include>

		</header>
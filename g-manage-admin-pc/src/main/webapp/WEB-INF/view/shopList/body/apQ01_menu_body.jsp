<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm.RefelerKbn"%>
<%@page import="com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm"%>
<%
	RefelerKbn refelerKbn = (RefelerKbn) session.getAttribute(ShopListBaseForm.SESSION_KEY.REFERER);
	pageContext.setAttribute("refelerKbn", refelerKbn);

	if (RefelerKbn.WEB_DETAIL == refelerKbn || RefelerKbn.CUSTOMER_WEB_DETAIL == refelerKbn) {
		String webId = (String) session.getAttribute(ShopListBaseForm.SESSION_KEY.WEB_ID);
		pageContext.setAttribute("refelerWebId", webId);
	}
%>

<c:set var="REFELER_KBN_WEB_DETAIL" value="<%=RefelerKbn.WEB_DETAIL %>" scope="page" />
<c:set var="REFELER_KBN_WEB_LSIT" value="<%=RefelerKbn.WEB_LIST %>" scope="page" />
<c:set var="REFELER_KBN_CUSTOMER" value="<%=RefelerKbn.CUSTOMER %>" scope="page" />
<c:set var="REFELER_KBN_CUSTOMER_WEB_DETAIL" value="<%=RefelerKbn.CUSTOMER_WEB_DETAIL %>" scope="page" />
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${REFELER_KBN_CUSTOMER eq refelerKbn}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
			<c:when test="${REFELER_KBN_WEB_LSIT eq refelerKbn or REFELER_KBN_WEB_DETAIL eq refelerKbn}">
				<li><a href="${f:url(navigationWebPath1)}">${f:h(navigationWebText1)}</a> </li>

				<c:if test="${REFELER_KBN_WEB_DETAIL eq refelerKbn}">
					<li><a href="${f:url(gf:makePathConcat1Arg(navigationWebPath2,refelerWebId))}">${f:h(navigationWebText2)}</a> </li>
				</c:if>
			</c:when>

			<%-- WEBデータ詳細を経由して顧客詳細から遷移した場合 --%>
			<c:when test="${REFELER_KBN_CUSTOMER_WEB_DETAIL eq refelerKbn}">
				<li><a href="${f:url(gf:makePathConcat2Arg(navigationPath4, customerId, refelerWebId))}">${f:h(navigationText3)}</a> </li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />
	<div class="top_menu" style="padding-bottom: 20px">
		<ul class="menu_list01 clear">
			<li>
				<h2>店舗データ管理</h2>
				<p>店舗データの登録、検索・編集・CSV操作が出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url(gf:makePathConcat1Arg('/shopList/input/index', customerId))}" title="店舗データ登録">店舗データ登録</a></li>
					<li><a href="${f:url(gf:makePathConcat1Arg('/shopList/list/index', customerId))}" title="店舗データ管理">店舗データ管理</a></li>
				</ul>
			</li>
			<li>
				<h2>ラベル管理</h2>
				<p>店舗データのラベルを行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url(gf:makePathConcat1Arg('/shopLabel/input/index', customerId))}" title="ラベルデータ登録">ラベルデータ登録</a></li>
					<li><a href="${f:url(gf:makePathConcat1Arg('/shopLabel/list', customerId))}" title="ラベルデータ管理">ラベルデータ管理</a></li>
				</ul>
			</li>
			<li>
				<h2>画像管理</h2>
				<p>顧客単位で画像の確認、登録、削除が出来ます。</p>
				<ul class="btn">
					<li><a href="${f:url(gf:makePathConcat1Arg('/customerImage/list/index', customerId))}" title="画像管理">画像管理</a></li>
				</ul>
			</li>
		</ul>
	</div>
	<hr />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${REFELER_KBN_CUSTOMER eq refelerKbn}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
				<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
			</c:when>
			<c:when test="${REFELER_KBN_WEB_LSIT eq refelerKbn or REFELER_KBN_WEB_DETAIL eq refelerKbn}">
				<li><a href="${f:url(navigationWebPath1)}">${f:h(navigationWebText1)}</a> </li>

				<c:if test="${REFELER_KBN_WEB_DETAIL eq refelerKbn}">
					<li><a href="${f:url(gf:makePathConcat1Arg(navigationWebPath2,refelerWebId))}">${f:h(navigationWebText2)}</a> </li>
				</c:if>
			</c:when>

			<%-- WEBデータ詳細を経由して顧客詳細から遷移した場合 --%>
			<c:when test="${REFELER_KBN_CUSTOMER_WEB_DETAIL eq refelerKbn}">
				<li><a href="${f:url(gf:makePathConcat2Arg(navigationPath4, customerId, refelerWebId))}">${f:h(navigationText3)}</a> </li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

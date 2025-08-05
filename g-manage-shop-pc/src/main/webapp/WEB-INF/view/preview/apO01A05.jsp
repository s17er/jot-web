<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/frontLayout.jsp" flush="false">
	<tiles:put name="title" value="${f:h(shopName)}の求人・転職情報 | グルメキャリー"/>
	<tiles:put name="head" value="/WEB-INF/view/common/detailHead.jsp" />
	<tiles:put name="content" value="/WEB-INF/view/preview/body/apO01A05_body.jsp" />
	<tiles:put name="h1Text" value="${f:h(shopName)}の求人・転職情報｜系列店舗から求人企業を探す"/>

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="actionPath" value="/preview/detail/" />
	<tiles:put name="jspTabKbn" value="shopList" />
	<tiles:put name="webDataDisplayFlg" value="${true}" />
</tiles:insert>
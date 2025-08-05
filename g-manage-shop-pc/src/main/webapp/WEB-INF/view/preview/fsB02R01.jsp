<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page"></c:set>
<tiles:insert template="/WEB-INF/view/common/frontSmartDetailLayout.jsp" flush="false">
	<tiles:put name="title" value="${f:h(shopName)}"/>
	<tiles:put name="h1Text" value="${f:h(shopName)}の求人・転職情報｜系列店舗から求人企業を探す"/>

	<tiles:put name="head" value="/WEB-INF/view/preview/smart/detailHead.jsp" />
	<tiles:put name="header" value="/WEB-INF/view/preview/smart/detailHeader.jsp" />
	<tiles:put name="content" value="/WEB-INF/view/preview/body/fsB02R01_body.jsp" />
	<tiles:put name="footer" value="/WEB-INF/view/preview/smart/detailFooter.jsp" />
	<tiles:put name="BACK_PATH" value="${backPath}"></tiles:put>


	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="actionPath" value="/webdata/list/" />
</tiles:insert>

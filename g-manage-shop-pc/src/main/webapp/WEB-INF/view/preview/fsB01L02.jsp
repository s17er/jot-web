<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page"></c:set>
<tiles:insert template="/WEB-INF/view/common/frontSmartDetailLayout.jsp" flush="false">
	<tiles:put name="title" value="${f:h(manuscriptName)} ${f:h(LABEL_SHOPLIST)}"/>

	<tiles:put name="head" value="/WEB-INF/view/preview/smart/listHead.jsp" />
	<tiles:put name="header" value="/WEB-INF/view/preview/smart/detailHeader.jsp" />
	<tiles:put name="content" value="/WEB-INF/view/preview/body/fsB01L02_body.jsp" />
	<tiles:put name="footer" value="/WEB-INF/view/preview/smart/detailFooter.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="actionPath" value="/webdata/list/" />
</tiles:insert>
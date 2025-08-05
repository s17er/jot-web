<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | ${f:h(LABEL_SHOPLIST)}確認画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/apQ_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DELETE}" />
	<tiles:put name="actionPath" value="/shopList/list/" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="${f:h(LABEL_SHOPLIST)}トップへ" />
	<tiles:put name="navigationPath2" value="/shopList/detail/backToIndex" />
	<tiles:put name="defaultMsg" value="削除が完了しました。" />
	<tiles:put name="pageTitle1" value="店舗データ" />
	<tiles:put name="pageTitleId1" value="title_shopListEdit"/>
</tiles:insert>
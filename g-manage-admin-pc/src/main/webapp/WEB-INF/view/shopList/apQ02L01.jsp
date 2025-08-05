<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | ${f:h(LABEL_SHOPLIST)}検索画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/apQ02_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/shopList/list/" />
	<tiles:put name="actionMaxRowPath" value="/shopList/list/changeMaxRow" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="${f:h(LABEL_SHOPLIST)}トップへ" />
	<tiles:put name="navigationPath2" value="/shopList/list/backToIndex" />
	<tiles:put name="pageTitle1" value="店舗データ" />
	<tiles:put name="pageTitleId1" value="title_shopListEdit"/>
	<tiles:put name="defaultMsg" value="" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayShopListImage" />
	<tiles:put name="upImgAjaxPath" value="/shopList/list/ajaxInsertShopImage" />
	<tiles:put name="deleteImgAjaxPath" value="/shopList/list/ajaxDeleteShopImage" />
</tiles:insert>
<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | ${f:h(LABEL_SHOPLIST)}"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/apQ01_menu_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_MENU}" />
	<tiles:put name="actionPath" value="" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="顧客データ一覧へ" />
	<tiles:put name="navigationPath2" value="/customer/list/searchAgain" />
	<tiles:put name="navigationText3" value="顧客データ詳細へ" />
	<tiles:put name="navigationPath3" value="/customer/detail/index/${f:h(customerId)}" />
	<tiles:put name="navigationPath4" value="/customer/detail/indexFromWebdata" />

	<tiles:put name="navigationWebText1" value="WEBデータ一覧へ" />
	<tiles:put name="navigationWebPath1" value="/webdata/list/searchAgain/" />
	<tiles:put name="navigationWebText2" value="WEBデータ詳細へ" />
	<tiles:put name="navigationWebPath2" value="/webdata/detail/index" />
	<tiles:put name="navigationWebText2" value="WEBデータ詳細へ" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
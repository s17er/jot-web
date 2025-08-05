<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | ラベル登録確認"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/apQ06_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/shopLabel/input/" />
	<tiles:put name="pageTitle1" value="ラベル登録確認" />
	<tiles:put name="pageTitleId1" value="title_shopListLabel" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="${f:h(LABEL_SHOPLIST)}トップへ" />
	<tiles:put name="navigationPath2" value="/shopLabel/input/backToIndex" />
	<tiles:put name="defaultMsg" value="必要事項を確認の上、「確認」ボタンを押して下さい。" />

	<tiles:put name="targetForm" value="inputForm" />

	<tiles:put name="headJsp" value="/WEB-INF/view/webdata/head/input.jsp" />
</tiles:insert>
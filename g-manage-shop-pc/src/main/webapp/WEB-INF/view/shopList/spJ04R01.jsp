<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="店舗一覧 ｰ 店舗編集画面 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DETAIL}" />
	<tiles:put name="actionPath" value="/shopList/detail/" />
	<tiles:put name="pageTitle" value="店舗データ詳細" />
	<tiles:put name="pageTitleId" value="shopListDetail"/>
	<tiles:put name="defaultMsg" value="" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayShopListImage" />
	<tiles:put name="copyActionPath" value="/shopList/input/copy"/>
</tiles:insert>
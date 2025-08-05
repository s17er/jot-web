<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="店舗一覧 ｰ 店舗編集画面 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/shopList/edit/" />
	<tiles:put name="pageTitle" value="店舗データ編集" />
	<tiles:put name="pageTitleId" value="shopListInput"/>
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「登録」ボタンを押して下さい。" />
	<tiles:put name="geoFailPath" value="/shopList/edit/failGeoCoding" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayShopListImage" />
</tiles:insert>
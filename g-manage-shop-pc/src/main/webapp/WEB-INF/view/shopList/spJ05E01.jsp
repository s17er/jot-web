<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="店舗一覧 ｰ 店舗編集画面 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ02_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/shopList/edit/" />
	<tiles:put name="pageTitle" value="店舗データ編集" />
	<tiles:put name="pageTitleId" value="shopListInput"/>
	<tiles:put name="defaultMsg" value="各項目を入力の上、「確認」ボタンを押して確認画面へ進んで下さい。" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayShopListImage" />
	<tiles:put name="upImgAjaxPath" value="/shopList/edit/upAjaxMaterial" />
	<tiles:put name="delImgAjaxPath" value="/shopList/edit/delAjaxMaterial" />

</tiles:insert>
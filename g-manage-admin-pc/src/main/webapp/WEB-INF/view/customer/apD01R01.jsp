<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 顧客データ詳細"/>
	<tiles:put name="content" value="/WEB-INF/view/customer/body/apD01_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DETAIL}" />
	<tiles:put name="actionPath" value="/customer/detail/" />
	<tiles:put name="pageTitle1" value="顧客データ詳細" />
	<tiles:put name="pageTitleId1" value="title_customerDetail" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="顧客データ一覧へ" />
	<tiles:put name="navigationPath2" value="/customer/list/searchAgain" />
	<tiles:put name="navigationText3" value="店舗データ登録・編集" />
	<tiles:put name="navigationPath3" value="/shopList/index" />
	<tiles:put name="navigationWebdataText" value="WEBデータ詳細へ" />
	<tiles:put name="navigationWebdataPath" value="/webdata/detail/index" />
	<tiles:put name="navigationListIndexPath" value="/customer/list/index" />
	<tiles:put name="navigationPathShopListWebDatailPath" value="/shopList/indexCustomerAndWebDatail" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
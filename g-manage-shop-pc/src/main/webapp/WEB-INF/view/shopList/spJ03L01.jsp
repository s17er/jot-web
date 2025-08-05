<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="店舗管理 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ03_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/shopList/list/" />
	<tiles:put name="actionMaxRowPath" value="/shopList/list/changeMaxRow" />
	<tiles:put name="pageTitle" value="店舗データ" />
	<tiles:put name="pageTitleId" value="shopListTop"/>
	<tiles:put name="defaultMsg" value="条件を選んで「検索」ボタンを押して下さい。
CSV（データ入力済み）よりデータを登録する場合は、「CSVインポート」ボタンを押して下さい。
検索結果のデータをCSV形式で保存する場合は「CSVエクスポート」ボタンを押して下さい。
新しくCSVよりデータを登録する場合は「CSVエクスポート」ボタンを押して空のCSVデータより入力を行ってください。" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayShopListImage" />
	<tiles:put name="upImgAjaxPath" value="/shopList/list/ajaxInsertShopImage" />
	<tiles:put name="deleteImgAjaxPath" value="/shopList/list/ajaxDeleteShopImage" />
</tiles:insert>
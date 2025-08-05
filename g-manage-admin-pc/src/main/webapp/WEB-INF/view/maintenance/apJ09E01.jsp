<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | タグデータの編集"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ09_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/tagManage/edit/update" />
	<tiles:put name="pageTitle1" value="WEBデータタグ編集" />
	<tiles:put name="pageTitleId1" value="title_webTagEdit" />
	<tiles:put name="pageTitle2" value="店舗一覧タグ編集" />
	<tiles:put name="pageTitleId2" value="title_shopListTagEdit" />


	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu" />
	<tiles:put name="navigationText2" value="マスタメンテナンスへ" />
	<tiles:put name="navigationPath2" value="/maintenance/menu" />
	<tiles:put name="defaultMsg" value="更新内容を変更後画面下部の更新ボタンを押下すると、更新内容が一括で更新されます。" />

	<tiles:put name="deletePath" value="/tagManage/edit/delete"></tiles:put>


<%-- 	<tiles:put name="headJsp" value="/WEB-INF/view/webdata/head/input.jsp" /> --%>
</tiles:insert>
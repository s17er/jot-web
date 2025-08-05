<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 顧客データ編集"/>
	<tiles:put name="content" value="/WEB-INF/view/customer/body/apD01_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/customer/edit/" />
	<tiles:put name="assignedCompanyAjaxPath" value="/ajax/index/limitAssignedCompanyPull/" />
	<tiles:put name="assignedSalesAjaxPath" value="/ajax/index/limitAssignedSalesPull/" />
	<tiles:put name="pageTitle1" value="顧客データ編集" />
	<tiles:put name="pageTitleId1" value="title_customerEdit" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="顧客データ一覧へ" />
	<tiles:put name="navigationPath2" value="/customer/list/" />
	<tiles:put name="customerDetailPath" value="/customer/detail/indexFromWebdata" />
	<tiles:put name="defaultMsg" value="必要事項を入力の上、「確認」ボタンを押して下さい。" />
</tiles:insert>
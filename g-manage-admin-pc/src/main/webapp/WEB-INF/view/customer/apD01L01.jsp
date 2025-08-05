<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 顧客データ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/customer/body/apD01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/customer/list/" />
	<tiles:put name="actionMaxRowPath" value="/customer/list/changeMaxRow" />
	<tiles:put name="assignedCompanyAjaxPath" value="/ajax/index/limitAssignedCompanyPull/" />
	<tiles:put name="assignedSalesAjaxPath" value="/ajax/index/limitAssignedSalesPull/" />
	<tiles:put name="pageTitle1" value="顧客データ一覧" />
	<tiles:put name="pageTitleId1" value="title_customerList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
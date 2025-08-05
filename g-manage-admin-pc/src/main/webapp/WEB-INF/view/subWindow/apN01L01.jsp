<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/subWindowLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 顧客データ検索"/>
	<tiles:put name="content" value="/WEB-INF/view/subWindow/body/apN01_select_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_OTHER}" />
	<tiles:put name="actionPath" value="/customerSearch/select/" />
	<tiles:put name="actionMaxRowPath" value="/customerSearch/select/changeMaxRow" />
	<tiles:put name="assignedCompanyAjaxPath" value="/ajax/index/limitAssignedCompanyPull/" />
	<tiles:put name="assignedSalesAjaxPath" value="/ajax/index/limitAssignedSalesPull/" />
	<tiles:put name="pageTitle" value="顧客データ検索" />
	<tiles:put name="pageTitleId" value="title_customerSearch" />
</tiles:insert>
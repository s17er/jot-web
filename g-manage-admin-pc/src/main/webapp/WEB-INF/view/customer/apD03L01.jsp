<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 顧客画像管理"/>
	<tiles:put name="content" value="/WEB-INF/view/customer/body/apD03L01_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/customerMailMag/list/" />
	<tiles:put name="pageTitle1" value="顧客画像管理" />
	<tiles:put name="pageTitleId1" value="" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="系列店舗情報トップへ" />
	<tiles:put name="navigationPath2" value="/shopList/index/${f:h(customerId)}" />
</tiles:insert>
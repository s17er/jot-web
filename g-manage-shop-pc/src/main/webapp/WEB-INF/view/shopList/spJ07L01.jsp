<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="画像管理 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ07L01_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_OTHER}" />
	<tiles:put name="actionPath" value="/shopList/inputCsv/" />
	<tiles:put name="geoFailPath" value="/shopList/inputCsv/failGeoCodeingDetail" />
	<tiles:put name="pageTitle" value="店舗一覧｜画像管理" />
	<tiles:put name="pageTitleId" value="shopListInput"/>
</tiles:insert>
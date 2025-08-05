<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="店舗一覧 ｰ 店舗CSV一覧画面 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ06_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_OTHER}" />
	<tiles:put name="actionPath" value="/shopList/inputCsv/" />
	<tiles:put name="actionMaxRowPath" value="/shopList/inputCsv/changeMaxRow" />
	<tiles:put name="pageTitle" value="店舗データ" />
	<tiles:put name="pageTitleId" value="shopListTop"/>
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
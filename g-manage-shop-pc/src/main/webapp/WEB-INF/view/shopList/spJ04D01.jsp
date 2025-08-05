<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 店舗データ削除完了画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DELETE}" />
	<tiles:put name="actionPath" value="/shopList/list/" />
	<tiles:put name="defaultMsg" value="削除が完了しました。" />
	<tiles:put name="pageTitle" value="店舗一覧｜管理" />
	<tiles:put name="pageTitleId" value="title_shopListEdit"/>
</tiles:insert>
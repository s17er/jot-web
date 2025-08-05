<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="店舗一覧 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ01_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="edit" />
	<tiles:put name="actionPath" value="" />
	<tiles:put name="pageTitle" value="店舗一覧" />
	<tiles:put name="pageTitleId" value="shopListTop" />
	<tiles:put name="defaultMsg" value=""/>
</tiles:insert>
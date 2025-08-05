<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="店舗一覧 ｰ 店舗CSV登録完了画面 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shopList/body/spJ_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/shopList/inputCsv/" />
	<tiles:put name="pageTitle" value="店舗一覧｜登録" />
	<tiles:put name="pageTitleId" value="shopListInput"/>
	<tiles:put name="defaultMsg" value="店舗の登録が完了しました。" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="企業情報の編集 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shop/body/spG01_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="edit" />
	<tiles:put name="actionPath" value="/shop/edit" />
	<tiles:put name="pageTitle" value="企業情報･設定" />
	<tiles:put name="pageTitleId" value="passEditComp" />
	<tiles:put name="defaultMsg" value="企業情報の登録が完了しました。"/>
</tiles:insert>
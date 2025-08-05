<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="企業情報の編集 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shop/body/spG01_detail_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${f:h(PAGE_EDIT)}" />
	<tiles:put name="actionPath" value="/shop/edit" />
	<tiles:put name="pageTitle" value="企業情報の編集" />
	<tiles:put name="pageTitleId" value="passEdit" />
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「登録」へお進みください。"/>
</tiles:insert>
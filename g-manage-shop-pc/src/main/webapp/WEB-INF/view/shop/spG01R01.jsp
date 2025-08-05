<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="企業情報･設定 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/shop/body/spG01_detail_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${f:h(PAGE_DETAIL)}" />
	<tiles:put name="actionPath" value="/shop/edit" />
	<tiles:put name="pageTitle" value="企業情報･設定" />
	<tiles:put name="pageTitleId" value="cominfoDetail" />
	<tiles:put name="defaultMsg" value= "「メールアドレスの追加」「パスワードの再設定」「メールマガジンの受信設定」などの<br />設定を行う場合は「編集」へお進みください。"/>
</tiles:insert>
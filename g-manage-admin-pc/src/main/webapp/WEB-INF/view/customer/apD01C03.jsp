<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 顧客データ登録完了"/>
	<tiles:put name="content" value="/WEB-INF/view/customer/body/apD_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/customer/input/" />
	<tiles:put name="pageTitle1" value="顧客データ登録完了" />
	<tiles:put name="pageTitleId1" value="title_customerInputComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="登録画面へ" />
	<tiles:put name="navigationPath2" value="/customer/input/index" />
	<tiles:put name="defaultMsg" value="登録しました。" />
</tiles:insert>
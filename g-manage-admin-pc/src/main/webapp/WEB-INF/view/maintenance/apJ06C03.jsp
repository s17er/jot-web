<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 事前登録の開催年登録完了"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/advancedRegistration/input/" />
	<tiles:put name="pageTitle1" value="事前登録の開催年登録完了" />
	<tiles:put name="pageTitleId1" value="title_fairDataInputComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メンテナンスメニューへ" />
	<tiles:put name="navigationPath2" value="/maintenance/menu/" />
	<tiles:put name="navigationText3" value="登録画面へ" />
	<tiles:put name="navigationPath3" value="/advancedRegistration/input/" />
	<tiles:put name="defaultMsg" value="登録が完了しました。" />
</tiles:insert>
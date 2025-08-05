<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 事前登録の開催年削除確認"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ06_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DELETE}" />
	<tiles:put name="actionPath" value="/advancedRegistration/delete/" />
	<tiles:put name="actionBackPath" value="/advancedRegistration/delete/correct" />
	<tiles:put name="pageTitle1" value="事前登録の開催年削除確認" />
	<tiles:put name="pageTitleId1" value="title_fairDataDelConf" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メンテナンスメニューへ" />
	<tiles:put name="navigationPath2" value="/maintenance/menu/" />
	<tiles:put name="navigationText3" value="事前登録の開催年一覧" />
	<tiles:put name="navigationPath3" value="/advancedRegistration/delete/correct" />
	<tiles:put name="defaultMsg" value="必要事項を入力の上、「削除」ボタンを押して下さい。" />
</tiles:insert>
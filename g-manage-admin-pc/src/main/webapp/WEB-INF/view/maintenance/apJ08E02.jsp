<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ編集確認"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ08_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/terminal/edit/" />
	<tiles:put name="pageTitle1" value="駅グループ" />
	<tiles:put name="pageTitleId1" value="title_terminalData" />
	<tiles:put name="pageTitle2" value="駅グループ編集確認" />
	<tiles:put name="pageTitleId2" value="title_terminalEditConf" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/manu/" />
	<tiles:put name="navigationText2" value="駅グループ一覧へ" />
	<tiles:put name="navigationPath2" value="/terminal/list/" />
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「登録」ボタンを押して下さい。" />
</tiles:insert>
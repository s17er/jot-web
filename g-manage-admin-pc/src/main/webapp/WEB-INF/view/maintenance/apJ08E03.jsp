<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 駅グループ編集完了"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/webdata/edit/" />
	<tiles:put name="pageTitle1" value="駅グループ編集完了" />
	<tiles:put name="pageTitleId1" value="title_terminalEditComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="駅グループ一覧へ" />
	<tiles:put name="navigationPath2" value="/terminal/list/" />
	<tiles:put name="navigationText3" value="メンテナンスへ" />
	<tiles:put name="navigationPath3" value="/maintenance/menu/" />
	<tiles:put name="defaultMsg" value="登録しました。" />
</tiles:insert>
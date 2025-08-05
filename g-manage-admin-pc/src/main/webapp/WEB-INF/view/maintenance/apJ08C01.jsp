<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 駅グループ登録"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ08_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/terminal/input/" />
	<tiles:put name="pageTitle1" value="駅グループ登録" />
	<tiles:put name="pageTitleId1" value="title_terminalInput" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メンテナンスメニューへ" />
	<tiles:put name="navigationPath2" value="/maintenance/menu/" />
	<tiles:put name="defaultMsg" value="必要事項を入力の上、「確認」ボタンを押して下さい。" />

	<tiles:put name="railroadAjaxPath" value="/ajax/index/limitRailroadPull/" />
	<tiles:put name="routeAjaxPath" value="/ajax/index/limitRoutePull/" />
	<tiles:put name="stationAjaxPath" value="/ajax/index/limitStationPull/" />

</tiles:insert>
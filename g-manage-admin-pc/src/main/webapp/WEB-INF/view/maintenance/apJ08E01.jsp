<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ編集"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ08_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/terminal/edit/" />
	<tiles:put name="pageTitle1" value="駅グループデータ選択" />
	<tiles:put name="pageTitleId1" value="title_terminalDataSelect" />
	<tiles:put name="pageTitle2" value="駅グループ編集" />
	<tiles:put name="pageTitleId2" value="title_terminalEdit" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu" />
	<tiles:put name="navigationText2" value="駅グループ一覧へ" />
	<tiles:put name="navigationPath2" value="/terminal/list/" />
	<tiles:put name="defaultMsg" value="必要事項を入力の上、「確認」ボタンを押して下さい。" />

	<% // 以下Ajax部品のパス %>
	<tiles:put name="railroadAjaxPath" value="/ajax/index/limitRailroadPull/" />
	<tiles:put name="routeAjaxPath" value="/ajax/index/limitRoutePull/" />
	<tiles:put name="stationAjaxPath" value="/ajax/index/limitStationPull/" />

<%-- 	<tiles:put name="headJsp" value="/WEB-INF/view/webdata/head/input.jsp" /> --%>
</tiles:insert>
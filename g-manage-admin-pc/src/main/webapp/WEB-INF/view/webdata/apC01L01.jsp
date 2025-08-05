<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/webdata/list/" />
	<tiles:put name="detailActionPath" value="/webdata/detail/" />
	<tiles:put name="changePagePath" value="/webdata/list/changePage/" />
	<tiles:put name="changeCountPagePath" value="/webdata/list/changeCountPage/" />
	<tiles:put name="actionMaxRowPath" value="/webdata/list/changeMaxRow/" />
	<tiles:put name="pageTitle1" value="WEBデータ一覧" />
	<tiles:put name="pageTitleId1" value="title_webdataList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />

	<% // 以下Ajax部品のパス %>
	<tiles:put name="volumeAjaxPath" value="/ajax/index/limitVolumePull/" />
	<tiles:put name="specialAjaxPath" value="/ajax/index/listSpecialSelect/" />
	<tiles:put name="salesAjaxPath" value="/ajax/index/limitSalesPull/" />
	<tiles:put name="webAreaAjaxPath" value="/ajax/index/limitWebAreaSelect/" />
	<tiles:put name="railroadAjaxPath" value="/ajax/index/limitRailroadPull/" />
	<tiles:put name="routeAjaxPath" value="/ajax/index/limitRoutePull/" />
	<tiles:put name="stationAjaxPath" value="/ajax/index/limitStationPull/" />

	<tiles:put name="headJsp" value="/WEB-INF/view/webdata/head/webdataList.jsp" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ登録"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC01_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/webdata/input/" />
	<tiles:put name="subWindowPath" value="/customerSearch/select/" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayWebdataImage" />
	<tiles:put name="actionThumbMaterialPath" value="/util/imageUtility/displayWebdataImage" />
	<tiles:put name="upImgAjaxPath" value="/webdata/input/upAjaxMaterial" />
	<tiles:put name="delImgAjaxPath" value="/webdata/input/deleteAjaxMaterial" />
	<tiles:put name="setUnUploadMaterial" value="/webdata/input/setUnUploadMaterial" />
	<tiles:put name="pageTitle1" value="顧客データ選択" />
	<tiles:put name="pageTitleId1" value="title_customerDataSelect" />
	<tiles:put name="pageTitle2" value="WEBデータ登録" />
	<tiles:put name="pageTitleId2" value="title_webdataInput" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="必要事項を入力の上、「確認」ボタンを押して下さい。" />

	<% // 以下Ajax部品のパス %>
	<tiles:put name="volumeAjaxPath" value="/ajax/index/limitVolumePull/" />
	<tiles:put name="specialAjaxPath" value="/ajax/index/limitSpecialSelect/" />
	<tiles:put name="webAreaAjaxPath" value="/ajax/index/limitWebAreaCheck/" />
	<tiles:put name="detailAreaAjaxPath" value="/ajax/index/limitDetailAreaCheck/" />
	<tiles:put name="mainStationAjaxPath" value="/ajax/index/limitMainStationCheck/" />
	<tiles:put name="railroadAjaxPath" value="/ajax/index/limitRailroadPull/" />
	<tiles:put name="routeAjaxPath" value="/ajax/index/limitRoutePull/" />
	<tiles:put name="stationAjaxPath" value="/ajax/index/limitStationPull/" />
	<tiles:put name="salesAjaxPath" value="/ajax/index/limitSalesPull/" />

	<tiles:put name="headJsp" value="/WEB-INF/view/webdata/head/input.jsp" />
</tiles:insert>
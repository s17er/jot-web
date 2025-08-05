<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ登録確認"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC01_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/webdata/input/" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayWebdataImage" />
	<tiles:put name="actionThumbMaterialPath" value="/util/imageUtility/displayWebdataImage" />
	<tiles:put name="pageTitle1" value="顧客データ" />
	<tiles:put name="pageTitleId1" value="title_customerData" />
	<tiles:put name="pageTitle2" value="WEBデータ登録確認" />
	<tiles:put name="pageTitleId2" value="title_webdataInputConf" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「登録」ボタンを押して下さい。" />
</tiles:insert>
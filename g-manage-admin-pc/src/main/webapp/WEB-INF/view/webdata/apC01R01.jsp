<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ詳細"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC01_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DETAIL}" />
	<tiles:put name="actionPath" value="/webdata/detail/" />
	<tiles:put name="actionMaterialPath" value="/util/imageUtility/displayWebdataImage" />
	<tiles:put name="actionThumbMaterialPath" value="/util/imageUtility/displayWebdataImage" />
	<tiles:put name="copyActionPath" value="/webdata/input/copy" />
	<tiles:put name="deleteActionPath" value="/webdata/delete/" />
	<tiles:put name="editActionPath" value="/webdata/edit/changeStatus" />
	<tiles:put name="editPubilicationActionPath" value="/webdata/edit/changePublicationEndDisplayFlg" />
	<tiles:put name="pageTitle1" value="顧客データ" />
	<tiles:put name="pageTitleId1" value="title_customerData" />
	<tiles:put name="pageTitle2" value="WEBデータ詳細" />
	<tiles:put name="pageTitleId2" value="title_webdataDetail" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="WEBデータ一覧へ" />
	<tiles:put name="navigationPath2" value="/webdata/list/" />
	<tiles:put name="customerDetailPath" value="/customer/detail/indexFromWebdata" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
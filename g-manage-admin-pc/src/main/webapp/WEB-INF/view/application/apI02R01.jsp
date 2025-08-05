<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 質問・店舗見学データ詳細"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/apI02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DETAIL}" />
	<tiles:put name="actionPath" value="/observateApplication/detail/" />
	<tiles:put name="pageTitle1" value="質問・店舗見学データ詳細" />
	<tiles:put name="pageTitleId1" value="title_applicationDetail02" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="質問・店舗見学データ一覧へ" />
	<tiles:put name="navigationPath2" value="/observateApplication/list/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
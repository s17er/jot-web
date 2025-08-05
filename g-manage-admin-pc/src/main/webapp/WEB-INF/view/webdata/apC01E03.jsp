<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ編集完了"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/webdata/edit/" />
	<tiles:put name="pageTitle1" value="WEBデータ編集完了" />
	<tiles:put name="pageTitleId1" value="title_webdataEditComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="WEBデータ一覧へ" />
	<tiles:put name="navigationPath2" value="/webdata/list/" />
	<tiles:put name="defaultMsg" value="登録しました。" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 誌面データ移行完了"/>
	<tiles:put name="content" value="/WEB-INF/view/magazineImport/body/apF01_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_OTHER}" />
	<tiles:put name="actionPath" value="/magazineImport/import/" />
	<tiles:put name="pageTitle1" value="誌面データ移行完了" />
	<tiles:put name="pageTitleId1" value="title_magazineImportComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="以下のデータが移行されました。" />
</tiles:insert>
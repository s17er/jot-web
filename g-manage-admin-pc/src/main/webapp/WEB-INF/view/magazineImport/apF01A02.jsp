<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 誌面データ移行待ち"/>
	<tiles:put name="content" value="/WEB-INF/view/magazineImport/body/apF01_wait_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_OTHER}" />
	<tiles:put name="actionPath" value="/magazineImport/import/" />
	<tiles:put name="pageTitle1" value="誌面データ移行待ち" />
	<tiles:put name="pageTitleId1" value="title_magazineImportWait" />
	<tiles:put name="defaultMsg" value="取り込み中です。しばらくお待ち下さい。" />
</tiles:insert>
<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メルマガ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/mailMag/body/apK01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/mailMag/list/" />
	<tiles:put name="changePagePath" value="/mailMag/list/changePage/" />
	<tiles:put name="actionMaxRowPath" value="/mailMag/list/changeMaxRow/" />
	<tiles:put name="detailPath" value="/mailMag/detail/index" />
	<tiles:put name="pageTitle1" value="メルマガ一覧" />
	<tiles:put name="pageTitleId1" value="title_mailMagList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メルマガメニューへ" />
	<tiles:put name="navigationPath2" value="/mailMag/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
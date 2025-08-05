<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 合同企業説明会エントリーデータ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/advancedRegistration/body/apH01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/advancedRegistrationMember/list/" />
	<tiles:put name="actionCsvPath" value="/member/list/output/" />
	<tiles:put name="actionPrintOutPath" value="/advancedRegistrationMember/list/printOut/" />
	<tiles:put name="actionMaxRowPath" value="/advancedRegistrationMember/list/changeMaxRow" />
	<tiles:put name="pageTitle1" value="合同企業説明会エントリーデータ一覧" />
	<tiles:put name="pageTitleId1" value="title_AdvancedMemberList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
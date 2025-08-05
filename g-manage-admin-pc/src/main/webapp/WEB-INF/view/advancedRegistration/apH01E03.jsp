<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 合同企業説明会エントリーデータ編集完了"/>
	<tiles:put name="content" value="/WEB-INF/view/advancedRegistration/body/apH01_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/advancedRegistrationMember/edit/" />
	<tiles:put name="pageTitle1" value="合同企業説明会エントリーデータ編集完了" />
	<tiles:put name="pageTitleId1" value="title_advancedMemberEditComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="合同説明会エントリーデータ一覧へ" />
	<tiles:put name="navigationPath2" value="/advancedRegistrationMember/list/searchAgain/" />
	<tiles:put name="defaultMsg" value="合同企業説明会エントリーデータの修正が完了しました。" />
</tiles:insert>
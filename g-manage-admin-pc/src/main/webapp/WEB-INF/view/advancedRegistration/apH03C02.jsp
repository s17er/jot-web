<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 合同説明会会員向けメルマガ登録確認"/>
	<tiles:put name="content" value="/WEB-INF/view/advancedRegistration/body/apH03_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/advancedRegistrationMemberMailMag/input/" />
	<tiles:put name="pageTitle1" value="合同説明会会員向けメルマガ登録確認" />
	<tiles:put name="pageTitleId1" value="title_advancedMemberMailMagInput" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="合同説明会エントリーデータ一覧へ" />
	<tiles:put name="navigationPath2" value="/advancedRegistrationMember/list/searchAgain/" />
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「送信」ボタンを押して下さい。" />
</tiles:insert>
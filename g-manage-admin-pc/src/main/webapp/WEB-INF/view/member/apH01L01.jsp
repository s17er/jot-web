<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 会員データ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/member/body/apH01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/member/list/" />
	<tiles:put name="actionCsvPath" value="/member/list/output/" />
	<tiles:put name="actionMaxRowPath" value="/member/list/changeMaxRow" />
	<tiles:put name="pageTitle1" value="会員データ一覧" />
	<tiles:put name="pageTitleId1" value="title_memberList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
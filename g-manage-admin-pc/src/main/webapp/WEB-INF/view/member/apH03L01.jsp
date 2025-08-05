<%@page pageEncoding="UTF-8"%>
<%-- 仮会員データ一覧 --%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 仮会員データ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/member/body/apH03_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/tempMember/list/" />
	<tiles:put name="actionCsvPath" value="/tempMember/list/output/" />
	<tiles:put name="actionMaxRowPath" value="/tempMember/list/changeMaxRow" />
	<tiles:put name="pageTitle1" value="仮会員データ一覧" />
	<tiles:put name="pageTitleId1" value="title_tempMemberList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
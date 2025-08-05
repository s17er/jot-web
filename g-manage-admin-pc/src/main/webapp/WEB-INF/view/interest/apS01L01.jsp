<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 気になる通知データ一覧"/>
	<tiles:put name="content" value="/WEB-INF/view/interest/body/apS01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/interest/list/" />
	<tiles:put name="actionCsvPath" value="/interest/list/output/" />
	<tiles:put name="actionMaxRowPath" value="/interest/list/changeMaxRow" />
	<tiles:put name="pageTitle1" value="気になる通知データ一覧" />
	<tiles:put name="pageTitleId1" value="title_interestList" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
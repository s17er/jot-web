<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 求人原稿一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/spD02_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/webdata/list/focusPhoneApplicationOrder" />
	<tiles:put name="changePagePath" value="/webdata/list/changePage/" />
	<tiles:put name="pageTitle" value="求人原稿一覧" />
	<tiles:put name="pageTitleId" value="jobinfo" />
	<tiles:put name="defaultMsg0" value="過去1年間の掲載原稿履歴です。"/>
	<tiles:put name="defaultMsg1" value="プレビューから原稿が確認できます。"/>
	<tiles:put name="defaultMsg2" value="応募者数をクリックすると、求人応募者の一覧が確認できます。"/>
</tiles:insert>
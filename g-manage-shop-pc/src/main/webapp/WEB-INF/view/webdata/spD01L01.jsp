<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="求人原稿 一覧 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/spD01_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/webdata/list/" />
	<tiles:put name="pageNaviPath" value="/webdata/list/changePage/" />
	<tiles:put name="pageTitle" value="求人･店舗情報" />
	<tiles:put name="pageTitleId" value="jobinfo" />
	<tiles:put name="defaultMsg0" value="過去1年間の掲載原稿の一覧となります。"/>
	<tiles:put name="defaultMsg1" value="応募者数をクリックすると応募者の一覧が確認できます。"/>
	<tiles:put name="defaultMsg2" value=""/>
</tiles:insert>
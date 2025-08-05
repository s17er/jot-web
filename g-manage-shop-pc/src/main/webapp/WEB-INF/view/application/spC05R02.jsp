<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 応募者詳細画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC05_detail_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="detail" />
	<tiles:put name="actionPath" value="/arbeit/detail" />
	<tiles:put name="pageTitle" value="応募者詳細" />
	<tiles:put name="pageTitleId" value="entryDetail" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
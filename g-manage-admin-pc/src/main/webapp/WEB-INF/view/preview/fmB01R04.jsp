<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/frontMobileLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} ${seoTitle}"/>
	<tiles:put name="head" value="/WEB-INF/view/common/listHead.jsp" />
	<tiles:put name="content" value="/WEB-INF/view/preview/body/fmB01R04_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="actionPath" value="/webdata/detail/" />
	<tiles:put name="listActionPath" value="/webdata/list/" />
</tiles:insert>
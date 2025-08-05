<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/frontLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} ${f:h(seoTitle)}"/>
	<tiles:put name="head" value="/WEB-INF/view/common/detailHead.jsp" />
	<tiles:put name="content" value="/WEB-INF/view/preview/body/apO01A03_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="actionPath" value="/detailPreview/detail/" />
	<tiles:put name="jspTabKbn" value="map" />
</tiles:insert>
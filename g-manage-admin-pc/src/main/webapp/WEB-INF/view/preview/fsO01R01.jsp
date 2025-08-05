<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/frontSmartDetailLayout.jsp" flush="false">
	<tiles:put name="title" value="${f:h(seoTitle)}"/>

	<tiles:put name="head" value="/WEB-INF/view/preview/smart/detailHead.jsp" />
	<tiles:put name="header" value="/WEB-INF/view/preview/smart/detailHeader.jsp" />
	<tiles:put name="content" value="/WEB-INF/view/preview/body/fsO01R01_body.jsp" />
	<tiles:put name="footer" value="/WEB-INF/view/preview/smart/detailFooter.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="actionPath" value="/webdata/list/" />

	<tiles:put name="detailHeaderTab" value="/WEB-INF/view/preview/smart/detailHeaderTab.jsp" />
	<tiles:put name="messagePageFlg" value="${false}" />
</tiles:insert>
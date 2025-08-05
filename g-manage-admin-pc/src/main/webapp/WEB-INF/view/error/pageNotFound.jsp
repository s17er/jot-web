<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="true">
<tiles:put name="title"  value="${SITE_NAME} | ページが見つかりません"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/pageNotFound_body.jsp" />
</tiles:insert>

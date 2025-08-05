<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/commonLayout.jsp" flush="true">
<tiles:put name="title"  value="${SITE_NAME} | システムエラー"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/systemError_body.jsp" />
</tiles:insert>

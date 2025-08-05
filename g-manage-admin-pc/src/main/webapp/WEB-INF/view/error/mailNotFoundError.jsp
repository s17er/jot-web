<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/commonLayout.jsp" flush="true">
<tiles:put name="title"  value="${SITE_NAME} | メールが存在しません"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/error_body.jsp" />
</tiles:insert>

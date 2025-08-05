<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="true">
<tiles:put name="title"  value="${SITE_NAME} | アクセス制限エラー"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/error_body.jsp" />
</tiles:insert>

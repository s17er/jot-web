<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/commonLayout.jsp" flush="true">
<tiles:put name="title"  value="${SITE_NAME} | セッションタイムアウト"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/error_body.jsp" />
<% // 以下ページ切り替え用パラメータ %>
<tiles:put name="pageFlg" value="sessionTimeout" />
</tiles:insert>

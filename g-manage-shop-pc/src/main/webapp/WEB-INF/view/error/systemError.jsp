<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
<tiles:put name="title"  value="システムエラー | グルメキャリー 顧客管理画面"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/systemError_body.jsp" />
</tiles:insert>

<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
<tiles:put name="title"  value="不正なプロセスエラー | グルメキャリー 顧客管理画面"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/error_body.jsp" />
</tiles:insert>

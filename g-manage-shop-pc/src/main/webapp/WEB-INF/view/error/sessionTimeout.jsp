<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
<tiles:put name="title"  value="セッションタイムアウト | グルメキャリー 顧客管理画面"/>
<tiles:put name="content" value="/WEB-INF/view/error/body/error_body.jsp" />
<% // 以下ページ切り替え用パラメータ %>
<tiles:put name="pageFlg" value="sessionTimeout" />
</tiles:insert>

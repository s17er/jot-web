<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="お問い合わせ | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/contact/body/spL01_conf_body.jsp" />

	<tiles:put name="actionPath" value="/contact/" />
</tiles:insert>
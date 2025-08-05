<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メルマガヘッダメッセージ削除"/>
	<tiles:put name="content" value="/WEB-INF/view/mailMag/body/apK02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DELETE}" />
	<tiles:put name="actionPath" value="/mailMagOption/headerDelete/" />
	<tiles:put name="deleteActionPath" value="/mailMagOption/headerDelete/submit" />
	<tiles:put name="pageTitle1" value="メルマガヘッダメッセージ削除" />
	<tiles:put name="pageTitleId1" value="title_mailMagHeadMsg" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メルマガメニューへ" />
	<tiles:put name="navigationPath2" value="/mailMag/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
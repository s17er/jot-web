<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メール詳細画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC04_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="detail" />
	<tiles:put name="actionPath" value="/observateApplicationMail/detail/submit" />
	<tiles:put name="actionDetailMailListPath" value="/observateApplication/detailMailList" />
	<tiles:put name="pageTitle" value="メール詳細" />
	<tiles:put name="pageTitleId" value="mailDetail" />
	<tiles:put name="defaultMsg" value="" />
	<tiles:put name="selectPatternAjaxPath" value="/util/patternSentence/addSentence" />
</tiles:insert>
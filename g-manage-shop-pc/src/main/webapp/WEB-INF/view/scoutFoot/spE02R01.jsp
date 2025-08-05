<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | メール詳細画面"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="detail" />
	<tiles:put name="actionPath" value="/scoutMail/detail/submit" />
	<tiles:put name="pageTitle" value="メール詳細" />
	<tiles:put name="pageTitleId" value="mailDetail" />
	<tiles:put name="defaultMsg" value=""/>
	<tiles:put name="memberListPath" value="/member/list" />
	<tiles:put name="scoutMailPath" value="/scoutMail/list" />
	<tiles:put name="keepBoxPath" value="/member/keepBox" />
	<tiles:put name="SEND" value="1" />
	<tiles:put name="RECEIVE" value="2" />
	<tiles:put name="selectPatternAjaxPath" value="/util/patternSentence/addSentence" />
</tiles:insert>
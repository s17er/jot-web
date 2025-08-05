<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="キープBOX | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE03_list_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="" />
	<tiles:put name="actionPath" value="/member/keepBox" />
	<tiles:put name="pageTitle" value="キープBOX" />
	<tiles:put name="pageTitleId" value="keepBox" />
	<tiles:put name="defaultMsg" value="" />
	<tiles:put name="memberListPath" value="/member/list" />
	<tiles:put name="scoutMailPath" value="/scoutMail/list" />
	<tiles:put name="keepBoxPath" value="/member/keepBox" />
	<tiles:put name="price" value="/price/" />
</tiles:insert>
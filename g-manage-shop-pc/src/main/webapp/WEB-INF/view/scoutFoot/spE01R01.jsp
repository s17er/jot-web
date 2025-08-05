<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 求職者詳細画面"/>
	<tiles:put name="content" value="/WEB-INF/view/scoutFoot/body/spE01_detail_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="detail" />
	<tiles:put name="actionPath" value="/member/detail" />
	<tiles:put name="pageTitle" value="求職者詳細" />
	<tiles:put name="pageTitleId" value="memberDetail" />
	<tiles:put name="defaultMsg1" value="スカウトメール送信ボタンから、求職者へスカウトメールを送信することができます。"/>
	<tiles:put name="defaultMsg2" value="気になるボタンをクリックすると、求職者のMYページに「あなたに興味を持った店舗」として表示されます。"/>
	<tiles:put name="listPath" value="/member/list" />
	<tiles:put name="backListPath" value="/member/detail/backList" />
	<tiles:put name="backKeepBoxPath" value="/member/keepBox/searchAgain" />
	<tiles:put name="backScoutMailPath" value="/scoutMember/list/searchAgain" />
	<tiles:put name="backPreApplicationMailPath" value="/preApplication/list/searchAgain" />
	<tiles:put name="scoutMailPath" value="/scoutMail/list" />
	<tiles:put name="keepPath" value="/member/detail/addKeepBox" />
	<tiles:put name="deleteKeepPath" value="/member/detail/deleteKeepBox" />
	<tiles:put name="footPrintPath" value="/member/detail/footPrint" />
	<tiles:put name="keepBoxPath" value="/member/keepBox" />
	<tiles:put name="scoutMailInputPath" value="/member/detail/sendMail" />

</tiles:insert>
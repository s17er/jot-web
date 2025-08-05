<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 応募者一覧画面"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC05_list_arbeit_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="list" />
	<tiles:put name="actionPath" value="/arbeit/list" />
	<tiles:put name="actionLumpSendPath" value="/arbeit/list/lumpSend" />
	<tiles:put name="pageTitle" value="グルメdeバイト応募者一覧" />
	<tiles:put name="pageTitleId" value="beitEntryList" />
	<tiles:put name="defaultMsg0" value="過去1年間のメール履歴一覧です。" />
	<tiles:put name="defaultMsg1" value="詳細をクリックすると、応募者の詳細がご覧いただけます。"/>
	<tiles:put name="defaultMsg2" value="詳細画面の「返信する」ボタンを押すと、応募者に返信を行うことができます。" />
	<tiles:put name="defaultMsg3" value="応募者をクリックすると、応募者のメール履歴がご覧いただけます。" />
	<tiles:put name="defaultMsg4" value="※チェックを入れた人にメールを一括送信できます。" />
	<tiles:put name="defaultMsg5" value="" />
	<tiles:put name="pageNaviPath" value="/arbeit/list/changePage/" />
</tiles:insert>
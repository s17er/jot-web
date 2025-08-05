<%@page pageEncoding="UTF-8"%>
<%-- 仮会員データ編集入力 --%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 仮会員データ編集"/>
	<tiles:put name="content" value="/WEB-INF/view/member/body/apH03_input_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/tempMember/edit/" />
	<tiles:put name="pageTitle1" value="仮会員データ編集" />
	<tiles:put name="pageTitleId1" value="title_tempMemberEdit" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="仮会員データ一覧へ" />
	<tiles:put name="navigationPath2" value="/member/list/" />
	<tiles:put name="defaultMsg" value="必要事項を入力の上、「確認」ボタンを押して下さい。" />
</tiles:insert>
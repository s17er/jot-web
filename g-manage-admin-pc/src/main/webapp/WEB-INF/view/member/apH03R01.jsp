<%@page pageEncoding="UTF-8"%>
<%-- 仮会員データ詳細 --%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 仮会員データ詳細"/>
	<tiles:put name="content" value="/WEB-INF/view/member/body/apH03_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DETAIL}" />
	<tiles:put name="actionPath" value="/tempMember/detail/" />
	<tiles:put name="pageTitle1" value="仮会員データ詳細" />
	<tiles:put name="pageTitleId1" value="title_tempMemberDetail" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="仮会員データ一覧へ" />
	<tiles:put name="navigationPath2" value="/tempMember/list/" />
	<tiles:put name="defaultMsg" value="" />

	<tiles:put name="editPath" value="/tempMember/edit/${id}"></tiles:put>
	<tiles:put name="signInPath" value="/tempMember/detail/signIn/${id}"></tiles:put>
</tiles:insert>
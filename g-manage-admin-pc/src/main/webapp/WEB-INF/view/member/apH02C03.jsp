<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 会員向けメルマガ登録完了"/>
	<tiles:put name="content" value="/WEB-INF/view/member/body/apH_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_OTHER}" />
	<tiles:put name="actionPath" value="/memberMailMag/input/" />
	<tiles:put name="pageTitle1" value="会員向けメルマガ登録完了" />
	<tiles:put name="pageTitleId1" value="title_memberMailMagInputComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="会員データ一覧へ" />
	<tiles:put name="navigationPath2" value="/member/list/" />
	<tiles:put name="defaultMsg" value="送信しました。" />
</tiles:insert>
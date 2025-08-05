<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 人材紹介登録者向けメルマガ登録確認"/>
	<tiles:put name="content" value="/WEB-INF/view/juskill/body/apQ02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_INPUT}" />
	<tiles:put name="actionPath" value="/juskillMailMag/input/" />
	<tiles:put name="pageTitle1" value="人材紹介登録者向けメルマガ登録確認" />
	<tiles:put name="pageTitleId1" value="title_juskillMemberMailMagInput" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="人材紹介登録者データ一覧へ" />
	<tiles:put name="navigationPath2" value="/juskillMember/list/" />
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「送信」ボタンを押して下さい。" />
</tiles:insert>
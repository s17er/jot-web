<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 特集データ削除完了"/>
	<tiles:put name="content" value="/WEB-INF/view/maintenance/body/apJ_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DELETE}" />
	<tiles:put name="actionPath" value="/special/delete/" />
	<tiles:put name="pageTitle1" value="特集データ削除完了" />
	<tiles:put name="pageTitleId1" value="title_specialDelComp" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="メンテナンスメニューへ" />
	<tiles:put name="navigationPath2" value="/maintenance/menu/" />
	<tiles:put name="navigationText3" value="特集データ一覧へ" />
	<tiles:put name="navigationPath3" value="/special/list/" />
	<tiles:put name="defaultMsg" value="削除しました。" />
</tiles:insert>
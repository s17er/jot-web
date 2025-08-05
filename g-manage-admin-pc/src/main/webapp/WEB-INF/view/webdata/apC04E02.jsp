<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ一括確定確認"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC_lumpConf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_EDIT}" />
	<tiles:put name="actionPath" value="/lumpDecide/edit/" />
	<tiles:put name="submitPath" value="/lumpDecide/edit/submit/" />
	<tiles:put name="backPath" value="/lumpDecide/edit/back/" />
	<tiles:put name="pageTitle1" value="WEBデータ一括確定確認" />
	<tiles:put name="pageTitleId1" value="title_webdataLumpDecideConf" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="WEBデータ一覧へ" />
	<tiles:put name="navigationPath2" value="/webdata/list/" />
	<tiles:put name="defaultMsg" value="内容を確認の上、「登録」ボタンを押して下さい。" />
</tiles:insert>
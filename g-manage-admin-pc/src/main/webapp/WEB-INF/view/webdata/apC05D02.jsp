<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | WEBデータ一括削除確認"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC_lumpConf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_DELETE}" />
	<tiles:put name="actionPath" value="/lumpDelete/delete/" />
	<tiles:put name="submitPath" value="/lumpDelete/delete/submit/" />
	<tiles:put name="backPath" value="/lumpDelete/delete/back/" />
	<tiles:put name="pageTitle1" value="WEBデータ一括削除確認" />
	<tiles:put name="pageTitleId1" value="title_webdataLumpDelConf" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="WEBデータ一覧へ" />
	<tiles:put name="navigationPath2" value="/webdata/list/" />
	<tiles:put name="defaultMsg" value="以下の内容でよろしければ、「削除」ボタンから削除して下さい。" />
</tiles:insert>
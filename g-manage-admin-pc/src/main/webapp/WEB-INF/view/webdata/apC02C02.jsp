<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 応募テストデータ登録確認"/>
	<tiles:put name="content" value="/WEB-INF/view/webdata/body/apC02_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_OTHER}" />
	<tiles:put name="actionPath" value="/appTest/input/" />
	<tiles:put name="pageTitle1" value="顧客データ" />
	<tiles:put name="pageTitleId1" value="title_customerData" />
	<tiles:put name="pageTitle2" value="応募テストデータ登録確認" />
	<tiles:put name="pageTitleId2" value="title_applicationTestInputConf" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="navigationText2" value="WEBデータ一覧へ" />
	<tiles:put name="navigationPath2" value="/webdata/list/" />
	<tiles:put name="defaultMsg" value="登録内容を確認の上、「送信」ボタンを押して下さい。" />
</tiles:insert>
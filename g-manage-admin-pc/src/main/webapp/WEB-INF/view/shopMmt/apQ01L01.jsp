<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/adminLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 掲載中の店舗出力"/>
	<tiles:put name="content" value="/WEB-INF/view/shopMmt/body/apQ01_index_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="${PAGE_LIST}" />
	<tiles:put name="actionPath" value="/shopMmt/index/" />
	<tiles:put name="actionCsvPath" value="/shopMmt/index/export/" />
	<tiles:put name="pageTitle1" value="掲載中の店舗出力" />
	<tiles:put name="pageTitleId1" value="title_shopMmt" />
	<tiles:put name="navigationText1" value="管理画面トップへ" />
	<tiles:put name="navigationPath1" value="/top/menu/" />
	<tiles:put name="defaultMsg" value="" />
</tiles:insert>
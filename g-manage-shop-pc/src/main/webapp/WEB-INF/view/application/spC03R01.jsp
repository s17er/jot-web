<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/commonLayout.jsp" flush="false">
	<tiles:put name="title" value="${SITE_NAME} | 応募テスト確認完了"/>
	<tiles:put name="content" value="/WEB-INF/view/application/body/spC03_conf_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageTitle" value="応募テスト確認完了" />
	<tiles:put name="pageInfo" value="応募テスト確認完了" />
	<tiles:put name="defaultMsg" value="メールアドレスの確認ができました。<br />アドレスに変更がある場合は、運営者までご連絡ください。" />
</tiles:insert>
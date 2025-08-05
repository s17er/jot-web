<%@page pageEncoding="UTF-8"%>
<tiles:insert template="/WEB-INF/view/common/shopLayout.jsp" flush="false">
	<tiles:put name="title" value="定型文の削除完了 | グルメキャリー 顧客管理画面"/>
	<tiles:put name="content" value="/WEB-INF/view/pattern/body/spF01_comp_body.jsp" />

	<% // 以下ページ切り替え用パラメータ %>
	<tiles:put name="pageKbn" value="delete" />
	<tiles:put name="actionPath" value="/pattern/delete" />
	<tiles:put name="pageTitle" value="定型文の削除完了" />
	<tiles:put name="pageTitleId" value="patternDelComp" />
	<tiles:put name="defaultMsg" value="定型文を削除しました。"/>
</tiles:insert>
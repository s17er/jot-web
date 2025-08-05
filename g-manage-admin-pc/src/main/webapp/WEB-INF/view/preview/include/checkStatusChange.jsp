<%@page pageEncoding="UTF-8"%>
<%-- WEBデータのチェックステータスを変更するためのボタンを表示するインクルードJSP --%>
<c:if test="${changeableCheckStatus}">
	<div id="changeCheckStatus">
		<s:form action="/detailPreview/dbDetail/">
			<html:hidden property="id"/>
			<html:submit property="changeCheckStatus" value="チェック済みにする" styleId="changeCheckStatusButton" />
		</s:form>
	</div>
</c:if>
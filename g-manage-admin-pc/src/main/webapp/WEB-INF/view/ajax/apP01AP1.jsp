<%@page pageEncoding="UTF-8"%>
<script>
$(function(){
	// selectBoxの設定
	$('#specialSelect').multiSelect({ keepOrder: true });

});
</script>
<% //特集のセレクトボックス  %>
<gt:specialList name="specialList" limitValue="${limitValue}"/>

<select name="specialIdList" multiple="multiple" id="specialSelect">
	<c:forEach items="${specialList}" var="s">
		<option value="${f:h(s.value)}">${f:h(s.label)}</option>
	</c:forEach>
</select>


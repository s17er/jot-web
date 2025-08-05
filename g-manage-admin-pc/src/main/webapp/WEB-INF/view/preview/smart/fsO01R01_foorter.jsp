<%@page pageEncoding="UTF-8"%>

<c:set var="telNo" value="${gf:findTelFirstElement(f:h(phoneReceptionist),true)}" scope="page" />

<c:if test="${applicationOkFlg or not empty telNo}">

<script type="text/javascript">
$(function(){
	$("#adBNRbtn").ready(function(){
		$("#adBNRimg").slideDown(1000);
		});
});
</script>

<div id="adBNRarea">
<div id="adBNRimg">
<c:if test="${not empty telNo}">
<span class="tel_font"><a href="tel:${telNo}">電話で応募</a></span>
</c:if>
<c:if test="${applicationOkFlg}">
	<span class="web_font"><a href="#">WEBで応募</a></span>
</c:if>
</div>
</div>
</c:if>
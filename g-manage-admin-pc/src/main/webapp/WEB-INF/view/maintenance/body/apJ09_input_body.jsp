<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>

<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn"%>
<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" scope="request"/>
<c:set var="SENDAI_AREA" value="<%= MAreaConstants.AreaCd.SENDAI_AREA %>" scope="request"/>

<c:set var="WEB_MODE" value="1" scope="page"/>
<c:set var="SHOP_MODE" value="2" scope="page"/>

<c:if test="${mode eq WEB_MODE}">
<c:set var="TAG_LIST" value="${webTagList}" scope="page"/>
</c:if>
<c:if test="${mode eq SHOP_MODE}">
<c:set var="TAG_LIST" value="${shopTagList}" scope="page"/>
</c:if>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<c:if test="${mode eq WEB_MODE}">
		<h2 class="title date">${f:h(pageTitle1)}</h2>
	</c:if>
	<c:if test="${mode eq SHOP_MODE}">
		<h2 class="title shop">${f:h(pageTitle2)}</h2>
	</c:if>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div id="wrap_form">

		<s:form action="${f:h(actionPath)}" styleId="editForm">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
				<c:forEach items="${TAG_LIST}" var="dto" varStatus="status">
				<tr>
					<td class="posi_center" width="40px">
						${status.count}
					</td>
					<td width="80%">
						<c:if test="${mode eq WEB_MODE}">
							<html:text property="webTagList[${status.index}].webTagName" style="width:90%; margin:2px;"/>
						</c:if>
						<c:if test="${mode eq SHOP_MODE}">
							<html:text property="shopTagList[${status.index}].shopListTagName" style="width:90%; margin:2px;"/>
						</c:if>
					</td>
					<td class="posi_center release bdrs_right">
						<input type="button" name="" value="削 除" data-tagdeleteid="${dto.id}" class="tagDeleteButton" />
					</td>
				</tr>
				</c:forEach>
			</table>
			<br>
			<br>
			<input type="button" name="" value="更 新" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" class="tagEditButton" />
			<html:hidden property="mode"/>
			<html:hidden property="id" value="" styleId="tagId"/>
			<html:hidden property="listIndex" value="" styleId="tagIndex" />
			<hr>
		</s:form>

		<s:form action="${f:h(deletePath)}" styleId="deleteForm">
			<html:hidden property="mode"/>
			<html:hidden property="id" value="" styleId="tagDeleteId"/>
		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<script type="text/javascript">
		$(".tagEditButton").on("click", function() {
			$("#editForm").submit();
		});

		$(".tagDeleteButton").on("click", function() {

			if (confirm('本当に削除してもよろしいですか？')) {

				const id = $(this).data("tagdeleteid");

				$("#tagDeleteId").val(id);

				$("#deleteForm").submit();
			}
		});

	</script>

</c:if>

</div>

<!-- #main# -->

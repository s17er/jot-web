<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>

<gt:typeList name="deliveryTypeKbnList" typeCd="<%=MTypeConstants.deliveryTypeKbn.TYPE_CD %>"   />
<c:set var="HTML" value="<%=MTypeConstants.deliveryTypeKbn.HTML%>" />
<c:set var="TEXT" value="<%=MTypeConstants.deliveryTypeKbn.TEXT%>" />

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle1)}" class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">タイトル</th>
					<td>${f:h(mailMagazineTitle)}</td>
				</tr>
				<tr>
					<th width="150">配信形式</th>
					<td>${f:label(deliveryType, deliveryTypeKbnList, 'value', 'label')}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">本文</th>
					<td class="bdrs_bottom">
					<c:choose>
						<c:when test="${deliveryType eq HTML }">
							${htmlBody}
						</c:when>
						<c:when test="${deliveryType eq TEXT }">
							${f:br(f:h(textBody))}
						</c:when>
					</c:choose>
					</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="submit" value="送 信" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

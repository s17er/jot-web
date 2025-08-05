<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<gt:areaList name="areaList"/>

<c:set var="ADMIN_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.ADMIN_SCREEN %>" scope="page" />

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<li><a href="${f:url(navigationPath3)}">${f:h(navigationText3)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">

		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th colspan="2" class="td_title bdrs_right">
						<c:choose>
							<c:when test="${managementScreenKbn eq ADMIN_SCREEN}">
								エリア共通&nbsp;
							</c:when>
							<c:otherwise>
								${f:label(areaCd, areaList, 'value', 'label')}&nbsp;
							</c:otherwise>
						</c:choose>
					</th>
				</tr>
				<tr>
					<th width="150" class="bdrs_bottom">本文</th>
					<td class="td_fck bdrs_bottom">
						${body}&nbsp;
					</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

		</s:form>

	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<hr />

</div>
<!-- #main# -->

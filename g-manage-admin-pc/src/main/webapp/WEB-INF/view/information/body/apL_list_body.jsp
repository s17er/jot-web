<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<gt:areaList name="areaList"/>

<c:set var="ADMIN_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.ADMIN_SCREEN %>" scope="page" />
<c:set var="SHOP_SCREEN" value="<%= MTypeConstants.ManagementScreenKbn.SHOP_SCREEN %>" scope="page" />
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title application">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<c:if test="${existDataFlg eq true}">
		<div id="wrap_form">

		<c:forEach items="${informationList}" var="dto">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th colspan="2" class="td_title bdrs_right">
						<c:choose>
							<c:when test="${managementScreenKbn eq ADMIN_SCREEN}">
								エリア共通&nbsp;
							</c:when>
							<c:otherwise>
								${f:label(dto.areaCd, areaList, 'value', 'label')}&nbsp;
							</c:otherwise>
						</c:choose>
					</th>
				</tr>
				<tr>
					<th width="150" class="bdrs_bottom">本文</th>
					<td class="td_fck bdrs_bottom">
			        	${dto.body}&nbsp;
			        </td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<input type="button" onclick="location.href='${f:url(dto.editPagePath)}'" value="編 集" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>
		</c:forEach>


		</div>
	</c:if>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<hr />
</div>
<!-- #main# -->

<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>

<gt:areaList name="areaList" authLevel="${userDto.authLevel}" />

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

	<s:form action="${f:h(actionPath)}showList" styleId="selectForm">
		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
<!-- 一旦コメントアウト
				<td>エリア&nbsp;:
					<gt:areaList name="area" />
					${f:label(areaCd, area, 'value', 'label')}
				</td>
 -->
<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
				<td class="pull_down">
					<gt:maxRowList name="maxRowList" value="${common['gc.volume.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
					<html:select property="maxRowValue"  onchange="changeMaxRow('selectForm');">
						<html:optionsCollection name="maxRowList" />
					</html:select>
				</td>
</c:if>
			</tr>
		</table>
	</s:form>
	<!-- #pullDown# -->
	<hr />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table tb_margin list_table stripe_table">
		<tr>
			<th width="30" class="posi_center">ID</th>
			<th width="90">メールマガジンID</th>
			<th width="90">配信予定日</th>
			<th width="90">配信エリア</th>
			<th>ヘッダメッセージ</th>
			<th width="45" class="posi_center">詳細</th>
			<th width="45" class="posi_center">編集</th>
			<th width="45" class="posi_center bdrs_right">削除</th>
		</tr>
		<% //ループ処理 %>

		<c:forEach var="dto" items="${dtoList}">
			<tr>
				<td class="posi_center">
					${f:h(dto.id)}
				</td>
				<td class="posi_center">
					${f:h(dto.mailMagazineId)}
				</td>
				<td class="posi_center">
					<fmt:formatDate value="${dto.deliveryScheduleDatetime}" pattern="yyyy年MM月dd日"/>
				</td>
				<td class="posi_center">
					${f:h(f:label(dto.areaCd, areaList, 'value', 'label'))}
				</td>
				<td>
					${f:h(gf:removeCrToSpace(gf:replaceStr(dto.optionValue, common['gc.mailMag.common.trim.length'], common['gc.replaceStr2'])))}
				</td>
				<td class="posi_center">
					<a href="${f:url(dto.detailPath)}">詳細</a>
				</td>
				<td class="posi_center">
					<c:if test="${dto.editFlg}">
						<a href="${f:url(dto.editPath)}">編集</a>
					</c:if>
				</td>
				<td  class="posi_center bdrs_right">
					<c:if test="${dto.editFlg}">
						<a href="${f:url(dto.deletePath)}">削除</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>
<!-- #main# -->

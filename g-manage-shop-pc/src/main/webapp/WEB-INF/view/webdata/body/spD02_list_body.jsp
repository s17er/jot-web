<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MStatusConstants"%>
<script type="text/javascript" src="${SHOP_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/preview.js"></script>
<script type="text/javascript">
$(function () {
	// 順番変更セレクトボックスのチェンジイベント
	$('#selectionKbn').on('change', function () {
		$("#search").trigger("click");
	});
});
</script>
<div id="main">

<div id="wrap_web-shoplist">
	<div class="page_back">
		<a href="${f:url('/webdata/list/showList')}" id="btn_back">戻る</a>
	</div>
	<h2>電話応募者 一覧</h2>
	<p class="explanation">
		原稿名：${f:h(tWeb.manuscriptName)}<br />
		掲載期間：<fmt:formatDate value="${postStartDatetime}" pattern="yyyy/MM/dd" /> ～ <fmt:formatDate value="${postEndDatetime}" pattern="yyyy/MM/dd" />（原稿番号：${f:h(tWeb.id)}）の電話履歴となります。
	</p>
	<div class="menu_tab">
		<div class="menu_list"><ul>
			<li class="tab_active">
				<a href="${f:url('/webdata/list/')}">求人原稿</a>
			</li>
			<li>
				<a href="${f:url('/shopList/')}">店舗一覧</a>
			</li>
		</ul></div>
	</div>

	<div id="wrap_masc_content">
		<div class="tab_area">
			<div class="tab_contents tab_active">
				<div class="narrowing_area">

				</div>
				<s:form action="${f:h(actionPath)}">
				<div class="selectbox">
					<html:select property="where_displayOrder" styleId="selectionKbn" class="sort_bar">
						<c:choose>
							<c:when test="${where_displayOrder == '1'}">
								<option value="1" selected>新着順</option>
							</c:when>
							<c:otherwise>
								<option value="1">新着順</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${where_displayOrder == '2'}">
								<option value="2" selected>通話切断理由順</option>
							</c:when>
							<c:otherwise>
								<option value="2">通話切断理由順</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${where_displayOrder == '3'}">
								<option value="3" selected>通話時間が多い順</option>
							</c:when>
							<c:otherwise>
								<option value="3">通話時間が多い順</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${where_displayOrder == '4'}">
								<option value="4" selected>通話ステータス順</option>
							</c:when>
							<c:otherwise>
								<option value="4">通話ステータス順</option>
							</c:otherwise>
						</c:choose>
					</html:select>
				</div>
				<html:hidden property="webId" value="${tWeb.id}"/>
				<html:submit property="search" value="" styleId="search" hidden="hidden"/>
				</s:form>
				<div class="webdata_list">
					<table cellpadding="0" cellspacing="0" border="0" class="all_table">
						<tbody>
						<tr>
							<th width="60">No</th>
							<th width="180">応募日時</th>
							<th>通話切断理由</th>
							<th width="160" class="posi_center">受信番号</th>
							<th width="160" class="posi_center">応募者番号</th>
							<th width="100" class="posi_center">通話時間</th>
							<th width="140" class="posi_center">通話ステータス</th>
						</tr>
						<c:forEach var="dto" items="${dtoList}" varStatus="status">
							<tr>
								<td>${f:h(status.count)}</td>
								<td><fmt:formatDate value="${dto.callNoteCaughtMemberTelDatetime}" pattern="yyyy/MM/dd HH:mm"/></td>
								<td>${f:h(dto.telStatusDetail)}</td>
								<td class="posi_center">${f:h(dto.customerTel)}</td>
								<td class="posi_center">${f:h(dto.memberTel)}</td>
								<td class="posi_center">${f:h(dto.telTime)}</td>
								<td class="posi_center">${f:h(dto.telStatusName)}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

</div>

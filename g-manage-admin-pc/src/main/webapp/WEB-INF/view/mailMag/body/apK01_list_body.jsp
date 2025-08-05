<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--
	// 「DatePicker」の搭載
	$(function(){
		$("#deliveryStartDate").datepicker();
		$("#deliveryEndDate").datepicker();
	});
// -->
</script>
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

	<!-- #wrap_serch# -->
	<div id="wrap_search">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">検索</th>
				</tr>
				<tr>
					<th>配信期間</th>
					<td class="release">
						<html:text property="deliveryStartDate" size="12" styleId="deliveryStartDate" styleClass="disabled" maxlength="10" />
						&nbsp;～&nbsp;
						<html:text property="deliveryEndDate" size="12" styleId="deliveryEndDate" styleClass="disabled" maxlength="10" />
					</td>
					<th>配信先</th>
					<td width="120">
						<gt:typeList name="deliveryKbnList" typeCd="<%=MTypeConstants.DeliveryKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="deliveryKbn">
							<html:optionsCollection name="deliveryKbnList" />
						</html:select>
					</td>
				</tr>
				<tr>
					<th>タイトル</th>
					<td class="release" colspan="3">
						<html:text property="mailMagazineTitle" size="60" />
					</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>
		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

<c:if test="${existDataFlg}">
	<!-- #wrap_result# -->
	<div id="wrap_result">
		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>${pageNavi.allCount}件検索されました。</td>
				<td class="pull_down">
					<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
						<gt:maxRowList name="maxRowList" value="${common['gc.mailMag.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
						<html:select property="maxRow" onchange="changeMaxRow('MaxRowSelect');">
							<html:optionsCollection name="maxRowList" />
						</html:select>
					</s:form>
				</td>
			</tr>
		</table>
		<!-- #pullDown# -->
		<hr />

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
				<td><!--
					<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
						<c:choose>
							<c:when test="${dto.linkFlg eq true}">
								<% // gt:PageNaviのpathはc:setで生成する。 %>
								<c:set var="pageLinkPath" scope="page" value="${changePagePath}${dto.pageNum}" />
								--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
							</c:when>
							<c:otherwise>
								--><span>${dto.label}</span><!--
							</c:otherwise>
						</c:choose>
					</gt:PageNavi>
				--></td>
			</tr>
		</table>
		<!-- #page# -->
		<hr />

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
			<tr>
				<th rowspan="2" width="10" class="posi_center">No.</th>
				<th width="70" class="posi_center bdrd_bottom">メルマガID</th>
				<th width="200" class="bdrd_bottom">PC版：タイトル</th>
				<th class="bdrd_bottom">PC版：本文</th>
				<th width="60" class="posi_center bdrd_bottom bdrd_bottom bdrs_right">配信先</th>
			</tr>
			<tr>
				<th class="posi_center">配信日</th>
				<th>モバイル版：タイトル</th>
				<th>モバイル版：本文</th>
				<th class="posi_center bdrs_right">詳細</th>
			</tr>
			<gt:typeList name="deliveryKbnList" typeCd="<%=MTypeConstants.DeliveryKbn.TYPE_CD %>" />
			<c:forEach var="dto" varStatus="status" items="${list}">
					<% //テーブルの背景色を変更するためのCSSをセット %>
					<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
					<tr>
						<td class="posi_center ${classStr}" rowspan="2"><fmt:formatNumber value="${status.index + 1}" /></td>
						<td width="70" class="posi_center bdrd_bottom ${classStr}">${dto.id}</td>
						<td width="200" class="bdrd_bottom ${classStr}">
							${gf:replaceStr(dto.pcMailMagazineTitle, common['gc.mailMag.mailMagazineTitle.length'], common['gc.replaceStr'])}&nbsp;
						</td>
						<td class="bdrd_bottom ${classStr}">
							${gf:replaceStr(f:h(dto.pcBody), common['gc.mailMag.body.length'], common['gc.replaceStr'])}&nbsp;
						</td>
						<td class="posi_center bdrd_bottom bdrs_right ${classStr}">${f:label(dto.deliveryKbn, deliveryKbnList, 'value', 'label')}</td>
					</tr>
					<tr>
						<td class="posi_center ${classStr}">
							<c:set var="DELIVERYFLG_NON" value="<%= MTypeConstants.DeliveryFlg.NON %>" />
							<c:choose>
								<c:when test="${DELIVERYFLG_NON eq dto.deliveryFlg}">
									<% // 未配信の場合は文言を表示 %>
									<gt:typeList name="deliveryFlgList" typeCd="<%=MTypeConstants.DeliveryFlg.TYPE_CD %>" />
									${f:label(dto.deliveryFlg, deliveryFlgList, 'value', 'label')}
								</c:when>
								<c:otherwise>
									<fmt:formatDate value="${f:date(dto.deliveryDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd" />&nbsp;
								</c:otherwise>
							</c:choose>
						</td>
						<td width="200" class="${classStr}">
							${gf:replaceStr(dto.mbMailMagazineTitle, common['gc.mailMag.mailMagazineTitle.length'], common['gc.replaceStr'])}&nbsp;
						</td>
						<td class="${classStr}">
							${gf:replaceStr(f:h(dto.mbBody), common['gc.mailMag.body.length'], common['gc.replaceStr'])}&nbsp;
						</td>
						<td class="posi_center bdrs_right ${classStr}"><a href="${f:url(gf:makePathConcat1Arg(detailPath, dto.id))}">詳細</a></td>
					</tr>
				</c:forEach>
		</table>
		<hr />

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
				<td><!--
					<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
						<c:choose>
							<c:when test="${dto.linkFlg eq true}">
								<% // gt:PageNaviのpathはc:setで生成する。 %>
								<c:set var="pageLinkPath" scope="page" value="${changePagePath}${dto.pageNum}" />
								--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
							</c:when>
							<c:otherwise>
								--><span>${dto.label}</span><!--
							</c:otherwise>
						</c:choose>
					</gt:PageNavi>
				--></td>
			</tr>
		</table>
		<!-- #page# -->
		<hr />

	</div>
	<!-- #wrap_result# -->
	<hr />
</c:if>

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

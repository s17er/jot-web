<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--

// 「DatePicker」の搭載
	$(function(){
		$("#postStartDate").datepicker();
		$("#postEndDate").datepicker();
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
	<div id="wrap_serch">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">特集検索</th>
				</tr>
				<tr>
					<th>エリア</th>
					<td colspan="3">
						<gt:areaList name="areaList" />
						<html:select property="areaCd">
							<html:optionsCollection name="areaList" />
						</html:select>
					</td>
				</tr>
				<tr>
					<th>掲載日</th>
					<td colspan="3">
						<html:text property="postStartDate" styleId="postStartDate"/> ～ <html:text property="postEndDate" styleId="postEndDate"/>
					</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<%-- IEでエンターキーによるsubmitを有効にするためのダミーのテキストエリア --%>
				<input type="text" name="dummy" style="display:none;">
				<html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table btm_margin list_table">
		<tr>
			<th width="20" class="posi_center" rowspan="2">No.</th>
			<th width="370" class="bdrd_bottom">特集名 (WEBデータ登録時の名称)</th>
			<th class="bdrd_bottom">掲載開始日時</th>
			<th class="bdrd_bottom">掲載終了日時</th>
			<th colspan="2" width="120" class="bdrd_bottom bdrs_right">アドレス</th>
		</tr>
		<tr>
			<th>表示名 (公開時の名称)</th>
			<th>画面表示エリア</th>
			<th>説明</th>
			<th width="60" class="posi_center">編集</th>
			<th width="60" class="posi_center bdrs_right">削除</th>
		</tr>

		<gt:areaList name="areaList" />
		<% //ループ処理 %>
		<c:forEach var="m" varStatus="status" items="${list}">
			<% //テーブルの背景色を変更するためのCSSをセット %>
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />

			<tr>
				<td rowspan="2" width="20" class="posi_center ${classStr}"><fmt:formatNumber value="${status.index + 1}" /></td>
				<td width="370" class="bdrd_bottom ${classStr}">${f:h(m.specialName)}</td>
				<td class="bdrd_bottom ${classStr}"><fmt:formatDate value="${f:date(m.postStartDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td class="bdrd_bottom ${classStr}"><fmt:formatDate value="${f:date(m.postEndDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" /></td>
				<td colspan="2" width="120" class="bdrd_bottom bdrs_right ${classStr}">
					<c:forEach items="${m.areaCd}" var="t" varStatus="status">
						/${areaLinkNameMap[t]}${f:h(m.url)}
						<c:if test="${!status.last}"><br></c:if>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td class="${classStr}">${f:h(m.displayName)}</td>
				<td class="${classStr}">
					<c:forEach items="${m.areaCd}" var="t" varStatus="status">
						${f:label(t, areaList, 'value', 'label')}
						<c:if test="${!status.last}">,&nbsp;</c:if>
					</c:forEach>&nbsp;
				</td>
				<td class="${classStr}">${gf:replaceStr(m.explanation, common['gc.special.explanation.length'], common['gc.replaceStr'])}&nbsp;</td>
				<td width="60" class="posi_center ${classStr}"><a href="${f:url(m.editPath)}">編集</a></td>
				<td width="60" class="posi_center bdrs_right ${classStr}"><a href="${f:url(m.deletePath)}">削除</a></td>
			</tr>

		</c:forEach>
		<% //ループ処理終了 %>
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

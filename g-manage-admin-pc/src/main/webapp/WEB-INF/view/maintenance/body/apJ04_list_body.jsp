<%@page pageEncoding="UTF-8"%>

<gt:authLevelList name="authLevelList"/>
<gt:companyList name="companyList"/>

<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>
<script type="text/javascript">
<!--
	//非表示要素の表示
	function showTbl(){
		$("#wrap_result").css("display","block");
	}

// -->
</script>

<script type="text/javascript">
	//<![CDATA[

	//ページ読み込み時にのみセットされる
	var changedFlg = false;

	/**
	 * 表示件数の変更後はフォーカスを外し、2度のサブミットをフラグで禁止する。
	 */
	function changeMaxRow() {

		if (!changedFlg) {

			changedFlg = true;

			window.focus();
			$("form#MaxRowSelect").submit();
		}
	}
	// ]]>
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

	<h2 class="title member">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div id="wrap_serch">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">営業担当者検索</th>
				</tr>
				<tr>
					<th>氏名</th>
					<td><html:text property="where_salesName" /></td>
					<th>氏名（カナ）</th>
					<td><html:text property="where_salesNameKana" /></td>
				</tr>
				<tr>
					<th>会社</th>
					<td><html:select property="where_companyId">
						<html:option value="" >--</html:option>
						<html:optionsCollection name="companyList" />
						</html:select>
					</td>
					<th>権限</th>
					<td><html:select property="where_authorityCd">
							<html:option value="" >--</html:option>
							<html:optionsCollection name="authLevelList" />
						</html:select>
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

<c:if test="${pageNavi ne null && pageNavi.allCount ne 0}">

	<!-- #wrap_result# -->


		<table cellpadding="0" cellspacing="0" border="0" class="number_table">
			<tr>
				<td>${pageNavi.allCount}件検索されました。</td>
				<td class="pull_down">

				<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
					表示件数:
				<html:select property="maxRow" onchange="changeMaxRow();">
					<html:option value="20">20件</html:option>
					<html:option value="">全件</html:option>
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
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/sales/list/changePage/${dto.pageNum}" />
				--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
			</c:if>
			<c:if test="${dto.linkFlg ne true}">
				--><span>${dto.label}</span><!--
			</c:if>
			</gt:PageNavi>
			--></td>

			</tr>
		</table>
		<!-- #page# -->

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
			<tr>
				<th width="15" class="posi_center">No.</th>
				<th width="65" class="posi_center">営業担当ID</th>
				<th>氏名</th>
				<th width="70" class="posi_center">権限</th>
				<th>会社</th>
				<th>メインアドレス</th>
				<th width="35" class="posi_center bdrs_right">詳細</th>
			</tr>

			<c:forEach var="dto" items="${salesList}" varStatus="status">

			<tr>
				<td class="posi_center"><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>
				<td class="posi_center">${f:h(dto.id)}&nbsp;</td>
				<td>${f:h(dto.salesName)}&nbsp;<br />
				${f:h(dto.salesNameKana)}&nbsp;</td>
				<td class="posi_center">${f:h(dto.authorityCdName)}&nbsp;</td>
				<td>${f:h(dto.companyName)}&nbsp;</td>
				<td>${f:h(dto.mainMail)}&nbsp;</td>
				<td class="posi_center bdrs_right"><a href="${f:url(dto.detailPath)}">詳細</a></td>
			</tr>
			</c:forEach>

		</table>
		<hr />

		<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/sales/list/changePage/${dto.pageNum}" />
				--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
			</c:if>
			<c:if test="${dto.linkFlg ne true}">
				--><span>${dto.label}</span><!--
			</c:if>
			</gt:PageNavi>

			--></td>
			</tr>
		</table>
		<!-- #page# -->


	</c:if>
	<!-- #wrap_result# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->

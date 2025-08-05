<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.entity.MMember"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.db.common.service.MemberService"%>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}" style="width: 300px;">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
	<h2 class="title member">合同説明会会員向けメルマガ登録確認</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div class="wrap_search">
		<table cellpadding="0" cellspacing="1" border="0" class="search_table btm_margin">
			<tr>
				<th class="td_title">メルマガ配信条件</th>
			</tr>
			<tr>
				<td>
					<logic:iterate id="data" collection="${conditionMap}">
						<span class="attention">${f:h(data.key)}:</span>
						${f:h(data.value)}&nbsp;
					</logic:iterate>
				&nbsp;</td>
			</tr>
		</table>
		<hr />
	</div>
	<!-- #wrap_serch# -->

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table btm_margin">
				<tr>
					<th width="150">タイトル</th>
					<td>${f:h(pcMailMagazineTitle)}</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">本文</th>
					<td class="bdrs_bottom">${f:br(f:h(pcBody))}</td>
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
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}" style="width: 300px;">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

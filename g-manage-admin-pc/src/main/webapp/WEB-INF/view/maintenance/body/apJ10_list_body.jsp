<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<gt:prefecturesList name="prefecturesList" blankLineLabel="--" />
<gt:typeList name="memberFlgList" typeCd="<%=MTypeConstants.MemberFlg.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
<gt:typeList name="terminalKbnList" typeCd="<%=MTypeConstants.TerminalKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}"/>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/table.color.js"></script>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div id="wrap_serch">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">いたずら応募条件検索</th>
				</tr>
				<tr>
					<th>氏名</th>
					<td>
						<html:text property="name" />
					</td>
					<th>氏名(カナ)</th>
					<td class="release">
						<html:text property="nameKana" />
			        </td>
				</tr>
				<tr>
					<th>都道府県</th>
					<td>
						<html:select property="prefecturesCd"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select>
					</td>
					<th>市区町村</th>
					<td>
						<html:text property="municipality" />
					</td>
				</tr>
				<tr>
					<th>その他住所</th>
					<td>
						<html:text property="address" />
					</td>
					<th>電話番号</th>
					<td>
						<html:text property="phoneNo1" style="width: 50px;"/>&nbsp;-&nbsp;<html:text property="phoneNo2" style="width: 50px;" />&nbsp;-&nbsp;<html:text property="phoneNo3" style="width: 50px;"/>
					</td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td>
						<html:text property="mailAddress" />
					</td>
					<th>会員/非会員</th>
					<td>
						<html:select property="memberFlg"  >
							<html:optionsCollection name="memberFlgList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>端末</th>
					<td>
						<html:select property="terminalKbn"  >
							<html:optionsCollection name="terminalKbnList"/>
						</html:select>
					</td>
					<th>自己PR</th>
					<td>
						<html:text property="applicationSelfPr" />
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
	<hr />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_result# -->
	<div id="wrap_result" >
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
			<tr>
				<th width="15" class="posi_center">ID</th>
				<th>氏名</th>
				<th>氏名(カナ)</th>
				<th>住所</th>
				<th>電話番号</th>
				<th>メールアドレス</th>
				<th>会員/非会員</th>
				<th>端末</th>

				<th width="45" class="posi_center bdrs_right">詳細</th>
			</tr>

		<% //ループ処理 %>
		<c:forEach items="${list}" var="m" varStatus="status">
			<tr>
				<td width="15" class="posi_center"><fmt:formatNumber value="${m.id}" /></td>
				<td>${f:h(m.name)}</td>
				<td>${f:h(m.nameKana)}</td>
				<td>
					<c:if test="${not empty m.prefecturesCd}">
						${f:label(m.prefecturesCd, prefecturesList, 'value', 'label')}&nbsp;
					</c:if>
					<c:if test="${not empty m.address}">
						${f:h(m.address)}
					</c:if>
				</td>
				<td>
					<c:if test="${not empty m.phoneNo1 and not empty m.phoneNo2 and not empty m.phoneNo3}">
						${f:h(m.phoneNo1)}-${f:h(m.phoneNo2)}-${f:h(m.phoneNo3)}
					</c:if>
				</td>
				<td>${f:h(m.mailAddress)}</td>
				<td>
					<c:if test="${not empty m.memberFlg}">
						${f:label(m.memberFlg, memberFlgList, 'value', 'label')}
					</c:if>
				</td>
				<td>
					<c:if test="${not empty m.terminalKbn}">
						${f:label(m.terminalKbn, terminalKbnList, 'value', 'label')}
					</c:if>
				</td>
				<td width="45" class="posi_center bdrs_right"><a href="${f:url(m.detailPath)}">詳細</a></td>
			</tr>
		</c:forEach>

		</table>
		<hr />
	</div>
	<!-- #wrap_result# -->
	<hr />
</c:if>
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

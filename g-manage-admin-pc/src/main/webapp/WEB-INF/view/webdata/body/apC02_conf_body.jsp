<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
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

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
		<tr>
			<th width="40" class="posi_center">顧客ID</th>
			<th>顧客名</th>
			<th>担当者名</th>
			<th>電話番号</th>
			<th>メールアドレス</th>
			<th class="bdrs_right">担当会社名：営業担当者名</th>
		</tr>
		<tr>
			<td class="posi_center">${f:h(customerDto.id)}&nbsp;</td>
			<td>${f:h(customerDto.customerName)}&nbsp;</td>
			<td>${f:h(customerDto.contactName)}&nbsp;</td>
			<td>${f:h(customerDto.phoneNo)}&nbsp;</td>
			<td>
				<span>${f:h(customerDto.mainMail)}&nbsp;</span>
				<span>
				<c:forEach items="${customerDto.subMailList}" var="subMail" varStatus="status">
					<c:if test="${status.first}"><br></c:if>${f:h(subMail)}<c:if test="${!status.last}"><br></c:if>
				</c:forEach>
				</span>
			</td>
			<td class="bdrs_right">${f:br(customerDto.companySalesName)}&nbsp;</td>
		</tr>
	</table>
	<hr />

	<h2 class="title date">${f:h(pageTitle2)}</h2>
	<hr />

	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
		<tr>
			<th width="60" class="posi_center">原稿番号</th>
			<th width="250">号数(掲載期間)</th>
			<th>原稿名</th>
			<th width="150">送信先メールアドレス</th>
			<th width="60" class="posi_center bdrs_right">応募テスト</th>
		</tr>
		<tr>
			<td class="posi_center">${f:h(id)}</td>
			<td width="250">
				<gt:volumeList name="volumeList" limitValue="${areaCd}" />
				${f:label(volumeId, volumeList, 'value', 'label')}&nbsp;
			</td>
			<td>${f:h(manuscriptName)}</td>
			<td width="150">
				${f:h(mail)}
				<c:forEach items="${subMailList}" var="subMail" varStatus="status">
					<c:if test="${status.first}"><br></c:if>${f:h(subMail)}<c:if test="${!status.last}"><br></c:if>
				</c:forEach>
			</td>
			<td class="posi_center bdrs_right">
				<gt:typeList name="applicationTestFlgList" typeCd="<%=MTypeConstants.ApplicationTestFlg.TYPE_CD %>" />
				${f:label(applicationTestFlg, applicationTestFlgList, 'value', 'label')}
			</td>
		</tr>
	</table>
	<hr />

	<p class="explain">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}" enctype="multipart/form-data">
			<html:hidden property="hiddenId" />
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">営業担当者</th>
					<td>${f:h(salesName)}</td>
				</tr>
				<tr>
					<th>連絡先アドレス</th>
					<td>
						${f:h(salesMail)}
						<c:if test="${not empty salesSubMail}"><br />${f:h(salesSubMail)}</c:if>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">コメント</th>
					<td class="bdrs_bottom">${f:br(f:h(comment))}&nbsp;</td>
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
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</c:if>

</div>
<!-- #main# -->

<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>
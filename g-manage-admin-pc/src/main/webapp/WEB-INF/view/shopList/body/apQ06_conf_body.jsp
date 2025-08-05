<%@page pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery-ui.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery-ui.js"></script>

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery.lightbox.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.core.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.lightbox.js"></script>

<div id="main">
	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		<c:if test="${not empty navigationPath3}">
		<li class="noDisplay">&nbsp;</li>
		<li><a href="${f:url(navigationPath3)}" class="reverseColor" style="color: #FFF;">${f:h(navigationText3)}</a></li>
		</c:if>
	</ul>

	<!-- #navigation# -->
	<gt:convertToCustomerName customerId="${customerId}" name="customName"/>
	<h2 title="${f:h(pageTitle1)}" class="title shop">${f:h(pageTitle1)}：${gf:replaceStr(customName, common['gc.shopList.customerName.trim.length'], common['gc.replaceStr'])}</h2>
	<hr />

	<s:form action="${f:h(actionPath)}" styleId="${targetForm}">
		<c:if test="${pageKbn ne PAGE_INPUT}">
			<html:hidden property="id"/>
		</c:if>
		 <c:if test="${not empty shopListDtoList}">
			<table class="table table-bordered cmn_table list_table shop_table">
				<tr>
				<th width="150" class="bdrs_right bdrs_bottom">
					ラベル名
				</th>
				<td class="bdrs_right bdrs_bottom">
					${f:h(labelName)}
				</td>
				</tr>
			</table>
			<br><br>
			<table class="table table-bordered cmn_table list_table shop_table">
			    <thead>
					<tr>
						<th>店舗名</th>
						<th>都道府県</th>
						<th>市区</th>
						<th width="140">業態1</th>
						<th width="140">業態2</th>
					</tr>
			    </thead>
			    <tbody>
			    	<c:forEach items="${shopListDtoList}" var="shopListDto">
						<tr>
							<td>${f:h(shopListDto.shopName)}</td>
							<td>${f:h(shopListDto.prefecturesName)}</td>
							<td>${f:h(shopListDto.cityName)}</td>
							<td width="140">${f:h(shopListDto.industryKbn1Label)}</td>
							<td width="140">${f:h(shopListDto.industryKbn2Label)}</td>
						</tr>
			    	</c:forEach>
			    </tbody>
			</table>
			<html:hidden property="customerId" value="${customerId}"/>
		</c:if>

		<div class="wrap_btn">
			<c:choose>
				<c:when test="${pageKbn eq PAGE_INPUT}">
					<html:submit property="submit" value="登録" />
					<html:submit property="correct" value="訂正" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_DETAIL}">
					<html:button property="edit" onclick="location.href='${f:url(gf:makePathConcat2Arg('/shopList/edit/index', customerId, id))}'" value="編集" />
					<html:hidden property="customerId"/>
					<html:hidden property="version"/>
					<html:submit property="delete" onclick="return confirm('削除してよろしいですか？')?true:false" value="削除" />
					<html:submit property="backToList"  value="戻る"/>
				</c:when>
				<c:when test="${pageKbn eq PAGE_EDIT}">
					<html:submit property="submit" value="更新" />
					<html:submit property="correct" value="訂正" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_OTHER}">
					<html:submit property="submitDetail" value="更新" />
					<html:submit property="correct" value="訂正" />
				</c:when>
			</c:choose>
		</div>
	</s:form>
</div>
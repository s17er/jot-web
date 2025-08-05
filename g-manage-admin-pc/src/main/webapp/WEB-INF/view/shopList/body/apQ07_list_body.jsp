<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey" %>

<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" scope="request"/>
<c:set var="LAT_LNG_KBN_LAT_LNG" value="<%=MTypeConstants.ShopListLatLngKbn.LAT_LNG %>" scope="page" />
<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="page" />
<c:set var="NOT_DISPLAY" value="<%= MTypeConstants.ShopListDisplayFlg.NOT_DISPLAY %>" scope="page"/>

<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<gt:areaList name="areaList" />

<script type='text/javascript' src='https://maps.google.com/maps/api/js?key=${gf:googleMapApiKey(GOOGLE_MAP_PROP_KEY)}&language=ja' charset='UTF-8'></script>
<script type="text/javascript">

$( document ).ready(function(){
	addLatLng();
});

	importNum = ${fn:length(csvMapList)};

	checkAddressNum = 0;

	failGeoIdArray = [];
	var importButtonClick = function() {
		$('#csvImport').click();
	};

	var uploadCsv = function() {
		$('#csvForm').submit();
	};

	//ページ読み込み時にのみセットされる
	var changedFlg = false;

	/**
	 * 表示件数の変更後はフォーカスを外し、2度のサブミットをフラグで禁止する。
	 */
	function changeMaxRow() {

		if (!changedFlg) {

			changedFlg = true;

			window.focus();
			$("#maxRowSelect").submit();
		}
	}
</script>

<div id="main">
	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<gt:convertToCustomerName customerId="${customerId}" name="customName"/>
	<h2 title="${f:h(pageTitle1)}" class="shopListTitle" id="${f:h(pageTitleId1)}">${gf:replaceStr(customName, common['gc.shopList.customerName.trim.length'], common['gc.replaceStr'])}</h2>
	<hr />
	<html:errors/>

	<c:if test="${existDataFlg}">
		<div id="wrap_result">
				<table cellpadding="0" cellspacing="0" border="0" class="number_table">
					<tr>
						<td>${csvMapList.size()}件インポートされました。</td>
					</tr>
				</table>
			<hr />

		<br>
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
				<tr>
					<th class="posi_center">店舗ID</th>
					<th class="posi_center">雇用形態</th>
					<th class="posi_center ">職種ID</th>
					<th class="posi_center">給与体系</th>
					<th class="posi_center ">給与下限1</th>
					<th class="posi_center  bdrs_right">給与上限1</th>
				</tr>
				<c:forEach items="${csvMapList}" var="csvMapList">
					<tr>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['shopListId'])}</td>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['employPtnKbnName'])}</td>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['jobKbnName'])}</td>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['saleryStructureKbnName'])}</td>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['lowerSalaryPrice'])}</td>
						<td class="posi_center  bdrs_right ${classStr}">${f:h(csvMapList['upperSalaryPrice'])}</td>
					</tr>
				</c:forEach>
			</table>
			<hr />
			<div class="wrap_btn">
				<c:if test="${errorFlg eq false}">
					<html:submit property="submit" value="一括登録" styleId="registerButton" style="display:none;" />
				</c:if>
				<c:if test="${errorFlg eq true}">
					<html:submit property="backToShopList" value="一覧に戻る" />
				</c:if>
			</div>
		</s:form>

		</div>
	</c:if>
	<c:if test="${existDataFlg eq false}">
		<div class="wrap_btn">
			<s:form action="${f:h(actionPath)}">
				<html:submit property="backToIndex" value="${f:h(LABEL_SHOPLIST)}トップへ戻る" />
			</s:form>
		</div>
	</c:if>
</div>
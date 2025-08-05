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

	/**
	 * 緯度経度の追加
	 */
	var addLatLng = function() {
		<c:forEach items="${csvMapList}" var="csvMapList">
			<c:choose>
				<c:when test="${csvMapList['latLngKbn'] eq LAT_LNG_KBN_LAT_LNG}">
					setLatLangValue("${f:h(csvMapList['id'])}",
							${f:h(csvMapList['latitude'])},
							${f:h(csvMapList['longitude'])});
					setGeoError(0);
				</c:when>
				<c:otherwise>
				var geocoder = new google.maps.Geocoder();
				geocoder.geocode({ 'address':  "${f:h(csvMapList['address'])}"}, function(results, status){
					  if (status == google.maps.GeocoderStatus.OK) {
						  var a = results[0].geometry.location;
						  setLatLangValue("${f:h(csvMapList['id'])}", a.lat(), a.lng());
						  setGeoError(0);
					  } else {
						  //setGeoError(${f:h(csvMapList['dispId'])});
						  setGeoError(0);
					  }
					});
				</c:otherwise>
			</c:choose>
	</c:forEach>
	};

	var setGeoError = function(id) {
		checkAddressNum++;
		if (id == 0) {
			<%-- 何もしない --%>
		} else {
			failGeoIdArray.push(id);
		}

		if (checkAddressNum == importNum) {
			<c:if test="${geoErrorFlg eq false}">
				if (failGeoIdArray != null && failGeoIdArray.length != 0) {
					var str = failGeoIdArray[0];
					for (var i = 1; i < failGeoIdArray.length; i++) {
						str += "," + failGeoIdArray[i];
					}
					$("#failGeoHidden").val(str);
					$("form#geoForm").submit();
				} else {
					<c:if test="${errorFlg eq false}">
						$("#registerButton").css("display", "inline");
						$("#registerWaitingButton").css("display", "none");
					</c:if>
				}
			</c:if>
		}
	};

	var setLatLangValue = function(id, latitude, longitude) {
		$("#latitude_" + id).val(latitude);
		$("#longitude_" + id).val(longitude);
	};

	function handleAll(selfName, targetName){

		if ($("#" + selfName).prop('checked')) {
			$("input[type='checkbox'][name='"+targetName+"']").prop('checked', true);
		} else {
			$("input[type='checkbox'][name='"+targetName+"']").prop('checked', false);
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
						<td>${pageNavi.allCount}件インポートされました。</td>
						<td class="pull_down">
							<s:form action="${actionMaxRowPath}" styleId="maxRowSelect">
								<gt:maxRowList name="maxRowList" value="${common['gc.csvShopList.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
									<html:select property="maxRow" onchange="changeMaxRow();">
										<html:optionsCollection name="maxRowList" />
									</html:select>
							</s:form>
						</td>
					</tr>
				</table>
			<hr />

			<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
			<c:if test="${dto.linkFlg eq true}">
				<%// vt:PageNaviのpathはc:setで生成する。 %>
				<c:set var="pageLinkPath" scope="page" value="/shopList/inputCsv/changePage/${dto.pageNum}" />
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
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
				<tr>
					<th class="posi_center"><input type="checkbox" id="allCheckFlg" onclick="handleAll('allCheckFlg', 'saveIdList' )" /></th>
					<th class="posi_center">ID</th>
					<th class="posi_center">店舗名</th>
					<th class="posi_center ">業態</th>
					<th class="posi_center">住所</th>
					<th class="posi_center ">求人募集</th>
					<th class="posi_center  bdrs_right">詳細</th>
				</tr>
				<c:forEach items="${csvMapList}" var="csvMapList">
					<c:choose>
						<c:when test="${NOT_DISPLAY eq f:h(csvMapList['displayFlg'])}"><tr style="background-color: #CCCCCC"></c:when>
						<c:otherwise><tr></c:otherwise>
					</c:choose>
						<td class="posi_center"><html:multibox property="saveIdList"  value="${f:h(csvMapList['id'])}" />
						<html:hidden name="csvMapList" property="latitude" indexed="true" styleId="latitude_${f:h(csvMapList['id'])}"/>
						<html:hidden name="csvMapList" property="longitude" indexed="true" styleId="longitude_${f:h(csvMapList['id'])}" /> </td>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['dispId'])}</td>
						<td class="${classStr}">${f:h(csvMapList['shopName'])}</td>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['industryName'])}</td>
						<td class="${classStr}">${f:h(csvMapList['address'])}&nbsp;${f:h(csvMapList['address2'])}</td>
						<td class="posi_center  ${classStr}">${f:h(csvMapList['jobOfferFlgName'])}</td>
						<td class="posi_center  bdrs_right ${classStr}"><a href="${f:url(csvMapList['detailPath'])}">詳細</a></td>
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
				<c:set var="pageLinkPath" scope="page" value="/shopList/inputCsv/changePage/${dto.pageNum}" />
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
			<div class="wrap_btn">
				<c:if test="${errorFlg eq false}">
					<html:button property="" value="位置情報取得中" styleId="registerWaitingButton" />
					<html:submit property="submit" value="一括登録" styleId="registerButton" style="display:none;" />
				</c:if>
				<html:submit property="delete" value="一括削除" onclick="return confirm('削除してよろしいですか？')? true : false;" />
			</div>
		</s:form>

		</div>
		<s:form action="/shopList/inputCsv/failGeoCodingList" styleId="geoForm">
			<html:hidden property="failGeoId" styleId="failGeoHidden"/>
		</s:form>
	</c:if>
	<c:if test="${existDataFlg eq false}">
		<div class="wrap_btn">
			<s:form action="${f:h(actionPath)}">
				<html:submit property="backToIndex" value="${f:h(LABEL_SHOPLIST)}トップへ戻る" />
			</s:form>
		</div>
	</c:if>
</div>
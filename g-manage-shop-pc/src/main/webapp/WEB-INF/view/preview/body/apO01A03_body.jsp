<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey" %>

<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="page" />

<script type='text/javascript' src='https://maps.google.com/maps/api/js?key=${gf:googleMapApiKey(GOOGLE_MAP_PROP_KEY)}&language=ja' charset='UTF-8'>
</script>
    <script type="text/javascript">
    //<![CDATA[
	var gmap = null;
	<%--
	 初期処理
	 ページ読み込み完了後に必ず実行します。
	--%>
	$( document ).ready(function(){
		afterPageLoad();
 	});

	var afterPageLoad = function() {
		<c:choose>
			<c:when test="${latLngAddress}">
				displayGmap(${f:h(convertMapAddressToLatLng)}, true);
			</c:when>
			<c:otherwise>
				var geocoder = new google.maps.Geocoder();
				  geocoder.geocode({ 'address':  "${f:h(mapAddress)}"}, function(results, status){
					  if (status == google.maps.GeocoderStatus.OK) {
						  var a = results[0].geometry.location;
						  displayGmap(a.lat(), a.lng(), true);
					  } else {
						  displayGmap(35.67445019897103, 139.76892471313476, false);
					  }
				});
			</c:otherwise>
		</c:choose>
	}

	var displayGmap = function(latitude, longitude, geocodeStatus) {
		if (geocodeStatus === false) {
			alert("位置情報の取得に失敗しました。");
			return;
		}
		var latLng = new google.maps.LatLng(latitude, longitude);
		var marker = null;
		var mapZoom = ${(not empty common['gourmetcaree.googleMap.zoom']) ? common['gourmetcaree.googleMap.zoom'] : 15};
		var mapOptions = {
				zoom : mapZoom,
				center : latLng,
				mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		gmap = new google.maps.Map(document.getElementById("gourmetcareeMap"), mapOptions);

			marker = new google.maps.Marker({
				position : latLng,
				map : gmap
			});

			attachMessage(marker, "${f:h(manuscriptName)}");
	};

	function attachMessage(targetMarker, msg) {
	    google.maps.event.addListener(targetMarker, 'click', function(event) {
	      new google.maps.InfoWindow({
	        content: msg
	      }).open(targetMarker.getMap(), targetMarker);
	    });
	  }
    //]]>
    </script>


<html:errors />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<div id="main">

		<%-- 詳細ページ共通ヘッダを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/apO01_header.jsp" %>

		<hr />

		<!-- 地図 -->
		<c:choose>
		<c:when test="${not empty mapAddress}">
		<div id="map" >
			<p><strong>[&nbsp;${f:h(mapTitle)}&nbsp;]</strong></p>

			<div id="gourmetcareeMap" style="width:450px; height:350px; margin:0px auto; padding:0px;"></div>

			<c:if test="${(not empty addressTraffic) && (not empty phoneReceptionist)}">
			<table cellpadding="0" cellspacing="0" border="0" summary="地図" class="tbl_detail">
				<c:if test="${not empty addressTraffic}">
				<tr>
					<th>面接地住所/交通</th>
					<td>
						${f:br(f:h(addressTraffic))}&nbsp;
					</td>
				</tr>
				</c:if>
				<c:if test="${not empty phoneReceptionist}">
				<tr>
					<th>電話番号/受付時間</th>
					<td>${f:br(f:h(phoneReceptionist))}&nbsp;</td>
				</tr>
				</c:if>
			</table>
			</c:if>

			<div class="wrap_btn">
				<a href="#" onclick="window.print();"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_printMap.gif" alt="地図を印刷する" /></a>
			</div>
			<hr />
		</div>
		<hr />
		</c:when>
		<c:otherwise>
			<p>Mapは現在準備中です。</p>
		</c:otherwise>
		</c:choose>
		<!-- 地図 -->

	</div>
</c:if>

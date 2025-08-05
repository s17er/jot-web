<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey" %>

<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<c:set var="NOT_DISPLAY" value="<%= MTypeConstants.ShopListDisplayFlg.NOT_DISPLAY %>" scope="page"/>
<gt:areaList name="areaList" />
<gt:prefecturesList name="prefList" />
<gt:typeList name="foreignAreaList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>"/>

<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" scope="request"/>
<c:set var="LAT_LNG_KBN_LAT_LNG" value="<%=MTypeConstants.ShopListLatLngKbn.LAT_LNG %>" scope="page" />
<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="page" />

<c:set var="DOMESTIC"  value="<%=MTypeConstants.DomesticKbn.DOMESTIC %>" scope="page"/>
<c:set var="ORVERSEAS"  value="<%=MTypeConstants.DomesticKbn.overseas %>" scope="page"/>

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
		var geocoder = new google.maps.Geocoder();
		<c:forEach items="${csvMapList}" var="csvMapList">

			<c:choose>
				<c:when test="${csvMapList['latLngKbn'] eq LAT_LNG_KBN_LAT_LNG}">
					setLatLangValue("${f:h(csvMapList['id'])}",
							${f:h(csvMapList['latitude'])},
							${f:h(csvMapList['longitude'])});
					setGeoError(0);
				</c:when>
				<c:otherwise>
					geocoder.geocode({ 'address':  '${f:h(csvMapList['address1'])}'}, function(results, status){
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

				<div id="wrap_web-shoplist">
					<div class="page_back">
						<a onclick="location.href='${f:url('/shopList/list/reShowList')}'" id="btn_back">戻る</a>
					</div>
					<h2>店舗一覧｜管理</h2>
					<div class="menu_tab">
						<div class="menu_list">
							<ul>
								<li>
									<a href="${f:url('/webdata/list/')}">求人原稿</a>
								</li>
								<li class="tab_active">
									<a href="${f:url('/shopList/')}">店舗一覧</a>
								</li>
							</ul>
						</div>
					</div>

					<div id="wrap_masc_content">
						<div class="tab_area">
							<div class="tab_contents tab_active" id="detail_list_Information">
								<div class="detail_list_area">
									<html:errors/>
									<s:form action="${f:h(actionPath)}" styleId="searchForm">
										<div class="narrowing_area">
											<p class="mailcount">
											全 ${f:h(pageNavi.allCount)} 件中 / ${f:h(pageNavi.minDispNum)} 件 〜 ${f:h(pageNavi.maxDispNum)} 件
											</p>
										</div>
									</s:form>

									<c:if test="${existDataFlg}">
									<div id="wrap_result">
										<table cellpadding="0" cellspacing="0" border="0" class="number_table" style="display:none">
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
											<!-- #page# -->
											<s:form action="${f:h(actionPath)}" styleId="listForm">
												<table cellpadding="0" cellspacing="0" border="0" class="all_table">
													<tbody>
														<tr>
															<th class="select_all" width="10px">
																<input type="checkbox" id="allcheck"
																	onclick="handleAllBoth(this);" class="checkBoxAll">
																<label for="allcheck"></label>
															</th>
															<th width="50px">ID</th>
															<th width="75px">エリア</th>
															<th width="145px">店舗名</th>
															<th width="165px">業態</th>
															<th width="125px">住所</th>
															<th class="posi_center" width="100px">編集</th>
														</tr>

														<c:forEach items="${csvMapList}" var="csvMapList">
																<c:choose>
																	<c:when test="${NOT_DISPLAY eq f:h(csvMapList['displayFlg'])}"><tr style="background-color: #CCCCCC"></c:when>
																	<c:otherwise><tr></c:otherwise>
																</c:choose>
																<td class="table-checkbox">
																	<html:multibox property="saveIdList" value="${f:h(csvMapList['id'])}" styleClass="lumpSendCheck" styleId="${f:h(csvMapList['id'])}" /><label for="${f:h(csvMapList['id'])}" class ="lumpSendCheck"></label>
																</td>
																<td>${f:h(csvMapList['dispId'])}</td>
																<html:hidden name="csvMapList" property="latitude" indexed="true" styleId="latitude_${f:h(csvMapList['id'])}"/>
																<html:hidden name="csvMapList" property="longitude" indexed="true" styleId="longitude_${f:h(csvMapList['id'])}" />
																<td>${f:label(csvMapList['areaCd'], areaList, 'value', 'label')}</td>
																<td>${f:h(csvMapList['shopName'])}</td>
																<td>${f:h(csvMapList['industryName'])}</td>
																<td>

																	<c:if test="${csvMapList['domesticKbn'] == DOMESTIC}">
																		<c:if test="${not empty csvMapList['prefecturesCd']}">
																			<gt:cityList name="cityList" prefecturesCd="${csvMapList['prefecturesCd']}" />
																			${f:label(csvMapList['prefecturesCd'], prefList, 'value', 'label')}
																			${f:label(csvMapList['cityCd'], cityList, 'value', 'label')}
																		</c:if>
																		${f:h(csvMapList['address'])}
																	</c:if>

																	<c:if test="${csvMapList['domesticKbn'] == ORVERSEAS}">
																		${f:label(csvMapList['shutokenForeignAreaKbn'], foreignAreaList, 'value', 'label')}
																		${f:h(csvMapList['foreignAddress'])}
																	</c:if>

																</td>
																<td class="posi_center shopLineheight">
																	<a href="${f:url(csvMapList['detailPath'])}">編集</a>
																</td>
															</tr>
														</c:forEach>

													</tbody>
												</table>
												<!-- #page# -->
												<table cellpadding="0" cellspacing="0" border="0" class="page">
													<tbody>
														<tr>
															<td>
																<div class="page">
																	<p>
																	<!--
																	<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
																	<c:if test="${dto.linkFlg eq true}">
																		<%// vt:PageNaviのpathはc:setで生成する。 %>
																		<c:set var="pageLinkPath" scope="page" value="/shopList/list/changePage/${dto.pageNum}" />
																		--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
																	</c:if>
																	<c:if test="${dto.linkFlg ne true}">
																		--><span>${dto.label}</span><!--
																	</c:if>
																	</gt:PageNavi>
																	-->
																	</p>
																</div>
															</td>
														</tr>
													</tbody>
												</table>
												<!-- #page# -->
												<div class="wrap_btn shopcsv__confirm__btn">
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
										<div class="wrap_btn shopcsv__confirm__btn">
											<html:button property="" onclick="location.href='${f:url('/shopList/index')}';" value="${f:h(LABEL_SHOPLIST)}トップへ戻る" styleId="btn_conf"/>
										</div>
									</c:if>
								</div>
							</div>
						</div>
					</div>
				</div>


</div>
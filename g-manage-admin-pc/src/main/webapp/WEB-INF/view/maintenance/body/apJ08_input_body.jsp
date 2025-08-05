<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>

<% /* CSSファイルを設定 */ %>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery.crossSelect.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery.lightbox.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.css" media="screen" />
<gt:prefecturesList name="prefList" noDisplayValue="<%= Arrays.asList(MPrefectures.KAIGAI) %>" blankLineLabel="${common['gc.pullDown']}" />
<jsp:include page="/WEB-INF/view/common/station_js.jsp"></jsp:include>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.js"></script>

<c:set var="jsonCount" value="${fn:length(sendJson)+1}" />
<c:if test="${pageKbn eq PAGE_INPUT }">
<c:set var="jsonCount" value="0" />
</c:if>

<script type="text/javascript">

	// 登録用JSONの数
	var count = ${jsonCount};
	// 登録用JSON
	var sendJson = new Array();

	$(function(){

		var prefCd = ${not empty prefecturesCd ? prefecturesCd : '$("#prefecturesCd").val()'};
		if ($.isNumeric(prefCd)) {
			setOption("${f:url('/ajax/json/railroad')}", {prefecturesCd : prefCd}, "companyCd");
		}

		$("#addRouteBtn").click(function(){

			var stationCd = $("#stationCd").val();

			if(!checkStationCd(stationCd)){
				return;
			}

			var text = $("#companyCd :selected").text() + '\u00a0：\u00a0' + $("#lineCd :selected").text() + '\u00a0：\u00a0' + $("#stationCd :selected").text() + '\u00a0\u00a0\u00a0';
			var $td = $('<td class="bdrs_right">')
						.text(text)
						.append("<button type='button' name='del' class='del' value=" + count + ">削除</button>")
						.append("<input type='hidden' name='sendJson[" + count + "].companyCd' value='" + $("#companyCd").val() + "'>")
						.append("<input type='hidden' name='sendJson[" + count + "].lineCd' value='" + $("#lineCd").val() + "'>")
						.append("<input type='hidden' class='stationCdHidden' name='sendJson[" + count + "].stationCd' value='" + stationCd + "'>");

			$("table#terminal_route").append($("<tr>").append($td));
			count++;
		});

		function checkStationCd(stationCd) {
			if($.isEmptyObject(stationCd)) {
				return false;
			}
			var flg = true;
			$(".stationCdHidden").each(function() {
				if ($(this).val()-0 === stationCd-0) {
					flg = false;
					return;
				}
			});
			return flg;
		}

		// 行を削除する
		$(document).on("click", ".del", function () {
			$(this).parent().remove();
		});

		// 都道府県の切り替え
		$(document).on('change', '#prefecturesCd', function(){
			$('table#terminal_route').empty();
			var cd = $(this).val();
			resetOption(["companyCd", "lineCd", "stationCd"]).then(function(){
				setOption("${f:url('/ajax/json/railroad')}", {prefecturesCd : cd}, "companyCd");
			});
			resetStationSearch();
			if(cd != '') {
				$("#searchRegistration").attr('disabled', false);
			} else {
				$("#searchRegistration").attr('disabled', 'disabled');
			}
		});

		// 鉄道会社の切り替え
		$(document).on('change', '#companyCd', function(){
			var cd = $(this).val();
			resetOption(["lineCd", "stationCd"]).then(function(){
				setOption("${f:url('/ajax/json/route')}", {companyCd : cd, prefecturesCd : $('[name="prefecturesCd"]').val()}, "lineCd");
			});
		});

		// 路線の切り替え
		$(document).on('change', '#lineCd', function(){
			var cd = $(this).val();
			resetOption(["stationCd"]).then(function(){
				setOption("${f:url('/ajax/json/station')}", {lineCd : cd}, "stationCd");
			});
		});

		// 駅の切り替え
		$(document).on('change', '#stationCd', function(){
			if($.isEmptyObject($(this).val())) {
				return;
			}
			// ボタンを非活性
			$("#addRouteBtn").attr("disabled", false);
		});

		<!-- 駅の検索登録用 -->
		$("#stationSearch").click(function(){
			var searchWord = $("#stationSearchBox").val();
			var prefCd;
			if("${prefecturesCd}" != "") {
				prefCd = "${prefecturesCd}";
			} else {
				prefCd = $("#prefecturesCd").val();
			}

			$.ajax({
				url : "${f:url('/shopList/ajax/searchStation')}",
				type : "POST",
				data : {
					'searchWord':searchWord,
					'prefCd':prefCd
				},
				async   : true,
				dataType : "json",
			}).done(function(data) {
				$("#searchStationTableBody").empty();
				if(data.length - 0 > 0) {
					$("#wrap_station_search table").show();
					$("#reflection").show();
					$("#wrap_station_search .error").hide();

					for(var i in data) {
						var checkbox = '<td class="posi_center">'+
						'<input type="checkbox" data-stationCd=' + data[i]['stationCd']+
						' data-lineCd = ' + data[i]['lineCd'] +
						' data-companyCd = ' + data[i]['companyCd'] +
						' data-stationName = ' + data[i]['stationName'] +
						' data-lineName = ' + data[i]['lineName'] +
						' data-companyName = ' + data[i]['companyName'] +
						'></td>';
						var companyName = '<td>'+ data[i]['companyName'] +'</td>';
						var lineName = '<td>'+ data[i]['lineName'] +'</td>';
						var stationName = '<td>'+ data[i]['stationName'] +'</td>';

						$('#searchStationTableBody').append(
							'<tr>'+ checkbox + companyName + lineName + stationName + '</tr>'
						);
					}
				} else {
					$("#wrap_station_search .error").show();
					$("#reflection").hide();
					$("#wrap_station_search table").hide();
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.log("error");
			});
		});

		$(document).on('change', '#allCheck', function() {
			var isCheck = $(this).is(':checked');
			$('.shop_table tbody tr').each(function(i, tr){
				// ラベルでのフィルタを考慮し、表示されている行のみ設定を変更する
				if ($(tr).is(':visible')) {
				    $(tr).find($('input[type=checkbox]')).prop('checked', isCheck);
				}
			});
		});


		$(document).on('click', '#reflection', function() {
			var checkValAry = $('#searchStationTableBody input:checkbox:checked');
			checkValAry.each(function(i, val){

				if(!checkStationCd(val.getAttribute('data-stationCd'))){
					return;
				}

				var text = val.getAttribute('data-companyName') + '\u00a0：\u00a0' + val.getAttribute('data-lineName') + '\u00a0：\u00a0' + val.getAttribute('data-stationName') + '\u00a0\u00a0\u00a0';
				var $td = $('<td class="bdrs_right">')
							.text(text)
							.append("<button type='button' name='del' class='del' value=" + count + ">削除</button>")
							.append("<input type='hidden' name='sendJson[" + count + "].companyCd' value='" + val.getAttribute('data-companyCd') + "'>")
							.append("<input type='hidden' name='sendJson[" + count + "].lineCd' value='" + val.getAttribute('data-lineCd') + "'>")
							.append("<input type='hidden' class='stationCdHidden' name='sendJson[" + count + "].stationCd' value='" + val.getAttribute('data-stationCd') + "'>");

				$("table#terminal_route").append($("<tr>").append($td));
				count++;
			});
		});

		function resetStationSearch() {
			$("#stationSearchBox").val(null);
			$("#searchStationTableBody").empty();
			$("#reflection").hide();
			$("#wrap_station_search table").hide();
			$("#wrap_station_search .error").hide();
		}

		$(document).on('click', '#searchRegistration',function() {
			$("#wrap_station_search input[type=checkbox]").prop('checked',false);
		});
		<!-- /駅の検索登録用 -->


	});

	$("[data-fancybox]").fancybox({
		 arrows : false,
		 modal  : true,
	});

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

	<h2 title="${f:h(pageTitle1)}" class="title date">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<html:hidden property="hiddenId" />

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="140">タイトル&nbsp;</th>
					<td><input type="text" value="${terminalTitle}" id="terminalTitle" name="terminalTitle"></td>
				</tr>
				<tr>
					<th>都道府県&nbsp;</th>
					<td>
						<c:if test="${pageKbn eq PAGE_EDIT}">
							${f:label(prefecturesCd, prefList, 'value', 'label')}
						</c:if>
						<c:if test="${pageKbn eq PAGE_INPUT}">
							<html:select property="prefecturesCd" styleId="prefecturesCd">
								<html:optionsCollection name="prefList"/>
							</html:select>
						</c:if>
					</td>
				</tr>
				<tr>
					<th>路線・最寄駅</th>
					<td class="release ajaxWrap">
						<c:if test="${pageKbn eq PAGE_INPUT}">
							<div id="railroadAjax">
								<select name="companyCd" id="companyCd">
									<option value="">${common['gc.pullDown']}</option>
								</select>
							</div>
							<div id="routeAjax">
								<select name="lineCd" id="lineCd">
									<option value="">${common['gc.pullDown']}</option>
								</select>
							</div>
							<div id="stationAjax">
								<select name="stationCd" id="stationCd">
									<option value="">${common['gc.pullDown']}</option>
								</select>
							</div>
						</c:if>
						<c:if test="${pageKbn eq PAGE_EDIT}">
							<div id="railroadAjax">
								<gt:railroadList name="railroadList" limitValue="${areaCd}" blankLineLabel="${common['gc.pullDown']}" />
								<html:select property="companyCd" styleId="companyCd">
									<html:optionsCollection name="railroadList" />
								</html:select>
							</div>
							<div id="routeAjax">
								<gt:routeList name="routeList" limitValue="${companyCd}" blankLineLabel="${common['gc.pullDown']}" />
								<html:select property="lineCd" styleId="lineCd">
									<html:optionsCollection name="routeList" />
								</html:select>
							</div>
							<div id="stationAjax">
								<gt:stationList name="stationList" limitValue="${lineCd}" blankLineLabel="${common['gc.pullDown']}" />
								<html:select property="stationCd" styleId="stationCd">
									<html:optionsCollection name="stationList" />
								</html:select>
							</div>
						</c:if>
						&nbsp;<input type="button" value="追加" id="addRouteBtn" disabled="disabled" /><br /><br />
						<c:if test="${pageKbn eq PAGE_EDIT}">
							<input type="button" value="検索登録" id="searchRegistration"  data-fancybox="modal" data-src="#wrap_station_search">

						</c:if>
						<c:if test="${pageKbn eq PAGE_INPUT}">
							<input type="button" value="検索登録" id="searchRegistration"  disabled data-fancybox="modal" data-src="#wrap_station_search">
						</c:if>
					</td>

				</tr>
			</table>
			<br><br>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table" id="terminal_route">
				<c:forEach items="${sendJson}" var="dto" varStatus="status">
					<tr>
						<td class="bdrs_right">
							<gt:rRailroadList name="railroadList" limitValue="${prefecturesCd}" />
							<gt:rRouteList name="routeList" limitValue="${dto['companyCd']}" />
							<gt:rStationList name="stationList" limitValue="${dto['lineCd']}" />
							${f:label(dto['companyCd'], railroadList, 'value', 'label')}&nbsp;：&nbsp;
							${f:label(dto['lineCd'], routeList, 'value', 'label')}&nbsp;：&nbsp;
							${f:label(dto['stationCd'], stationList, 'value', 'label')}&nbsp;&nbsp;&nbsp;
							<html:hidden property="sendJson[${status.index}].companyCd" value="${dto['companyCd']}"/>
							<html:hidden property="sendJson[${status.index}].lineCd" value="${dto['lineCd']}"/>
							<html:hidden styleClass="stationCdHidden" property="sendJson[${status.index}].stationCd" value="${dto['stationCd']}"/>
							<button type='button' name='del' class='del' value="${status.index}">削除</button>
						</td>
					</tr>
				</c:forEach>
			</table>

			<hr>
			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
				</c:choose>
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

<!-- 駅の検索登録のモーダル -->
<div id="wrap_station_search" style="display: none; width:1000px; height:600px;" >
<h2 class="cmnSttl">駅の検索登録</h2>
<br>
	<div>
		<input type="text" id="stationSearchBox">　　　<input type="button" value="検索" id="stationSearch">
	</div>
	<br><br>

	<div class="error" style="display: none">
		<ul><li>該当するデータが見つかりませんでした。</li></ul>
	</div>

	<table class="table table-bordered cmn_table list_table shop_table" style="display: none" role="grid">
	    <thead>
			<tr>
				<th width="20"><input type="checkbox" id="allCheck"></th>
				<th>鉄道会社</th>
				<th>路線</th>
				<th>駅</th>
			</tr>
	    </thead>
	    <tbody id="searchStationTableBody" >
	    </tbody>
	</table>

	<hr />

	<div class="wrap_btn">
		<input type="button" name="conf" value="反 映"  id="reflection" onclick="parent.jQuery.fancybox.close();" style="display: none">
		<input type="button" name="back" value="戻 る"  id="back" onclick="parent.jQuery.fancybox.close();">
	</div>
</div>

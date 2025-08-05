<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<%
	String areaCd = String.valueOf(request.getAttribute("APPLICATION_AREA_CD"));
%>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/style.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/member.css" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/modal2.js"></script>
<script type="text/javascript">

	/** 指定したIDの操作ボタン押下時に対象のチェックボックスを全選択、全解除する */
	function handleAll(selfId, targetName){

		if ($("#" + selfId).attr('checked')) {
			$("input[@type='checkbox'][name='"+targetName+"']").attr('checked', true);
		} else {
			$("input[@type='checkbox'][name='"+targetName+"']").attr('checked', false);
		}
	}

	function handleAllBoth(elm) {
		var checked = elm.checked;

		$(".memberIdCheckBox").prop("checked", checked);
		$(".memberIdAllCheckBox").prop("checked", checked);
	}


	/** キープBOXに追加のAjax */
	function addKeepBox(memberId, targetId) {
		setAddKeepBoxAjax("${f:url('/member/list/addKeepBox')}", memberId, targetId, afterLoad);
	}

	/** キープBOXに追加のAjax */
	function deleteKeepBox(memberId, targetId) {
		setAddKeepBoxAjax("${f:url('/member/list/deleteKeepBox')}", memberId, targetId, afterLoadDeleted);
	}

	/** キープBOXに追加後の処理 */
	function afterLoad(str) {
		if (str == "errorAddKeepBox") {
			alert("キープBOXへの追加に失敗しました。");
		} else {
			$("#keepButton" + str).css("display", "none");
			$("#addKeepBox" + str).css("display", "none");
			$("#deleteKeepButton" + str).css("display", "block");
			$("#deleteKeepBox" + str).css("display", "block");
		}
	};

	/** キープBOXから削除後の処理 */
	function afterLoadDeleted(str) {
		if (str == "errorDeleteKeepBox") {
			alert("キープBOXからの削除に失敗しました。");
		} else {
			$("#keepButton" + str).css("display", "block");
			$("#addKeepBox" + str).css("display", "block");
			$("#deleteKeepButton" + str).css("display", "none");
			$("#deleteKeepBox" + str).css("display", "none");
		}
	};

	$(function(){
		$('.close').click(function(){
			var name = $(this).data('name');
			var checkedLabel = $('[name="' + name + '"]:checked').map(function(){
				return $(this).next('label').text();
			}).get();

			console.log($(this));

			$("#" + name + "_label").html(checkedLabel.join("&nbsp;／&nbsp;"));
		});

		// 検索ボタンの連打対策
		var searchFlg = true;
		$("#search_tag").click(function(){
			if(searchFlg) {
				searchFlg = false;
				$('#search_btn').click()
			}
		});
	});
</script>
<script>
		$(function(){
			$(".search_botan_in").click();
		});
</script>
<script>
    $(function(){
        $('.prefectures:checked').each(function(i, elm){
        	togglePref($(this));
        });

        $(document).on('change', '.prefectures' , function(){
        	togglePref($(this));
        });

     	// 件数変更
        $("#maxRowSelect").on("change", function() {
        	$("#search").trigger("click");
        });

        function togglePref($pref) {
            var prefCd = $pref.val();
            if ($pref.is(':checked')) {
                $('#pref_' + prefCd).show();
            } else {
                $('#pref_' + prefCd).hide();
                $('[data-pref="' + prefCd + '"]').prop('checked', false);
            }
            if($('.prefectures:checked').length > 0) {
                $('#notSelectPref').hide();
                $(".modal-next").show();
            } else {
                $('#notSelectPref').show();
                $(".modal-next").hide();
            }
        }
    });
</script>
<c:if test="${pageNavi.allCount == null}">
	<script>
		$(function(){
			$(".search_botan_in").click();
		});
	</script>
</c:if>
<c:if test="${pageNavi.allCount != null}">
	<script>
		$(function(){
			$("#conditions").show();
		});
	</script>
</c:if>
<script>
	function checkPref($prefValue) {
		var checkFlg = $("input[data-pref='" + $prefValue + "']:not(:checked)").length > 0;

		if(checkFlg) {
			$('[data-pref="' + $prefValue + '"]').prop('checked', true);
		}else {
			$('[data-pref="' + $prefValue + '"]').prop('checked', false);
		}

	}

	function showMunicipalities(){
		document.getElementById('modal-city').classList.remove('show-modal');
		document.getElementById('modal-municipalities').classList.add('show-modal');

		$("input[name=where_prefList]:checked").each(function(){
			$("#pref_" + $(this).val()).show();
		});
	}

	function resetModalClass(){
		document.getElementById('modal-municipalities').classList.remove('show-modal');
		document.getElementById('modal-city').classList.add('show-modal');
	}

    function setAreaLabel() {
        var ary = [];
        $('.cities:checked').each(function(i, elm){
            var prefName = $(elm).data('prefName');
            var cityName = $(elm).next('label').text();
            if (ary[prefName]) {
                var tmp = ary[prefName];
                tmp.push(cityName);
            } else {
                ary[prefName] = [cityName];
            }
        });
        var labels = [];
        for (key in ary) {
            var city = ary[key];
            labels.push(key + ' - ' + city.join('　'));
        }
        $('#where_areaList_label').html(labels.join('<br>'));
    }

</script>
<script type="text/javascript">
$(function(){
	setAreaLabel();
});
</script>
<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>" />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="${common['gc.pullDown']}" />
<gt:typeList name="dbForeignAreaList" typeCd="<%=MTypeConstants.ForeignAreaKbn.getTypeCd((String) request.getAttribute(\"APPLICATION_AREA_CD\")) %>" />
<gt:typeList name="salaryKbnList" typeCd="<%=MTypeConstants.SalaryKbn.TYPE_CD %>"  />
<gt:ageList name="ageList"  blankLineLabel="${common['gc.pullDown']}" />
<gt:areaList name="areaList" />
<gt:maxRowList name="maxRowList" value="20,50,100" suffix="件" />
<gt:prefecturesList name="prefecturesList"  />
<c:set var="SHUTOKEN_AREA_NEM" value="首都圏"/>
<c:set var="SENDAI_AREA_NEM" value="仙台"/>
<c:set var="FOREIGN_AREA_NEM" value="海外"/>
<c:set var="areaGroupMap" value="<%=MAreaConstants.AreaGroup.areaGroupMap %>" />
<c:set var="IU_RESORT_OTHER" value="<%=String.valueOf(MTypeConstants.ShutokenWebAreaKbn.IU_RESORT_OTHER) %>" />
<c:set var="NOT_DECIDE" value="<%=String.valueOf(MTypeConstants.ShutokenWebAreaKbn.NOT_DECIDE) %>" />

<!-- #main# -->
<div id="main">
<div id="wrap_member">
	<h2>スカウト･会員検索</h2>
	<p class="explanation">
		求職者を検索して「気になる」「スカウトメール」を送り、求職者へ御社の求人情報を知ってもらえます。<br>
		※スカウトメールは求人が掲載されている期間のみご利用できます。
	</p>
	<div class="menu_tab">
		<div class="menu_list"><ul>
			<li class="tab_active">
				<a href="${f:url('/member/list/')}">会員検索</a>
			</li>
			<li>
				<a href="${f:url('/member/keepBox/')}">キープBOX</a>
			</li>
			<li>
				<a href="${f:url('/scoutMail/list/')}">スカウトメール
					<c:if test="${unReadScoutMailFlg}">
					<span id="mail_notification"></span>
					</c:if>
				</a>
			</li>
		</ul></div>
	</div>

	<div id="wrap_scse_content">
		<div class="tab_area">
			<div class="tab_contents tab_active" id="member_list_search">
				<div class="member_list_area">
					<div class="scoutmail_count">
						<p class="count_txt">
							スカウトメール残数<br>
							<c:choose>
								<c:when test="${scoutMailRemainDto.isUseUnlimitScoutMailFlg }">
									無制限（有効期限：<fmt:formatDate value="${scoutMailRemainDto.useEndDatetime}" pattern="yyyy/MM/dd" />）
								</c:when>
								<c:otherwise>
									${f:h(scoutMailRemainDto.remainFreeScoutCount)}通<span>（お試し）</span>
									<c:if test="${scoutMailRemainDto.paidMailList != null }">
										<c:forEach items="${scoutMailRemainDto.paidMailList}" var="t" varStatus="status">
											+&nbsp;
											${f:h(t.scoutRemainCount)}通<span>（有効期限：<fmt:formatDate value="${t.useEndDatetime}" pattern="yyyy/MM/dd" />）</span>
										</c:forEach>
									</c:if>
								</c:otherwise>
							</c:choose>
						</p>
						<p class="scoutprice_txt">※スカウトメールの<a href="${f:url('/price')}">追加料金はこちら</a></p>
					</div>

					<s:form action="${f:h(actionScoutPath)}" styleId="searchForm">
						<table cellpadding="0" cellspacing="0" border="0" class="detail_table search_table" id="webdata_table"><tbody>
							<tr>
								<th class="td_title" colspan="2">会員検索</th>
							</tr>
							<tr>
								<th>希望雇用形態</th>
								<td>
									<p><input type="button" name="" class="button-link modal-open" data-target="modal01" value="選択"></p>
									<div id="modal01" class="modal">
										<div class="modal-bg modal-close">
										</div>
										<div class="modal-content">
											<ul>
												<c:forEach var="t" items="${employPtnList}">
													<li>
														<html:multibox property="where_empPtnKbn" value="${f:h(t.value)}" id="where_empPtnKbn${f:h(t.value)}" />
														<label for="where_empPtnKbn${f:h(t.value)}">${f:h(t.label)}</label>
													</li>
												</c:forEach>
											</ul>
											<a class="modal-close close" data-name="where_empPtnKbn">決定</a>
										</div>
									</div>
									<div id="where_empPtnKbn_label" class="label_display">
										<c:forEach items="${where_empPtnKbn}" var="empPtnKbn" varStatus="status">
											${f:label(empPtnKbn, employPtnList, 'value', 'label')}<c:if test="${!status.last}">&nbsp;/&nbsp;</c:if>
										</c:forEach>
									</div>
								</td>
							</tr>
							<tr>
								<th>希望業態</th>
								<td>
									<p><input type="button" name="" class="button-link modal-open" data-target="modal02" value="選択"></p>
									<div id="modal02" class="modal">
										<div class="modal-bg modal-close">
										</div>
										<div class="modal-content">
											<ul>
												<c:forEach var="t" items="${industryList}">
													<li>
														<html:multibox property="where_industryCd" value="${f:h(t.value)}" id="where_industryCd${f:h(t.value)}" />
														<label for="where_industryCd${f:h(t.value)}">${f:h(t.label)}</label>
													</li>
												</c:forEach>
											</ul>
											<a class="modal-close close" data-name="where_industryCd">決定</a>
										</div>
									</div>
									<div id="where_industryCd_label" class="label_display">
										<c:forEach items="${where_industryCd}" var="industryCd" varStatus="status">
											${f:label(industryCd, industryList, 'value', 'label')}<c:if test="${!status.last}">&nbsp;/&nbsp;</c:if>
										</c:forEach>
									</div>
								</td>
							</tr>
							<tr>
								<th>希望勤務地<br>(市区町村まで)</th>
								<td>
									<p><input type="button" name="" class="button-link modal-open" data-target="modal03" value="選択"></p>
									<div id="modal03" class="modal">
										<div class="modal-bg modal-close" onclick="resetModalClass()">
										</div>
										<div id="modal-city" class="modal-content show-modal">
											<table class="city">
												<tbody>
													<c:forEach var="area" items="${areaGroupMap}">
														<tr>
															<th class="sp_trigger">${f:h(area.key)}</th>
															<td class="sp_accordion">
																<ul class="clearfix">
																	<c:forEach var="pref" items="${area.value}">
																		<li>
																			<html:multibox property="where_prefList" value="${pref.key}" styleId="where_prefList${pref.key}" styleClass="prefectures" />
																			<label for="where_prefList${pref.key}">${f:h(pref.value)}</label>
																		</li>
																	</c:forEach>
																</ul>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
											<a class="modal-close modal-close2">閉じる</a>
											<c:choose>
												<c:when test="${where_areaList != null}">
													<a id="modal-open3" class="modal-next" onclick="showMunicipalities()">次へ</a>
												</c:when>
												<c:otherwise>
													<a id="modal-open3" class="modal-next" onclick="showMunicipalities()" style="display: none">次へ</a>
												</c:otherwise>
											</c:choose>

										</div>
										<script>
											var selectCityCd = [];
											<c:forEach items="${where_areaList}" var="selectArea">selectCityCd.push('${selectArea}');</c:forEach>
										</script>
										<div id="modal-municipalities" class="modal-content">
											<table class="municipalities">
												<tbody>
													<c:forEach items="${prefecturesList}" var="pref">
														<c:if test="${pref.value != 48}">
															<tr id="pref_${pref.value}" style="display: none">
																<th class="sp_trigger">${f:h(pref.label)}</th>
																<td class="sp_accordion">
																	<button type="button" class="muicipalities-checkbtn" onclick="checkPref(${pref.value})"><span class="material-icons">check</span>すべて選択</button>
																	<ul class="clearfix" id="city_${pref.value}"></ul>
																</td>
															</tr>
															<script>
																	$.ajax({
																		'url' : "${f:url('/ajax/json/city')}",
																		'data' : {prefecturesCd : ${pref.value}},
																		'type' : "GET",
																		"success" : function(result) {
																			if ($.isEmptyObject(result)) {
																				return;
																			}
																			for (var i in result) {
																				var checked = "";
																				if ($.inArray(result[i].cd + "", selectCityCd) !== -1) {
																					var checked = 'checked';
																				}
																				$("#city_${pref.value}").append('<li><input type="checkbox" name="where_areaList" '+ checked + ' value="' + result[i].cd + '" id="where_areaList' + result[i].cd + '" class="prefectures_${pref.value} cities" data-pref="${pref.value}" data-pref-name="${pref.label}" onclick="setAreaLabel()"><label for="where_areaList' + result[i].cd + '">' + result[i].name + '</label></li>');
																			}
																		}
																	});
															</script>
														</c:if>
													</c:forEach>
												</tbody>
											</table>
											<a class="goback-modal-city" onclick="resetModalClass()">戻る</a>
											<a id="modal-open4" class="modal-close city_btn" onclick="resetModalClass(), setAreaLabel()">決定</a>
										</div>
									</div>
									<div id="where_areaList_label" class="label_display area_label_display"></div>
								</td>
							</tr>
							<tr>
								<th>希望職種</th>
								<td>
									<p><input type="button" name="" class="button-link modal-open" data-target="modal04" value="選択"></p>
									<div id="modal04" class="modal">
										<div class="modal-bg modal-close">
										</div>
										<div class="modal-content">
											<ul>
												<c:forEach var="t" items="${jobList}">
													<li>
														<html:multibox property="where_job" value="${f:h(t.value)}" id="where_job${f:h(t.value)}" />
														<label for="where_job${f:h(t.value)}">${f:h(t.label)}</label>
													</li>
												</c:forEach>
											</ul>
											<a class="modal-close close" data-name="where_job">決定</a>
										</div>
									</div>
									<div id="where_job_label" class="label_display">
										<c:forEach items="${where_job}" var="jobKbn" varStatus="status">
											${f:label(jobKbn, jobList, 'value', 'label')}<c:if test="${!status.last}">&nbsp;/&nbsp;</c:if>
										</c:forEach>
									</div>
								</td>
							</tr>
							<tr>
								<th>年齢</th>
								<td>
									<div class="selectbox">
										<html:select property="where_lowerAge">
											<html:optionsCollection name="ageList"/>
										</html:select>
									</div>
									～
									<div class="selectbox">
										<html:select property="where_upperAge">
											<html:optionsCollection name="ageList"/>
										</html:select>
									</div>
								</td>
							</tr>
							<tr>
								<th>性別</th>
								<td class="release bdrs_bottom">
									<div class="selectbox">
										<html:select property="where_sexKbn">
											<html:optionsCollection name="sexKbnList"/>
										</html:select>
									</div>
								</td>
							</tr>
							<tr class="pc_none scout_detailsearch d-search_open"><th>さらに詳しく検索する</th></tr>
						</tbody></table>
						<div class="d-search">
						<table cellpadding="0" cellspacing="0" border="0" class="detail_table search_table" id="webdata_table"><tbody>
							<tr>
								<th>現住所<br>(都道府県まで)</th>
								<td>
									<p><input type="button" name="" class="button-link modal-open" data-target="modal05" value="選択"></p>
									<div id="modal05" class="modal">
										<div class="modal-bg modal-close">
										</div>
										<div class="modal-content">
											<table class="city">
												<tbody>
													<c:forEach var="area" items="${areaGroupMap}">
														<tr>
															<th class="sp_trigger">${f:h(area.key)}</th>
															<td class="sp_accordion">
																<ul class="clearfix">
																	<c:forEach var="pref" items="${area.value}">
																		<li><html:multibox property="where_addressList" value="${pref.key}" styleId="where_addressList${pref.key}" styleClass="prefectures" />
																		<label for="where_addressList${pref.key}">${f:h(pref.value)}</label></li>
																	</c:forEach>
																</ul>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
											<a class="modal-close modal-close2">閉じる</a>
											<a id="modal-open3" class="modal-close close" data-name="where_addressList">決定</a>
										</div>
									</div>
									<div id="where_addressList_label" class="label_display area_label_display">
										<c:forEach items="${where_addressList}" var="pref" varStatus="status">
											${f:label(pref, prefecturesList, 'value', 'label')}<c:if test="${!status.last}">&nbsp;/&nbsp;</c:if>
										</c:forEach>
									</div>
								</td>
							</tr>
							<tr>
								<th>取得資格</th>
								<td>
									<p><input type="button" name="" class="button-link modal-open" data-target="modal06" value="選択"></p>
									<div id="modal06" class="modal">
										<div class="modal-bg modal-close">
										</div>
										<div class="modal-content">
											<ul>
												<c:forEach var="t" items="${qualificationList}">
													<li>
														<html:multibox property="where_qualification" value="${f:h(t.value)}" id="where_qualification${f:h(t.value)}" />
														<label for="where_qualification${f:h(t.value)}">${f:h(t.label)}</label>
													</li>
												</c:forEach>
											</ul>
											<a class="modal-close close" data-name="where_qualification">決定</a>
										</div>
									</div>
									<div id="where_qualification_label" class="label_display">
										<c:forEach items="${where_qualification}" var="qualification" varStatus="status">
											${f:label(qualification, qualificationList, 'value', 'label')}<c:if test="${!status.last}">&nbsp;/&nbsp;</c:if>
										</c:forEach>
									</div>
								</td>
							</tr>
							<tr>
								<th>キーワード</th>
								<td class="release bdrs_bottom">
									<html:text property="where_keyword" placeholder="例）商品開発　新店舗立ち上げ"/>
								</td>
							</tr>
							<tr>
								<th>その他条件</th>
								<td class="release">
									<html:checkbox property="where_transferFlg" value="1" id="transference"/>
									<label for="transference">転勤可</label>
									<html:checkbox property="where_midnightShiftFlg" value="1" id="nightshift"/>
									<label for="nightshift">深夜勤務可</label><br>
									<html:checkbox property="where_scoutedFlg" value="1" id="scouted"/>
									<label for="scouted">スカウト済みを除外</label>
									<html:checkbox property="where_favoriteFlg" value="1" id="favorite"/>
									<label for="favorite">「気になる」済みを除外</label><br>
									<html:checkbox property="where_subscFlg" value="1" id="subsc"/>
									<label for="subsc">「応募あり」を除外</label>
									<html:checkbox property="where_refuseFlg" value="1" id="refuse"/>
									<label for="refuse">「辞退」を除外</label>
								</td>
							</tr>
						</tbody></table>
						</div>
						<div class="wrap_btn">
							<c:if test="${searchConditionSavedFlg != null}">
								<button type="button" class="btn_mem_save_search" onclick="$('#load_btn').click();">
									<span>保存した条件で検索</span>
								</button>
								<html:submit property="loadConditions" value="読み込み" style="display:none" styleId="load_btn" />
							</c:if>
							<button type="button" class="btn_mem_save" onclick="$('#save_btn').click();">
								<span>この条件で保存する</span>
							</button>
							<html:submit property="saveConditions" value="保存" style="display:none" styleId="save_btn" />
							<button type="button" name="search" class="btn_mem_search" id="search_tag" onclick="$('#search').click();">
								<span>この条件で検索する</span>
							</button>
							<html:submit property="search" value="検索" style="display:none" styleId="search" />
						</div>

					<html:errors/>
					<c:if test="${existDataFlg}">
					<div id="wrap_result">
							<table cellpadding="0" cellspacing="0" border="0" class="number_table"><tbody>
								<tr>
									<td><span class="count_big">${pageNavi.allCount}</span>名が検索されました。</td>
									<td class="pull_down">
										<div class="selectbox">
                                        <html:select property="maxRow" styleId="maxRowSelect">
                                            <html:optionsCollection name="maxRowList"/>
                                        </html:select>
										</div>
									</td>
								</tr>
							</tbody></table>
							<!-- #page# -->

							<table cellpadding="0" cellspacing="0" border="0" class="page">
								<tr>
									<td><!--
										<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
											<c:if test="${dto.linkFlg eq true}">
												<%// vt:PageNaviのpathはc:setで生成する。 %>
												<c:set var="pageLinkPath" scope="page" value="/member/list/changePage/${dto.pageNum}" />
												--><span><a href="${f:url(pageLinkPath)}" class="">${dto.label}</a></span><!--
											</c:if>
											<c:if test="${dto.linkFlg ne true}">
												--><span>${dto.label}</span><!--
											</c:if>
										</gt:PageNavi>
									--></td>
								</tr>
							</table>
							<!-- #page# -->
									<table cellpadding="0" cellspacing="0" border="0" class="all_table"><tbody>
										<tr>
											<th class="select_all" width="10px">
											<c:choose>
												<c:when test="${scoutUseFlg}">
													<input type="checkbox" id="allcheck" onclick="handleAllBoth(this);" class="checkBoxAll">
													<label for="allcheck"></label>
												</c:when>
												<c:otherwise>
													&nbsp;
												</c:otherwise>
											</c:choose>
											</th>
											<th width="50px">No</th>
											<th width="75px">ID</th>
											<th width="170px">状態</th>
											<th width="100px" class="posi_center">性別・年齢</th>
											<th width="">住所</th>
											<th width="120px">ログイン日</th>
											<th class="posi_center" width="100px">キープBOX</th>
											<th class="posi_center" width="80px">会員情報</th>
										</tr>
										<c:forEach var="dto" items="${memberDtoList}" varStatus="memberStatus">
											<tr>
												<td class="table-checkbox">
													<c:choose>
														<c:when test="${scoutUseFlg and dto.scoutMailOkFlg}">
															<html:multibox property="checkId" value="${dto.id}" styleId="${dto.id}" class="memberIdCheckBox"/>
															<label for="${dto.id}"></label>
														</c:when>
														<c:otherwise>
															&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
												<td><fmt:formatNumber value="${memberStatus.index + 1}" pattern="0" /></td>
												<td>${dto.id}</td>
												<td>
													<ul class="scout_userstats">
														<c:if test="${dto.scoutFlg eq 1}">
															<c:choose>
																<c:when test="${dto.scoutReceiveKbn eq 2}">
																	<li><img src="https://www.gourmetcaree.jp/shopContent/images/scout/icon_receive.gif" alt="スカウト後、返信あり" title="スカウト後、返信あり" width="20" height="20">&nbsp;<fmt:formatDate value="${dto.vMemberMailbox.sendDatetime}" pattern="yyyy/MM/dd" /></li>
																</c:when>
																<c:when test="${dto.scoutReceiveKbn eq 3}">
																	<li><img src="https://kanri.gourmetcaree.jp/help/shop-s/images/icon/icon_turndown.gif" alt="スカウト辞退" title="スカウト辞退" width="20" height="20">&nbsp;<fmt:formatDate value="${dto.vMemberMailbox.sendDatetime}" pattern="yyyy/MM/dd" /></li>
																</c:when>
																<c:otherwise>
																	<li><img src="https://kanri.gourmetcaree.jp/help/shop-s/images/icon/icon_scout.gif" alt="スカウト済み" title="スカウト済み" width="20" height="20">&nbsp;<fmt:formatDate value="${dto.vMemberMailbox.sendDatetime}" pattern="yyyy/MM/dd" /></li>
																</c:otherwise>
															</c:choose>
														</c:if>
														<c:if test="${dto.footprintFlg eq 1}"><li><img src="https://kanri.gourmetcaree.jp/help/shop-s/images/icon/icon_interest.gif" alt="気になる" title="気になる済み" width="20" height="20" />&nbsp;<fmt:formatDate value="${dto.tFootprintDate}" pattern="yyyy/MM/dd" /></li></c:if>
														<c:if test="${dto.applicationFlg eq 1}"><li><img src="https://www.gourmetcaree.jp/shopContent/images/scout/icon_entry.gif" alt="応募あり" title="応募あり" width="20" height="20" />&nbsp;<fmt:formatDate value="${dto.applicationMail.sendDatetime}" pattern="yyyy/MM/dd" /></li></c:if>
													</ul>
												</td>
												<td class="posi_center">
													<button name="" class="button-link modal-open" data-target="userWishData${memberStatus.index + 1}">
                                                        <c:if test="${dto.sexKbn eq '1'}"><div class="button-link-gender">男性</div></c:if>
	                                                    <c:if test="${dto.sexKbn eq '2'}"><div class="button-link-gender">女性</div></c:if>
														<c:if test="${dto.sexKbn eq '3'}"><div class="button-link-gender">回答なし</div></c:if>
                                                        <div class="button-link-age">(${f:h(dto.age)})</div>
                                                        <div class="button-link-detailbtn">条件表示</div>
                                                    </button>
													<div id="userWishData${memberStatus.index + 1}" class="modal">
														<div class="modal-bg modal-close">
														</div>
														<div class="modal-content">
															<dl>
																<dt>希望職種</dt>
                                                                <dd>
	                                                                <c:choose>
		                                                                <c:when test="${empty dto.job}">
		                                                                    データ未入力&nbsp;
		                                                                </c:when>
	                                                                	<c:otherwise>
		                                                                	<gt:convertToJobName items="${dto.job}"/>&nbsp;
		                                                                </c:otherwise>
	                                                            	</c:choose>
                                                                </dd>
																<dt>希望業種</dt>
                                                                <dd>
	                                                                <c:choose>
		                                                                <c:when test="${empty dto.industry}">
		                                                                    データ未入力&nbsp;
		                                                                </c:when>
		                                                                <c:otherwise>
		                                                                	<gt:convertToIndustryName items="${dto.industry}"/>&nbsp;
		                                                                </c:otherwise>
	                                                            	</c:choose>
                                                                </dd>
																<dt>希望年収</dt>
                                                                <dd>
	                                                                <c:choose>
		                                                                <c:when test="${empty dto.salaryKbn}">
		                                                                    データ未入力&nbsp;
		                                                                </c:when>
		                                                                <c:otherwise>
		                                                                	${f:label(dto.salaryKbn, salaryKbnList, 'value', 'label')}&nbsp;
		                                                                </c:otherwise>
	                                                            	</c:choose>
                                                                </dd>
																<dt>希望勤務地</dt>
                               									<c:choose>
	                                                                <c:when test="${empty dto.hopeCityMap}">
		                                                                <dd>
		                                                                    データ未入力&nbsp;
		                                                                </dd>
	                                                                </c:when>
	                                                                <c:otherwise>
		                                                                <dd class="pAddress">
																			<c:forEach var="city" items="${dto.hopeCityMap}" varStatus="prefStatus">
																				<div class="province">
																					${f:label(city.key, prefecturesList, 'value', 'label')}
																				</div>
																				<div class="cities">
																					<c:forEach items="${city.value}" var="cityName" varStatus="status">
																						${f:h(cityName)}
																						<c:if test="${!status.last}">,&nbsp;</c:if>
																					</c:forEach>
																				</div>
																			</c:forEach>
		                                                               	</dd>
	                                                                </c:otherwise>
                                                            	</c:choose>
															</dl>
															<div class="btn">
																<ul>
																	<c:choose>
																		<c:when test="${dto.considerationFlg eq 0}">
																			<li id="keepButton${memberStatus.index + 1}"><a href="#" onclick="addKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"><button type="button">キープする&nbsp;<span class="material-icons-outlined">person_add</span></button></a></li>
																			<li id="deleteKeepButton${memberStatus.index + 1}" hidden="hidden"><a href="#" onclick="deleteKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"><button type="button" class="leaveKeep" data-id="${dto.keepId}">キープから外す&nbsp;<span class="material-icons-outlined">person_remove</span></button></a></li>
																		</c:when>
																		<c:when test="${dto.considerationFlg eq 1}">
																			<li id="keepButton${memberStatus.index + 1}" hidden="hidden"><a href="#" onclick="addKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"><button type="button">キープする&nbsp;<span class="material-icons-outlined">person_add</span></button></a></li>
																			<li id="deleteKeepButton${memberStatus.index + 1}"><a href="#" onclick="deleteKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"><button type="button" class="leaveKeep" data-id="${dto.keepId}">キープから外す&nbsp;<span class="material-icons-outlined">person_remove</span></button></a></li>
																		</c:when>
																	</c:choose>
																	<li><a href="${f:url(dto.detailPath)}"><button type="button">詳細を見る&nbsp;<span class="material-icons-outlined">arrow_right</span></button></a></li>
																</ul>
															</div>
														</div>
													</div>
												</td>
												<td>${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}${f:h(gf:toMunicipality(dto.municipality))}</td>
												<td>${f:h(dto.lastLoginDatetime)}</td>
												<td class="posi_center">
													<c:choose>
														<c:when test="${dto.considerationFlg eq 0}">
															<a href="#" onclick="addKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"  id="addKeepBox${memberStatus.index + 1}">キープする</a>
															<a href="#" onclick="deleteKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"  id="deleteKeepBox${memberStatus.index + 1}" hidden="hidden">キープから外す</a>
														</c:when>
														<c:when test="${dto.considerationFlg eq 1}">
															<a href="#" onclick="addKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"  id="addKeepBox${memberStatus.index + 1}" hidden="hidden">キープする</a>
															<a href="#" onclick="deleteKeepBox(${dto.id},'${memberStatus.index + 1}'); return false;"  id="deleteKeepBox${memberStatus.index + 1}">キープから外す</a>
														</c:when>
													</c:choose>
												</td>
												<td class="posi_center shopLineheight">
													<a href="${f:url(dto.detailPath)}">詳細</a>
												</td>
											</tr>
										</c:forEach>
									</tbody></table>
									<div class="wrap_btn">
										<c:if test="${scoutUseFlg}">
											<html:submit property="lumpSend" value="一括送信" class="btn_mem_search"/>
										</c:if>
										<html:submit property="lumpAddKeepBox" value="一括キープ" class="btn_mem_search"/>
									</div>
									<!-- #page# -->

									<table cellpadding="0" cellspacing="0" border="0" class="page">
										<tr>
											<td><!--
												<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
													<c:if test="${dto.linkFlg eq true}">
														<%// vt:PageNaviのpathはc:setで生成する。 %>
														<c:set var="pageLinkPath" scope="page" value="/member/list/changePage/${dto.pageNum}" />
														--><span><a href="${f:url(pageLinkPath)}" class="">${dto.label}</a></span><!--
													</c:if>
													<c:if test="${dto.linkFlg ne true}">
														--><span>${dto.label}</span><!--
													</c:if>
												</gt:PageNavi>
											--></td>
										</tr>
									</table>
									<!-- #page# -->
						</div>
					</c:if>
					</s:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- #main# -->
<script>
// モーダルウィンドウを開く
$('.modal-open').on('click', function(){
  var target = $(this).data('target');
  var modal = document.getElementById(target);

  $('#content').addClass('fixed');
  $(modal).fadeIn();
  return false;
});

// モーダルウィンドウを閉じる
$('.modal-close').on('click', function(){
  $('#content').removeClass('fixed');
  $('.modal').fadeOut();
  return false;
});
</script>
<c:if test="${where_areaList != null}">
	<script>
		$(function(){
			setAreaLabel();
		});
	</script>
</c:if>
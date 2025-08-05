
<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<c:set var="LABEL_SHOP_LIGHTLIST" value="<%=LabelConstants.ShopList.SHOP_LIGHT_LIST %>" scope="page" />
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" />
<c:set var="SENDAI_AREA" value="<%= MAreaConstants.AreaCd.SENDAI_AREA %>" />
<c:set var="SHOP_LIST_NO_DISPLAY" value="<%=MTypeConstants.ShopListDisplayKbn.NASHI %>" scope="page" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/vtip.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/vtip-min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/checkConfFlg.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/preview.js"></script>
<script type="text/javascript">

	// エリアの保持
	var tempAreaCd = "";
	// 所属会社の保持
	var tempCompanyId = "";

	<c:if test="${anchor != 0}">
		window.location.hash = "${anchor}";
	</c:if>

	/**
	 * エラー処理
	 */
	function errorAjax(responseText, status) {
		alert('切り替えに失敗しました。再度処理を行ってください。');
	}

	/**
	 * WEBデータ/アクセスカウントの切り替え
	 */
	$(function() {
		$("#list_change li").click(function() {
			var num = $("#list_change li").index(this);
			$(".content_wrap").addClass('disnon');
			$(".content_wrap").eq(num).removeClass('disnon');
			$("#list_change li").removeClass('select');
			$(this).addClass('select')
		});
	});

	function outputCSV() {
		location.href="${f:url('/webdata/list/output')}";
	}

	/**
	 * 初期処理
	 */
	$(document).ready(function(){

		$.ajaxSetup({
			error : errorAjax
		});

		// 所属会社の保持
		tempCompanyId = $('select#companyId').val();

		var value = $('select#areaCd').val();
		// エリア保持に変数にセット
		tempAreaCd = value;


		// エリアが未選択の場合、関連項目を非活性
		if (value == "") {
			// エリア未選択処理
			selectArea('');
		} else {
			var defaultAreaCd = "${f:h(SHUTOKEN_AREA)}";

			// FireFoxのキャッシュ用
			if ($('select#volumeId').val() == "") {
				// 号数を未選択に。
				$('#volumeAjax').load('${f:url(volumeAjaxPath)}',{'limitValue': tempAreaCd},
					function(responseText, status, XMLHttpRequest) {
						if (status == 'error') { return false; }
					}
				);
			}
		}

		// 会社が未選択の場合、営業担当者を非活性
		if (tempCompanyId == "") {
			// 営業担当者を初期表示
			setAjaxParts('${f:url(salesAjaxPath)}', 'salesAjax', '');
		}
	});

	/** 指定したIDの操作ボタン押下時に対象のチェックボックスを全選択、全解除する */
	function handleAll(selfId, targetName){

		if ($("#" + selfId).attr('checked')) {
			$("input[@type='checkbox'][name='"+targetName+"']").attr('checked', true);
		} else {
			$("input[@type='checkbox'][name='"+targetName+"']").attr('checked', false);
		}
	}

	/** WEBデータ一覧・PV応募データのwebIdチェックボックスを全選択・解除する */

	function handleAllBoth(elm) {
		var checked = elm.checked;

		$(".webIdCheckBox").prop("checked", checked);
		$(".webIdAllCheckBox").prop("checked", checked);
	}

	/**
	 * エリア選択時の処理
	 * @param value エリアコード
	 */
	function selectArea(value) {

		// 号数の表示
		$('#volumeAjax').load('${f:url(volumeAjaxPath)}',{'limitValue': value},
			// 勤務地エリアの連動
			function(responseText, status, XMLHttpRequest) {
				if (status == 'error') { return false; }
			}
		);
		// 特集の初期表示
		$('#special').load('${f:url(specialAjaxPath)}',{'limitValue': value});

		$.ajax({
			'url' : "${f:url('/ajax/json/detailAreaList')}",
			'data' : "areaCd=" + value,
			'type' : "POST",
			"success" : function(result) {
			    var json = JSON.parse(result);
                var ul = $("#detailAreaUl");
                ul.html("");
                for (var i in json) {
                    var dto = json[i];
                    var id = "area_s_c" + dto.value;
                    ul.append('<li>' +
						'<input type="checkbox" name="detailAreaList" value="' + dto.value + '" id="'+ id + '" />' +
                    	'<label for="' + id + '">&nbsp;' + dto.label + '</label>' +
						'</li>');
                }

                var addClass;
                var removeClass;
                if (value == '${SENDAI_AREA}') {
                    addClass = "autoDispWrap1";
                    removeClass = "autoDispWrap3";
                } else {
                    addClass = "autoDispWrap3";
                    removeClass = "autoDispWrap1";
                }

                var div = $("#detailAreaDiv");
                div.removeClass(removeClass);
                div.addClass(addClass);
            }
		});

		return true;
	}

	/**
	 * エリア選択時の連動を行う
	 */
	function areaLimitLoad() {

		 window.focus();

		 var value = $("#areaCd").val();

		// 値の取得に失敗した場合は処理しない
		if ($("#areaCd").val() == undefined) {
			return false;
		}

		 // 同じ場合は処理しない
		if (tempAreaCd == value) {
			return;
		}

		// エリアコードを保持
		tempAreaCd = value;

		// エリア選択処理
		selectArea(value);
	 }

	 /**
	 * 所属会社選択時の連動を行う
	 */
	function assignedCompanyLimitLoad () {

		window.focus();

		var value = $("#companyId").val();

		// 同じ場合は処理しない
		if (tempCompanyId == value) {
			return;
		}

		// 会社が未選択の場合
		if (value == "") {
			// 営業担当者を初期表示
			setAjaxParts('${f:url(salesAjaxPath)}', 'salesAjax', '');

		// 会社が選択された場合
		} else {
			// 営業担当者を連動
			setAjaxParts('${f:url(salesAjaxPath)}', 'salesAjax', value);
		}
		tempCompanyId = value;
	}

	// 「DatePicker」の搭載
	$(function(){
		$("#start_ymd").datepicker();
		$("#end_ymd").datepicker();
	});


var onclickWebId = function(elm) {
	var checked = elm.checked;
	var webId = elm.value;

	$(".webIdCheck_" + webId).attr("checked", checked);
};

$(function() {
	$(".webIdMultibox").each(function(checkbox) {
		var webId = this.value;
		$(".webIdCheck_" + webId).attr("checked", this.checked);
	});

$(function() {
	var check = $("input:radio[name=movieFlg]:checked").val();
	$(".movieFlg").on('click', function(){
		if(check == $(this).val()) {
			$(this).attr("checked",false);
			check = null;
		}else{
			check = $(this).val();
		}
	});
});
});
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title date">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div class="wrap_search">
		<s:form action="${f:h(actionPath)}">

			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">検索</th>
				</tr>
				<tr>
					<th>エリア&nbsp;<span class="attention">※必須</span></th>
					<td>
						<gt:areaList name="areaList" authLevel="${userDto.authLevel}" />
						<html:select property="areaCd" styleId="areaCd" onchange="areaLimitLoad(); return false;">
							<html:optionsCollection name="areaList"/>
						</html:select>
					</td>
					<th>号数</th>
					<td>
						<div id="volumeAjax">
							<gt:volumeList name="volumeList" limitValue="${areaCd}" blankLineLabel="${common['gc.pullDown']}" />
							<html:select property="volumeId" styleId="volumeId">
								<html:optionsCollection name="volumeList" />
							</html:select>
						</div>
					</td>
				</tr>
				<tr>
					<th>掲載期間</th>
					<td class="release">
						<html:text property="postFromDate" size="12" maxlength="10" styleId="start_ymd" styleClass="disabled" />
							&nbsp;～&nbsp;
						<html:text property="postToDate" size="12" maxlength="10" styleId="end_ymd" styleClass="disabled" />
					</td>
					<th>チェック状態</th>
					<td class="release">
						<gt:typeList name="checkedStatusList" typeCd="<%=MTypeConstants.WebdataCheckedStatus.TYPE_CD %>" blankLineLabel="--" />
						<html:select property="checkedStatus">
							<html:optionsCollection name="checkedStatusList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>サイズ</th>
					<td>
						<gt:typeList name="sizeKbnList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="sizeKbn" styleId="sizeKbn">
							<html:optionsCollection name="sizeKbnList" />
						</html:select>&nbsp;
					</td>
					<th>ステータス</th>
					<td class="release" >
						<div>
							<gt:statusList name="displayStatusList" statusKbn="<%=String.valueOf(MTypeConstants.StatusKbn.DIPLAY_STATUS_VALUE) %>" />
							<c:forEach items="${displayStatusList}" var="t">
								<span>
									<html:multibox property="displayStatusList" value="${f:h(t.value)}" styleId="displayStatus_${f:h(t.value)}" />
									<label for="displayStatus_${f:h(t.value)}">&nbsp;${f:h(t.label)}</label>
								</span>
							</c:forEach>
						</div>
					</td>
				</tr>
				<tr>
                    <th>応募フォーム</th>
                    <td class="release">
                        <gt:typeList name="applicationFormList" typeCd="<%=MTypeConstants.ApplicationFormKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
                        <html:select property="applicationFormKbn" styleId="reasonableKbn">
                            <html:optionsCollection name="applicationFormList"/>
                        </html:select>
                    </td>
					<th>系列店舗業態</th>
					<td>
						<gt:typeList name="industryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="industryKbn" styleId="industryKbn">
							<html:optionsCollection name="industryKbnList" />
						</html:select>&nbsp;
					</td>
				</tr>
				<tr>
					<th>担当会社名</th>
					<td class="release ajaxWrap" >
						<div id="companyAjax">
							<c:choose>
								<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
									<gt:assignedCompanyList name="where_companyList" blankLineLabel="${common['gc.pullDown']}" />
									<html:select property="companyId" styleId="companyId" onchange="assignedCompanyLimitLoad(); return false;">
										<html:optionsCollection name="where_companyList" />
									</html:select>&nbsp;
								</c:when>
								<c:otherwise>
									<gt:assignedCompanyList name="where_companyList" authLevel="${f:h(userDto.authLevel)}" companyId="${companyId}" />
									${f:label(companyId, where_companyList, 'value', 'label')}&nbsp;
								</c:otherwise>
							</c:choose>
						</div>
					</td>
					<th>営業担当者名</th>
					<td class="release ajaxWrap" >
					<gt:assignedSalesList name="where_salesList" limitValue="${companyId}" blankLineLabel="${common['gc.pullDown']}" />
						<div id="salesAjax">
							<html:select property="salesId" styleId="salesId">
								<html:optionsCollection name="where_salesList"/>
							</html:select>&nbsp;
						</div>
					</td>
				</tr>
				<tr>
					<th>顧客名</th>
					<td><html:text property="customerName" /></td>
					<th>顧客ID</th>
					<td class="release"><html:text property="customerId" /></td>

				</tr>
				<tr>
					<th>原稿名</th>
					<td><html:text property="manuscriptName" /></td>
					<th>原稿番号</th>
					<td><html:text property="whereWebId" /></td>
				</tr>
				<tr>
					<th>連載</th>
					<td class="release">
						<html:text property="serialPublication" size="20"/>
					</td>
					<th>特集</th>
					<td class="release" id="special">
						<gt:specialList name="specialList" blankLineLabel="${common['gc.pullDown']}" limitValue="${areaCd}" />
						<html:select property="specialId" styleId="specialSelect" style="min-width:270px">
							<html:optionsCollection name="specialList" />
						</html:select>
					</td>
				</tr>
				<tr>
					<th>ピックアップ求人</th>
					<td class="release">
						<gt:typeList name="attentionShopFlgList" typeCd="<%=MTypeConstants.AttentionShopFlg.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="attentionShopFlg">
							<html:optionsCollection name="attentionShopFlgList" />
						</html:select>
					</td>
					<th>${f:h(LABEL_SHOPLIST)}</th>
					<td class="release">
						<gt:typeList name="shopListDisplayKbnList" typeCd="<%= MTypeConstants.ShopListDisplayKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}"/>
						<html:select property="shopListDisplayKbn">
							<html:optionsCollection name="shopListDisplayKbnList"/>
						</html:select>
					</td>
				</tr>
                <tr>
                    <th>IP電話番号</th>
                    <td class="release">
                        <html:text property="ipPhone" size="20"/>
                    </td>
                    <th>タグ</th>
                    <td class="release">
						<gt:webDataTagList name="webDataTagList" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="webDataTagId" styleId="webDataTagId" >
							<html:optionsCollection name="webDataTagList"/>
						</html:select>

                    </td>
				<tr>
					<th>PM動画</th>
					<td class="release">
						<label><html:radio property="movieFlg" value="0" class="movieFlg"/>なし</label>
						<label><html:radio property="movieFlg" value="1" class="movieFlg" style="margin-left:50px;"/>あり</label>
					</td>
					<th>キーワード</th>
					<td class="release">
						<html:text property="keyword" size="45"/>
					</td>
				</tr>
				<tr>
					<th>検索上位表示</th>
					<td class="release">
						<gt:typeList name="searchPreferentialFlgList" typeCd="<%=MTypeConstants.SearchPreferentialFlg.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
						<html:select property="searchPreferentialFlg">
							<html:optionsCollection name="searchPreferentialFlgList" />
						</html:select>
					</td>
					<th></th>
					<td></td>
				</tr>
                <tr>
                    <th style="width:90px !important;"><a name="detailAreaPosition" id="detailAreaPosition"></a>チェック項目検索</th>
                    <td class="release" colspan="3">
                        <div class="autoDispWrap3">
                            <label class="area_acc_btn" for="Panel1">職種<span class="ac_caution">クリックすると職種が表示されます。</span></label>
                            <gt:typeList name="jobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD%>" />
                            <input type="checkbox" id="Panel1" class="on-off on-box1" />
                            <ul>
                                <c:forEach items="${jobKbnList}" var="t">
                                    <c:set var="jobId" value="job_s_c${t.value}" />
                                    <li>
                                        <html:multibox property="jobKbnList" styleId="${f:h(jobId)}" value="${f:h(t.value)}" />
                                        <label for="${f:h(jobId)}">&nbsp;${f:h(t.label)}</label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="autoDispWrap3">
                            <label class="area_acc_btn" for="Panel2">雇用形態<span class="ac_caution">クリックすると雇用形態が表示されます。</span></label>
                            <input type="checkbox" id="Panel2" class="on-off on-box2" />
                            <ul>
                                <gt:typeList name="empList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD%>" />
                                <c:forEach items="${empList}" var="t">
                                    <c:set var="empId" value="employ_s_c${t.value}" />
                                    <li>
                                        <html:multibox property="employPtnKbnList" value="${f:h(t.value)}" styleId="${f:h(empId)}" />
                                        <label for="${f:h(empId)}">&nbsp;${f:h(t.label)}</label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>

                        <div class="autoDispWrap3">
                            <label class="area_acc_btn" for="Panel5">マーク<span class="ac_caution">クリックするとマークが表示されます。</span></label>
                            <input type="checkbox" id="Panel5" class="on-off on-box4" />
                            <ul>
                                <gt:typeList name="treatmentList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD%>" />
                                <c:forEach items="${treatmentList}" var="t" >
                                    <c:set var="treatId" value="treat_${t.value}" />
                                    <li>
                                        <html:multibox property="treatmentList" value="${f:h(t.value)}" styleId="${f:h(treatId)}" />
                                        <label for="${f:h(treatId)}">&nbsp;${f:h(t.label)}</label>
                                    </li>
                                </c:forEach>
                                <gt:typeList name="otherList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD%>" />
                                <c:forEach items="${otherList}" var="t" >
                                    <c:set var="otherId" value="other_${t.value}" />
                                    <li>
                                        <html:multibox property="otherConditionList" value="${f:h(t.value)}" styleId="${f:h(otherId)}" />
                                        <label for="${f:h(otherId)}">&nbsp;${f:h(t.label)}</label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </td>
                </tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>
		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

<c:if test="${existDataFlg}">

	<!-- #wrap_result# -->
	<div id="wrap_result">
		<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
			<table cellpadding="0" cellspacing="0" border="0" class="number_table">
				<tr>
					<td>${pageNavi.allCount}件検索されました。</td>
					<td class="pull_down">
							<gt:maxRowList name="maxRowList" value="${common['gc.webdata.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
							<html:select property="maxRow" onchange="changeMaxRow('MaxRowSelect');">
								<html:optionsCollection name="maxRowList" />
							</html:select>
					</td>
				</tr>
			</table>
		</s:form>
		<!-- #pullDown# -->
		<hr />


		<div id="area_btn" class="clear">
			<c:set var="PREVIEW" value="${listViewDto.previewUrl}" scope="page" />
			<input type="button" name="" value="WEB掲載確認" onclick="window.open('${f:h(PREVIEW)}', '', 'width=1280,height=600,scrollbars=yes'); return false;" />
			<hr />
			<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
				<ul id="list_change">
					<c:choose>
						<c:when test="${displayCountFlg}">
							<c:set var="webListClass" value="disnon" scope="page" />
							<c:set var="countListClass" value="" scope="page" />
							<li>Webデータ一覧</li>
							<li class="select">PV・応募データ</li>
						</c:when>
						<c:otherwise>
							<c:set var="webListClass" value="" scope="page" />
							<c:set var="countListClass" value="disnon" scope="page" />
							<li class="select">Webデータ一覧</li>
							<li>PV・応募データ</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</c:if>
		</div>
		<hr />

		<s:form action="${f:h(actionPath)}" enctype="multipart/form-data">
			<div id="webdata" class="content_wrap ${f:h(webListClass)}">
				<!-- #page# -->
				<table cellpadding="0" cellspacing="0" border="0" class="page">
					<tr>
						<td><!--
							<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
								<c:choose>
									<c:when test="${dto.linkFlg eq true}">
										<% // gt:PageNaviのpathはc:setで生成する。 %>
										<c:set var="pageLinkPath" scope="page" value="${changePagePath}${dto.pageNum}" />
											--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
									</c:when>
									<c:otherwise>
										--><span>${dto.label}</span><!--
									</c:otherwise>
								</c:choose>
							</gt:PageNavi>
						--></td>
					</tr>
				</table>
				<!-- #page# -->
				<hr />

				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table" id="web_list_table">
					<tr>
						<th width="25" class="posi_center bdrn_bottom">No.</th>
						<th width="50" class="posi_center bdrd_bottom">求人識別番号</th>
						<th width="65" class="posi_center bdrd_bottom">原稿番号</th>
						<th width="50" class="posi_center bdrd_bottom">サイズ</th>
						<th colspan="2" class="bdrd_bottom">原稿名</th>
						<th class="posi_center bdrd_bottom">${f:h(LABEL_SHOP_LIGHTLIST)}</th>
						<th width="70" class="posi_center bdrd_bottom bdrs_right">詳細</th>
					</tr>
					<tr>
						<th class="posi_center"><input type="checkbox" id="allcheck_pvdata" onclick="handleAllBoth(this);" class="webIdAllCheckBox"/></th>
						<th class="posi_center" >号数</th>
						<th class="posi_center" >掲載期間</th>
						<th width="60" class="posi_center">ステータス</th>
						<th>顧客</th>
						<th width="150">担当会社</th>
						<th width="100">営業担当</th>
						<th class="posi_center bdrs_right">連載</th>
					</tr>
					<gt:typeList name="sizeList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />
					<gt:typeList name="serialList" typeCd="<%= MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD %>"/>
					<gt:companyList name="companyList" />
					<gt:salesList name="salesList" />

					<% //ループ処理 %>
					<c:forEach var="m" varStatus="status" items="${list}">
						<% //テーブルの背景色を変更するためのCSSをセット %>
						<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
						<tr>
							<%// No. %>
							<td class="posi_center bdrn_bottom ${classStr}"><fmt:formatNumber value="${status.index + 1}" /></td>
							<%// 求人識別番号 %>
							<td class="posi_center bdrd_bottom ${classStr}">
							<span class="vtip">${f:h(m.webNo)}&nbsp;</span>
							</td>
							<%// 原稿番号 %>
							<td class="posi_center bdrd_bottom ${classStr}">${f:h(m.id)}</td>
							<%// サイズ %>
							<td class="posi_center bdrd_bottom ${classStr}">${f:label(m.sizeKbn, sizeList, 'value', 'label')}</td>
							<%// 原稿名 %>
							<td colspan="2" class="bdrd_bottom ${classStr}"><span class="fontBold">${f:h(m.manuscriptName)}</span></td>
							<%// 店舗一覧 %>
							<td class="posi_center bdrd_bottom ${classStr}">
							<c:if test="${m.shopListDisplayKbn ne SHOP_LIST_NO_DISPLAY and not empty m.customerId}">
								${f:h(m.shopListJobOfferCount)}/${m.shopListPublicationCount}/${f:h(m.shopListCount)}件
							</c:if>
							<c:if test="${m.shopListDisplayKbn eq SHOP_LIST_NO_DISPLAY}">
								${f:label(m.shopListDisplayKbn, shopListDisplayKbnList, 'value', 'label')}
								<c:if test="${not empty m.customerId}">
									&nbsp;<html:button property="" value="${f:h(LABEL_SHOPLIST)}" onclick="location.href='${f:url(gf:makePathConcat1Arg('/shopList/index/indexWebList', m.customerId))}';" />
								</c:if>
							</c:if>
							</td>
							<%// 詳細 %>
							<td width="70" class="posi_center bdrd_bottom bdrs_right ${classStr}"><a href="${f:url(m.detailPath)}" name="${f:h(m.id)}">詳細</a></td>
						</tr>
						<tr>
							<%// No. の下のチェックボックス %>
							<td class="posi_center ${classStr}"><html:multibox property="webId" value="${m.id}" styleClass="webIdMultibox webIdCheckBox webIdCheck_${m.id}" onclick="onclickWebId(this);"/></td>
							<%// 号数 %>
							<td class="posi_center ${classStr}">${ f:h(m.volume) }</td>
							<%// 掲載期間 %>
							<td class="posi_center ${classStr}">
								<c:if test="${not empty m.volume}">
									<fmt:formatDate value="${f:date(m.postStartDatetime, 'yyyy-MM-dd')}" pattern="yy/MM/dd" />～<fmt:formatDate value="${f:date(m.postEndDatetime, 'yyyy-MM-dd')}" pattern="yy/MM/dd" />
								</c:if>
							</td>
							<%// ステータス %>
							<td class="posi_center ${classStr}">
								${f:label(m.displayStatus, displayStatusList, 'value', 'label')}<br />
								${f:label(m.checkedStatus, checkedStatusList, 'value', 'label')}
							</td>
							<%//顧客名 %>
							<td class="${classStr}">${f:h(m.customerName)}&nbsp;</td>
							<%// 担当会社 %>
							<td width="150" class="${classStr}">${f:label(m.companyId, companyList, 'value', 'label')}&nbsp;</td>
							<%// 営業担当 %>
							<td width="100" class="${classStr}">${f:label(m.salesId, salesList, 'value', 'label')}&nbsp;</td>
							<%// 連載区分(連載) %>
							<td class="posi_center  bdrs_right ${classStr}">${f:br(f:h(m.serialPublication))}</td>
						</tr>
					</c:forEach>
				</table>
				<hr />
				<!-- #page# -->
				<table cellpadding="0" cellspacing="0" border="0" class="page">
					<tr>
						<td><!--
							<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
								<c:choose>
									<c:when test="${dto.linkFlg eq true}">
										<% // gt:PageNaviのpathはc:setで生成する。 %>
										<c:set var="pageLinkPath" scope="page" value="${changePagePath}${dto.pageNum}" />
										--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
									</c:when>
									<c:otherwise>
										--><span>${dto.label}</span><!--
									</c:otherwise>
								</c:choose>
							</gt:PageNavi>
						--></td>
					</tr>
				</table>
				<!-- #page# -->
				<hr />
			</div>

		<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
			<div id="entry" class="content_wrap ${f:h(countListClass)}">
				<!-- #page# -->
				<table cellpadding="0" cellspacing="0" border="0" class="page">
					<tr>
						<td><!--
							<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
								<c:choose>
									<c:when test="${dto.linkFlg eq true}">
										<% // gt:PageNaviのpathはc:setで生成する。 %>
										<c:set var="pageLinkPath" scope="page" value="${changeCountPagePath}${dto.pageNum}" />
										--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
									</c:when>
									<c:otherwise>
										--><span>${dto.label}</span><!--
									</c:otherwise>
								</c:choose>
							</gt:PageNavi>
						--></td>
					</tr>
				</table>
				<!-- #page# -->
				<hr />

				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table" id="count_list_table">
					<tr>
						<th width="25" class="posi_center bdrn_bottom">No.</th>
						<th width="55" class="posi_center bdrd_bottom">求人識別番号</th>
						<th width="60" class="posi_center bdrd_bottom">原稿番号</th>
						<th width="50" class="posi_center bdrd_bottom">サイズ</th>
						<th class="bdrd_bottom">原稿名</th>
						<th width="55" class="posi_center bdrd_bottom">PV/PC</th>
						<th width="55" class="posi_center bdrd_bottom">PV/スマホ</th>
						<th width="55" class="posi_center bdrd_bottom">PV/合計</th>
                        <th width="55" class="posi_center bdrd_bottom">WEB応募</th>
                        <th width="55" class="posi_center bdrd_bottom bdrs_right">プレ応募</th>
					</tr>
					<tr class="count_tr">
						<th class="posi_center"><input type="checkbox" id="allcheck" onclick="handleAllBoth(this);" class="webIdAllCheckBox"/></th>
						<th colspan="2" class="posi_center" >掲載期間</th>
						<th width="60" class="posi_center">ステータス</th>
						<th>顧客</th>
						<th colspan="4" class="posi_center">原稿確認</th>
						<th class="posi_center bdrs_right">電話応募</th>
					</tr>

					<gt:typeList name="sizeList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />
					<gt:typeList name="serialList" typeCd="<%= MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD %>"/>
					<gt:companyList name="companyList" />
					<gt:salesList name="salesList" />

					<% //ループ処理 %>
					<c:forEach var="m" varStatus="status" items="${list}">
						<% //テーブルの背景色を変更するためのCSSをセット %>
						<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
						<tr>
							<%// No. %>
							<td class="posi_center bdrn_bottom ${classStr}"><fmt:formatNumber value="${status.index + 1}" /></td>
							<%// 求人識別番号 %>
							<td class="posi_center bdrd_bottom ${classStr}">
							<span class="vtip">${f:h(m.webNo)}&nbsp;</span>
							</td>
							<%// 原稿番号 %>
							<td class="posi_center bdrd_bottom ${classStr}">${f:h(m.id)}</td>
							<%// サイズ %>
							<td class="posi_center bdrd_bottom ${classStr}">${f:label(m.sizeKbn, sizeList, 'value', 'label')}</td>
							<%// 原稿名 %>
							<td class="bdrd_bottom ${classStr}"><span class="fontBold">${f:h(m.manuscriptName)}</span></td>

							<%-- PV/PC --%>
							<td class="posi_center bdrd_bottom ${classStr}"><fmt:formatNumber value="${m.pvPcCount == null ? 0 : m.pvPcCount}" pattern="###,##0" /></td>
							<%-- PV/スマホ --%>
							<td class="posi_center bdrd_bottom ${classStr}"><fmt:formatNumber value="${(m.pvSmartPhoneCount == null ? 0 : m.pvSmartPhoneCount) + (m.pvMbCount == null ? 0 : m.pvMbCount)}" pattern="###,##0" /></td>
							<%-- PV/合計 --%>
							<td class="posi_center bdrd_bottom ${classStr}"><fmt:formatNumber value="${m.pvSumCount}" pattern="###,##0" /></td>
                                <%-- WEB応募 --%>
                            <td class="posi_center bdrd_bottom ${classStr}">
                                <fmt:formatNumber var="num" value="${m.applicationCount}" pattern="###,##0" />
								<c:set var="link" value="/application/list/clearSearch/?where_webId=${f:h(m.id)}&where_areaCd=${f:h(m.areaCd)}" />
                                <a href="${f:url(link)}" >${f:h(num)}</a>
                            </td>
							<td class="posi_center bdrs_right ${classStr}">
                                <fmt:formatNumber var="num" value="${m.preApplicationCount}" pattern="###,##0" />
								<c:set var="link" value="/preApplication/list/clearSearch/?where_webId=${f:h(m.id)}&where_areaCd=${f:h(m.areaCd)}" />
                                <a href="${f:url(link)}" >${f:h(num)}</a>
							</td>
						</tr>
						<tr>
							<%// No. の下のチェックボックス %>
							<td class="posi_center ${classStr}"><input type="checkbox" class="webIdCheckBox webIdCheck_${m.id}" value="${m.id}" onclick="onclickWebId(this);"></td>
							<%// 掲載期間 %>
							<td colspan="2" class="posi_center ${classStr}">
								<c:if test="${not empty m.volume}">
									<fmt:formatDate value="${f:date(m.postStartDatetime, 'yyyy-MM-dd')}" pattern="yyyy/MM/dd" />～<fmt:formatDate value="${f:date(m.postEndDatetime, 'yyyy-MM-dd')}" pattern="yyyy/MM/dd" />
								</c:if>
							</td>
							<%// ステータス %>
							<td class="posi_center ${classStr}">
								${f:label(m.displayStatus, displayStatusList, 'value', 'label')}<br />
								${f:label(m.checkedStatus, checkedStatusList, 'value', 'label')}
							</td>
							<%//顧客名 %>
							<td class="${classStr}">${f:h(m.customerName)}&nbsp;</td>
							<td colspan="4" class="posi_center ${classStr}">
								<a href="javascript void(0);" onclick="window.open('${f:h(m.previewUrl)}', '', 'width=1280,height=600'); return false;">PC原稿</a>
								<a href="javascript void(0);" onclick="window.open('${f:h(m.previewUrl)}', '', 'width=400,height=600'); return false;">スマホ原稿</a>
							</td>
                                <%-- 電話応募 --%>
                            <td class="posi_center bdrd_bottom bdrs_right ${classStr}">
                                <c:set var="appLink" value="/ipPhoneHistory/list/clearSearch/?webId=${f:h(m.id)}" />
                                <fmt:formatNumber var="num" value="${m.ipPhoneHistoryCount == null ? 0 : m.ipPhoneHistoryCount}" pattern="###,##0" />
                                <a href="${f:url(appLink)}">${f:h(num)}</a>
                            </td>
						</tr>
					</c:forEach>
				</table>
				<hr />

				<!-- #page# -->
				<table cellpadding="0" cellspacing="0" border="0" class="page">
					<tr>
						<td><!--
							<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
								<c:choose>
									<c:when test="${dto.linkFlg eq true}">
										<% // gt:PageNaviのpathはc:setで生成する。 %>
										<c:set var="pageLinkPath" scope="page" value="${changeCountPagePath}${dto.pageNum}" />
										--><span><a href="${f:url(pageLinkPath)}">${dto.label}</a></span><!--
									</c:when>
									<c:otherwise>
										--><span>${dto.label}</span><!--
									</c:otherwise>
								</c:choose>
							</gt:PageNavi>
						--></td>
					</tr>
				</table>
				<!-- #page# -->
				<hr />
			</div>

		</c:if>

			<div class="wrap_btn">
				<html:submit property="lumpCopy" value="一括コピー" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
					<html:submit property="lumpDecide" value="一括確定" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:if>
				<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
					<html:submit property="lumpDelete" value="一括削除" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:if>
					<html:button property="output" value="CSV作成" onclick="outputCSV();" />
			</div>
		</s:form>
	</div>
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />




    <c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_OTHER or userDto.authLevel eq AUTH_LEVEL_SALES}">
        <script type="text/javascript">
            var searchApplication = function(webId, areaCd) {
              var form = $("#appSearchForm");
              $("#appWebId").val(webId);
              $("#appAreaCd").val(areaCd);
              form.submit();
            };
        </script>
        <s:form action="/application/list/search/" method="POST" styleId="appSearchForm">
            <input type="hidden" name="where_webId" id="appWebId" />
            <input type="hidden" name="where_areaCd" id="appAreaCd" />
        </s:form>
    </c:if>
</c:if>

</div>
<!-- #main# -->
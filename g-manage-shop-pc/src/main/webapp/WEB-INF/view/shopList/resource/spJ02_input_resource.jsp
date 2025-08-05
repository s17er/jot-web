<%--
店舗データ登録・編集のCSS、JS
 --%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<c:set var="MATERIAL_LOGO" value="<%=MTypeConstants.ShopListMaterialKbn.LOGO%>" scope="request" />
<c:set var="MATERIAL_KBN_MAIN_1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1%>" scope="request" />
<c:set var="SALERY_STRUCTURE_KBN_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>" scope="page" />
<c:set var="SALERY_STRUCTURE_KBN_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>" scope="page" />

<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/jquery/validationEngine.jquery.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/js/fancybox/jquery.fancybox.css" media="screen" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/jquery/moment.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/fancybox/jquery.fancybox.js"></script>

<script type="text/javascript" >

// キャッシュを回避するための値
var cacheTmpKey = 0;

<%-- 文字カウント用JSON(最大値を格納しておく) --%>
var countJson = {shopInformation:300};

// 国内
var DOMESTIC = ${DOMESTIC} - 0;

// その他の雇用形態表示監視用変数 0:非表示 1:表示
let termsSelectToggleNum = 0;

// ラジオボタン　あり　なし　定数
const RADIO_YES = 'あり';
const RADIO_NO = 'なし';

// 雇用形態
const EMPLOY_SEISYAIN = 1;
const EMPLOY_KEIYAKU_SYAIN = 2;
const EMPLOY_ARUBAITO_PART = 3;
const EMPLOY_HAKEN = 4;
const EMPLOY_OTHER = 6;

window.onload = function () {

	/*=============
		新規店舗登録-最寄り駅検索ポップアップ
	==============*/
	const stationLi = document.querySelectorAll('#station-popup .wrap .pContent li');

}
function openSpopup(){
	document.getElementById('station-popup').classList.add('open');
}
function closeSpopup(){
	document.getElementById('station-popup').classList.remove('open');
}

$(function(){

	// 店舗登録画面　フォーム内でエンターを押したときに発動するボタンを指定
	$('#shopListForm input[type="text"]').on('keydown',function(event) {
	     if ( event.which == 13 ) {
	         $("#enterButton").trigger('click');
	         return false;
	     }
	 });

	changeCharCount("catchCopy");
	changeCharCount("shopInformation");
	changeCharCount("arbeitFreeComment");
	changeCharCount("industryText");
	changeCharCount("arbeitShopSingleWord");

	changeDomesticKbn();

	if($("#prefecturesCd").val() > 0) {
		$("#searchRegistration").attr('disabled', false);
	}

	// 初期表示で都道府県が選ばれているときは鉄道会社をセットしておく
	var prefCd = ${not empty prefecturesCd ? prefecturesCd : '$("#prefecturesCd").val()'};
	if ($.isNumeric(prefCd)) {
		setOption("${f:url('/ajax/json/railroad')}", {prefecturesCd : prefCd}, "companyCd");
	}

	 // 国内外の切り替え
	 $('[name="domesticKbn"]').change(function(){
		 changeDomesticKbn();
	 });

	// 都道府県の切り替え
	$(document).on('change', '#prefecturesCd', function(){
		$('[name="cityCd"]').empty();
		$('[name="cityCd"]').append(new Option("市区町村を選ぶ", ""));
		var cd = $(this).val();
		resetOption(["companyCd", "lineCd", "stationCd"]).then(function(){
			// 鉄道会社
			setOption("${f:url('/ajax/json/railroad')}", {prefecturesCd : cd}, "companyCd");
			// 市区町村
			setOption("${f:url('/ajax/json/city')}", {prefecturesCd : cd}, "cityCd");
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

	$("#openDateYear").on('change', function(){
		getLastMonth();
	});

	$("#openDateMonth").on('change', function(){
		getLastMonth();
	});


	 toggleBeitRequired();

	// ライト版掲載しない場合は必須をはずす
	$('[name=jobOfferFlg]').change(function(){
		toggleBeitRequired();
	});

	function toggleBeitRequired() {
		if($('#jobOfferFlg').text() == 'あり') {
			$('.beitRequired').show();
			$('.beitRequiredObj').addClass('validate[required]');
		} else {
			$('.beitRequired').hide();
			$('.beitRequiredObj').removeClass('validate[required]');
		}
	}

	//駅の検索登録モーダルのリセット
	function resetStationSearch() {
		$("#sbox").val(null);
		$(".pContent ul").empty();
		$(".pContent ul").hide();
		$("#submit").hide();
        $(".stationError").show();
	}

	// -----------------募集職種の条件変更-----------------

	// 職種配列のindex
	var conditionIndex = ${not empty displayConditionDtoList ? fn:length(displayConditionDtoList) : 0};

	// 選択肢の初期表示
	if($('input[name="terms__radio__judge"]:checked').val() == RADIO_YES){
		$('.term_select_area').show();
	}else {
		$('.term_select_area').hide();
	}

	// 編集画面・登録画面の初期表示
	if(conditionIndex > 0){
		$('input[name="employJobKbnList"]:checked').each(function(){
			var employJobValue = $(this).val();
			var employValue = $(this).data('employ-value');

			// 契約社員（employ-value:2,4,6）が選択されていたら表示する
			if(employValue == EMPLOY_KEIYAKU_SYAIN || employValue == EMPLOY_HAKEN || employValue == EMPLOY_OTHER){
				termsSelectToggleNum = 1;
				$('.terms__select__hidden').show();
				return;
			}
		});
	}

	// 選択肢の表示切り替え
	$(document).on('change', 'input[name="terms__radio__judge"]', function(){
		var radioValue = $(this).val();
		if(radioValue == RADIO_YES){
			$('.term_select_area').show();
		}else if(radioValue == RADIO_NO){
			// なしに変更したときにチェックボックス全解除・モーダル全削除
			$('input[name="employJobKbnList"]:checked').each(function(){
				var employJobValue = $(this).val();
				$('[data-employ-job-value-modal="' + employJobValue + '"]').remove();
				$('[data-employ-job-value-term-setting="' + employJobValue + '"]').remove();
				$(this).prop('checked', false);
			});
			// その他の雇用形態を非表示
			$('.terms__select__hidden').hide();
			termsSelectToggleNum = 0;
			// 職種配列のindexをリセット
			conditionIndex = 0;
			$('.term_select_area').hide();
		}
	});

	// その他の雇用形態を表示
	$(document).on('click', '#view__termsmore', function(){
		// 非表示のときに押されたら表示　表示のときに押されたら非表示
		if (termsSelectToggleNum == 0) {
			$('.terms__select__hidden').show();
			termsSelectToggleNum = 1;
		} else {
			$('.terms__select__hidden').hide();
			termsSelectToggleNum = 0;
		}
	});

	// 職種の募集要項設定ボタンの追加
	$(document).on('change', 'input[name="employJobKbnList"]', function(){
		// チェックしたときにボタン・モーダル要素の追加　はずしたときに削除
		var jobCheckbox = $(this);
		var employJobValue = jobCheckbox.val();
		if(jobCheckbox.prop('checked')){
			// 各値を取得
			var employValue = jobCheckbox.data('employ-value');
			var jobValue = jobCheckbox.data('job-value');
			var EmployName = jobCheckbox.data('employ-name');
			var JobName = jobCheckbox.data('job-name');

			// テンプレをクローン
			var template = $('#term__setting__template').clone(true);
			var termsButtonAndModalHtml = template.html();
			// モーダルのテンプレートをクローン
			var modal = $('#terms__setting__modal__template').clone(true);
			var modalHtml = modal.html();
			// 作成したHTMLの内容を置換
			termsButtonAndModalHtml = termsButtonAndModalHtml.replace(/@employJobValue/g, employJobValue);
			termsButtonAndModalHtml = termsButtonAndModalHtml.replace(/@employValue/g, employValue);
			termsButtonAndModalHtml = termsButtonAndModalHtml.replace(/@jobValue/g, jobValue);
			termsButtonAndModalHtml = termsButtonAndModalHtml.replace(/@EmployName/g, EmployName);
			termsButtonAndModalHtml = termsButtonAndModalHtml.replace(/@JobName/g, JobName);
			termsButtonAndModalHtml = termsButtonAndModalHtml.replace(/@index/g, conditionIndex);
			modalHtml = modalHtml.replace(/@employJobValue/g, employJobValue);
			modalHtml = modalHtml.replace(/@JobName/g, JobName);
			modalHtml = modalHtml.replace(/@index/g, conditionIndex);
			modalHtml = modalHtml.replace(/@employValue/g, employValue);
			modalHtml = modalHtml.replace(/@jobValue/g, jobValue);

			// リストに追加
			$("#term__setting__lists__area").append(termsButtonAndModalHtml);
			$("#all").append(modalHtml);

			// フォームの外にあるモーダルのInputの内容をフォーム内のInputにコピー
			$("#salaryStructureKbn" + employJobValue).val($('[data-salaryStructureKbn="' + employJobValue + '"]').val());
			$("#lowerSalaryPrice" + employJobValue).val($('[data-lowerSalaryPrice="' + employJobValue + '"]').val());
			$("#upperSalaryPrice" + employJobValue).val($('[data-upperSalaryPrice="' + employJobValue + '"]').val());
			$("#salaryDetail" + employJobValue).val($('[data-salaryDetail="' + employJobValue + '"]').val());
			$("#salary" + employJobValue).val($('[data-salary="' + employJobValue + '"]').val());

			$("#annualLowerSalaryPrice" + employJobValue).val($('[data-annualLowerSalaryPrice="' + employJobValue + '"]').val());
			$("#annualUpperSalaryPrice" + employJobValue).val($('[data-annualUpperSalaryPrice="' + employJobValue + '"]').val());
			$("#annualSalaryDetail" + employJobValue).val($('[data-annualSalaryDetail="' + employJobValue + '"]').val());
			$("#annualSalary" + employJobValue).val($('[data-annualSalary="' + employJobValue + '"]').val());

			$("#monthlyLowerSalaryPrice" + employJobValue).val($('[data-monthlyLowerSalaryPrice="' + employJobValue + '"]').val());
			$("#monthlyUpperSalaryPrice" + employJobValue).val($('[data-monthlyUpperSalaryPrice="' + employJobValue + '"]').val());
			$("#monthlySalaryDetail" + employJobValue).val($('[data-monthlySalaryDetail="' + employJobValue + '"]').val());
			$("#monthlySalary" + employJobValue).val($('[data-monthlySalary="' + employJobValue + '"]').val());

			conditionIndex++;
		}else{
			// 追加したボタンとモーダルを削除
			removeCondition(employJobValue);
		}
	});

	// モーダルのInputの内容をフォーム内のInputにコピーする
	$(document).on('change', '.copyOriginInput', function(){
		var thisElement = $(this);
		var employJobValue = thisElement.data('employ-job-value');
		var dataName = thisElement.data('name');
		var value = thisElement.val();
		// 入力値をフォーム内のHiddenにコピー
		$("#" + dataName + employJobValue).val(value);
	});

	// 削除ボタン押下時の処理
	$(document).on('click', '.term__edit__deletebtn', function(){
		var employJobValue = $(this).data('employ-job-value');
		// 追加したボタンとモーダルを削除
		removeCondition(employJobValue);

		// チェックボックスのチェックを外す
		$('input[name="employJobKbnList"]' + 'input[value="' + employJobValue + '"]').prop('checked', false);
	});

	/*=============
	「募集職種の条件変更」のポップアップ（モーダル）
	==============*/
	// モーダル表示
	$(document).on('click', '.term__edit__editbtn', function(){
		// どのモーダルを表示するか判別するため、employJobValueを取得
		var employJobValue = $(this).data('employ-job-value');
		$('[data-employ-job-value-modal="' + employJobValue + '"]').addClass('showmodal');
	});
	// モーダル非表示
	$(document).on('click', '.terms__setting__modal__closebtn', function(){
		$('[data-employ-job-value-modal="' + $(this).data('employ-job-value') + '"]').removeClass('showmodal');
	});

	// ボタン削除
	function removeCondition(employJobValue){
		$('[data-employ-job-value-modal="' + employJobValue + '"]').remove();
		$('[data-employ-job-value-term-setting="' + employJobValue + '"]').remove();
		renumberConditionIndex();
	}

	// input nameの配列番号を振り直す
	function renumberConditionIndex() {
		var count = 0;
		var $wraps = $('#term__setting__lists__area .term__setting__list');
		$wraps.each(function(){
			$(this).find('input').each(function(){
				var name = $(this).attr('name');
				name = name.replace(/\d/, count);
				$(this).attr('name', name);
			});
			count++;
		});
		conditionIndex = count;
	}

	// -----------------募集職種の条件変更ここまで-----------------

});

// 駅関連
$(function(){

	// タイトルの切り替え
	toggleStationTitle();

	// 駅の並び替え
	$('#sortable').sortable({
		cursor: 'move',                     //移動中のカーソル
		opacity: 0.7,                       //移動中の項目の透明度
		placeholder: "ui-state-highlight",  //ドロップ先の色指定(Styleで指定可能)
		forcePlaceholderSize: true,         //trueでドラッグした要素のサイズを自動取得できる
		// 変更されたら配列のindexを付け替え
		update : function(e, ui) {
			sortIndex();
		}
	});

	$( '#sortable' ).disableSelection();

	// 配列のindex
	var index = ${not empty stationDtoList ? fn:length(stationDtoList) : 0};


	$('#addRouteBtn').click(function(){

		var stationCd = $("#stationCd").val();
		if(!checkStationCd(stationCd)){
			return;
		}

		var textParams = {
			'companyCd'		: $('[name="companyCd"]').val(),
			'companyName'	: $('[name="companyCd"] option:selected').text(),
			'lineCd' 		: $('[name="lineCd"]').val(),
			'lineName'		: $('[name="lineCd"] option:selected').text(),
			'stationCd'		: $('[name="stationCd"]').val(),
			'stationName'	: $('[name="stationCd"] option:selected').text(),
			'timeRequiredMinute' : '',
		};

		createStationTemplate(textParams, []);
	});

	// 駅グループ追加
	$('#addGroupBtn').click(function(){
		if (!$.isNumeric($('#terminal').val())) {
			return;
		}

		console.info($('#terminal').val());
		$.ajax({
			'url' : "${f:url('/ajax/json/terminalStation')}",
			'data' : {terminalId : $('#terminal').val()},
			'type' : "POST",
			"success" : function(result) {
				if ($.isEmptyObject(result)) {
					return;
				}
				var options = [];
				for (var i in result) {
					if(!checkStationCd(result[i].stationCd)) {
						continue;
					}
					var textParams = {
							'companyCd'		: result[i].companyCd,
							'companyName'	: result[i].companyName,
							'lineCd' 		: result[i].lineCd,
							'lineName'		: result[i].lineName,
							'stationCd'		: result[i].stationCd,
							'stationName'	: result[i].stationName,
						};

					createStationTemplate(textParams, []);
				}
			}
		});
	});

	/**
	 * 駅の行を追加
	 */
	function createStationTemplate(textParams, selectParams) {
		// テンプレフォームをクローン
		var template = $('.cloneStation').clone(true);
		//template.attr('style', '').removeClass('cloneStation').attr('id', 'occu' + stationCd);
		// 並び替え時に必要なclassをセット
		template.find('.wrapStation').addClass('activeWrapStation');
		template.find('.displayOrder').val(index + 1);
		// selectの変更
		for (selectKey in selectParams) {
			template.find('[name$="' + selectKey + '"]').val(selectParams[selectKey]);
		}
		var txt = template.html();
		// 値の置き換え
		for(textKey in textParams){
			regexp = new RegExp('@' + textKey, 'g'),
			txt = txt.replace(regexp, textParams[textKey]);
		}
		txt = txt.replace(/ disabled="disabled"/g, '');
		txt = txt.replace(/@index/g, index);
		$("#sortable").append(txt);
		$('#stationTitle').show();
		index++;
	}

	// 駅の削除
	$(document).on('click', ".occuDelete", function(){
		var id = $(this).data('stationCd');
		$('#wrap' + id).remove();
		sortIndex();
	});

	// 駅の全削除
	$(document).on('click', "#allDeleteRouteBtn", function(){
		$('#sortable').empty();
		$('#stationTitle').hide();
	});

	// 駅タイトル部分の切り替え
	function toggleStationTitle() {
		if($('.activeWrapStation').size()) {
			$('#stationTitle').show();
		} else {
			$('#stationTitle').hide();
		}
	}

	// 並び替えを行う
	function sortIndex() {
		var count = 1;
		var $wraps = $('#sortable .activeWrapStation');
		$wraps.each(function(){
			// 並び順を設定
			$(this).find('.displayOrder').each(function(i, elem){
				$(elem).val(count);
			});
            count++;
		});
		index = count-1;
	}

	/**
	 * 駅追加時のチェック
	 */
	function checkStationCd(stationCd) {

		var flg = true;
		$(".stationCdHidden").each(function() {
			if ($(this).val()-0 === stationCd-0) {
				flg = false;
				return;
			}
		});
		return flg;
	}

	<!-- 駅の検索登録用 -->
	$("#sbtn").click(function(){
		var searchWord = $("#sbox").val();
		var prefCd = $("#prefecturesCd").val();
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
			$(".pContent ul").empty();
			if(data.length - 0 > 0) {
				$(".pContent").show();
                $(".pContent ul").show();
				$("#submit").show();
				$(".stationError").hide();

				for(var i in data) {

                    var checkbox = '<li><label><input type="checkbox"' +
                                    ' data-stationCd =' + data[i]['stationCd'] +
                                    ' data-lineCd = ' + data[i]['lineCd'] +
                                    ' data-companyCd = ' + data[i]['companyCd'] +
                                    ' data-stationName = ' + data[i]['stationName'] +
                                    ' data-lineName = ' + data[i]['lineName'] +
                                    ' data-companyName = ' + data[i]['companyName'] +
                                    '><p class="checkIcon"><span class="material-icons-outlined">radio_button_unchecked</span></p><div class="stationDetail">' +
									'<p class="company">' + data[i]['companyName'] + '</p>' +
									'<p class="train">' + data[i]['lineName'] + '</p>' +
									'<p class="stationName">' + data[i]['stationName'] +'駅</p>' +
								    '</div></label></li>';
                    $('.pContent ul').append(checkbox);
				}

                /*=============
		        新規店舗登録-最寄り駅検索ポップアップ
                ==============*/
                var stationLi = document.querySelectorAll('#station-popup .wrap .pContent li');
                for (var item of stationLi){
                    item.onclick = function() {
                        if(this.querySelector('input[type="checkbox"]').checked){
                            this.querySelector('input[type="checkbox"]').checked = false;
                            this.querySelector('.checkIcon span').innerText = 'radio_button_unchecked';
                            this.querySelector('.checkIcon span').style.color = '#a8a8a8';
                        }else{
                            this.querySelector('input[type="checkbox"]').checked = true;
                            this.querySelector('.checkIcon span').innerText = 'radio_button_checked';
                            this.querySelector('.checkIcon span').style.color = '#f3852d';
                        }
                        return false;
                    }
                }
			} else {
                $(".pContent ul").hide();
				$("#submit").hide();
				$(".stationError").show();
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


	$(document).on('click', '#submit', function() {
		var checkValAry = $('.pContent input:checkbox:checked');
		checkValAry.each(function(i, val){

			if(!checkStationCd(val.getAttribute('data-stationCd'))){
				return;
			}

			var textParams = {
					'companyCd'		: val.getAttribute('data-companyCd'),
					'companyName'	: val.getAttribute('data-companyName'),
					'lineCd' 		: val.getAttribute('data-lineCd'),
					'lineName'		: val.getAttribute('data-lineName'),
					'stationCd'		: val.getAttribute('data-stationCd'),
					'stationName'	: val.getAttribute('data-stationName'),
					'timeRequiredMinute' : '',
				};

			createStationTemplate(textParams, []);
			closeSpopup();
		});
	});

	$(document).on('click', '#searchRegistration',function() {
		$("#wrap_station_search input[type=checkbox]").prop('checked',false);
	});
	<!-- /駅の検索登録用 -->
});


/**
 * 国内外の切り替え
*/
function changeDomesticKbn(){
	// 日本の場合
	if($('[name="domesticKbn"]').val() - 0 == DOMESTIC){
		$(".japanArea").show();
		$(".foreignArea").hide();
		$('[name="shutokenForeignAreaKbn"]').val('');
		$('[name="foreignAddress"]').val('');
	// 海外の場合
	} else {
		$(".japanArea").hide();
		$(".foreignArea").show();
		$('[name="prefecturesCd"]').val('');
		$('[name="cityCd"]').val('');
		$('[name="address"]').val('');
		resetOption(["companyCd", "lineCd", "stationCd"]);
	}
}


<%-- 文字カウント用JSON(最大値を格納しておく) --%>
var countJson = {shopInformation:300, arbeitFreeComment:300, catchCopy:30, industryText:20, arbeitShopSingleWord:13};


<%-- 文字カウントを行う --%>
var changeCharCount = function(targetName) {
	var maxCount = countJson[targetName];
	var count = 0;
	var textId = "#" + targetName + "TextId";
	var inputStr = $(textId).val();

	if (typeof(inputStr) == "undefined") {
		count = 0;
	} else {
		count = getCount(inputStr);
	}

	var targetSpan = $("#" + targetName + "Span");

	if (maxCount <= 0) {
		targetSpan.css("display", "none");
		return;
	} else {
		targetSpan.css("display", "inline");
	}

	var remainCount = maxCount - count;
	targetSpan.html("残り&nbsp;<span class=\"number\">" + remainCount + "</span>&nbsp;字");

	if (remainCount < 0) {
		targetSpan.css({color:"#F00", fontWeight:"bold"});
	} else {
		targetSpan.css({color:"#000", fontWeight:"normal"});
	}

};

<%-- 引数の文字数を取得 --%>
var getCount = function(str) {
	var replacedStr = str.replace(/\r\n/g, "rn");
	replacedStr = replacedStr.replace(/\r|\n/g, "rn");
	return replacedStr.length;
};

/**
 * フォームからonkeyup、onchangeで呼び出されるテキストカウントです。
 */
function countTextChar(obj) {
	changeCharCount(obj.name);
}

function getLastMonth(){
	var openDateYear = String($("#openDateYear").val());
	var openDateMonth = String($("#openDateMonth").val());

	if(openDateMonth.length == 1){
		openDateMonth = "0" + openDateMonth;
	}

	if(openDateYear == "") {
		$("#openDateLimitDisplayDate").val(null);
	}else if(openDateYear == "" && openDateMonth == "") {
		$("#openDateLimitDisplayDate").val(null);
	}else{
		var m = moment(String(openDateYear) + String(openDateMonth) + "01", "YYYYMMDD");
		m.add('month', 1).endOf('month')
		var limit = m.format('YYYY-MM-DD');
		$("#openDateLimitDisplayDate").val(limit);
	}
}

/**
 * フリーワード
 */
$(function(){

	$('#freeword').tagit({
		placeholderText:"タグをつけることができます",
		singleField: true,
		singleFieldNode: $('#tagList'),
		afterTagAdded: function(event, ui) {
			var tags = [];
			$(".tagit-label").each(function (){
				tags.push($(this).text());
			})
			$('#tagList').val(tags.join(","));
		},
		afterTagRemoved: function(event, ui) {
			var tags = [];
			$(".tagit-label").each(function (){
				tags.push($(this).text());
			})
			$('#tagList').val(tags.join(","));
		}
		});

	$('.tg').click(function(){
		var tags = [];
		var addTag = $(this).text();
		$('#freeword').tagit("createTag", addTag);
		$(".tagit-label").each(function (){
			tags.push($(this).text());
		})
		$('#tagList').val(tags.join(","));
	});
});

	/**
	 * 画像データをアップロードする
	 */
	function addMaterial(kbn) {
		var file = $('#upImg_' + kbn).prop("files")[0];
		// ファイルを選択していない場合は処理しない
		if (file == null || file == undefined) {
			return;
		}
		var fd = new FormData();
		fd.append('imgFile', file);
		fd.append('hiddenMaterialKbn', kbn);

	    // アップロード
		$.ajax({
				url : "${f:url(upImgAjaxPath)}",
				type : "POST",
				data : fd,
				cache : false,
				contentType : false,
				processData : false,
				dataType : "json",
		}).done(function(data) {
				// エラーが発生した場合は警告
				if(data.error) {
					alert(data.error);
					return;
				}

				// 画像の差し替え(ブラウザキャッシュを回避するため、パスにインクリメントパラメータを設定しておく)
				var imgPath = data.imgPath + "?cacheTmpKey" + cacheTmpKey + "=" + cacheTmpKey++;
				var altVal = $('#uploaded_img_' + kbn).attr('data-alt-val');

				var imgTag = '<a href="' + imgPath +'" title="' + altVal + '" data-lightbox="photo">'
								 + '<img class="thumbnail_image" alt="' + altVal + '" src="' + imgPath +'" /><br />'
							 + '</a>';

				// サムネイル表示
				$('#uploaded_'+ kbn).show();
				$('#uploaded_img_'+ kbn).html(imgTag);

				// フォームを隠す
				$("#noUpload_" + kbn).hide();

		}).fail(function(jqXHR, textStatus, errorThrown) {
			alert("アップロードに失敗しました。はじめから処理をやり直してください。");
			console.log(textStatus);
			console.log(errorThrown);
		});
	}

	/**
	 * 素材データを削除する
	 */
	function delMaterial(kbn) {
		//削除処理
		if (confirm('削除してもよろしいですか？')) {
			var fd = new FormData();
			fd.append('hiddenMaterialKbn', kbn);
			$.ajax({
					url : "${f:url(delImgAjaxPath)}",
					type : "POST",
					data : fd,
					cache : false,
					contentType : false,
					processData : false,
					dataType : "json"

				}).done(function(data) {
					// エラーが発生した場合は警告
					if(data.error) {
						alert(data.error);
						return;
					}

					// サムネイル隠す
					$('#uploaded_'+ kbn).hide();

					// フォーム表示
					$("#noUpload_" + kbn).show();

				}).fail(function(jqXHR, textStatus, errorThrown) {
					alert("削除に失敗しました。はじめから処理をやり直してください。");
					console.log(textStatus);
					console.log(errorThrown);
				});
		}
	}

	// メディア選択のモーダル準備
	$(function() {
	    $( '#media_select_1').leanModal({
	        top: 50,                     // モーダルウィンドウの縦位置を指定
	        overlay : 0.5,               // 背面の透明度
	        closeButton: ".modal_close"  // 閉じるボタンのCSS classを指定
	    });
	    $( '#media_select_2').leanModal({
	        top: 50,                     // モーダルウィンドウの縦位置を指定
	        overlay : 0.5,               // 背面の透明度
	        closeButton: ".modal_close"  // 閉じるボタンのCSS classを指定
	    });
	});

	$(function(){

		$('.select_media_btn').click(function(){
			var mediaType = $(this).attr('data-media-kbn');
			console.log("mediaKbn:"+mediaType);
			$("#mediaType").val(mediaType);
		});

		// メディア選択されたときのイベント
		$('.tile li').click(function(){
			var value = $(this).attr('data-media-id');
			console.log('value:' + value);

			var imageName = $(this).children("span").text();
			console.log('imageName:' + imageName);

			var imageSrc = $(this).children("img").attr('src');

			var kbn = $("#mediaType").val();
			var altVal = $('#uploaded_img_' + kbn).attr('data-alt-val');
			var imgTag = '<a href="' + imageSrc +'" title="' + altVal + '" data-lightbox="photo">'
							+ '<img class="thumbnail_image" alt="' + altVal + '" src="' + imageSrc +'" /><br />'
							+ '</a>';

			// サムネイルの表示
			$('#select_uploaded_'+ kbn).show();
			$('#select_uploaded_img_'+ kbn).html(imgTag);

			// hiddenにセット
			$('#mediaKbn'+ kbn).val(value);

			// フォームを隠す
			$("#noUpload_" + kbn).hide();

			// モーダル閉じる
			$('.modal_close')[0].click();
		});

		// アップロード済みなら削除ボタンを表示
		var mainImg_uploadFlg = ${gf:isMapExsists(materialMap, MATERIAL_KBN_MAIN_1)};
		var logoImg_uploadFlg = ${gf:isMapExsists(materialMap, MATERIAL_LOGO)};

		var mainImg_selectFlg = ${not empty mainImgSelect};
		var logoImg_selectFlg = ${not empty logoImgSelect};

		if (mainImg_uploadFlg == true) {
			$('#noUpload_1').hide();
			$('#select_uploaded_1').hide();

		} else if (mainImg_selectFlg == true) {
			$("#mediaKbn1").val(${mainImgSelect});
			$('#noUpload_1').hide();
			$('#uploaded_1').hide();
		} else {
			$('#uploaded_1').hide();
			$('#select_uploaded_1').hide();
		}

		if (logoImg_uploadFlg == true) {
			$('#noUpload_2').hide();
			$('#select_uploaded_2').hide();
		} else if (logoImg_selectFlg == true) {
			$("#mediaKbn2").val(${logoImgSelect});
			$('#noUpload_2').hide();
			$('#uploaded_2').hide();
		} else {
			$('#uploaded_2').hide();
			$('#select_uploaded_2').hide();
		}

	});

	/**
	 * 選択を削除する
	 */
	function delSelectMaterial(kbn) {
		//削除処理
		if (confirm('削除してもよろしいですか？')) {

			// サムネイル隠す
			$('#mediaKbn'+ kbn).val('');
			$('#select_uploaded_'+ kbn).hide();

			// フォーム表示
			$("#noUpload_" + kbn).show();

		}
	}

	// ボタン処理
	$(function(){
		// 確認ボタン
		$('#btn_conf').click(function(){
			removePost();
		});
		// 戻るボタン
		$('#btn_back').click(function(){
			$('.cloneStation').remove();
		});

		// POST時にエラーがあれば処理をしない
		function removePost() {
			$("#shopListForm").bind("jqv.form.result", function(event, errorFound) {
				if(!errorFound) {
					changeFileEnable(true);
					$('.cloneStation').remove();
				}
			});
		}
		/**
		 * ファイル入力フォームの活性、非活性を制御
		 */
		 function changeFileEnable(enable) {
			$('input[type="file"]').prop("disabled", enable);
		}

	});

	// プルダウンのセット
	function setOption(url,jsonParam, selectorId) {
		$.ajax({
			'url' : url,
			'data' : jsonParam,
			'type' : "POST",
			"success" : function(result) {
				if ($.isEmptyObject(result)) {
					return;
				}
				var options = [];
				for (var i in result) {
					options.push(new Option(result[i].name, result[i].cd));
				}
				$("#" + selectorId).append(options);
			}
		});
	}

	// プルダウンをリセット
	function resetOption(selectorIds) {
		for (var i in selectorIds) {
			$("#" + selectorIds[i]).empty();
			$("#" + selectorIds[i]).append(new Option("${common['gc.pullDown']}", ""));
		}
		// ボタンを非活性
		$("#addRouteBtn").attr("disabled", true);
		// プルダウンのリセットが先に動かないように同期処理にしておく
		return new Promise(function(resolve, reject){
			resolve();
		});
	}

	$("[data-fancybox]").fancybox({
		 arrows : false,
		 modal  : true,
	});

</script>

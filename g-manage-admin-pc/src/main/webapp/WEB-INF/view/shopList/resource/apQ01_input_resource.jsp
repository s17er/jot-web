<%--
店舗データ登録・編集のCSS、JS
 --%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<c:set var="MATERIAL_LOGO" value="<%=MTypeConstants.ShopListMaterialKbn.LOGO%>" scope="request" />
<c:set var="MATERIAL_KBN_MAIN_1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1%>" scope="request" />
<c:set var="SALERY_STRUCTURE_KBN_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>" scope="page" />
<c:set var="SALERY_STRUCTURE_KBN_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>" scope="page" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/validationEngine.jquery.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.css" media="screen" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.validationEngine-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/moment.js"></script>

<script type="text/javascript" src="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/tablesorter/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/tablesorter/jquery.tablesorter.widgets.min.js"></script>

<jsp:include page="/WEB-INF/view/common/station_js.jsp"></jsp:include>

<script type="text/javascript">
	// キャッシュを回避するための値
	var cacheTmpKey = 0;

	// 国内
	var DOMESTIC = ${DOMESTIC} - 0;

	$(function(){

		<c:if test="${not empty targetAnchor}">
			location.href="#${f:h(targetAnchor)}";
		</c:if>

		changeCharCount("catchCopy");
		changeCharCount("shopInformation");
		changeCharCount("arbeitFreeComment");
		$("#shopListForm").validationEngine();

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
			$('[name="cityCd"]').append(new Option("${common['gc.pullDown']}", ""));
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

		//駅の検索登録モーダルのリセット
		function resetStationSearch() {
			$("#stationSearchBox").val(null);
			$("#searchStationTableBody").empty();
			$("#reflection").hide();
			$("#wrap_station_search table").hide();
			$("#wrap_station_search .error").hide();
		}
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
			template.attr('style', '').removeClass('cloneStation').attr('id', 'occu' + stationCd);
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
		$("#stationSearch").click(function(){
			var searchWord = $("#stationSearchBox").val();
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
			});
		});

		$(document).on('click', '#searchRegistration',function() {
			$("#wrap_station_search input[type=checkbox]").prop('checked',false);
		});
		<!-- /駅の検索登録用 -->

	});

	<!-- 職種モーダル用JS -->
	$(function(){

		$("#occuOther a").on("click", function() {
			$("#otherBox").slideToggle();
			$(this).toggleClass("active");
		});

		$('.activeWrapJob').each(function(){
			var wrapId = $(this).attr('id').replace('wrap', '');
			$(this).attr('data-sort', $("#job" + wrapId).data('sort'));
			setJobBtnColor(wrapId);
		});

		// 職種配列のindex
		var conditionIndex = ${not empty displayConditionDtoList ? fn:length(displayConditionDtoList) : 0};

		// 職種チェックボックスの変更
		$('input[name=employJobKbnList]').change(function(){
			var employValue = $(this).data('employValue');
			var jobValue = $(this).data('jobValue');
			var employName = $(this).data('employName');
			var jobName = $(this).data('jobName');
			var sort = $(this).data('sort');
			var id = employValue + '-' + jobValue;

			// チェックされたら入力フォームをセット
			if ($(this).prop('checked')) {
				// テンプレフォームをクローン
				var template = $('.cloneJob').clone(true);
				template.attr('style', '').removeClass('cloneJob').attr('id', 'occu' + id);
				// 並び替え時に必要なclassをセット
				template.find('.wrapJob').addClass('activeWrapJob');
				template.find('.displayOrder').val(conditionIndex + 1);
				// IDの置き換え
				var txt = template.html();
				txt = txt.replace(/@employValue/g, employValue);
				txt = txt.replace(/@jobValue/g, jobValue);
				txt = txt.replace(/@employName/g, employName);
				txt = txt.replace(/@jobName/g, jobName);
				txt = txt.replace(/ disabled="disabled"/g, '');
				txt = txt.replace(/@index/g, conditionIndex);
				txt = txt.replace(/@sort/g, sort);
				if("正社員" != employName) {
					txt = txt.replace("期間の定めなし", "内定時までに開示します");
				}

				var jobLength = $('.activeWrapJob').length;

				if(jobLength == 0) {
					$("#sortable_occ").append(txt);
				} else {
					var insertFlg = false;
					$('.activeWrapJob').each(function(index){
						if($(this).data('sort') > sort) {
							$(this).before(txt);
							insertFlg = true;
						}
						if(insertFlg) {
							return false;
						}
						if(!insertFlg && (index + 1) == jobLength) {
							$("#sortable_occ").append(txt);
						}
					});
				}
				renumberConditionIndex();
				conditionIndex++;

			// チェックが外れたら行を削除して配列indexの並び替え
			} else {
				$("#wrap" + id).remove();
                renumberConditionIndex();
			}
		});

		// 職種の削除
		$(document).on('click', ".occuDelete", function(){
			if(window.confirm('本当によろしいですか？')){
				var id = $(this).data('employValue') + '-' + $(this).data('jobValue');
				$('#wrap' + id).remove();
				$('#job' + id).prop('checked', false);
                renumberConditionIndex();
			}
		});

		// 職種登録フォームの登録（モーダルを閉じる）
		$(document).on('click', ".fcclose", function(){
			var wrapId = $(this).data('employValue') + '-' + $(this).data('jobValue');
			setJobBtnColor(wrapId);
		});

		// 募集要項の作成をしたタイミングで,コピー選択を更新
		$(document).on('click', '.occuAddBtn', function(){
			var wrapId = $(this).data('wrapId');
			var options = [new Option('コピー元を選択してください', '')];
			// 職種フォームのタイトルからプルダウンを生成（data-wrap-idにIDを仕込んでおく）
			$('.activeWrapJob .occupation').each(function(i, elem){
				if (wrapId != $(elem).data('wrapId')) {
					options.push(new Option($(elem).text(), $(elem).data('wrapId')));
				}
			});
			$('#jobCopy' + wrapId).empty();
			$('#jobCopy' + wrapId).append(options);
		});

		// 職種の値をコピー
		$(document).on('click', '.jobCopyBtn', function(){
			var wrapId = $(this).data('wrapId');
			var selectId = $('#jobCopy' + wrapId).val();
			// 未選択の場合は処理しない
			if ($.isEmptyObject(selectId)) {
				return;
			}
			// コピー先
			var $originForm = $('#modal' + wrapId + ' table');
			// コピー元
			var $targetForm = $('#modal' + selectId + ' table');
			// 値をコピー
			$targetForm.find('input, textarea, select').each(function(i, elem){
				var nameSelector = '[data-name="' + $(elem).data('name') + '"]';
				// チェックボックスの場合は同じ値を設定変更
				if($(elem).is(':checkbox') || $(elem).is(':radio')) {
					$originForm.find(nameSelector + '[value="' + $(elem).val() + '"]').prop('checked', $(elem).prop('checked'));
				}
				// ボタンの場合は無視
				else if ($(elem).is(':button')) {
				}
				// テキスト、テキストエリア、selectはそのまま移行
				else {
					$originForm.find(nameSelector).val($(elem).val());
				}

				if ($(elem).hasClass('text_count')) {
					countText($originForm.find(nameSelector));
				}
			});


			// 給与の選択切り替え時、月収、年収が選択されていれば「万」を表
			changeSaleryStructure($originForm.find('[data-name=saleryStructureKbn]'));
		});

		// 給与の選択切り替え時、月収、年収が選択されていれば「万」を表示
		$(document).on('change', 'select[data-name=saleryStructureKbn]', function(){
			changeSaleryStructure($(this));
		});

		function changeSaleryStructure($select) {
			var saleryVal = $select.val();
			var manTextClass = $select.attr('data-man-text');
			if (saleryVal == ${SALERY_STRUCTURE_KBN_MONTHLY} || saleryVal == ${SALERY_STRUCTURE_KBN_YEARLY}) {
				$('.' + manTextClass).show();
			} else {
				$('.' + manTextClass).hide();
			}
		}

		// ボタンの色をセット
		function setJobBtnColor(wrapId){
			var editFlg = false;
			$('#modal' + wrapId).find('input,textarea').each(function(i, elem){
				if($(elem).is(':checkbox') || $(elem).is(':radio')) {
					if ($(elem).prop('checked')) {
						editFlg = true;
					}
				}
				else if ($(elem).is(':button')) {
					// ボタンの場合は無視
				}
				else {
					if($(elem).val()) {
						editFlg = true;
					}
				}
				// ボタンを編集に変更
				if (editFlg) {
					$('#occuBtn' + wrapId).attr('name', 'occuEdit');
					$('#editFlg' + wrapId).val('1');
					return;
				}
			});
			// 中身が空の場合は登録ボタンにする
			 if(!editFlg) {
				$('#occuBtn' + wrapId).attr('name', 'occuNew');
				$('#editFlg' + wrapId).val('0');
			}
		}

        // input nameの配列番号を振り直す
        function renumberConditionIndex() {
            var count = 0;
            var $wraps = $('.activeWrapJob');
            $wraps.each(function(){
                $(this).find('input').each(function(){
                    var name = $(this).attr('name');
                    if(name != undefined){
                        name = name.replace(/\d{1,2}/, count);
                        $(this).attr('name', name);
                    }
                });
                $(this).find('select').each(function(){
                    var name = $(this).attr('name');
                    if(name != undefined){
                        name = name.replace(/\d{1,2}/, count);
                        $(this).attr('name', name);
                    }

                    var manText = $(this).data('man-text');
                    if(manText != undefined){
                        manText = manText.replace(/\d{1,2}/, count);
                        $(this).attr('data-man-text', manText);
                    }
                });
                $(this).find('textarea').each(function(){
                    var name = $(this).attr('name');
                    if(name != undefined){
                        name = name.replace(/\d{1,2}/, count);
                        $(this).attr('name', name);
                    }
                });
                $(this).find('b').each(function(){
                    var className = $(this).attr('class');
                    if(className != undefined){
                        className = className.replace(/\d{1,2}/, count);
                        $(this).attr('class', className);
                    }
                });
                count++;
            });
            conditionIndex = count;
        }
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
	var countJson = {shopInformation:300, arbeitFreeComment:300, catchCopy:30};


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
		if(openDateYear == "" && openDateMonth == "") {
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

		// 入力フォームを読み取り専用に設定する
		$('#freeword').find('input.ui-widget-content').prop('readonly', true);

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
		$('#conf_btn').click(function(){
			removePost();
		});
		// 戻るボタン
		$('#back_btn').click(function(){
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

		 toggleBeitRequired();

		// ライト版掲載しない場合は必須をはずす
		$('[name=jobOfferFlg]').change(function(){
			toggleBeitRequired();
		});

		function toggleBeitRequired() {
			if($('[name=jobOfferFlg]:checked').val() == 1) {
				$('.beitRequired').show();
				$('.beitRequiredObj').addClass('validate[required]');
			} else {
				$('.beitRequired').hide();
				$('.beitRequiredObj').removeClass('validate[required]');
			}
		}

	});

	$("[data-fancybox]").fancybox({
		 arrows : false,
		 modal  : true,
	});

</script>

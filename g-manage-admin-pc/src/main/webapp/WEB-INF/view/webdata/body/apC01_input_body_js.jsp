<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<c:set var="SALERY_STRUCTURE_KBN_MONTHLY" value="<%=MTypeConstants.SaleryStructureKbn.MONTHLY %>" scope="page" />
<c:set var="SALERY_STRUCTURE_KBN_YEARLY" value="<%=MTypeConstants.SaleryStructureKbn.YEARLY %>" scope="page" />
<!-- フリーワード入力欄用JS -->
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/jquery.tagit.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/tag-it.min.js"></script>

<!-- 特集 -->
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/multi-select/css/multi-select.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/multi-select/jquery.multi-select.js"></script>
<script>
$(function(){
	// selectBoxの設定
	$('#specialSelect').multiSelect({ keepOrder: true });

	<!-- 他の人が編集していないかどうかチェック -->
	<c:if test="${alertOtherAccessFlg}">
		alert("${f:h(webDataAccessDto.salesName)}さんが編集を行っている可能性があります。");
	</c:if>

});
</script>


<!-- スムーススクロール用JS -->
<script type="text/javascript">
$(function(){
	$('a[href^=#],a[href^=\\/]').click(function(){
		var speed = 400;
		var href= $(this).attr("href");
		var hash= href.split("#");
		href = $("#" + hash[1]);
		var target = $(href == "#" || href == "" ? 'html' : href);
		var position = target.offset().top - 0;
		$("html, body").animate({scrollTop:position}, speed, "swing");
		return false;
	});
	if (location.hash != "") {
		var speed2 = 0;
		var href2 = $(location.hash);
		location.hash = "";
		$(window).load(function(){
			var position2 = href2.offset().top - 0;
			$("html, body").animate({scrollTop:position2}, speed2, "swing");
			return false;
		});
	}
});
</script>

<!-- フォーム補助用JS -->
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/validationEngine.jquery.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.validationEngine-ja.js"></script>
<script type="text/javascript">
$(function(){
	//フォームバリデーション
	$("#webForm").validationEngine();
});
</script>
<!-- //フォーム補助用JS -->

<!-- 職種並び替え -->
<script type="text/javascript" src="${ADMIN_CONTENS}/js/tablesorter/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/tablesorter/jquery.tablesorter.widgets.min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/tablesorter/jquery.ui.touch-punch.min.js"></script>


<!-- 系列店舗選択用 -->
<script>
	// 選択中の職種店舗
	var checkedJobShopIds = {};
	var doInitJobShopListFlg = false;
	$(function(){

		initShopListDisplay();
		// ajaxの処理が終わってから選択中の職種店舗を反映する
		$(document).ajaxStop(function() {
			initJobShopList();
		});

		$('.shop_table').tablesorter({
			headers:{
				0:{sorter:false},
			},
		});

		// 店舗の全チェック
		$(document).on('change', '#allCheck', function() {
			var isCheck = $(this).is(':checked');
			$('.shop_table tbody tr').each(function(i, tr){
				// ラベルとエリアでのフィルタを考慮し、表示されている行のみ設定を変更する
				if ($(tr).is(':visible')) {
					$(tr).find($('input[type=checkbox]')).prop('checked', isCheck);
				}
			});
			setShopListIdValue();
			setInitShopListIdValueForIndeed();
		});

		// 店舗の全チェック
		$(document).on('change', '#indeedAllCheck', function() {
			var isCheck = $(this).is(':checked');
			$('.indeed_shop_table tbody tr').each(function(i, tr){
				// ラベルとエリアでのフィルタを考慮し、表示されている行のみ設定を変更する
				if ($(tr).is(':visible')) {
					$(tr).find($('input[type=checkbox]')).prop('checked', isCheck);
				}
			});
			setShopListIdValueForIndeed();
		});

		// indeed出力用の店舗IDのチェックが変更されたとき
		$(document).on('change', '.indeed_shop_table tbody input:checkbox', function(){
			setShopListIdValueForIndeed();
		});

		// 店舗IDのチェックが変更されたとき
		$(document).on('change', '.shop_table tbody input:checkbox', function(){
			setShopListIdValue();

			// チェックされた店舗の要素・ID
			let changeShop = $(this);
			let targetShopId = $(this).val();
			// チェックされた店舗と同じ店舗をインディード側でもチェック
			$('.indeed_shop_table tbody input').each(function(i, shopId){
				// チェックされた店舗と同じ場合
				if ($(shopId).val() == targetShopId) {
					// チェックされた場合
					if (changeShop.is(':checked')) {
						$(shopId).prop('checked', true);
					// チェックが外された場合
					} else {
						$(shopId).prop('checked', false);
					}
				}
			});

			// インディード側のhidden要素を再設定
			// IDをリセット
			$('#shopListIdForIndeedValue').empty();
			$('#shopListIdForIndeedValue input:hidden').remove();
			// チェックされている値
			let checkValAry = $('.indeed_shop_table tbody input:checkbox:checked');
			// インディード選択店舗の件数を設定
			$("#selectShopCountTextForIndeed").text(checkValAry.length);
			// hiddenに値をセット
			checkValAry.each(function(i, shopId){
				let shopIdVal = $(shopId).val();
				// 店舗名を取得
				let shopName = $("label[for='shopListId" + shopIdVal + "']").first().text();
				$('#shopListIdForIndeedValue').append($('<input type="checkbox" />').attr('name','shopListIdListForIndeed').attr('checked', 'checked').attr('data-shopName', shopName).val(shopIdVal));
			});
		});

		// 店舗選択のエリア選択
		$(document).on('change', '#selectArea, #selectLabel', function(){
			var areaVal = $('#selectArea').val();
			var labelVal = $('#selectLabel').val();

			$('.shop_table tbody tr').each(function(i, tr){
				if(($.isEmptyObject(areaVal) || $(tr).hasClass(areaVal)) && ($.isEmptyObject(labelVal) || $(tr).hasClass(labelVal))) {
					$(tr).show();
				} else {
					$(tr).hide();
				}
			});
		})

		// indeed出力用店舗選択のエリア選択
		$(document).on('change', '#selectAreaForIndeed, #selectLabelForIndeed', function(){
			var areaVal = $('#selectAreaForIndeed').val();
			var labelVal = $('#selectLabelForIndeed').val();

			$('.indeed_shop_table tbody tr').each(function(i, tr){
				if(($.isEmptyObject(areaVal) || $(tr).hasClass(areaVal)) && ($.isEmptyObject(labelVal) || $(tr).hasClass(labelVal))) {
					$(tr).show();
				} else {
					$(tr).hide();
				}
			});
		})

		// 職種に紐づく店舗のチェックボックスが変更されたときの処理
		$(document).on('change', 'input[name^="webJobDtoList"][name$="shopIdList"]', function(){
			// 選択した店舗のIDをオブジェクトに保存する
			var name = $(this).attr('name');
			checkedJobShopIds[name] = getCheckedJobShopIdValues(name);
		})

		// 職種店舗全選択チェックボックスが変更されたときの処理
		$(document).on('change', '.jobShopListAllCheckbox', function(){
			var index = $(this).data('index');
			if(isNaN(index)){
				return;
			}
			var checked = $(this).prop('checked');
			var name = 'webJobDtoList[' + index + '].shopIdList';
			$('input[name="' + name + '"').prop('checked', checked);
			checkedJobShopIds[name] = getCheckedJobShopIdValuesByChecked(name, checked);
		});
	});

	// 指定のNameのチェックボックスの状態を取得する
	function getCheckedJobShopIdValues(name){
		var values = {};
		$('input[name="' + name + '"]').each(function(i, elem){
			values[$(this).val()] = $(this).prop("checked");
		});
		return values;
	}

	// 指定のNameのチェックボックスの状態を取得する（選択状態指定）
	function getCheckedJobShopIdValuesByChecked(name, checked){
		var values = {};
		$('input[name="' + name + '"]').each(function(i, elem){
			values[$(this).val()] = checked;
		});
		return values;
	}

	// 店舗IDの値をセットする
	function setShopListIdValue() {
		// IDをリセット
		$('#shopListIdValue').empty();
		$('.jobShopCheckbox').empty();
		$('#shopListIdValue input:hidden').remove();
		// チェックされている値
		var $checkValAry = $('.shop_table tbody input:checkbox:checked');
		// 件数を設定
		$('#selectShopCountText').text($checkValAry.length);
		// hiddenに値をセット
		$checkValAry.each(function(i, shopId){
			var shopIdVal = $(shopId).val();
			// 店舗名を取得
			var shopName = $("label[for='shopListId" + shopIdVal + "']").first().text();
			$('#shopListIdValue').append($('<input type="checkbox" />').attr('name','shopListIdList').attr('checked', 'checked').attr('data-shopName', shopName).val(shopIdVal));
			// 職種モーダルに店舗チェックボックスを追加
			$(".jobShopCheckbox").each(function(i, elem) {
				// name属性に使用するIndex番号を取得（取得できなければ置換文字を指定）
				var index = $(this).data('index');
				var indexText = isNaN(index) ? '@index' : index;
				$(this).append(
					$('<li />').append(
						$('<label />').append(
							// チェックボックス追加 選択済みにする nameを設定する Value値を店舗IDにする
							getJobShopCheckbox('webJobDtoList[' + indexText + '].shopIdList', shopIdVal)
							// 店舗名表示
						).append(shopName)
					)
				);
			});
		});
		// 店舗が選択されていれば店舗チェックボックスを表示
		if($checkValAry.length > 0){
			$('.jobShopList').show();
		}else {
			$('.jobShopList').hide();
		}
	}

	// indeed出力用の店舗IDの値をセットする
	function setShopListIdValueForIndeed() {
		// IDをリセット
		$('#shopListIdForIndeedValue').empty();
		$('#shopListIdForIndeedValue input:hidden').remove();
		// チェックされている値
		var $checkValAry = $('.indeed_shop_table tbody input:checkbox:checked');
		// 件数を設定
		$("#selectShopCountTextForIndeed").text($checkValAry.length);
		// hiddenに値をセット
		$checkValAry.each(function(i, shopId){
			var shopIdVal = $(shopId).val();
			// 店舗名を取得
			var shopName = $("label[for='shopListId" + shopIdVal + "']").first().text();
			$('#shopListIdForIndeedValue').append($('<input type="checkbox" />').attr('name','shopListIdListForIndeed').attr('checked', 'checked').attr('data-shopName', shopName).val(shopIdVal));
		});
	}

	// indeed出力用の店舗IDの値を店舗選択と同じ内容でセットする
	function setInitShopListIdValueForIndeed() {
		// IDをリセット
		$('#shopListIdForIndeedValue').empty();
		$('#shopListIdForIndeedValue input:hidden').remove();
		var shopIdAry = [];
		// チェックされている値
		var checkValAry = $('.shop_table tbody input:checkbox:checked');
		// 件数を設定
		$("#selectShopCountTextForIndeed").text(checkValAry.length);
		// hiddenに値をセット
		checkValAry.each(function(i, shopId){
			var shopIdVal = $(shopId).val();
			// 店舗名を取得
			var shopName = $("label[for='shopListId" + shopIdVal + "']").first().text();
			$('#shopListIdForIndeedValue').append($('<input type="checkbox" />').attr('name','shopListIdListForIndeed').attr('checked', 'checked').attr('data-shopName', shopName).val(shopIdVal));

			shopIdAry.push(shopIdVal);
		});

		// indeed出力用店舗一覧の中から店舗選択のidと一致する項目をチェック状態にする
		var shopListAry = $('.indeed_shop_table tbody input');
		shopListAry.each(function(i, shopId){
			var val = $(shopId).val();
			if (shopIdAry.indexOf(val) !== -1) {
				$(shopId).prop('checked', true);
			} else {
				$(shopId).prop('checked', false);
			}
		});
	}

	// 追加する職種店舗のチェックボックスを取得する
	function getJobShopCheckbox(name, shopId){
		// 既に職種店舗を追加済みで、意図的にチェックを外している場合はチェックなし
		if(name in checkedJobShopIds){
			if(shopId in checkedJobShopIds[name]){
				if(!checkedJobShopIds[name][shopId]){
					return $('<input type="checkbox" />').attr('name', name).attr('data-name','webJobDtoList').val(shopId);
				}
			}
		}
		// それ以外は全てチェックあり
		return $('<input type="checkbox" />').attr('checked', 'checked').attr('data-name','webJobDtoList').attr('name', name).val(shopId);
	}

	// 職種店舗の初期選択状態を反映する
	function initJobShopList(){
		// 職種が登録されていれば処理開始
		if(${not empty webJobDtoList ? fn:length(webJobDtoList) : 0} > 0 && !doInitJobShopListFlg){
			<c:forEach items="${webJobDtoList}" var="webJobDto" varStatus="status">
				// 職種店舗のチェックを外す
				$('input[name="webJobDtoList[${status.index}].shopIdList"]').prop('checked', false);

				// 選択済みの職種店舗オブジェクトに未チェック（false）として追加する
				var values = {};
				<c:forEach items="${shopListIdList}" var="shopId">
					values[${shopId}] = false;
				</c:forEach>
				checkedJobShopIds['webJobDtoList[${status.index}].shopIdList'] = values;

				// 職種店舗のチェックを反映する
				<c:forEach items="${webJobDto.shopIdList}" var="shopId">
					var jobShopList = $('input[name="webJobDtoList[${status.index}].shopIdList"][value="${shopId}"]');
					if(jobShopList.length > 0){
						jobShopList.eq(0).prop('checked', true);
						// 選択済み職種店舗オブジェクトにチェック（true）として追加する
						checkedJobShopIds['webJobDtoList[${status.index}].shopIdList']['${shopId}'] = true;
					}
				</c:forEach>
			</c:forEach>
			doInitJobShopListFlg = true;
		}
	}
</script>
<!-- 系列店舗選択用 -->

<script type="text/javascript">
<!-- 職種モーダル用JS -->
$(function(){

	$("#occuOther a").on("click", function() {
		$("#otherBox").slideToggle();
		$(this).toggleClass("active");
	});

	$('.activeWrapJob').each(function(){
		var wrapId = $(this).attr('id').replace('wrap', '');
		setJobBtnColor(wrapId);
	});

	// 職種配列のindex
	var index = ${not empty webJobDtoList ? fn:length(webJobDtoList) : 0};

	// 職種チェックボックスの変更
	$('input[name=employJobKbnList]').change(function(){
		var employValue = $(this).data('employValue');
		var jobValue = $(this).data('jobValue');
		var employName = $(this).data('employName');
		var jobName = $(this).data('jobName');
		var id = employValue + '-' + jobValue;

		// チェックされたら入力フォームをセット
		if ($(this).prop('checked')) {
			// テンプレフォームをクローン
			var template = $('.cloneJob').clone(true);
			template.attr('style', '').removeClass('cloneJob').attr('id', 'occu' + id);
			// 並び替え時に必要なclassをセット
			template.find('.wrapJob').addClass('activeWrapJob');
			template.find('.displayOrder').val(index + 1);
			// IDの置き換え
			var txt = template.html();
			txt = txt.replace(/@employValue/g, employValue);
			txt = txt.replace(/@jobValue/g, jobValue);
			txt = txt.replace(/@employName/g, employName);
			txt = txt.replace(/@jobName/g, jobName);
			txt = txt.replace(/ disabled="disabled"/g, '');
			txt = txt.replace(/@index/g, index);
			if("正社員" != employName) {
				txt = txt.replace("期間の定めなし", "内定時までに開示します");
			}
			$("#sortable_occ").append(txt);
			index++;

		// チェックが外れたら行を削除して配列indexの並び替え
		} else {
			$("#wrap" + id).remove();
			sortIndex();
		}
	});

	// 職種の削除
	$(document).on('click', ".occuDelete", function(){
		if(window.confirm('本当によろしいですか？')){
			var id = $(this).data('employValue') + '-' + $(this).data('jobValue');
			$('#wrap' + id).remove();
			$('#job' + id).prop('checked', false);
			sortIndex();
		}
	});

	// 職種登録フォームの登録（モーダルを閉じる）
	$(document).on('click', ".fcclose", function(){
		var wrapId = $(this).data('employValue') + '-' + $(this).data('jobValue');
		setJobBtnColor(wrapId);
	});

	// 職種の並び替え
	$('#sortable_occ').sortable({
		cursor: 'move',                     //移動中のカーソル
		opacity: 0.7,                       //移動中の項目の透明度
		placeholder: "ui-state-highlight",  //ドロップ先の色指定(Styleで指定可能)
		forcePlaceholderSize: true,         //trueでドラッグした要素のサイズを自動取得できる
		// 変更されたら配列のindexを付け替え
		update : function(e, ui) {
			sortIndex();
		}
	});

	$( '#sortable_occ' ).disableSelection();

	// 並び替えを行う
	function sortIndex() {
		var count = 1;
		var $wraps = $('#sortable_occ .activeWrapJob');
		$wraps.each(function(){
			// 並び順を設定
			$(this).find('.displayOrder').each(function(i, elem){
				$(elem).val(count);
			});
            count++;
		});
	}

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

		// 文字カウントを変更
		$('#modal' + wrapId + ' .text_count').each(function(i, elem){
			countText($(elem));
		});
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
	$(document).on('change', '[data-name=saleryStructureKbn]', function(){
		changeSaleryStructure($(this));
	});

	function changeSaleryStructure($select) {
		var saleryVal = $select.val();
		var manTextClass = $select.data('man-text');
		if (saleryVal == ${SALERY_STRUCTURE_KBN_MONTHLY} || saleryVal == ${SALERY_STRUCTURE_KBN_YEARLY}) {
			$('.' + manTextClass).show();
		} else {
			$('.' + manTextClass).hide();
		}
	}

	// 職種用のテキスト文字数カウント
	$(document).on('keyup keydown keypress', '.text_count', function(){
		countText($(this));
	});

	// カウント
	function countText($obj) {
		var maxCount = $obj.data('maxLength');
		if (!$.isNumeric(maxCount)) {
			return;
		}
		var wrapId = $obj.data('wrapId');
		var remainCount = $obj.val().length;

		var copyVal = $obj.val();
		var linefeedCount = 0;
		var loop = true;
		do{
			if(copyVal.indexOf('\n') > -1) {
				copyVal = copyVal.replace('\n', '');
				linefeedCount++;
			}else{
				loop = false;
			}
		}while(loop)

		var $targetSpan = $('#' + $obj.data('name') + 'Span' + wrapId);
		$targetSpan.text('残り' + (maxCount - remainCount - linefeedCount) + '文字');
		if (remainCount > maxCount) {
			$targetSpan.css({color:"#F00", fontWeight:"bold"});
		} else {
			$targetSpan.css({color:"#000", fontWeight:"normal"});
		}
	}
});

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
</script>
<!-- //職種モーダル用JS -->


<!-- 画像アップロードJS -->
<script>
	// キャッシュを回避するための値
	var cacheTmpKey = 0;

	/**
	 * 素材データをアップロードする
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
				var imgThumbPath = data.imgThumbPath + "?cacheTmpKey" + cacheTmpKey + "=" + cacheTmpKey;
				var altVal = $('#img_' + kbn).attr('data-alt-val');
				var imgTag = '<a href="' + imgPath +'" title="' + altVal + '" data-lightbox="photo">'
								 + '<img alt="' + altVal + '" src="' + imgThumbPath +'" /><br /><br />'
							 + '</a>';
				$('#img_' + kbn).html(imgTag);

				// ボタンの制御
				$("#upImg_" + kbn).val("");
				$("#upImg_" + kbn).hide();
				$("#uploadImgBtn_" + kbn).hide();
				$("#deleteImgBtn_" + kbn).show();

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
					// 画像の削除
					$('#img_' + kbn).html("");
					// ボタンの制御
					$("#upImg_" + kbn).show();
					$("#uploadImgBtn_" + kbn).show();
					$("#deleteImgBtn_" + kbn).hide();

				}).fail(function(jqXHR, textStatus, errorThrown) {
					alert("削除に失敗しました。はじめから処理をやり直してください。");
					console.log(textStatus);
					console.log(errorThrown);
				});
		}
	}


</script>

<script>
	/*** 顧客検索サブ画面の設定 ***/

	/**
	 * 顧客検索画面を起動し、選択したデータを返却する
	 */
	function selectCustomer() {

		// 顧客検索画面の呼び出し
		window.open('${f:url(subWindowPath)}','selectSub','width=1000,height=1200,scrollbars=yes');

	}

	// 顧客設定後の処理
	function callbackCustomer(customerId
			, customerName
			, contactName
			, areaCd
			, areaName
			, phoneNo
			, mainMail
			, subMail
			, companySalesName) {


		// 画面表示
		$("#customerId").text(customerId);
		$("#customerName").text(customerName);
		$("#contactName").text(contactName);
		$("#customerAreaName").text(areaName);
		$("#phoneNo").text(phoneNo);
		$("#mainMail").text(mainMail);
		var subMail = $.trim(subMail);
		console.info(subMail);
		if (!subMail == "") {
			$("#subMail").html('<br>' + subMail);
		} else {
			$("#subMail").html("");
		}
		console.info($("#subMail").html());
	    $("#companySalesName").html(companySalesName);

		// WEBデータのフォームにセット
		$("#hiddenCustomerId").val(customerId);
		$("#hiddenCustomerName").val(customerName);
		$("#hiddenContactName").val(contactName);
		$("#hiddenCustomerAreaName").val(areaName);
		$("#hiddenCustomerAreaCd").val(areaCd);
		$("#hiddenPhoneNo").val(phoneNo);
		$("#hiddenMainMail").val(mainMail);
		$("#hiddenSubMail").val(subMail);
		console.info($("#hiddenSubMail").val());

		$("#hiddenCompanySalesName").val(companySalesName);

		<c:if test="${pageKbn ne PAGE_INPUT}">
			$("#ipPhoneTr").css("display", "");
		</c:if>

		if (customerId) {
			$('#shopSelectBtn').attr('disabled', false);
			$('#indeedShopSelectBtn').attr('disabled', false);
		} else {
			$('#shopSelectBtn').attr('disabled', 'disabled');
			$('#indeedShopSelectBtn').attr('disabled', 'disabled');
		}

		setShopList(customerId, [], []);
		return;

	}

	/**
	 * 店舗一覧の初期表示
	*/
	function initShopListDisplay() {

		var shopListIds = [
			<c:forEach items="${shopListIdList}" var="shopId">
				${shopId},
			</c:forEach>
			];

		var shopListIdsForIndeed = [
			<c:forEach items="${shopListIdListForIndeed}" var="shopId">
				${shopId},
			</c:forEach>
			];
		var customerId = "${customerDto.id}";

		if (!$.isEmptyObject(customerId)) {
			setShopList(parseInt(customerId), shopListIds, shopListIdsForIndeed);
		}
	}
	/**
	 * 顧客切り替え時に店舗一覧をセットする
	*/
	function setShopList(customerId, shopListIds, shopListIdsForIndeed) {
		// 一覧を空にする
		$('.shop_table tbody').empty();
		$("#wrap_shop_search #selectArea").empty();
		$("#wrap_shop_search #selectLabel").empty();
		$('#shopListIdValue').empty();
		$('.indeed_shop_table tbody').empty();
		$("#wrap_shop_indeed_search #selectArea").empty();
		$("#wrap_shop_indeed_search #selectLabel").empty();
		$('#shopListIdForIndeedValue').empty();
		// 職種の店舗チェックボックスを削除
		removeJobShopList();

		// 店舗の取得
		$.ajax({
			'url' : "${f:url('/ajax/json/shopList')}",
			'data' : "customerId=" + customerId,
			'type' : "POST",
			"success" : function(result) {

				// 件数をセット
				$("#allShopCountText").text(result.length);
				$("#selectShopCountText").text(0);
				$("#selectShopCountTextForIndeed").text(0);

				if ($.isEmptyObject(result)) {
					$('#shopSelectBtn').attr('disabled', 'disabled');
					$('#indeedShopSelectBtn').attr('disabled', 'disabled');
					return;
				}

				// ラベルフィルタのOption
				var labelOptions = [new Option('選択してください', '')];
				var labelOptionsIndeed = [];

				// ラベルの重複を判定する
				var checkLabelId = [];

				// エリアフィルタのOption
				var areaOptions = [new Option('選択してください', '')];
				var areaOptionsIndeed = [];

				// エリアの重複を判定
				checkAreaId = [];

				var prefix = 'label_';

				for (var i in result) {

					var $tr = $('<tr>');

					// 【ラベルのフィルタ処理】
					// ・<tr>タグに「label_[labelId]」に設定
					// ・ラベルをフィルタする「select」をvalue=id,text=labelNameで作成
					// ・selectからラベルを選択しフィルタすると、trに設定したclassのみ表示される
					// ※フィルタ処理は、「#selectLabel」のchange処理参照
					var labelClass = [];
					if (!$.isEmptyObject(result[i].labelDtoList)) {
						for (var j in result[i].labelDtoList) {
							var labelDto = result[i].labelDtoList[j];
							// フィルタするためにclassを作成
							labelClass.push(prefix + labelDto.id);
							// ラベルプルダウン用の設定
							if (checkLabelId.indexOf(labelDto.id) == -1) {
								labelOptions.push(new Option(labelDto.labelName, prefix + labelDto.id));
								labelOptionsIndeed.push(new Option(labelDto.labelName, prefix + labelDto.id));
								checkLabelId.push(labelDto.id);
							}
						}
					}

					var className = '';

					// ラベルとエリアでフィルタするためにtrにclassを設定
					if (labelClass.length === 0) {
						className = 'prefecturesCd_' + result[i].prefecturesCd;
					} else {
						className = labelClass.join(' ') + ' prefecturesCd_' + result[i].prefecturesCd;
					}

					$tr.attr('class', className);

					// テーブルの中身設定
					if (!$.isEmptyObject(shopListIds) && $.inArray(parseInt(result[i].id), shopListIds) > -1) {
						$('<td class="posi_center">').append($('<input />', { type: 'checkbox', name: 'shopListId', value: result[i].id, id:'shopListId' + result[i].id, checked: 'checked' })).appendTo($tr);
					} else {
						$('<td class="posi_center">').append($('<input />', { type: 'checkbox', name: 'shopListId', value: result[i].id, id:'shopListId' + result[i].id })).appendTo($tr);
					}
					$('<td>').append($('<label>', {for: 'shopListId' + result[i].id}).text(result[i].shopName)).appendTo($tr);
					$('<td>').text(result[i].prefecturesName).appendTo($tr);
					$('<td>').text(result[i].cityName).appendTo($tr);
					$('<td>').text(result[i].industryKbn1Label).appendTo($tr);
					$('<td>').text(result[i].industryKbn2Label).appendTo($tr);
					$('.shop_table tbody').append($tr);


					// indeed店舗表示用の設定
					var $tr = $('<tr>');

					// ラベルとエリアでフィルタするためにtrにclassを設定
					$tr.attr('class', className);

					// 都道府県が重複していない場合
					if (checkAreaId.indexOf(result[i].prefecturesCd) == -1) {
						areaOptions.push(new Option(result[i].prefecturesName, 'prefecturesCd_' + result[i].prefecturesCd));
						areaOptionsIndeed.push(new Option(result[i].prefecturesName, 'prefecturesCd_' + result[i].prefecturesCd));
						checkAreaId.push(result[i].prefecturesCd);
					}

					// テーブルの中身設定
					if (!$.isEmptyObject(shopListIdsForIndeed) && $.inArray(parseInt(result[i].id), shopListIdsForIndeed) > -1) {
						$('<td class="posi_center">').append($('<input />', { type: 'checkbox', name: 'shopListIdForIndeed', value: result[i].id, id:'shopListId' + result[i].id, checked: 'checked' })).appendTo($tr);
					}else{
						$('<td class="posi_center">').append($('<input />', { type: 'checkbox', name: 'shopListIdForIndeed', value: result[i].id, id:'shopListId' + result[i].id })).appendTo($tr);
					}
					$('<td>').append($('<label>', {for: 'shopListId' + result[i].id}).text(result[i].shopName)).appendTo($tr);
					$('<td>').text(result[i].prefecturesName).appendTo($tr);
					$('<td>').text(result[i].cityName).appendTo($tr);
					$('<td>').text(result[i].industryKbn1Label).appendTo($tr);
					$('<td>').text(result[i].industryKbn2Label).appendTo($tr);
					$('.indeed_shop_table tbody').append($tr);
                }
				// テーブルの更新
				$(".shop_table").trigger("update");
				$(".indeed_shop_table").trigger("update");

				// エリア・ラベルの選択肢を更新
				$("#wrap_shop_search #selectArea").append(areaOptions);
				$("#wrap_shop_search #selectLabel").append(labelOptions);
				$("#wrap_shop_indeed_search #selectAreaForIndeed").append(areaOptionsIndeed);
				$("#wrap_shop_indeed_search #selectLabelForIndeed").append(labelOptionsIndeed);

				// ボタンの活性化
				$('#shopSelectBtn').attr('disabled', false);
				$('#indeedShopSelectBtn').attr('disabled', false);

				// 選択件数
				$('#selectShopCountText').text(shopListIds.length);
				$("#selectShopCountTextForIndeed").text(shopListIdsForIndeed.length);

				setShopListIdValue();
				setShopListIdValueForIndeed();
            }
		});
	}

	/**
	 * 顧客検索結果をリセットする
	 */
	function resetCustomer() {

		if(!confirm("クリアしますか？")) {
			return;
		}

		($("#customerId").text(" "));
		($("#customerName").text(" "));
		($("#contactName").text(" "));
		($("#customerAreaName").text(" "));
		($("#phoneNo").text(" "));
		($("#mainMail").text(" "));
		($("#subMail").text(" "));
	    ($("#companySalesName").html("　"));

		// WEBデータのフォームにセット
		($("#hiddenCustomerId").val(""));
		($("#hiddenCustomerName").val(""));
		($("#hiddenContactName").val(""));
		($("#hiddenCustomerAreaName").val(""));
		($("#hiddenCustomerAreaCd").val(""));
		($("#hiddenPhoneNo").val(""));
		($("#hiddenMainMail").val(""));
		($("#hiddenSubMail").val(""));
		($("#hiddenCompanySalesName").val(""));

		<c:if test="${pageKbn ne PAGE_INPUT}">
			$("#ipPhoneTr").css("display", "none");
		</c:if>

		// 店舗検索ボタンを非活性
		$('#shopSelectBtn').attr('disabled', 'disabled');
		$('#indeedShopSelectBtn').attr('disabled', 'disabled');
		// 系列店舗一覧を削除
		$('.shop_table tbody').empty();
		$("#wrap_shop_search #selectArea").empty();
		$("#wrap_shop_search #selectLabel").empty();
		$('#shopListIdValue').empty();
		$('#allShopCountText').text(0);
		$('#selectShopCountText').text(0);
		$("#selectShopCountTextForIndeed").text(0);
		$('.indeed_shop_table tbody').empty();
		$("#wrap_shop_indeed_search #selectArea").empty();
		$("#wrap_shop_indeed_search #selectLabel").empty();
		$('#shopListIdForIndeedValue').empty();
		// 職種の店舗チェックボックスを削除
		removeJobShopList();

	}

	/**
	 * 職種の店舗チェックボックスを削除する
	 */
	function removeJobShopList(){
		$('.jobShopCheckbox').empty();
		$('.jobShopList').hide();
		checkedJobShopIds = {};
	}
</script>


<!-- Ajaxの設定 -->
<script>

// エリアの保持
var tempAreaCd = "";
// 所属会社の保持
var tempCompanyId = "";

// 初期処理
$(function(){

	// エリアの保持
	var value = $('select#areaCd').val();
	tempAreaCd = value;
	// 所属会社の保持
	tempCompanyId = $('select#companyId').val();
	// エリアが未選択の場合、関連項目を非活性

	if (value == "") {
		// エリア未選択時の処理
		selectArea('');

	} else {
		// FireFoxのキャッシュ用
		if ($('select#volumeId').val() == "") {
			// 号数の連動
			setVolumeAjaxParts('${f:url(volumeAjaxPath)}', 'volumeAjax', value, '1');
		}

		// エリア保持に変数にセット
		tempAreaCd = value;
	}

	// カーソル位置の設定
	var cursorPosition = "";
	if (cursorPosition != "") {
		var tempCursorPosition = cursorPosition;
		$('#cursorPosition').val("");
		location.href='#' + tempCursorPosition;
	}
});

/**
 * エリア選択時の処理
 */
function selectArea(value) {

	// 号数の初期表示
	$('#volumeAjax').load('${f:url(volumeAjaxPath)}',{'limitValue': value, 'authLevel': '${userDto.authLevel}'});

	// 特集の初期表示
	$('#special').load('${f:url(specialAjaxPath)}',{'limitValue': value});

	// 西日本のエリアを選択時IP電話を非表示
	if(value >= 3){
		$("#ipPhoneTr").hide();
	} else {
		$("#ipPhoneTr").show();
	}

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
		return false;
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

	tempCompanyId = value;

	// 会社が未選択の場合
	if (value == "") {
		// 営業担当者を初期表示
		setAjaxParts('${f:url(salesAjaxPath)}', 'salesAjax', '');

	// 会社が選択された場合
	} else {
		// 営業担当者を連動
		setAjaxParts('${f:url(salesAjaxPath)}', 'salesAjax', value);
	}

}

/**
 * エラー処理
 */
function errorAjax(responseText, status) {
	alert('切り替えに失敗しました。再度処理を行ってください。');
}

// ボタン処理
$(function(){
	// 確認ボタン
	$('#conf_btn').click(function(){
		removePost();
	});
	// 戻るボタン
	$('#back_btn').click(function(){
		removePost();
	});

	// POST時にエラーがあれば処理をしない
	function removePost() {
		$("#webForm").bind("jqv.form.result", function(event, errorFound) {
			if(!errorFound) {
				changeFileEnable(true);
				$('.cloneJob').remove();
			}
		});
	}
});

/**
 * ファイル入力フォームの活性、非活性を制御
 */
function changeFileEnable(enable) {
	$('input[type="file"]').prop("disabled", enable);
};


// プレビューボタンを押した時
var preview = function(windowName, submitBtn) {

	changeFileEnable(true);

	//ウィンドウopen
	editTarget(windowName);

	var action = $("#webForm").attr("action");
	// サブミットしてプレビュー表示
	$("#webForm").attr("action", action + $(submitBtn).attr("name"));
	$("#webForm").submit();

	changeFileEnable(false);

	$("#webForm").attr("action", action);
	return false;
};

</script>


<!-- サイズ変更 -->
<script>

$(function(){

	// 初期
	changeWebSize(${sizeKbn});
    selectWebSize(${sizeKbn});

	// サイズ変更時
	$("input[name='sizeKbn']").change(function(){
		// 共通処理のためcommonに記載
		changeWebSize($(this).val());
	    selectWebSize($(this).val());

	    setUnUploadMaterial($(this).val());
	});

	// 住所変更時の処理
	/*$('[name="mapAddress"]').change(function(){

		var address = $(this).val();
		$("#latitude").val("");
		$("#longitude").val("");
		marker.setPosition(null);

		if ($.isEmptyObject(address)) {
			console.info(address);
			return;
		}

		// 地図から住所をセット
		geoCoder.geocode({'address': address}, function(results, status){
			if(status == google.maps.GeocoderStatus.OK){
				var latLng = results[0].geometry.location;
				setLatLng(latLng);
			} else {
				alert('地図の設定に失敗しました。再度処理を行ってください。');
				console.log(status);
			}
		});
	});*/
});


function setUnUploadMaterial(sizeKbn) {

	var fd = new FormData();
	fd.append('sizeKbn', sizeKbn);
	$.ajax({
			url : "${f:url(setUnUploadMaterial)}",
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
		console.info(data);
		for(kbn in data){
			// 画像の差し替え(ブラウザキャッシュを回避するため、パスにインクリメントパラメータを設定しておく)
			var imgPath = data[kbn].imgPath + "?cacheTmpKey" + cacheTmpKey + "=" + cacheTmpKey++;
			var imgThumbPath = data[kbn].imgThumbPath + "?cacheTmpKey" + cacheTmpKey + "=" + cacheTmpKey;
			var altVal = $('#img_' + kbn).attr('data-alt-val');
			var imgTag = '<a href="' + imgPath +'" title="' + altVal + '" data-lightbox="photo">'
							 + '<img alt="' + altVal + '" src="' + imgThumbPath +'" /><br /><br />'
						 + '</a>';
			$('#img_' + kbn).html(imgTag);

			// ボタンの制御
			$("#upImg_" + kbn).val("");
			$("#upImg_" + kbn).hide();
			$("#uploadImgBtn_" + kbn).hide();
			$("#deleteImgBtn_" + kbn).show();
		}

	}).fail(function(jqXHR, textStatus, errorThrown) {
		alert("初期画像登録に失敗しました。はじめから処理をやり直してください。");
		console.log(textStatus);
		console.log(errorThrown);
	});
}

var countJson = {personHunting:${common['gc.webdata.personHunting.maxLength']},
				mapTitle:25,
				catchCopy1:20,
				catchCopy2:20,
				catchCopy3:20,
				sentence1:200,
				sentence2:0,
				sentence3:0,
				captionA:${common['gc.webdata.caption.maxLength']},
				captionB:${common['gc.webdata.caption.maxLength']},
				captionC:${common['gc.webdata.caption.maxLength']},
				attentionHereTitle:15,
				attentionHereSentence:${common['gc.webdata.attentionHere.maxLength']},
				attentionShopName:30,
				attentionShopSentence:93};

// サイズ変更時の処理
function selectWebSize(sizeKbn) {
	if (sizeKbn == ${f:h(SIZE_TEXT)}) {
		countJson.sentence1 = ${common['gc.webdata.sentence1.maxLength6']};
		countJson.sentence2 = 0;
		countJson.attentionHereTitle = 0;
		countJson.attentionHereSentence = 0;
		countJson.sentence3 = 0;
		countJson.captionA = 0;
		countJson.captionB = 0;
		countJson.captionC = 0;
	}

	if (sizeKbn == ${f:h(SIZE_A)} || sizeKbn == ${f:h(SIZE_B)} ) {
		countJson.sentence1 = ${common['gc.webdata.sentence1.maxLength1']};
		countJson.sentence2 = 0;
		countJson.sentence3 = 0;
		countJson.captionA = 0;
		countJson.captionB = 0;
		countJson.captionC = 0;


		if ( sizeKbn == ${f:h(SIZE_B)}) {
			countJson.attentionHereTitle = 15;
			countJson.attentionHereSentence = ${common['gc.webdata.attentionHere.maxLength']};
		} else {
			countJson.attentionHereTitle = 0;
			countJson.attentionHereSentence = 0;
		}
	}

	if (sizeKbn == ${f:h(SIZE_C)}) {
		countJson.sentence1 = ${common['gc.webdata.sentence1.maxLength3']};
		countJson.sentence2 = ${common['gc.webdata.sentence2.maxLength3']};
		countJson.sentence3 = 0;
		countJson.captionA = ${common['gc.webdata.caption.maxLength']};
		countJson.captionB = ${common['gc.webdata.caption.maxLength']};
		countJson.captionC = ${common['gc.webdata.caption.maxLength']};
		countJson.attentionHereTitle = 15;
		countJson.attentionHereSentence = ${common['gc.webdata.attentionHere.maxLength']};
	}

	if (sizeKbn == ${f:h(SIZE_D)}) {
		countJson.sentence1 = ${common['gc.webdata.sentence1.maxLength4']};
		countJson.sentence2 = ${common['gc.webdata.sentence2.maxLength4']};
		countJson.sentence3 = ${common['gc.webdata.sentence3.maxLength']};
		countJson.captionA = ${common['gc.webdata.caption.maxLength']};
		countJson.captionB = ${common['gc.webdata.caption.maxLength']};
		countJson.captionC = ${common['gc.webdata.caption.maxLength']};
		countJson.attentionHereTitle = 15;
		countJson.attentionHereSentence = ${common['gc.webdata.attentionHere.maxLength']};
	}

	if (sizeKbn == ${f:h(SIZE_E)}) {
		countJson.sentence1 = ${common['gc.webdata.sentence1.maxLength5']};
		countJson.sentence2 = ${common['gc.webdata.sentence2.maxLength5']};
		countJson.sentence3 = ${common['gc.webdata.sentence3.maxLength']};
		countJson.captionA = ${common['gc.webdata.caption.maxLength']};
		countJson.captionB = ${common['gc.webdata.caption.maxLength']};
		countJson.captionC = ${common['gc.webdata.caption.maxLength']};
		countJson.attentionHereTitle = 15;
		countJson.attentionHereSentence = ${common['gc.webdata.attentionHere.maxLength']};
	}

	// 文字数チェックを切り替える
	for (var key in countJson) {
		var $obj = $('#' + key + 'TextId');
		if ($.isEmptyObject($obj)) continue;
		$obj.validationEngine('updatePromptsPosition');
		$obj.removeClass(function(index, className) {
		    return (className.match(/validate\[maxSize\[\d+\]\]/g) || []).join(' ');
		});
		if (countJson[key] > 0) {
			$obj.addClass('validate[maxSize[' + countJson[key] + ']]');
		}
	}


	changeCharCountAll(countJson);
}


/**
 * 全てのテキストフォームのカウントを変更します。
 */
function changeCharCountAll(json) {

	for (var i in json) {
		changeCharCount(i);
	}
}

/**
 * targetNameのテキストフォームのカウントを変更します。
 */
function changeCharCount(targetName) {
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
}

/**
 * フォームからonkeyup、onchangeで呼び出されるテキストカウントです。
 */
function countTextChar(obj) {
	changeCharCount(obj.name);
}

function getCount(str) {
	var replacedStr = str.replace(/\r\n/g, "rn");
	replacedStr = replacedStr.replace(/\r|\n/g, "rn");
	return replacedStr.length;
}

var ajaxSending = false;


/**
* IP電話番号を取得します。
*/
function getIPPhoneNo() {

	if (ajaxSending) {
		return ;
	}
	ajaxSending = true;
	var url = '${f:url('/webdata/edit/ipPhone')}';

	// 各フィールドから値を取得してJSONデータを作成
    var data = {
    	phoneNo1: $("input[name='phoneNo1']").val(),
    	phoneNo2: $("input[name='phoneNo2']").val(),
    	phoneNo3: $("input[name='phoneNo3']").val(),
    	ipPhoneNo1: $("input[name='ipPhoneNo1']").val(),
    	ipPhoneNo2: $("input[name='ipPhoneNo2']").val(),
    	ipPhoneNo3: $("input[name='ipPhoneNo3']").val(),
    	id: $("input[name='hiddenId']").val(),
    	customerId: $("#hiddenCustomerId").val(),

    };

	$.ajax({
		url: url,
		data:data,
        contentType: 'application/json', // リクエストの Content-Type
        dataType: "json",           // レスポンスをJSONとしてパースする
		success: function(msg){

			var len = msg.length;
			for(var i in msg) {
				var index = parseInt(i) + 1;
				var inputStr = "input[name='ipPhoneNo" + (index) + "']";
		    	$(inputStr).val(msg[i].ipTel);

				var spanStr = "#spanIpPhoneNo" + (index);

		    	$(spanStr).html(msg[i].ipTel);

				for (var index = 1; index <= 3; index++) {
					var inputStr = "input[name='phoneNo" + (index) + "']";
					var input = $(inputStr).val();

					if (input == "" || input == null) {
						var spanStr = "#spanIpPhoneNo" + (index);
				    	$(spanStr).html("");
					}
				}
			}
		},
		error: function(msg) {
			alert( "IP電話番号の取得に失敗しました。");

            return true;
		},
		complete: function(msg) {
			ajaxSending = false;
		},
	});
}
</script>

<script type="text/javascript">

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

</script>

<script type="text/javascript">
$(function(){
	setTopInterviewUrlValidate();

	$("#mtBlogId").on('change', function(){
		setTopInterviewUrlValidate();
	});

	function setTopInterviewUrlValidate(){
		var selectVal = $("#mtBlogId").val();
		if(selectVal != '') {
			$("#topInterviewUrl").addClass("validate[required]");
		}else{
			$("#topInterviewUrl").removeClass("validate[required]");
		}
	}
});
</script>

<script type="text/javascript">
$(function(){
	// エリアコード取得
	var areaValue = $("#areaCd").val();
	// 西日本の場合非表示
	if(areaValue >= 3) {
		$("#ipPhoneTr").hide();
	}
});
</script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#attentionShopStartDate").datepicker({dateFormat: 'yy/mm/dd'});
		$("#attentionShopEndDate").datepicker({dateFormat: 'yy/mm/dd'});
		$("#searchPreferentialStartDate").datepicker({dateFormat: 'yy/mm/dd'});
		$("#searchPreferentialEndDate").datepicker({dateFormat: 'yy/mm/dd'});
	});
</script>
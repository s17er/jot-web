<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<c:set var="MATERIAL_LOGO" value="<%=MTypeConstants.ShopListMaterialKbn.LOGO%>" scope="request" />
<c:set var="MATERIAL_KBN_MAIN_1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1%>" scope="request" />
<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" scope="request"/>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<c:set var="NOT_DISPLAY" value="<%= MTypeConstants.ShopListDisplayFlg.NOT_DISPLAY %>" scope="page"/>
<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<gt:typeList name="seatList" typeCd="<%=MTypeConstants.SeatKbn.TYPE_CD %>"/>
<gt:typeList name="salesPerCustomerKbn" typeCd="<%=MTypeConstants.SalesPerCustomerKbn.TYPE_CD %>"/>
<gt:typeList name="workCharacteristicList" typeCd="<%=MTypeConstants.WorkCharacteristicKbn.TYPE_CD %>"/>
<gt:typeList name="shopCharacteristicList" typeCd="<%=MTypeConstants.ShopCharacteristicKbn.TYPE_CD %>"/>
<gt:typeList name="companyCharacteristicList" typeCd="<%=MTypeConstants.CompanyCharacteristicKbn.TYPE_CD %>"/>
<gt:areaList name="areaList" blankLineLabel="--" />
<gt:typeList name="selectJobOfferList" typeCd="<%=MTypeConstants.JobOfferFlg.TYPE_CD %>"/>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/js/lightbox/css/lightbox.min.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/lightbox/js/lightbox.min.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/jquery/jquery.leanModal.min.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/preview.js"></script>
<script type="text/javascript">
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
			$("form#MaxRowSelect").submit();
		}
	}

	var changeDispOrder = function(entityId) {
		var toDispOrder = $("#toDispOrder_" + entityId).val();
		$("#targetEntityId").val(entityId);
		$("#targetDispOrder").val(toDispOrder);
		$("form#displayChangeForm").submit();
	};

	var showPreview = function() {
		window.open('${viewDto.previewUrl}', 'preview', 'width=1280,height=600');
	};

	$(function(){
		$('#checkBoxAll').click(function(){
			if(this.checked){
				$('.idCheckBox').attr('checked','checked');
			}else{
				$('.idCheckBox').removeAttr('checked');
			}
		});

		// 「この条件で検索するボタン」押されたときに検索する
		$('#search').click(function(){
			$("#searchButton").trigger("click");
		});

		// メディア選択のモーダル準備
	    $( '.select_media_btn').leanModal({
	        top: 50,                     // モーダルウィンドウの縦位置を指定
	        overlay : 0.5,               // 背面の透明度
	        closeButton: ".modal_close"  // 閉じるボタンのCSS classを指定
	    });

		var mediaKbn;
		var shopListId;

		$('.select_media_btn').click(function(){
			mediaKbn = $(this).data('media-kbn');
			shopListId = $(this).data('shop-list-id');
			console.info('mediaKbn',mediaKbn);
			console.info('shopListId',shopListId);
		});

		// メディア選択されたときのイベント
		$('.tile li').click(function(){
			var value = $(this).attr('data-media-id');
			console.log('value:' + value);

			var imageName = $(this).children("span").text();
			console.log('imageName:' + imageName);

			var imageSrc = $(this).children("img").attr('src');

			var fd = new FormData();
			fd.append('customerImageId', value);
			fd.append('shopListId', shopListId);
			fd.append('shopListMaterialKbn', mediaKbn);

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
				console.info(data);
				// エラーが発生した場合は警告
				if(data.error) {
					alert('登録に失敗しました。再度処理を行ってください。');
					// モーダル閉じる
					$('.modal_close').click();
					return;
				}
				var imgTag = '<a href="' + imageSrc +'" data-lightbox="photo">'
				+ '<img class="thumbnail_image" src="' + imageSrc +'" data-media-id="' + value + '"  />'
				+ '</a>';

				// サムネイルの表示
				$('#image_' + shopListId + '_' + mediaKbn).html(imgTag);

				console.info($('#image_' + shopListId + '_' + mediaKbn).html());

				// 画像選択ボタンを隠す
				$('#select_media_' + shopListId + '_' + mediaKbn).hide();

				// 削除ボタンを表示
				$("#delete_media_" + shopListId + '_' + mediaKbn).show();

				mediaKbn = "";
				shopListId = "";

				// モーダル閉じる
				$('.modal_close').click();

			}).fail(function(jqXHR, textStatus, errorThrown) {
				alert('通信に失敗しました。再度処理を行ってください。');
				// モーダル閉じる
				$('.modal_close').click();
				console.log(textStatus);
				console.log(errorThrown);
			});

		});

		$('.delete_media_btn').click(function(){

			if (!confirm('削除しますか？')) {
				return;
			}

			var delMediaKbn = $(this).data('media-kbn');
			var delShopListId = $(this).data('shop-list-id');

			var fd = new FormData();
			fd.append('shopListId', delShopListId);
			fd.append('shopListMaterialKbn', delMediaKbn);

		    // 削除
			$.ajax({
				url : "${f:url(deleteImgAjaxPath)}",
				type : "POST",
				data : fd,
				cache : false,
				contentType : false,
				processData : false,
				dataType : "json",

			}).done(function(data) {
				console.info(data);
				// エラーが発生した場合は警告
				if(data.error) {
					alert('削除に失敗しました。再度処理を行ってください。');
					return;
				}

				// サムネイルの削除
				$('#image_' + delShopListId + '_' + delMediaKbn).html("");

				// 画像選択ボタンを隠す
				$('#select_media_' + delShopListId + '_' + delMediaKbn).show();

				// 削除ボタンを表示
				$("#delete_media_" + delShopListId + '_' + delMediaKbn).hide();

			}).fail(function(jqXHR, textStatus, errorThrown) {
				alert('通信に失敗しました。再度処理を行ってください。');
				console.log(textStatus);
				console.log(errorThrown);
			});
		});

	});

</script>
<style>
.thumbnail_image {
	width:100%;
	margin-bottom: 5px !important;
}
.delete_media_btn {
	min-width: 24px;
}


/*
系列店舗登録・編集　フォーム修正（JOT作業分）
---------------------------------------------------------------------------------*/
/*.shopTable {
	table-layout:fixed;
}

.shopTable th{
	box-sizing: border-box;

}*/

.shopOrder {
	box-sizing: border-box;
}

.btn {
    display: inline-block;
    padding: .4em 2em;
    text-decoration: none;
    text-align: center;
    border-radius: 3px;
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
}

.btn-primary {
    background-color: #ff9900;
    color: #fff;
    border: 1px solid #d88100;
}

.shopTable .shopLineheight {
	line-height: 2em;
}

.media_modal {
    background: none repeat scroll 0 0 #FFFFFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.7);
    display: none;
    padding: 5px 10px;
    width: 80%;
    text-align: left;
    overflow-y: auto;
    max-height: 90%;
}

.cmnSttl {
    font-size: 108%;
    padding: 4px 10px;
    margin: 10px 0;
    color: #fff;
    background: #ff9900;
    text-align: left;
    font-weight: bold;
}

ul.tile {
    list-style: none;
    padding: 0;
    width: 100%;
}

.flex_container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
}

ul.tile li {
    padding: 0 0 10px 0;
    margin: 0;
    width: 202px;
    overflow-wrap: break-word;
    list-style-type: none;
}

img.media_image {
    width: 200px;
    height: 200px;
    background-color: #ececec;
    object-fit: contain;
}

#media_close {
    padding-top: 20px;
    text-align: center;
}

.btn {
    display: inline-block;
    padding: .4em 2em;
    text-decoration: none;
    text-align: center;
    border-radius: 3px;
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
}

.basic_button {
    color: #333 !important;
    border: none;
    background: #EEE !important;
    border: 1px solid #ccc;
}

#lean_overlay {
    position: fixed;
    z-index: 100;
    top: 0px;
    left: 0px;
    height: 100%;
    width: 100%;
    background: #000;
    display: none;
}

</style>
<div id="main">
				<div id="wrap_web-shoplist">
					<div class="page_back">
						<a onclick="location.href='${f:url('/shopList/')}'" id="btn_back">戻る</a>
					</div>
					<h2>店舗一覧｜管理</h2>
					<p class="explanation">
						条件を選んで「検索する」ボタンを押して下さい。<br>
						チェックボックスより店舗を選択し「表示」「非表示」の設定を行うと求人原稿に反映されます。
					</p>
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

					<s:form action="${f:h(actionPath)}changeDispOrder" styleId="displayChangeForm">
						<html:hidden property="changeDisplayId" styleId="targetEntityId"/>
						<html:hidden property="toDisplayOrder" styleId="targetDispOrder"/>
					</s:form>


					<div id="wrap_masc_content">
						<div class="tab_area">
							<div class="tab_contents tab_active" id="detail_list_Information">
								<div class="detail_list_area">
									<s:form action="${f:h(actionPath)}" styleId="searchForm">
										<div class="narrowing_area">
											<p class="mailcount">
											全 ${f:h(pageNavi.allCount)} 件中 / ${f:h(pageNavi.minDispNum)} 件 〜 ${f:h(pageNavi.maxDispNum)} 件
											</p>
											<div class="s_conditions cond_open">表示条件の設定</div>
											<div class="narrowing_check">
												<div class="narr_number">
													表示件数
													<ul>
														<li>
															<html:radio property="maxRow" value="20" styleId="twenty" checked="checked"/><label for="twenty">20件</label>
														</li>
														<li>
															<html:radio property="maxRow" value="50" styleId="fifty"/><label for="fifty">50件</label>
														</li>
														<li>
															<html:radio property="maxRow" value="100" styleId="onehundred"/><label for="onehundred">100件</label>
														</li>
													</ul>
												</div>
												<div class="narr_shopname">
													<span>店舗名</span><br>
													<html:text property="where_shopName" placeholder="〇〇店" />
												</div>
												<div class="narr_shopaddress">
													<span>住所</span><br>
													<html:text property="where_address" placeholder="〇〇区"/>
												</div>
												<div class="narr_shopindustry">
													<span>業態</span><br>
													<div class="selectbox">
														<html:select property="where_industryKbn">
															<html:option value="">すべて</html:option>
															<html:optionsCollection name="selectIndustryList"/>
														</html:select>
													</div>
												</div>
												<div class="accordion_btn shoplist__search__btn" style="margin: 0 auto 0 auto!important">
                                                    <html:submit property="search" value="検索" style="display:none" styleId="searchButton"/>
													<button class="btn_accordion_search" id="search" type="button" style="margin-top:20px">
														<span>この条件で検索する</span>
													</button>
												</div>
											</div>
										</div>
									</s:form>

									<c:if test="${existDataFlg && shopList.size() > 0}">
									<div id="wrap_result">
										<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
											<table cellpadding="0" cellspacing="0" border="0" class="width_fix number_table " style="display:none;">
												<tr>
													<td>${pageNavi.allCount}件検索されました。</td>
													<td class="pull_down">
														<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
															<gt:maxRowList name="maxRowList"
																value="${common['gc.shopList.maxRow']}"
																suffix="${common['gc.maxRow.suffix']}" />
															<html:select property="maxRow"
																onchange="changeMaxRow('selectForm');">
																<html:optionsCollection name="maxRowList" />
															</html:select>
														</s:form>
													</td>
												</tr>
											</table>
										</s:form>
											<table cellpadding="0" cellspacing="0" border="0"
												class="width_fix number_table">
												<tbody>
													<tr>
														<td>
															<input type="button" name="" value="店舗一覧を確認"
																onclick="showPreview();">
														</td>
													</tr>
												</tbody>
											</table>
											<html:errors/>
											<!--
											<div class="wrap_btn_left">
												<input type="button" name="" value="店舗一覧情報確認" onclick="showPreview();">
											</div>
											-->
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
											<s:form action="${f:h(actionPath)}" styleId="listForm">
												<table cellpadding="0" cellspacing="0" border="0" class="all_table">
													<tbody>
														<tr>
															<th class="select_all" width="10px">
																<input type="checkbox" id="allcheck"
																	onclick="handleAllBoth(this);" class="checkBoxAll">
																<label for="allcheck"></label>
															</th>
															<th class="posi_center" width="50px">順番</th>
															<th width="75px">ID</th>
															<th width="145px">店舗名</th>
															<th width="165px">住所</th>
															<th width="125px">業態</th>
															<th class="posi_center" width="100px">メイン画像</th>
															<th class="posi_center" width="100px">編集</th>
														</tr>

														<c:forEach items="${shopList}" var="t">
															<c:choose>
															<c:when test="${NOT_DISPLAY eq f:h(t.dispFlg)}"><tr style="background-color: #CCCCCC"></c:when>
															<c:otherwise><tr></c:otherwise>
															</c:choose>
															<td class="table-checkbox">
																<html:multibox property="changeIdArray" value="${f:h(t.id)}" styleClass="lumpSendCheck" styleId="${f:h(t.id)}" /><label for="${f:h(t.id)}" class ="lumpSendCheck"></label>
															</td>
															<td>
																<input type="text" id="toDispOrder_${f:h(t.id)}" value="${f:h(t.dispOrder)}" class="shopOrder" />
																<input type="button" value="変更" onclick="changeDispOrder(${f:h(t.id)});" />
															</td>
															<td>${f:h(t.id)}</td>
															<td>${f:h(t.shopName)}</td>
															<td>${f:h(t.address1)}</td>
															<td>
																${f:label(t.industryNameList[0], selectIndustryList, 'value', 'label')}
																<c:if test="${t.industryNameList[1] != null }">
																	／${f:label(t.industryNameList[1], selectIndustryList, 'value', 'label')}
																</c:if>
															</td>
															<td class="posi_center">
																<span id="image_${t.id}_${MATERIAL_KBN_MAIN_1}">
																	<c:if test="${t.materialExistsDto.isMain1ExistFlg}">
																		<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, t.id))}" data-lightbox="logo" title="メイン"><img class="thumbnail_image" alt="メイン" src="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_KBN_MAIN_1, t.id))}" /></a>
																	</c:if>
																</span>
																<span id="delete_media_${t.id}_${MATERIAL_KBN_MAIN_1}" style="${t.materialExistsDto.isMain1ExistFlg ? '' : 'display:none'}"
																		class="btn basic_button delete_media_btn"
																		data-shop-list-id="${t.id}" data-media-kbn="${MATERIAL_KBN_MAIN_1}">削除</span>
																<span id="select_media_${t.id}_${MATERIAL_KBN_MAIN_1}" class="btn btn-primary select_media_btn" style="${t.materialExistsDto.isMain1ExistFlg ? 'display:none' : ''}"
																	 data-shop-list-id="${t.id}" data-media-kbn="${MATERIAL_KBN_MAIN_1}" href="#media_modal" >画像BOX</span>
															</td>
															<td class="posi_center shopLineheight">
																<a href="${f:url(t.detailPath)}">編集</a>
															</td>
														</c:forEach>
													</tbody>
												</table>
												<div id="media_modal" class="media_modal">
													<h2 class="cmnSttl">画像選択</h2>
													<ul class="flex_container tile">
														<c:forEach var="image" items="${viewDto.imageList}">
														<li data-media-id="${f:h(image.id)}">
															<img class="media_image" src="${f:url(image.filePath)}" />
															<span class="thumbnail_name">${f:h(image.fileName)}</span>
														</li>
														</c:forEach>
													</ul>
													<div id="media_close">
														<a class="modal_close btn basic_button">閉じる</a>
													</div>
												</div>
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
												<div class="wrap_btn">
													<html:submit property="doLumpChangeDisplayOn" value="表示"/>
													<html:submit property="doLumpChangeDisplayOff" value="非表示"/>
													<html:submit property="doLumpDelete" value="一括削除" onclick="return confirm('削除してよろしいですか？')? true : false;"/>
												</div>
										</s:form>
									</div>
									</c:if>

									<div class="wrap_btn imexport">
										<s:form action="${f:h(actionPath)}importCsv" styleId="csvForm" enctype="multipart/form-data" style="display:inline;">
											<input type="file" name="csvFormFile" value="参照" />
											<input type="button" value="インポート" onclick="uploadCsv();"/>
										</s:form>
										<s:form action="${f:h(actionPath)}" styleId="csvForm" style="display:inline;" >
											<html:submit property="export" value="エクスポート" />
										</s:form>

										<p class="explanation">
											インポート 　･･･ CSVファイルをご用意いただき、複数の店舗を登録・編集ができます。<br>
											エクスポート ･･･ チェックを入れたデータをCSVファイルでダウンロードができます。<br><br>
											※CSVファイルより店舗の登録を行う場合は、エクスポートしたCSVファイルを書き換えご利用ください。
										</p>
									</div>
									<div class="csvGuide">
										<p class="Stadd_link">CSVファイル 項目表は<a href="${SHOP_CONTENS}/項目表.xlsx">こちら</a></p>
									</div>
									<s:form action="${f:h(actionPath)}importCsv" styleId="csvForm" enctype="multipart/form-data">
										<input type="file" name="csvFormFile" value="参照" id="csvImport" size="50"
											style="display:none;" onchange="uploadCsv();" />&nbsp;
										<!--<html:file property="importCsvFileName" styleId="csvImport" style="display:none;"  onchange="uploadCsv();"/>-->
									</s:form>
								</div>
							</div>
						</div>
					</div>
					</div>
</div>
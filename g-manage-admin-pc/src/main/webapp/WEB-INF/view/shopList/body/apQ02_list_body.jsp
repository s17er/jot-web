<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/style2.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/shared2.css" />
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.entity.MPrefectures"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<c:set var="SHUTOKEN_AREA" value="<%= MAreaConstants.AreaCd.SHUTOKEN_AREA %>" scope="request"/>
<c:set var="NOT_DISPLAY" value="<%= MTypeConstants.ShopListDisplayFlg.NOT_DISPLAY %>" scope="page"/>
<c:set var="MATERIAL_LOGO" value="<%=MTypeConstants.ShopListMaterialKbn.LOGO%>" scope="request" />
<c:set var="MATERIAL_KBN_MAIN_1" value="<%=MTypeConstants.ShopListMaterialKbn.MAIN_1%>" scope="request" />
<gt:typeList name="selectIndustryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
<gt:typeList name="seatList" typeCd="<%=MTypeConstants.SeatKbn.TYPE_CD %>"/>
<gt:typeList name="salesPerCustomerKbn" typeCd="<%=MTypeConstants.SalesPerCustomerKbn.TYPE_CD %>"/>
<gt:typeList name="workCharacteristicList" typeCd="<%=MTypeConstants.WorkCharacteristicKbn.TYPE_CD %>"/>
<gt:typeList name="shopCharacteristicList" typeCd="<%=MTypeConstants.ShopCharacteristicKbn.TYPE_CD %>"/>
<gt:typeList name="companyCharacteristicList" typeCd="<%=MTypeConstants.CompanyCharacteristicKbn.TYPE_CD %>"/>
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"/>
<gt:typeList name="JobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>
<gt:typeList name="SaleryStructureKbnList" typeCd="<%=MTypeConstants.NewSaleryStructureKbn.TYPE_CD %>"/>

<gt:areaList name="areaList" />
<%-- CSS,JSのインクルード --%>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/lightbox/css/lightbox.min.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/lightbox/js/lightbox.min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.leanModal.min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/preview.js" ></script>
<script type="text/javascript">
	var importButtonClick = function() {
		$('#csvImport').click();
	};

	var importJobButtonClick = function() {
		$('#jobCsvImport').click();
	};

	var uploadCsv = function() {
		$('#csvForm').submit();
	};

	var uploadJobCsv = function() {
		$('#jobCsvForm').submit();
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
</style>

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
	<s:form action="${f:h(actionPath)}changeDispOrder" styleId="displayChangeForm">
		<html:hidden property="changeDisplayId" styleId="targetEntityId"/>
		<html:hidden property="toDisplayOrder" styleId="targetDispOrder"/>
	</s:form>
	<s:form action="${f:h(actionPath)}">
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
			<tr>
				<th>店舗名</th>
				<td>
					<html:text property="where_shopName" />
				</td>
			</tr>
			<tr>
				<th>業態</th>
				<td>
					<html:select property="where_industryKbn">
						<html:option value="">--</html:option>
						<html:optionsCollection name="selectIndustryList"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<th>住所</th>
				<td><html:text property="where_address" /></td>
			</tr>
			<tr>
				<gt:typeList name="searchOpenDateFlgList" typeCd="<%=MTypeConstants.searchOpenDateFlg.TYPE_CD %>" blankLineLabel="--"/>
				<th>オープン日</th>
				<td>
					<html:select property="where_searchOpenDateFlg">
						<html:optionsCollection name="searchOpenDateFlgList"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<th class="bdrs_bottom">フリーワード</th>
				<td class="release bdrs_bottom">
					<html:text property="where_keyword" />
				</td>
			</tr>
		</table>
		<div class="wrap_btn">
			<html:submit property="search" value="検索" />
		</div>
	</s:form>
		<c:if test="${existDataFlg eq false}">
		<b>店舗登録・編集用 CSV</b><br>
			<div class="wrap_btn">
				<s:form action="${f:h(actionPath)}importCsv" styleId="csvForm" enctype="multipart/form-data" style="display:inline;">
					<input type="file" name="csvFormFile" value="参照" />
					<input type="button" value="CSVインポート" onclick="uploadCsv();" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%--<input type="button" onclick="importButtonClick();" value="CSVインポート" /> --%>
				</s:form>
				<s:form action="${f:h(actionPath)}" styleId="csvForm" style="display:inline;" >
					<html:submit property="export" value="CSVエクスポート" />
				</s:form>
			</div>
			<b>募集職種・給与上書き用 CSV</b><br>
			<div class="wrap_btn">
				<s:form action="${f:h(actionPath)}importJobCsv" styleId="jobCsvForm" enctype="multipart/form-data" style="display:inline;">
					<input type="file" name="jobCsvFormFile" value="参照" />
					<input type="button" value="CSVインポート" onclick="uploadJobCsv();" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%--<input type="button" onclick="importButtonClick();" value="CSVインポート" /> --%>
				</s:form>
				<s:form action="${f:h(actionPath)}" styleId="csvForm" style="display:inline;" >
					<html:submit property="exportJob" value="CSVエクスポート" />
				</s:form>
			</div>
		</c:if>

	<c:if test="${existDataFlg}">
		<div id="wrap_result">
			<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
				<table cellpadding="0" cellspacing="0" border="0" class="number_table">
					<tr>
						<td>${pageNavi.allCount}件検索されました。</td>
						<td class="pull_down">
								<s:form action="${actionMaxRowPath}" styleId="MaxRowSelect">
									<gt:maxRowList name="maxRowList" value="${common['gc.shopList.maxRow']}" suffix="${common['gc.maxRow.suffix']}" />
										<html:select property="maxRow" onchange="changeMaxRow('selectForm');">
										<html:optionsCollection name="maxRowList" />
										</html:select>
								</s:form>
						</td>
					</tr>
				</table>
			</s:form>
			<hr />

			<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
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
			--></td>

			</tr>
		</table>
		<!-- #page# -->

		<div class="wrap_btn_left">
			<html:button property="" onclick="showPreview();" value="${f:h(LABEL_SHOPLIST)}確認" />
		</div>
		<s:form action="${f:h(actionPath)}" styleId="listForm">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table shopTable">
				<tr>
					<th class="posi_center " width="10px" ><input type="checkbox" id="checkBoxAll" /></th>
					<th class="posi_center " width="30px">ID</th>
					<th class="posi_center " width="70px">順番</th>
					<th class="posi_center">店舗名</th>
					<th class="posi_center " width="50px">業態</th>
					<th class="posi_center">住所</th>
					<th class="posi_center " width="70px">メイン画像</th>
					<th class="posi_center " width="70px">ロゴ画像</th>
					<th class="posi_center  bdrs_right" width="50px">詳細</th>
				</tr>
				<c:forEach items="${shopList}" var="t">
					<c:choose>
					<c:when test="${NOT_DISPLAY eq f:h(t.dispFlg)}"><tr style="background-color: #CCCCCC"></c:when>
					<c:otherwise><tr></c:otherwise>
					</c:choose>
						<%-- IDのチェックボックス--%>
						<td class="posi_center"><html:multibox property="changeIdArray" value="${f:h(t.id)}" styleClass="idCheckBox" /> </td>
						<%-- ID --%>
						<td class="posi_center  ${classStr}">${f:h(t.id)}</td>
						<%-- 順番 --%>
						<td class="${classStr}"><input type="text" id="toDispOrder_${f:h(t.id)}" value="${f:h(t.dispOrder)}" style="max-width: 50px;" class="shopOrder" /><br><input type="button" value="変更" onclick="changeDispOrder(${f:h(t.id)});" /></td>
						<%-- 店舗名 --%>
						<td class="${classStr}" style="overflow: hidden; white-space: normal; max-width: 120px;">${f:h(t.shopName)}</td>
						<%-- 業態1 --%>
						<td class="posi_center  ${classStr}">
							${f:label(t.industryNameList[0], selectIndustryList, 'value', 'label')}<br>
							${f:label(t.industryNameList[1], selectIndustryList, 'value', 'label')}
						</td>
						<%-- 住所 --%>
						<td class="${classStr}" style="overflow: hidden; white-space: normal; max-width: 120px">${f:h(t.address1)}</td>
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
						<td class="posi_center">
							<span id="image_${t.id}_${MATERIAL_LOGO}">
								<c:if test="${t.materialExistsDto.isLogoExistFlg}">
									<a href="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_LOGO, t.id))}" data-lightbox="logo" title="ロゴ"><img class="thumbnail_image" alt="ロゴ" src="${f:url(gf:makePathConcat2Arg(actionMaterialPath, MATERIAL_LOGO, t.id))}" /></a>
								</c:if>
							</span>
							<span id="delete_media_${t.id}_${MATERIAL_LOGO}" style="${t.materialExistsDto.isLogoExistFlg ? '' : 'display:none'}"
									class="btn basic_button delete_media_btn"
									data-shop-list-id="${t.id}" data-media-kbn="${MATERIAL_LOGO}">削除</span>
							<span id="select_media_${t.id}_${MATERIAL_LOGO}" class="btn btn-primary select_media_btn" style="${t.materialExistsDto.isLogoExistFlg ? 'display:none' : ''}"
								 data-shop-list-id="${t.id}" data-media-kbn="${MATERIAL_LOGO}" href="#media_modal" >画像BOX</span>
						</td>
						<%-- 詳細 --%>
						<td class="posi_center  bdrs_right shopLineheight ${classStr}">
							<a href="${f:url(t.detailPath)}">詳細</a><br>
						</td>
					</tr>
				</c:forEach>
			</table>
			<hr />
			<%-- メディアモーダル ここから --%>
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
			<%-- メディアモーダル ここまで --%>
			<div class="wrap_btn">
			<html:submit property="doLumpChangeDisplayOn" value="表示" />
			<html:submit property="doLumpChangeDisplayOff" value="非表示" />
			<html:submit property="doLumpDelete" value="一括削除" onclick="return confirm('削除してよろしいですか？')? true : false;" />
			</div>
			<!-- #page# -->
		<table cellpadding="0" cellspacing="0" border="0" class="page">
			<tr>
			<td><!--
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
			--></td>

			</tr>
		</table>
		<!-- #page# -->
		</s:form>
			<b>店舗登録・編集用 CSV</b><br>
			<div class="wrap_btn">
					<s:form action="${f:h(actionPath)}importCsv" styleId="csvForm" enctype="multipart/form-data" style="display:inline;">
					<input type="file" name="csvFormFile" value="参照" />
					<input type="button" value="CSVインポート" onclick="uploadCsv();" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</s:form>
				<s:form action="${f:h(actionPath)}" styleId="csvForm" style="display:inline;">
					<html:submit property="export" value="CSVエクスポート" />
				</s:form>
			</div>
			<b>募集職種・給与上書き用 CSV</b><br>
			<div class="wrap_btn">
					<s:form action="${f:h(actionPath)}importJobCsv" styleId="jobCsvForm" enctype="multipart/form-data" style="display:inline;">
					<input type="file" name="jobCsvFormFile" value="参照" />
					<input type="button" value="CSVインポート" onclick="uploadJobCsv();" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</s:form>
				<s:form action="${f:h(actionPath)}" styleId="csvForm" style="display:inline;">
					<html:submit property="exportJob" value="CSVエクスポート" />
				</s:form>
			</div>
		</div>
	</c:if>
	<ul class="shoplist_csvsample_lists">
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				業態表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${selectIndustryList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				席数区分表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${seatList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				客単価区分表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${salesPerCustomerKbn}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				仕事の特徴表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${workCharacteristicList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				職場・会社の特徴表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${shopCharacteristicList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				店舗の特徴表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${companyCharacteristicList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				雇用形態表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${employPtnKbnList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				職種表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${JobKbnList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
		<li class="shoplist_csvsample_list">
			<div class="shoplist_csvsample_list_ttl">
				給与体系表
			</div>
			<select size="4" class="shoplist_csvsample_list_select">
				<c:forEach items="${SaleryStructureKbnList}" var="t">
					<option>${f:h(t.value)}：${f:h(t.label)}</option>
				</c:forEach>
			</select>
		</li>
	</ul>

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
</div>
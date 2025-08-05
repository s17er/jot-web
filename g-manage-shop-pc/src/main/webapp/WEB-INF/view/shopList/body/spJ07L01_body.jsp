<%--
顧客画像管理
 --%>
<%@page pageEncoding="UTF-8"%>

<c:set var="UP_IMAGE_PATH" value="/customerImage/list/upImage" scope="page" />
<c:set var="DEL_IMAGE_PATH" value="/customerImage/list/delImage" scope="page" />

<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/js/lightbox/css/lightbox.min.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/lightbox/js/lightbox.min.js"></script>



<!-- #main# -->
<div id="main">

				<div id="wrap_web-shoplist">
					<div class="page_back">
						<a onclick="location.href='${f:url('/shopList/')}'" id="btn_back">戻る</a>
					</div>
					<h2>${f:h(pageTitle)}</h2>
					<p class="explanation">
						画像の管理を行います。アップロードする画像をドロップ、またはクリックして画像を選択してください。
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
					<div id="wrap_masc_content">
						<div class="tab_area">
						    <html:errors/>
							<div class="tab_contents tab_active" id="detail_list_Information">
								<div class="img_detail_area">
									<div id="image_upload_section">
										<div id="drop_area" ondragover="onDragOver(event)" ondrop="onDrop(event)">
											<div>
												ここに画像をドラッグアンドドロップ、またはクリックして画像を選択してください。<br>複数ファイルも同時にアップロードできます。（jpegのみ）
											</div>
											<input type="button" name="upImage" value="ファイルを選択">
										</div>
										<input type="file" id="fileSelect" onchange="onChange(event)" multiple style="display:none"/>

										<table id="image_list" class="all_table">
											<thead>
												<tr>
													<th>サムネイル画像</th>
													<th>ファイル名</th>
													<th width="50px">削除</th>
												</tr>
											</thead>
											<tbody>
											<c:forEach var="image" items="${imageList}">
												<tr id="image_${image.id}" class="item">
													<td>
														<a href="${f:url(image.filePath)}" title="${f:h(image.fileName)}" data-lightbox="photo">
															<img src="${f:url(image.filePath)}" width="130" />
														</a>
													</td>
													<td class="fileName">${f:h(image.fileName)}</td>
													<td><input type="button" value="削除" onclick="clickDelete(${image.id})" /></td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
</div>

<script>
	// File APIに対応していない場合はエリアを隠す
	if (!window.File) {
		document.getElementById('drop_area').style.display = "none";
	}

	// ブラウザ上でファイルを展開する挙動を抑止
	function onDragOver(event) {
		event.preventDefault();
	}

	// Drop領域にドロップした際のファイルのプロパティ情報読み取り処理
	function onDrop(event) {

		// ブラウザ上でファイルを展開する挙動を抑止
		event.preventDefault();

		// ドロップされたファイルのfilesプロパティを参照
		var files = event.dataTransfer.files;

		console.log("files:" + files.length);

		for (var i=0; i<files.length; i++) {
			// 一件ずつアップロード
			imageFileUpload(files[i]);
		}
	}

	// Drop領域クリック時の処理
	$('#drop_area').on('click', function() {
		$('#fileSelect').click();
	})

	// Drop領域クリック時のファイルプロパティ情報読み取り処理
	function onChange(event) {

		// ブラウザ上でファイルを展開する挙動を抑止
		event.preventDefault();

		// クリックされたファイルのfilesプロパティを参照
		var files = event.target.files;
		var len = Object.keys(files).length;
		console.log(files);
		console.log("files:" + Object.keys(files).length);

		for (var i=0; i<len; i++) {
			// 一件ずつアップロード
			imageFileUpload(files[i]);
		}
	}

	// ファイルを選択ボタンクリック時の処理
	$('#fileSelectBtn').on('click', function() {
		$('#fileSelect').click();
	})

	var template = ''
		+ '<tr id="image_%ID%" class="item">'
		+ '<td><a href="%PATH%" title="%FILE_NAME%" data-lightbox="photo"><img src="%PATH%" width="130" /></a></td>'
		+ '<td class="fileName">%FILE_NAME%</td>'
		+ '<td><input type="button" value="削除" onclick="clickDelete(%ID%)" /></td></tr>';

	// ファイルアップロード
	function imageFileUpload(f) {
		var formData = new FormData();
		formData.append('formFile', f);

		$.ajax({
			type: 'POST',
			contentType: false,
			processData: false,
			url: '${f:url(UP_IMAGE_PATH)}',
			data: formData,
			dataType: 'text',
			success: function(data) {
				console.log("success:" + data);
				var jsonObj = JSON.parse(data);

				if (jsonObj.errorFlg) {
					alert(jsonObj.message);
					return;
				}

				var a = template.replace(/%PATH%/g, "<%=request.getContextPath()%>" + jsonObj.imageDto.filePath);
				a = a.replace(/%FILE_NAME%/g, jsonObj.imageDto.fileName);
				a = a.replace(/%ID%/g, jsonObj.imageDto.id);

				// 同じ名前のファイルは消す
				$(".fileName").each(function(index) {
					var fileName = $(this).text();
					if (fileName == jsonObj.imageDto.fileName) {
						// 上書きされた画像の行を非表示にする
						$(this).parent().hide();
					}
				});

				$("#image_list tbody").append(a);
			},
			error: function(e) {
				console.log(e);
			}
		});
	}

	// 画像の削除
	function clickDelete(imageId) {
		if (window.confirm('完全に削除します。よろしいですか？')) {
			$.ajax({
				type: 'POST',
				contentType: false,
				processData: false,
				url: '${f:url(DEL_IMAGE_PATH)}/' + imageId,
				success: function(data) {
					console.log("success:" + data);
					$("#image_"+imageId).remove();
				},
				error: function(e) {
					alert("画像削除時にエラーが発生しました。");
				}
			});
		}
	}

</script>
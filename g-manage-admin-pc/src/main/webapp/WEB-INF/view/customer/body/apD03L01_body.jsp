<%--
顧客画像管理
 --%>
<%@page pageEncoding="UTF-8"%>

<c:set var="UP_IMAGE_PATH" value="/customerImage/list/upImage/${f:h(customerId)}" scope="page" />
<c:set var="DEL_IMAGE_PATH" value="/customerImage/list/delImage/${f:h(customerId)}/" scope="page" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/lightbox/css/lightbox.min.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/lightbox/js/lightbox.min.js"></script>

<style>
	#drop_area {
		height:150px;
		padding:10px;
		background-color: #EEE;
		border: #CCC dotted 2px;
		margin-bottom: 10px;
	}

	#drop_area div {
		height: 100%;
		line-height: 1.3em;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	table.image_list {
		*border-collapse: collapse; /* IE7 and lower */
		border-spacing: 0;
		width: 100%;
	}

	.zebra tbody tr:nth-child(even) {
		background: #ffedd3;
		-webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;
		-moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;
		box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;
	}

	#fileSelectBtn {
		height: 30px;
		width: 120px;
		margin-bottom: 20px;
	}
</style>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />


	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">画像の管理を行います。アップロードする画像をドロップ、またはクリックして画像を選択してください。</p>
	<hr />

	<html:errors/>

	<div class="container">
		<div id="image_upload_section">
			<div id="drop_area" ondragover="onDragOver(event)" ondrop="onDrop(event)">
				<div>ここに画像をドラッグアンドドロップ、またはクリックして画像を選択してください。（Jpegのみ）<br />複数ファイルも同時にアップロードできます。</div>
			</div>
		</div>
		<input type="file" id="fileSelect" onchange="onChange(event)" multiple style="display:none"/>
		<input type="button" id="fileSelectBtn" name="upImage" value="ファイルを選択" />
		<table id="image_list" class="cmn_table list_table image_list zebra">
			<thead>
				<tr>
					<th>サムネイル画像</th>
					<th>ファイル名</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="image" items="${imageList}">
					<tr id="image_${image.id}" class="item">
						<td>
							<a href="${f:url(image.filePath)}" title="${f:h(image.fileName)}" data-lightbox="photo">
								<img src="${f:url(image.filePath)}" alt="${f:h(image.fileName)}" width="130" />
							</a>
						</td>
						<td class="fileName">${f:h(image.fileName)}</td>
						<td><input type="button" value="削除" onclick="clickDelete(${image.id})" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br />
</div>

<script>
	// File APIに対応していない場合はエリアを隠す
	if (!window.File) {
		document.getElementById('image_upload_section').style.display = "none";
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
	$('#image_upload_section').on('click', function() {
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
						$(this).remove();
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
				url: '${f:url(DEL_IMAGE_PATH)}' + imageId,
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
<%@page pageEncoding="UTF-8"%>
<script>
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
</script>

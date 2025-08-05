<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/pattern.css" />

<!-- #main# -->
<div id="main">

<!--
	<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}">${f:h(pageTitle)}</h2>
	<hr />

	<p id="txt_comp">${f:h(defaultMsg) }</p>

	<div id="wrap_pattern">
	<h2>定型文の登録完了</h2>
 -->

	<div id="wrap_pattern">
		<h2>${f:h(pageTitle)}</h2>
		<div id="wrap_patt_content">
			<div class="tab_area">
				<div class="tab_contents tab_active" id="pattern_list">
					<div class="pattern_list_area">
						<p id="txt_comp">${f:h(defaultMsg) }</p>
					</div>
					<div class="wrap_btn">
						<input type="button" name="submit" value="定型文一覧へ戻る" id="btn_regist" onclick="location.href='${f:url('/pattern/list/')}';">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
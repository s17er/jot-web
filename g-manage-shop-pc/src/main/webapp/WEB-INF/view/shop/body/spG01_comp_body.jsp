<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">

	<div id="wrap_web-shoplist">
		<h2>${f:h(pageTitle)}</h2>
		<div id="wrap_masc_content">
			<div class="tab_area">
				<div class="tab_contents tab_active">
					<div class="detail_area">
						<p id="txt_comp">${f:h(defaultMsg)}</p>
					</div>
					<div class="wrap_btn">
						<input type="button" name="submit" value="企業情報･設定へ戻る" id="btn_regist"
							onclick="location.href='${f:url('/shop/')}';">
					</div>
				</div>
			</div>
		</div>
	</div>

</div>

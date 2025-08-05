<%@page pageEncoding="UTF-8"%>


<script type="text/javascript">
<!--
	window.resizeTo(1000,700);
// -->
</script>


<div id="main">

	<h2 title="エラー" class="title" id="error" >エラー</h2>
	<br />

	<html:errors />

	<html:messages id="msg" message="true">
		<div class="error">
			<ul>
				<li>
					<bean:write name="msg" ignore="true" />
	 			</li>
			</ul>
		</div>
	</html:messages>
	<br />
	<div class="wrap_btn">
		<input type="button" name="" value="このウィンドウを閉じる" onclick="window.close();" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);" id="btn_conf" />
	</div>
	<br />
</div>
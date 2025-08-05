<%@page pageEncoding="UTF-8"%>


<script type="text/javascript">
<!--
	window.resizeTo(1000,700);
// -->
</script>


<div id="main">

	<h2 title="エラー" class="errorTitle" id="title_error" >エラー</h2>
	<hr />
	<br />
	<div id="errMes" class="error">
		<html:errors />
	</div>

	<html:messages id="msg" message="true">
		<bean:write name="msg" ignore="true" />
	</html:messages>
	<br />
	<div class="wrap_btn">
		<input type="button" name="" value="このウィンドウを閉じる" onclick="window.close();" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
	</div>
	<br />
</div>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>

<gt:typeList name="deliveryTypeKbnList" typeCd="<%=MTypeConstants.deliveryTypeKbn.TYPE_CD %>"   />

<script type="text/javascript" src="${ADMIN_CONTENS}/ckeditor/ckeditor.js"></script>

<c:set var="HTML" value="<%=MTypeConstants.deliveryTypeKbn.HTML%>" />
<c:set var="TEXT" value="<%=MTypeConstants.deliveryTypeKbn.TEXT%>" />

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 title="${f:h(pageTitle1)}" class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<c:if test="${existDataFlg}">

		<!-- #wrap_form# -->
		<div id="wrap_form">
			<s:form action="${f:h(actionPath)}">
				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
					<tr>
						<th width="150">タイトル&nbsp;<span class="attention">※必須</span></th>
						<td><html:text property="mailMagazineTitle" /></td>
					</tr>
				<tr>
					<th width="150">配信形式&nbsp;<span class="attention">※必須</span></th>
					<td>
						<c:forEach items="${deliveryTypeKbnList}" var="t" varStatus="status">
							<label for="${status.index}"><html:radio property="deliveryType" value="${t.value}" styleId="${status.index}" style="width:initial"/>${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
					<tr>
						<th class="bdrs_bottom">本文&nbsp;<span class="attention">※必須</span></th>
					<c:choose>
						<c:when test="${deliveryType eq HTML }">
							<td class="bdrs_bottom" id="html">
								<html:textarea property="htmlBody" cols="60" rows="10" id="htmlBody"></html:textarea>
							</td>
							<td class="bdrs_bottom" id="text" style="display:none">
								<html:textarea property="textBody" cols="60" rows="10"></html:textarea>
							</td>
						</c:when>
						<c:when test="${deliveryType eq TEXT }">
							<td class="bdrs_bottom" id="html" style="display:none">
								<html:textarea property="htmlBody" cols="60" rows="10" id="htmlBody"></html:textarea>
							</td>
							<td class="bdrs_bottom" id="text">
								<html:textarea property="textBody" cols="60" rows="10"></html:textarea>
							</td>
						</c:when>
					</c:choose>
					</tr>
				</table>
				<hr />

				<div class="wrap_btn">
					<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</div>

			</s:form>
		</div>
		<!-- #wrap_form# -->
		<hr />
		<br />
		<div style="display: none">
			<input type="file" name="imageFile" id="imageFile"/>&nbsp;
		</div>
	</c:if>
	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->
<script>
$(function () {
    $('input[name="deliveryType"]').on('change', function () {
		if(this.value == 1) {
			$("#html").show();
			$("#text").hide();
		} else {
			$("#html").hide();
			$("#text").show();
		}
    });
    $("#imageFile").on('change', function(){
    	addMaterial();
    });
});
function addMaterial() {
	var file = $('#imageFile').prop("files")[0];

	// ファイルを選択していない場合は処理しない
	if (file == null || file == undefined) {
		return;
	}

	var fd = new FormData();
	fd.append('imageFile', file);


    // アップロード
	$.ajax({
		url : "${f:url(upImageAjaxPath)}",
		type : "POST",
		data : fd,
		cache : false,
		contentType : false,
		processData : false,
		dataType : "json",
	}).done(function(data) {
		var fileDirectory = data['fileDirectory'];
		if(null != fileDirectory) {
			var insert = "<br><img src='" + fileDirectory + "'><br>";
			$("#htmlBody").append(insert);
			CKEDITOR.instances.htmlBody.setData(CKEDITOR.instances.htmlBody.getData() + insert)
		}
	})
}
CKEDITOR.replace('htmlBody', {
//  uiColor: '#EEEEEE',
//  width:800,
//  height: 200,
});

CKEDITOR.on('instanceReady', function( ev ){
    var editor = ev.editor;
    editor.on( 'beforeCommandExec', function( ev ){
        if('image' === ev.data.name){
            $('#imageFile').click();
            return false;
        }
    });
});
</script>
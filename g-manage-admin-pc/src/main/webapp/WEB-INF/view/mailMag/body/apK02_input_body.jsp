<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page pageEncoding="UTF-8"%>

<c:set var="HTML" value="<%=MTypeConstants.deliveryTypeKbn.HTML%>" />
<c:set var="TEXT" value="<%=MTypeConstants.deliveryTypeKbn.TEXT%>" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/ckeditor/ckeditor.js"></script>
<script type="text/javascript">
<!--
	// 「DatePicker」の搭載
	$(function(){
		$("#deliveryScheduleDatetime").datepicker();
	});
// -->
</script>

<!-- #main# -->
<div id="main">
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:if test="${not empty navigationPath2}">
			<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		</c:if>
	</ul>

	<hr />
	<html:errors/>

	<h2 class="title application">${f:h(pageTitle1)}</h2>

	<s:form action="${f:h(actionPath)}" >
	<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
<!-- 一旦コメントアウト
		<tr>
			<th width="150">
				エリア
			</th>
			<td>
				<gt:areaList name="areaList" />
				${f:label(areaCd, areaList, 'value', 'label')}
			</td>
		</tr>
 -->
		<tr>
			<th>
				配信予定日
			</th>
			<td>
				<html:text property="deliveryScheduleDatetime" styleId="deliveryScheduleDatetime" />
			</td>
		</tr>
 		<tr>
 			<th>配信エリア</th>
 			<td>
 				<gt:areaList name="areaList" authLevel="${userDto.authLevel}" />
				<c:choose>
					<c:when test="${pageKbn eq PAGE_INPUT}">
		                <c:forEach items="${areaList}" var="t">
		                    <li class="areaCd">
		                        <html:multibox property="areaCd" value="${f:h(t.value)}" styleId="areaCd${t.value}" />
		                        <label for="areaCd${t.value}">&nbsp;${f:h(t.label)}</label>
		                    </li>
		                </c:forEach>
					</c:when>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<html:select property="areaCd" styleId="areaCd">
							<html:optionsCollection name="areaList"/>
						</html:select>
					</c:when>
				</c:choose>
 			</td>
 		</tr>
		<tr>
			<th>配信形式&nbsp;</th>
			<td>
				<gt:typeList name="deliveryTypeKbnList" typeCd="<%=MTypeConstants.deliveryTypeKbn.TYPE_CD %>"/>
				<c:forEach items="${deliveryTypeKbnList}" var="t" varStatus="status">
					<label for="${status.index}"><html:radio property="deliveryType" value="${t.value}" styleId="${status.index}" style="width:initial"/>${f:h(t.label)}</label>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th class="bdrs_bottom">
				ヘッダメッセージ
			</th>
			<c:choose>
				<c:when test="${deliveryType eq HTML }">
					<td class="bdrs_bottom" id="html">
						<html:textarea property="htmlBody" styleId="optionValue"></html:textarea>
					</td>
					<td class="bdrs_bottom" id="text" style="display:none">
						<html:textarea property="textBody" styleId="optionValue"></html:textarea>
					</td>
				</c:when>
				<c:when test="${deliveryType eq TEXT }">
					<td class="bdrs_bottom" id="html" style="display:none">
						<html:textarea property="htmlBody" styleId="optionValue"></html:textarea>
					</td>
					<td class="bdrs_bottom" id="text">
						<html:textarea property="textBody" styleId="optionValue"></html:textarea>
					</td>
				</c:when>
			</c:choose>
		</tr>
	</table>

	<div class="wrap_btn">
		<c:choose>
			<c:when test="${pageKbn eq PAGE_INPUT}">
				<html:submit property="conf" value="確認"/>
			</c:when>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<html:submit property="conf" value="確認"/>
				<html:submit property="backToDetail" value="詳細へ戻る"/>
			</c:when>
		</c:choose>

	</div>
	</s:form>
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:if test="${not empty navigationPath2}">
			<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		</c:if>
	</ul>
	<div style="display: none">
		<input type="file" name="imageFile" id="imageFile"/>&nbsp;
	</div>
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
			$("#optionValue").append(insert);
			CKEDITOR.instances.optionValue.setData(CKEDITOR.instances.optionValue.getData() + insert)
		}
	})
}
CKEDITOR.replace('optionValue', {
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
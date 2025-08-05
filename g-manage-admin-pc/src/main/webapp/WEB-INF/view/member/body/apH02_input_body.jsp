<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.entity.MMember"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page import="com.gourmetcaree.db.common.service.MemberService"%>

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

	<h2 class="title member">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_serch# -->
	<div class="wrap_search">
		<table cellpadding="0" cellspacing="1" border="0" class="search_table btm_margin">
			<tr>
				<th class="td_title">メルマガ配信条件</th>
			</tr>
			<tr>
				<td>
					<c:set var="ID" value="<%= MMember.ID %>" />
					<c:if test="${!empty whereMap[ID]}">
						<span class="attention">ID：</span>
						${f:h(whereMap[ID])}&nbsp;&nbsp;
					</c:if>

					<c:set var="MEMBER_NAME" value="<%= MMember.MEMBER_NAME %>" />
					<c:if test="${!empty whereMap[MEMBER_NAME]}">
						<span class="attention">氏名：</span>
						${f:h(whereMap[MEMBER_NAME])}&nbsp;&nbsp;
					</c:if>

					<c:set var="AREA_CD" value="<%= MMember.AREA_CD %>" />
					<c:if test="${!empty whereMap[AREA_CD]}">
						<gt:areaList name="areaList" authLevel="${userDto.authLevel}"/>
						<span class="attention">エリア：</span>
						${f:label(whereMap[AREA_CD], areaList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<gt:prefecturesList name="prefecturesList" />
					<c:set var="HOPE_AREA" value="hope_area" />
					<c:if test="${!empty whereMap[HOPE_AREA]}">
						<span class="attention">希望勤務地：</span>
						${f:label(whereMap[HOPE_AREA], prefecturesList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="PREFECTURES_CD" value="<%= MMember.PREFECTURES_CD %>" />
					<c:if test="${!empty whereMap[PREFECTURES_CD]}">
						<span class="attention">都道府県：</span>
						${f:label(whereMap[PREFECTURES_CD], prefecturesList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="INDUSTRY_KBN" value="<%= MTypeConstants.IndustryKbn.TYPE_CD %>" />
					<c:if test="${!empty whereMap[INDUSTRY_KBN]}">
						<gt:typeList name="industryList" typeCd="${INDUSTRY_KBN}" />
						<span class="attention">希望業態：</span>
						${f:label(whereMap[INDUSTRY_KBN], industryList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="EMPLOY_PTN_KBN" value="<%= MMember.EMPLOY_PTN_KBN %>" />
					<c:if test="${!empty whereMap[EMPLOY_PTN_KBN]}">
						<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
						<span class="attention">雇用形態：</span>
						${f:label(whereMap[EMPLOY_PTN_KBN], employPtnList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="SEX_KBN" value="<%= MMember.SEX_KBN %>" />
					<c:if test="${!empty whereMap[SEX_KBN]}">
						<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
						<span class="attention">性別：</span>
						${f:label(whereMap[SEX_KBN], sexList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="LOWER_AGE" value="<%= MemberService.LOWER_AGE %>" />
					<c:set var="UPPER_AGE" value="<%= MemberService.UPPER_AGE %>" />
					<c:if test="${!empty whereMap[LOWER_AGE] or !empty whereMap[UPPER_AGE]}">
						<span class="attention">年齢：</span>
							<c:if test="${!empty whereMap[LOWER_AGE]}">
								${f:h(whereMap[LOWER_AGE])}
							</c:if>
							～
							<c:if test="${!empty whereMap[UPPER_AGE]}">
								${f:h(whereMap[UPPER_AGE])}
							</c:if>&nbsp;&nbsp;
					</c:if>

					<c:set var="FROM_UPDATE_DATE" value="<%= MemberService.FROM_UPDATE_DATE %>" />
					<c:set var="TO_UPDATE_DATE" value="<%= MemberService.TO_UPDATE_DATE %>" />
					<c:if test="${!empty whereMap[FROM_UPDATE_DATE] or !empty whereMap[TO_UPDATE_DATE]}">
						<span class="attention">更新日：</span>
							<c:if test="${!empty whereMap[FROM_UPDATE_DATE]}">
								${f:h(whereMap[FROM_UPDATE_DATE])}
							</c:if>
							～
							<c:if test="${!empty whereMap[TO_UPDATE_DATE]}">
								${f:h(whereMap[TO_UPDATE_DATE])}
							</c:if>&nbsp;&nbsp;
					</c:if>

					<c:set var="JUSKILL_FLG" value="<%= MMember.JUSKILL_FLG %>" />
					<c:if test="${!empty whereMap[JUSKILL_FLG]}">
						<gt:typeList name="juskillList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>" />
						<span class="attention">ジャスキル登録：</span>
						${f:label(whereMap[JUSKILL_FLG], juskillList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="JUSKILL_CONTACT_FLG" value="<%= MMember.JUSKILL_CONTACT_FLG %>" />
					<c:if test="${!empty whereMap[JUSKILL_CONTACT_FLG]}">
						<gt:typeList name="juskillContactList" typeCd="<%=MTypeConstants.JuskillContactFlg.TYPE_CD %>" />
						<span class="attention">転職相談窓口：</span>
						${f:label(whereMap[JUSKILL_CONTACT_FLG], juskillContactList, 'value', 'label')}&nbsp;&nbsp;
					</c:if>

					<c:set var="LOGIN_ID" value="<%= MMember.LOGIN_ID %>" />
					<c:if test="${!empty whereMap[LOGIN_ID]}">
						<span class="attention">メールアドレス：</span>
						${f:h(whereMap[LOGIN_ID])}&nbsp;&nbsp;
					</c:if>
				&nbsp;</td>
			</tr>
		</table>
		<hr />
	</div>
	<!-- #wrap_serch# -->

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table btm_margin">
				<tr>
					<th colspan="2" class="bdrs_right">会員版メルマガ</th>
				</tr>
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
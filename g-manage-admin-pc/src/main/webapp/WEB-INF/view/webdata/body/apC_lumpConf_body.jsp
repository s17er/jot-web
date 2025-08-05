<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<gt:areaList name="areaList" />
<gt:simpleVolumeList name="simpleVolumeList" limitValue="${areaCd}" authLevel="${userDto.authLevel}" />
<gt:typeList name="sizeList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />
<gt:statusList name="displayStatusList" statusKbn="<%=String.valueOf(MTypeConstants.StatusKbn.DIPLAY_STATUS_VALUE) %>" />
<gt:companyList name="companyList" />
<gt:salesList name="salesList" />
<gt:typeList name="applicationFormList" typeCd="<%=MTypeConstants.ApplicationFormKbn.TYPE_CD %>" />


<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.disableDoubleSubmit.js"></script>
<script type="text/javascript">
<!--

	$(function() {
		$("#lumpForm").disableDoubleSubmit(30000);
	});

	//非表示要素の表示
	function showTbl(){
		$("#wrap_result").css("display","block");
	}

	// 「DatePicker」の搭載
	$(function(){
		$("#start_ymd").datepicker();
		$("#end_ymd").datepicker();
	});
// -->
</script>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title date">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<s:form action="${f:h(actionPath)}" enctype="multipart/form-data" styleId="lumpForm">

		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table stripe_table">
			<tr>
				<th width="25" class="posi_center bdrn_bottom">No.</th>
				<th width="50" class="posi_center bdrd_bottom">原稿番号</th>
				<th width="50" class="posi_center bdrd_bottom">サイズ</th>
				<th colspan="5" class="bdrd_bottom bdrs_right">原稿名</th>
			</tr>
			<tr>
				<th class="posi_center">選択</th>
				<th class="posi_center">号数</th>
				<th class="posi_center">登録連載</th>
				<th width="60" class="posi_center">ステータス</th>
				<th>顧客</th>
				<th>会社名</th>
				<th>営業担当</th>
				<th width="75" class="posi_center bdrs_right">応募フォーム</th>
			</tr>

			<gt:typeList name="serialList" typeCd="<%=MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD %>"/>
			<c:forEach var="dtoList" varStatus="status" items="${dtoList}">
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
				<tr>
					<td class="posi_center bdrn_bottom ${classStr}"><fmt:formatNumber value="${status.index + 1}" /></td>
					<td class="posi_center bdrd_bottom ${classStr}">${f:h(dtoList.id)}&nbsp;</td>
					<td class="posi_center bdrd_bottom ${classStr}">${f:label(dtoList.sizeKbn, sizeList, 'value', 'label')}&nbsp;</td>
					<td colspan="5" class="bdrd_bottom bdrs_right ${classStr}">${f:h(dtoList.manuscriptName)}&nbsp;</td>
				</tr>
				<tr>
					<td class="posi_center ${classStr}"><input type="checkbox" name="" checked="checked" disabled="disabled" /></td>
					<td class="posi_center ${classStr}">${f:label(dtoList.volumeId, simpleVolumeList, 'value', 'label')}&nbsp;</td>
					<td class="posi_center ${classStr}">${f:br(f:h(dtoList.serialPublication))}</td>
					<td class="posi_center ${classStr}">${f:label(dtoList.displayStatus, displayStatusList, 'value', 'label')}&nbsp;</td>
					<td class="${classStr}">${f:h(dtoList.customerName)}&nbsp;</td>
					<td class="${classStr}">${f:label(dtoList.companyId, companyList, 'value', 'label')}&nbsp;</td>
					<td class="${classStr}">${f:label(dtoList.salesId, salesList, 'value', 'label')}&nbsp;</td>
					<td class="posi_center bdrs_right ${classStr}">${f:label(dtoList.applicationFormKbn, applicationFormList, 'value', 'label')}&nbsp;</td>
				</tr>
			</c:forEach>

		</table>
		<hr />

		<div class="wrap_btn">

			<c:choose>
				<c:when test="${pageKbn eq PAGE_INPUT}">
					<html:submit value="登 録" name="submit" onclick="$('#lumpForm').attr('action', '${f:url(submitPath)}')" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:when>
				<c:when test="${pageKbn eq PAGE_EDIT}">
					<html:submit value="登 録" name="submit" onclick="if(!confirm('確定してもよろしいですか?')) {return false;}else{$('#lumpForm').attr('action', '${f:url(submitPath)}')};" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					<html:submit property="back" value="戻 る" onclick="$('#lumpForm').attr('action', '${f:url(backPath)}');"  onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:when>
				<c:when test="${pageKbn eq PAGE_DELETE}">
					<c:if test="${existDataFlg eq true}">
						<html:submit value="削 除" name="submit" onclick="if(!confirm('削除してもよろしいですか?')) {return false;}else{$('#lumpForm').attr('action', '${f:url(submitPath)}')};" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:if>
					<html:submit property="back" value="戻 る" onclick="$('#lumpForm').attr('action', '${f:url(backPath)}');"  onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				</c:when>
			</c:choose>
		</div>

	</s:form>
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->
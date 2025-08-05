<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList" blankLineLabel="${common['gc.pullDown']}" />
<gt:volumeList name="volumeList" limitValue="${areaCd}" blankLineLabel="${common['gc.pullDown']}" authLevel="${userDto.authLevel}" />
<gt:simpleVolumeList name="simpleVolumeList" limitValue="${areaCd}" blankLineLabel="${common['gc.pullDown']}" authLevel="${userDto.authLevel}" />
<gt:typeList name="sizeList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" blankLineLabel="${common['gc.pullDown']}" />
<gt:statusList name="displayStatusList" statusKbn="<%=String.valueOf(MTypeConstants.StatusKbn.DIPLAY_STATUS_VALUE) %>" />
<gt:companyList name="companyList" />
<gt:salesList name="salesList" />
<gt:typeList name="applicationFormList" typeCd="<%=MTypeConstants.ApplicationFormKbn.TYPE_CD %>" />

<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript">
<!--

	//新規で契約を作成
	function renewal() {

	var value = document.getElementById("volumeAllId").value;

	for (var i = 0; i < ${fn:length(dtoList)}; i++) {
		document.getElementById("volumes_" + i).value = value;
	}

	}

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


<s:form action="${f:h(actionPath)}" enctype="multipart/form-data">
<c:if test="${existDataFlg}">
	<!-- #wrap_form# -->
	<div id="wrap_form">
		<p>「号数反映」ボタンを押すと、一覧の号数全てを指定の号数にすることが出来ます。</p>
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
			<tr>
				<th width="150" class="bdrs_bottom">反映号数</th>
				<td class="bdrs_bottom">
		        エリア：&nbsp;${f:label(areaCd, areaList, 'value', 'label')}
				&nbsp;
		        号数：&nbsp;
		        <html:select property="volumeAll" styleId="volumeAllId">
		        	<html:optionsCollection name="volumeList" />
		        </html:select>

		        </td>
			</tr>
		</table>
		<hr />

		<div class="wrap_btn">
			<input type="button" name="" value="号数反映"  onclick="if(confirm('反映します。よろしいですか？')){renewal();}"  onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
		</div>
	</div>
	<!-- #wrap_form# -->
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>



		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table list_table">
			<tr>
				<th width="25" class="posi_center bdrn_bottom">No.</th>
				<th width="50" class="posi_center bdrd_bottom">原稿番号</th>
				<th width="50" class="posi_center bdrd_bottom">サイズ</th>
				<th colspan="5" class="bdrd_bottom bdrs_right">原稿名</th>
			</tr>
			<tr>
				<th class="posi_center">選択</th>
				<th class="posi_center">号数</th>
				<th class="posi_center">連載</th>
				<th width="60" class="posi_center">ステータス</th>
				<th>顧客</th>
				<th>担当会社</th>
				<th>営業担当</th>
				<th width="75" class="posi_center bdrs_right">応募フォーム</th>
			</tr>
			<gt:typeList name="serialList" typeCd="<%=MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD %>"/>
			<c:forEach var="dtoList" varStatus="status" items="${dtoList}">
			<c:set var="classStr" value="${gf:odd(status.index, 'blue', '')}" scope="page" />
				<tr>
					<td class="posi_center bdrn_bottom ${classStr}"><fmt:formatNumber value="${status.index + 1}" /></td>
					<td class="posi_center bdrd_bottom ${classStr}">${f:h(dtoList.id)}</td>
					<td class="posi_center bdrd_bottom ${classStr}">
						<html:select name="dtoList" property="sizeKbn" indexed="true" >
							<html:optionsCollection name="sizeList" />
						</html:select>
					</td>
					<td colspan="5" class="bdrd_bottom bdrs_right ${classStr}">${f:h(dtoList.manuscriptName)}</td>
				</tr>
				<tr>
					<td class="posi_center ${classStr}"><input type="checkbox" name="" checked="checked" disabled="disabled" /></td>
					<td class="posi_center ${classStr}">
						<html:select name="dtoList" property="volumeId" indexed="true" styleClass="volumeAgreement" styleId="volumes_${f:h(status.index)}">
							<html:optionsCollection name="simpleVolumeList" />
						</html:select>
					</td>
					<td class="posi_center ${classStr}"><html:textarea name="dtoList" property="serialPublication" indexed="true" maxlength="9" style="width: 108px; height:40px;"></html:textarea></td>
					<td class="posi_center ${classStr}">${f:label(dtoList.displayStatus, displayStatusList, 'value', 'label')}</td>
					<td class="${classStr}">${f:h(dtoList.customerName)}&nbsp;</td>
					<td class="${classStr}">${f:label(dtoList.companyId, companyList, 'value', 'label')}&nbsp;</td>
					<td class="${classStr}">${f:label(dtoList.salesId, salesList, 'value', 'label')}&nbsp;</td>
					<td class="posi_center bdrs_right ${classStr}">${f:label(dtoList.applicationFormKbn, applicationFormList, 'value', 'label')}&nbsp;</td>
				</tr>
			</c:forEach>
		</table>
		<hr />

		<div class="wrap_btn">
			<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
		</div>
</c:if>
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
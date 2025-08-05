<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:prefecturesList name="prefecturesList" />
<gt:birthYearList name="birthYearList" blankLineLabel="--" />
<gt:birthMonthList name="birthMonthList" blankLineLabel="--" />
<gt:birthDayList name="birthDayList" blankLineLabel="--" />
<gt:typeList name="juskillMigrationFlgList" typeCd="<%=MTypeConstants.JuskillMigrationFlg.TYPE_CD %>" />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="healthKbnList" typeCd="<%=MTypeConstants.HealthKbn.TYPE_CD %>" />
<gt:typeList name="sinKbnList" typeCd="<%=MTypeConstants.SinKbn.TYPE_CD %>" />
<gt:typeList name="gangstersKbnList" typeCd="<%=MTypeConstants.GangstersKbn.TYPE_CD %>" />
<gt:typeList name="historyModificationFlgList" typeCd="<%=MTypeConstants.HistoryModificationFlg.TYPE_CD %>" />
<gt:typeList name="onLeavingModificationFlgList" typeCd="<%=MTypeConstants.OnLeavingModificationFlg.TYPE_CD %>" />
<gt:typeList name="qualificationModificationFlgList" typeCd="<%=MTypeConstants.QualificationModificationFlg.TYPE_CD %>" />

<script>
/**
 * 素材データをアップロードする
 */
function addMaterial(kbn) {
	var file = $('#resume').prop("files")[0];

	// ファイルを選択していない場合は処理しない
	if (file == null || file == undefined) {
		return;
	}

	var fd = new FormData();
	fd.append('pdfFile', file);
	fd.append('hiddenMaterialKbn', kbn);

    // アップロード
	$.ajax({
			url : "${f:url(upPdfAjaxPath)}",
			type : "POST",
			data : fd,
			cache : false,
			contentType : false,
			processData : false,
			dataType : "json",
	}).done(function(data) {
			// エラーが発生した場合は警告
			if(data.error) {
				alert(data.error);
				return;
			}

			$("#displayFileName").text(data.fileName);
			$("#pdfFileName").val(data.fileName);
			// ボタンの制御
			$("#resume").val("");
			$("#resume").hide();
			$("#uploadResumeBtn").hide();
			$("#deleteResumeBtn").show();

		}).fail(function(jqXHR, textStatus, errorThrown) {
			alert("アップロードに失敗しました。はじめから処理をやり直してください。");
			console.log(textStatus);
			console.log(errorThrown);
		});
}

/**
 * 素材データを削除する
 */
function delMaterial(kbn) {

	//削除処理
	if (confirm('削除してもよろしいですか？')) {
		var fd = new FormData();
		fd.append('hiddenMaterialKbn', kbn);
		$.ajax({
				url : "${f:url(delPdfAjaxPath)}",
				type : "POST",
				data : fd,
				cache : false,
				contentType : false,
				processData : false,
				dataType : "json"

			}).done(function(data) {
				// エラーが発生した場合は警告
				if(data.error) {
					alert(data.error);
					return;
				}
				$("#displayFileName").text("");
				$("#pdfFileName").val("");
				// 画像の削除
				$('#resume').html("");
				// ボタンの制御
				$("#resume").show();
				$("#uploadResumeBtn").show();
				$("#deleteResumeBtn").hide();


			}).fail(function(jqXHR, textStatus, errorThrown) {
				alert("削除に失敗しました。はじめから処理をやり直してください。");
				console.log(textStatus);
				console.log(errorThrown);
			});
	}
}
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

	<h2 class="title member">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">
		<s:form action="${f:h(actionPath)}" enctype="multipart/form-data">

			<html:hidden property="hiddenId" />

			<c:if test="${existDataFlg eq true}">

			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">ID</th>
					<td>${f:h(id)}&nbsp;</td>
				</tr>
				<tr>
					<th>人材紹介登録者データ移行会員</th>
					<td>${f:label(juskillMigrationFlg, juskillMigrationFlgList, 'value', 'label')}</td>
				</tr>
				<tr>
					<th width="150">人材紹介登録者No</th>
					<td><html:text property="juskillMemberNo" /></td>
				</tr>
				<tr>
					<th>登録日</th>
					<td>
						${f:h(juskillEntryDate)}&nbsp;
					</td>
				</tr>
				<tr>
					<th>履歴書データ</th>
					<td>
						<c:set var="resume_kbn" value="<%= MTypeConstants.JuskillMemberMaterialKbn.RESUME%>" />
						<c:choose>
							<c:when test="${pdfFileName == null}">
								<label id="displayFileName">${pdfFileName}</label><br><br>
								<input type="file" name="resume" id="resume" value="参照" size="50"  />&nbsp;
								<html:button property="upMaterial" styleId="uploadResumeBtn" onclick="addMaterial(${resume_kbn })" value="アップロード" />
								<html:button property="deleteMaterial" styleId="deleteResumeBtn" onclick="delMaterial(${resume_kbn })" value="削除" style="display:none" />
							</c:when>
							<c:otherwise>
								<label id="displayFileName">${pdfFileName}</label><br><br>
								<input type="file" name="resume" id="resume" value="参照" size="50" style="display:none" />&nbsp;
								<html:button property="upMaterial" styleId="uploadResumeBtn" onclick="addMaterial(${resume_kbn})" value="アップロード" style="display:none" />
								<html:button property="deleteMaterial" styleId="deleteResumeBtn" onclick="delMaterial(${resume_kbn})" value="削除" />
							</c:otherwise>
						</c:choose>
						<html:hidden property="hiddenMaterialKbn" styleId="hiddenMaterialKbn" />
						<html:hidden property="pdfFileName" styleId="pdfFileName"/>
					</td>
				</tr>
				<tr>
					<th>氏名</th>
					<td><html:text property="juskillMemberName" /></td>
				</tr>
				<tr>
					<th>氏名(苗字のみ)</th>
					<td><html:text property="familyName" /></td>
				</tr>
				<tr>
					<th>ヒアリング担当者</th>
					<td><html:text property="hearingRep" /></td>
				</tr>
				<tr>
					<th>性別</th>
					<td class="release">
						<c:forEach items="${sexKbnList}" var="t">
							<html:radio property="sexKbn" value="${t.value}" styleId="sexKbn_${t.value}" styleClass="release"  />
							<c:if test="${t.value != 3 }">
								<label for="sexKbn_${t.value}">${f:h(t.label)}性</label>
							</c:if>
							<c:if test="${t.value == 3 }">
								<label for="sexKbn_${t.value}">${f:h(t.label)}</label>
							</c:if>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>生年月日</th>
					<td>
						<html:select property="birthYear"  >
							<html:optionsCollection name="birthYearList"/>
						</html:select>&nbsp;年
						<html:select property="birthMonth"  >
							<html:optionsCollection name="birthMonthList"/>
						</html:select>&nbsp;月
						<html:select property="birthDate"  >
							<html:optionsCollection name="birthDayList"/>
						</html:select>&nbsp;日
					</td>
				</tr>
				<tr>
					<th>転職時期</th>
					<td><html:text property="transferTiming" /></td>
				</tr>
				<tr>
					<th>希望業態</th>
					<td><html:text property="hopeIndustry" /></td>
				</tr>
				<tr>
					<th>希望職種</th>
					<td><html:text property="hopeJob" /></td>
				</tr>
				<tr>
					<th>経験</th>
					<td><html:textarea property="experience" style="width: 300px; height: 19px;"></html:textarea></td>
				</tr>
                <tr>
                    <th>希望年収(月給)</th>
                    <td><html:text property="closestStation" /></td>
                <tr>
				<tr>
					<th>希望勤務時間・休日数</th>
					<td><html:text property="hopeJob2" /></td>
				</tr>
				<tr>
					<th>郵便番号</th>
					<td><html:text property="zipCd" /></td>
				</tr>
				<tr>
					<th>住所1</th>
					<td>
						<html:select property="prefecturesCd">
							<html:optionsCollection name="prefecturesList"/>
						</html:select><br />
						<html:text property="address" style="margin-top: 5px" />
					</td>
				</tr>
				<tr>
					<th>ビル名</th>
					<td><html:text property="buildingName" /></td>
				</tr>
				<tr>
					<th>路線・最寄駅</th>
					<td><html:text property="route" /></td>
				</tr>
				<tr>
					<th>電話1</th>
					<td><html:text property="phoneNo1" /></td>
				</tr>
				<tr>
					<th>電話2</th>
					<td><html:text property="phoneNo2" /></td>
				</tr>
				<tr>
					<th>連絡先3（ＰＣメール）</th>
					<td><html:text property="pcMail" /></td>
				</tr>
				<tr>
					<th>メール</th>
					<td><html:text property="mail" /></td>
				</tr>
				<tr>
					<th>PW</th>
					<td><html:text property="pw" /></td>
				</tr>
				<tr>
					<th>最終学歴</th>
					<td><html:text property="finalSchoolHistory" /></td>
				</tr>
				<tr>
					<th>出身地</th>
					<td><html:textarea property="schoolHistoryNote" style="width: 300px; height: 19px;"></html:textarea></td>
				</tr>
				<tr>
					<th>家族構成</th>
					<td><html:text property="finalCareerHistory" /></td>
				</tr>
				<tr>
					<th>職歴</th>
					<td>
						<c:if test="${not empty careerHistoryList}">
							<c:forEach var="career" items="${careerHistoryList}" varStatus="status">
								<div style="margin-bottom:5px">
								<html:textarea property="careerHistoryList[${status.index}]" style="width: 300px; height: 19px;"></html:textarea>
								</div>
							</c:forEach>
							<c:forEach begin="${fn:length(careerHistoryList)}" end="3" var="i" step="1">
								<div style="margin-bottom:5px">
								<textarea name="careerHistoryList[${i}]" style="width: 300px; height: 19px;"></textarea>
								</div>
							</c:forEach>
						</c:if>
						<c:if test="${empty careerHistoryList}">
							<c:forEach begin="0" end="3" var="i" step="1">
							<html:textarea property="careerHistoryList" style="width: 300px; height: 19px;"></html:textarea>
							<br>
							<br>
							</c:forEach>
						</c:if>
					</td>
				</tr>
				<tr>
					<th>取得資格</th>
					<td><html:text property="qualification" /></td>
				</tr>
				<tr>
					<th>登録経路</th>
					<td><html:text property="entryPath" /></td>
				</tr>
				<tr>
					<th>特記事項</th>
					<td>
						<html:textarea property="notice" cols="60"></html:textarea>
					</td>
				</tr>
				<tr>
					<th>転職状況</th>
					<td><html:text property="jobChangeStatus" /></td>
				</tr>
				<tr>
					<th>備考欄</th>
					<td><html:text property="mailHope" /></td>
				</tr>
				<tr>
					<th>健康状態について</th>
					<td>
						<c:forEach items="${healthKbnList}" var="t">
							<html:radio property="healthKbn" value="${t.value}" styleId="healthKbn_${t.value}" styleClass="release"  />
							<label for="healthKbn_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>刑事罰、訴訟の有無について</th>
					<td>
						<c:forEach items="${sinKbnList}" var="t">
							<html:radio property="sinKbn" value="${t.value}" styleId="sinKbn_${t.value}" styleClass="release"  />
							<label for="sinKbn_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>暴力団関係、反社会的勢力等との関わりについて</th>
					<td>
						<c:forEach items="${gangstersKbnList}" var="t">
							<html:radio property="gangstersKbn" value="${t.value}" styleId="gangstersKbn_${t.value}" styleClass="release"  />
							<label for="gangstersKbn_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">履歴書、職務経歴書の記載事項について</th>
					<td class="bdrs_bottom">
						<b>a)　社歴・学歴</b><br>
						<c:forEach items="${historyModificationFlgList}" var="t">
							<html:radio property="historyModificationFlg" value="${t.value}" styleId="historyModificationFlg_${t.value}" styleClass="release"  />
							<label for="historyModificationFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach><br><br>
						<b>b)　入退社年月日・退社事由</b><br>
						<c:forEach items="${onLeavingModificationFlgList}" var="t">
							<html:radio property="onLeavingModificationFlg" value="${t.value}" styleId="onLeavingModificationFlg_${t.value}" styleClass="release"  />
							<label for="onLeavingModificationFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach><br><br>
						<b>c)　取得資格</b><br>
						<c:forEach items="${qualificationModificationFlgList}" var="t">
							<html:radio property="qualificationModificationFlg" value="${t.value}" styleId="qualificationModificationFlg_${t.value}" styleClass="release"  />
							<label for="qualificationModificationFlg_${t.value}">${f:h(t.label)}</label>
						</c:forEach>
					</td>
				</tr>
			</table>
			<hr />

			<div class="wrap_btn">
				<html:submit property="conf" value="確 認" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>

			</c:if>
		</s:form>
	</div>
	<!-- #wrap_form# -->
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<hr />

</div>
<!-- #main# -->

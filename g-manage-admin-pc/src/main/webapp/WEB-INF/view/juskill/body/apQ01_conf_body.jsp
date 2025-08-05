<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:prefecturesList name="prefecturesList" />
<gt:typeList name="juskillMigrationFlgList" typeCd="<%=MTypeConstants.JuskillMigrationFlg.TYPE_CD %>"  />
<gt:typeList name="sexKbnList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="healthKbnList" typeCd="<%=MTypeConstants.HealthKbn.TYPE_CD %>" />
<gt:typeList name="sinKbnList" typeCd="<%=MTypeConstants.SinKbn.TYPE_CD %>" />
<gt:typeList name="gangstersKbnList" typeCd="<%=MTypeConstants.GangstersKbn.TYPE_CD %>" />
<gt:typeList name="historyModificationFlgList" typeCd="<%=MTypeConstants.HistoryModificationFlg.TYPE_CD %>" />
<gt:typeList name="onLeavingModificationFlgList" typeCd="<%=MTypeConstants.OnLeavingModificationFlg.TYPE_CD %>" />
<gt:typeList name="qualificationModificationFlgList" typeCd="<%=MTypeConstants.QualificationModificationFlg.TYPE_CD %>" />
<% /* javascriptファイルを設定 */ %>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/checkConfFlg.js"></script>

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
		<s:form action="${f:h(actionPath)}">
		<c:if test="${existDataFlg eq true}">
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="150">ID</th>
					<td>${f:h(id)}&nbsp;</td>
				</tr>
				<tr>
					<th width="150">人材紹介登録者No</th>
					<td>${f:h(juskillMemberNo)}&nbsp;</td>
				</tr>
				<tr>
					<th>人材紹介登録者データ移行会員</th>
					<td>${f:label(juskillMigrationFlg, juskillMigrationFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>会員ID</th>
					<td>
						<c:if test="${not empty memberId}">
		                  <c:set var="memberPath" value="/member/detail/index/${memberId}" />
		                  <a href="${f:url(memberPath)}">${memberId}</a>
						</c:if>
						<c:if test="${empty memberId}">
							未登録
						</c:if>
					</td>
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
						<c:choose>
							<c:when test="${pageKbn eq PAGE_DETAIL}">
								<a href="${resumeFilePath}" target="_blank">
									${f:br(f:h(pdfFileName))}
								</a>&nbsp;
							</c:when>
							<c:otherwise>
									${f:br(f:h(pdfFileName))}&nbsp;
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>氏名</th>
					<td>${f:h(juskillMemberName)}&nbsp;</td>
				</tr>
				<tr>
					<th>氏名(苗字のみ)</th>
					<td>${f:h(familyName)}&nbsp;</td>
				</tr>
				<tr>
					<th>ヒアリング担当者</th>
					<td>${f:h(hearingRep)}&nbsp;</td>
				</tr>
				<tr>
					<th>性別</th>
					<c:if test="${sexKbn != 3 }">
						<td>${f:label(sexKbn, sexKbnList, 'value', 'label')}性&nbsp;</td>
					</c:if>
					<c:if test="${sexKbn == 3 }">
						<td>${f:label(sexKbn, sexKbnList, 'value', 'label')}&nbsp;</td>
					</c:if>
				</tr>
				<tr>
					<th>生年月日</th>
					<td>
						<c:if test="${not empty birthYear}">
							${f:h(birthYear)}年${f:h(birthMonth)}月${f:h(birthDate)}日 (${f:h(age)}歳)&nbsp;
						</c:if>
					</td>
				</tr>
				<tr>
					<th>転職時期</th>
					<td>${f:h(transferTiming)}&nbsp;</td>
				</tr>
				<tr>
					<th>希望業態</th>
					<td>${f:h(hopeIndustry)}&nbsp;</td>
				</tr>
				<tr>
					<th>希望職種</th>
					<td>${f:h(hopeJob)}&nbsp;</td>
				</tr>
				<tr>
					<th>経験</th>
					<td>${f:h(experience)}&nbsp;</td>
				</tr>
				<tr>
					<th>希望年収(月給)</th>
					<td>${f:h(closestStation)}&nbsp;</td>
				</tr>
				<tr>
					<th>希望勤務時間・休日数</th>
					<td>${f:h(hopeJob2)}&nbsp;</td>
				</tr>
				<tr>
					<th>郵便番号</th>
					<td>${f:h(zipCd)}&nbsp;</td>
				</tr>
				<tr>
					<th>住所1</th>
					<td>${f:label(prefecturesCd, prefecturesList, 'value', 'label')}${f:h(address)}&nbsp;</td>
				</tr>
				<tr>
					<th>ビル名</th>
					<td>${f:h(buildingName)}&nbsp;</td>
				</tr>
				<tr>
					<th>路線・最寄り駅</th>
					<td>${f:h(route)}&nbsp;</td>
				</tr>
				<tr>
					<th>電話1</th>
					<td>${f:h(phoneNo1)}&nbsp;</td>
				</tr>
				<tr>
					<th>電話2</th>
					<td>${f:h(phoneNo2)}&nbsp;</td>
				</tr>
				<tr>
					<th>連絡先3（ＰＣメール）</th>
					<td>${f:h(pcMail)}&nbsp;</td>
				</tr>
				<tr>
					<th>メール</th>
					<td>${f:h(mail)}&nbsp;</td>
				</tr>
				<tr>
					<th>PW</th>
					<td>${f:h(pw)}&nbsp;</td>
				</tr>
				<tr>
					<th>最終学歴</th>
					<td>${f:h(finalSchoolHistory)}&nbsp;</td>
				</tr>
				<tr>
					<th>出身地</th>
					<td>${f:br(f:h(schoolHistoryNote))}&nbsp;</td>
				</tr>
				<tr>
					<th>家族構成</th>
					<td>${f:h(finalCareerHistory)}&nbsp;</td>
				</tr>
				<tr>
					<th>職歴</th>
					<td>
						<c:if test="${not empty careerHistoryList}">
							<c:forEach var="career" items="${careerHistoryList}" varStatus="status">
								<div <c:if test="${!status.last}">style="margin-bottom:5px"</c:if>>
								<fmt:formatNumber value="${status.count}" pattern="0" />.<br />
								${f:br(f:h(career))}
								</div>
							</c:forEach>
						</c:if>
					</td>
				</tr>
				<tr>
					<th>取得資格</th>
					<td>${f:h(qualification)}&nbsp;</td>
				</tr>
				<tr>
					<th>登録経路</th>
					<td>${f:h(entryPath)}&nbsp;</td>
				</tr>
				<tr>
					<th>特記事項</th>
					<td>${f:br(f:h(notice))}&nbsp;</td>
				</tr>
				<tr>
					<th>転職状況</th>
					<td>${f:h(jobChangeStatus)}&nbsp;</td>
				</tr>
				<tr>
					<th>備考欄</th>
					<td>${f:h(mailHope)}&nbsp;</td>
				</tr>
				<tr>
					<th>健康状態について</th>
					<td>${f:label(healthKbn, healthKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>刑事罰、訴訟の有無について</th>
					<td>${f:label(sinKbn, sinKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th>暴力団関係、反社会的勢力等との関わりについて</th>
					<td>${f:label(gangstersKbn, gangstersKbnList, 'value', 'label')}&nbsp;</td>
				</tr>
				<tr>
					<th class="bdrs_bottom">履歴書、職務経歴書の記載事項について</th>
					<td class="bdrs_bottom">
						<b>a)　社歴・学歴</b><br>
						${f:label(historyModificationFlg, historyModificationFlgList, 'value', 'label')}<br><br>
						<b>b)　入退社年月日・退社事由</b><br>
						${f:label(onLeavingModificationFlg, onLeavingModificationFlgList, 'value', 'label')}<br><br>
						<b>c)　取得資格</b><br>
						${f:label(qualificationModificationFlg, qualificationModificationFlgList, 'value', 'label')}
					</td>
				</tr>
			</table>
			<hr />

			<br><br>

			<c:if test="${pageKbn eq PAGE_DETAIL}">
				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
					<tr>
						<th class="bdrs_right"><b>登録者内容確認URL</b></th>
					</tr>
					<tr>
						<td class="bdrs_bottom">
							お疲れ様です。<br>
							下記URLより人材紹介登録者情報をご確認くださいませ。<br>
							URLとパスワードは同じメールで送信しないようにしてください。<br>
							どうぞよろしくお願い致します。<br>
							<br>
							<b>社外用</b><br>
							${f:br(f:h(previewUrl))}&nbsp;<br>
							<br>
							<b>閲覧用パスワード</b><br>
							${f:br(f:h(password))}&nbsp;
						</td>
					</tr>
				</table>
				<hr />
				<br />
			</c:if>

					<div class="wrap_btn">
						<c:choose>
							<c:when test="${pageKbn eq PAGE_EDIT}">
								<html:submit property="submit" value="登 録"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);"
									onmouseout="outBtn(this);" />
								<html:submit property="correct" value="訂 正"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);"
									onmouseout="outBtn(this);" />
							</c:when>
							<c:when test="${pageKbn eq PAGE_DETAIL}">
								<c:set var="editPath" value="/juskillMember/edit/index/${id}" />
								<input type="button" name="edit" value="編 集"
									onclick="location.href='${f:url(editPath)}'"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);"
									onmouseout="outBtn(this);" />
								<input type="button" name="delete" value="削 除" onclick="deleteConf('delProcessFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
								<html:submit property="back" value="戻 る"
									onmousedown="downBtn(this);" onmouseup="upBtn(this);"
									onmouseout="outBtn(this);" />
							</c:when>
						</c:choose>
					</div>
			</c:if>
		</s:form>
		<% //削除の場合は、確認のためにフラグを立ててから画面遷移  %>
		<s:form action="${f:h(deleteActionPath)}" styleId="deleteForm" >
			<html:hidden property="id" value="${f:h(id)}" />
			<html:hidden property="version" value="${f:h(version)}" />
			<input type="hidden" id="delProcessFlg">
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

</div>
<!-- #main# -->

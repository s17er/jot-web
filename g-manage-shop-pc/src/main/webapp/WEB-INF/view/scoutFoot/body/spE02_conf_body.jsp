<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.shop.pc.application.form.application.ApplicationForm"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm"%>

<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />

<c:set scope="page" var="RECEIVE" value="<%=MTypeConstants.SendKbn.RECEIVE %>" />
<c:set scope="page" var="FROM_PAGE_KBN" value="<%=ApplicationMailForm.FROM_MAIL_DETAIL%>" />

<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>
<gt:typeList name="employPtnKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"/>
<gt:typeList name="foogExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>" />
<gt:typeList name="jobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>
<gt:typeList name="expManagerKbnList" typeCd="<%=MTypeConstants.ExpManagerKbn.TYPE_CD %>" />
<gt:typeList name="expManagerYearKbnList" typeCd="<%=MTypeConstants.ExpManagerYearKbn.TYPE_CD %>" />
<gt:typeList name="expManagerPersonsKbnList" typeCd="<%=MTypeConstants.ExpManagerPersonsKbn.TYPE_CD %>" />
<gt:typeList name="foreignWorkFlgKbnList" typeCd="<%=MTypeConstants.ForeignWorkFlg.TYPE_CD %>" />
<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>"/>
<gt:typeList name="transferFlgList" typeCd="<%=MTypeConstants.TransferFlg.TYPE_CD %>" />
<gt:typeList name="midnightShiftFlgList" typeCd="<%=MTypeConstants.MidnightShiftFlg.TYPE_CD %>"  />
<gt:typeList name="salaryKbnList" typeCd="<%=MTypeConstants.SalaryKbn.TYPE_CD %>"  />

<c:set var="employPtnMap" value="<%=MTypeConstants.EmployPtnKbn.employPtnSmallLabelMap %>"/>
<gt:sentenceList name="sentenceList" limitValue="${userDto.customerId}" blankLineLabel="定型文を選択" />

<script type="text/javascript" src="${SHOP_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript">
/** ナビゲーション */
	$(function() {
		$('#nav_mail').addClass('active');
	});
	$(function(){
		$(".menu_top li a").each(function(){
			if(this.href == location.href) {
				$(this).parents("li").addClass("active");
			}
		});
	});
	// メールタブ
	$(function(){
		$(".menu_tab li a").each(function(){
			if(this.href == location.href) {
				$(this).parents("li").addClass("tab_active");
			}
		});
	});
	$(function() {
		$('form').submit(function(){
			if(!checkMail()) {
				alert('件名と本文を入力してください。');
				return false;
			}
		});
	});
</script>
<script type="text/javascript">
//<![CDATA[
	var windowArray = new Object();
	function detailWindow(id) {
		windowArray[id] = window.open('/shop-pc/application/detail/subApplicationDetail/' + id, id,'width=800,height=700,scrollbars=yes');
	}
// ]]>
</script>
<script type="text/javascript">
//<![CDATA[


	var onchangeSelectionKbn = function(selectId, id) {

		var kbn = $("#" + selectId);

		var selectionJson = {
				scoutMailLogId : id,
				selectionKbn : kbn.val()
		};

		$.ajax({
			url: '${f:url("/scoutMail/list/ajaxSelectionFlg/")}',
			data: selectionJson,
			type: 'post'
		});

	};

	var onchangeMemo = function(selectId, id) {

		var kbn = $("#" + selectId);

		var selectionJson = {
				scoutMailLogId : id,
				memo : kbn.val()
		};

		$.ajax({
			url: '${f:url("/scoutMail/list/editMemo/")}',
			data: selectionJson,
			type: 'post'
		});
	};
// ]]>
</script>
<script type="text/javascript">
<!--

function checkMail() {
	if($.trim($("#mailBody").val()) && $.trim($("#subject").val())) {
		return true;
	} else {
		return false;
	}
}

function selectPattern() {

	if ($("#patternId").val() == "") {
		alert("定型文を選択してください。");
		return;
	}

	setAjaxPartsWithAfter('${f:url(selectPatternAjaxPath)}', $("#patternId").val(), sendPattern);
};

function sendPattern(str) {
	if (str == "errorSentence") {
		alert("定型文が削除されている可能性があります。");
	} else if (str == "errorNoSelect") {
		alert("定型文を選択してください。");
	} else if (str == "errorAddUrl") {
		alert("現在公開中の求人原稿はありません。");
	} else if (str == "errorMember") {
		alert("会員名の取得に失敗しました。");
	} else {
		$('#mailBody').val(str);
		$('[name=subject]').val($("#patternId option:selected").text());
	}
};


//-->
</script>
<!-- #main# -->
<div id="main">
	<div id="wrap_mail">
		<div class="page_back">
			<c:choose>
				<c:when test="${fromPage == 'send' }">
					<a href="${f:url('/scoutMail/list/changeBox/1')}" id="btn_back"><span class="pc_none">スカウトメール一覧へ</span>戻る</a><!-- ※メール種類よってテキスト&リンク変更 -->
				</c:when>
				<c:otherwise>
					<a href="${f:url('/scoutMail/list/showList')}" id="btn_back"><span class="pc_none">スカウトメール一覧へ</span>戻る</a><!-- ※メール種類よってテキスト&リンク変更 -->
				</c:otherwise>
			</c:choose>

		</div>
		<h2 class="name">${scoutMailTargetDto.memberId}<c:if test="${scoutMailTargetDto.exitMemberFlg}">（${scoutMailTargetDto.age}）</c:if><span class="hope_txt"></span>
		</h2>
		<div class="menu_tab">
			<div class="menu_list">
			<ul>
				<li class="tab_active">
					<a href="#detail_Mail">メール画面</a>
				</li>
				<c:if test="${scoutMailTargetDto.exitMemberFlg}">
					<li>
						<a href="#detail_Information">プロフィール</a>
					</li>
				</c:if>
			</ul>
			</div>
		</div>

		<c:if test="${errorMessage != null}">
			<div class="error"><ul><li>${errorMessage}</li></ul></div>
		</c:if>

		<c:if test="${scoutMailTargetDto.exitMemberFlg}">
			<div class="made_mest">
				<div class="made_status">
					<div class="selectbox">
						<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>" />
						<select class="status_bar" name="selectionKbn" id="selectionKbn" onchange="onchangeSelectionKbn('selectionKbn', '${scoutMailLogId}');">
								<c:forEach items="${selectionList}" var="i">
									<c:set var="selected" value="${(i.value eq scoutMailTargetDto.selectionFlg) ? 'selected' : ''}" />
									<option value="${f:h(i.value)}" ${f:h(selected)}>${f:h(i.label)}</option>
								</c:forEach>
						</select>
					</div>
				</div>
				<div class="made_memo">
					<html:text property="memo" value="${f:h(scoutMailTargetDto.memo)}" placeholder="メモを入力" id="memo${scoutMailTargetDto.id}" class="form-bg"/>
					<input type="button" name="editMemo" value="登録" class="btn_entry" onClick="onchangeMemo('memo${scoutMailTargetDto.id }', '${scoutMailTargetDto.id }');">
				</div>
			</div>
		</c:if>
		<div id="wrap_mail_content">
			<div class="tab_area">
				<div class="tab_contents tab_active" id="detail_Mail">
					<div class="message_area" id="scroll-pos">
						<c:forEach var="dto" items="${dataList}" varStatus="status">
							<c:choose>
								<c:when test="${dto.senderKbn != 1}">
									<c:choose>
										<c:when test="${status.last}">
											<div class="message_receive" id="lastMessage">
												<div class="receive_txt">
													<p class="receive_time">${dto.sendDatetime}</p>
													<div class="message_title">件名｜${dto.subject} </div>
													${f:br(gf:editPcLink(dto.body))}&nbsp;
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="message_receive">
												<div class="receive_txt">
													<p class="receive_time">${dto.sendDatetime}</p>
													<div class="message_title">件名｜${dto.subject} </div>
													${f:br(gf:editPcLink(dto.body))}&nbsp;
												</div>
											</div>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${dto.senderKbn == 1}">
									<c:choose>
										<c:when test="${status.last}">
											<div class="message_send" id="lastMessage">
												<div class="send_txt">
													<p class="send_time">${dto.sendDatetime}</p>
													<div class="message_title">件名｜${dto.subject} </div>
													${f:br(gf:editPcLink(dto.body))}&nbsp;
												</div>
												<c:if test="${dto.receiveReadingDatetime != null}">
													<p class="send_ar">既読</p>
												</c:if>
											</div>
										</c:when>
										<c:otherwise>
											<div class="message_send">
												<div class="send_txt">
													<p class="send_time">${dto.sendDatetime}</p>
													<div class="message_title">件名｜${dto.subject} </div>
													${f:br(gf:editPcLink(dto.body))}&nbsp;
												</div>
												<c:if test="${dto.receiveReadingDatetime != null}">
													<p class="send_ar">既読</p>
												</c:if>
											</div>
										</c:otherwise>
									</c:choose>
								</c:when>
							</c:choose>
							<c:if test="${status.last && !scoutMailTargetDto.exitMemberFlg}">
								<div class="member_cancel_message">このユーザーは退会した為、<br class="pc_none">メッセージのやり取りはできません。</div>
							</c:if>
						</c:forEach>
					</div>
					<c:if test="${isSendMailFlg}">
						<s:form action="${f:h(actionPath)}">
						<html:hidden property="scoutMailLogId"/>
						<div class="message_input_area">
							<div class="sub_fiph">
								<div class="messinp_fiph">
									<div class="selectbox">
										<html:select property="sentenceId"  styleId="patternId" >
											<html:optionsCollection name="sentenceList"/>
										</html:select>
									</div>
									<input type="button" name="" value="決定" onclick="selectPattern(); return false;" id="btn_selectPattern">
								</div>
								<div class="messinp_sub">
									<html:text property="subject" placeholder="件名" id="subject" />
								</div>
							</div>
							<div class="matx_send">
								<div class="messinp_matx">
									<html:textarea property="mailBody" rows="4" styleId="mailBody" placeholder="本文"></html:textarea>
								</div>
								<div class="messinp_send">
									<input type="submit" name="conf" value="送信" id="btn_conf">
								</div>
							</div>
						</div>
						</s:form>
					</c:if>
				</div>

				<!-- プロフィール -->
				<div class="tab_contents" id="detail_Information">
					<div class="detail_area">
						<div class="det_profile">
							<div class="l_title"><h3>基本情報</h3></div>
							<div class="r_details">
								<table cellpadding="0" cellspacing="0" border="0" class="detail_table">
									<tbody>
									<tr>
										<th>会員ID</th>
										<td>${f:h(scoutMailTargetDto.memberId)}</td>
									</tr>
									<tr>
										<th>性別</th>
										<td>${f:label(scoutMailTargetDto.sexKbn, sexList, 'value', 'label')} &nbsp;</td>
									</tr>
									<tr>
										<th>年齢</th>
										<td><fmt:formatDate value="${scoutMailTargetDto.birthday}" pattern="yyyy/MM/dd" />&nbsp;&nbsp;${scoutMailTargetDto.age}才&nbsp;</td>
									</tr>
									<tr>
										<th>住所</th>
										<td>
										<gt:prefecturesList name="prefecturesList"  />
										${f:label(scoutMailTargetDto.prefecturesCd, prefecturesList, 'value', 'label')}
										${f:h(scoutMailTargetDto.municipality)}
										&nbsp;</td>
									</tr>
									<tr>
										<th>転勤の可・不可</th>
										<td>${f:label(scoutMailTargetDto.transferFlg, transferFlgList, 'value', 'label')}</td>
									</tr>
									<tr>
										<th>深夜勤務の可・不可</th>
										<td>${f:label(scoutMailTargetDto.midnightShiftFlg, midnightShiftFlgList, 'value', 'label')}</td>
									</tr>
									<tr>
										<th>希望雇用形態</th>
										<td>
											<c:forEach items="${scoutMailTargetDto.employPtnKbnList}" var="employPtnKbn">
												${f:label(employPtnKbn, employPtnKbnList, 'value', 'label')} &nbsp;
											</c:forEach>
										</td>
									</tr>
									<tr>
										<th>希望職種</th>
										<td>
											<c:forEach items="${scoutMailTargetDto.jobKbnList}" var="jobKbn">
												${f:label(jobKbn, jobKbnList, 'value', 'label')} &nbsp;
											</c:forEach>
										</td>
									</tr>
									<tr>
										<th>希望勤務地</th>
										<td>
											<c:forEach var="city" items="${scoutMailTargetDto.hopeCityMap}" varStatus="prefStatus">
												<c:if test="${!prefStatus.first}"><br></c:if>
												${f:label(city.key, prefecturesList, 'value', 'label')}&nbsp;-
												<c:forEach items="${city.value}" var="cityName" varStatus="status">
													${f:h(cityName)}
													<c:if test="${!status.last}">,&nbsp;</c:if>
												</c:forEach>
											</c:forEach>
										</td>
									</tr>
									<tr>
										<th>希望年収</th>
										<td>
											${f:label(scoutMailTargetDto.salaryKbn, salaryKbnList, 'value', 'label')}
										</td>
									</tr>
								</tbody></table>
							</div>
						</div>
						<div class="det_pr">
							<div class="l_title"><h3>自己PR</h3></div>
							<div class="r_details">
								<table cellpadding="0" cellspacing="0" border="0" class="detail_table">
									<tbody><tr>
										<th>飲食業界経験年数</th>
										<td>${f:label(scoutMailTargetDto.foodExpKbn, foogExpKbnList, 'value', 'label')}&nbsp;</td>
									</tr>
									<tr>
										<th class="bdrs_bottom">マネジメント経験</th>
										<td class="bdrs_bottom">役職：${f:label(scoutMailTargetDto.expManagerKbn, expManagerKbnList, 'value', 'label')}&nbsp;<br>
											経験年数：${f:label(scoutMailTargetDto.expManagerYearKbn, expManagerYearKbnList, 'value', 'label')}&nbsp;<br>
											人数：${f:label(scoutMailTargetDto.expManagerPersonsKbn, expManagerPersonsKbnList, 'value', 'label')}&nbsp;<br>
										</td>
									</tr>
									<tr>
										<th>海外勤務経験</th>
										<td>${f:label(scoutMailTargetDto.foreignWorkFlg, foreignWorkFlgKbnList, 'value', 'label')}</td>
									</tr>
									<tr>
										<th>取得資格</th>
										<td>
											<c:forEach var="qualificationKbn" items="${scoutMailTargetDto.qualificationKbnList}" varStatus="status">
												${f:label(qualificationKbn, qualificationList, 'value', 'label')}
												<c:if test="${status.last ne true}">
													<br>
												</c:if>
											</c:forEach>
										</td>
									</tr>
									<tr>
										<th>自己PR</th>
										<td>${f:br(f:h(scoutMailTargetDto.scoutSelfPr))}</td>
									</tr>
								</tbody></table>
							</div>
						</div>
						<c:if test="${scoutMailTargetDto.schoolHistoryExistFlg}">
							<div class="det_jobcareer">
								<div class="l_title"><h3>学歴</h3></div>
								<div class="r_details">
									<table cellpadding="0" cellspacing="0" border="0" class="detail_table"><tbody>
										<tr>
											<th class="bdrs_bottom">最終学歴</th>
											<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD %>"/>
											<td class="bdrs_bottom">学校名：${f:h(scoutMailTargetDto.schoolName)}<br>
												学部・学科：${f:h(scoutMailTargetDto.department)}<br>
												現在の状況：${f:label(scoutMailTargetDto.graduationKbn, graduationKbnList, 'value', 'label')}<br>
											</td>
										</tr>
									</tbody></table>
								</div>
							</div>
						</c:if>
						<c:if test="${careerHistoryList.size() > 0}">
							<c:forEach items="${careerHistoryList}" var="dto" varStatus="status">
								<div class="det_jobcareer">
									<div class="l_title"><h3>職務経歴${status.count}</h3></div>
									<div class="r_details">
										<table cellpadding="0" cellspacing="0" border="0" class="detail_table"><tbody>
											<tr>
												<th>会社名</th>
												<td>${f:h(dto.companyName)}</td>
											</tr>
											<tr>
												<th>職種</th>
												<td>${f:br(f:h(dto.jobKbnName))}</td>
											</tr>
											<tr>
												<th>業態</th>
												<td>${f:br(f:h(dto.industryKbnName))}</td>
											</tr>
											<tr>
												<th>勤務期間</th>
												<td>${f:br(f:h(dto.workTerm))}</td>
											</tr>
											<tr>
												<th>業務内容</th>
												<td>${f:br(f:h(dto.businessContent))}</td>
											</tr>
											<tr>
												<th>客席数</th>
												<td>${f:br(f:h(dto.seat))}</td>
											</tr>
											<tr>
												<th>月売上</th>
												<td>${f:br(f:h(dto.monthSales))}</td>
											</tr>
										</tbody></table>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- #main# -->
<script>
	$('.message_area').scrollTop(($("#lastMessage").offset().top - 200));
</script>
<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>
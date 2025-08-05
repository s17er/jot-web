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
				arbeitApplicationId : id,
				selectionKbn : kbn.val()
		};

		$.ajax({
			url: '${f:url("/arbeitMail/list/ajaxSelectionFlg/")}',
			data: selectionJson,
			type: 'post'
		});

	};

	var onchangeMemo = function(selectId, id) {

		var kbn = $("#" + selectId);

		var selectionJson = {
				arbeitApplicationId : id,
				memo : kbn.val()
		};

		$.ajax({
			url: '${f:url("/arbeitMail/list/editMemo/")}',
			data: selectionJson,
			type: 'post'
		});
	};
// ]]>
</script>
<script type="text/javascript">
<!--

$(function(){
	var submitFlg = true;
	$("#btn_conf").on('click', function(){
		if(submitFlg) {
			submitFlg = false;
			return ture;
		} else {
			return false;
		}
	});
});


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
					<a href="${f:url('/arbeitMail/list/sendBox')}" id="btn_back"><span class="pc_none">グルメdeバイト応募メール一覧へ</span>戻る</a><!-- ※メール種類よってテキスト&リンク変更 -->
				</c:when>
				<c:otherwise>
					<a href="${f:url('/arbeitMail/list/showList')}" id="btn_back"><span class="pc_none">グルメdeバイト応募メール一覧へ</span>戻る</a><!-- ※メール種類よってテキスト&リンク変更 -->
				</c:otherwise>
			</c:choose>
		</div>
		<h2 class="name">${arbeitApplication.name }（${arbeitApplication.age}）<span class="hope_txt"></span>
		</h2>
		<div class="menu_tab">
			<div class="menu_list">
			<ul>
				<li class="tab_active">
					<a href="#detail_Mail">メール画面</a>
				</li>
				<li>
					<a href="#detail_Information">プロフィール</a>
				</li>
			</ul>
			</div>
		</div>

		<c:if test="${errorMessage != null}">
			<div class="error"><ul><li>${errorMessage}</li></ul></div>
		</c:if>

		<div class="made_mest">
			<div class="made_status">
				<div class="selectbox">
					<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>" />
					<select class="status_bar" name="selectionKbn" id="selectionKbn" onchange="onchangeSelectionKbn('selectionKbn', '${arbeitApplication.id }');">
							<c:forEach items="${selectionList}" var="i">
								<c:set var="selected" value="${(i.value eq arbeitApplication.selectionFlg) ? 'selected' : ''}" />
								<option value="${f:h(i.value)}" ${f:h(selected)}>${f:h(i.label)}</option>
							</c:forEach>
					</select>
				</div>
			</div>
			<div class="made_memo">
				<html:text property="memo" value="${f:h(arbeitApplication.memo)}" placeholder="メモを入力" id="memo${arbeitApplication.id}" class="form-bg"/>
				<input type="button" name="editMemo" value="登録" class="btn_entry" onClick="onchangeMemo('memo${arbeitApplication.id }', '${arbeitApplication.id }');">
			</div>
		</div>
		<div id="wrap_mail_content">
			<div class="tab_area">
				<div class="tab_contents tab_active" id="detail_Mail">
					<div class="message_area" id="scroll-pos">
						<c:forEach var="dto" items="${mailList}" varStatus="status">
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
						</c:forEach>
					</div>
					<s:form action="${f:h(actionPath)}">
					<html:hidden property="arbeitApplicationId"/>
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
				</div>

				<!-- プロフィール -->
				<div class="tab_contents" id="detail_Information">
					<div class="detail_area">
						<div class="det_profile">
							<div class="l_title"><h3>基本情報</h3></div>
							<div class="r_details">
								<table cellpadding="0" cellspacing="0" border="0" class="detail_table">
									<tbody><tr>
										<th>応募日時</th>
										<td><fmt:formatDate value="${arbeitApplication.applicationDatetime}" pattern="yyyy/MM/dd HH:mm" /></td>
									</tr>
									<tr>
										<th>グルメdeバイト応募ID</th>
										<td>${f:h(arbeitApplication.id)} &nbsp;</td>
									</tr>
									<tr>
										<th>氏名(漢字）</th>
										<td>${f:h(arbeitApplication.name)} &nbsp;</td>
									</tr>
									<tr>
										<th>氏名(カナ）</th>
										<td>${f:h(arbeitApplication.nameKana)} &nbsp;</td>
									</tr>
									<tr>
										<th>性別</th>
										<td>${f:label(arbeitApplication.sexKbn, sexList, 'value', 'label')} &nbsp;</td>
									</tr>
									<tr>
										<th>年齢</th>
										<td>${f:h(arbeitApplication.age)}才&nbsp;</td>
									</tr>
									<tr>
										<th>住所</th>
										<td>
										<gt:prefecturesList name="prefecturesList"  />
										${f:label(arbeitApplication.prefecturesCd, prefecturesList, 'value', 'label')}
										${f:h(arbeitApplication.municipality)}
										${f:h(arbeitApplication.address)}
										&nbsp;</td>
									</tr>
									<tr>
										<th>電話番号</th><td>${f:h(arbeitApplication.phoneNo)}</td>
									</tr>
									<tr>
										<th>メールアドレス</th>
										<td>${f:h(arbeitApplication.mailAddress)} &nbsp;</td>
									</tr>
									<tr>
										<th>現在の職業</th>
										<gt:typeList name="currentList" typeCd="<%=MTypeConstants.CurrentJob.TYPE_CD %>"/>
										<td>${f:label(arbeitApplication.currentJobKbn, currentList, 'value', 'label')}</td>
									</tr>
									<tr>
										<th>勤務可能時期</th>
										<gt:typeList name="possibleList" typeCd="<%=MTypeConstants.PossibleWorkTermKbn.TYPE_CD %>" />
										<td>${f:label(arbeitApplication.possibleEntryTermKbn, possibleList, 'value', 'label')}</td>
									</tr>
									<tr>
										<th>飲食店勤務の経験</th>
										<gt:typeList name="ariNashiList" typeCd="<%=MTypeConstants.AriNashiKbn.TYPE_CD %>"/>
										<td>${f:label(arbeitApplication.foodExpKbn, ariNashiList, 'value', 'label')}</td>
									</tr>
									<tr>
										<th>希望連絡時間・連絡方法</th>
										<td>${f:h(arbeitApplication.connectionTime)} &nbsp;</td>
									</tr>
									<tr>
										<th>自己PR</th>
										<td>${f:h(arbeitApplication.applicationSelfPr)} &nbsp;</td>
									</tr>
								</tbody></table>
							</div>
						</div>
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
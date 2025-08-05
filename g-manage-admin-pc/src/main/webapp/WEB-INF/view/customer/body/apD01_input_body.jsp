<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page import="com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<c:set var="SCOUT_MAIL_LOG_KBN_ADD_MANUAL" value="<%=MTypeConstants.ScoutMailLogKbn.ADD_MANUAL %>" scope="page" />
<%@page import="org.seasar.framework.container.factory.SingletonS2ContainerFactory" %>
<%@page import="org.seasar.framework.container.S2Container" %>
<%@page import="org.seasar.framework.util.ClassUtil" %>
<%@page import="org.seasar.framework.util.FieldUtil" %>
<%
	// WEBIDを取得
	String webId = (String) session.getAttribute(CustomerForm.SESSION_KEY.WEB_ID);
	pageContext.setAttribute("webId", webId);
%>

<gt:areaList name="area" />
<gt:assignedCompanyList name="assignedCompany" limitValue="${areaCd}" authLevel="${userDto.authLevel}"  blankLineLabel="--" />
<gt:assignedSalesList name="assignedSales" limitValue="${assignedCompanyId}"  blankLineLabel="--" />
<gt:prefecturesList name="prefectures" blankLineLabel="--" />
<gt:companyList name="companyList" />
<gt:salesList name="salesList" />
<gt:typeList name="submailReceptionFlgList" typeCd="<%=MTypeConstants.SubmailReceptionFlg.TYPE_CD %>" />
<gt:typeList name="loginFlgList" typeCd="<%=MTypeConstants.LoginFlg.TYPE_CD %>" />
<gt:typeList name="publicationFlgList" typeCd="<%=MTypeConstants.PublicationFlg.TYPE_CD %>" />
<gt:typeList name="scoutUseFlgList" typeCd="<%=MTypeConstants.ScoutUseFlg.TYPE_CD %>" />
<gt:typeList name="publicationEndDisplayFlgList" typeCd="<%=MTypeConstants.PublicationEndDisplayFlg.TYPE_CD %>" />
<gt:typeList name="mailMagReceptionFlgList" typeCd="<%=MTypeConstants.CustomerMailMagazineReceptionFlg.TYPE_CD %>"/>
<gt:typeList name="shutokenForeignAreaKbnList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>" />

<c:set var="DATE_FORMAT" value="<%=GourmetCareeConstants.DATE_FORMAT_SLASH %>" scope="page" />


<%	/* CSSファイルを設定 */	 %>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/thickbox.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<%	/* javascriptファイルを設定 */	 %>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/thickbox.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>

<!-- フォーム補助用JS -->
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/validationEngine.jquery.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.autoKana.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.validationEngine-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery.validationEngine.js"></script>
 <script src="https://yubinbango.github.io/yubinbango/yubinbango.js" charset="UTF-8"></script>
<script type="text/javascript">
$(function(){
	//フリガナ補完
	$.fn.autoKana('#customerName', '#customerNameKana', {
		katakana : true
	});
	$.fn.autoKana('#contactName', '#contactNameKana', {
		katakana : true
	});

	//フォームバリデーション
	$("#AddAssigned").validationEngine();
});
</script>
<!-- //フォーム補助用JS -->

<script type="text/javascript">

<!--
<c:choose>
	<c:when test="${pageKbn ne PAGE_EDIT}">
	$(document).ready(function(){

		areaLimitLoad ();
	});

	</c:when>

</c:choose>
	$(document).ready(function(){
		//copyScoutCount();
		$("#scoutManualEndDate").datepicker();
		$("#scoutUseStartDatetime").datepicker();
	});


	function copyScoutCount() {
		var scoutCountHidden = $("#scoutCountHidden");
		var scoutAddText = $("#scoutAddText");
		var scoutCount = scoutCountHidden.val();

		if (isInt(scoutCount)) {
			scoutAddText.val(scoutCount);
			return;
		}

		scoutCountHidden.val(0);
		scoutAddText.val(0);

	}
	// optionの初期値
	var initOption = '<option value="">--</option>';

	// Ajaxの設定
	// エリア選択時の連動
	function areaLimitLoad () {
		window.focus();

		// 会社の連動
		$('#assignedCompanyAjax').load('${f:url(assignedCompanyAjaxPath)}',{'limitValue': ''},
			// 営業担当者の初期化
			function(responseText, status, XMLHttpRequest) {
				$('#assignedSalesAjax').load('${f:url(assignedSalesAjaxPath)}',{'limitValue': ''});
			}
		);
		$('#pathId').css('display', 'inline');
	}

	// 会社選択時の連動
	function assignedCompanyLimitLoad () {
		window.focus();
		if ($('#assignedCompanyId').val() == "") {
			// 営業担当者の初期化
			$('#assignedSalesId').children().remove();
			$('#assignedSalesId').append(initOption);

		} else {
			setAjaxParts('${f:url(assignedSalesAjaxPath)}', 'assignedSalesAjax', $("#assignedCompanyId").val());
		};
	}

	<%// TODO %>
	function onClickScoutAddButton(addNum) {
		var scoutCountHidden = $("#scoutCountHidden");
		var scoutCount = getIntValue(scoutCountHidden.val(), 0)
							+ getIntValue(addNum, 0);

		scoutCountHidden.val(scoutCount);
		$("#scoutAddText").val(scoutCount);
	}

	function clearScoutCount() {
		$("#scoutCountHidden").val(0);
		$("#scoutAddText").val(0);
	}

	function isInt(value) {
		var num = parseInt(value);
		return isNaN(num) === false;
	}

	function getIntValue(value, defaultNum) {
		if (defaultNum == undefined || defaultNum == null) {
			defaultNum = 0;
		}

		if (isInt(value)) {
			return parseInt(value);
		}
		return defaultNum;
	}

// -->
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title customer">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

	<!-- #wrap_form# -->
	<div id="wrap_form">

		<s:form action="${f:h(actionPath)}" styleId="AddAssigned" styleClass="h-adr">
			<span class="p-country-name" style="display:none;">Japan</span>
			<html:hidden property="hiddenId" />

			<c:if test="${existDataFlg eq true}">

			<!-- ################# 顧客情報 ################# -->
			<h3 class="subtitle">顧客情報</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<tr>
							<th width="140">顧客ID</th>
							<th width="20"></th>
							<td>${f:h(id)}&nbsp;</td>
						</tr>
						<tr>
							<th>登録日</th>
							<th></th>
							<td>${f:h(registrationDatetime)}&nbsp;</td>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<th>顧客名</th>
					<th><span class="attention">必須</span></th>
					<td><html:text property="customerName" styleId="customerName" styleClass="validate[required]" /></td>
				</tr>
				<tr>
					<th>顧客名（カナ）</th>
					<th><span class="attention">必須</span></th>
					<td><html:text property="customerNameKana" styleClass="active validate[required]" styleId="customerNameKana" /></td>
				</tr>
				<tr>
					<th>エリア</th>
					<th><span class="attention">必須</span></th>
					<td>
						<ul class="checklist_3col clear">
							<c:forEach items="${area}" var="a">
								<li><label><html:radio property="areaCd" value="${a.value}" styleId="areaCd_${a.value}" styleClass="validate[required]" />${f:h(a.label)}</label></li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<tr>
					<th>会社名＜公開側表示用＞</th>
					<th></th>
					<td><html:text property="displayCompanyName" styleId="displayCompanyName"/></td>
				</tr>
				<tr>
					<th>住所</th>
					<th></th>
					<td>
						<table class="nbr">
							<tr>
								<th>郵便番号：</th>
								<td>
									<html:text property="zipCd" size="10" maxlength="8" styleClass="disabled p-postal-code hankaku validate[custom[zip]]" data-prompt-position="topLeft" placeholder="(例)123-4567"/>
								</td>
							</tr>
							<tr>
								<th>都道府県：</th>
								<td>
									<html:select property="prefecturesCd" styleClass="p-region-id" >
										<html:optionsCollection name="prefectures"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<th>市区町村：</th>
								<td>
									<html:text property="municipality" placeholder="例)中央区" styleClass="p-locality" />
								</td>
							</tr>
							<tr>
								<th>町名番地・ビル名：</th>
								<td>
									<html:text property="address" placeholder="1-8-21 第21中央ビル3F" styleClass="p-street-address p-extended-address" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<th><span class="attention">必須</span></th>
					<td class="release">
						<html:text property="phoneNo1" size="5" styleClass="disabled hankaku validate[required,custom[phone]]" data-prompt-position="topLeft" />&nbsp;-&nbsp;
						<html:text property="phoneNo2" size="5" styleClass="disabled hankaku validate[condRequired[phoneNo1],custom[phone]]" data-prompt-position="topLeft" />&nbsp;-&nbsp;
						<html:text property="phoneNo3" size="5" styleClass="disabled hankaku validate[condRequired[phoneNo2],custom[phone]]" data-prompt-position="topLeft" />
					</td>
				</tr>
				<tr>
					<th>FAX番号</th>
					<th></th>
					<td class="release">
						<html:text property="faxNo1" size="5" styleClass="disabled hankaku validate[custom[phone]]" data-prompt-position="topLeft" />&nbsp;-&nbsp;
						<html:text property="faxNo2" size="5" styleClass="disabled hankaku validate[condRequired[faxNo1],custom[phone]]" data-prompt-position="topLeft" />&nbsp;-&nbsp;
						<html:text property="faxNo3" size="5" styleClass="disabled hankaku validate[condRequired[faxNo2],custom[phone]]" data-prompt-position="topLeft" /></td>
				</tr>
				<tr>
					<th>会社情報</th>
					<th></th>
					<td>
						<table class="nbr">
							<tbody>
							<tr>
								<th>設立：</th>
								<td>
									<html:text property="establishment" placeholder="例)平成14年8月" />
								</td>
								</tr>
								<tr>
								<th>資本金：</th>
								<td>
									<html:text property="capital" placeholder="例)5,000万円" />
								</td>
								</tr>
								<tr>
								<th>代表者：</th>
								<td>
									<html:text property="representative" placeholder="例)山田　太郎" />
								</td>
								</tr>
								<tr>
								<th>従業員：</th>
								<td>
									<html:text property="employee" placeholder="例)80名" />
								</td>
								</tr>
								<tr>
								<th>会社情報：</th>
								<td>
									<html:textarea property="businessContent" cols="5" rows="6" style="width:450px;" placeholder="例）
1.飲食店経営
2.レストランファンド経営
3.店舗開発コンサルティング
4.英会話教室経営"></html:textarea>
								</td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<th>ホームページ</th><!-- デフォルトでnofollow -->
					<th></th>
					<td>
						<table class="nbr">
							<tbody>
							<c:forEach items="${homepageDtoList}" var="dto" varStatus="status">
								<tr>
									<th>${status.index + 1}&nbsp;&nbsp;&nbsp;URL：</th>
									<td>
										<html:text property="homepageDtoList[${status.index}].url" styleClass="hankaku inactive txtInput490 validate[custom[url]]" placeholder="http://～" />
							        </td>
								</tr>
								<tr>
									<th>説明：</th>
									<td>
										<html:text property="homepageDtoList[${status.index}].comment" styleClass="inactive txtInput490" />
							        </td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</td>
				</tr>
				<c:choose>
					<c:when test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
						<tr>
							<th>スカウトメール使用可否</th>
							<th><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
								<c:forEach items="${scoutUseFlgList}" var="t">
									<c:choose>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
										<li><label><html:radio property="scoutUseFlg" value="${t.value}" styleId="scoutUseFlg_${t.value}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
									</c:when>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_STAFF}">
										<li><label><html:radio property="scoutUseFlg" value="${t.value}" styleId="scoutUseFlg_${t.value}" styleClass="disabled"  disabled="true"  />${f:h(t.label)}</label></li>
									</c:when>
									</c:choose>
								</c:forEach>
								</ul>
							</td>
						</tr>
						<c:choose>
							<c:when test="${pageKbn eq PAGE_INPUT}">
								<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
									<tr>
										<th>スカウトメール</th>
										<th><span class="attention">必須</span></th>
										<td class="release">
											<table>
												<tr>
													<td style="border:none;">
														有料(6カ月有効)：
													</td>
													<td style="border:none;">
														<html:text property="scoutUseStartDatetime" styleId="scoutUseStartDatetime" placeholder="利用開始日を選択"/>
													</td>
													<td style="border:none;">
														合計:&nbsp;<html:text property="scoutAddCount" size="2"/>通
													</td>
												</tr>
												<tr>
													<td style="border:none;">
														有料(無制限)：
													</td>
													<td style="border:none;">
														<html:text property="scoutUseEndDatetime" styleId="scoutManualEndDate" placeholder="利用終了日を選択"/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</c:if>
								<c:if test="${userDto.authLevel eq AUTH_LEVEL_STAFF}">
									<tr>
										<th>スカウトメール</th>
										<th><span class="attention">必須</span></th>
										<td class="release">
											<table>
												<tr>
													<td style="border:none;">
														有料(6カ月有効)：
													</td>
													<td style="border:none;">
														<html:text property="scoutUseStartDatetime" styleId="scoutUseStartDatetime" styleClass="disabled" placeholder="利用開始日を選択" disabled="true"/>
													</td>
													<td style="border:none;">
														合計:&nbsp;<html:text property="scoutAddCount" size="2" styleClass="disabled" disabled="true"/>通
													</td>
												</tr>
												<tr>
													<td style="border:none;">
														有料(無制限)：
													</td>
													<td style="border:none;">
														<html:text property="scoutUseEndDatetime" styleId="scoutManualEndDate" styleClass="disabled" placeholder="利用終了日を選択" disabled="true" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</c:if>
							</c:when>
							<c:when test="${pageKbn eq PAGE_EDIT}">
								<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALES}">
									<tr>
										<th>
											スカウトメール<br />
											<span class="attention">追加する場合のみ追加件数を入力</span>
										</th>
										<th><span class="attention">必須</span></th>
										<td class="release">
										<%// TODO %>
											<table>
												<tr>
													<td style="border:none;">
														有料(6カ月有効)：
													</td>
													<td style="border:none;">
														<html:text property="scoutUseStartDatetime" styleId="scoutUseStartDatetime" placeholder="利用開始日を選択"/>
													</td>
													<td style="border:none;">
														合計:&nbsp;<html:text property="scoutAddCount" size="2"/>通
														削除:&nbsp;<html:text property="scoutRemoveCount" size="2"/>通
													</td>
												</tr>
												<tr>
													<td style="border:none;">
														有料(無制限)：
													</td>
													<td style="border:none;">
														<html:text property="scoutUseEndDatetime" styleId="scoutManualEndDate" placeholder="利用終了日を選択"/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</c:if>
								<tr>
									<th>残りスカウトメール数</th>
									<th></th>
									<td class="release">
										<c:choose>
											<c:when test="${existRemainScoutMail}">
												<c:if test="${freeRemainScoutMail gt 0}">
													無料分 ${f:h(freeRemainScoutMail)}通<br />
												</c:if>
												<c:if test="${existRemainBoughtScoutMail}">
													購入分
													<c:forEach items="${boughtScoutMailRemainDtoList}" var="dto" varStatus="status">
														${f:h(dto.scoutRemainCount)}通 (<fmt:formatDate value="${dto.useEndDatetime}" pattern="${DATE_FORMAT}" />)
														<c:if test="${status.last eq false}">
														/
														</c:if>
													</c:forEach>
													<br />
												</c:if>
												<c:if test="${ unlimitedRemainScoutMail gt 0}">
													無期限分 ${f:h(unlimitedRemainScoutMail)}通<br />
												</c:if>
											</c:when>
											<c:otherwise>
												0通
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<th>スカウトメール追加履歴</th>
									<th></th>
									<td>
										<gt:typeList name="scoutMailKbnList" typeCd="<%=MTypeConstants.ScoutMailKbn.TYPE_CD %>"/>
										<c:forEach items="${scoutMailAddHistoryList}" var="dto">
											${f:h(dto.addDate)}&nbsp;${f:h(dto.addCount)}通&nbsp;<!--
											 -->(${f:label(dto.scoutMailKbn, scoutMailKbnList, 'value', 'label')}<!--
											<c:if test="${dto.scoutMailLogKbn eq SCOUT_MAIL_LOG_KBN_ADD_MANUAL}">
												 -->・手動<!--
											</c:if>
											 -->)<br />
										</c:forEach>
									</td>
								</tr>
							</c:when>
						</c:choose>

						<tr>
							<th class="">掲載終了後のページ表示</th>
							<th><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
								<c:forEach items="${publicationEndDisplayFlgList}" var="t">
									<c:choose>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
										<li><label><html:radio property="publicationEndDisplayFlg" value="${t.value}" styleId="publicationEndDisplayFlg_${t.value}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
									</c:when>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_STAFF}">
										<li><label><html:radio property="publicationEndDisplayFlg" value="${t.value}" styleId="publicationEndDisplayFlg_${t.value}" styleClass="disabled"  disabled="true"  />${f:h(t.label)}</label></li>
									</c:when>
									</c:choose>
								</c:forEach>
								</ul>
							</td>
						</tr>
						<tr>
							<th>備考</th>
							<th></th>
							<td><html:textarea property="note" cols="60" rows="10" ></html:textarea></td>
						</tr>
					</c:when>
					<c:when test="${userDto.authLevel eq AUTH_LEVEL_AGENCY}">
						<tr>
							<th class="">掲載終了後のページ表示</th>
							<th><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
								<c:forEach items="${publicationEndDisplayFlgList}" var="t">
									<c:choose>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_AGENCY or userDto.authLevel eq AUTH_LEVEL_SALES}">
										<li><label><html:radio property="publicationEndDisplayFlg" value="${t.value}" styleId="publicationEndDisplayFlg_${t.value}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
									</c:when>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_STAFF}">
										<li><label><html:radio property="publicationEndDisplayFlg" value="${t.value}" styleId="publicationEndDisplayFlg_${t.value}" styleClass="disabled"  disabled="true"  />${f:h(t.label)}</label></li>
									</c:when>
									</c:choose>
								</c:forEach>
								</ul>
							</td>
						</tr>
					</c:when>
				</c:choose>
			</table>

			<!-- ################# 担当者情報 ################# -->
			<h3 class="subtitle">担当者情報</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="140">担当者名</th>
					<th width="20"><span class="attention">必須</span></th>
					<td><html:text property="contactName" styleId="contactName" styleClass="validate[required]" /></td>
				</tr>
				<tr>
					<th>担当者名（カナ）</th>
					<th><span class="attention">必須</span></th>
					<td><html:text property="contactNameKana" styleClass="active validate[required]" styleId="contactNameKana" /></td>
				</tr>
				<tr>
					<th>担当者部署</th>
					<th></th>
					<td><html:text property="contactPost" /></td>
				</tr>
				<tr>
					<th>メインアドレス</th>
					<th><span class="attention">必須</span></th>
					<td><html:text property="mainMail" styleClass="disabled hankaku validate[required]" /></td>
				</tr>
				<tr>
					<th>サブアドレス</th>
					<th></th>
					<td>
						<c:forEach items="${subMailDtoList}" var="dto" varStatus="status">
							<html:text property="subMailDtoList[${status.index}].subMail" styleClass="disabled hankaku" styleId="subMail_${status.index}" />&nbsp;
							<c:forEach items="${submailReceptionFlgList}" var="t">
								<html:radio property="subMailDtoList[${status.index}].submailReceptionFlg" value="${t.value}"
									styleId="submailReceptionFlg_${t.value}_${status.index}" styleClass="release validate[condRequired[subMail_${status.index}]]" data-prompt-position="topLeft" />&nbsp;<label for="submailReceptionFlg_${t.value}_${status.index}">${f:h(t.label)}</label>&nbsp;&nbsp;&nbsp;
							</c:forEach>
							<c:if test="${!status.last}"><br><br></c:if>
						</c:forEach>
			        </td>
				</tr>
				<tr>
					<th>ログインID</th>
					<th><span class="attention">必須</span></th>
					<td>
						<html:text property="loginId" styleClass="disabled hankaku validate[required,custom[loginId],minSize[6]]" /><br />
						<span class="attention">※半角英数字（0～9、A～Z）、6文字以上で入力して下さい。</span>
					</td>
				</tr>
				<tr>
					<c:if test="${pageKbn eq PAGE_INPUT}">
						<th>パスワード</th>
						<th><span class="attention">必須</span></th>
						<td>
							<html:password property="password" styleClass="disabled validate[required,custom[onlyLetterNumber],minSize[6],maxSize[20]]" styleId="password" maxlength="20" /><br />
							<span class="explain">▼確認のため再度入力して下さい。</span><br />
							<html:password property="rePassword" styleClass="disabled validate[required,equals[password]]" maxlength="20" /><br />
							<span class="attention">※半角英数字（0～9、A～Z）、6～20文字で入力して下さい。</span>
						</td>
					</c:if>
					<c:if test="${pageKbn eq PAGE_EDIT}">
						<th>パスワード</th>
						<th></th>
						<td>
							<html:password property="password" styleClass="disabled validate[custom[onlyLetterNumber],minSize[6],maxSize[20]]" styleId="password" maxlength="20" /><br />
							<span class="explain">▼確認のため再度入力して下さい。</span><br />
							<html:password property="rePassword" styleClass="disabled validate[equals[password]]" maxlength="20" /><br />
							<span class="attention">※半角英数字（0～9、A～Z）、6～20文字で入力して下さい。</span>
						</td>
					</c:if>
				</tr>
				<c:choose>
					<c:when test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
						<tr>
							<th>ログイン可否</th>
							<th><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
									<c:forEach items="${loginFlgList}" var="t">
										<c:choose>
										<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
											<li><label><html:radio property="loginFlg" value="${t.value}" styleId="loginFlg_${t.value}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
										</c:when>
										<c:when test="${userDto.authLevel eq AUTH_LEVEL_STAFF}">
											<li><label><html:radio property="loginFlg" value="${t.value}" styleId="loginFlg_${t.value}" styleClass="disabled"  disabled="true" />${f:h(t.label)}</label></li>
										</c:when>
										</c:choose>
									</c:forEach>
								</ul>
							</td>
						</tr>
						<tr id="inputCompany0">
							<th>担当会社&nbsp;/&nbsp;営業担当者</th>
							<th><span class="attention">必須</span></th>
							<td class="release ajaxWrap" >
								<div id="assignedCompanyAjax">
									<html:select property="assignedCompanyId" styleId="assignedCompanyId"  onchange="assignedCompanyLimitLoad(); return false;">
										<html:option value="">--</html:option>
										<html:optionsCollection name="companyList"/>
									</html:select>
									&nbsp;
								</div>
								<div id="assignedSalesAjax">
									<c:choose>
										<c:when test="${assignedCompanyId eq null || assignedCompanyId eq ''}" >
											<html:select property="assignedSalesId" styleId="assignedSalesId" styleClass="validate[required]">
												<html:option value="">--</html:option>
											</html:select>
										</c:when>
										<c:otherwise>
											<html:select property="assignedSalesId" styleId="assignedSalesId" styleClass="validate[required]">
												<html:optionsCollection name="assignedSales"/>
											</html:select>
										</c:otherwise>
									</c:choose>
								</div>
								&nbsp;
								<html:submit property="addAssigned" value="追加" />
							</td>
						</tr>
						<c:forEach items="${companySalesList}" var="dto">
							<tr>
								<th>担当会社&nbsp;/&nbsp;営業担当者</th>
								<th></th>
								<td class="release">${f:label(dto.companyId, companyList, 'value', 'label')}：&nbsp;${f:label(dto.salesId, salesList, 'value', 'label')}&nbsp;&nbsp;
								<html:submit property="deleteAssigned" value="削除" onclick="if(!confirm('削除してもよろしいですか?')) {return false;};" />
								<html:hidden property="deleteSalesId" value="${dto.salesId}" />
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:when test="${userDto.authLevel eq AUTH_LEVEL_AGENCY}">
						<tr>
							<th>ログイン可否</th>
							<th><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
									<c:forEach items="${loginFlgList}" var="t">
										<c:choose>
										<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_AGENCY or userDto.authLevel eq AUTH_LEVEL_SALES}">
											<li><label><html:radio property="loginFlg" value="${t.value}" styleId="loginFlg_${t.value}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
										</c:when>
										<c:when test="${userDto.authLevel eq AUTH_LEVEL_STAFF}">
											<li><label><html:radio property="loginFlg" value="${t.value}" styleId="loginFlg_${t.value}" styleClass="disabled"  disabled="true" />${f:h(t.label)}</label></li>
										</c:when>
										</c:choose>
									</c:forEach>
								</ul>
							</td>
						</tr>
					</c:when>
				</c:choose>
			</table>

			<!-- ################# メールマガジン設定 ################# -->
			<h3 class="subtitle">メールマガジン設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:choose>
					<c:when test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
						<tr>
							<th width="140">受信可否</th>
							<th width="20"><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
									<c:forEach items="${mailMagReceptionFlgList}" var="t">
										<li><label><html:radio property="mailMagazineReceptionFlg" value="${f:h(t.value)}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
									</c:forEach>
								</ul>
								<span class="attention">※受信可のサブアドレスにも配信されます。</span>
							</td>
						</tr>
						<tr>
							<th class="">メールマガジンエリア</th>
							<th><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
									<c:forEach items="${area}" var="t">
									<li>
										<label><html:multibox property="mailMagazineAreaCdList" value="${f:h(t.value)}" styleClass="validate[required]" data-prompt-position="topLeft" />${f:h(t.label)}版</label>
									</li>
									</c:forEach>
								</ul>
							</td>
						</tr>
						<c:if test="${pageKbn ne PAGE_INPUT}">
							<tr>
								<th>店舗エリア</th>
								<th></th>
								<td>
									<c:if test="${not empty shopListPrefecturesCdList}">
										<h4 class="chara">国内</h4>
										<ul class="shoplist clear">
											<c:forEach items="${shopListPrefecturesCdList}" var="todoufuken">
												<li>${f:label(todoufuken, prefectures, 'value', 'label')}</li>
											</c:forEach>
										</ul>
									</c:if>
									<c:if test="${not empty shopListShutokenForeignAreaKbnList}">
										<h4 class="chara">海外</h4>
										<ul class="shoplist clear">
											<c:forEach items="${shopListShutokenForeignAreaKbnList}" var="shutokenForeignAreaKbn">
												<li>${f:label(shutokenForeignAreaKbn, shutokenForeignAreaKbnList, 'value', 'label')}</li>
											</c:forEach>
										</ul>
									</c:if>
								</td><!-- 系列店舗に設定されている所在地域をテキスト表示 -->
							</tr>
							<tr>
								<th>店舗業態</th>
								<th></th>
								<td>
									<ul class="shoplist clear">
										<gt:typeList name="industryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"/>
										<c:forEach items="${shopListIndustryKbnList}" var="industryKbn">
											<li>${f:label(industryKbn, industryKbnList, 'value', 'label')}</li>
										</c:forEach>
									</ul>
								</td><!-- 系列店舗に設定されている業態をテキスト表示 -->
							</tr>
							<tr>
								<th>店舗数</th>
								<th></th>
								<td>
									<c:if test="${shopListCount > 0}">
										${f:h(shopListCount)}店舗
									</c:if>
									<c:if test="${shopListCount eq 0}">
										未登録
									</c:if>
								</td>
							</tr>
						</c:if>
					</c:when>
					<c:when test="${userDto.authLevel eq AUTH_LEVEL_AGENCY}">
						<tr>
							<th width="140">受信可否</th>
							<th width="20"><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
									<c:forEach items="${mailMagReceptionFlgList}" var="t">
										<li><label><html:radio property="mailMagazineReceptionFlg" value="${f:h(t.value)}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
									</c:forEach>
								</ul>
								<span class="attention">※受信可のサブアドレスにも配信されます。</span>
							</td>
						</tr>
						<tr>
							<th class="">メールマガジンエリア</th>
							<th><span class="attention">必須</span></th>
							<td class="release">
								<ul class="checklist_3col clear">
									<c:forEach items="${area}" var="t">
									<li>
										<label><html:multibox property="mailMagazineAreaCdList" value="${f:h(t.value)}" styleClass="validate[required]" data-prompt-position="topLeft" />${f:h(t.label)}版</label>
									</li>
									</c:forEach>
								</ul>
							</td>
						</tr>
					</c:when>
				</c:choose>
			</table>

			<c:if test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
				<br><br>

				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
					<tr>
						<th width="140">顧客分類</th>
						<th width="20"><span class="attention">必須</span></th>
						<td class="release">
							<ul class="checklist_3col clear">
								<gt:typeList name="customerTestFlgList" typeCd="<%=MTypeConstants.CustomerTestFlg.TYPE_CD %>"/>
								<c:forEach items="${customerTestFlgList}" var="t">
									<li><label><html:radio property="testFlg" value="${f:h(t.value)}" styleId="testFlg_${t.value}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
								</c:forEach>
							</ul>
						</td>
					</tr>
					<tr>
						<th>掲載可否</th>
						<th><span class="attention">必須</span></th>
						<td class="release">
							<ul class="checklist_3col clear">
								<c:forEach items="${publicationFlgList}" var="t">
									<c:choose>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_SALES}">
										<li><label><html:radio property="publicationFlg" value="${t.value}" styleId="publicationFlg_${t.value}" styleClass="validate[required]" />${f:h(t.label)}</label></li>
									</c:when>
									<c:when test="${userDto.authLevel eq AUTH_LEVEL_STAFF}">
										<li><label><html:radio property="publicationFlg" value="${t.value}" styleId="publicationFlg_${t.value}" styleClass="disabled"  disabled="true" />${f:h(t.label)}</label></li>
									</c:when>
									</c:choose>
								</c:forEach>
							</ul>
						</td>
					</tr>
				</table>
			</c:if>
			<hr />

			<div class="wrap_btn">
				<html:submit property="conf"  value="確 認"  onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT}">
						<c:choose>
							<c:when test="${not empty webId}">
								<input type="button" name="back" value="戻 る"  onclick="location.href='${f:url(gf:makePathConcat2Arg(customerDetailPath, id, webId))}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
							</c:when>
							<c:otherwise>
								<html:submit property="back" value="戻 る" onclick="$('#AddAssigned').validationEngine('detach');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"   />
							</c:otherwise>
						</c:choose>
					</c:when>
				</c:choose>
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
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->

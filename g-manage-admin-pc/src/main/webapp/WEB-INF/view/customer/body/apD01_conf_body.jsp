<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page import="org.seasar.framework.container.factory.SingletonS2ContainerFactory" %>
<%@page import="org.seasar.framework.container.S2Container" %>
<%@page import="org.seasar.framework.util.ClassUtil" %>
<%@page import="org.seasar.framework.util.FieldUtil" %>

<style>
/*ダイアログ全体*/
#popup_container.custom1 {

	border-radius: 12px 12px;

	border-top:solid 5px #FF0000;
	border-right:solid 5px #FF0000;
	border-bottom:solid 5px #FF0000;
	border-left:solid 5px #FF0000;

	font-family: Georgia, serif;
	color: #000000;
	background: #FFFFFF;
	min-width: 370px;
	max-width: 400px;
}


/*タイトル部分*/
#popup_container.custom1 #popup_title {

	border-radius: 1px 1px 0 ;

	margin:0;
	padding:24px 0;
	font-size:24px;
	border:none;
	background:#FF0000;
	color:#fff;
	line-height:10px;

	border-left-style: medium;

	width: 100%;
}



/*タイトルより下の部分*/
#popup_container.custom1 #popup_content {
}
/*メッセージ部分*/
#popup_container.custom1 #popup_message {
	margin:0;
	padding:0 12px 24px 12px;

	font-size: 24px;
}
/*ボタン部分*/
#popup_container.custom1 INPUT[type='button'] {
	margin-top: 24px;
}

#popup_container.custom1 #popup_panel {
	padding-bottom: 20px;
}


</style>

<link rel="stylesheet" href="${ADMIN_CONTENS}/css/jquery/jquery-ui.min.css" type="text/css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/jquery-ui.min.js"></script>

<script type="text/javascript">

$( function() {

	$( "#deleteConfMsg" ).dialog({
		resizable: false,
		autoOpen: false,
		height: "auto",
		width: 400,
		modal: true,
		closeText: "キャンセル",
		buttons: {
			"削除": function() {
      			location.href='${f:url(deletePath)}';
			 },
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		}
	});
});


var deleteConfirm = function() {
	$( "#deleteConfMsg" ).dialog( "open" );
}

</script>


<c:set var="DATE_FORMAT" value="<%=GourmetCareeConstants.DATE_FORMAT_SLASH %>" scope="page" />
<c:set var="SCOUT_MAIL_LOG_KBN_ADD_MANUAL" value="<%=MTypeConstants.ScoutMailLogKbn.ADD_MANUAL %>" scope="page" />
<gt:areaList name="areaList" />
<gt:companyList name="companyList" />
<gt:salesList name="salesList" />
<gt:prefecturesList name="prefectures"/>
<gt:typeList name="submailReceptionFlgList" typeCd="<%=MTypeConstants.SubmailReceptionFlg.TYPE_CD %>" />
<gt:typeList name="loginFlgList" typeCd="<%=MTypeConstants.LoginFlg.TYPE_CD %>" />
<gt:typeList name="publicationFlgList" typeCd="<%=MTypeConstants.PublicationFlg.TYPE_CD %>" />
<gt:typeList name="scoutUseFlgList" typeCd="<%=MTypeConstants.ScoutUseFlg.TYPE_CD %>" />
<gt:typeList name="publicationEndDisplayFlgList" typeCd="<%=MTypeConstants.PublicationEndDisplayFlg.TYPE_CD %>" />
<gt:typeList name="shutokenForeignAreaKbnList" typeCd="<%=MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD %>" />

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:choose>
			<c:when test="${pageKbn eq PAGE_EDIT}">
				<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
			</c:when>
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<c:choose>
					<c:when test="${not empty webId}">
						<li><a href="${f:url(navigationListIndexPath)}">${f:h(navigationText2)}</a></li>
						<li><a href="${f:url(gf:makePathConcat1Arg(navigationWebdataPath, webId))}" >${f:h(navigationWebdataText)}</a></li>
						<li><a href="${f:url(gf:makePathConcat2Arg(navigationPathShopListWebDatailPath, id, webId))}" class="reverseColor" style="color: #FFF;">${f:h(navigationText3)}</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
						<li class="noDisplay">&nbsp;</li>
						<li><a href="${f:url(gf:makePathConcat1Arg(navigationPath3, id))}" class="reverseColor" style="color: #FFF;">${f:h(navigationText3)}</a></li>
					</c:otherwise>
				</c:choose>
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
		<s:form action="${f:h(actionPath)}">
		<c:if test="${existDataFlg eq true}">

			<!-- ################# 顧客情報 ################# -->
			<h3 class="subtitle">顧客情報</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_EDIT or pageKbn eq PAGE_DETAIL}">
						<tr>
							<th width="160">顧客ID</th>
							<td>${f:h(id)}&nbsp;</td>
						</tr>
						<tr>
							<th width="160">登録日</th>
							<td>${f:h(registrationDatetime)}&nbsp;</td>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<th width="160">顧客名</th>
					<td>${f:h(customerName)}&nbsp;</td>
				</tr>
				<tr>
					<th>顧客名（カナ）</th>
					<td>${f:h(customerNameKana)}&nbsp;</td>
				</tr>
				<tr>
					<th>エリア</th>
					<td>${f:h(f:label(areaCd, areaList, 'value', 'label'))}&nbsp;</td>
				</tr>
				<tr>
					<th width="160">会社名＜公開側表示用＞</th>
					<td>${f:h(displayCompanyName)}&nbsp;</td>
				</tr>
				<tr>
					<th>住所</th>
					<td>
					〒${f:h(zipCd)}<br>
					${f:label(prefecturesCd, prefectures, 'value', 'label')}${f:h(municipality)}${f:h(address)}</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td>${f:h(phoneNo1)}-${f:h(phoneNo2)}-${f:h(phoneNo3)}&nbsp;</td>
				</tr>
				<tr>
					<th>FAX番号</th>
					<td>
					<c:if test="${faxNo1 ne ''}">${f:h(faxNo1)}-${f:h(faxNo2)}-${f:h(faxNo3)}</c:if>&nbsp;
					</td>
				</tr>
				<tr>
					<th>会社情報</th>
					<td>
						<table class="nbr">
							<tbody>
							<c:if test="${not empty establishment}">
								<tr>
									<th>設立：</th>
									<td>
										${f:h(establishment)}
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty capital}">
								<tr>
									<th>資本金：</th>
									<td>
										${f:h(capital)}
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty representative}">
								<tr>
									<th>代表者：</th>
									<td>
										${f:h(representative)}
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty employee}">
								<tr>
								<th>従業員：</th>
									<td>
										${f:h(employee)}
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty businessContent}">
								<tr>
									<th>会社情報：</th>
									<td>
										${f:br(f:h(businessContent))}
									</td>
								</tr>
							</c:if>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<th>ホームページ</th><!-- デフォルトでnofollow -->
					<td>
						<c:choose>
							<c:when test="${not empty homepageDtoList}">
								<table class="nbr">
									<tbody>
									<c:forEach items="${homepageDtoList}" var="dto" varStatus="status">
										<c:if test="${not empty dto.url}">
										<tr>
											<th>${status.index + 1}&nbsp;&nbsp;&nbsp;URL：</th>
											<td>${f:h(dto.url)}</td>
										</tr>
										<tr>
											<th>説明：</th>
											<td>${f:h(dto.comment)}</td>
										</tr>
										</c:if>
									</c:forEach>
									</tbody>
								</table>
							</c:when>
							<c:otherwise>
								&nbsp;
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<c:if test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
					<tr>
						<th>スカウトメール使用可否</th>
						<td>${f:label(scoutUseFlg, scoutUseFlgList, 'value', 'label')}&nbsp;</td>
					</tr>

					<c:choose>
						<c:when test="${pageKbn eq PAGE_INPUT}">
							<tr>
								<th class="release">スカウトメール</th>
								<td class="release">
									<c:choose>
										<c:when test="${scoutAddCount != null && scoutAddCount != 0}">
											利用開始日 ${f:h(scoutUseStartDatetime)}&nbsp;追加 ${f:h(scoutAddCount)}通
										</c:when>
										<c:when test="${scoutUseEndDatetime != null}">
											利用終了日 ${f:h(scoutUseEndDatetime)}&nbsp;追加 無制限
										</c:when>
									</c:choose>
								</td>
							</tr>
						</c:when>

						<c:when test="${pageKbn eq PAGE_EDIT}">
						<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN or userDto.authLevel eq AUTH_LEVEL_STAFF or userDto.authLevel eq AUTH_LEVEL_SALES}">
							<tr>
								<th>スカウトメール</th>
								<td>
									<c:choose>
										<c:when test="${scoutAddCount != null && scoutAddCount != 0}">
											利用開始日 ${f:h(scoutUseStartDatetime)}&nbsp;追加 ${f:h(scoutAddCount)}通
										</c:when>
										<c:when test="${(scoutAddCount == null || scoutAddCount == 0) && scoutRemoveCount != 0}">
											削除 ${f:h(scoutRemoveCount)}通
										</c:when>
										<c:when test="${scoutUseEndDatetime != null}">
											利用終了日 ${f:h(scoutUseEndDatetime)}&nbsp;無制限
										</c:when>
									</c:choose>
								</td>
							</tr>
						</c:if>
							<tr>
								<th>スカウトメール追加履歴</th>
								<td>
									<gt:typeList name="scoutMailKbnList" typeCd="<%=MTypeConstants.ScoutMailKbn.TYPE_CD %>"/>
									<c:forEach items="${scoutMailAddHistoryList}" var="dto">
										${f:h(dto.addDate)}&nbsp;${f:h(dto.addCount)}通&nbsp;
										(${f:label(dto.scoutMailKbn, scoutMailKbnList, 'value', 'label')}<!--
										<c:if test="${dto.scoutMailLogKbn eq SCOUT_MAIL_LOG_KBN_ADD_MANUAL}">
											 -->・手動<!--
										</c:if>
										 -->)<br />
									</c:forEach>
								</td>
							</tr>
						</c:when>

						<c:when test="${pageKbn eq PAGE_DETAIL}">
							<tr>
								<th>スカウトメール</th>
								<td>
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
								<td>
									<gt:typeList name="scoutMailKbnList" typeCd="<%=MTypeConstants.ScoutMailKbn.TYPE_CD %>"/>
									<c:forEach items="${scoutMailAddHistoryList}" var="dto">
										${f:h(dto.addDate)}&nbsp;${f:h(dto.addCount)}通&nbsp;
										(${f:label(dto.scoutMailKbn, scoutMailKbnList, 'value', 'label')}<!--
										<c:if test="${dto.scoutMailLogKbn eq SCOUT_MAIL_LOG_KBN_ADD_MANUAL}">
											 -->・手動<!--
										</c:if>
										 -->)<br />
									</c:forEach>
								</td>
							</tr>
							<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
								<tr>
									<th>未読応募メール数</th>
									<td>${f:h(unopenedAppricationCnt)}&nbsp;通</td>
								</tr>
								<tr>
									<th>未読スカウトメール数</th>
									<td>${f:h(unopenedScoutCnt)}&nbsp;通</td>
								</tr>
							</c:if>
							<tr>
								<th>最終ログイン日時</th>
								<td class="release">${loginDatetime eq null ? '未ログイン' : f:h(loginDatetime)}</td>
							</tr>
						</c:when>

					</c:choose>
				</c:if>
				<tr>
					<th class="">掲載終了後のページ表示<br /></th>
					<td class="release">
							${f:label(publicationEndDisplayFlg, publicationEndDisplayFlgList, 'value', 'label')}&nbsp;
					</td>
				</tr>
				<c:if test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
					<tr>
						<th>備考</th>
						<td>${f:br(f:h(note))}&nbsp;</td>
					</tr>
				</c:if>
			</table>

			<!-- ################# 担当者情報 ################# -->
			<h3 class="subtitle">担当者情報</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="160">担当者名</th>
					<td>${f:h(contactName)}&nbsp;</td>
				</tr>
				<tr>
					<th>担当者名（カナ）</th>
					<td>${f:h(contactNameKana)}&nbsp;</td>
				</tr>
				<tr>
					<th>担当者部署</th>
					<td>${f:h(contactPost)}&nbsp;</td>
				</tr>

				<tr>
					<th>メインアドレス</th>
					<td>${f:h(mainMail)}&nbsp;</td>
				</tr>
				<tr>
					<th>サブアドレス</th>
					<td>
						<c:choose>
							<c:when test="${not empty subMailDtoList}">
								<c:forEach items="${subMailDtoList}" var="dto" varStatus="status">
									<c:if test="${not empty dto.subMail}">
										${f:h(dto.subMail)}&nbsp;&nbsp;
										[&nbsp;${f:label(dto.submailReceptionFlg, submailReceptionFlgList, 'value', 'label')}&nbsp;]
										<c:if test="${!status.last}"><br></c:if>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								&nbsp;
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>ログインID</th>
					<td>${f:h(loginId)}&nbsp;</td>
				</tr>
				<c:choose>
					<c:when test="${pageKbn ne PAGE_DETAIL}">
					<tr>
						<th>パスワード</th>
						<td>${f:h(dispPassword)}&nbsp;</td>
					</tr>
					</c:when>
				</c:choose>
				<tr>
					<th>ログイン可否</th>
					<td>${f:label(loginFlg, loginFlgList, 'value', 'label')}&nbsp;</td>
				</tr>
				<c:if test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
					<tr>
						<th>担当会社&nbsp;/&nbsp;営業担当者</th>
						<td>${f:label(assignedCompanyId, companyList, 'value', 'label')}：&nbsp;${f:label(assignedSalesId, salesList, 'value', 'label')}</td>
					</tr>

					<c:forEach items="${companySalesList}" var="dto">
					<tr>
						<th>担当会社&nbsp;/&nbsp;営業担当者</th>
						<td>${f:label(dto.companyId, companyList, 'value', 'label')}：&nbsp;${f:label(dto.salesId, salesList, 'value', 'label')}</td>
					</tr>
					</c:forEach>
				</c:if>
			</table>

			<!-- ################# メールマガジン設定 ################# -->
			<h3 class="subtitle">メールマガジン設定</h3>
			<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
				<tr>
					<th width="160">受信可否</th>
					<td class="release">
						<gt:typeList name="mailMagReceptionFlgList" typeCd="<%=MTypeConstants.CustomerMailMagazineReceptionFlg.TYPE_CD %>"/>
						${f:label(mailMagazineReceptionFlg, mailMagReceptionFlgList, 'value', 'label')}
					</td>
				</tr>
				<tr>
					<th class="">メールマガジンエリア</th>
					<td class="release">
						<ul class="shoplist clear">
						<c:forEach items="${mailMagazineAreaCdList}" var="t" varStatus="status">
							<li>${f:h(f:label(t, areaList, 'value', 'label'))}</li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				<c:if test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
					<c:if test="${pageKbn ne PAGE_INPUT}">
						<tr>
							<th>店舗エリア</th>
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
							<td>
								<c:if test="${shopListCount > 0}">
									${f:h(shopListCount)}店舗
								</c:if>
								<c:if test="${shopListCount == 0}">
									未登録
								</c:if>
							</td>
						</tr>
					</c:if>
				</c:if>
			</table>

			<c:if test="${userDto.authLevel ne AUTH_LEVEL_AGENCY}">
				<br><br>

				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
					<tr>
						<th width="160">顧客分類</th>
						<td class="release">
							<gt:typeList name="testFlgList" typeCd="<%=MTypeConstants.CustomerTestFlg.TYPE_CD %>"/>
								${f:label(testFlg, testFlgList, 'value', 'label')}&nbsp;
						</td>
					</tr>
					<tr>
						<th>掲載可否</th>
						<td>${f:label(publicationFlg, publicationFlgList, 'value', 'label')}&nbsp;</td>
					</tr>
				</table>
			</c:if>
			<hr />

			<div class="wrap_btn">
				<c:choose>
					<c:when test="${pageKbn eq PAGE_INPUT or pageKbn eq PAGE_EDIT}">
						<html:submit property="submit" value="登 録" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:submit property="correct" value="訂 正" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					</c:when>
					<c:when test="${pageKbn eq PAGE_DETAIL}">
						<c:if test="${editFlg}">
						<input type="button" name="submit" value="編 集" onclick="location.href='${f:url(editPath)}'" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<c:if test="${userDto.authLevel eq AUTH_LEVEL_ADMIN}">
						<input type="button" name="delete" value="削 除" onclick="deleteConfirm();" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
						<%-- WEBデータIDが入っていた場合は非表示 --%>
						<c:if test="${empty webId}">
							<html:submit property="back" value="戻 る" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						</c:if>
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
			<c:when test="${pageKbn eq PAGE_DETAIL}">
				<c:choose>
					<c:when test="${not empty webId}">
						<li><a href="${f:url(navigationListIndexPath)}">${f:h(navigationText2)}</a></li>
						<li><a href="${f:url(gf:makePathConcat1Arg(navigationWebdataPath, webId))}" >${f:h(navigationWebdataText)}</a></li>
						<li><a href="${f:url(gf:makePathConcat2Arg(navigationPathShopListWebDatailPath, id, webId))}" class="reverseColor" style="color: #FFF;">${f:h(navigationText3)}</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
						<li class="noDisplay">&nbsp;</li>
						<li><a href="${f:url(gf:makePathConcat1Arg(navigationPath3, id))}" class="reverseColor" style="color: #FFF;">${f:h(navigationText3)}</a></li>
					</c:otherwise>
				</c:choose>
			</c:when>
		</c:choose>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>

<style>
.ui-dialog-title {
	text-align: left;
}
</style>
<div id="deleteConfMsg" style="display: none; text-align: left" title="本当に削除してもよろしいですか？">
	<p style="font-size: 16px; line-height: 2em;"><b>削除すると、この顧客にひもづく原稿データが<br>全て消えてしまいます。</b></p>
</div>
<!-- #main# -->

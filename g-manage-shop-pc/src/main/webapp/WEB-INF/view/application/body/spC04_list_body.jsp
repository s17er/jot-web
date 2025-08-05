<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

	<c:set scope="page" var="RECEIVE" value="<%=MTypeConstants.SendKbn.RECEIVE %>" />
	<c:set scope="page" var="SEND" value="<%=MTypeConstants.SendKbn.SEND %>" />

	<c:set scope="page" var="UNOPENED" value="<%=MTypeConstants.MailStatus.UNOPENED%>" />
	<c:set scope="page" var="OPENED" value="<%=MTypeConstants.MailStatus.OPENED%>" />
	<c:set scope="page" var="REPLIED" value="<%=MTypeConstants.MailStatus.REPLIED%>" />

	<gt:typeList name="foogExpKbnList" typeCd="<%=MTypeConstants.FoodExpKbn.TYPE_CD %>" blankLineLabel="--"/>
	<gt:typeList name="mailStatusList" typeCd="<%=MTypeConstants.MailStatus.TYPE_CD %>"/>
	<gt:areaList name="areaList" />

<script type="text/javascript">
//<![CDATA[


	var onchangeSelectionKbn = function(selectId, id) {

		var kbn = $("#" + selectId);

		var selectionJson = {
				observateApplicationId : id,
				selectionKbn : kbn.val()
		};

		$.ajax({
			url: '${f:url("/observateApplicationMail/list/ajaxSelectionFlg/")}',
			data: selectionJson,
			type: 'post'
		});

	};

	var onchangeMemo = function(selectId, id) {

		var kbn = $("#" + selectId);

		var selectionJson = {
				observateApplicationId : id,
				memo : kbn.val()
		};

		$.ajax({
			url: '${f:url("/observateApplicationMail/list/editMemo/")}',
			data: selectionJson,
			type: 'post'
		});
	};

// ]]>
</script>
<!-- #main# -->
<div id="main">

<div id="wrap_mail">
	<h2>質問メール</h2>
	<p class="explanation">
		過去1年間のメール履歴一覧です。<br>
		質問者をクリックすると、質問メールの詳細がご覧いただけます。
	</p>
	<div class="menu_tab">
		<div class="menu_list">
			<ul>
				<li>
					<a href="${f:url('/applicationMail/list/')}">応募メール
						<c:if test="${unReadApplicationMailFlg}">
						<span id="mail_notification"></span>
						</c:if>
					</a>
				<li>
					<a href="${f:url('/preApplicationMail/list/')}">プレ応募メール
						<c:if test="${unReadPreApplicationMailFlg}">
						<span id="mail_notification"></span>
						</c:if>
					</a>
				</li>
				<li>
					<a href="${f:url('/scoutMail/list/')}">スカウトメール
						<c:if test="${unReadScoutMailFlg}">
						<span id="mail_notification"></span>
						</c:if>
					</a>
				</li>
				<li class="tab_active">
					<a href="${f:url('/observateApplicationMail/list/')}">質問メール
						<c:if test="${unReadObservateApplicationMailFlg}">
						<span id="mail_notification"></span>
						</c:if>
					</a>
				</li>
			</ul>
		</div>
	</div>

	<div class="send_receive_tab">
		<ul class="send_receive_ul">
			<li class="send_receive_list send <c:if test='${sendKbn == 2}'>on</c:if>"><a href="${f:url('/observateApplicationMail/list/')}" class="send_receive_anc">受信</a></li>
			<li class="send_receive_list receive <c:if test='${sendKbn == 1}'>on</c:if>"><a href="${f:url('/observateApplicationMail/list/sendBox')}" class="send_receive_anc">送信</a></li>
		</ul>
	</div>

	<c:if test="${sendKbn == 2}">
		<div id="wrap_mail_content">
			<div class="tab_area">
				<!-- 応募メール -->
				<div class="tab_contents tab_active" id="questionMail">
					<s:form action="${f:h(actionPath)}">
					<div class="narrowing_area">
						<p class="mailcount">全 ${pageNavi.allCount } 件中 / ${pageNavi.offset + 1 } 件 〜 ${pageNavi.limit >= pageNavi.allCount ? pageNavi.allCount : pageNavi.limit } 件</p>
						<div class="s_conditions cond_open">表示条件の設定</div>
						<div class="narrowing_check">
								<div class="narr_number">
									<span>表示件数</span>
									<ul>
										<li><html:radio property="where_displayCount" value="20" styleId="twenty"/><label for="twenty">20件</label></li>
										<li><html:radio property="where_displayCount" value="50" styleId="fifty"/><label for="fifty">50件</label></li>
										<li><html:radio property="where_displayCount" value="100" styleId="onehundred"/><label for="onehundred">100件</label></li>
									</ul>
								</div>
								<div class="narr_mail">
									<span>メール状態</span>
									<ul>
						                <c:forEach items="${mailStatusList}" var="t">
						                    <li><html:multibox property="where_mailStatus" value="${f:h(t.value)}" styleId="where_mailStatus${t.value}" /><label for="where_mailStatus${t.value}">&nbsp;${f:h(t.label)}</label></li>
						                </c:forEach>
									</ul>
								</div>
								<div class="narr_area">
									<span>掲載エリア</span>
									<ul>
						                <c:forEach items="${areaList}" var="t">
						                    <li><html:multibox property="where_areaCd" value="${f:h(t.value)}" styleId="where_areaCd${t.value}" /><label for="where_areaCd${t.value}">&nbsp;${f:h(t.label)}</label></li>
						                </c:forEach>
									</ul>
								</div>
								<div class="narr_word">
									<span>フリーワード</span><br>
									<html:text property="where_keyword" size="30" placeholder="フリーワードを入力" /><br />
								</div>
								<html:submit property="search" value="検索" class="btn_search"/>
						</div>
					</div>

					<c:if test="${errorMessage != null}">
						<div class="error"><ul><li>${errorMessage}</li></ul></div>
					</c:if>

					<c:if test="${existDataFlg}">
					<div class="allselect_page">
						<div class="all_select">
							<input type="checkbox" id="allcheck" onclick="handleAllBoth(this);" class="memberIdAllCheckBox">
							<label for="allcheck">すべてを選択</label>
						</div>
						<div class="page">
							<p><!--
				            <gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}" prevLabel="前へ" nextLabel="次へ">
				                <c:choose>
				                    <c:when test="${dto.linkFlg eq true}">
				                        --><span><a href="${f:url(gf:concat2Str(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
				                    </c:when>
				                    <c:otherwise>
				                        --><span>${dto.label}</span><!--
				                    </c:otherwise>
				                </c:choose>
				            </gt:PageNavi>-->
							</p>
						</div>
					</div>
					<div class="application_list">
						<table class="appmail_table">
							<tbody>
								<c:forEach var="dto" items="${dataList}">
									<tr>
										<td class="table-checkbox"><input type="checkbox" name="changeIdArray" value="${dto.id}" id="${dto.id}" class="lumpSendCheck"><label for="${dto.id}"></label></td>
										<td class="table-name"><p><a href="${f:url(gf:concat2Str('/observateApplicationMail/detail/index/', dto.observateApplicationId)) }?fromPage=receive"><span class="name">${dto.fromName }</span><span class="age">（${dto.age}）</span></a></p></td>
										<td class="table-status">
											<ul class="table-status">
												<li id="status">
													<div class="selectbox">
														<gt:typeList name="selectionList" typeCd="<%=MTypeConstants.SelectionFlg.TYPE_CD %>" />
														<select name="selectionKbn" id="selectionKbn${dto.observateApplicationId}" onchange="onchangeSelectionKbn('selectionKbn${dto.observateApplicationId}', '${dto.observateApplicationId}');">
															<c:forEach items="${selectionList}" var="i">
																<c:set var="selected" value="${(i.value eq dto.selectionFlg) ? 'selected' : ''}" />
																<option value="${f:h(i.value)}" ${f:h(selected)}>${f:h(i.label)}</option>
															</c:forEach>
														</select>
													</div>
												</li>
												<li id="memo">
													<div class="mest_memo">
													<html:text property="memo" value="${f:h(dto.memo)}" placeholder="メモを入力" id="memo${dto.observateApplicationId}" class="form-bg"/>
													<input type="button" name="editMemo" value="登録" class="btn_entry" onClick="onchangeMemo('memo${dto.observateApplicationId}', '${dto.observateApplicationId}');">
													</div>
												</li>
											</ul>
										</td>
										<td class="table-date"><p>${dto.sendDatetime}</p></td>
									</tr>
									<tr>
										<td class="table-info">
											<span class="table-mailtitle">${dto.subject}</span>
											<div class="repo">
												<ul class="">
													<c:choose>
														<c:when test="${dto.unsubscribeFlg}">
															<li class="member_cancel_tag">退会ユーザー</li>
														</c:when>
														<c:otherwise>
															<li>${dto.areaName}</li>
															<li>求人原稿名： ${dto.applicationName}</li>
														</c:otherwise>
													</c:choose>
												</ul>
											</div>
										</td>
										<c:choose>
											<c:when test="${dto.mailStatus == 1}">
												<td class="table-icon"><span id="mail_notification"><a href="../detail/index/00001/" title="新着メールあり"></a></span></td>
											</c:when>
											<c:when test="${dto.mailStatus == 3}">
												<td class="table-icon"><span id="mail_replied"><a href="#応募者メールリンク" title="返信済"><!-- 返信アイコン表示 --><!-- 返信アイコン表示 --></a></span></td>
											</c:when>
										</c:choose>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="btn_page">
							<div class="btn">
								<div class="wrap_btn">
									<html:submit property="doLumpChangeRead" value="既読にする" styleId="btn_alreadyread"/>
									<html:submit property="doLumpChangeUnRead" value="未読にする" styleId="btn_unread"/>
									<html:submit property="doLumpChangeUnDisplay" value="削除" styleId="btn_undispayied" onclick="return confirm('選択したやり取りは管理画面上から削除され、今後確認できなくなります。\r\n本当に削除しますか?')? true : false;"/>
								</div>
							</div>
							<div class="page">
								<p><!--
					            <gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}" prevLabel="前へ" nextLabel="次へ">
					                <c:choose>
					                    <c:when test="${dto.linkFlg eq true}">
					                        --><span><a href="${f:url(gf:concat2Str(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
					                    </c:when>
					                    <c:otherwise>
					                        --><span>${dto.label}</span><!--
					                    </c:otherwise>
					                </c:choose>
					            </gt:PageNavi>-->
								</p>
							</div>
						</div>
					</div>
					</c:if>
				</s:form>
				</div>
			</div>
		</div>
	</c:if>
	<c:if test="${sendKbn == 1 && existDataFlg}">
		<div id="wrap_result">
			<!-- #page# -->
			<div class="page">
				<p><!--
	            <gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}" prevLabel="前へ" nextLabel="次へ">
	                <c:choose>
	                    <c:when test="${dto.linkFlg eq true}">
	                        --><span><a href="${f:url(gf:concat2Str(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
	                    </c:when>
	                    <c:otherwise>
	                        --><span>${dto.label}</span><!--
	                    </c:otherwise>
	                </c:choose>
	            </gt:PageNavi>-->
				</p>
			</div>
			<!-- #page# -->
			<table cellpadding="0" cellspacing="0" border="0" class="all_table"><tbody>
				<tr>
					<th width="50px">状態</th>
					<th width="150px">開封日時</th>
					<th width="300px">件名</th>
					<th width="">宛先</th>
					<th width="">送信日時</th>
				</tr>
				<c:forEach var="dto" items="${dataList}" varStatus="status">
					<tr>
						<td>
							<c:choose>
								<c:when test="${dto.receiveReadFlg}">
									<img src="${SHOP_CONTENS}/images/svg-read.svg" alt="既読" width="40px" height="40px"/>
								</c:when>
								<c:otherwise>
									<img src="${SHOP_CONTENS}/images/svg-unread.svg" alt="未読" width="40px" height="40px"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:if test="${dto.receiveReadFlg}">
								<fmt:formatDate value="${dto.receiveReadingDatetime}" pattern="yyyy/MM/dd HH:mm" />
							</c:if>
						</td>
						<td><a href="${f:url(gf:concat2Str('/observateApplicationMail/detail/index/', dto.observateApplicationId)) }?fromPage=send">${f:h(dto.subject)}</a>&nbsp;</td>
						<td>${f:h(dto.toName)}</td>
						<td>${f:h(dto.sendDatetime)}</td>
					</tr>
				</c:forEach>
			</tbody></table>
			<!-- #page# -->
			<div class="page">
				<p><!--
	            <gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}" prevLabel="前へ" nextLabel="次へ">
	                <c:choose>
	                    <c:when test="${dto.linkFlg eq true}">
	                        --><span><a href="${f:url(gf:concat2Str(pageNaviPath, dto.pageNum))}">${dto.label}</a></span><!--
	                    </c:when>
	                    <c:otherwise>
	                        --><span>${dto.label}</span><!--
	                    </c:otherwise>
	                </c:choose>
	            </gt:PageNavi>-->
				</p>
			</div>
			<!-- #page# -->
			</div>
	</c:if>
</div>

</div>
<!-- #main# -->

<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page pageEncoding="UTF-8"%>

<gt:prefecturesList name="prefecturesList" />
<gt:areaList name="areaList" />
<gt:typeList name="salaryKbnList" typeCd="<%=MTypeConstants.SalaryKbn.TYPE_CD %>"  />
<gt:maxRowList name="maxRowList" value="20,50,100" suffix="件" />

<gt:scoutMailPaidCount name="scoutMailPaidCountDto" customerId="${userDto.customerId}" />

<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/member.css" />

<div id="main">

    <div id="wrap_memberkeep">
        <h2>キープBOX</h2>
        <p class="explanation">
            キープした求職者を一覧で表示されます。<br>「気になる」「スカウトメール」を送り、求職者へ御社の求人情報を知ってもらえます。<br>
            ※スカウトメールは求人が掲載されている期間のみご利用できます。また有効期限がございますのでご注意ください。
        </p>
        <div class="menu_tab">
            <div class="menu_list"><ul>
                <li>
                    <a href="${f:url(memberListPath)}">会員検索</a>
                </li>
                <li class="tab_active">
                    <a href="${f:url(keepBoxPath)}">キープBOX</a>
                </li>
                <li>
                    <a href="${f:url('/scoutMail/list/')}">スカウトメール
						<c:if test="${unReadScoutMailFlg}">
						<span id="mail_notification"></span>
						</c:if>
                    </a>
                </li>
            </ul></div>
        </div>

        <div id="wrap_scse_content">
            <div class="tab_area">
                <div class="tab_contents tab_active" id="member_keepBox">
                    <div class="member_list_area">
                        <div class="scoutmail_count">
						<p class="count_txt">
							スカウトメール残数<br>
							<c:choose>
								<c:when test="${scoutMailRemainDto.isUseUnlimitScoutMailFlg }">
									無制限（有効期限：<fmt:formatDate value="${scoutMailRemainDto.useEndDatetime}" pattern="yyyy/MM/dd" />）
								</c:when>
								<c:otherwise>
									${f:h(scoutMailRemainDto.remainFreeScoutCount)}通<span>（お試し）</span>
									<c:if test="${scoutMailRemainDto.paidMailList != null }">
										<c:forEach items="${scoutMailRemainDto.paidMailList}" var="t" varStatus="status">
											+&nbsp;
											${f:h(t.scoutRemainCount)}通<span>（有効期限：<fmt:formatDate value="${t.useEndDatetime}" pattern="yyyy/MM/dd" />）</span>
										</c:forEach>
									</c:if>
								</c:otherwise>
							</c:choose>
						</p>
                            <p class="scoutprice_txt">※スカウトメールの<a href="${f:url(price)}">追加料金はこちら</a></p>
                        </div>
                        <div id="wrap_result">
                                <s:form action="${f:h(actionPath)}/changeMaxRow" styleId="changeMaxRowForm" >
                                    <table cellpadding="0" cellspacing="0" border="0" class="number_table">
                                        <tbody>
                                            <tr>
                                                <td><span class="count_big">${f:h(pageNavi.allCount)}</span>名がキープされています。</td>
                                                <td class="pull_down">
                                                    <div class="selectbox">
                                                        <html:select property="maxRow" styleId="maxRowSelect">
                                                            <html:optionsCollection name="maxRowList"/>
                                                        </html:select>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </s:form>
                                <c:if test="${existDataFlg}">
                               	<html:errors/>
                                <html:messages id="msg" message="true">
									<div class="error">
										<ul>
											<li>
								  				<bean:write name="msg" ignore="true"   />
								  			</li>
										</ul>
									</div>
								</html:messages>
    <!-- 							<div class="wrap_btn_left">
                                    <input type="button" name="" value="店舗一覧情報確認" onclick="showPreview();">
                                </div> -->
                                <!-- #page# -->
                                <div class="page">
                                            <p>
                                                <!--
                                            <gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
                                                <c:if test="${dto.linkFlg eq true}">
                                                    <%// vt:PageNaviのpathはc:setで生成する。 %>
                                                    <c:set var="pageLinkPath" scope="page" value="/member/keepBox/changePage/${dto.pageNum}" />
                                                    --><span><a href="${f:url(pageLinkPath)}" class="">${dto.label}</a></span><!--
                                                </c:if>
                                                <c:if test="${dto.linkFlg ne true}">
                                                    --><span>${dto.label}</span><!--
                                                </c:if>
                                            </gt:PageNavi>
                                    -->
                                            </p>
                                </div>
                                <!-- #page# -->
                                    <s:form action="${f:h(actionPath)}">
                                        <table cellpadding="0" cellspacing="0" border="0" class="all_table"><tbody>
                                            <tr>
                                                <th class="select_all" width="10px">
                                                    <input type="checkbox" id="allcheck" class="checkBoxAll">
                                                    <label for="allcheck"></label>
                                                </th>
                                                <th width="40px">No</th>
                                                <th width="65px">ID</th>
                                                <th width="150px">状態</th>
                                                <th width="120px" class="posi_center">性別・年齢</th>
                                                <th width="140px">住所</th>
                                                <th width="120px">ログイン日</th>
                                                <th class="posi_center" width="80px">会員情報</th>
                                            </tr>

                                            <c:forEach var="dto" items="${memberDtoList}" varStatus="status">
                                                <c:set var="classStr" value="${gf:odd(status.index, 'alternate', '')}" scope="page" />
                                            <tr>
                                                <td class="table-checkbox">
                                                	<html:multibox property="checkId" value="${dto.id}" id="${dto.id}" class="lumpSendCheck" data-keepId="${dto.keepId}"/><label for="${dto.id}"></label>
                                                </td>
                                                <td><fmt:formatNumber value="${status.index + 1}" pattern="0" /></td>
                                                <td>${f:h(dto.id)}&nbsp;</td>
                                                <td>
                                                	<ul class="scout_userstats">
                                                        <c:if test="${dto.scoutFlg eq 1}">
                                                            <c:choose>
                                                                <c:when test="${dto.scoutReceiveKbn eq 2}">
                                                                    <li>
                                                                        <img src="${SHOP_CONTENS}/images/scout/icon_receive.gif" alt="スカウト受け取る" width="20" height="20" />&nbsp;
                                                                        <fmt:formatDate value="${dto.vMemberMailbox.sendDatetime}" pattern="yyyy/MM/dd" />
                                                                    </li>
                                                                </c:when>
                                                                <c:when test="${dto.scoutReceiveKbn eq 3}">
                                                                    <li>
                                                                        <img src="${SHOP_CONTENS}/images/scout/icon_turndown.gif" alt="スカウト断る" width="20" height="20" />&nbsp;
                                                                        <fmt:formatDate value="${dto.vMemberMailbox.sendDatetime}" pattern="yyyy/MM/dd" />
                                                                    </li>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <li>
                                                                        <img src="${SHOP_CONTENS}/images/scout/icon_scout.gif" alt="スカウト済" width="20" height="20" />&nbsp;
                                                                        <fmt:formatDate value="${dto.vMemberMailbox.sendDatetime}" pattern="yyyy/MM/dd" />
                                                                    </li>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${dto.footprintFlg eq 1}">
                                                            <li>
                                                                <img src="${SHOP_CONTENS}/images/scout/icon_interest.gif" alt="気になる" width="20" height="20" />&nbsp;
                                                                <fmt:formatDate value="${dto.tFootprintDate}" pattern="yyyy/MM/dd" />
                                                            </li>
                                                        </c:if>
                                                        <c:if test="${dto.applicationFlg eq 1}">
                                                            <li>
                                                                <img src="${SHOP_CONTENS}/images/scout/icon_entry.gif" alt="応募あり" width="20" height="20" />&nbsp;
                                                                <fmt:formatDate value="${dto.applicationMail.sendDatetime}" pattern="yyyy/MM/dd" />
                                                            </li>
                                                        </c:if>
                                                        &nbsp;
                                                    </ul>
                                                </td>
                                                <td class="posi_center">
                                                    <button name="" class="button-link modal-open" data-target="userWishData${dto.id}">
                                                        <c:if test="${dto.sexKbn eq '1'}"><div class="button-link-gender">男性</div></c:if>
	                                                    <c:if test="${dto.sexKbn eq '2'}"><div class="button-link-gender">女性</div></c:if>
														<c:if test="${dto.sexKbn eq '3'}"><div class="button-link-gender">回答なし</div></c:if>
                                                        <div class="button-link-age">(${f:h(dto.age)})</div>
                                                        <div class="button-link-detailbtn">条件表示</div>
                                                    </button>
                                                   	<div id="userWishData${dto.id}" class="modal">
                                                        <div class="modal-bg modal-close">
                                                        </div>
                                                        <div class="modal-content">
                                                            <dl>
                                                                <dt>希望職種</dt>
                                                                <dd>
                                                                <c:choose>
                                                                <c:when test="${empty dto.job}">
                                                                    データ未入力&nbsp;
                                                                </c:when>
                                                                <c:otherwise>
                                                                	<gt:convertToJobName items="${dto.job}"/>&nbsp;
                                                                </c:otherwise>
                                                            	</c:choose>
                                                                </dd>
                                                                <dt>希望業種</dt>
                                                                <dd>
                                                                <c:choose>
                                                                <c:when test="${empty dto.industry}">
                                                                    データ未入力&nbsp;
                                                                </c:when>
                                                                <c:otherwise>
                                                                	<gt:convertToIndustryName items="${dto.industry}"/>&nbsp;
                                                                </c:otherwise>
                                                            	</c:choose>
                                                                </dd>
                                                                <dt>希望年収</dt>
                                                                <dd>
                                                                <c:choose>
                                                                <c:when test="${empty dto.salaryKbn}">
                                                                    データ未入力&nbsp;
                                                                </c:when>
                                                                <c:otherwise>
                                                                	${f:label(dto.salaryKbn, salaryKbnList, 'value', 'label')}&nbsp;
                                                                </c:otherwise>
                                                            	</c:choose>
                                                                </dd>
                                                                <dt>希望勤務地</dt>
                               									<c:choose>
                                                                <c:when test="${empty dto.hopeCityMap}">
                                                                <dd>
                                                                    データ未入力&nbsp;
                                                                </dd>
                                                                </c:when>
                                                                <c:otherwise>
                                                                <dd class="pAddress">
																	<c:forEach var="city" items="${dto.hopeCityMap}" varStatus="prefStatus">
																	<div class="province">
																	${f:label(city.key, prefecturesList, 'value', 'label')}
																	</div>
																	<div class="cities">
																		<c:forEach items="${city.value}" var="cityName" varStatus="status">
																		${f:h(cityName)}
																		<c:if test="${!status.last}">,&nbsp;</c:if>
																		</c:forEach>
																	</div>
																</c:forEach>
                                                               	</dd>
                                                                </c:otherwise>
                                                            	</c:choose>
                                                            </dl>
                                                            <div class="btn">
                                                                <ul>
                                                                    <li><button type="button" class="leaveKeep" data-id="${dto.keepId}">キープを外す&nbsp;<span class="material-icons-outlined">person_remove</span></button></li>
                                                                    <li><button onclick="location.href='${f:url(gf:makePathConcat1Arg('/member/detail/indexFromKeepBox', dto.id))}'" type="button">詳細を見る&nbsp;<span class="material-icons-outlined">arrow_right</span></button></li>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label')}${f:h(dto.municipality)}&nbsp;</td>
                                                <td>${f:h(dto.lastLoginDatetime)}&nbsp;</td>
                                                <td class="posi_center shopLineheight">
                                                    <a href="${f:url(gf:makePathConcat1Arg('/member/detail/indexFromKeepBox', dto.id))}">詳細</a>
                                                    <html:multibox property="deleteId" value="${dto.keepId}" id="${dto.keepId}" class="lumpSendCheck" style="display:none"/><label for="${dto.keepId}" style="display:none"></label>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                        </table>
                                        <span></span>
                                        <div class="wrap_btn">
                                        	<html:submit property="lumpSend" value="一括送信"  styleId="btn_send"  />
                                            <html:submit property="lumpDelete" value="一括削除"  styleId="btn_del"  />
                                        </div>
                                        <!-- #page# -->
                                        <div class="page">
                                            <p>
                                                <!--
                                            <gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
                                                <c:if test="${dto.linkFlg eq true}">
                                                    <%// vt:PageNaviのpathはc:setで生成する。 %>
                                                    <c:set var="pageLinkPath" scope="page" value="/member/keepBox/changePage/${dto.pageNum}" />
                                                    --><span><a href="${f:url(pageLinkPath)}" class="">${dto.label}</a></span><!--
                                                </c:if>
                                                <c:if test="${dto.linkFlg ne true}">
                                                    --><span>${dto.label}</span><!--
                                                </c:if>
                                            </gt:PageNavi>
                                    -->
                                            </p>
                                        </div>
                                        <!-- #page# -->
                                    </s:form>
                                    </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

<script>
// 件数変更
$("#maxRowSelect").on("change", function() {
	$("#changeMaxRowForm").submit();
});

// モーダルウィンドウを開く
$('.modal-open').on('click', function(){
  var target = $(this).data('target');
  var modal = document.getElementById(target);

  $('#content').addClass('fixed');
  $(modal).fadeIn();
  return false;
});

// モーダルウィンドウを閉じる
$('.modal-close').on('click', function(){
  $('#content').removeClass('fixed');
  $('.modal').fadeOut();
  return false;
});

// 送信用チェックボックスと削除用チェックボックスを同期させる
$('input[name="checkId"]').each(function(index, element) {
	checkDeleteCheckbox($(element));
})
$('input[name="checkId"]').on('click', function(){
	checkDeleteCheckbox($(this));
});
$('input[name="checkId"]').on('change', function(){
	checkDeleteCheckbox($(this));
});
//送信用チェックボックスと削除用チェックボックスを同期させる関数
function checkDeleteCheckbox(element){
	let id = element.attr("data-keepId");
	if(element.prop("checked")){
		$("#" + id).prop("checked", true);
	}else{
		$("#" + id).prop("checked", false);
	}
}

// モーダルキープを外す
$('.leaveKeep').on('click', function(){
	let leaveKeepElement = $(this);
	$('input[name="deleteId"]').each(function(index, element) {
		if($(element).attr("id") == leaveKeepElement.attr("data-id")){
			$(element).prop("checked", true);
		}else{
			$(element).prop("checked", false);
		}
		// 一括削除を押す
		$('input[name="lumpDelete"]').trigger("click");
	})
});
</script>

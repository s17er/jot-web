<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.db.common.constants.MStatusConstants"%>

<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/table.color.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/preview.js"></script>
<script type="text/javascript">
$(function () {
	/** 表示件数 */
	const NARR_NUMBER = 'narr_number';
	/** 原稿状態 */
	const NARR_MAIL = 'narr_mail';
	/** 掲載エリア */
	const NARR_AREA = 'narr_area';
	/** 原稿サイズ */
	const NARR_SIZE = 'narr_size';
	/** 表示順 */
	const SHOW_ORDER = 'showOrder';
	/** 表示順の値 */
	const SHOW_ORDER_VALUES = ['1', '2', '3', '4', '5'];

	// URLパラメータを取得
	let param = location.search;
	// URLパラメータに基づいてフォームを選択状態にする
	checkForm(param);

	// メニュータブ
	$(".menu_tab li a").each(function () {
		if (this.href == getUrl()) {
			$(this).parents("li").addClass("tab_active");
		}
	});

	// 順番変更セレクトボックスのチェンジイベント
	$('#selectionKbn').on('change', function () {
		$("#search").trigger("click");
	});

	/** 検索条件を取得します */
	function getSearchParam() {
		let searchParam = {
			'narr_number': [],
			'narr_mail': [],
			'narr_area': [],
			'narr_size': [],
		};
		searchParam['narr_number'].push($('input[name="narr_number"]:checked').val());
		$('input[name="narr_mail"]:checked').each(function (index, value) {
			searchParam['narr_mail'].push($(value).val());
		});
		$('input[name="narr_area"]:checked').each(function (index, value) {
			searchParam['narr_area'].push($(value).val());
		});
		$('input[name="narr_size"]:checked').each(function (index, value) {
			searchParam['narr_size'].push($(value).val());
		});
		return searchParam;
	};

	/** 表示順を取得します */
	function getShowOrder(element) {
		return element.val();
	};

	/** 検索パラメータをつけてアクセスし直します */
	function search(searchParam, showOrder) {
		let url = getUrl();
		let param = searchParam;
		if (showOrder != null) {
			param['showOrder'] = showOrder;
		}

		window.location.href = url + '?' + $.param(param, true);
	}

	/** フォームを選択状態にする */
	function checkForm(search) {
		let param = url_query_param(search);

		// 表示件数を選択状態にする
		check(NARR_NUMBER, param);

		// 原稿状態を選択状態にする
		check(NARR_MAIL, param);

		// 掲載エリアを選択状態にする
		check(NARR_AREA, param);

		// 原稿サイズを選択状態にする
		check(NARR_SIZE, param);

		// 表示順を選択状態にする
		if (SHOW_ORDER in param) {
			// デフォルトは新着順にする
			let value = param[SHOW_ORDER][0];
			if($.inArray(value, SHOW_ORDER_VALUES) == -1){
				$('#selectionKbn').val(SHOW_ORDER_VALUES[0]);
			}else {
				$('#selectionKbn').val(value);
			}
		}
	}

	/** 指定のNameのフォームを選択状態にする */
	function check(name, param) {
		if (name in param) {
			$('input[name="' + name + '"]').each(function (index, value) {
				$.each(param[name], function (i, v) {
					if ($(value).val() == v) {
						$(value).prop('checked', true);
					}
				});
			});
		}
	}

	// URLパラメータを配列に変換
	function url_query_param(search) {
		search = search.replace('?', '');
		var ar_search = search.split('&');
		var param = new Array();
		ar_search.forEach(function (val) {
			var sep_val = val.split('=');
			if (Array.isArray(param[sep_val[0]])) {
				param[sep_val[0]].push(sep_val[1]);
			} else {
				param[sep_val[0]] = [sep_val[1]];
			}
		});
		return param;
	}

	/** URLを取得 */
	function getUrl() {
		let url = window.location.href.replace(location.search, '');
		url = url.replace(/changePage\/\d*/, '');
		return url;
	}
});
</script>

<!-- #main# -->
<div id="main">
				<div id="wrap_web-shoplist">
					<h2>${f:h(pageTitle)}</h2>
					<p class="explanation">
						${f:h(defaultMsg0)}<br>
						${f:h(defaultMsg1)}${f:h(defaultMsg2)}
					</p>
					<div class="menu_tab">
						<div class="menu_list">
							<ul>
								<li class="tab_active">
									<a href="${f:url('/webdata/list/')}">求人原稿</a>
								</li>
								<li>
									<a href="${f:url('/shopList/')}">店舗一覧</a>
								</li>
							</ul>
						</div>
					</div>

					<s:form action="${f:h(actionPath)}">
					<div id="wrap_masc_content">
						<div class="tab_area">
							<div class="tab_contents tab_active">

								<!-- 検索エリア -->

								<div class="narrowing_area">
									<p class="mailcount">全 ${pageNavi.allCount} 件中 / ${pageNavi.minDisp} 件 〜 ${pageNavi.maxDisp}  件</p>
									<div class="narrowing_check">
										<div class="narr_number">
											表示件数：
											<ul>
												<li><html:radio property="where_displayCount" value="20" styleId="twenty"/><label for="twenty">20件</label></li>
												<li><html:radio property="where_displayCount" value="50" styleId="fifty"/><label for="fifty">50件</label></li>
												<li><html:radio property="where_displayCount" value="100" styleId="onehundred"/><label for="onehundred">100件</label></li>
											</ul>
										</div>
										<div class="narr_manuscript">
											原稿状態：
											<ul>
												<li><html:multibox property="where_webStatus" value="4" styleId="unread"/><label for="unread">掲載中</label></li>
												<li><html:multibox property="where_webStatus" value="5" styleId="not_replied"/><label for="not_replied">過去掲載</label></li>
											</ul>
										</div>
										<div class="narr_area">
											掲載エリア：
											<ul>
												<li><html:multibox property="where_areaCd" value="1" styleId="kanto" /><label for="kanto">首都圏</label></li>
												<li><html:multibox property="where_areaCd" value="2" styleId="tohoku" /><label for="tohoku">東北</label></li>
												<li><html:multibox property="where_areaCd" value="3" styleId="kansai" /><label for="kansai">関西</label></li>
												<li><html:multibox property="where_areaCd" value="4" styleId="tokai" /><label for="tokai">東海</label></li>
												<li><html:multibox property="where_areaCd" value="5" styleId="kyushu-okinawa" /><label for="kyushu-okinawa">首都圏</label></li>
											</ul>
										</div>
										<div class="narr_size">
											原稿サイズ：
											<ul>
												<li><html:multibox property="where_sizeKbn" value="1" styleId="A_type" /><label for="A_type">Aタイプ</label></li>
												<li><html:multibox property="where_sizeKbn" value="2" styleId="B_type" /><label for="B_type">Bタイプ</label></li>
												<li><html:multibox property="where_sizeKbn" value="3" styleId="C_type" /><label for="C_type">Cタイプ</label></li>
												<li><html:multibox property="where_sizeKbn" value="4" styleId="D_type" /><label for="D_type">Dタイプ</label></li>
												<li><html:multibox property="where_sizeKbn" value="5" styleId="E_type" /><label for="E_type">Eタイプ</label></li>
												<li><html:multibox property="where_sizeKbn" value="6" styleId="WEB_type" /><label for="WEB_type">テキストWEBタイプ</label></li>
											</ul>
										</div>
										<div class="accordion_btn">
											<html:submit property="search" value="この条件で検索する" class="btn_search" styleId="search"/>
										</div>

									</div>
									<div class="s_conditions cond_open">表示条件の変更</div>
								</div>


								<!-- ▼▼▼掲載求人がある場合▼▼▼ -->
								<c:if test="${existDataFlg eq true}">

								<!-- 並べ替えのセレクトボックス -->
								<div class="selectbox">
									<html:select property="where_displayOrder" styleId="selectionKbn" class="sort_bar">
										<c:choose>
											<c:when test="${where_displayOrder == '1'}">
												<option value="1" selected>新着順</option>
											</c:when>
											<c:otherwise>
												<option value="1">新着順</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${where_displayOrder == '2'}">
												<option value="2" selected>PV数が多い順</option>
											</c:when>
											<c:otherwise>
												<option value="2">PV数が多い順</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${where_displayOrder == '3'}">
												<option value="3" selected>応募数が多い順</option>
											</c:when>
											<c:otherwise>
												<option value="3">応募数が多い順</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${where_displayOrder == '4'}">
												<option value="4" selected>プレ応募数が多い順</option>
											</c:when>
											<c:otherwise>
												<option value="4">プレ応募数が多い順</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${where_displayOrder == '5'}">
												<option value="5" selected>電話応募数が多い順</option>
											</c:when>
											<c:otherwise>
												<option value="5">電話応募数が多い順</option>
											</c:otherwise>
										</c:choose>
									</html:select>
								</div>

								<!-- ページャー（上） -->

								<table cellpadding="0" cellspacing="0" border="0" class="page">
									<tbody>
										<tr>
											<td>
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
											</td>
										</tr>
									</tbody>
								</table>


								<!-- 求人原稿一覧 -->
								<div class="webdata_list">
									<table cellpadding="0" cellspacing="0" border="0" class="all_table">
										<tbody>
											<tr>
												<th width="85" class="">原稿番号</th>
												<th>原稿名〔エリア〕</th>
												<th width="110">掲載期間</th>
												<th width="65" class="posi_center">サイズ</th>
												<th width="65" class="posi_center">PV数</th>
												<th width="70" class="posi_center">応募数</th>
												<th width="75" class="posi_center">プレ応募</th>
												<th width="75" class="posi_center">電話応募</th>
												<th width="60" class="posi_center">閲覧</th>
											</tr>

											<gt:typeList name="sizeList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />
											<c:set var="displayStatusPostEnd" value="<%=MStatusConstants.DisplayStatusCd.POST_END %>" />
											<% //ループ処理 %>
											<c:forEach var="m" varStatus="status" items="${list}">

												<% //テーブルの背景色を変更するため掲載終了の場合はCSSをセット %>
												<c:set var="classStr" value="${m.displayStatus eq displayStatusPostEnd ? 'end' : ''}" />
											<tr>
												<td class="${classStr}">${f:h(m.id)}</td>
												<td class="${classStr}">${f:h(m.manuscriptName)}〔${f:h(m.MArea.areaName)}〕</td>
												<td class="${classStr}"><fmt:formatDate value="${m.postStartDatetime}" pattern="yyyy/MM/dd" /> ～ <fmt:formatDate value="${m.postEndDatetime}" pattern="yyyy/MM/dd" /></td>
												<td class="posi_center ${classStr}">${f:label(m.sizeKbn, sizeList, 'value', 'label')}</td>
												<td class="posi_center ${classStr}">${f:h(m.allAccessCount)}</td>
												<td class="posi_center ${classStr}">
												<% //応募フォーム有の場合は応募件数を表示（0件はリンクしない） %>
												<c:choose>
													<c:when test="${m.applicationOkFlg}">
														<c:choose>
															<c:when test="${m.applicationCount eq 0}">
																${f:h(m.applicationCount)}
															</c:when>
															<c:otherwise>
																<a href="${f:url(m.applicationPath)}" target="_self">${f:h(m.applicationCount)}</a>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
												</td>
												<td class="posi_center ${classStr}">
												<c:choose>
													<c:when test="${m.preApplicationCount eq 0}">
														${f:h(m.preApplicationCount)}
													</c:when>
													<c:otherwise>
														<a href="${f:url(m.preApplicationPath)}" target="_self">${f:h(m.preApplicationCount)}</a>
													</c:otherwise>
												</c:choose>
												</td>
												<td class="posi_center ${classStr}">
												<c:choose>
													<c:when test="${m.phoneApplicationCount eq 0}">
														${f:h(m.phoneApplicationCount)}
													</c:when>
													<c:otherwise>
														<a href="${f:url(m.phoneApplicationPath)}" target="_self">${f:h(m.phoneApplicationCount)}</a>
													</c:otherwise>
												</c:choose>
												</td>
												<td class="posi_center ${classStr}">
												<c:choose>
													<c:when test="${m.renewalFlg eq true}">
														<c:set var="PREVIEW" value="${m.listPreviewUrl}" scope="page" />
														<a href="${PREVIEW}" id="previewPc" onclick="window.open('${f:h(PREVIEW)}', '', 'width=1280,height=600,scrollbars=yes'); return false;" target="PC_PREVIEW">View</a>
													</c:when>
													<c:otherwise>
														<a href="${f:url(gf:makePathConcat1Arg('/listPreview/list/showPcPreview', m.id))}" id="previewPc" onclick="editTarget('previewPc', 'PC_PREVIEW');" target="PC_PREVIEW">View</a>
													</c:otherwise>
												</c:choose>
												</td>
											</tr>
											</c:forEach>
										</tbody>
									</table>

									<!-- ページャー（下） -->
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

								<!-- ▼▼▼掲載求人がない場合▼▼▼ -->
								<c:if test="${existDataFlg eq false}">
								<div class="error"><ul><li>過去の掲載求人はございません。</li></ul></div>
								</c:if>

							</div>

						</div>
					</div>
					</s:form>
				</div>
</div>
<!-- #main# -->

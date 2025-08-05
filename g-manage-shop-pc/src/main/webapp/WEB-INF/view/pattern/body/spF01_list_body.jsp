<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${SHOP_CONTENS}/js/table.color.js"></script>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/pattern.css" />

<!-- #main# -->
<div id="main">
				<div id="wrap_pattern">
					<div class="page_back shopat_page_back pc_none">
						<a onclick="location.href='${f:url('/top/menu/')}'" id="btn_back">戻る</a>
					</div>

					<h2>定型文の設定</h2>
					<p class="explanation">
						定型文を作成･編集が行えます。<br>
						定型文を登録しておくと、応募者へのメール作成の際にいつでも呼び出して使うことができます。<br>編集を行う場合は「メール件名」をクリックまたはタップし、お進みください。
					</p>

					<html:errors/>
					<div id="wrap_patt_content">
						<div class="tab_area">
							<div class="tab_contents tab_active" id="pattern_list">
								<div class="pattern_list_area">
									<s:form action="${f:h(actionPath)}" method="post">
										<c:if test="${existDataFlg eq true}">
										<table cellpadding="0" cellspacing="0" border="0" class="all_table">
											<tbody>
												<tr>
													<th class="select_all" width="10px">
														<input type="checkbox" id="allcheck" class="checkBoxAll">
														<label for="allcheck"></label>
													</th>
													<th>メール件名</th>
													<th width="140px">登録日時</th>
												</tr>
												<c:forEach items="${list}" var="m" varStatus="status">
													<tr>
														<td class="table-checkbox">
														<input type="checkbox" value="${f:h(m.id)}" id="${f:h(m.id)}"
															name="changeIdArray" class="lumpSendCheck"><label
															for="${f:h(m.id)}"></label>
														</td>
														<td><a href="${f:url(m.detailPath)}">${f:h(m.sentenceTitle)}</a></td>
														<td><fmt:formatDate value="${f:date(m.registrationDatetime, 'yyyy-MM-dd HH:mm:ss')}" pattern="yyyy/MM/dd HH:mm" /></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										</c:if>
										<div class="wrap_btn">
											<html:button property="" value="新規作成" onclick="location.href='${f:url('/pattern/input/')}'" styleId="btn_newInput" />
											<html:submit property="doLumpDelete" value="一括削除" onclick="return confirm('削除してよろしいですか？')? true : false;" />
										</div>
									</s:form>
								</div>
							</div>
						</div>
					</div>
				</div>
</div>
<!-- #main# -->


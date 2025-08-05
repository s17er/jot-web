<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${SHOP_CONTENS}/js/checkConfFlg.js"></script>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/pattern.css" />

<!-- #main# -->
<div id="main">

				<div id="wrap_pattern">
					<div class="page_back">
						<a onclick="location.href='${f:url('/pattern/list/')}'" id="btn_back">戻る</a>
					</div>
					<h2>${f:h(pageTitle)}</h2>
					<html:errors/>
					<c:if test="${existDataFlg eq true}">
					<div id="wrap_patt_content">
						<div class="tab_area">
							<div class="tab_contents tab_active" id="pattern_list">
								<div class="pattern_list_area">
									<s:form action="${f:h(actionPath)}">
									<html:hidden property="hiddenId" />
										<table cellpadding="0" cellspacing="0" border="0"
											class="detail_table patt_table">
											<tbody>
												<tr>
													<th>メール件名</th>
													<td>${f:h(sentenceTitle)}</td>
												</tr>
												<tr>
													<th>定型文</th>
													<td>${f:br(f:h(body))}</td>
												</tr>
											</tbody>
										</table>
										<p class="patt_txt">
											!!target_member!! ＝ 応募者の名前が自動で入力されます。
										</p>
										<div class="wrap_btn">
											<c:choose>
												<c:when test="${pageKbn eq PAGE_INPUT or pageKbn eq PAGE_EDIT}">
													<html:submit property="submit"  value="登録"  styleId="btn_regist" />
													<html:submit property="correct"  value="訂正"  styleId="btn_correct" />
												</c:when>
												<c:when test="${pageKbn eq PAGE_DETAIL}">
													<html:button property="" value="削除" onclick="deleteConf('processFlg', 'deleteForm');" styleId="btn_delete" />
													<html:button property="" value="編集" onclick="location.href='${f:url(editPath)}'" styleId="btn_edit" />
												</c:when>
											</c:choose>
										</div>

									</s:form>
								</div>
							</div>
						</div>
					</div>

					<c:if test="${pageKbn eq PAGE_DETAIL}">
						<% //削除の場合は、確認のためにフラグを立てる  %>
						<s:form action="${f:h(deleteActionPath)}" styleId="deleteForm" >
							<html:hidden property="id" value="${f:h(id)}" /><!--
							--><html:hidden property="processFlg" styleId="processFlg" /><!--
							--><html:hidden property="version" value="${f:h(version)}" />
						</s:form>
					</c:if>

					</c:if>
				</div>

</div>

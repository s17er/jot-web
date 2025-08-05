<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/pattern.css" />

<!-- #main# -->
<div id="main">

				<div id="wrap_pattern">
					<c:choose>
						<c:when test="${pageKbn eq PAGE_EDIT}">
							<div class="page_back">
								<a onclick="location.href='${f:url('/pattern/detail/index/')}${id}'" id="btn_back">戻る</a>
							</div>
						</c:when>
						<c:otherwise>
							<div class="page_back">
								<a onclick="location.href='${f:url('/pattern/list/')}'" id="btn_back">戻る</a>
							</div>
						</c:otherwise>
					</c:choose>
					<h2>${f:h(pageTitle)}</h2>
					<html:errors />
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
													<td>
														<html:text property="sentenceTitle" />
													</td>
												</tr>
												<tr>
													<th>定型文</th>
													<td>
														<html:textarea property="body" cols="60" rows="10"></html:textarea>
													</td>
												</tr>
											</tbody>
										</table>
										<p class="patt_txt">
											!!target_member!! ＝ 応募者の名前が自動で入力されます。
										</p>
										<div class="wrap_btn">
											<html:submit property="submit"  value="登録"  styleId="btn_regist" />
										</div>
									</s:form>
								</div>
							</div>
						</div>
					</div>
					</c:if>
				</div>

</div>

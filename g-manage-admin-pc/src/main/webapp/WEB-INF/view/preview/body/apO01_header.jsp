<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />
<gt:typeList name="dbOtherConditionKbnList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD %>" />

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />
<c:set var="BRIEFING_KBN_JOIN" value="<%=MTypeConstants.BriefingPresentKbn.JOIN %>" scope="page" />

<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />

		<%-- パスを生成 --%>
		<c:choose>
		<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
			<c:set var="TAB_DETAIL_PATH" value="${gf:makePathConcat2Arg('/detailPreview/detail/displayDetail', id, inputFormKbn)}" scope="page" />
			<c:set var="TAB_MESSAGE_PATH" value="${gf:makePathConcat2Arg('/detailPreview/detail/displayMessage', id, inputFormKbn)}" scope="page" />
			<c:set var="TAB_MAP_PATH" value="${gf:makePathConcat2Arg('/detailPreview/detail/displayMap', id, inputFormKbn)}" scope="page" />
			<c:set var="LOGO_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayWebdataImage', MATERIAL_KBN_LOGO, idForDirName)}" scope="page" />
			<c:set var="TAB_SHOPLIST_PATH" value="${gf:makePathConcat3Arg('/detailPreview/detail/displayShopList', firstShopListId, id, inputFormKbn)}" scope="page" />
			<c:set var="MOVIE_PATH" value="/detailPreview/detail/displayMovie/${f:h(id)}/${f:h(inputFormKbn)}" scope="page" />
		</c:when>
		<c:otherwise>
			<c:set var="TAB_DETAIL_PATH" value="${gf:makePathConcat1Arg('/detailPreview/dbDetail/displayDetail', id)}" scope="page" />
			<c:set var="TAB_MESSAGE_PATH" value="${gf:makePathConcat1Arg('/detailPreview/dbDetail/displayMessage', id)}" scope="page" />
			<c:set var="TAB_MAP_PATH" value="${gf:makePathConcat1Arg('/detailPreview/dbDetail/displayMap', id)}" scope="page" />
			<c:set var="LOGO_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_LOGO)}" scope="page" />
			<c:set var="TAB_SHOPLIST_PATH" value="${gf:makePathConcat2Arg('/detailPreview/dbDetail/displayShopList', firstShopListId, id)}" scope="page" />
			<c:set var="MOVIE_PATH" value="/detailPreview/dbDetail/displayMovie/${f:h(id)}" scope="page" />
		</c:otherwise>
		</c:choose>



		<div id="loginSearch" class="clear">
			<p><strong>グルメ太郎 様　ログイン中</strong></p>
			<div class="area_btn">
				<input type="button" value="" id="btn_termSearch" />
				<input type="button" value="" id="btn_kentou" />
				<html:submit property="index" value="　" styleId="btn_NRlogout" />

			</div>
		</div>

		<hr />

		<div id="shop_name" class="clear">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="660">
						<h3>${f:h(manuscriptName)}<br />
						<span>[&nbsp;<strong>
							<%--業種（業態） --%>
							<c:forEach items="${industryKbnList}" var="industryValue" varStatus="status">
								${f:label(industryValue, dbIndustryKbnList, 'value', 'label')}
								<c:if test="${!status.last}">&nbsp;/&nbsp;</c:if>
							</c:forEach>
						</strong>&nbsp;]</span>
						</h3>
					</td>
					 <td width="250" class="shop_logo">
						<c:if test="${imageCheckMap[MATERIAL_KBN_LOGO]}">
							<img src="${f:url(LOGO_IMG_PATH)}" alt="${f:h(manuscriptName)}" />
						</c:if>
					</td>
				</tr>
				<tr>
					<td colspan="2"><!--
						--><div class="spc_icon clear">

							<%-- 雇用形態 --%>
							<c:forEach items="${employPtnList}" var="employValue">
								<img src="${f:h(frontHttpUrl)}${gf:concat4Str(imgLocation, '/icon_employ0', employValue, '.gif')}" alt="${f:label(employValue, dbEmployKbnList, 'value', 'label')}" />
							</c:forEach>
							<%--待遇検索条件 --%>
							<c:forEach items="${treatmentKbnList}" var="treatmentValue">
								<img src="${f:h(frontHttpUrl)}${gf:concat4Str(imgLocation, '/icon_treatment0', treatmentValue, '.gif')}" alt="${f:label(treatmentValue, dbTreatmentKbnList, 'value', 'label')}" />
							</c:forEach>
							<c:forEach items="${otherConditionKbnList}" var="otherConditionValue">
								<c:if test="${otherConditionValue ne 1}">
									<li><img src="${gf:concat5Str(frontHttpUrl, imgLocation, '/icon_aother0', otherConditionValue, '.gif')}" alt="${f:label(otherConditionValue, dbOtherConditionKbnList, 'value', 'label')}"  width="25" height="25" /></li>
								</c:if>
							</c:forEach>
							<c:if test="${not empty topInterviewUrl}">
								<a href="${f:h(topInterviewUrl)}" target="_blank"><img title="トップインタビュー" alt="トップインタビュー" src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/Interview.jpg" class="topinterview" /></a>
							</c:if>
							<c:if test="${not empty movieUrl}">
								<a href="${f:url(MOVIE_PATH)}"><img alt="動画で見る" src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/movie_btn.jpg" class="topinterview" /></a>
							</c:if>
							<c:if test="${BRIEFING_KBN_JOIN eq briefingPresentKbn}">
								<a href="#" >
									<img title="企業説明会参加" alt="企業説明会参加" src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/ParticipationCompany.jpg" class="topinterview" />
								</a>
							</c:if>
						</div>
								<!--

						--><p>掲載期間：
							<fmt:formatDate value="${postStartDatetime}" pattern="yyyy/MM/dd" />
							～<fmt:formatDate value="${postEndDatetime}" pattern="yyyy/MM/dd" />
						</p>
					</td>
				</tr>
			</table>
			<div class="spc_btn">
				<a href="#" title="検討中BOXに追加する"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_consider.gif" alt="検討中BOXに追加する" /></a>
				<a href="#" onclick="window.print(); return false;" title="この求人を印刷する"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_print.gif" alt="この求人を印刷する" /></a>
			</div>
		</div>
		<hr />

		<div id="detail_navi" class="clear">
			<ul><!--
					--><li id="${(jspTabKbn eq 'detail') ? 'mn_detail_on' : 'mn_detail_off'}"><a href="${f:url(TAB_DETAIL_PATH)}" title="求人情報詳細">求人情報詳細</a></li><!--
				<c:choose>
				<c:when test="${!empty mapAddress}">
					--><li id="${(jspTabKbn eq 'map') ? 'mn_map_on' : 'mn_map_off'}"><a href="${f:url(TAB_MAP_PATH)}" title="MAP">MAP</a></li><!--
				</c:when>
				</c:choose>
				<c:if test="${shopListDisplayFlg}">
					--><li id="${(jspTabKbn eq 'shopList') ? 'mn_shopList_on' : 'mn_shopList_off'}"><a href="${f:url(TAB_SHOPLIST_PATH)}">${f:h(LABEL_SHOPLIST)}</a></li><!--
				</c:if>
			--></ul>
			<hr />

			<div class="socialArea">
				<%-- facebook シェアボタン --%>
				<img src="${f:h(frontHttpUrl)}/images/preview/btn_facebook.png" alt="" />

				<%-- twetter ツイートボタン --%>
				<img src="${f:h(frontHttpUrl)}/images/preview/btn_twitter.png" alt="" />

				<%-- google+ +1ボタン --%>
				<img src="${f:h(frontHttpUrl)}/images/preview/btn_google.png" alt="" />

				<%-- TODO mixi チェックボタン--%>
				<img src="${f:h(frontHttpUrl)}/images/preview/btn_mixi.png" alt="" />
			</div>

		</div>


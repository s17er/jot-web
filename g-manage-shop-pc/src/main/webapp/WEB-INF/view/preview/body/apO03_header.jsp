<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm"%>

<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />
<gt:typeList name="dbOtherConditionKbnList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD %>" />

<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />

		<div id="loginSearch" class="clear">
			<p><strong>グルメ太郎 様　ログイン中</strong></p>
			<div class="area_btn">
				<input type="button" value="" id="btn_termSearch" />
				<input type="button" value="" id="btn_kentou" />
				<html:submit property="index" value="　" styleId="btn_NRlogout" />

			</div>
		</div>

		<hr />

		<div id="shop_name">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td>
						<h3>${f:h(customerName)}<br />

						<%-- 業種非表示
						<span>[&nbsp;<strong>
							<%--業種（業態） --%
							<c:forEach items="${industryKbnList}" var="industryValue" varStatus="status">
								${f:label(industryValue, dbIndustryKbnList, 'value', 'label')}
								<c:if test="${!status.last}">&nbsp;/&nbsp;</c:if>
							</c:forEach>
						</strong>&nbsp;]</span>
						 --%>
						</h3>
					</td>
					<td class="shop_logo">
						<%-- ロゴ画像非表示
						<c:if test="${imageCheckMap[MATERIAL_KBN_LOGO]}">
							<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_LOGO))}" alt="${f:h(manuscriptName)}" />
						</c:if>
						 --%>
					</td>
				</tr>
				<tr>
					<td colspan="2"><!--
						--><div class="spc_icon clear">

							<%-- 雇用形態 --%
							<c:forEach items="${employPtnList}" var="employValue">
								<img src="${f:h(frontHttpUrl)}${gf:concat3Str('/img/icon_employ0', employValue, '.gif')}" alt="${f:label(employValue, dbEmployKbnList, 'value', 'label')}" />
							</c:forEach>
							<%--待遇検索条件 --%
							<c:forEach items="${treatmentKbnList}" var="treatmentValue">
								<img src="${f:h(frontHttpUrl)}${gf:concat3Str('/img/icon_treatment0', treatmentValue, '.gif')}" alt="${f:label(treatmentValue, dbTreatmentKbnList, 'value', 'label')}" />
							</c:forEach>
							<c:forEach items="${otherConditionKbnList}" var="otherConditionValue">
								<c:if test="${otherConditionValue ne 1}">
									<li><img src="${gf:concat4Str(frontHttpUrl, '/img/icon_aother0', otherConditionValue, '.gif')}" alt="${f:label(otherConditionValue, dbOtherConditionKbnList, 'value', 'label')}"  width="25" height="25" /></li>
								</c:if>
							</c:forEach>
							<c:if test="${not empty topInterviewUrl}">
								<a href="${f:h(topInterviewUrl)}" target="_blank"><img title="トップインタビュー" alt="トップインタビュー" src="${f:h(frontHttpUrl)}/images/search/Interview.jpg" class="topinterview" /></a>
							</c:if>
							--%>
						</div><!--

						--><p>掲載期間：
							XXXX/XX/XX ～ XXXX/XX/XX
						</p>
					</td>
				</tr>
			</table>
		</div>
		<hr />

		<%-- パスを生成 --%>

		<div id="detail_navi" class="clear">
			<ul><!--
					--><li id="mn_detail_off"><a href="#" title="求人情報詳細">求人情報詳細</a></li><!--

					--><li id="mn_map_off"><a href="#" title="MAP">MAP</a></li><!--

					--><li id="mn_shopList_on"><a href="${f:url('/shopListPreview/detail/index')}">${f:h(LABEL_SHOPLIST)}</a></li><!--
			--></ul>
			<hr />

			<div class="spc_btn">
				<a href="#" title="検討中BOXに追加する"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_consider.gif" alt="検討中BOXに追加する" /></a>
				<a href="#" onclick="window.print(); return false;" title="この求人を印刷する"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_print.gif" alt="この求人を印刷する" /></a>
			</div>

		</div>


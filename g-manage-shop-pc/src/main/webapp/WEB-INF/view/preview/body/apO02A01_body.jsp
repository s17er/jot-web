<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm"%>
<%@ page import="com.gourmetcaree.db.common.constants.MAreaConstants" %>

<%
	String mAreaCd = String.valueOf(request.getAttribute("areaCd"));
%>
<c:set var="SENDAI_AREA" value="<%=MAreaConstants.AreaCd.SENDAI_AREA%>" />
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>" scope="page" />
<c:set var="SIZE_B" value="<%=MTypeConstants.SizeKbn.B %>" scope="page" />
<c:set var="SIZE_C" value="<%=MTypeConstants.SizeKbn.C %>" scope="page" />
<c:set var="SIZE_D" value="<%=MTypeConstants.SizeKbn.D %>" scope="page" />
<c:set var="SIZE_E" value="<%=MTypeConstants.SizeKbn.E %>" scope="page" />
<c:set var="SIZE_TEXT_WEB" value="<%=MTypeConstants.SizeKbn.TEXT_WEB %>" scope="page" />
<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />

<c:set var="MATERIAL_KBN_MAIN_1" value="<%=MTypeConstants.MaterialKbn.MAIN_1 %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_A" value="<%=MTypeConstants.MaterialKbn.PHOTO_A %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_B" value="<%=MTypeConstants.MaterialKbn.PHOTO_B %>" scope="page" />
<c:set var="MATERIAL_KBN_FREE" value="<%=MTypeConstants.MaterialKbn.FREE %>" scope="page" />

<c:set var="MATERIAL_TYPECODE" value="<%=MTypeConstants.MaterialKbn.TYPE_CD %>" scope="page" />
<c:set var="BRIEFING_JOIN_KBN_JOIN" value="<%=MTypeConstants.BriefingPresentKbn.JOIN%>" scope="page" />

<gt:typeList name="dbWebAreaList" typeCd="<%=MTypeConstants.WebAreaKbn.getTypeCd(mAreaCd) %>" noDisplayValue="<%=MTypeConstants.WebAreaKbn.getWebdataNoDispList(mAreaCd) %>" />
<gt:typeList name="sizeKbnList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />
<gt:typeList name="dbOtherConditionKbnList" typeCd="<%=MTypeConstants.OtherConditionKbn.TYPE_CD %>" />

<gt:typeList name="dbApplicationFormKbnList" typeCd="<%=MTypeConstants.ApplicationFormKbn.TYPE_CD %>"/>
<gt:typeList name="dbObservationKbnList" typeCd="<%=MTypeConstants.ObservationKbn.TYPE_CD %>"/>
<gt:typeList name="dbShopListDisplayKbnList" typeCd="<%=MTypeConstants.ShopListDisplayKbn.TYPE_CD %>"/>
<gt:typeList name="dbShopsKbnList" typeCd="<%=MTypeConstants.ShopsKbn.TYPE_CD %>"/>
<gt:specialList name="specialList"/>
<gt:typeList name="dbForeignAreaList" typeCd="<%=MTypeConstants.ForeignAreaKbn.getTypeCd(mAreaCd) %>"/>
<gt:typeList name="dbJobKbnList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>"/>
<gt:typeList name="dbAttentionShopFlgList" typeCd="<%=MTypeConstants.AttentionShopFlg.TYPE_CD %>"/>

<c:set var="OTHER_CONDITION_OPENING" value="<%=MTypeConstants.OtherConditionKbn.OPENING_STAFF %>" scope="page" />
<c:set var="OTHER_CONDITION_AMATEUR" value="<%=MTypeConstants.OtherConditionKbn.AMATEUR %>" scope="page" />
<c:set var="OTHER_CONDITION_NEW_GRADUATES" value="<%=MTypeConstants.OtherConditionKbn.NEW_GRADUATES %>" scope="page" />

<c:set var="ENUM_DRAFT_PREVIEW"  value="<%=ListForm.PreviewMethodKbn.DRAFT_PREVIEW %>" scope="page" />

	<div id="main">

		<%-- 詳細ページ共通ヘッダを差し込む --%>
		<div id="loginSearch" class="clear">
			<p><strong>グルメ太郎 様　ログイン中</strong></p>
			<div class="area_btn">
				<input type="button" value="" id="btn_termSearch" />
				<input type="button" value="" id="btn_kentou" />
				<html:submit property="index" value="　" styleId="btn_NRlogout" />

			</div>
		</div>

		<hr />

		<div id="result">
			<div id="number">
				<h3>
					以下の検索条件で&nbsp;<span>${(pageNavi != null) ? pageNavi.allCount : 0}</span>&nbsp;件が該当しました。
				</h3>
				<p>
					検索条件は指定されていません
				</p>
			</div>
			<hr />

			<div id="research">
				<a href="#">条件を追加・変更して再検索</a>
			</div>
		</div>
		<hr />


		<html:errors />



	<% //データが取得できなければ表示しない %>
	<c:if test="${existDataFlg eq true}">

		<div class="pageno clear">
			<ul class="mn_help">
				<li><a href="#" title="">アイコンの説明はコチラ</a></li>
			</ul>
			<ul class="pg_move"><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
				<c:choose>
					<c:when test="${dto.linkFlg eq true}">
						--><li><html:link href="${f:url(gf:makePathConcat1Arg('/listPreview/list/changePage', dto.pageNum))}" name="searchConditionMap"> ${dto.label}</html:link></li><!--
					</c:when>
					<c:otherwise>
						--><li>${dto.label}</li><!--
					</c:otherwise>
				</c:choose>
			</gt:PageNavi>
			--></ul>
		</div>
		<hr />

		<div id="wrapper">
			<c:forEach items="${dataList}" var="dto">
				<%-- パスを生成 --%>
				<c:choose>
				<c:when test="${previewMethodKbn eq ENUM_DRAFT_PREVIEW }">
					<c:set var="DETAIL_PREVIEW_PATH" value="${gf:makePathConcat3Arg('/detailPreview/draftDetail/index', dto.id, accessCd, areaCd)}" scope="page" />
				</c:when>
				<c:otherwise>
					<c:set var="DETAIL_PREVIEW_PATH" value="${gf:makePathConcat1Arg('/detailPreview/detail/index', dto.id)}" scope="page" />
				</c:otherwise>
				</c:choose>

				<c:set var="MAIN_1_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_MAIN_1)}" scope="page" />
				<c:set var="PHOTO_A_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_PHOTO_A)}" scope="page" />
				<c:set var="PHOTO_B_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_PHOTO_B)}" scope="page" />
				<c:set var="FREE_IMG_PATH" value="${gf:makePathConcat2Arg('/util/imageUtility/displayImage', dto.id, MATERIAL_KBN_FREE)}" scope="page" />


				<c:if test="${previewMethodKbn eq ENUM_DRAFT_PREVIEW}">
					<table cellpadding="0" cellspacing="1" class="preview_table" >
						<tr>
							<%-- 原稿番号 --%>
							<th>原稿番号</th>
							<td colspan="3">
								<c:if test="${not empty dto.id and dto.id gt 0}">
									${f:h(dto.id)}
								</c:if>
							</td>
							<%-- 掲載期間 --%>
							<th>掲載期間</th>
							<td colspan="5">
								<c:if test="${dto.postDateExist}">
									<fmt:formatDate value="${dto.postStartDatetime}" pattern="yyyy/MM/dd HH:mm"/>
									～<fmt:formatDate value="${dto.postEndDatetime}" pattern="yyyy/MM/dd HH:mm"/>
								</c:if>
							</td>
						</tr>
						<tr>
							<%-- 原稿名 --%>
							<th>原稿名</th>
							<td colspan="9">${f:h(dto.manuscriptName)}</td>
						</tr>
						<tr>
							<%-- サイズ --%>
							<th>サイズ</th>
							<td>${f:label(dto.sizeKbn , sizeKbnList, 'value', 'label')}</td>
							<%-- 応募フォーム区分 --%>
							<th>応募フォーム</th>
							<td>${f:label(dto.applicationFormKbn, dbApplicationFormKbnList, 'value', 'label')}</td>
							<%-- 質問・店舗見学フォーム --%>
							<th>質問・店舗見学フォーム</th>
							<td>${f:label(dto.observationKbn, dbObservationKbnList, 'value', 'label')}</td>
							<%-- 店舗一覧 --%>
							<th>${f:h(LABEL_SHOPLIST)}</th>
							<td>${f:label(dto.shopListDisplayKbn, dbShopListDisplayKbnList, 'value', 'label')}</td>
							<%-- 店舗数 --%>
							<th>店舗数</th>
							<td>${f:label(dto.shopsKbn, dbShopsKbnList, 'value', 'label')}</td>
						</tr>
						<tr>
							<%-- 特集 --%>
							<th>特集</th>
							<td colspan="9">
								<c:forEach items="${dto.specialIdList}" var="t" varStatus="sts">
									${f:label(t, specialList, 'value', 'label')}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 勤務地エリア(WEBエリアから名称変更) & 海外エリア --%>
							<th>勤務地エリア</th>
							<td colspan="9">
								<c:forEach items="${dto.webAreaKbnList}" var="t" varStatus="sts">
									${f:label(t, dbWebAreaList, 'value', 'label')}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
								<c:forEach items="${dto.foreignAreaKbnList}" var="t" varStatus="sts">
									<c:choose>
										<c:when test="${dto.webAreaKbnExist eq false and sts.first}">
										</c:when>
										<c:otherwise>
											/
										</c:otherwise>
									</c:choose>
									${f:label(t, dbForeignAreaList, 'value', 'label')}
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 勤務地詳細エリア --%>
							<th>勤務地詳細エリア</th>
							<td colspan="9">
                                <c:choose>
                                    <c:when test="${areaCd eq SENDAI_AREA}">
                                        <gt:typeList name="dbDetailAreaKbnList" typeCd="<%=MTypeConstants.DetailAreaKbnGroup.getTypeCd(mAreaCd) %>"/>
                                    </c:when>
                                    <c:otherwise>
                                        <gt:typeList name="dbDetailAreaKbnList" typeCd="<%=MTypeConstants.DetailAreaKbn.getTypeCd(mAreaCd) %>"/>
                                    </c:otherwise>
                                </c:choose>

								<c:forEach items="${dto.detailAreaKbnList}" var="t" varStatus="sts">
									${f:label(t, dbDetailAreaKbnList, 'value', 'label')}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 路線・最寄り駅--%>
							<th>路線・最寄駅</th>
							<td colspan="9">
								<c:forEach items="${dto.webRouteList}" var="t" varStatus="sts">
									${f:h(t.railroadName)}
									:${f:h(t.routeName)}
									:${f:h(t.stationName)}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 雇用形態 --%>
							<th>雇用形態</th>
							<td colspan="9">
								<c:forEach items="${dto.employPtnList}" var="t" varStatus="sts">
									${f:label(t, dbEmployKbnList, 'value', 'label')}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 職種検索条件区分 --%>
							<th>職種検索条件</th>
							<td colspan="9">
								<c:forEach items="${dto.jobKbnList}" var="t" varStatus="sts">
									${f:label(t, dbJobKbnList, 'value', 'label')}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 業態 --%>
							<th>業態</th>
							<td colspan="9">
								<c:forEach items="${dto.industryKbnList}" var="t" varStatus="sts">
									${f:label(t, dbIndustryKbnList, 'value', 'label')}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 待遇検索条件区分 --%>
							<th>待遇検索条件</th>
							<td colspan="9">
								<c:forEach items="${dto.treatmentKbnList}" var="t" varStatus="sts">
									${f:label(t, dbTreatmentKbnList, 'value', 'label')}
									<c:if test="${sts.last eq false}">
										/
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<%-- 他条件区分 --%>
							<c:set var="otherConditionMap" value="${dto.otherConditionExistNameMap}" scope="page" />
							<%-- その他条件 オープニングスタッフ --%>
							<th>オープニング<br />スタッフ募集</th>
							<td>${f:h(otherConditionMap[OTHER_CONDITION_OPENING])}</td>

							<%-- その他条件 シロウト --%>
							<th>飲食業界<br />未経験者歓迎</th>
							<td>${f:h(otherConditionMap[OTHER_CONDITION_AMATEUR])}</td>

							<%-- その他条件 新卒 --%>
							<th>新卒者歓迎</th>
							<td>${f:h(otherConditionMap[OTHER_CONDITION_NEW_GRADUATES])}</td>



							<%-- 応募先アドレス --%>
							<th>応募先<br />メールアドレス</th>
							<td colspan="3">
								${f:h(dto.mail)}
								<c:if test="${not empty dto.customerSubMailAddress}">
									<br />${f:h(dto.customerSubMailAddress)}
								</c:if>
							</td>
						</tr>
						<tr>
							<%-- 地図タイトル --%>
							<th>MAPタイトル</th>
							<td colspan="4">${f:h(dto.mapTitle)}</td>
							<%-- 地図用住所 --%>
							<th>MAP住所</th>
							<td colspan="4">${f:h(dto.mapAddress)}</td>
						</tr>
						<tr>
							<th>ピックアップ求人</th>
							<td>${f:label(dto.attentionShopFlg, dbAttentionShopFlgList, 'value', 'label')}</td>
							<th>ピックアップ求人<br />文章</th>
							<td colspan="7">${f:br(f:h(dto.attentionShopSentence))}</td>
						</tr>
						<tr>
							<th>IP電話</th>
							<td colspan="3">
								<c:forEach items="${dto.ipPhoneList}" var="t">
									${f:h(t)}<br />
								</c:forEach>
							</td>
							<th>受信元番号</th>
							<td colspan="5">
								<c:forEach items="${dto.telList}" var="t">
									${f:h(t)}<br />
								</c:forEach>
							</td>
						</tr>
					</table>
				</c:if>

				<div id="contents">
					<div id="header_shopname" class="clear">
						<h4><a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank">${f:h(gf:replaceStr(dto.manuscriptName, common['gc.webdata.shopName.trim.length'], common['gc.replaceStr']))}</a></h4>
							<c:if test="${not empty dto.industryKbnList}">
								<ul class="gyotai clear">
									<c:forEach var="industryValue" items="${dto.industryKbnList}">
										<li><img src="${gf:concat5Str(frontHttpUrl, imgLocation, '/icon_industry', industryValue, '.gif')}" alt="${f:label(industryValue, dbIndustryKbnList, 'value', 'label')}" /></li>
									</c:forEach>
								</ul>
							</c:if>
					</div>

					<div id="header_icon" class="clear">
						<c:if test="${not empty dto.treatmentKbnList or not empty dto.employPtnList}">
							<ul class="taigu clear">
								<c:forEach items="${dto.employPtnList}" var="employValue">
									<li><img src="${gf:concat5Str(frontHttpUrl, imgLocation, '/icon_employ0', employValue, '.gif')}" alt="${f:label(employValue, dbEmployKbnList, 'value', 'label')}" width="25" height="25" /></li>
								</c:forEach>
								<c:forEach items="${dto.treatmentKbnList}" var="treatmentValue">
									<li><img src="${gf:concat5Str(frontHttpUrl, imgLocation, '/icon_treatment0', treatmentValue, '.gif')}" alt="${f:label(treatmentValue, dbTreatmentKbnList, 'value', 'label')}"  width="25" height="25" /></li>
								</c:forEach>
								<c:forEach items="${dto.otherConditionKbnList}" var="otherConditionValue">
									<c:if test="${otherConditionValue ne 1}">
										<li><img src="${gf:concat5Str(frontHttpUrl, imgLocation, '/icon_aother0', otherConditionValue, '.gif')}" alt="${f:label(otherConditionValue, dbOtherConditionKbnList, 'value', 'label')}"  width="25" height="25" /></li>
									</c:if>
								</c:forEach>
							</ul>
						</c:if>

						<ul class="oubo">
							<li><a href="#" title="検討中BOXに追加する"><img src="${f:h(frontHttpUrl)}${f:h(imgLocation)}/btn_consider.gif" width="135" height="25" alt="検討中BOXに追加する" /></a></li>
							<li><a href="${f:url(DETAIL_PREVIEW_PATH)}"  target="_blank"><img src="${f:h(frontHttpUrl)}${f:h(imgLocation)}/btn_detail.gif" width="105" height="25" alt="求人詳細を見る" /></a></li>
							<c:if test="${not empty dto.topInterviewUrl}">
								<li class="interview"><a href="${f:h(dto.topInterviewUrl)}" target="_blank"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/Interview.jpg" width="135" height="25" /></a></li>
							</c:if>
							<c:if test="${not empty dto.movieUrl}">
								<c:choose>
									<c:when test="${previewMethodKbn eq ENUM_DRAFT_PREVIEW }">
										<c:set var="MOVIE_PATH" value="/detailPreview/draftDetail/displayMovie/${f:h(dto.id)}/${f:h(accessCd)}/${f:h(areaCd)}" scope="page" />
									</c:when>
									<c:otherwise>
										<c:set var="MOVIE_PATH" value="/detailPreview/detail/displayMovie/${f:h(dto.id)}" scope="page" />
									</c:otherwise>
								</c:choose>
								<li><a href="${f:url(MOVIE_PATH)}" target="blank"><img alt="動画で見る" src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/movie_btn.jpg" /></a></li>
							</c:if>
							<c:if test="${BRIEFING_JOIN_KBN_JOIN eq dto.briefingPresentKbn}">
								<li><a href="#" ><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/ParticipationCompany.jpg"" alt="企業説明会参加" width="100" height="25" /></a></li>
							</c:if>
						</ul>
					</div>


					<c:choose>
						<c:when test="${dto.sizeKbn eq SIZE_TEXT_WEB}">
							<c:if test="${dto.catchCopyExist}">
								<div id="mainVisual_textWeb">
									<h4>${f:br(dto.catchCopy)}</h4>
								</div>
							</c:if>
						</c:when>
						<c:when test="${dto.sizeKbn eq SIZE_A or dto.sizeKbn eq SIZE_B}">
							<div id="mainVisual_BA" class="clear">
								<c:if test="${dto.materialSearchListExistFlg eq true}">
									<div class="photo_BA">
										<a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank"><img src="${f:url(MAIN_1_IMG_PATH)}" width="246" height="186" alt="${f:h(dto.manuscriptName)}" /></a>
									</div>
								</c:if>
								<c:if test="${dto.catchCopyExist or not empty dto.sentence1}">
								<div class="text_box_BA">
									<c:if test="${dto.catchCopyExist}">
										<h4>${f:br(dto.catchCopy)}</h4>
									</c:if>
									<c:if test="${not empty dto.sentence1}">
										<p>${f:br(gf:removeHtmlTags(dto.sentence1))}</p>
									</c:if>
								</div>
								<div class="next">
									<a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank">…&raquo;&nbsp;続きを読む</a>
								</div>
								</c:if>
							</div>
						</c:when>
						<c:when test="${dto.sizeKbn eq SIZE_C}">
							<div id="mainVisual_C-type" class="clear">
								<c:if test="${dto.materialSearchListExistFlg eq true}">
									<div class="C_main_photo">
											<a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank"><img src="${f:url(MAIN_1_IMG_PATH)}" width="246" height="186" alt="${f:h(dto.manuscriptName)}" /></a>
									</div>
								</c:if>
								<c:if test="${dto.captionMaterialExist eq true}">
									<ul class="sub_photro">
										<c:if test="${dto.materialCaptionAExistFlg eq true}">
											<li><a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank"><img src="${f:url(PHOTO_A_IMG_PATH)}" width="121" height="92" alt="${f:h(dto.manuscriptName)}" /></a></li>
										</c:if>
										<c:if test="${dto.materialCaptionBExistFlg eq true}">
											<li><a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank"><img src="${f:url(PHOTO_B_IMG_PATH)}" width="121" height="92" alt="${f:h(dto.manuscriptName)}" /></a></li>
										</c:if>
									</ul>
								</c:if>
								<c:if test="${dto.catchCopyExist or not empty dto.sentence1}">
								<div class="text_box_C clear">
									<c:if test="${dto.catchCopyExist}">
										<h4>${f:br(dto.catchCopy)}</h4>
									</c:if>
									<c:if test="${not empty dto.sentence1}">
										<p>${f:br(gf:removeHtmlTags(dto.sentence1))}</p>
									</c:if>
								</div>
								<div class="next">
									<a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank">…&raquo;&nbsp;続きを読む</a>
								</div>
								</c:if>
							</div>
						</c:when>
						<c:when test="${dto.sizeKbn eq SIZE_D}">
							<div id="mainVisual_D-type" class="clear">
								<c:if test="${dto.materialSearchListExistFlg eq true}">
									<div class="D_free">
											<a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank"><img src="${f:url(FREE_IMG_PATH)}" width="370" height="250" alt="${f:h(dto.manuscriptName)}" /></a>
									</div>
								</c:if>
								<c:if test="${dto.catchCopyExist or not empty dto.sentence1}">
								<div class="text_box_D">
									<c:if test="${dto.catchCopyExist}">
										<h4>${f:br(dto.catchCopy)}</h4>
									</c:if>
									<c:if test="${not empty dto.sentence1}">
										<p>${f:br(gf:removeHtmlTags(dto.sentence1))}&nbsp;&nbsp;</p>
									</c:if>
								</div>
								<div class="next">
									<a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank">…&raquo;&nbsp;続きを読む</a>
								</div>
								</c:if>
							</div>
						</c:when>
						<c:when test="${dto.sizeKbn eq SIZE_E}">
							<c:if test="${dto.materialSearchListExistFlg eq true}">
								<div id="mainVisual">
										<a href="${f:url(DETAIL_PREVIEW_PATH)}" target="_blank"><img src="${f:url(FREE_IMG_PATH)}" width="880" height="270" alt="${f:h(dto.manuscriptName)}" /></a>
								</div>
							</c:if>
						</c:when>
					</c:choose>

					<c:if test="${dto.anyListInformation}">
						<table cellspacing="0" cellpadding="0" border="0" class="kikikirin">
							<c:if test="${not empty dto.recruitmentJob}">
								<tr class="section">
									<th class="shosai">募集職種</th>
									<td>${f:h(gf:removeCrToSpace(gf:replaceStr(dto.recruitmentJob, common['gc.webdata.common.trim.length'], common['gc.replaceStr2'])))}</td>
								</tr>
							</c:if>
							<c:if test="${not empty dto.salary}">
								<tr class="section">
									<th class="shosai">給与</th>
									<td>${f:h(gf:removeCrToSpace(gf:replaceStr(dto.salary, common['gc.webdata.common.trim.length'], common['gc.replaceStr2'])))}</td>
								</tr>
							</c:if>
							<c:if test="${not empty dto.personHunting}">
								<tr class="section">
									<th class="shosai">求める人物像・資格</th>
									<td>${f:h(gf:removeCrToSpace(gf:replaceStr(dto.personHunting, common['gc.webdata.common.trim.length'], common['gc.replaceStr2'])))}</td>
								</tr>
							</c:if>
							<c:if test="${not empty dto.workingPlace}">
								<tr>
									<th class="shosai">勤務地エリア・最寄駅</th>
									<td>${f:h(gf:removeCrToSpace(gf:replaceStr(dto.workingPlace, common['gc.webdata.common.trim.length'], common['gc.replaceStr2'])))}</td>
								</tr>
							</c:if>
						</table>
					</c:if>
				</div>
			</c:forEach>
		</div>



		<div class="pageno clear">
			<ul class="pg_move"><!--
			<gt:PageNavi var="dto" currentPage="${pageNavi.currentPage}" lastPage="${pageNavi.lastPage}">
				<c:choose>
					<c:when test="${dto.linkFlg eq true}">
						--><li><html:link href="${f:url(gf:makePathConcat1Arg('/webdata/list/changePage', dto.pageNum))}" name="searchConditionMap"> ${dto.label}</html:link></li><!--
					</c:when>
					<c:otherwise>
						--><li>${dto.label}</li><!--
					</c:otherwise>
				</c:choose>
			</gt:PageNavi>
		--></ul>
		</div>
	</c:if>
	<hr />
</div>


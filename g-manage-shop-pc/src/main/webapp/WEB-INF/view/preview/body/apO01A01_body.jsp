<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<gt:typeList name="dbIndustryKbnList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="dbEmployKbnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>" />
<gt:typeList name="dbTreatmentKbnList" typeCd="<%=MTypeConstants.TreatmentKbn.TYPE_CD %>" />

<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />

<c:set var="MATERIAL_KBN_RIGHT" value="<%=MTypeConstants.MaterialKbn.RIGHT %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN1" value="<%=MTypeConstants.MaterialKbn.MAIN_1 %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN2" value="<%=MTypeConstants.MaterialKbn.MAIN_2 %>" scope="page" />
<c:set var="MATERIAL_KBN_MAIN3" value="<%=MTypeConstants.MaterialKbn.MAIN_3 %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_A" value="<%=MTypeConstants.MaterialKbn.PHOTO_A %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_B" value="<%=MTypeConstants.MaterialKbn.PHOTO_B %>" scope="page" />
<c:set var="MATERIAL_KBN_PHOTO_C" value="<%=MTypeConstants.MaterialKbn.PHOTO_C %>" scope="page" />
<c:set var="MATERIAL_KBN_ATTENTION_HERE" value="<%=MTypeConstants.MaterialKbn.ATTENTION_HERE %>" scope="page" />
<c:set var="MATERIAL_KBN_LOGO" value="<%=MTypeConstants.MaterialKbn.LOGO %>" scope="page" />

<c:set var="OBSERVATION_KBN_QUESTION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION%>" scope="page" />
<c:set var="OBSERVATION_KBN_QUESTION_OBSERVATION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION%>" scope="page" />

<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>" scope="page" />
<c:set var="SIZE_C" value="<%=MTypeConstants.SizeKbn.C %>" scope="page" />
<c:set var="SIZE_TEXT" value="<%=MTypeConstants.SizeKbn.TEXT_WEB %>" scope="page" />

<html:errors />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<div id="main" class="clear">
	<div id="main">
		<%-- 詳細ページ共通ヘッダを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/apO01_header.jsp" %>
	</div>
	<c:if test="${not empty recruitmentJob}">
			<div id="txt-top">■募集職種&nbsp;${f:h(recruitmentJob)}</div>
		</c:if>

<!-- メイン画像表示切替用設定 -->
<script type="text/javascript" src="${f:h(frontHttpUrl)}/js/jquery.cycle.all.js"></script>
<script type="text/javascript">
$.fn.cycle.defaults.speed   = 900;
$.fn.cycle.defaults.timeout = 3000;

$(function() {
    // run the code in the markup!
    $('#demos pre code').each(function() {
        eval($(this).text());
    });
});
</script>
<style type="text/css">
pre { display:none }
</style>
<!-- メイン画像表示切替用設定-ココまで -->

		<div id="displayDetail">
		<div id="contents">
			<div class="intro clear">

				<c:if test="${blockMainImage eq true and blockMainImageExist}">
				<div class="photo_area">
					<c:choose>
						<c:when test="${sizeKbnABC}">
							<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
								<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN1))}" width="330" height="250" alt="${f:h(manuscriptName)}" />
							</c:if>
						</c:when>
						<c:when test="${sizeKbnDE}">
						<div id="demos">
						<div id="fade">
							<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN1))}" width="330" height="250" alt="${f:h(manuscriptName)}" />
							</c:if>
							<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN2]}">
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN2))}" width="330" height="250" alt="${f:h(manuscriptName)}" />
							</c:if>
							<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN3]}">
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN3))}" width="330" height="250" alt="${f:h(manuscriptName)}" />
							</c:if>
						</div>
						<pre><code class="mix">$('#fade').cycle();</code></pre>
						</div>
						</c:when>
					</c:choose>
					<c:if test="${sizeKbnDE eq true}">
						<ul class="sub_photo clear">
							<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN1]}">
								<li><img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN1))}" width="102" height="77" alt="${f:h(manuscriptName)}" /></li>
							</c:if>
							<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN2]}">
								<li><img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN2))}" width="102" height="77" alt="${f:h(manuscriptName)}" /></li>
							</c:if>
							<c:if test="${imageCheckMap[MATERIAL_KBN_MAIN3]}">
								<li><img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_MAIN3))}" width="102" height="77" alt="${f:h(manuscriptName)}" /></li>
							</c:if>
						</ul>
					</c:if>
				</div>
				</c:if>
				<c:if test="${catchCopyExist or not empty sentence1}">
					<c:choose>
						<c:when test="${sizeKbn eq SIZE_TEXT}">
							<div class="section">
								<c:if test="${catchCopyExist}">
									<h4>${f:br(catchCopy)}</h4>
								</c:if>
								<c:if test="${not empty sentence1}">
									<p>${f:br(sentence1)}</p>
								</c:if>
							</div>
						</c:when>
						<c:otherwise>
							<div class="comment">
								<c:if test="${catchCopyExist}">
									<h4>${f:br(catchCopy)}</h4>
								</c:if>
								<c:if test="${not empty sentence1}">
									<p>${f:br(sentence1)}</p>
								</c:if>
							</div>
						</c:otherwise>
					</c:choose>

				</c:if>
				</div>

				<c:if test="${(blockRight eq true or sizeKbn eq SIZE_C) and blockRightExist eq true}">
					<div class="intro2 clear">
						<c:if test="${blockPhoto eq true and imageCheckMap[MATERIAL_KBN_RIGHT]}">
							<div class="right_photo">
								<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_RIGHT))}" width="180" height="250" alt="${f:h(manuscriptName)}" />
							</div>
						</c:if>

						<c:if test="${not empty sentence2}">
							<c:choose>
								<c:when test="${sizeKbn eq SIZE_C}">
									<p>${f:br(sentence2)}</p>
								</c:when>
								<c:when test="${sizeKbnDE}">
									<div class="comment">
										<p>${f:br(sentence2)}</p>
									</div>
								</c:when>
							</c:choose>

						</c:if>
					</div>
				</c:if>

				<c:if test="${blockCaption eq true}">
					<div class="cap_area">
						<ul class="cap_photo clear">
							<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_A]}">
								<li>
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_A))}" width="235" height="170" alt="${f:h(manuscriptName)}" />
									<c:if test="${not empty captionA}">
										<p>${f:br(captionA)}</p>
									</c:if>
								</li>
							</c:if>
							<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_B]}">
								<li>
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_B))}" width="235" height="170" alt="${f:h(manuscriptName)}" />
									<c:if test="${not empty captionB}">
										<p>${f:br(captionB)}</p>
									</c:if>
								</li>
							</c:if>
							<c:if test="${imageCheckMap[MATERIAL_KBN_PHOTO_C]}">
								<li>
									<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_PHOTO_C))}" width="235" height="170" alt="${f:h(manuscriptName)}" />
									<c:if test="${not empty captionC}">
										<p>${f:br(captionC)}</p>
									</c:if>
								</li>
							</c:if>
						</ul>
					</div>
				</c:if>


				<c:if test="${blockSentence3 and not empty sentence3}">
					<div class="md_text">
						<p>${f:br(sentence3)}</p>
					</div>
				</c:if>

				<c:if test="${blockAttentionHere and blockAttentionHereExist and not empty attentionHereTitle}">
					<div class="chumoku">
						<div class="ch_area clear">
							<c:if test="${imageCheckMap[MATERIAL_KBN_ATTENTION_HERE]}">
							<div class="ch_photo">
								<img src="${f:url(gf:makePathConcat2Arg('/util/imageUtility/displayImage', id, MATERIAL_KBN_ATTENTION_HERE))}" width="175" height="150" alt="${f:h(manuscriptName)}" />
							</div>
							</c:if>

							<div class="comment">
								<c:if test="${not empty attentionHereTitle}"><P class="chTitle">【${f:h(attentionHereTitle)}】</P></c:if>
								<c:if test="${not empty attentionHereSentence}"> <p class="chText">${f:br(attentionHereSentence)}</p></c:if>
				        	</div>
						</div>

					</div>
				</c:if>
			</div>

			<c:if test="${recruitInformationExist eq true}">
				<table cellspacing="0" cellpadding="0" border="0" class="tbl_detail tbl_detail_top" summary="募集要項">
				<tbody>
					<tr>
						<th class="title" colspan="2">募集要項</th>
					</tr>
					<c:if test="${not empty recruitmentJob}">
						<tr>
							<th>募集職種</th>
							<td>${f:br(f:h(recruitmentJob))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty salary}">
						<tr>
							<th>給与</th>
							<td>${f:br(f:h(salary))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty personHunting}">
						<tr>
							<th>求める人物像・資格</th>
							<td>${f:br(f:h(personHunting))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty workingHours}">
						<tr>
							<th>勤務時間</th>
							<td>${f:br(f:h(workingHours))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty workingPlace}">
						<tr>
							<th>勤務地エリア・最寄駅</th>
							<td>${f:br(f:h(workingPlace))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty workingPlaceDetail}" >
						<tr>
							<th>勤務地詳細</th>
							<td>${f:br(f:h(workingPlaceDetail))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty treatment}">
						<tr>
							<th>待遇</th>
							<td>${f:br(f:h(treatment))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty holiday}">
						<tr>
							<th>休日休暇</th>
							<td>${f:br(f:h(holiday))}</td>
						</tr>
					</c:if>
				</tbody>
				</table>
			</c:if>

			<c:if test="${shopDataExist eq true}">
				<table cellspacing="0" cellpadding="0" border="0" class="tbl_detail" summary="お店・会社DATA">
				<tbody>
					<tr>
						<th class="title" colspan="2">お店・会社DATA</th>
					</tr>
					<c:if test="${not empty seating}">
						<tr>
							<th>客席数・坪数</th>
							<td>${f:br(f:h(seating))}</td>
						</tr>
					</c:if>
					<c:if test="${not empty unitPrice}">
						<tr>
							<th>客単価</th>
							<td>${f:br(f:h(unitPrice))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty businessHours}">
						<tr>
							<th>営業時間</th>
							<td>${f:br(f:h(businessHours))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty openingDay}">
						<tr>
							<th>オープン日</th>
							<td>${f:br(f:h(openingDay))}&nbsp;</td>
						</tr>
					</c:if>

					<c:if test="${not empty shopInformation}">
						<tr>
							<th>会社情報</th>
							<td>${f:br(f:h(shopInformation))}</td>
						</tr>
					</c:if>

					<c:if test="${anyHomepageAddressExist eq true}">
						<tr>
							<th>ホームページ</th>
							<td>
								<c:if test="${not empty homepage1}">
									<a href="${f:h(homepage1)}" target="_blank">${f:h(displayHomepage1)}</a>&nbsp;<br />
								</c:if>
								<c:if test="${not empty homepage2}">
									<a href="${f:h(homepage2)}" target="_blank">${f:h(displayHomepage2)}</a>&nbsp;<br />
								</c:if>
								<c:if test="${not empty homepage3}">
									<a href="${f:h(homepage3)}" target="_blank">${f:h(displayHomepage3)}</a>&nbsp;
								</c:if>
							</td>
						</tr>
					</c:if>
				</tbody>
				</table>
			</c:if>

			<c:if test="${applicationDataExist eq true}">
				<table cellspacing="0" cellpadding="0" border="0" class="tbl_detail" summary="応募DATA">
				<tbody>
					<tr>
						<th class="title" colspan="2">応募DATA</th>
					</tr>

					<c:if test="${not empty phoneReceptionist}">
						<tr>
							<th>電話番号/受付時間</th>
							<td>
								${f:br(f:h(phoneReceptionist))}
							</td>
						</tr>
					</c:if>

					<c:if test="${not empty applicationMethod}">
						<tr>
							<th>応募方法</th>
							<td>${f:br(f:h(applicationMethod))}</td>
						</tr>
					</c:if>

					<c:if test="${not empty addressTraffic}">
						<tr>
							<th>面接地住所/交通</th>
							<td>${f:br(f:h(addressTraffic))}</td>
						</tr>
					</c:if>
					</tbody>
				</table>
			</c:if>
			<c:if test="${applicationButtonBlock}">
				<div class="button-bottom">
					<c:choose>
						<c:when test="${applicationOkFlg}">
							<a href="#"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_entryForm.gif" alt="応募フォームはこちら" /></a>
							<c:set var="observationClassName" value="marginLeft" scope="page" />
						</c:when>
						<c:otherwise>
							<c:set var="observationClassName" value="" scope="page" />
						</c:otherwise>
					</c:choose>

					<c:choose>
						<c:when test="${observationKbn eq OBSERVATION_KBN_QUESTION}">
							<a href="#" class="${f:h(observationClassName)}"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/tokyo/btn_question.gif" /></a>
						</c:when>
						<c:when test="${observationKbn eq OBSERVATION_KBN_QUESTION_OBSERVATION}">
							<a href="#" class="${f:h(observationClassName)}"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/tokyo/btn_QorTenpo.gif" /></a>
						</c:when>
					</c:choose>
				</div>
			</c:if>
		</div>
	</div>
</c:if>

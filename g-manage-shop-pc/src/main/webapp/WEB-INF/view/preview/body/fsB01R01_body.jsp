<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<c:set var="MATERIAL_MAIN_1" value="<%=MTypeConstants.MaterialKbn.MAIN_1 %>" scope="page" />
<c:set var="detailMsgPath" value="${gf:makePathConcat3Arg('/detailPreview/smartDraftDetail/displayDetailMsg', id, accessCd, siteAreaCd )}"></c:set>
<c:set var="catchCopyLength" value="40" scope="page" />
<c:set var="sentence1Length" value="13" scope="page" />
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<c:set var="ICON_SIZE" value="20" scope="page" />

<c:choose>
	<c:when test="${shopMessageExist eq true}">
		<c:set var="backPath" value="${gf:makePathConcat3Arg('/detailPreview/smartDraftDetail/displayDetailMsg', id, accessCd, siteAreaCd)}" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="backPath" value="${gf:makePathConcat3Arg('/listPreview/draftList/showSmartKPreview', id, accessCd, siteAreaCd)}" scope="request" />
	</c:otherwise>
</c:choose>

<%-- フッター帯の部分をインクルード --%>
<jsp:include page="/WEB-INF/view/preview/smart/fsB01R01_foorter.jsp" />
<%-- 共通部分をインクルード --%>
<jsp:include page="fsB01R_header.jsp" />

<c:if test="${existDataFlg eq true}">
<jsp:include page="/WEB-INF/view/preview/smart/headerContents.jsp" />



<!-- // 募集要項 -->
<c:if test="${recruitmentDataExist eq true}">
	<div class="gBar">募集要項</div>
	<dl class="dataBox">
		<c:if test="${not empty recruitmentJob}">
			<dt>◆募集職種</dt>
			<dd>
				${f:br(f:h(recruitmentJob)) }
			</dd>
		</c:if>

		<c:if test="${not empty salary}">
			<dt>◆給与</dt>
			<dd>
				${f:br(f:h(salary))}
			</dd>
		</c:if>

		<c:if test="${not empty personHunting}">
			<dt>◆求める人物像・資格</dt>
			<dd>${f:br(f:h(personHunting))}</dd>
		</c:if>
		<c:if test="${not empty workingHours}">
			<dt>◆勤務時間</dt>
			<dd>${f:br(f:h(workingHours))}</dd>
		</c:if>

		<c:if test="${not empty workingPlace}">
			<dt>◆勤務地エリア・最寄駅</dt>
			<dd>${f:br(f:h(workingPlace))}</dd>
		</c:if>

		<c:if test="${not empty workingPlaceDetail}">
			<dt>◆勤務地詳細</dt>
			<dd>${f:br(f:h(workingPlaceDetail))}</dd>
		</c:if>

		<c:if test="${not empty treatment}">
			<dt>◆待遇</dt>
			<dd>
				${f:br(f:h(treatment))}
			</dd>
		</c:if>

		<c:if test="${not empty holiday}">
			<dt>◆休日休暇</dt>
			<dd>
				${f:br(f:h(holiday))}
			</dd>
		</c:if>
	</dl>
</c:if>


	<%-- 応募の部分をインクルード --%>
	<jsp:include page="fsB01R_application.jsp" />

<c:if test="${shopDataExistForSmart eq true}">
	<!-- // お店・会社情報 -->
	<div class="gBar">お店・会社DATA</div>
	<dl class="dataBox">
		<c:if test="${not empty seating}">
			<dt>◆客席数・坪数</dt>
			<dd>${f:br(f:h(seating))}</dd>
		</c:if>

		<c:if test="${not empty unitPrice}">
			<dt>◆客単価</dt>
			<dd>${f:br(f:h(unitPrice))}</dd>
		</c:if>

		<c:if test="${not empty businessHours}">
			<dt>◆営業時間</dt>
			<dd>${f:br(f:h(businessHours))}</dd>
		</c:if>

		<c:if test="${not empty openingDay}">
			<dt>◆オープン日</dt>
			<dd>${f:br(f:h(openingDay))}</dd>
		</c:if>

		<c:if test="${not empty shopInformation}">
			<dt>◆会社情報</dt>
			<dd>${f:br(f:h(shopInformation))}</dd>
		</c:if>

		<c:if test="${not empty homepage1 or not empty homepage2 or  not empty homepage3}">
			<dt>◆ホームページ</dt>
			<dd>
				<c:if test="${not empty homepage1}">
					<a href="${f:h(homepage1)}" target="_blank">${f:h(displayHomepage1)}</a><br>
				</c:if>
				<c:if test="${not empty homepage2}">
					<a href="${f:h(homepage2)}" target="_blank">${f:h(displayHomepage2)}</a><br>
				</c:if>
				<c:if test="${not empty homepage3}">
					<a href="${f:h(homepage3)}" target="_blank">${f:h(displayHomepage3)}</a>
				</c:if>
			</dd>
		</c:if>

		<c:if test="${sizeKbn eq SIZE_A and not empty message}">
			<dt>◆メッセージ</dt>
			<dd>${f:br(f:h(message))}</dd>
		</c:if>
	</dl>
</c:if>

<c:if test="${applicationDataExistForSmart eq true}">
	<!-- // 応募DATA -->
	<div class="gBar">応募DATA</div>
	<dl class="dataBox">
		<c:if test="${not empty phoneReceptionist}">
			<dt id="tel">◆電話番号/受付時間</dt>
			<dd>${f:br(gf:editTelLink(f:h(phoneReceptionist), true))}
		</c:if>
		<c:if test="${not empty applicationMethod}">
			<dt>◆応募方法</dt>
			<dd>${f:br(f:h(applicationMethod))}<br>
		</c:if>
		<c:if test="${not empty addressTraffic or not empty mapAddress}">
			<dt>◆面接地住所/交通</dt>
			<dd>
				<c:if test="${not empty mapAddress}">
					<a href="${f:h(gf:concat2Str('https://maps.google.co.jp/maps?hl=ja&q=', mapAddress))}"  target="_blank" id="mapLink">${f:h(mapTitleForSmart)}</a>
				</c:if>
				<c:if test="${not empty addressTraffic}">
					${f:br(f:h(addressTraffic))}
				</c:if>
			</dd>
		</c:if>
	</dl>
</c:if>

<!-- // 詳細を見る -->
<c:if test="${shopMessageExist eq true}">
	<jsp:include page="../smart/detailFooterTab.jsp" />
</c:if>


	<%-- 応募の部分をインクルード --%>
	<jsp:include page="fsB01R_application.jsp" />

</c:if>



<c:if test="${publicationEndFlg eq true}">
	<div id="contents">
		<div id="industry">
			<span>【業態】</span>
			${f:h(recruitmentIndustry)}
		</div>
	</div>
	<div id="endData">
		<p>この求人情報は、掲載期間が終了しています。</p>
		<c:if test="${customerWebdataExistFlg eq true}">
			<a href="${f:url(listPath) }">&raquo;現在掲載中の「${f:h(customerName)}」の求人はこちら</a>
		</c:if>
	</div>
</c:if>
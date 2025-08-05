<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants.ManagementScreenKbn" %>

<%@page pageEncoding="UTF-8"%>
<!-- #main# -->
<div id="main">
<c:if test="${unReadApplicationMailFlg || unReadPreApplicationMailFlg || unReadScoutMailFlg || unReadObservateApplicationMailFlg || unReadArbeitMailFlg}">
	<div class="notification_block">
		<ul>
			<c:if test="${unReadApplicationMailFlg}">
				<li><a href="${f:url('/applicationMail/list/')}"><span class="material-icons">chevron_right</span> 未読の応募メールが${applicationMailCount}件あります。</a></li>
			</c:if>
			<c:if test="${unReadPreApplicationMailFlg}">
				<li><a href="${f:url('/preApplicationMail/list/')}"><span class="material-icons">chevron_right</span> 未読のプレ応募メールが${preApplicationMailCount}件あります。</a></li>
			</c:if>
			<c:if test="${unReadScoutMailFlg}">
				<li><a href="${f:url('/scoutMail/list/')}"><span class="material-icons">chevron_right</span> 未読のスカウトメールが${scoutMailCount}件あります。</a></li>
			</c:if>
			<c:if test="${unReadObservateApplicationMailFlg}">
				<li><a href="${f:url('/observateApplicationMail/list/')}"><span class="material-icons">chevron_right</span> 未読の質問メールが${observateApplicationMailCount}件あります。</a></li>
			</c:if>
		</ul>
	</div>
</c:if>

<div id="wrap_state">
	<c:choose>
		<c:when test="${list.size() == 0 }">
			<h2>現在、掲載中の求人がございません。</h2>
		</c:when>
		<c:when test="${list.size() > 0 }">
			<h2>現在、掲載中の求人アクティビティです。</h2>
		</c:when>
	</c:choose>

	<gt:areaList name="areaList" />
	<gt:typeList name="sizeList" typeCd="<%=MTypeConstants.SizeKbn.TYPE_CD %>" />

	<div class="stats_area">
		<c:forEach var="m" varStatus="status" items="${list}">
			<c:if test="${status.index == 1}">
				<div id="accordion_datas">
			</c:if>
			<div class="stats_data">
				<div class="annotation">
					<div class="name"><a href="${m.previewUrl}" target="_blank">${m.manuscriptName}</a></div>
					<ul class="tag">
						<li>掲載エリア:&nbsp;${f:label(m.areaCd, areaList, 'value', 'label')}</li>
						<li>原稿番号:&nbsp;${m.id}</li>
						<li>掲載期間:&nbsp;<fmt:formatDate value="${m.postStartDatetime}" pattern="yyyy/MM/dd" /> ～ <fmt:formatDate value="${m.postEndDatetime}" pattern="yyyy/MM/dd" /></li>
					</ul>
				</div>
				<ul>
					<li class="size">
						<h3>原稿サイズ</h3>
						<div class="present">${f:label(m.sizeKbn, sizeList, 'value', 'label')}<span> type</span></div>
					</li>
					<li class="page_view">
						<h3>PV数</h3>
						<div class="present">${m.allAccessCount}<span> 回</span></div>
					</li>
					<li class="application">
						<h3>応募</h3>
						<div class="present">${m.applicationCount}<span> 件</span></div>
					</li>
					<li class="pre_application">
						<h3>プレ応募</h3>
						<div class="present">${m.preApplicationCount}<span> 件</span></div>
					</li>
					<li class="tel_application">
						<h3>電話応募</h3>
						<div class="present">${m.ipPhoneHistoryCount}<span> 件</span></div>
					</li>
				</ul>
			</div>
			<c:if test="${status.last && list.size() > 1}">
				</div>
				<button id="more_stats" onclick="more_stats()"><span class="material-icons-outlined">keyboard_arrow_down</span>&nbsp;掲載中の他の原稿を<span id="viewText">見る</span></button>
			</c:if>
		</c:forEach>
	</div>
	<p class="data_link"><a href="${f:url('/webdata/list')}">これまでに掲載した原稿データを見る</a></p>
</div>

<!-- #wrap_news# -->
<h2 title="グルメキャリーからのお知らせ" class="wrap_news_title">グルメキャリーからのお知らせ</h2>
<div id="wrap_news">
<gt:informationTag name="information" areaCd="${userDto.areaCd}" managementScreenKbn="<%=ManagementScreenKbn.SHOP_SCREEN%>"/>
<div>
	 ${information}
</div>
<!-- #wrap_news# -->

</div>
<!-- #main# -->

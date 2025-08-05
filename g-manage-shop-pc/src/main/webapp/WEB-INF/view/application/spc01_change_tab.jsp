<%@page import="com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="MAIL_KBN" value="<%=MAIL_KBN.APPLICATION_MAIL %>" scope="page" />

<div id="wrap_tab" class="clear">
<ul id="mn_tab" class="clear">
	<c:choose>
		<%-- 応募 --%>
		<c:when test="${mailKbn eq 'APPLICATION_MAIL' or mailKbn eq 'APPLICATION_MAIL_LIST'}">
			<li id="mn_entryMail"><a href="${f:url('/applicationMail/list/')}"  title="応募メールに切替">応募メールに切替</a></li>
			<li id="mn_noPreEntryMail"><a href="${f:url('/preApplicationMail/list/')}"  title="プレ応募メールに切替">プレ応募メールに切替</a></li>
			<li id="mn_noScoutMail"><a href="${f:url('/scoutMail/list/mailBox/')}" title="スカウトメールに切替" class="scout_a">スカウトメールに切替</a></li>
			<li id="mn_noobservationMail"><a href="${f:url('/observateApplicationMail/list/')}" title="質問メールに切替">質問メールに切替</a></li>
			<li id="mn_noarbeitMail"><a href="${f:url('/arbeitMail/list/')}" title="グルメdeバイトメールに切替">グルメdeバイトメールに切替</a></li>
		</c:when>
		<%-- プレ応募 --%>
		<c:when test="${mailKbn eq 'PRE_APPLICATION_MAIL' or mailKbn eq 'PRE_APPLICATION_MAIL_LIST'}">
			<li id="mn_noentryMail"><a href="${f:url('/applicationMail/list/')}"  title="応募メールに切替">応募メールに切替</a></li>
			<li id="mn_preEntryMail"><a href="${f:url('/preApplicationMail/list/')}"  title="プレ応募メールに切替">プレ応募メールに切替</a></li>
			<li id="mn_noScoutMail"><a href="${f:url('/scoutMail/list/mailBox/')}" title="スカウトメールに切替" class="scout_a">スカウトメールに切替</a></li>
			<li id="mn_noobservationMail"><a href="${f:url('/observateApplicationMail/list/')}" title="質問メールに切替">質問メールに切替</a></li>
			<li id="mn_noarbeitMail"><a href="${f:url('/arbeitMail/list/')}" title="グルメdeバイトメールに切替">グルメdeバイトメールに切替</a></li>
		</c:when>
		<%-- スカウト --%>
		<c:when test="${mailKbn eq 'SCOUT_MAIL' or mailKbn eq 'SCOUT_MAIL_LIST'}">
			<li id="mn_noentryMail"><a href="${f:url('/applicationMail/list/')}"  title="応募メールに切替">応募メールに切替</a></li>
			<li id="mn_noPreEntryMail"><a href="${f:url('/preApplicationMail/list/')}"  title="プレ応募メールに切替">プレ応募メールに切替</a></li>
			<li id="mn_scoutMail"><a href="${f:url('/scoutMail/list/mailBox/')}" title="スカウトメールに切替" class="scout_a">スカウトメールに切替</a></li>
			<li id="mn_noobservationMail"><a href="${f:url('/observateApplicationMail/list/')}" title="質問メールに切替">質問メールに切替</a></li>
			<li id="mn_noarbeitMail"><a href="${f:url('/arbeitMail/list/')}" title="グルメdeバイトメールに切替">グルメdeバイトメールに切替</a></li>
		</c:when>
		<%-- 質問 --%>
		<c:when test="${mailKbn eq 'OBSERVATE_APPLICATION' or mailKbn eq 'OBSERVATE_APPLICATION_LIST'}">
			<li id="mn_noentryMail"><a href="${f:url('/applicationMail/list/')}"  title="応募メールに切替">応募メールに切替</a></li>
			<li id="mn_noPreEntryMail"><a href="${f:url('/preApplicationMail/list/')}"  title="プレ応募メールに切替">プレ応募メールに切替</a></li>
			<li id="mn_noScoutMail"><a href="${f:url('/scoutMail/list/mailBox/')}" title="スカウトメールに切替" class="scout_a">スカウトメールに切替</a></li>
			<li id="mn_observationMail"><a href="${f:url('/observateApplicationMail/list/')}" title="質問メールに切替">質問メールに切替</a></li>
			<li id="mn_noarbeitMail"><a href="${f:url('/arbeitMail/list/')}" title="グルメdeバイトメールに切替">グルメdeバイトメールに切替</a></li>
		</c:when>
		<%-- グルメdeバイト --%>
		<c:when test="${mailKbn eq 'ARBEIT_APPLICATION' or mailKbn eq 'ARBEIT_APPLICATION_LIST'}">
			<li id="mn_noentryMail"><a href="${f:url('/applicationMail/list/')}"  title="応募メールに切替">応募メールに切替</a></li>
			<li id="mn_noPreEntryMail"><a href="${f:url('/preApplicationMail/list/')}"  title="プレ応募メールに切替">プレ応募メールに切替</a></li>
			<li id="mn_noScoutMail"><a href="${f:url('/scoutMail/list/mailBox/')}" title="スカウトメールに切替" class="scout_a">スカウトメールに切替</a></li>
			<li id="mn_noobservationMail"><a href="${f:url('/observateApplicationMail/list/')}" title="質問メールに切替">質問メールに切替</a></li>
			<li id="mn_arbeitMail"><a href="${f:url('/arbeitMail/list/')}" title="グルメdeバイトメールに切替">グルメdeバイトメールに切替</a></li>
		</c:when>
	</c:choose>
</ul>
<hr />

<ul id="mn_how">
<c:choose>
	<%-- 応募 --%>
	<c:when test="${mailKbn eq 'APPLICATION_MAIL'}">
		<li id="mn_use"><a href="#" onclick="openShopApplicationMail(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
	<%-- スカウト --%>
	<c:when test="${mailKbn eq 'SCOUT_MAIL'}">
		<li id="mn_use"><a href="#" onclick="openShopApplicationMail(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
	<%-- 質問 --%>
	<c:when test="${mailKbn eq 'OBSERVATE_APPLICATION'}">
		<li id="mn_use"><a href="#" onclick="openShopApplicationMail(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
	<%-- グルメdeバイト --%>
	<c:when test="${mailKbn eq 'ARBEIT_APPLICATION'}">
		<li id="mn_use"><a href="#" onclick="openShopApplicationMail(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
</c:choose>
</ul>

<ul id="mn_how">
<c:choose>
	<c:when test="${mailKbn eq 'APPLICATION_MAIL_LIST'}">
		<li id="mn_icon"><a href="#" onclick="openShopApplicationIcon(${userDto.areaCd});return false;">アイコンの説明はコチラ</a></li>
				<li id="mn_use"><a href="#" onclick="openShopApplicant(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
	<c:when test="${mailKbn eq 'SCOUT_MAIL_LIST'}">
		<li id="mn_icon"><a href="#" onclick="openShopApplicationIcon(${userDto.areaCd});return false;">アイコンの説明はコチラ</a></li>
				<li id="mn_use"><a href="#" onclick="openShopApplicant(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
	<c:when test="${mailKbn eq 'OBSERVATE_APPLICATION_LIST'}">
		<li id="mn_icon"><a href="#" onclick="openShopApplicationIcon(${userDto.areaCd});return false;">アイコンの説明はコチラ</a></li>
				<li id="mn_use"><a href="#" onclick="openShopApplicant(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
	<c:when test="${mailKbn eq 'ARBEIT_APPLICATION_LIST'}">
		<li id="mn_icon"><a href="#" onclick="openShopApplicationIcon(${userDto.areaCd});return false;">アイコンの説明はコチラ</a></li>
				<li id="mn_use"><a href="#" onclick="openShopApplicant(${userDto.areaCd});return false;">このページの使い方</a></li>
	</c:when>
</c:choose>
</ul>

</div>

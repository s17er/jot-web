<%@page pageEncoding="UTF-8"%>
<!-- <ul>タグ内にinsertしてください。 -->
<c:choose>
	<%-- キープBOXから来た場合 --%>
	<c:when test="${fromKeepboxFlg eq true }">
		<li id="mn_noMemberList"><a href="${f:url(listPath)}" title="求職者一覧に切替">求職者一覧に切替</a></li>
		<li id="mn_noScoutMail"><a href="${f:url('/scoutMail/list/')}" title="スカウトメール・気になる通知に切替" class="scout_a">スカウトメール・気になる通知に切替</a></li>
		<li id="mn_keepBox"><a href="${f:url(keepBoxPath)}" title="キープBOXに切替">キープBOXに切替</a></li>
	</c:when>
	<%-- スカウトメール（スカウト・足あと）から来た場合 --%>
	<c:when test="${fromScoutMailFlg eq true }">
		<li id="mn_noMemberList"><a href="${f:url(listPath)}" title="求職者一覧に切替">求職者一覧に切替</a></li>
		<li id="mn_scoutMail"><a href="${f:url('/scoutMail/list/')}" title="スカウトメール・気になる通知に切替" class="scout_a">スカウトメール・気になる通知に切替</a></li>
		<li id="mn_noKeepBox"><a href="${f:url(keepBoxPath)}" title="キープBOXに切替">キープBOXに切替</a></li>
	</c:when>
	<%-- スカウトメール（メールBOX）から来た場合 --%>
	<c:when test="${fromMailBoxScoutMailFlg eq true }">
		<li id="mn_noentryMail"><a href="${f:url('/application/list/')}"  title="応募メールに切替">応募メールに切替</a></li>
		<li id="mn_noPreEntryMail"><a href="${f:url('/preApplicationMail/list/')}"  title="プレ応募メールに切替">プレ応募メールに切替</a></li>
		<li id="mn_scoutMail"><a href="${f:url('/scoutMail/list/mailBox/')}" title="スカウトメール・気になる通知に切替" class="scout_a">スカウトメール・気になる通知に切替</a></li>
		<li id="mn_noobservationMail"><a href="${f:url('/observateApplication/list/')}" title="質問メールに切替">質問メールに切替</a></li>
		<li id="mn_noarbeitMail"><a href="${f:url('/arbeit/list/')}" title="グルメdeバイトメールに切替">グルメdeバイトメールに切替</a></li>
	</c:when>
	<c:otherwise>
		<li id="mn_memberList"><a href="${f:url(listPath)}" title="求職者一覧に切替">求職者一覧に切替</a></li>
		<li id="mn_noScoutMail"><a href="${f:url('/scoutMail/list/')}" title="スカウトメール・気になる通知に切替" class="scout_a">スカウトメール・気になる通知に切替</a></li>
		<li id="mn_noKeepBox"><a href="${f:url(keepBoxPath)}" title="キープBOXに切替">キープBOXに切替</a></li>
	</c:otherwise>
</c:choose>

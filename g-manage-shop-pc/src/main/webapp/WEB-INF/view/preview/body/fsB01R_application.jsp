<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<c:set var="OBSERVATION_KBN_QUESTION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION%>" scope="page" />
<c:set var="OBSERVATION_KBN_QUESTION_OBSERVATION" value="<%=MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION%>" scope="page" />

<c:set var="telNo" value="${gf:findTelFirstElement(f:h(phoneReceptionist),true)}" scope="page" />

<%-- 応募の部分のJSP --%>
<c:if test="${applicationButtonBlock}">
	<!-- // この求人への応募はコチラから -->
	<div class="recruitBox">
		<c:if test="${applicationButtonBlock}">
			<p>この求人への応募はコチラから</p>
		</c:if>
		<c:if test="${applicationOkFlg}">
			<a href="#" class="webBtn">webから応募する</a>
		</c:if>

		<c:if test="${not empty telNo}">
				<a href="#" class="webBtn">電話から応募する</a>
		</c:if>

		<c:choose>
			<c:when test="${observationKbn eq OBSERVATION_KBN_QUESTION}">
				<a href="#" class="shopBtn">ご質問</a>
			</c:when>
			<c:when test="${observationKbn eq OBSERVATION_KBN_QUESTION_OBSERVATION}">
				<a href="#" class="shopBtn">ご質問&nbsp;or&nbsp;店舗見学</a>
			</c:when>
		</c:choose>

		<a href="#" class="webBtn">検討中BOXに追加する</a>
		<span class="telTxt">電話応募の場合は電話番号をタップしてください。</span>
	</div>
</c:if>
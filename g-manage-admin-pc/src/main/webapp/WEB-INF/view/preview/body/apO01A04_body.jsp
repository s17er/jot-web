<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>

<c:set var="APPLICATION_FORM_KBN_EXIST" value="<%=MTypeConstants.ApplicationFormKbn.EXIST %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" />

<%-- パスを生成 --%>
		<c:choose>
		<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
			<c:set var="DETAIL_PATH" value="${gf:makePathConcat2Arg('/detailPreview/detail/displayDetail', id, inputFormKbn)}" scope="page" />
		</c:when>
		<c:otherwise>
			<c:set var="DETAIL_PATH" value="${gf:makePathConcat1Arg('/detailPreview/dbDetail/displayDetail', id)}" scope="page" />
		</c:otherwise>
		</c:choose>

<html:errors />

<% //データが取得できなければ表示しない %>
<c:if test="${existDataFlg eq true}">
	<div id="main">

		<%-- 詳細ページ共通ヘッダを差し込む --%>
		<%@include file="/WEB-INF/view/preview/body/apO01_header.jsp" %>
		<hr />

		<!-- 動画start -->
		<div id="movie">
			<iframe width="420" height="315" src="${movieUrl}?rel=0&showinfo=0" frameborder="0" allowfullscreen></iframe>
			<a class="btn" href="${movieUrl}" data-lity="data-lity"> <span class="movie_btn"><img src="${f:h(frontHttpUrl)}${f:h(imgLocation)}/cmn/movie_icon.png" alt="再生アイコン"></span></a>
			<c:if test="${not empty movieComment}">
				<div class="movie_area">
					<p class="movie_logo_img">
						<img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/movie_logo.png" alt="動画で分かるこのお店・会社の魅力" />
					</p>
					<p class="movie_text">
						${f:br(movieComment)}
					</p>
				</div>
			</c:if>
		</div>
		<div class="button-bottom">
			<a href="${f:url(DETAIL_PATH)}"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/search/btn_displayDetail.gif" alt="詳細へ戻る" /></a>
		</div>
		<!-- 動画end -->
	</div>
</c:if>

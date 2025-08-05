<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm"%>
<c:set var="ENUM_IMG_METHOD_KBN_DB" value="<%=ListForm.ImgMethodKbn.IMG_FROM_DB %>" scope="page" />
<c:set var="ENUM_IMG_METHOD_KBN_SESSION" value="<%=ListForm.ImgMethodKbn.IMG_FROM_SESSION %>" scope="page"/>
	<!-- // 戻るボタン -->
	<!-- // シャドウバー -->
	<div class="sBar"></div>
		<!-- // トップへ戻る↑ -->
		<div class="clearfix">
			<div id="goTop"><a href="#top">ページトップへ↑</a></div>
			<!-- // ←戻る -->
		<div id="goBack">
			<c:choose>
				<c:when test="${imgMethodKbn eq ENUM_IMG_METHOD_KBN_SESSION }">
					<a href="${f:url(gf:makePathConcat1Arg('/listPreview/list/showSmartPhoneInputPreview', inputFormKbn))}">←戻る</a>
				</c:when>
				<c:otherwise>
					<a href="${f:url(gf:makePathConcat1Arg('/listPreview/list/showSmartPhoneListPreview', id))}">←戻る</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

<!-- // フッター -->
<!-- // フッター -->
<footer>
<a href="#">ホーム</a>｜<a href="#">掲載をお考えの企業様へ</a>｜<a href="#">ヘルプ</a><br>
<a href="#">このサイトを友達に教える</a>｜<a href="#" >本誌購読はコチラ</a><br>
<a href="#" >関西版</a>｜<a href="#" >東海版</a>
<p>表示モード ： スマートフォン | <a href="#" >PC</a></p>
<!-- // 会社アドレス -->
<address>
Copyright 2013 (C) J office Tokyo Co.,Ltd.
</address>
</footer>

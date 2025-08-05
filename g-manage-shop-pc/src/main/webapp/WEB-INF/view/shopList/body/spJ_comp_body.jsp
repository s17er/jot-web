<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="LABEL_SHOPLIST" value="<%=LabelConstants.ShopList.SHOP_LIST %>" scope="page" />
<!-- #main# -->
<div id="main">
	<div id="wrap_web-shoplist">
		<h2>${f:h(pageTitle)}</h2>
		<div class="menu_tab">
			<div class="menu_list">
				<ul>
					<li>
						<a href="${f:url('/webdata/list/')}">求人原稿</a>
					</li>
					<li class="tab_active">
						<a href="${f:url('/shopList/')}">店舗一覧</a>
					</li>
				</ul>
			</div>
		</div>
		<div id="wrap_masc_content">
			<div class="tab_area">
				<div class="tab_contents tab_active">
					<div class="detail_area">
						<p id="txt_comp">${f:h(defaultMsg)}</p>
					</div>
					<div class="wrap_btn">
						<c:choose>
							<c:when test="${pageKbn eq PAGE_INPUT}">
								<input type="button" name="submit" value="店舗一覧へ戻る" id="btn_regist" onclick="location.href='${f:url('/shopList/index/')}';">
							</c:when>
							<c:when test="${pageKbn eq PAGE_EDIT}">
								<input type="button" name="submit" value="検索結果へ戻る" id="btn_regist" onclick="location.href='${f:url('/shopList/list/reShowList')}';">
							</c:when>
							<c:when test="${pageKbn eq PAGE_DELETE}">
								<input type="button" name="submit" value="検索結果へ戻る" id="btn_regist" onclick="location.href='${f:url('/shopList/list/reShowList')}';">
							</c:when>
							<c:otherwise>
								<input type="button" name="submit" value="店舗一覧へ戻る" id="btn_regist" onclick="location.href='${f:url('/shopList/index/')}';">
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- #main# -->
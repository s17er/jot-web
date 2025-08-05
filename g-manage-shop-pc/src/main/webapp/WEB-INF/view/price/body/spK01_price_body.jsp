<%@page import="com.gourmetcaree.shop.pc.sys.util.ShopPcUtil"%>
<c:set var="includeValue" value="<%=ShopPcUtil.readPriceIncludeFile() %>" scope="page" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/other.css" />
<div id="main">

	<div id="wrap_othe">
		<h2>オプション料金</h2>
		<p class="explanation">
			オプションを追加する場合は「営業担当」または<br class="pc_none" />「<a href="${f:url('/contact/')}"
				class="contact_color">お問い合わせ</a>」よりお申し付けください。
			<br />※エリアにより料金･サービスが異なります。ご注意ください。
		</p>
		<div id="wrap_pric_content">
			${includeValue}
		</div>
	</div>

</div>
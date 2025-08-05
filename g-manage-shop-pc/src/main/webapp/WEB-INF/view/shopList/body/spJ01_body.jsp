<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />
<div id="main">
<div id="wrap_web-shoplist">
	<h2>${f:h(pageTitle)}</h2>
	<p class="explanation">
		ご登録いただいた店舗情報は<img class="spo_img" src="${f:h(SHOP_CONTENS)}/images/sponsor1.svg" width="72" height="20" alt="Indeed"><img class="spo_img" src="${f:h(SHOP_CONTENS)}/images/sponsor2.svg" width="102" height="20" alt="求人ボックス"><img class="spo_img" src="${f:h(SHOP_CONTENS)}/images/sponsor3.svg" width="100" height="20" alt="スタンバイ">などに反映されます。<br>また各画像には画像サイズの指定がございますので、ご注意ください。
	</p>
	<div class="menu_tab">
		<div class="menu_list"><ul>
			<li>
				<a href="${f:url('/webdata/list/')}">求人原稿</a>
			</li>
			<li class="tab_active">
				<a href="${f:url('/shopList/')}">店舗一覧</a>
			</li>
		</ul></div>
	</div>

	<div id="wrap_masc_content">
		<div class="tab_area">
			<!-- 応募メール -->
			<div class="tab_contents tab_active">
				<div class="shop_list">
					<ul>
						<li class="registration">
						<a href="${f:url('/shopList/input')}">
							<div class="shop_icon"><p></p></div>
							<div class="shop_txt">
								<h3>店舗登録</h3>
								<p>新たに店舗を追加する場合はこちらからご登録いただけます。</p></div>
						</a>
						</li>
						<li class="edit">
						<a href="${f:url('/shopList/list')}">
							<div class="shop_icon"><p></p></div>
							<div class="shop_txt">
								<h3>店舗管理</h3>
								<p>店舗情報の編集･削除や一時的に非表示することができます。CSVインポート･エクスポートが行えます。</p></div>
						</a>
						</li>
						<li class="img_mgt">
						<a href="${f:url('/customerImage/list/index')}">
							<div class="shop_icon"><p></p></div>
							<div class="shop_txt">
								<h3>画像管理</h3>
								<p>画像のアップロード･削除を行い、「店舗管理」で画像の変更できます。</p></div>
						</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
</div>

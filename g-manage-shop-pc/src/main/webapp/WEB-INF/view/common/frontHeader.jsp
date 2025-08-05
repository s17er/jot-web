<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<c:set var="AREA_CD_SHUTOKEN" value="<%=MAreaConstants.AreaCd.SHUTOKEN_AREA%>" />
<div id="header" class="clear">

	<c:choose>
		<c:when test="${not empty h1Text}">
			<h1>${h1Text}</h1>
		</c:when>
		<c:when test="${areaCd eq AREA_CD_SHUTOKEN}">
			<h1>飲食求人・飲食店就職はグルメキャリー</h1>
		</c:when>
		<c:otherwise>
			<h1>飲食求人・飲食店就職はグルメキャリー</h1>
		</c:otherwise>
	</c:choose>

	<div id="head_left" class="clear">
		<p>
			<span>飲食・レストラン専門求人情報&nbsp;グルメキャリー首都圏版</span><br />専門サイトが厳選した“働きたい飲食店”を掲載！webだけの求人情報・飲食店アルバイト 飲食バイトも！<br /> <span style="color: #FF0000">毎週木曜日求人情報更新中！</span>
		</p>
		<h2 title="飲食求人 グルメキャリー首都圏版">
			<a href="#"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/logo.gif" alt="飲食求人 グルメキャリー首都圏版" /></a>
		</h2>
	</div>
	<div id="head_right">
		<ul id="mn_site">
			<li><a href="#"  title="仙台の飲食求人"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_sendai.gif" alt="仙台版の飲食求人" /></a></li>
			<li><a href="#" title="東海・北陸の飲食求人"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_tokai.gif" alt="東海・北陸版の飲食求人" /></a></li>
			<li><a href="#"  title="関西の飲食求人"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_kansai.gif" alt="関西版の飲食求人" /></a></li>
			<li><a href="#" title="九州・沖縄の飲食求人"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_kyusyu.gif" alt="九州・沖縄版の飲食求人" /></a></li>
			<li><a href="#"  title="就活生のための飲食業界研究・企業紹介エフラボ2019"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/btn_shinsotsu.gif" alt="就活生のための飲食業界研究・企業紹介エフラボ2019" /></a></li>
		</ul>
		<ul id="mn_sub">
			<li><a href="#" title="サイトマップ">サイトマップ</a>|</li>
			<li><a href="#">初めての方へ</a>|</li>
			<li><a href="#" title="全路線・駅一覧">全路線・駅一覧</a></li>
		</ul>
	</div>

</div>

<div id="navigation" class="clear">
	<nav>
		<ul class="mega_menu">
			<li class="search_menu mypage_btn"><a href="#"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi01_off.gif" alt="MYページ" id="navi01_off" onMouseOver="MM_swapImage('navi01_off','','${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi01_on.gif',1)" onMouseOut="MM_swapImgRestore()" /></a></li>
			<li class="menu__mega search_menu"><a href="#" class="init-bottom"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi02_off.gif" alt="業態から探す" id="navi02_off" onMouseOver="MM_swapImage('navi02_off','','${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi02_on.gif',1)" onMouseOut="MM_swapImgRestore()" /></a>
				<ul class="menu__second-level industry">
					<li class="triangle01"></li>
					<li class="triangle01_b"></li>
					<li class="search_menu_list01"><a href="#">和食</a></li>
					<li class="search_menu_list01"><a href="#">日本料理・懐石</a></li>
					<li class="search_menu_list01"><a href="#">居酒屋</a></li>
					<li class="search_menu_list01"><a href="#">魚料理</a></li>
					<li class="search_menu_list01"><a href="#">うどん・蕎麦</a></li>
					<li class="search_menu_list01"><a href="#">寿司</a></li>
					<li class="search_menu_list01"><a href="#">洋食・西洋料理</a></li>
					<li class="search_menu_list01"><a href="#">中華料理</a></li>
					<li class="search_menu_list01"><a href="#">フランス料理</a></li>
					<li class="search_menu_list01"><a href="#">ビストロ</a></li>
					<li class="search_menu_list01"><a href="#">イタリアン</a></li>
					<li class="search_menu_list01"><a href="#">ピッツェリア</a></li>
					<li class="search_menu_list01"><a href="#">バール</a></li>
					<li class="search_menu_list01"><a href="#">スペイン料理</a></li>
					<li class="search_menu_list01"><a href="#">和洋創作料理</a></li>
					<li class="search_menu_list01"><a href="#">ダイニング</a></li>
					<li class="search_menu_list01"><a href="#">韓国料理</a></li>
					<li class="search_menu_list01"><a href="#">アジアン・エスニック</a></li>
					<li class="search_menu_list01"><a href="#">BAR・レストランバー</a></li>
					<li class="search_menu_list01"><a href="#">専門店(各国料理）</a></li>
					<li class="search_menu_list01"><a href="#">パティスリー・製菓</a></li>
					<li class="search_menu_list01"><a href="#">ベーカリー・製パン</a></li>
					<li class="search_menu_list01"><a href="#">カフェ・喫茶</a></li>
					<li class="search_menu_list01"><a href="#">ホテル・旅館</a></li>
					<li class="search_menu_list01"><a href="#">焼肉・肉料理</a></li>
					<li class="search_menu_list01"><a href="#">ステーキ・鉄板焼</a></li>
					<li class="search_menu_list01"><a href="#">炭火焼・グリル</a></li>
					<li class="search_menu_list01"><a href="#">焼鳥・鳥料理</a></li>
					<li class="search_menu_list01"><a href="#">鍋料理</a></li>
					<li class="search_menu_list01"><a href="#">串揚げ・串カツ</a></li>
					<li class="search_menu_list01"><a href="#">とんかつ</a></li>
					<li class="search_menu_list01"><a href="#">ラーメン</a></li>
					<li class="search_menu_list01"><a href="#">天ぷら</a></li>
					<li class="search_menu_list01"><a href="#">うなぎ</a></li>
					<li class="search_menu_list01"><a href="#">ウエディング</a></li>
					<li class="search_menu_list01"><a href="#">お好み焼き・たこ焼き</a></li>
					<li class="search_menu_list01"><a href="#">定食・丼</a></li>
					<li class="search_menu_list01"><a href="#">カレー</a></li>
					<li class="search_menu_list01"><a href="#">郷土料理</a></li>
					<li class="search_menu_list01"><a href="#">野菜・オーガニック</a></li>
					<li class="search_menu_list01"><a href="#">ファミリーレストラン</a></li>
					<li class="search_menu_list01"><a href="#">チェーンレストラン</a></li>
					<li class="search_menu_list01"><a href="#">ファストフード</a></li>
					<li class="search_menu_list01"><a href="#">FC</a></li>
					<li class="search_menu_list01"><a href="#">ケータリング</a></li>
					<li class="search_menu_list01"><a href="#">惣菜・DELI・仕出し・弁当</a></li>
					<li class="search_menu_list01"><a href="#">給食・社員食堂・介護・病院</a></li>
					<li class="search_menu_list01"><a href="#">スーパー・小売店</a></li>
					<li class="search_menu_list01"><a href="#">精肉・鮮魚</a></li>
					<li class="search_menu_list01"><a href="#">デリバリー</a></li>
					<li class="search_menu_list01"><a href="#">人材紹介・派遣</a></li>
					<li class="search_menu_list01"><a href="#">スクール</a></li>
				</ul></li>
			<li class="menu__mega search_menu"><a href="#" class="init-bottom"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi03_off.gif" alt="職種から探す" id="navi03_off" onMouseOver="MM_swapImage('navi03_off','','${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi03_on.gif',1)" onMouseOut="MM_swapImgRestore()" /></a>
				<ul class="menu__second-level job">
					<li class="triangle02"></li>
					<li class="triangle02_b"></li>
					<li class="search_menu_list02_short"><a href="#">料理長候補</a></li>
					<li class="search_menu_list02_short"><a href="#">調理スタッフ</a></li>
					<li class="search_menu_list02"><a href="#">寿司職人</a></li>
					<li class="search_menu_list02"><a href="#">調理補助</a></li>
					<li class="search_menu_list02_short"><a href="#">ブランジェ・製パン</a></li>
					<li class="search_menu_list02_short"><a href="#">パティシエ・製菓</a></li>
					<li class="search_menu_list02"><a href="#">ピッツァイオーロ</a></li>
					<li class="search_menu_list02"><a href="#">蕎麦・うどん職人</a></li>
					<li class="search_menu_list02_short"><a href="#">焼鳥調理</a></li>
					<li class="search_menu_list02_short"><a href="#">鉄板調理</a></li>
					<li class="search_menu_list02"><a href="#">生産加工（鮮魚・精肉etc.）・製造</a></li>
					<li class="search_menu_list02"><a href="#">店長候補・マネージャー候補・支配人候補</a></li>
					<li class="search_menu_list02_short"><a href="#">ホールサービス</a></li>
					<li class="search_menu_list02_short"><a href="#">ソムリエ・ソムリエール</a></li>
					<li class="search_menu_list02"><a href="#">女将・和装ホール</a></li>
					<li class="search_menu_list02"><a href="#">販売</a></li>
					<li class="search_menu_list02_short"><a href="#">バリスタ</a></li>
					<li class="search_menu_list02_short"><a href="#">バーテンダー</a></li>
					<li class="search_menu_list02"><a href="#">レセプショニスト</a></li>
					<li class="search_menu_list02"><a href="#">ウェディングプランナー</a></li>
					<li class="search_menu_list02_short"><a href="#">栄養士・管理栄養士</a></li>
					<li class="search_menu_list02_short"><a href="#">スクール講師</a></li>
					<li class="search_menu_list02"><a href="#">本部スタッフ（開発・総務・人事・営業etc.）</a></li>
					<li class="search_menu_list02"><a href="#">SV・エリアマネージャー</a></li>
					<li class="search_menu_list02_short"><a href="#">委託店長・経営者</a></li>
					<li class="search_menu_list02_short"><a href="#">その他</a></li>
				</ul></li>
			<li class="menu__mega search_menu"><a href="#" class="init-bottom"><img src="${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi04_off.gif" alt="場所で探す" id="navi04_off" onMouseOver="MM_swapImage('navi04_off','','${f:h(frontHttpUrl)}${f:h(imagesLocation)}/cmn/navi04_on.gif',1)" onMouseOut="MM_swapImgRestore()" /></a>
				<ul class="menu__second-level area">
					<li class="triangle03"></li>
					<li class="triangle03_b"></li>
					<li class="search_menu_list03"><a href="#">東京東部(千代田区・中央区・港区・江東区・江戸川区・葛飾区)</a></li>
					<li class="search_menu_list03"><a href="#">東京西部(新宿区・練馬区・中野区・杉並区・板橋区)</a></li>
					<li class="search_menu_list03"><a href="#">東京南部(渋谷区・品川区・大田区・目黒区・世田谷区)</a></li>
					<li class="search_menu_list03"><a href="#">東京北部(豊島区・文京区・台東区・荒川区・墨田区・足立区・北区)</a></li>
					<li class="search_menu_list03"><a href="#">東京都下(武蔵野・西東京・多摩・町田エリア その他東京都)</a></li>
					<li class="search_menu_list03"><a href="#">神奈川県(横浜エリア 川崎エリア 湘南エリア その他神奈川)</a></li>
					<li class="search_menu_list03"><a href="#">千葉県(千葉・船橋・津田沼エリア 柏・松戸エリア その他千葉県)</a></li>
					<li class="search_menu_list03"><a href="#">埼玉県(さいたま市エリア 川越・所沢エリア その他埼玉県)</a></li>
					<li class="search_menu_list03"><a href="#">IUターン・リゾート・その他</a></li>
					<li class="search_menu_list03"><a href="#">海外専用求人ページへ</a></li>
					<li class="areadetail_title">詳細エリア</li>
					<li class="s_m_list03">
						<ul>
							<li class="s_m_list03_d"><a href="#">丸の内・八重洲</a></li>
							<li class="s_m_list03_d"><a href="#">渋谷・恵比寿</a></li>
							<li class="s_m_list03_d"><a href="#">代官山・中目黒</a></li>
							<li class="s_m_list03_d"><a href="#">新宿・代々木</a></li>
							<li class="s_m_list03_d width_ad"><a href="#">原宿・表参道</a></li>
							<li class="s_m_list03_d"><a href="#">六本木・麻布十番・西麻布</a>
							<li class="s_m_list03_d"><a href="#">広尾・白金・目黒</a></li>
							<li class="s_m_list03_d"><a href="#">赤坂・永田町</a></li>
							<li class="s_m_list03_d"><a href="#">銀座・築地・有楽町</a></li>
							<li class="s_m_list03_d width_ad"><a href="#">下北沢･明大前</a></li>
							<li class="s_m_list03_d"><a href="#">二子玉川・成城学園</a></li>
							<li class="s_m_list03_d"><a href="#">新橋・浜松町・田町</a></li>
							<li class="s_m_list03_d"><a href="#">品川・蒲田・大井町</a></li>
							<li class="s_m_list03_d"><a href="#">自由が丘・武蔵小杉</a></li>
							<li class="s_m_list03_d width_ad"><a href="#">池袋・練馬・板橋</a></li>
							<li class="s_m_list03_d"><a href="#">四ツ谷・飯田橋</a></li>
							<li class="s_m_list03_d"><a href="#">神田・秋葉原</a></li>
							<li class="s_m_list03_d"><a href="#">日本橋</a></li>
							<li class="s_m_list03_d"><a href="#">上野・浅草・北千住</a></li>
							<li class="s_m_list03_d width_ad"><a href="#">お台場・豊洲・有明</a></li>
							<li class="s_m_list03_d"><a href="#">吉祥寺・高円寺・中野</a></li>
							<li class="s_m_list03_d"><a href="#">国分寺・立川・八王子</a></li>
							<li class="s_m_list03_d"><a href="#">府中・調布</a></li>
						</ul>
					</li>
					<li class="s_m_list03">
						<ul>
							<li class="s_m_list03_d"><a href="#">町田・相模原・大和</a></li>
							<li class="s_m_list03_d"><a href="#">横浜（西区・中区・神奈川区）</a></li>
							<li class="s_m_list03_d"><a href="#">横浜市北部</a></li>
							<li class="s_m_list03_d"><a href="#">横浜市南部</a></li>
							<li class="s_m_list03_d width_ad"><a href="#">鎌倉･逗子・横須賀</a></li>
							<li class="s_m_list03_d"><a href="#">藤沢・茅ヶ崎・湘南</a></li>
							<li class="s_m_list03_d"><a href="#">川崎</a></li>
							<li class="s_m_list03_d"><a href="#">小田原・箱根</a></li>
						</ul>
					</li>
					<li class="s_m_list03">
						<ul>
							<li class="s_m_list03_d"><a href="#">千葉・船橋</a></li>
							<li class="s_m_list03_d"><a href="#">浦安・舞浜</a></li>
							<li class="s_m_list03_d"><a href="#">柏・松戸</a></li>
						</ul>
					</li>
					<li class="s_m_list03">
						<ul>
							<li class="s_m_list03_d"><a href="#">越谷・草加・春日部</a></li>
							<li class="s_m_list03_d"><a href="#">所沢・川越</a></li>
							<li class="s_m_list03_d"><a href="#">大宮・浦和・さいたま市</a></li>
						</ul>
					</li>
				</ul></li>
			<li><p>求人更新日 XXXX/XX/XX  »  次回更新日 XXXX/XX/XX</p></li>
		</ul>
	</nav>
</div>

<!--グローバルメニュー-->
<script type="text/javascript" src="/js/fixbar.js"></script>
<!--pagetopボタン-->
<script type="text/javascript" src="/js/pagetop.js"></script>
<!--pagetopボタン-->
<p id="page-top">
	<a href="#" class="hvr-bob"></a>
</p>
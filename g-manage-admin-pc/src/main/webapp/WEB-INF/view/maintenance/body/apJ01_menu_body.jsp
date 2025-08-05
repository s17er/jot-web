<%@page pageEncoding="UTF-8"%>

<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<div class="top_menu" style="padding-bottom: 20px">
		<ul class="menu_list01 clear">
			<li>
				<h2>号数データ管理</h2>
				<p>号数データの登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/volume/input/')}" title="号数データ登録">号数データ登録</a></li>
					<li><a href="${f:url('/volume/list/')}" title="号数データ管理">号数データ管理</a></li>
				</ul>
			</li>
			<li>
				<h2>特集データ管理</h2>
				<p>特集データの登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/special/input/')}" title="特集データ登録">特集データ登録</a></li>
					<li><a href="${f:url('/special/list/')}" title="特集データ管理">特集データ管理</a></li>
				</ul>
			</li>
			<li>
				<h2>駅グループ管理</h2>
				<p>駅グループの登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/terminal/input/')}" title="駅グループ登録">駅グループ登録</a></li>
					<li><a href="${f:url('/terminal/list/')}" title="駅グループ管理">駅グループ管理</a></li>
				</ul>
			</li>
		</ul>
		<ul class="menu_list01 clear">
			<li>
				<h2 class="ico05">合同企業説明会管理</h2>
				<p>合同企業説明会、事前登録開催年の登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/advancedRegistration/input/')}" title="合同企業説明会登録">合同企業説明会登録</a></li>
					<li><a href="${f:url('/advancedRegistration/list/')}" title="合同企業説明会管理">合同企業説明会管理</a></li>
				</ul>
			</li>
			<li>
				<h2 class="ico04">営業担当者管理</h2>
				<p>営業担当者の登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/sales/input/')}" title="営業担当者登録">営業担当者登録</a></li>
					<li><a href="${f:url('/sales/list/')}" title="営業担当者管理">営業担当者管理</a></li>
				</ul>
			</li>
			<li>
				<h2 class="ico02">会社管理</h2>
				<p>会社の登録・表示・編集を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/company/input/')}" title="会社登録">会社登録</a></li>
					<li><a href="${f:url('/company/list/')}" title="会社管理">会社管理</a></li>
				</ul>
			</li>
		</ul>
		<ul class="menu_list01 clear">
			<li>
				<h2>タグ管理機能</h2>
				<p>系列店舗に紐づいたタグとWEBデータに紐づいたタグの編集を行うことが出来ます。</p>
				<ul class="btn">
					<li><a href="${f:url('/tagManage/edit/1')}" title="WEBデータのタグ管理">WEBデータのタグ管理</a></li>
					<li><a href="${f:url('/tagManage/edit/2')}" title="系列店舗のタグ管理">系列店舗のタグ管理</a></li>
				</ul>
			</li>
			<li>
				<h2>いたずら応募管理機能</h2>
				<p>通常応募・プレ応募の処理内でいたずら応募と判定する条件の管理を行うことが出来ます。</p>
				<ul class="btn">
					<li class="regist"><a href="${f:url('/mischief/input/')}" title="条件登録">条件登録</a></li>
					<li><a href="${f:url('/mischief/list/')}" title="条件管理">条件管理</a></li>
				</ul>
			</li>
		</ul>
	</div>
	<hr />


	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

</div>
<!-- #main# -->
